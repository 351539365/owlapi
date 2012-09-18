package org.semanticweb.owlapi.api.test;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLVariable;

@SuppressWarnings("javadoc")
public class SWRLAnnotationTestCase {
    String NS = "http://protege.org/ontologies/SWRLAnnotation.owl";

    OWLClass A;
    OWLAxiom AXIOM;

    @Before
    public void setUp() {
        OWLDataFactory factory = OWLManager.getOWLDataFactory();
        A = factory.getOWLClass(IRI.create(NS + "#A"));
        SWRLVariable x = factory.getSWRLVariable(IRI.create(NS + "#x"));
        SWRLAtom atom = factory.getSWRLClassAtom(A, x);
        Set<SWRLAtom> consequent = new TreeSet<SWRLAtom>();
        consequent.add(atom);
        OWLAnnotation annotation = factory.getOWLAnnotation(factory.getRDFSComment(), factory.getOWLLiteral("Not a great rule"));
        Set<OWLAnnotation> annotations = new TreeSet<OWLAnnotation>();
        annotations.add(annotation);
        AXIOM = factory.getSWRLRule(new TreeSet<SWRLAtom>(), consequent, annotations);
        // System.out.println("Using " + AXIOM + " as a rule");
    }

    @Test
    public void shouldRoundTripAnnotation() throws OWLOntologyCreationException,
    OWLOntologyStorageException, IOException {
        OWLOntology ontology = createOntology();
        assertTrue(ontology.containsAxiom(AXIOM));
        String saved = saveOntology(ontology);
        ontology = loadOntology(saved);
        assertTrue(ontology.containsAxiom(AXIOM));
    }

    public OWLOntology createOntology() throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.createOntology(IRI.create(NS));
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.add(new AddAxiom(ontology, AXIOM));
        manager.applyChanges(changes);
        return ontology;
    }



    public String saveOntology(OWLOntology ontology) throws IOException,
    OWLOntologyStorageException {
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        StringDocumentTarget target = new StringDocumentTarget();
        manager.saveOntology(ontology, target);
        return target.toString();
    }

    public OWLOntology loadOntology(String ontologyFile)
            throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        return manager.loadOntologyFromOntologyDocument(new StringDocumentSource(
                ontologyFile));
    }


}
