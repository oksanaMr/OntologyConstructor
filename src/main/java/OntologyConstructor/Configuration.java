package OntologyConstructor;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private OntologyProvider ontologyProvider;

    public Configuration() throws OWLOntologyCreationException {
        ontologyProvider = new OntologyProvider();
    }

    @Bean
    public OntologyProvider ontologyProvider(){
        return ontologyProvider;
    }

}
