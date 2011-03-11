package org.coode.owlapi.rdfxml.parser;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.*;

/*
 * Copyright (C) 2006, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 08-Dec-2006<br><br>
 */
public class ObjectMinCardinalityTranslator extends AbstractClassExpressionTranslator {

    public ObjectMinCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }

    public boolean matchesStrict(IRI mainNode) {
        return isRestrictionStrict(mainNode) && isNonNegativeIntegerStrict(mainNode, OWL_MIN_CARDINALITY) && isObjectPropertyStrict(mainNode, OWL_ON_PROPERTY);
    }

    public boolean matchesLax(IRI mainNode) {
        return isNonNegativeIntegerLax(mainNode, OWL_MIN_CARDINALITY) && isObjectPropertyLax(mainNode, OWL_ON_PROPERTY);
    }

    public OWLObjectMinCardinality translate(IRI mainNode) {
        getConsumer().consumeTriple(mainNode, RDF_TYPE.getIRI(), OWL_RESTRICTION.getIRI());
        int cardi = translateInteger(mainNode, OWL_MIN_CARDINALITY);
        IRI propertyIRI = getConsumer().getResourceObject(mainNode, OWL_ON_PROPERTY, true);
        OWLObjectPropertyExpression property = getConsumer().translateObjectPropertyExpression(propertyIRI);
        IRI fillerIRI = getConsumer().getResourceObject(mainNode, OWL_ON_CLASS, true);
        if (fillerIRI != null && !getConsumer().getConfiguration().isStrict()) {
            // Be tolerant
            OWLClassExpression filler = getConsumer().translateClassExpression(fillerIRI);
            return getDataFactory().getOWLObjectMinCardinality(cardi, property, filler);
        }
        else {
            return getDataFactory().getOWLObjectMinCardinality(cardi, property);
        }
    }
}
