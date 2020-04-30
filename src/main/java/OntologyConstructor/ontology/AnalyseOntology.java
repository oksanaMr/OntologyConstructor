package OntologyConstructor.ontology;

import org.semanticweb.owlapi.model.*;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;
import org.swrlapi.sqwrl.exceptions.SQWRLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalyseOntology {

    public List<String> getSeqOperation(OWLOntology o, OWLNamedIndividual product, String productPrefix) throws SQWRLException, SWRLParseException {
        List<String> seqOperation = new ArrayList<>();

        OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
        OWLObjectProperty nextOperationProperty = df.getOWLObjectProperty(IRI.create("http://baseOntology/NextOperation"));
        OWLAxiom transitiveAxiom = df.getOWLTransitiveObjectPropertyAxiom(nextOperationProperty);
        boolean checkTransitive = o.containsAxiom(transitiveAxiom);
        if(checkTransitive){
            o.getOWLOntologyManager().removeAxiom(o, transitiveAxiom);
        }

        SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine (o);

        SQWRLResult result = queryEngine.runSQWRLQuery ("Q1",
                "baseOntology:Operation(?o) ^ baseOntology:NextOperation(?o, ?n) ^ baseOntology:ProductProperty(?o, "
                        + productPrefix + product.getIRI().getShortForm() + ") ^ baseOntology:ProductProperty(?n,"
                        + productPrefix + product.getIRI().getShortForm() + ")-> sqwrl:select(?o,?n)");

        while(result.next()){
            seqOperation.add(result.getNamedIndividual("o").getIRI().getShortForm() + "->" + result.getNamedIndividual("n").getIRI().getShortForm() + "\n");
        }

        if(checkTransitive) {
            o.getOWLOntologyManager().addAxiom(o, transitiveAxiom);
        }

        queryEngine.deleteSWRLRule("Q1");
        Collections.sort(seqOperation);
        return seqOperation;
    }

    public List<String> getFirstOperation(OWLOntology o, OWLNamedIndividual product, String productPrefix) throws SQWRLException, SWRLParseException {
        List<String> firstOperationList = new ArrayList<>();

        SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine (o);

        SQWRLResult result = queryEngine.runSQWRLQuery ("Q2",
                "baseOntology:Operation(?o) ^ baseOntology:ProductProperty(?o," + productPrefix
                        + product.getIRI().getShortForm() + ") ^ baseOntology:isFirst(?o," + productPrefix
                        + product.getIRI().getShortForm() + ") -> sqwrl:select(?o)");

        while(result.next()){
            firstOperationList.add(result.getNamedIndividual("o").getIRI().getShortForm() + "\n");
        }

        if(firstOperationList.isEmpty()){
            result = queryEngine.runSQWRLQuery ("Q3",
                    "baseOntology:Operation(?o) ^ baseOntology:NextOperation(?o, ?n) ^ baseOntology:ProductProperty(?o, " + productPrefix
                            + product.getIRI().getShortForm() + ") ^ baseOntology:ProductProperty(?n," + productPrefix
                            + product.getIRI().getShortForm() + ") .  sqwrl:makeSet(?s1, ?o) ^ sqwrl:makeSet(?s2, ?n)  .  sqwrl:difference(?s3, ?s1, ?s2) ^ sqwrl:element(?e, ?s3) -> sqwrl:select(?e)");

            while(result.next()){
                firstOperationList.add(result.getNamedIndividual("e").getIRI().getShortForm() + "\n");
            }
        }

        queryEngine.deleteSWRLRule("Q2");
        queryEngine.deleteSWRLRule("Q3");
        return firstOperationList;
    }

    public List<String> getLastOperation(OWLOntology o, OWLNamedIndividual product, String productPrefix) throws SQWRLException, SWRLParseException {
        List<String> lastOperationList = new ArrayList<>();

        SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine (o);

        SQWRLResult result = queryEngine.runSQWRLQuery ("Q4",
                "baseOntology:Operation(?o) ^ baseOntology:ProductProperty(?o, " + productPrefix
                        + product.getIRI().getShortForm() + ") ^ baseOntology:isLast(?o," + productPrefix + product.getIRI().getShortForm() + ") -> sqwrl:select(?o)");

        while(result.next()){
            lastOperationList.add(result.getNamedIndividual("o").getIRI().getShortForm() + "\n");
        }

        if(lastOperationList.isEmpty()){
            result = queryEngine.runSQWRLQuery ("Q5",
                    "baseOntology:Operation(?o) ^ baseOntology:NextOperation(?o, ?n) ^ baseOntology:ProductProperty(?o, " + productPrefix
                            + product.getIRI().getShortForm() + ") ^ baseOntology:ProductProperty(?n," + productPrefix
                            + product.getIRI().getShortForm() + ") .  sqwrl:makeSet(?s1, ?o) ^ sqwrl:makeSet(?s2, ?n)  .  sqwrl:difference(?s3, ?s2, ?s1) ^ sqwrl:element(?e, ?s3) -> sqwrl:select(?e)");

            while(result.next()){
                lastOperationList.add(result.getNamedIndividual("e").getIRI().getShortForm() + "\n");
            }
        }

        queryEngine.deleteSWRLRule("Q4");
        queryEngine.deleteSWRLRule("Q5");
        return lastOperationList;
    }

    public List<String> checkCycle(OWLOntology o, OWLNamedIndividual product, String productPrefix) throws SQWRLException, SWRLParseException {
        List<String> cycle = new ArrayList<>();

        OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
        OWLObjectProperty nextOperationProperty = df.getOWLObjectProperty(IRI.create("http://baseOntology/NextOperation"));
        OWLAxiom transitiveAxiom = df.getOWLTransitiveObjectPropertyAxiom(nextOperationProperty);
        boolean checkTransitive = o.containsAxiom(transitiveAxiom);
        if(!checkTransitive){
            o.getOWLOntologyManager().addAxiom(o, transitiveAxiom);
        }

        SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine (o);

        SQWRLResult result = queryEngine.runSQWRLQuery ("Q6",
                "baseOntology:Operation(?o) ^ baseOntology:NextOperation(?o, ?n) ^ baseOntology:ProductProperty(?o," +  productPrefix +
                        product.getIRI().getFragment() + ") ^ baseOntology:ProductProperty(?n," +  productPrefix +
                        product.getIRI().getFragment() + ") -> sqwrl:select(?o,?n)");

        while(result.next()){
            OWLNamedIndividual operation = df.getOWLNamedIndividual(result.getNamedIndividual("o").getIRI());
            OWLNamedIndividual nextOperation = df.getOWLNamedIndividual(result.getNamedIndividual("n").getIRI());
            if(operation.getIRI() == nextOperation.getIRI()){
                cycle.add(operation.getIRI().getShortForm()+ "\n");
            }
        }

        queryEngine.deleteSWRLRule("Q6");
        if(!checkTransitive) {
            o.getOWLOntologyManager().removeAxiom(o, transitiveAxiom);
        }
        return cycle;
    }

    public List<String> getBadFirstOperation(OWLOntology o, OWLNamedIndividual product, String productPrefix) throws SQWRLException, SWRLParseException {
        List<String> listBadFirstOperation = new ArrayList<>();

        SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine (o);

        SQWRLResult result = queryEngine.runSQWRLQuery ("Q7",
                "baseOntology:Operation(?o) ^ baseOntology:ProductProperty(?o," + productPrefix
                        + product.getIRI().getShortForm() + ") ^ baseOntology:isFirst(?o," + productPrefix
                        + product.getIRI().getShortForm() + ") -> sqwrl:select(?o)");

        if(!result.isEmpty()){
            SQWRLResult result1 = queryEngine.runSQWRLQuery ("Q8",
                    "baseOntology:Operation(?o) ^ baseOntology:NextOperation(?o, ?n) ^ baseOntology:ProductProperty(?o," + productPrefix +
                            product.getIRI().getShortForm() + ") ^ baseOntology:ProductProperty(?n," + productPrefix +
                            product.getIRI().getShortForm() + ") ^ baseOntology:Operation(?a) ^ baseOntology:ProductProperty(?a," + productPrefix + product.getIRI().getShortForm()
                            + ") ^ ont:isFirst(?a," + productPrefix + product.getIRI().getShortForm() + ")  .  sqwrl:makeSet(?s1, ?o) ^ sqwrl:makeSet(?s2, ?n) ^ sqwrl:makeSet(?s3, ?a)  .  sqwrl:difference(?s4, ?s1, ?s2) ^ sqwrl:difference(?s5, ?s4, ?s3) ^ sqwrl:element(?e, ?s5) -> sqwrl:select(?e)");

            while(result1.next()){
                listBadFirstOperation.add(result1.getNamedIndividual("e").getIRI().getShortForm() + "\n");
            }
        }
        queryEngine.deleteSWRLRule("Q7");
        queryEngine.deleteSWRLRule("Q8");

        return listBadFirstOperation;
    }

    public List<String> getBadLastOperation(OWLOntology o, OWLNamedIndividual product, String productPrefix) throws SQWRLException, SWRLParseException {
        List<String> listBadLastOperation = new ArrayList<>();

        SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine (o);

        SQWRLResult result = queryEngine.runSQWRLQuery ("Q9",
                "baseOntology:Operation(?o) ^ baseOntology:ProductProperty(?o, " + productPrefix
                        + product.getIRI().getShortForm() + ") ^ baseOntology:isLast(?o," + productPrefix + product.getIRI().getShortForm() + ") -> sqwrl:select(?o)");

        if(!result.isEmpty()){
            result = queryEngine.runSQWRLQuery ("Q10",
                    "baseOntology:Operation(?o) ^ baseOntology:NextOperation(?o, ?n) ^ baseOntology:ProductProperty(?o," + productPrefix +
                            product.getIRI().getShortForm() + ") ^ baseOntology:ProductProperty(?n," + productPrefix +
                            product.getIRI().getShortForm() + ") ^ baseOntology:Operation(?a) ^ baseOntology:ProductProperty(?a," + productPrefix + product.getIRI().getFragment()
                            + ") ^ ont:isLast(?a," + productPrefix + product.getIRI().getShortForm() + ")  .  sqwrl:makeSet(?s1, ?o) ^ sqwrl:makeSet(?s2, ?n) ^ sqwrl:makeSet(?s3, ?a)  .  sqwrl:difference(?s4, ?s2, ?s1) ^ sqwrl:difference(?s5, ?s4, ?s3) ^ sqwrl:element(?e, ?s5) -> sqwrl:select(?e)");

            while(result.next()){
                listBadLastOperation.add(result.getNamedIndividual("e").getIRI().getShortForm() + "\n");
            }
        }
        queryEngine.deleteSWRLRule("Q9");
        queryEngine.deleteSWRLRule("Q10");

        return listBadLastOperation;
    }

    public List<String> getHangingOperation(OWLOntology o, OWLNamedIndividual product, String productPrefix) throws SQWRLException, SWRLParseException {
        List<String> listHangingLastOperation = new ArrayList<>();

        SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine (o);

        SQWRLResult result = queryEngine.runSQWRLQuery ("Q11",
                "baseOntology:Operation(?o) ^ baseOntology:NextOperation(?o, ?n) ^ baseOntology:ProductProperty(?o, " + productPrefix
                        + product.getIRI().getShortForm() + ") ^ baseOntology:ProductProperty(?n," + productPrefix
                        + product.getIRI().getShortForm() + ") ^ baseOntology:Operation(?a) ^ baseOntology:PreviousOperation(?a, ?b) ^ baseOntology:ProductProperty(?a, " + productPrefix
                        + product.getIRI().getShortForm() + ") ^ baseOntology:ProductProperty(?b," + productPrefix
                        + product.getIRI().getShortForm() + ") ^ baseOntology:Operation(?c) ^ baseOntology:ProductProperty(?c," + productPrefix
                        + product.getIRI().getShortForm() + ") .  sqwrl:makeSet(?s1, ?o) ^ sqwrl:makeSet(?s2, ?a) ^ sqwrl:makeSet(?s3, ?c)  . sqwrl:difference(?s4, ?s3, ?s1) ^ sqwrl:difference(?s5, ?s4, ?s2) ^ sqwrl:element(?e, ?s5)-> sqwrl:select(?e)");

        while(result.next()){
            listHangingLastOperation.add(result.getNamedIndividual("e").getIRI().getShortForm() + "\n");
        }

        return listHangingLastOperation;
    }
}
