package OntologyConstructor.controllers;

import OntologyConstructor.OntologyProvider;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
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
    @PostMapping("/setOntologyIRI")
    public Map<String, String> setOntologyIRI(@ModelAttribute MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();

        String iri = new String(file.getBytes());
        String ontologyIRI = iri.substring(0,iri.indexOf(";"));
        String ontologyVersionIRI = iri.substring(iri.indexOf(";")+1,iri.length());
        System.out.println(iri + "\n" + ontologyIRI + "\n" + ontologyVersionIRI);

        OWLOntologyID newOntologyID = new OWLOntologyID(IRI.create(ontologyIRI),IRI.create(ontologyVersionIRI));
        SetOntologyID setOntologyID = new SetOntologyID(ontologyProvider.getOntology(), newOntologyID);
        ontologyProvider.getManager().applyChange(setOntologyID);

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
    @GetMapping("/openOntology")
    public Boolean openOntology1() throws OWLOntologyCreationException {

        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open", FileDialog.LOAD);
        dialog.setFilenameFilter((File dir, String name) -> name.endsWith(".owl"));
        dialog.setAlwaysOnTop(true);
        dialog.setAutoRequestFocus(true);
        dialog.toFront();
        dialog.setVisible(true);
        dialog.toFront();
        String dir = dialog.getDirectory();
        String file = dialog.getFile();

        if(file != null){
            File newFile = new File(dir, file);
            ontologyProvider.setFileOntology(newFile);
            ontologyProvider.setOntology(ontologyProvider.getManager().loadOntologyFromOntologyDocument(newFile));
            return true;
        }

        return false;
    }

    @CrossOrigin
    @GetMapping("/saveOntology")
    public Boolean saveOntology() throws IOException, OWLOntologyStorageException {

        FileDialog dialog = new FileDialog((Frame)null, "Select File to Save", FileDialog.LOAD);
        dialog.setFilenameFilter((File dir, String name) -> name.endsWith(".owl"));
        dialog.setAlwaysOnTop(true);
        dialog.setAutoRequestFocus(true);
        dialog.setVisible(true);
        dialog.toFront();
        String dir = dialog.getDirectory();
        String file = dialog.getFile();

        if(file != null){
            File saveFile = new File(dir, file);
            ontologyProvider.getManager().saveOntology(ontologyProvider.getOntology(), new FileOutputStream(saveFile));
        }

        return true;
    }
}
