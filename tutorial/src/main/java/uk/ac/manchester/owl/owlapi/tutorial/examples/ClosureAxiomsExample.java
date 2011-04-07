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

package uk.ac.manchester.owl.owlapi.tutorial.examples;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.owl.owlapi.tutorial.ClosureAxioms;

import java.net.URI;

/**
 * <p>This class demonstrates some aspects of the OWL API. It expects three
 * arguments:</p>
 * <ol>
 * <li>The URI of an ontology</li>
 * <p/>
 * <li>The URI of destination</li>
 * <p/>
 * <li>The URI of a class</li>
 * </ol>
 * <p>When executed, the class will find all subclass axioms that form part of the
 * definition of the given class. For each of these, if the superclass is a
 * conjunction of existential restrictions, then an additional subclass axiom
 * will be added to the ontology, "closing" the restrictions.</p>
 * <p/>
 * Author: Sean Bechhofer<br>
 * The University Of Manchester<br>
 * Information Management Group<br>
 * Date: 24-April-2007<br>
 * <br>
 */
public class ClosureAxiomsExample {

    public static void usage() {
        System.out.println("Usage: ClosureAxiomsExample --input=URL --output=URL --class=URL");
    }

    public static void main(String[] args) {
        /* An example illustrating the addition of closure axioms. */


        try {
            LongOpt[] longopts = new LongOpt[11];
            String inputOntology = null;
            String outputOntology = null;
            String classToClose = null;

            longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, '?');
            longopts[1] = new LongOpt("input", LongOpt.REQUIRED_ARGUMENT, null, 'i');
            longopts[2] = new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o');
            longopts[3] = new LongOpt("class", LongOpt.REQUIRED_ARGUMENT, null, 'c');

            Getopt g = new Getopt("", args, "?:i:o:c", longopts);
            int c;

            while ((c = g.getopt()) != -1) {
                switch (c) {
                    case '?':
                        usage();
                        System.exit(0);
                    case 'i':
                        /* input */
                        inputOntology = g.getOptarg();
                        break;
                    case 'o':
                        /* output */
                        outputOntology = g.getOptarg();
                        break;
                    case 'c':
                        /* class */
                        classToClose = g.getOptarg();
                        break;
                }
            }

            /* Create and Ontology Manager */
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            if (inputOntology == null || outputOntology == null || classToClose == null) {
                usage();
                System.exit(1);
            }

            IRI documentIRI = IRI.create(inputOntology);
            IRI classIRI = IRI.create(classToClose);
            IRI outputDocumentIRI = IRI.create(outputOntology);

            /* Load an ontology */
            System.out.println("Loading: " + documentIRI);
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
            System.out.println("Ontology Loaded...");
            System.out.println("Logical URI : " + documentIRI);
            System.out.println("Document IRI: " + ontology.getOntologyID());
            System.out.println("Format      : " + manager.getOntologyFormat(ontology));

            ClosureAxioms closureAxioms = new ClosureAxioms(manager, ontology);

            OWLClass clazz = manager.getOWLDataFactory().getOWLClass(classIRI);
            System.out.println("Class URI   : " + classIRI);
            System.out.println(clazz);

            /* Add the closure axioms */
            closureAxioms.addClosureAxioms(clazz);

            /* Now save a copy to another location */
            System.out.println("Saving: " + outputDocumentIRI);

            manager.saveOntology(ontology, outputDocumentIRI);
            System.out.println("Ontology Saved...");
            System.out.println("Document IRI : " + outputDocumentIRI);

            /* Remove the ontology from the manager */
            manager.removeOntology(ontology);
            System.out.println("Done");
        }
        catch (OWLException e) {
            e.printStackTrace();
        }
    }
}