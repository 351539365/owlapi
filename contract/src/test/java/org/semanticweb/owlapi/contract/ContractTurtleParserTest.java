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
package org.semanticweb.owlapi.contract;

import static org.mockito.Mockito.mock;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;

import org.junit.Test;
import org.semanticweb.owlapi.formats.RDFOntologyFormat;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.xml.sax.SAXException;

import uk.ac.manchester.cs.owl.owlapi.turtle.parser.ConsoleTripleHandler;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.NullTripleHandler;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.OWLRDFConsumerAdapter;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.TripleHandler;

@SuppressWarnings({ "unused", "javadoc" })
public class ContractTurtleParserTest {
    private static final String URN_A_FAKE = "urn:aFake";

    public void shouldTestConsoleTripleHandler() throws OWLException {
        ConsoleTripleHandler testSubject0 = new ConsoleTripleHandler();
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE),
                IRI(URN_A_FAKE));
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "");
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "", "");
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "",
                IRI(URN_A_FAKE));
        testSubject0.handlePrefixDirective("", "");
        testSubject0.handleBaseDirective("");
        testSubject0.handleComment("");
        testSubject0.handleEnd();
    }

    @Test
    public void shouldTestNullTripleHandler() throws OWLException {
        NullTripleHandler testSubject0 = new NullTripleHandler();
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE),
                IRI(URN_A_FAKE));
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "");
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "", "");
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "",
                IRI(URN_A_FAKE));
        testSubject0.handlePrefixDirective("", "");
        testSubject0.handleBaseDirective("");
        testSubject0.handleComment("");
        testSubject0.handleEnd();
    }

    @Test
    public void shouldTestOWLRDFConsumerAdapter() throws OWLException,
            SAXException {
        OWLRDFConsumerAdapter testSubject0 = new OWLRDFConsumerAdapter(
                Utils.getMockOntology(), new OWLOntologyLoaderConfiguration());
        new OWLRDFConsumerAdapter(Utils.getMockOntology(),
                new OWLOntologyLoaderConfiguration());
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE),
                IRI(URN_A_FAKE));
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "");
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "", "");
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "",
                IRI(URN_A_FAKE));
        testSubject0.handlePrefixDirective("", "");
        testSubject0.handleBaseDirective("");
        testSubject0.handleComment("");
        testSubject0.handleEnd();
        testSubject0.setOntologyFormat(mock(RDFOntologyFormat.class));
        testSubject0.startModel("");
        testSubject0.addModelAttribte("", "");
        testSubject0.includeModel("", "");
        testSubject0.logicalURI("");
        testSubject0.statementWithLiteralValue("", "", "", "", "");
        testSubject0.statementWithResourceValue("", "", "");
    }

    @Test
    public void shouldTestInterfaceTripleHandler() throws OWLException {
        TripleHandler testSubject0 = mock(TripleHandler.class);
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "");
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "",
                IRI(URN_A_FAKE));
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE), "", "");
        testSubject0.handleTriple(IRI(URN_A_FAKE), IRI(URN_A_FAKE),
                IRI(URN_A_FAKE));
        testSubject0.handlePrefixDirective("", "");
        testSubject0.handleBaseDirective("");
        testSubject0.handleComment("");
        testSubject0.handleEnd();
    }
}
