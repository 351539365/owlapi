package org.semanticweb.owlapi.api.test;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * Date: 28-May-2009
 */
public abstract class AbstractAxiomsRoundTrippingTestCase extends AbstractRoundTrippingTest {

    private Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();


    @Override
	final protected OWLOntology createOntology() {
            OWLOntology ont = getOWLOntology("Ont");
            axioms.clear();
            axioms.addAll(createAxioms());
            getManager().addAxioms(ont, axioms);
            return ont;
    }

    @Override
	public void testRDFXML() throws Exception {
        super.testRDFXML();
    }

    @Override
	public void testOWLXML() throws Exception {
        super.testOWLXML();
    }

    @Override
	public void testFunctionalSyntax() throws Exception {
        super.testFunctionalSyntax();
    }

    @Override
	public void testTurtle() throws Exception {
        super.testTurtle();
    }

    @Override
	public void testManchesterOWLSyntax() throws Exception {
        super.testManchesterOWLSyntax();
    }

    protected abstract Set<? extends OWLAxiom> createAxioms();
}
