package OntologyConstructor.controllers;

import OntologyConstructor.OntologyProvider;
import org.semanticweb.owlapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
}
