package OntologyConstructor.controllers;

import OntologyConstructor.OntologyProvider;
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
@RequestMapping({"/class"})
@RestController
public class ClassController {

    @Autowired
    private OntologyProvider ontologyProvider;

    @CrossOrigin
    @GetMapping("/getAllClasses")
    public Map<IRI, String> getAllClasses() {
        Map<IRI, String> allClassesList = new HashMap<>();
        Set<OWLClass> classes = ontologyProvider.getOntology().getClassesInSignature();
        for(OWLClass owlClass: classes){
            allClassesList.put(owlClass.getIRI(),owlClass.getIRI().getShortForm());
        }
        return allClassesList;
    }

    @CrossOrigin
    @PostMapping("/getClassDescription")
    public Map<String, List<String>> getClassDescription(@ModelAttribute MultipartFile file) throws IOException {

        String classIRI = new String(file.getBytes(), StandardCharsets.UTF_8);

        Map<String, List<String>> classDescription = new HashMap<>();
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(classIRI));

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyProvider.getOntology());

        List<String> individuals = reasoner.getInstances(owlClass, true).getFlattened()
                .stream().map(individual -> individual.getIRI().toString()).collect(Collectors.toList());;
        classDescription.put("individuals", individuals);

        List<String> superClasses = reasoner.getSuperClasses(owlClass, true).getFlattened()
                .stream().map(superClass -> superClass.getIRI().toString()).collect(Collectors.toList());
        superClasses.remove("http://www.w3.org/2002/07/owl#Thing");
        classDescription.put("superClasses", superClasses);

        List<String> subClasses = reasoner.getSubClasses(owlClass, true).getFlattened()
                .stream().map(subClass -> subClass.getIRI().toString()).collect(Collectors.toList());
        subClasses.remove("http://www.w3.org/2002/07/owl#Nothing");
        classDescription.put("subClasses", subClasses);

        List<String> equivalents = reasoner.getEquivalentClasses(owlClass).getEntities()
                .stream().map(equivalent -> equivalent.getIRI().toString()).collect(Collectors.toList());;
        equivalents.remove(classIRI);
        classDescription.put("equivalents", equivalents);

        List<String> disjointClasses = reasoner.getDisjointClasses(owlClass).getFlattened()
                .stream().map(disjoint -> disjoint.getIRI().toString()).collect(Collectors.toList());
        disjointClasses.remove(classIRI);
        classDescription.put("disjointClasses", disjointClasses);

        return classDescription;
    }

    @CrossOrigin
    @PostMapping("/addNewClass")
    public Map<String, String> addNewClass(@ModelAttribute MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();

        String classIRI = new String(file.getBytes(), StandardCharsets.UTF_8);
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(classIRI));

        OWLDeclarationAxiom declarationAxiom = ontologyProvider.getDataFactory().getOWLDeclarationAxiom(owlClass);
        ontologyProvider.getManager().addAxiom(ontologyProvider.getOntology(), declarationAxiom);

        map.put("classIRI", owlClass.getIRI().toString());
        map.put("className", owlClass.getIRI().getShortForm());
        return map;
    }

    @CrossOrigin
    @PostMapping("/editIRIClass")
    public Map<String, String> editIRIClass(@ModelAttribute MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();

        String iri = new String(file.getBytes(), StandardCharsets.UTF_8);
        String oldIRIClass = iri.substring(0,iri.indexOf(";"));
        String newIRIClass = iri.substring(iri.indexOf(";")+1);

        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(oldIRIClass));

        OWLEntityRenamer entityRenamer = new OWLEntityRenamer(ontologyProvider.getManager(),Collections.singleton(ontologyProvider.getOntology()));
        ontologyProvider.getManager().applyChanges(entityRenamer.changeIRI(owlClass,IRI.create(newIRIClass)));

        owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(newIRIClass));

        map.put("classIRI", owlClass.getIRI().toString());
        map.put("className", owlClass.getIRI().getShortForm());

        return map;
    }

    @CrossOrigin
    @PostMapping("/deleteClass")
    public Boolean deleteClass(@ModelAttribute MultipartFile file) throws IOException {

        String classIRI = new String(file.getBytes(), StandardCharsets.UTF_8);
        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create(classIRI));

        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ontologyProvider.getOntology()));
        owlClass.accept(remover);
        return ontologyProvider.getManager().applyChanges(remover.getChanges()) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/deleteAxiomClass")
    public Boolean deleteAxiomClass(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        String class1IRI = data.substring(0,data.indexOf(";"));
        String class2IRI = data.substring(data.indexOf(";")+1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!")+1);

        OWLClass class1 = ontologyProvider.getDataFactory().getOWLClass(IRI.create(class1IRI));
        OWLClass class2 = ontologyProvider.getDataFactory().getOWLClass(IRI.create(class2IRI));
        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(class2IRI));
        OWLAxiom axiom = null;

        switch (type){
            case "individual":
                axiom = ontologyProvider.getDataFactory().getOWLClassAssertionAxiom(class1, individual);
                break;
            case "superclass":
                axiom = ontologyProvider.getDataFactory().getOWLSubClassOfAxiom(class1, class2);
                break;
            case "subclass":
                axiom = ontologyProvider.getDataFactory().getOWLSubClassOfAxiom(class2, class1);
                break;
            case "equivalent":
                axiom = ontologyProvider.getDataFactory().getOWLEquivalentClassesAxiom(class1, class2);
                break;
            case "disjoint":
                axiom = ontologyProvider.getDataFactory().getOWLDisjointClassesAxiom(class1, class2);
                break;
        }

        RemoveAxiom moveAxiom = new RemoveAxiom (ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }

    @CrossOrigin
    @PostMapping("/addAxiomClass")
    public Boolean addAxiomClass(@ModelAttribute MultipartFile file) throws IOException {

        String data = new String(file.getBytes(), StandardCharsets.UTF_8);
        String class1IRI = data.substring(0,data.indexOf(";"));
        String class2IRI = data.substring(data.indexOf(";")+1, data.indexOf("!"));
        String type = data.substring(data.indexOf("!")+1);

        OWLClass class1 = ontologyProvider.getDataFactory().getOWLClass(IRI.create(class1IRI));
        OWLClass class2 = ontologyProvider.getDataFactory().getOWLClass(IRI.create(class2IRI));
        OWLNamedIndividual individual = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(class2IRI));
        OWLAxiom axiom = null;

        switch (type){
            case "individual":
                axiom = ontologyProvider.getDataFactory().getOWLClassAssertionAxiom(class1, individual);
                break;
            case "superclass":
                axiom = ontologyProvider.getDataFactory().getOWLSubClassOfAxiom(class1, class2);
                break;
            case "subclass":
                axiom = ontologyProvider.getDataFactory().getOWLSubClassOfAxiom(class2, class1);
                break;
            case "equivalent":
                axiom = ontologyProvider.getDataFactory().getOWLEquivalentClassesAxiom(class1, class2);
                break;
            case "disjoint":
                axiom = ontologyProvider.getDataFactory().getOWLDisjointClassesAxiom(class1, class2);
                break;
        }

        AddAxiom moveAxiom = new AddAxiom (ontologyProvider.getOntology(), axiom);
        return ontologyProvider.getManager().applyChange(moveAxiom) == ChangeApplied.SUCCESSFULLY;
    }
}
