/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
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
 * Copyright 2011, University of Manchester
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
package org.semanticweb.owlapi.api.test.syntax;

import static org.junit.Assert.*;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

import java.net.URISyntaxException;

import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.junit.Test;
import org.semanticweb.owlapi.api.test.Factory;
import org.semanticweb.owlapi.api.test.baseclasses.AbstractFileRoundTrippingTestCase;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/** Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 19-Aug-2010 */
public class TurtleSharedBlankNodeTestCase extends AbstractFileRoundTrippingTestCase {
    @Override
    protected String getFileName() {
        return "annotatedpropertychain.ttl.rdf";
    }

    @Test
    public void testLoadingUTF8BOM() throws URISyntaxException,
            OWLOntologyCreationException {
        IRI uri = IRI.create(getClass().getResource("/ttl-with-bom.ttl").toURI());
        Factory.getManager().loadOntologyFromOntologyDocument(uri);
    }

    @Test
    public void shouldParseOntologyThatworked() throws OWLOntologyCreationException {
        // given
        String working = "@prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#> .\n"
                + "@prefix foaf:    <http://xmlns.com/foaf/0.1/> .\n"
                + "foaf:fundedBy rdfs:isDefinedBy <http://xmlns.com/foaf/0.1/> .";
        OWLDataFactory df = Factory.getFactory();
        OWLAxiom expected = AnnotationAssertion(df.getRDFSIsDefinedBy(),
                IRI("http://xmlns.com/foaf/0.1/fundedBy"),
                IRI("http://xmlns.com/foaf/0.1/"));
        // when
        OWLOntology o = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(working));
        // then
        assertTrue(o.getAxioms().contains(expected));
    }

    @Test
    public void shouldParseOntologyThatBroke() throws OWLOntologyCreationException {
        // given
        String input = "@prefix f:    <urn:test/> . f:r f:p f: .";
        OWLDataFactory df = Factory.getFactory();
        OWLAxiom expected = df.getOWLAnnotationAssertionAxiom(
                df.getOWLAnnotationProperty(IRI("urn:test/p")), IRI("urn:test/r"),
                IRI("urn:test/"));
        // when
        OWLOntology o = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(input));
        // then
        assertTrue(o.getAxioms().contains(expected));
    }

    // test for 3309666
    @Test
    public void shouldResolveAgainstBase() throws OWLOntologyCreationException {
        // given
        String input = "@base <http://test.org/path#> .\n" + "<a1> <b1> <c1> .";
        // when
        OWLOntology o = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(input));
        // then
        String axioms = o.getAxioms().toString();
        assertTrue(axioms.contains("http://test.org/a1"));
        assertTrue(axioms.contains("http://test.org/b1"));
        assertTrue(axioms.contains("http://test.org/c1"));
    }

    // test for 3543488
    @Test
    public void shouldRoundTripTurtleWithsharedBnodes()
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        String input = "@prefix ex: <http://example.com/test> .\n"
                + "ex:ex1 a ex:Something ; ex:prop1 _:a .\n"
                + "_:a a ex:Something1 ; ex:prop2 _:b .\n"
                + "_:b a ex:Something ; ex:prop3 _:a .";
        OWLOntology ontology = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(input));
        StringDocumentTarget t = new StringDocumentTarget();
        TurtleOntologyFormat format = new TurtleOntologyFormat();
        ontology.getOWLOntologyManager().saveOntology(ontology, format, t);
        String onto1 = t.toString();
        ontology = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(t.toString()));
        t = new StringDocumentTarget();
        format = new TurtleOntologyFormat();
        ontology.getOWLOntologyManager().saveOntology(ontology, format, t);
        String onto2 = t.toString();
        assertEquals(onto1, onto2);
    }

    // test for 335
    @Test
    public void shouldParseScientificNotation() throws OWLOntologyCreationException,
            OWLOntologyStorageException {
        String input = "<http://dbpedia.org/resource/South_Africa> <http://dbpedia.org/ontology/areaTotal> 1e+07 .";
        OWLOntology ontology = Factory.getManager().loadOntologyFromOntologyDocument(
                new StringDocumentSource(input));
        StringDocumentTarget t = new StringDocumentTarget();
        TurtleOntologyFormat format = new TurtleOntologyFormat();
        ontology.getOWLOntologyManager().saveOntology(ontology, format, t);
        String onto1 = t.toString();
        System.out
                .println("TurtleSharedBlankNodeTestCase.shouldParseScientificNotation() "
                        + onto1);
        // ontology = Factory.getManager().loadOntologyFromOntologyDocument(
        // new StringDocumentSource(t.toString()));
        // t = new StringDocumentTarget();
        // format = new TurtleOntologyFormat();
        // ontology.getOWLOntologyManager().saveOntology(ontology, format, t);
        // String onto2 = t.toString();
        // assertEquals(onto1, onto2);
    }
}
