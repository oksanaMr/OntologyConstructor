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
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplString;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping({"/individual"})
@RestController
public class IndividualController {

    @Autowired
    private OntologyProvider ontologyProvider;

    @CrossOrigin
    @GetMapping("/getAllIndividuals")
    public Map<IRI, String> getAllIndividuals() {
        Map<IRI, String> allIndividualsList = new HashMap<>();
        Set<OWLNamedIndividual> classes = ontologyProvider.getOntology().getIndividualsInSignature();
        for(OWLNamedIndividual individual: classes){
            allIndividualsList.put(individual.getIRI(),individual.getIRI().getShortForm());
        }
        return allIndividualsList;
    }

    @CrossOrigin
    @PostMapping("/getIndividualDescription")
    public Map<String, List<String>> getIndividualDescription(@ModelAttribute MultipartFile file) throws IOException {

        String indIRI = new String(file.getBytes(), StandardCharsets.UTF_8);

        Map<String, List<String>> individualDescription = new HashMap<>();
        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(indIRI));

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyProvider.getOntology());

        List<String> types = reasoner.getTypes(individual, true).getFlattened()
                .stream().map(OWLEntity::toStringID).collect(Collectors.toList());
        individualDescription.put("types", types);

        List<String> sameIndividuals = new ArrayList<>();
        ontologyProvider.getOntology().getSameIndividualAxioms(individual)
                .forEach(axiom -> axiom.getIndividuals().forEach(ind -> sameIndividuals.add(ind.toStringID())));
        sameIndividuals.remove(indIRI);
        individualDescription.put("sameIndividuals", sameIndividuals);

        List<String> diffIndividuals = new ArrayList<>();
        ontologyProvider.getOntology().getDifferentIndividualAxioms(individual)
                .forEach(axiom -> axiom.getIndividuals().forEach(ind -> sameIndividuals.add(ind.toStringID())));
        diffIndividuals.remove(indIRI);
        individualDescription.put("diffIndividuals", diffIndividuals);

        List<String> objectPropAxiom = new ArrayList<>();
        ontologyProvider.getOntology().getObjectPropertyAssertionAxioms(individual)
                .forEach(axiom -> objectPropAxiom.add(axiom.getProperty().getNamedProperty().toStringID()
                        + " " + axiom.getObject().asOWLNamedIndividual().toStringID()));
        individualDescription.put("objectPropAxiom", objectPropAxiom);

        List<String> dataPropAxiom = new ArrayList<>();
        ontologyProvider.getOntology().getDataPropertyAssertionAxioms(individual)
                .forEach(axiom -> dataPropAxiom.add(axiom.getProperty().asOWLDataProperty().toStringID()+ " "
                        + axiom.getObject().getLiteral()));
        individualDescription.put("dataPropAxiom", dataPropAxiom);

        List<String> negativeObjectPropAxiom = new ArrayList<>();
        ontologyProvider.getOntology().getNegativeObjectPropertyAssertionAxioms(individual)
                .forEach(axiom -> negativeObjectPropAxiom.add(axiom.getProperty().getNamedProperty().toStringID() + " "
                + axiom.getObject().asOWLNamedIndividual().toStringID()));
        individualDescription.put("negativeObjectPropAxiom", negativeObjectPropAxiom);

        List<String> negativeDataPropAxiom = new ArrayList<>();
        ontologyProvider.getOntology().getNegativeDataPropertyAssertionAxioms(individual)
                .forEach(axiom -> negativeDataPropAxiom.add(axiom.getProperty().asOWLDataProperty().toStringID() + " "
                        + axiom.getObject().getLiteral()));
        individualDescription.put("negativeDataPropAxiom", negativeDataPropAxiom);

        return individualDescription;
    }

    @CrossOrigin
    @PostMapping("/addNewIndividual")
    public Map<String, String> addNewIndividual(@ModelAttribute MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();

        String indIRI = new String(file.getBytes(), StandardCharsets.UTF_8);
        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(indIRI));

        OWLDeclarationAxiom declarationAxiom = ontologyProvider.getDataFactory().getOWLDeclarationAxiom(individual);
        ontologyProvider.getManager().addAxiom(ontologyProvider.getOntology(), declarationAxiom);

        map.put("individualIRI", individual.toStringID());
        map.put("individualName", individual.getIRI().getShortForm());
        return map;
    }

    @CrossOrigin
    @PostMapping("/editIRIIndividual")
    public Map<String, String> editIRIIndividual(@ModelAttribute MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();

        String iri = new String(file.getBytes(), StandardCharsets.UTF_8);
        String oldIRI = iri.substring(0,iri.indexOf(";"));
        String newIRI = iri.substring(iri.indexOf(";")+1);

        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(oldIRI));

        OWLEntityRenamer entityRenamer = new OWLEntityRenamer(ontologyProvider.getManager(),Collections.singleton(ontologyProvider.getOntology()));
        ontologyProvider.getManager().applyChanges(entityRenamer.changeIRI(individual,IRI.create(newIRI)));

        individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(newIRI));

        map.put("individualIRI", individual.toStringID());
        map.put("individualName", individual.getIRI().getShortForm());

        return map;
    }

    @CrossOrigin
    @PostMapping("/deleteIndividual")
    public Boolean deleteIndividual(@ModelAttribute MultipartFile file) throws IOException {

        String indIRI = new String(file.getBytes(), StandardCharsets.UTF_8);
        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(indIRI));

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontologyProvider.getOntology()));
        individual.accept(remover);
        return ontologyProvider.getManager().applyChanges(remover.getChanges()) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/addAxiomIndividual")
    public Boolean addAxiomIndividual(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        String indIRI = data.substring(0, data.indexOf(";"));
        String objectIRI = data.substring(data.indexOf(";") + 1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!") + 1);

        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(indIRI));
        OWLNamedIndividual individual1 = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(objectIRI));
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(objectIRI));
        OWLAxiom axiom = null;

        switch (type) {
            case "type":
                axiom = ontologyProvider.getDataFactory().getOWLClassAssertionAxiom(owlClass, individual);
                break;
            case "equivalent":
                axiom = ontologyProvider.getDataFactory().getOWLSameIndividualAxiom(individual, individual1);
                break;
            case "diffInd":
                axiom = ontologyProvider.getDataFactory().getOWLDifferentIndividualsAxiom(individual, individual1);
                break;
        }

        AddAxiom moveAxiom = new AddAxiom(ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/addPropIndividual")
    public Boolean addPropIndividual(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        String indIRI = data.substring(0, data.indexOf(";"));
        String objectIRI = data.substring(data.indexOf(";") + 1, data.indexOf("*"));
        String value = data.substring(data.indexOf("*") + 1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!") + 1);

        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(indIRI));
        OWLAxiom axiom = null;

        switch (type) {
            case "objectProp":
                OWLObjectProperty objectProp = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(objectIRI));
                OWLNamedIndividual individual1 = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(value));
                axiom = ontologyProvider.getDataFactory().getOWLObjectPropertyAssertionAxiom(objectProp, individual, individual1);
                break;
            case "dataProp":
                OWLDataProperty dataProp = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(objectIRI));
                axiom = ontologyProvider.getDataFactory().getOWLDataPropertyAssertionAxiom(dataProp, individual, value);
                break;
            case "negativeObjectProp":
                OWLObjectProperty negativeObjectProp = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(objectIRI));
                OWLNamedIndividual ind = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(value));
                axiom = ontologyProvider.getDataFactory().getOWLNegativeObjectPropertyAssertionAxiom(negativeObjectProp, individual, ind);
                break;
            case "negativeDataProp":
                OWLDataProperty negativeDataProp = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(objectIRI));
                axiom = ontologyProvider.getDataFactory().getOWLNegativeDataPropertyAssertionAxiom(negativeDataProp, individual, new OWLLiteralImplString(value));
                break;
        }

        AddAxiom moveAxiom = new AddAxiom(ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/deleteAxiomIndividual")
    public Boolean deleteAxiomIndividual(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        String indIRI = data.substring(0,data.indexOf(";"));
        String objectIRI = data.substring(data.indexOf(";")+1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!")+1);

        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(indIRI));
        OWLNamedIndividual individual1 = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(objectIRI));
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(objectIRI));
        OWLAxiom axiom = null;

        switch (type) {
            case "type":
                axiom = ontologyProvider.getDataFactory().getOWLClassAssertionAxiom(owlClass, individual);
                break;
            case "equivalent":
                axiom = ontologyProvider.getDataFactory().getOWLSameIndividualAxiom(individual, individual1);
                break;
            case "diffInd":
                axiom = ontologyProvider.getDataFactory().getOWLDifferentIndividualsAxiom(individual, individual1);
                break;
        }

        RemoveAxiom moveAxiom = new RemoveAxiom (ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/deletePropIndividual")
    public Boolean deletePropIndividual(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        System.out.println(data);
        String indIRI = data.substring(0, data.indexOf(";"));
        String objectIRI = data.substring(data.indexOf(";") + 1, data.indexOf(" "));
        String value = data.substring(data.indexOf(" ") + 1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!") + 1);

        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(indIRI));
        OWLAxiom axiom = null;

        switch (type) {
            case "objectProp":
                OWLObjectProperty objectProp = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(objectIRI));
                OWLNamedIndividual individual1 = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(value));
                axiom = ontologyProvider.getDataFactory().getOWLObjectPropertyAssertionAxiom(objectProp, individual, individual1);
                break;
            case "dataProp":
                OWLDataProperty dataProp = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(objectIRI));
                axiom = ontologyProvider.getDataFactory().getOWLDataPropertyAssertionAxiom(dataProp, individual, value);
                break;
            case "negativeObjectProp":
                OWLObjectProperty negativeObjectProp = ontologyProvider.getDataFactory().getOWLObjectProperty(IRI.create(objectIRI));
                OWLNamedIndividual ind = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(value));
                axiom = ontologyProvider.getDataFactory().getOWLNegativeObjectPropertyAssertionAxiom(negativeObjectProp, individual, ind);
                break;
            case "negativeDataProp":
                OWLDataProperty negativeDataProp = ontologyProvider.getDataFactory().getOWLDataProperty(IRI.create(objectIRI));
                axiom = ontologyProvider.getDataFactory().getOWLNegativeDataPropertyAssertionAxiom(negativeDataProp, individual, new OWLLiteralImplString(value));
                break;
        }

        RemoveAxiom moveAxiom = new RemoveAxiom (ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }
}
