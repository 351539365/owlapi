package org.coode.owlapi.rdfxml.parser;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
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
 * Date: 10-Dec-2006<br><br>
 */
public class GTPAnnotationResourceTripleHandler extends AbstractResourceTripleHandler {

    public GTPAnnotationResourceTripleHandler(OWLRDFConsumer consumer) {
        super(consumer);
    }


    public boolean canHandleStreaming(IRI subject, IRI predicate, IRI object) {
        return !isAnonymous(subject) &&  !isAnonymous(object) && getConsumer().isAnnotationProperty(predicate);
    }


    public boolean canHandle(IRI subject, IRI predicate, IRI object) {
        boolean builtInAnnotationProperty = OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS.contains(predicate);
        return !getConsumer().isAxiom(subject) && !getConsumer().isAnnotation(subject) && (builtInAnnotationProperty || !predicate.isReservedVocabulary());
    }


    public void handleTriple(IRI subject, IRI predicate, IRI object) throws UnloadableImportException {
        OWLAnnotationValue value;
        if (isAnonymous(object)) {
            value = getDataFactory().getOWLAnonymousIndividual(object.toString());
        }
        else {
            value = object;
        }
        OWLAnnotationProperty prop = getDataFactory().getOWLAnnotationProperty(predicate);
        OWLAnnotation anno = getDataFactory().getOWLAnnotation(prop, value);
        OWLAnnotationSubject annoSubject;
        if(isAnonymous(subject)) {
            annoSubject = getDataFactory().getOWLAnonymousIndividual(subject.toString());
        }
        else {
            annoSubject = subject;
        }

        if (getConsumer().isOntology(subject)) {
            // Assume we annotation our ontology?
            getConsumer().addOntologyAnnotation(anno);
        }
        else {
            OWLAxiom decAx = getDataFactory().getOWLAnnotationAssertionAxiom(annoSubject, anno, getPendingAnnotations());
            addAxiom(decAx);
        }
    }


}
