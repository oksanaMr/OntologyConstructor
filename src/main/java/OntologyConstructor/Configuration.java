package OntologyConstructor;

import OntologyConstructor.ontology.OntologyProvider;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.context.annotation.Bean;

import java.net.URISyntaxException;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private OntologyProvider ontologyProvider;

    public Configuration() throws OWLOntologyCreationException, URISyntaxException {
        ontologyProvider = new OntologyProvider();
    }

    @Bean
    public OntologyProvider ontologyProvider(){
        return ontologyProvider;
    }

}
