package OntologyConstructor.ontology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class OntologyProvider {

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    private File fileOntology;

    public OntologyProvider() throws OWLOntologyCreationException, URISyntaxException {
        manager = OWLManager.createOWLOntologyManager();
        fileOntology = Paths.get(getClass().getClassLoader().getResource("baseOntology.owl").toURI()).toFile();
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
