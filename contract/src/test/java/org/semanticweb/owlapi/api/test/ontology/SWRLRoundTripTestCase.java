/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.api.test.ontology;

import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.semanticweb.owlapi.api.test.baseclasses.TestBase;
import org.semanticweb.owlapi.formats.ManchesterOWLSyntaxOntologyFormat;
import org.semanticweb.owlapi.formats.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

@SuppressWarnings("javadoc")
public class SWRLRoundTripTestCase extends TestBase {

    @Test
    public void shouldDoCompleteRoundtrip()
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        String NS = "urn:test";
        OWLClass A = Class(IRI(NS + "#A"));
        OWLDataProperty P = df.getOWLDataProperty(IRI(NS + "#P"));
        SWRLVariable X = df.getSWRLVariable(IRI(NS + "#X"));
        SWRLVariable Y = df.getSWRLVariable(IRI(NS + "#Y"));
        OWLOntology ontology = m.createOntology(IRI(NS));
        Set<SWRLAtom> body = new TreeSet<SWRLAtom>();
        body.add(df.getSWRLDataPropertyAtom(P, X, Y));
        body.add(df.getSWRLDataRangeAtom(
                df.getOWLDatatype(XSDVocabulary.STRING.getIRI()), Y));
        Set<SWRLAtom> head = new TreeSet<SWRLAtom>();
        head.add(df.getSWRLClassAtom(A, X));
        SWRLRule rule = df.getSWRLRule(body, head);
        ontology.getOWLOntologyManager().addAxiom(ontology, rule);
        ontology = roundTrip(ontology, new OWLXMLOntologyFormat());
        OWLOntology onto2 = roundTrip(ontology, new OWLXMLOntologyFormat());
        equal(ontology, onto2);
    }

    @Test
    public void shouldDoCompleteRoundtripManchesterOWLSyntax()
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        String NS = "urn:test";
        OWLClass A = Class(IRI(NS + "#A"));
        OWLDataProperty P = df.getOWLDataProperty(IRI(NS + "#P"));
        SWRLVariable X = df.getSWRLVariable(IRI(NS + "#X"));
        SWRLVariable Y = df.getSWRLVariable(IRI(NS + "#Y"));
        OWLOntology ontology = m.createOntology(IRI(NS));
        Set<SWRLAtom> body = new TreeSet<SWRLAtom>();
        body.add(df.getSWRLDataPropertyAtom(P, X, Y));
        body.add(df.getSWRLDataRangeAtom(
                df.getOWLDatatype(XSDVocabulary.STRING.getIRI()), Y));
        Set<SWRLAtom> head = new TreeSet<SWRLAtom>();
        head.add(df.getSWRLClassAtom(A, X));
        SWRLRule rule = df.getSWRLRule(body, head);
        ontology.getOWLOntologyManager().addAxiom(ontology, rule);
        ontology = roundTrip(ontology, new ManchesterOWLSyntaxOntologyFormat());
        OWLOntology onto2 = roundTrip(ontology,
                new ManchesterOWLSyntaxOntologyFormat());
        equal(ontology, onto2);
    }
}
