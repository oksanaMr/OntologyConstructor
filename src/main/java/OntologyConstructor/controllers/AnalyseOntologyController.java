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
    public Map<String,String> getAnalyse(@ModelAttribute MultipartFile file) throws IOException, SQWRLException, SWRLParseException {

        String productIRI = new String(file.getBytes(), StandardCharsets.UTF_8);

        OWLNamedIndividual product = ontologyProvider.getDataFactory().getOWLNamedIndividual(IRI.create(productIRI));

        PrefixManager prefixManager = (PrefixDocumentFormat) ontologyProvider.getManager().getOntologyFormat(ontologyProvider.getOntology());
        String shortPrefix = getKey(prefixManager.getPrefixName2PrefixMap(),product.getIRI().getNamespace());

        Map<String,String> map = new HashMap<>();
        AnalyseOntology analyseOntology = new AnalyseOntology();

        String seqOperation = "";
        String resultAnalyse = "";

        List<String> listOperation;
        List<String> allOperation = analyseOntology.getAllOperation(ontologyProvider.getOntology(), product, shortPrefix);
        List<String> operations;

        if(allOperation.isEmpty()){
            resultAnalyse += "Для данного продукта не найдено ни одной операции\n";
            seqOperation += "Последовательность операций не найдена\n";
        }
        else {
            listOperation = analyseOntology.getSeqOperation(ontologyProvider.getOntology(),product, shortPrefix);
            if(listOperation.isEmpty()){
                seqOperation += "Последовательность операций не найдена\n";
                resultAnalyse += "Следующие операции не соединены:\n";
                resultAnalyse += allOperation;
            }
            else {
                seqOperation += listOperation;
                operations = analyseOntology.getFirstOperation(ontologyProvider.getOntology(), product, shortPrefix);
                if(operations.isEmpty()){
                    resultAnalyse += "Первые операции не найдены\n";
                }
                else {
                    resultAnalyse += "Первые операции:\n";
                    resultAnalyse += operations;
                }
                operations.clear();
                operations = analyseOntology.getLastOperation(ontologyProvider.getOntology(), product, shortPrefix);
                if(operations.isEmpty()){
                    resultAnalyse += "\nПоследние операции не найдены\n";
                }
                else {
                    resultAnalyse += "\nПоследние операции:\n";
                    resultAnalyse += operations;
                }
                operations.clear();
                operations = analyseOntology.checkCycle(ontologyProvider.getOntology(),product, shortPrefix);
                if(operations.isEmpty()){
                    resultAnalyse += "\nЦиклов в последовательности операций не обнаружено\n";
                }
                else {
                    resultAnalyse += "\nВ последовательности операций обнаружен цикл!\nОперации входящие в цикл:\n";
                    resultAnalyse += operations;
                }
                operations.clear();
                operations = analyseOntology.getBadFirstOperation(ontologyProvider.getOntology(), product, shortPrefix);
                if(!operations.isEmpty()){
                    resultAnalyse += "\nПоследовательность операций не полная!\nДля следующих операций не указана предыдущая операция:\n";
                    resultAnalyse += operations;
                }
                operations.clear();
                operations = analyseOntology.getBadLastOperation(ontologyProvider.getOntology(), product, shortPrefix);
                if(!operations.isEmpty()){
                    resultAnalyse += "\nПоследовательность операций не полная!\nДля следующих операций не указана следующая операция:\n";
                    resultAnalyse += operations;
                }
                operations.clear();
                operations = analyseOntology.getHangingOperation(ontologyProvider.getOntology(), product, shortPrefix);
                if(!operations.isEmpty()){
                    resultAnalyse += "\nПоследовательность операций не полная!\nОбнаружены висящие вершины:\n";
                    resultAnalyse += operations;
                }
            }
        }

        seqOperation = seqOperation.replaceAll(",","");
        seqOperation = seqOperation.replaceAll( "\\[","");
        seqOperation = seqOperation.replaceAll( "]","");
        resultAnalyse = resultAnalyse.replaceAll(",","");
        resultAnalyse = resultAnalyse.replaceAll( "\\[","");
        resultAnalyse = resultAnalyse.replaceAll( "]","");
        map.put("seqOperation", seqOperation);
        map.put("resultAnalyse", resultAnalyse);

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
