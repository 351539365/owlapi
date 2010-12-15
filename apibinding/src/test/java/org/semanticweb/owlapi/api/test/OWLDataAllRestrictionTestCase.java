package org.semanticweb.owlapi.api.test;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLRestriction;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group
 * Date: 25-Oct-2006
 */
public class OWLDataAllRestrictionTestCase extends AbstractOWLRestrictionWithFillerTestCase<OWLDataProperty, OWLDataRange> {

    @Override
	protected OWLRestriction createRestriction(OWLDataProperty prop, OWLDataRange filler) throws Exception {
        return getFactory().getOWLDataAllValuesFrom(prop, filler);
    }


    @Override
	protected OWLDataProperty createProperty() throws OWLException {
        return createOWLDataProperty();
    }


    @Override
	protected OWLDataRange createFiller() throws OWLException {
        return createOWLDatatype();
    }
}
