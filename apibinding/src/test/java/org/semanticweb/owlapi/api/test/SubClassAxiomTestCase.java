package org.semanticweb.owlapi.api.test;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;


/**
 * Author: Matthew Horridge<br> The University Of Manchester<br> Information Management Group<br> Date:
 * 12-Oct-2008<br><br>
 */
public class SubClassAxiomTestCase extends AbstractFileRoundTrippingTestCase {


    protected String getFileName() {
        return "SubClassOf.rdf";
    }


    public void testCorrectAxioms() {
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        OWLClass clsA = getOWLClass("A");
        OWLClass clsB = getOWLClass("B");
        axioms.add(getFactory().getOWLSubClassOfAxiom(clsA, clsB));
        assertEquals(getOnt().getAxioms(), axioms);
    }


    /**
     * Tests the isGCI method on OWLSubClassAxiom
     */
    public void testIsGCIMethod() {
        OWLClass clsA = getOWLClass("A");
        OWLClass clsB = getOWLClass("B");
        OWLClass clsC = getOWLClass("C");
        OWLClassExpression desc = getFactory().getOWLObjectIntersectionOf(clsA, clsC);
        OWLSubClassOfAxiom ax1 = getFactory().getOWLSubClassOfAxiom(clsA, clsB);
        assertFalse(ax1.isGCI());
        OWLSubClassOfAxiom ax2 = getFactory().getOWLSubClassOfAxiom(desc, clsB);
        assertTrue(ax2.isGCI());

    }
}
