package org.coode.owl.rdfxml.parser.tests;

import junit.framework.TestCase;
import org.coode.owl.rdfxml.parser.RDFXMLParserFactory;
import org.semanticweb.owl.io.OWLParserFactoryRegistry;
import org.semanticweb.owl.model.*;
import uk.ac.manchester.cs.owl.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.OWLOntologyManagerImpl;
import uk.ac.manchester.cs.owl.ParsableOWLOntologyFactory;

import java.net.URI;
/*
 * Copyright (C) 2007, University of Manchester
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
 * Date: 08-Aug-2007<br><br>
 */
public class FailedImportsTestCase extends TestCase {

    public void testImports() throws Exception {
        OWLDataFactory dataFactory = new OWLDataFactoryImpl();
        OWLOntologyManager manager = new OWLOntologyManagerImpl(dataFactory);
        OWLParserFactoryRegistry.getInstance().registerParserFactory(new RDFXMLParserFactory());
        manager.addOntologyFactory(new ParsableOWLOntologyFactory());
        URI a = getClass().getResource("/owlapi/A.owl").toURI();
        final URI b = getClass().getResource("/owlapi/B.owl").toURI();

        manager.addURIMapper(new OWLOntologyDocumentMapper() {

            private URI ontBURI = URI.create("http://www.semanticweb.org/ontologies/2007/7/A.owl");

            public URI getDocumentIRI(URI ontologyURI) {
                if (ontologyURI.equals(ontBURI)) {
                    return b;
                } else {
                    return ontologyURI;
                }
            }
        });
        // B imports A

        OWLOntology o;
        try {
            o = manager.loadOntologyFromPhysicalURI(b);
            assertTrue(o.getImports(manager).isEmpty());
        }
        catch (OWLOntologyCreationException e) {
            System.out.println("Failed to load ontology");
        }


        assertEquals(1, manager.getOntologies().size());

        manager.loadOntologyFromPhysicalURI(a);

        for (OWLOntology ont : manager.getOntologies()) {
            System.out.println("ont = " + ont);
            System.out.println("classes = " + ont.getReferencedClasses());
        }

        assertEquals(2, manager.getOntologies().size());
    }
}
