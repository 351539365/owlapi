package org.semanticweb.owlapi.api.test;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * Date: 25-Nov-2009
 */
public class DisjointClassesAxiomNaryAnnotatedTestCase extends AbstractAnnotatedAxiomRoundTrippingTestCase {

    @Override
    @SuppressWarnings("unused")
	protected OWLAxiom getMainAxiom(Set<OWLAnnotation> annos) {
        return getFactory().getOWLDisjointClassesAxiom(getOWLClass("A"), getOWLClass("B"), getOWLClass("C"), getOWLClass("D"));
    }
}
