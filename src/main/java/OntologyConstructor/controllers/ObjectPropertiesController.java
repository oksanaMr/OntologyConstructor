package OntologyConstructor.controllers;

import OntologyConstructor.ontology.OntologyProvider;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping({"/objectProperty"})
@RestController
public class ObjectPropertiesController {

    @Autowired
    private OntologyProvider ontologyProvider;

    @CrossOrigin
    @GetMapping("/getAllProperties")
    public Map<IRI, String> getAllProperties() {
        Map<IRI, String> allPropertiesList = new HashMap<>();
        Set<OWLObjectProperty> properties = ontologyProvider.getOntology().getObjectPropertiesInSignature();
        for(OWLObjectProperty property: properties){
            allPropertiesList.put(property.getIRI(),property.getIRI().getShortForm());
        }
        return allPropertiesList;
    }

    @CrossOrigin
    @PostMapping("/getPropertyDescription")
    public Map<String, List<String>> getPropertyDescription(@ModelAttribute MultipartFile file) throws IOException {

        String propertyIRI = new String(file.getBytes(), StandardCharsets.UTF_8);

        Map<String, List<String>> propertyDescription = new HashMap<>();
        OWLObjectProperty property = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(propertyIRI));

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyProvider.getOntology());

        List<String> domains = reasoner.getObjectPropertyDomains(property, true).getFlattened()
                .stream().map(domain -> domain.getIRI().toString()).collect(Collectors.toList());;
        propertyDescription.put("domains", domains);

        List<String> ranges = reasoner.getObjectPropertyRanges(property, true).getFlattened()
                .stream().map(range -> range.getIRI().toString()).collect(Collectors.toList());
        propertyDescription.put("ranges", ranges);

        List<String> equivalents = new ArrayList<>();
        ontologyProvider.getOntology().getEquivalentObjectPropertiesAxioms(property)
                .forEach(equivalent -> equivalent.getPropertiesMinus(property)
                        .forEach(prop -> equivalents.add(prop.getNamedProperty().getIRI().toString())));
        propertyDescription.put("equivalents", equivalents);

        List<String> inverseProps = new ArrayList<>();
        ontologyProvider.getOntology().getInverseObjectPropertyAxioms(property)
                .forEach(inverse -> inverse.getPropertiesMinus(property)
                        .forEach(prop -> inverseProps.add(prop.getNamedProperty().getIRI().toString())));
        propertyDescription.put("inverseProps", inverseProps);

        List<String> disjointProps = new ArrayList<>();
        ontologyProvider.getOntology().getDisjointObjectPropertiesAxioms(property)
                .forEach(disjoint -> disjoint.getPropertiesMinus(property)
                        .forEach(prop -> disjointProps.add(prop.getNamedProperty().getIRI().toString())));
        propertyDescription.put("disjointProps", disjointProps);

        List<String> superProps = reasoner.getSuperObjectProperties(property, true).getFlattened()
                .stream().map(superProp -> superProp.getNamedProperty().getIRI().toString()).collect(Collectors.toList());
        superProps.remove("http://www.w3.org/2002/07/owl#topObjectProperty");
        propertyDescription.put("superProps", superProps);

        List<String> subProps = reasoner.getSubObjectProperties(property, true).getFlattened()
                .stream().map(subProp -> subProp.getNamedProperty().getIRI().toString()).collect(Collectors.toList());
        subProps.remove("http://www.w3.org/2002/07/owl#bottomObjectProperty");
        propertyDescription.put("subProps", subProps);

        List<String> isFunctional = Collections.singletonList(String.valueOf(ontologyProvider.getOntology().getFunctionalObjectPropertyAxioms(property).size()));
        propertyDescription.put("isFunctional", isFunctional);

        List<String> isInverseFunctional = Collections.singletonList(String.valueOf(ontologyProvider.getOntology().getInverseFunctionalObjectPropertyAxioms(property).size()));
        propertyDescription.put("isInverseFunctional", isInverseFunctional);

        List<String> isTransitive = Collections.singletonList(String.valueOf(ontologyProvider.getOntology().getTransitiveObjectPropertyAxioms(property).size()));
        propertyDescription.put("isTransitive", isTransitive);

        List<String> isSymmetric = Collections.singletonList(String.valueOf(ontologyProvider.getOntology().getSymmetricObjectPropertyAxioms(property).size()));
        propertyDescription.put("isSymmetric", isSymmetric);

        List<String> isAsymmetric = Collections.singletonList(String.valueOf(ontologyProvider.getOntology().getAsymmetricObjectPropertyAxioms(property).size()));
        propertyDescription.put("isAsymmetric", isAsymmetric);

        List<String> isReflexive = Collections.singletonList(String.valueOf(ontologyProvider.getOntology().getReflexiveObjectPropertyAxioms(property).size()));
        propertyDescription.put("isReflexive", isReflexive);

        List<String> isIrreflexive = Collections.singletonList(String.valueOf(ontologyProvider.getOntology().getIrreflexiveObjectPropertyAxioms(property).size()));
        propertyDescription.put("isIrreflexive", isIrreflexive);

        return propertyDescription;
    }

    @CrossOrigin
    @PostMapping("/addNewProperty")
    public Map<String, String> addNewProperty(@ModelAttribute MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();

        String propertyIRI = new String(file.getBytes(), StandardCharsets.UTF_8);
        OWLObjectProperty property = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(propertyIRI));

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

        String iri = new String(file.getBytes(), StandardCharsets.UTF_8);
        String oldIRI = iri.substring(0,iri.indexOf(";"));
        String newIRI = iri.substring(iri.indexOf(";")+1);

        OWLObjectProperty property = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(oldIRI));

        OWLEntityRenamer entityRenamer = new OWLEntityRenamer(ontologyProvider.getManager(),Collections.singleton(ontologyProvider.getOntology()));
        ontologyProvider.getManager().applyChanges(entityRenamer.changeIRI(property,IRI.create(newIRI)));

        property = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(newIRI));

        map.put("propertyIRI", property.getIRI().toString());
        map.put("propertyName", property.getIRI().getShortForm());

        return map;
    }

    @CrossOrigin
    @PostMapping("/deleteProperty")
    public Boolean deleteProperty(@ModelAttribute MultipartFile file) throws IOException {

        String propertyIRI = new String(file.getBytes(), StandardCharsets.UTF_8);
        OWLObjectProperty property = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(propertyIRI));

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontologyProvider.getOntology()));
        property.accept(remover);
        return ontologyProvider.getManager().applyChanges(remover.getChanges()) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/addAxiomProperty")
    public Boolean addAxiomProperty(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        String propertyIRI = data.substring(0,data.indexOf(";"));
        String objectIRI = data.substring(data.indexOf(";")+1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!")+1);

        OWLObjectProperty property = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(propertyIRI));
        OWLObjectProperty property1 = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(objectIRI));
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(objectIRI));
        OWLAxiom axiom = null;

        switch (type){
            case "domain":
                axiom = ontologyProvider.getDataFactory().getOWLObjectPropertyDomainAxiom(property, owlClass);
                break;
            case "range":
                axiom = ontologyProvider.getDataFactory().getOWLObjectPropertyRangeAxiom(property, owlClass);
                break;
            case "inverse":
                axiom = ontologyProvider.getDataFactory().getOWLInverseObjectPropertiesAxiom(property, property1);
                break;
            case "equivalent":
                axiom = ontologyProvider.getDataFactory().getOWLEquivalentObjectPropertiesAxiom(property, property1);
                break;
            case "disjoint":
                axiom = ontologyProvider.getDataFactory().getOWLDisjointObjectPropertiesAxiom(property, property1);
                break;
            case "subProp":
                axiom = ontologyProvider.getDataFactory().getOWLSubObjectPropertyOfAxiom(property1, property);
                break;
            case "superProp":
                axiom = ontologyProvider.getDataFactory().getOWLSubObjectPropertyOfAxiom(property, property1);
                break;
        }

        AddAxiom moveAxiom = new AddAxiom (ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/deleteAxiomProperty")
    public Boolean deleteAxiomProperty(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        String propertyIRI = data.substring(0,data.indexOf(";"));
        String objectIRI = data.substring(data.indexOf(";")+1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!")+1);

        OWLObjectProperty property = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(propertyIRI));
        OWLObjectProperty property1 = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(objectIRI));
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(objectIRI));
        OWLAxiom axiom = null;

        switch (type){
            case "domain":
                axiom = ontologyProvider.getDataFactory().getOWLObjectPropertyDomainAxiom(property, owlClass);
                break;
            case "range":
                axiom = ontologyProvider.getDataFactory().getOWLObjectPropertyRangeAxiom(property, owlClass);
                break;
            case "inverse":
                axiom = ontologyProvider.getDataFactory().getOWLInverseObjectPropertiesAxiom(property, property1);
                break;
            case "equivalent":
                axiom = ontologyProvider.getDataFactory().getOWLEquivalentObjectPropertiesAxiom(property, property1);
                break;
            case "disjoint":
                axiom = ontologyProvider.getDataFactory().getOWLDisjointObjectPropertiesAxiom(property, property1);
                break;
            case "subProp":
                axiom = ontologyProvider.getDataFactory().getOWLSubObjectPropertyOfAxiom(property1, property);
                break;
            case "superProp":
                axiom = ontologyProvider.getDataFactory().getOWLSubObjectPropertyOfAxiom(property, property1);
                break;
        }

        RemoveAxiom moveAxiom = new RemoveAxiom (ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/changeCharacter")
    public Boolean changeCharacter(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        String propertyIRI = data.substring(0,data.indexOf(";"));
        String value = data.substring(data.indexOf(";")+1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!")+1);

        OWLObjectProperty property = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(propertyIRI));
        OWLAxiom axiom = null;

        switch (type){
            case "functional":
                axiom = ontologyProvider.getDataFactory().getOWLFunctionalObjectPropertyAxiom(property);
                break;
            case "inverseFunctional":
                axiom = ontologyProvider.getDataFactory().getOWLInverseFunctionalObjectPropertyAxiom(property);
                break;
            case "transitive":
                axiom = ontologyProvider.getDataFactory().getOWLTransitiveObjectPropertyAxiom(property);
                break;
            case "symmetric":
                axiom = ontologyProvider.getDataFactory().getOWLSymmetricObjectPropertyAxiom(property);
                break;
            case "asymmetric":
                axiom = ontologyProvider.getDataFactory().getOWLAsymmetricObjectPropertyAxiom(property);
                break;
            case "reflexive":
                axiom = ontologyProvider.getDataFactory().getOWLReflexiveObjectPropertyAxiom(property);
                break;
            case "irreflexive":
                axiom = ontologyProvider.getDataFactory().getOWLIrreflexiveObjectPropertyAxiom(property);
                break;
        }

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
