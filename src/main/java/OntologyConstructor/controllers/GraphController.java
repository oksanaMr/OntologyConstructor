package OntologyConstructor.controllers;

import OntologyConstructor.ontology.OntologyProvider;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping({"/graph"})
@RestController
public class GraphController {

    @Autowired
    private OntologyProvider ontologyProvider;

    @CrossOrigin
    @GetMapping("/getNodes")
    public Map<String, List<String>> getNodes() {
        Map<String, List<String>> nodes = new HashMap<>();

        ontologyProvider.getOntology().getClassesInSignature()
                .forEach(owlClass -> nodes.put(owlClass.toStringID(), Arrays.asList(owlClass.getIRI().getShortForm(), "class")));

        ontologyProvider.getOntology().getIndividualsInSignature()
                .forEach(individual -> nodes.put(individual.toStringID(), Arrays.asList(individual.getIRI().getShortForm(), "individual")));

        return nodes;
    }

    @CrossOrigin
    @GetMapping("/getEdges")
    public List<List<String>> getEdges() {
        List<List<String>> edges = new ArrayList<>();

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyProvider.getOntology());

        ontologyProvider.getOntology().getClassesInSignature()
                .forEach(owlClass -> {
                    reasoner.getSubClasses(owlClass, true).getFlattened()
                            .forEach(subClass -> {
                                if(!subClass.isOWLNothing())
                                edges.add(Arrays.asList(owlClass.toStringID(),subClass.toStringID(),"subclass"));});
                    reasoner.getEquivalentClasses(owlClass).getEntities()
                            .forEach(equivalent -> {
                                if (equivalent != owlClass)
                                edges.add(Arrays.asList(owlClass.toStringID(), equivalent.toStringID(), "equivalent"));});
                });

        ontologyProvider.getOntology().getIndividualsInSignature()
                .forEach(individual -> {
                    reasoner.getTypes(individual, true).getFlattened()
                            .forEach(owlClass -> edges.add(Arrays.asList(owlClass.toStringID(), individual.toStringID(), "has individual")));
                    ontologyProvider.getOntology().getSameIndividualAxioms(individual)
                            .forEach(axiom -> axiom.getIndividuals().forEach(ind -> edges.add(Arrays.asList(individual.toStringID(), ind.toStringID(), "same individual"))));
                    ontologyProvider.getOntology().getObjectPropertyAssertionAxioms(individual)
                            .forEach(axiom -> edges.add(Arrays.asList(individual.toStringID(), axiom.getObject().toStringID(), axiom.getProperty().getNamedProperty().getIRI().getShortForm())));
                });

        return edges;
    }
}
