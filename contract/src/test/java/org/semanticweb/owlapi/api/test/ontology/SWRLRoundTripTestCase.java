/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2014, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2014, The University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.semanticweb.owlapi.api.test.ontology;

import static org.junit.Assert.assertEquals;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

import java.util.Set;
import java.util.TreeSet;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.junit.Test;
import org.semanticweb.owlapi.api.test.Factory;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

@SuppressWarnings("javadoc")
public class SWRLRoundTripTestCase {
    // test for 3562978
    @Test
    public void shouldDoCompleteRoundtrip()
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        String NS = "urn:test";
        OWLDataFactory factory = Factory.getFactory();
        OWLClass A = Class(IRI(NS + "#A"));
        OWLDataProperty P = factory.getOWLDataProperty(IRI(NS + "#P"));
        SWRLVariable X = factory.getSWRLVariable(IRI(NS + "#X"));
        SWRLVariable Y = factory.getSWRLVariable(IRI(NS + "#Y"));
        OWLOntology ontology = Factory.getManager().createOntology(IRI(NS));
        Set<SWRLAtom> body = new TreeSet<SWRLAtom>();
        body.add(factory.getSWRLDataPropertyAtom(P, X, Y));
        body.add(factory.getSWRLDataRangeAtom(
                factory.getOWLDatatype(XSDVocabulary.STRING.getIRI()), Y));
        Set<SWRLAtom> head = new TreeSet<SWRLAtom>();
        head.add(factory.getSWRLClassAtom(A, X));
        SWRLRule rule = factory.getSWRLRule(body, head);
        ontology.getOWLOntologyManager().addAxiom(ontology, rule);
        StringDocumentTarget t = new StringDocumentTarget();
        OWLXMLOntologyFormat format = new OWLXMLOntologyFormat();
        ontology.getOWLOntologyManager().saveOntology(ontology, format, t);
        String onto1 = t.toString();
        ontology = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(t.toString()));
        t = new StringDocumentTarget();
        format = new OWLXMLOntologyFormat();
        ontology.getOWLOntologyManager().saveOntology(ontology, format, t);
        String onto2 = t.toString();
        assertEquals(onto1, onto2);
    }

    @Test
    public void shouldDoCompleteRoundtripManchesterOWLSyntax()
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        String NS = "urn:test";
        OWLDataFactory factory = Factory.getFactory();
        OWLClass A = Class(IRI(NS + "#A"));
        OWLDataProperty P = factory.getOWLDataProperty(IRI(NS + "#P"));
        SWRLVariable X = factory.getSWRLVariable(IRI(NS + "#X"));
        SWRLVariable Y = factory.getSWRLVariable(IRI(NS + "#Y"));
        OWLOntology ontology = Factory.getManager().createOntology(IRI(NS));
        Set<SWRLAtom> body = new TreeSet<SWRLAtom>();
        body.add(factory.getSWRLDataPropertyAtom(P, X, Y));
        body.add(factory.getSWRLDataRangeAtom(
                factory.getOWLDatatype(XSDVocabulary.STRING.getIRI()), Y));
        Set<SWRLAtom> head = new TreeSet<SWRLAtom>();
        head.add(factory.getSWRLClassAtom(A, X));
        SWRLRule rule = factory.getSWRLRule(body, head);
        ontology.getOWLOntologyManager().addAxiom(ontology, rule);
        StringDocumentTarget t = new StringDocumentTarget();
        ManchesterOWLSyntaxOntologyFormat format = new ManchesterOWLSyntaxOntologyFormat();
        ontology.getOWLOntologyManager().saveOntology(ontology, format, t);
        String onto1 = t.toString();
        ontology = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(onto1));
        t = new StringDocumentTarget();
        format = new ManchesterOWLSyntaxOntologyFormat();
        ontology.getOWLOntologyManager().saveOntology(ontology, format, t);
        String onto2 = t.toString();
        assertEquals(onto1, onto2);
    }
}
