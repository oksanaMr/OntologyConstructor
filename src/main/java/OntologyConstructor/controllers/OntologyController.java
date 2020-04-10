package OntologyConstructor.controllers;

import OntologyConstructor.OntologyProvider;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping({"/ontology"})
@RestController
public class OntologyController {

    @Autowired
    private OntologyProvider ontologyProvider;

    @CrossOrigin
    @GetMapping("/getOntologyIRI")
    public Map<String, String> getOntologyIRI() {
        Map<String, String> map = new HashMap<>();

        map.put("OntologyID",ontologyProvider.getOntology().getOntologyID().toString());
        map.put("OntologyIRI", ontologyProvider.getOntology().getOntologyID().getOntologyIRI().isPresent() ? ontologyProvider.getOntology().getOntologyID().getOntologyIRI().toString() : null);
        map.put("OntologyVersionIRI", ontologyProvider.getOntology().getOntologyID().getVersionIRI().isPresent() ? ontologyProvider.getOntology().getOntologyID().getVersionIRI().toString() : null);

        return map;
    }

    @CrossOrigin
    @GetMapping("/getOntologyPrefix")
    public Map<String, String> getOntologyMapper() {

        PrefixManager prefixManager = (PrefixDocumentFormat) ontologyProvider.getManager().getOntologyFormat(ontologyProvider.getOntology());
        Map<String, String> map = prefixManager.getPrefixName2PrefixMap();

        return map;
    }

    @CrossOrigin
    @PostMapping("/openOntology")
    public Boolean openOntology(@RequestParam("file") MultipartFile file) throws OWLOntologyCreationException, IOException {

        File newFile = new File("C:/Users/пк/Documents/OntologyConstructor", file.getOriginalFilename());
        newFile.createNewFile();
        file.transferTo(newFile);

        ontologyProvider.setFileOntology(newFile);
        ontologyProvider.setOntology(ontologyProvider.getManager().loadOntologyFromOntologyDocument(newFile));

        return true;
    }

    @CrossOrigin
    @PostMapping("/saveOntology")
    public Boolean saveOntology(@RequestBody String fileName) throws IOException, OWLOntologyStorageException {

        File file = new File("C:/Users/пк/Documents/OntologyConstructor",fileName);
        file.createNewFile();
        ontologyProvider.getManager().saveOntology(ontologyProvider.getOntology(), new FileOutputStream(file));

        return true;
    }
}
