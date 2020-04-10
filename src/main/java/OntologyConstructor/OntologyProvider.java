package OntologyConstructor;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;

public class OntologyProvider {

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    private File fileOntology = new File("C:/Users/пк/Documents/baseOntology1.txt");

    public OntologyProvider() throws OWLOntologyCreationException {
        manager = OWLManager.createOWLOntologyManager();
        //ontology = manager.createOntology();
        ontology = manager.loadOntologyFromOntologyDocument(fileOntology);
        dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
    }

    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public void setManager(OWLOntologyManager manager) {
        this.manager = manager;
    }

    public void setDataFactory(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    public File getFileOntology() {
        return fileOntology;
    }

    public void setFileOntology(File fileOntology) {
        this.fileOntology = fileOntology;
    }
}
