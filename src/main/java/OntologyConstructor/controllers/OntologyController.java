package OntologyConstructor.controllers;

import OntologyConstructor.ontology.OntologyProvider;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
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
    public Map<String, String> getOntologyPrefix() {

        PrefixManager prefixManager = (PrefixDocumentFormat) ontologyProvider.getManager().getOntologyFormat(ontologyProvider.getOntology());

        return prefixManager.getPrefixName2PrefixMap();
    }

    @CrossOrigin
    @GetMapping("/getMetrics")
    public Map<String, Integer> getMetrics() {
        Map<String, Integer> map = new HashMap<>();

        map.put("Количество классов", ontologyProvider.getOntology().getClassesInSignature().size());
        map.put("Количество объектных свойств", ontologyProvider.getOntology().getObjectPropertiesInSignature().size());
        map.put("Количество свойств данных", ontologyProvider.getOntology().getDataPropertiesInSignature().size());
        map.put("Количество объектов", ontologyProvider.getOntology().getIndividualsInSignature().size());
        map.put("Количество аксиом", ontologyProvider.getOntology().getAxiomCount());

        return map;
    }

    @CrossOrigin
    @GetMapping("/openOntology")
    public Boolean openOntology() throws OWLOntologyCreationException {

        final Frame iFRAME = new Frame();
        iFRAME.setAlwaysOnTop(true);
        iFRAME.setLocationRelativeTo(null);
        iFRAME.setVisible(true);
        iFRAME.setVisible(false);

        FileDialog dialog = new FileDialog(iFRAME, "Select File to Open", FileDialog.LOAD);
        dialog.setFile("*.owl;*.txt");
        dialog.setVisible(true);
        dialog.pack();
        iFRAME.dispose();
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
    @GetMapping("/saveOntologyAs")
    public Boolean saveOntologyAs() throws IOException, OWLOntologyStorageException {

        final Frame iFRAME = new Frame();
        iFRAME.setAlwaysOnTop(true);
        iFRAME.setLocationRelativeTo(null);
        iFRAME.setVisible(true);
        iFRAME.setVisible(false);

        FileDialog dialog = new FileDialog(iFRAME, "Select File to Save", FileDialog.SAVE);
        dialog.setFile("*.owl;*.txt");
        dialog.setVisible(true);
        iFRAME.dispose();
        String dir = dialog.getDirectory();
        String file = dialog.getFile();

        if(file != null){
            File saveFile = new File(dir, file);
            ontologyProvider.getManager().saveOntology(ontologyProvider.getOntology(), new FileOutputStream(saveFile));
        }

        return true;
    }

    @CrossOrigin
    @GetMapping("/saveOntology")
    public Boolean saveOntology() throws IOException, OWLOntologyStorageException, URISyntaxException {

        if(ontologyProvider.getFileOntology().getAbsolutePath().equals(Paths.get(getClass().getClassLoader().getResource("baseOntology.owl").toURI()).toString())){
            saveOntologyAs();
        }
        else{
            ontologyProvider.getManager().saveOntology(ontologyProvider.getOntology());
        }

        return true;
    }
}
