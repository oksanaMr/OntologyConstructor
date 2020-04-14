package OntologyConstructor.controllers;

import OntologyConstructor.OntologyProvider;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
}
