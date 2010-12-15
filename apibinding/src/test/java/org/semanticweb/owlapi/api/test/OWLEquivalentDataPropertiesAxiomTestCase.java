package org.semanticweb.owlapi.api.test;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObject;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group
 * Date: 25-Oct-2006
 */
public class OWLEquivalentDataPropertiesAxiomTestCase extends AbstractOWLNaryOperandsObjectTestCase<OWLDataProperty> {


    @Override
	protected OWLObject createObject(Set<OWLDataProperty> operands) throws Exception {
        return getFactory().getOWLEquivalentDataPropertiesAxiom(operands);
    }


    @Override
	protected OWLDataProperty createOperand() throws Exception {
        return createOWLDataProperty();
    }
}
