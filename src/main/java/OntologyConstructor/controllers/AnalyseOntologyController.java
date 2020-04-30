package OntologyConstructor.controllers;

import OntologyConstructor.ontology.AnalyseOntology;
import OntologyConstructor.ontology.OntologyProvider;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.exceptions.SQWRLException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping({"/analyseOntology"})
@RestController
public class AnalyseOntologyController {

    @Autowired
    private OntologyProvider ontologyProvider;

    @CrossOrigin
    @GetMapping("/getProducts")
    public List<String> getProducts() {

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontologyProvider.getOntology());

        OWLClass owlClass = ontologyProvider.getDataFactory().getOWLClass(IRI.create("http://baseOntology/Product"));

        return reasoner.getInstances(owlClass, true).getFlattened()
                .stream().map(product -> product.getIRI().toString()).collect(Collectors.toList());
    }

    @CrossOrigin
    @PostMapping("/getAnalyse")
    public Map<String,List<String>> getAnalyse(@ModelAttribute MultipartFile file) throws IOException, SQWRLException, SWRLParseException {

        String productIRI = new String(file.getBytes(), StandardCharsets.UTF_8);

        OWLNamedIndividual product = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(productIRI));
        PrefixManager prefixManager = (PrefixDocumentFormat) ontologyProvider.getManager().getOntologyFormat(ontologyProvider.getOntology());
        Map<String, String> prefixMap = prefixManager.getPrefixName2PrefixMap();
        String shortPrefix = getKey(prefixMap,product.getIRI().getNamespace());

        Map<String,List<String>> map = new HashMap<>();
        AnalyseOntology analyseOntology = new AnalyseOntology();

        map.put("seqOperation", analyseOntology.getSeqOperation(ontologyProvider.getOntology(),product, shortPrefix));
        map.put("firstOperation", analyseOntology.getFirstOperation(ontologyProvider.getOntology(), product, shortPrefix));
        map.put("lastOperation", analyseOntology.getLastOperation(ontologyProvider.getOntology(), product, shortPrefix));
        map.put("cycle", analyseOntology.checkCycle(ontologyProvider.getOntology(),product, shortPrefix));
        map.put("badFirstOperation", analyseOntology.getBadFirstOperation(ontologyProvider.getOntology(), product, shortPrefix));
        map.put("badLastOperation", analyseOntology.getBadLastOperation(ontologyProvider.getOntology(), product, shortPrefix));
        map.put("hangingOperation", analyseOntology.getHangingOperation(ontologyProvider.getOntology(), product, shortPrefix));

        return map;
    }

    private <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
