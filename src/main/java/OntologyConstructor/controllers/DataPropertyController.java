package OntologyConstructor.controllers;

import OntologyConstructor.OntologyProvider;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping({"/dataProperty"})
@RestController
public class DataPropertyController {

    @Autowired
    private OntologyProvider ontologyProvider;

    @CrossOrigin
    @GetMapping("/getAllProperties")
    public Map<IRI, String> getAllProperties() {
        Map<IRI, String> allPropertiesList = new HashMap<>();
        Set<OWLDataProperty> properties = ontologyProvider.getOntology().getDataPropertiesInSignature();
        for(OWLDataProperty property: properties){
            allPropertiesList.put(property.getIRI(),property.getIRI().getShortForm());
        }
        return allPropertiesList;
    }

    @CrossOrigin
    @GetMapping("/getXSDTypes")
    public Map<IRI, String> getXSDTypes() {
        Map<IRI, String> xsdTypesList = new HashMap<>();
        XSDVocabulary[] types = XSDVocabulary.values();
        for(XSDVocabulary type: types){
            xsdTypesList.put(type.getIRI(),type.getPrefixedName());
        }
        return xsdTypesList;
    }

    @CrossOrigin
    @PostMapping("/getPropertyDescription")
    public Map<String, List<String>> getPropertyDescription(@ModelAttribute MultipartFile file) throws IOException {

        String propertyIRI = new String(file.getBytes());

        Map<String, List<String>> propertyDescription = new HashMap<>();
        OWLDataProperty property = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(propertyIRI));

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyProvider.getOntology());

        List<String> domains = reasoner.getDataPropertyDomains(property, true).getFlattened()
                .stream().map(domain -> domain.getIRI().toString()).collect(Collectors.toList());
        propertyDescription.put("domains", domains);

        List<String> ranges = ontologyProvider.getOntology().getDataPropertyRangeAxioms(property)
                .stream().map(range -> range.getRange().toString()).collect(Collectors.toList());
        propertyDescription.put("ranges", ranges);

        List<String> equivalents = new ArrayList<>();
        ontologyProvider.getOntology().getEquivalentDataPropertiesAxioms(property)
                .forEach(equivalent -> equivalent.getPropertiesMinus(property)
                        .forEach(prop -> equivalents.add(prop.asOWLDataProperty().getIRI().toString())));
        propertyDescription.put("equivalents", equivalents);

        List<String> disjointProps = reasoner.getDisjointDataProperties(property).getFlattened()
                .stream().map(disjoint -> disjoint.getIRI().toString()).collect(Collectors.toList());
        disjointProps.remove("http://www.w3.org/2002/07/owl#topDataProperty");
        disjointProps.remove("http://www.w3.org/2002/07/owl#bottomDataProperty");
        propertyDescription.put("disjointProps", disjointProps);

        List<String> superProps = reasoner.getSuperDataProperties(property, true).getFlattened()
                .stream().map(superProp -> superProp.getIRI().toString()).collect(Collectors.toList());
        superProps.remove("http://www.w3.org/2002/07/owl#topDataProperty");
        propertyDescription.put("superProps", superProps);

        List<String> subProps = reasoner.getSubDataProperties(property, true).getFlattened()
                .stream().map(subProp -> subProp.getIRI().toString()).collect(Collectors.toList());
        subProps.remove("http://www.w3.org/2002/07/owl#bottomDataProperty");
        propertyDescription.put("subProps", subProps);

        List<String> isFunctional = Collections.singletonList(String.valueOf(ontologyProvider.getOntology().getFunctionalDataPropertyAxioms(property).size()));
        propertyDescription.put("isFunctional", isFunctional);

        return propertyDescription;
    }

    @CrossOrigin
    @PostMapping("/addNewProperty")
    public Map<String, String> addNewProperty(@ModelAttribute MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();

        String propertyIRI = new String(file.getBytes());
        OWLDataProperty property = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(propertyIRI));

        OWLDeclarationAxiom declarationAxiom = ontologyProvider.getDataFactory().getOWLDeclarationAxiom(property);
        ontologyProvider.getManager().addAxiom(ontologyProvider.getOntology(), declarationAxiom);

        map.put("propertyIRI", property.getIRI().toString());
        map.put("propertyName", property.getIRI().getShortForm());
        return map;
    }

    @CrossOrigin
    @PostMapping("/editIRIProperty")
    public Map<String, String> editIRIProperty(@ModelAttribute MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();

        String iri = new String(file.getBytes());
        String oldIRI = iri.substring(0,iri.indexOf(";"));
        String newIRI = iri.substring(iri.indexOf(";")+1);

        OWLDataProperty property = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(oldIRI));

        OWLEntityRenamer entityRenamer = new OWLEntityRenamer(ontologyProvider.getManager(),Collections.singleton(ontologyProvider.getOntology()));
        ontologyProvider.getManager().applyChanges(entityRenamer.changeIRI(property,IRI.create(newIRI)));

        property = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(newIRI));

        map.put("propertyIRI", property.getIRI().toString());
        map.put("propertyName", property.getIRI().getShortForm());

        return map;
    }

    @CrossOrigin
    @PostMapping("/deleteProperty")
    public Boolean deleteProperty(@ModelAttribute MultipartFile file) throws IOException {

        String propertyIRI = new String(file.getBytes());
        OWLDataProperty property = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(propertyIRI));

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontologyProvider.getOntology()));
        property.accept(remover);
        return ontologyProvider.getManager().applyChanges(remover.getChanges()) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/addAxiomProperty")
    public Boolean addAxiomProperty(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes());
        String propertyIRI = data.substring(0,data.indexOf(";"));
        String objectIRI = data.substring(data.indexOf(";")+1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!")+1);

        OWLDataProperty property = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(propertyIRI));
        OWLDataProperty property1 = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(objectIRI));
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(objectIRI));
        OWLDataRange range = ontologyProvider.getDataFactory().getOWLDatatype(IRI.create(objectIRI));
        OWLAxiom axiom = null;

        switch (type){
            case "domain":
                axiom = ontologyProvider.getDataFactory().getOWLDataPropertyDomainAxiom(property, owlClass);
                break;
            case "range":
                axiom = ontologyProvider.getDataFactory().getOWLDataPropertyRangeAxiom(property, range);
                break;
            case "equivalent":
                axiom = ontologyProvider.getDataFactory().getOWLEquivalentDataPropertiesAxiom(property, property1);
                break;
            case "disjoint":
                axiom = ontologyProvider.getDataFactory().getOWLDisjointDataPropertiesAxiom(property, property1);
                break;
            case "subProp":
                axiom = ontologyProvider.getDataFactory().getOWLSubDataPropertyOfAxiom(property1, property);
                break;
            case "superProp":
                axiom = ontologyProvider.getDataFactory().getOWLSubDataPropertyOfAxiom(property, property1);
                break;
        }

        AddAxiom moveAxiom = new AddAxiom (ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/deleteAxiomProperty")
    public Boolean deleteAxiomProperty(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes());
        String propertyIRI = data.substring(0,data.indexOf(";"));
        String objectIRI = data.substring(data.indexOf(";")+1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!")+1);

        OWLDataProperty property = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(propertyIRI));
        OWLDataProperty property1 = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(objectIRI));
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(objectIRI));
        OWLDataRange range = null;
        if(type.equals("range")){
            range = ontologyProvider.getDataFactory().getOWLDatatype(XSDVocabulary.parseShortName(objectIRI).getIRI());
        }
        OWLAxiom axiom = null;

        switch (type){
            case "domain":
                axiom = ontologyProvider.getDataFactory().getOWLDataPropertyDomainAxiom(property, owlClass);
                break;
            case "range":
                axiom = ontologyProvider.getDataFactory().getOWLDataPropertyRangeAxiom(property, range);
                break;
            case "equivalent":
                axiom = ontologyProvider.getDataFactory().getOWLEquivalentDataPropertiesAxiom(property, property1);
                break;
            case "disjoint":
                axiom = ontologyProvider.getDataFactory().getOWLDisjointDataPropertiesAxiom(property, property1);
                break;
            case "subProp":
                axiom = ontologyProvider.getDataFactory().getOWLSubDataPropertyOfAxiom(property1, property);
                break;
            case "superProp":
                axiom = ontologyProvider.getDataFactory().getOWLSubDataPropertyOfAxiom(property, property1);
                break;
        }

        RemoveAxiom moveAxiom = new RemoveAxiom (ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/changeCharacter")
    public Boolean changeCharacter(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes());
        String propertyIRI = data.substring(0,data.indexOf(";"));
        String value = data.substring(data.indexOf(";")+1);

        OWLDataProperty property = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(propertyIRI));
        OWLAxiom axiom = ontologyProvider.getDataFactory().getOWLFunctionalDataPropertyAxiom(property);

        System.out.println(data);
        System.out.println(value);

        if(value.equals("true")){
            AddAxiom moveAxiom = new AddAxiom (ontologyProvider.getOntology(), axiom);
            return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
        }
        else {
            RemoveAxiom moveAxiom = new RemoveAxiom (ontologyProvider.getOntology(), axiom);
            return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
        }
    }
}
