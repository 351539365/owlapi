package org.semanticweb.owlapi.api.test.axioms;

import static org.junit.Assert.assertTrue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.api.test.Factory;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/** test for 3178902 adapted from the report Thimoty provided. */
@SuppressWarnings("javadoc")
public class ThreeEquivalentsRoundTripTestCase {
    private static final String NS = "http://protege.org/ontologies";
    private OWLClass b;
    private OWLClass c;
    private OWLObjectProperty p;
    private OWLObjectProperty q;
    private OWLAxiom axiomToAdd;

    @Before
    public void setUp() {
        b = Class(IRI(NS + "#B"));
        c = Class(IRI(NS + "#C"));
        p = ObjectProperty(IRI(NS + "#p"));
        q = ObjectProperty(IRI(NS + "#q"));
        axiomToAdd = EquivalentClasses(Class(IRI(NS + "#A")), ObjectSomeValuesFrom(p, b),
                ObjectSomeValuesFrom(q, c));
    }

    @Test
    public void shouldRoundTrip() throws OWLOntologyCreationException,
            OWLOntologyStorageException {
        // given
        OWLOntology ontology = Factory.getManager().createOntology();
        ontology.getOWLOntologyManager().addAxiom(ontology, axiomToAdd);
        String saved = saveOntology(ontology);
        // when
        ontology = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(saved));
        // then
        saved = saveOntology(ontology);
        assertTrue(ontology.containsObjectPropertyInSignature(p.getIRI()));
        assertTrue(ontology.containsObjectPropertyInSignature(q.getIRI()));
        assertTrue(ontology.containsClassInSignature(b.getIRI()));
        assertTrue(ontology.containsClassInSignature(c.getIRI()));
    }

    private String saveOntology(OWLOntology o) throws OWLOntologyStorageException {
        StringDocumentTarget target = new StringDocumentTarget();
        o.getOWLOntologyManager().saveOntology(o, target);
        return target.toString();
    }
}
