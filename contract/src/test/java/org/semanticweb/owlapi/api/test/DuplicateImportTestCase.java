package org.semanticweb.owlapi.api.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class DuplicateImportTestCase {

    @Test
    public void shouldLoad() throws Exception {
        File ontologyByName;
        File ontologyByVersion;
        File ontologyByOtherPath;
        File importsBothNameAndVersion;
        File importsBothNameAndOther;
        ontologyByName = File.createTempFile("temp", "main.owl");
        ontologyByVersion = File.createTempFile("temp", "version.owl");
        ontologyByOtherPath = File.createTempFile("temp", "other.owl");
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.createOntology(new OWLOntologyID(IRI
                .create(ontologyByName), IRI.create(ontologyByVersion)));
        manager.saveOntology(ontology, IRI.create(ontologyByName));
        manager.saveOntology(ontology, IRI.create(ontologyByVersion));
        manager.saveOntology(ontology, IRI.create(ontologyByOtherPath));
        importsBothNameAndVersion = File.createTempFile("temp",
                "importsNameAndVersion.owl");
        importsBothNameAndOther = File.createTempFile("temp", "importsNameAndOther.owl");
        manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLOntology ontology1 = manager.createOntology(IRI
                .create(importsBothNameAndVersion));
        OWLOntology ontology2 = manager.createOntology(IRI
                .create(importsBothNameAndOther));
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.add(new AddImport(ontology1, factory.getOWLImportsDeclaration(IRI
                .create(ontologyByName))));
        changes.add(new AddImport(ontology1, factory.getOWLImportsDeclaration(IRI
                .create(ontologyByVersion))));
        changes.add(new AddImport(ontology2, factory.getOWLImportsDeclaration(IRI
                .create(ontologyByName))));
        changes.add(new AddImport(ontology2, factory.getOWLImportsDeclaration(IRI
                .create(ontologyByOtherPath))));
        manager.applyChanges(changes);
        manager.saveOntology(ontology1, IRI.create(importsBothNameAndVersion));
        manager.saveOntology(ontology2, IRI.create(importsBothNameAndOther));
        // when
        manager = OWLManager.createOWLOntologyManager();
        OWLOntology o1 = manager.loadOntology(IRI.create(importsBothNameAndVersion));
        manager = OWLManager.createOWLOntologyManager();
        OWLOntology o2 = manager.loadOntology(IRI.create(importsBothNameAndOther));
        // then
        assertNotNull(o1);
        assertNotNull(o2);
    }
}
