package org.obolibrary.obo2owl;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.obolibrary.obo2owl.Obo2OWLConstants.Obo2OWLVocabulary;
import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatConstants.OboFormatTag;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

@SuppressWarnings("javadoc")
public class Owl2OboAltIdTest extends OboFormatTestBasics {

    @Test
    public void testOwl2OboClass() throws Exception {
        OWLOntology simple = getOWLOntology();
        // add class A
        OWLClass classA = df.getOWLClass(
            IRI.create(Obo2OWLConstants.DEFAULT_IRI_PREFIX + "TEST_0001"));
        simple.addAxiom(df.getOWLDeclarationAxiom(classA));
        // add a label and OBO style ID
        addLabelAndId(classA, "test1", "TEST:0001", simple);
        // add deprecated class B as an alternate ID for A
        OWLClass classB = df.getOWLClass(
            IRI.create(Obo2OWLConstants.DEFAULT_IRI_PREFIX + "TEST_0002"));
        simple.addAxiom(df.getOWLDeclarationAxiom(classB));
        setAltId(classB, simple);
        // add comment to alt_id class, which is not expressible in OBO
        addAnnotation(classB, df.getRDFSComment(), df.getOWLLiteral("Comment"),
            simple);
        // translate to OBO
        OWLAPIOwl2Obo owl2obo = new OWLAPIOwl2Obo(
            simple.getOWLOntologyManager());
        OBODoc oboDoc = owl2obo.convert(simple);
        // check result: expect only one term frame for class TEST:0001 with
        // alt_id Test:0002
        Collection<Frame> termFrames = oboDoc.getTermFrames();
        assertEquals(1, termFrames.size());
        Frame frame = termFrames.iterator().next();
        assertEquals("TEST:0001", frame.getId());
        Collection<Clause> altIdClauses = frame
            .getClauses(OboFormatTag.TAG_ALT_ID);
        assertEquals(1, altIdClauses.size());
        String altId = altIdClauses.iterator().next().getValue(String.class);
        assertEquals("TEST:0002", altId);
        // roundtrip back to OWL, check that comment is still there
        OWLAPIObo2Owl obo2owl = new OWLAPIObo2Owl(
            OWLManager.createOWLOntologyManager());
        OWLOntology roundTripped = obo2owl.convert(oboDoc);
        Set<OWLAnnotationAssertionAxiom> annotations = roundTripped
            .getAnnotationAssertionAxioms(classB.getIRI());
        assertEquals(4, annotations.size()); // three for the alt-id plus one
        // for the comment
        String comment = null;
        for (OWLAnnotationAssertionAxiom ax : annotations) {
            if (ax.getProperty().isComment()) {
                Optional<OWLLiteral> asLiteral = ax.getValue().asLiteral();
                if (asLiteral.isPresent()) {
                    comment = asLiteral.get().getLiteral();
                }
            }
        }
        assertEquals("Comment", comment);
    }

    @Test
    public void testOwl2OboProperty() throws Exception {
        OWLOntology simple = getOWLOntology();
        // add prop1
        OWLObjectProperty p1 = df.getOWLObjectProperty(
            IRI.create(Obo2OWLConstants.DEFAULT_IRI_PREFIX + "TEST_0001"));
        simple.addAxiom(df.getOWLDeclarationAxiom(p1));
        // add label and OBO style id for
        addLabelAndId(p1, "prop1", "TEST:0001", simple);
        // add deprecated prop 2 as an alternate ID for prop 1
        OWLObjectProperty p2 = df.getOWLObjectProperty(
            IRI.create(Obo2OWLConstants.DEFAULT_IRI_PREFIX + "TEST_0002"));
        simple.addAxiom(df.getOWLDeclarationAxiom(p2));
        setAltId(p2, simple);
        // add comment to alt_id class, which is not expressible in OBO
        addAnnotation(p2, df.getRDFSComment(), df.getOWLLiteral("Comment"),
            simple);
        // translate to OBO
        OWLAPIOwl2Obo owl2obo = new OWLAPIOwl2Obo(
            simple.getOWLOntologyManager());
        OBODoc oboDoc = owl2obo.convert(simple);
        // check result: expect only one typdef frame for prop TEST:0001 with
        // alt_id Test:0002
        Collection<Frame> termFrames = oboDoc.getTypedefFrames();
        assertEquals(1, termFrames.size());
        Frame frame = termFrames.iterator().next();
        assertEquals("TEST:0001", frame.getId());
        Collection<Clause> altIdClauses = frame
            .getClauses(OboFormatTag.TAG_ALT_ID);
        assertEquals(1, altIdClauses.size());
        String altId = altIdClauses.iterator().next().getValue(String.class);
        assertEquals("TEST:0002", altId);
        // roundtrip back to OWL, check that comment is still there
        OWLAPIObo2Owl obo2owl = new OWLAPIObo2Owl(
            OWLManager.createOWLOntologyManager());
        OWLOntology roundTripped = obo2owl.convert(oboDoc);
        Set<OWLAnnotationAssertionAxiom> annotations = roundTripped
            .getAnnotationAssertionAxioms(p2.getIRI());
        assertEquals(4, annotations.size()); // three for the alt-id plus one
        // for the comment
        String comment = null;
        for (OWLAnnotationAssertionAxiom ax : annotations) {
            if (ax.getProperty().isComment()) {
                Optional<OWLLiteral> asLiteral = ax.getValue().asLiteral();
                if (asLiteral.isPresent()) {
                    comment = asLiteral.get().getLiteral();
                }
            }
        }
        assertEquals("Comment", comment);
    }

    private void addLabelAndId(OWLNamedObject obj, String label, String id,
        OWLOntology o) {
        OWLDataFactory f = o.getOWLOntologyManager().getOWLDataFactory();
        addAnnotation(obj, f.getRDFSLabel(), f.getOWLLiteral(label), o);
        OWLAnnotationProperty idProp = f.getOWLAnnotationProperty(
            OWLAPIObo2Owl.trTagToIRI(OboFormatTag.TAG_ID.getTag()));
        addAnnotation(obj, idProp, f.getOWLLiteral(id), o);
    }

    private void setAltId(OWLNamedObject obj, OWLOntology o) {
        OWLDataFactory f = o.getOWLOntologyManager().getOWLDataFactory();
        addAnnotation(obj,
            f.getOWLAnnotationProperty(Obo2OWLVocabulary.IRI_IAO_0100001.iri),
            f.getOWLLiteral("TEST:0001"), o);
        addAnnotation(obj,
            f.getOWLAnnotationProperty(Obo2OWLConstants.IRI_IAO_0000231),
            Obo2OWLConstants.IRI_IAO_0000227, o);
        addAnnotation(obj, f.getOWLDeprecated(), f.getOWLLiteral(true), o);
    }

    private void addAnnotation(OWLNamedObject obj, OWLAnnotationProperty p,
        OWLAnnotationValue v, OWLOntology ont) {
        ont.addAxiom(df.getOWLAnnotationAssertionAxiom(obj.getIRI(),
            df.getOWLAnnotation(p, v)));
    }
}
