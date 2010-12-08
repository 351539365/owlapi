package org.semanticweb.owlapi.api.test;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group
 * Date: 25-Oct-2006
 */
public class OWLDataPropertyDomainAxiomTestCase extends AbstractOWLBinaryOperandAxiomTestCase<OWLDataProperty, OWLClassExpression> {


    protected OWLDataProperty createLeftOperand() throws Exception {
        return createOWLDataProperty();
    }


    protected OWLClassExpression createRightOperand() throws Exception {
        return createOWLClass();
    }


    protected OWLAxiom createAxiom(OWLDataProperty leftOperand, OWLClassExpression rightOperand) throws Exception {
        return getFactory().getOWLDataPropertyDomainAxiom(leftOperand, rightOperand);
    }
}
