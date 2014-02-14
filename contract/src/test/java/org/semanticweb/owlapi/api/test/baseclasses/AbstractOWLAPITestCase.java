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
package org.semanticweb.owlapi.api.test.baseclasses;

import static org.junit.Assert.*;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.api.test.anonymous.AnonymousIndividualsNormaliser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.PrefixOWLOntologyFormat;
import org.semanticweb.owlapi.formats.RDFOntologyFormat;
import org.semanticweb.owlapi.formats.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLRuntimeException;

/** @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics
 *         Group, Date: 10-May-2008 */
@SuppressWarnings("javadoc")
public abstract class AbstractOWLAPITestCase {
    protected OWLDataFactory df = OWLManager.getOWLDataFactory();
    protected OWLOntologyManager m = OWLManager.createOWLOntologyManager();
    protected OWLOntologyManager m1 = OWLManager.createOWLOntologyManager();

    public boolean equal(OWLOntology ont1, OWLOntology ont2) {
        if (!ont1.isAnonymous() && !ont2.isAnonymous()) {
            assertEquals("Ontologies supposed to be the same",
                    ont1.getOntologyID(), ont2.getOntologyID());
        }
        assertEquals("Annotations supposed to be the same",
                ont1.getAnnotations(), ont2.getAnnotations());
        Set<OWLAxiom> axioms1 = ont1.getAxioms();
        Set<OWLAxiom> axioms2 = ont2.getAxioms();
        // This isn't great - we normalise axioms by changing the ids of
        // individuals. This relies on the fact that
        // we iterate over objects in the same order for the same set of axioms!
        AnonymousIndividualsNormaliser normaliser1 = new AnonymousIndividualsNormaliser(
                df);
        axioms1 = normaliser1.getNormalisedAxioms(axioms1);
        AnonymousIndividualsNormaliser normaliser2 = new AnonymousIndividualsNormaliser(
                df);
        axioms2 = normaliser2.getNormalisedAxioms(axioms2);
        if (!axioms1.equals(axioms2)) {
            int counter = 0;
            StringBuilder sb = new StringBuilder();
            Set<OWLAxiom> leftOnly = new HashSet<OWLAxiom>();
            Set<OWLAxiom> rightOnly = new HashSet<OWLAxiom>();
            for (OWLAxiom ax : axioms1) {
                if (!axioms2.contains(ax)) {
                    if (!isIgnorableAxiom(ax, false)) {
                        leftOnly.add(ax);
                        sb.append("Rem axiom: ");
                        sb.append(ax);
                        sb.append("\n");
                        counter++;
                    }
                }
            }
            for (OWLAxiom ax : axioms2) {
                if (!axioms1.contains(ax)) {
                    if (!isIgnorableAxiom(ax, true)) {
                        rightOnly.add(ax);
                        sb.append("Add axiom: ");
                        sb.append(ax);
                        sb.append("\n");
                        counter++;
                    }
                }
            }
            if (counter > 0) {
                // a test fails on OpenJDK implementations because of ordering
                // testing here if blank node ids are the only difference
                boolean fixed = !verifyErrorIsDueToBlankNodesId(leftOnly,
                        rightOnly);
                if (fixed) {
                    new RuntimeException().printStackTrace(System.out);
                    String x = this.getClass().getSimpleName()
                            + " roundTripOntology() Failing to match axioms: "
                            + sb.toString();
                    System.out.println(x);
                    fail(x);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        assertEquals(axioms1, axioms2);
        return true;
    }

    /** @param leftOnly
     * @param rightOnly
     * @return */
    public static boolean verifyErrorIsDueToBlankNodesId(
            Set<OWLAxiom> leftOnly, Set<OWLAxiom> rightOnly) {
        Set<String> leftOnlyStrings = new HashSet<String>();
        Set<String> rightOnlyStrings = new HashSet<String>();
        for (OWLAxiom ax : leftOnly) {
            leftOnlyStrings.add(ax.toString()
                    .replaceAll("_:anon-ind-[0-9]+", "blank")
                    .replaceAll("_:genid[0-9]+", "blank"));
        }
        for (OWLAxiom ax : rightOnly) {
            rightOnlyStrings.add(ax.toString()
                    .replaceAll("_:anon-ind-[0-9]+", "blank")
                    .replaceAll("_:genid[0-9]+", "blank"));
        }
        return rightOnlyStrings.equals(leftOnlyStrings);
    }

    /** ignore declarations of builtins and of named individuals - named
     * individuals do not /need/ a declaration, but addiong one is not an error.
     * 
     * @param parse
     *            true if the axiom belongs to the parsed ones, false for the
     *            input
     * @return true if the axiom can be ignored */
    public boolean isIgnorableAxiom(OWLAxiom ax, boolean parse) {
        if (ax instanceof OWLDeclarationAxiom) {
            OWLDeclarationAxiom d = (OWLDeclarationAxiom) ax;
            if (parse) {
                // all extra declarations in the parsed ontology are fine
                return true;
            }
            // declarations of builtin and named individuals can be ignored
            return d.getEntity().isBuiltIn()
                    || d.getEntity().isOWLNamedIndividual();
        }
        return false;
    }

    private String uriBase = "http://www.semanticweb.org/owlapi/test";

    public OWLOntologyManager getManager() {
        return m;
    }

    public OWLOntology getOWLOntology(String name) {
        try {
            IRI iri = IRI(uriBase + "/" + name);
            if (m.contains(iri)) {
                return m.getOntology(iri);
            } else {
                return m.createOntology(iri);
            }
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
    }

    public OWLOntology loadOntology(String fileName) {
        try {
            URL url = getClass().getResource("/" + fileName);
            return m.loadOntologyFromOntologyDocument(
                    new IRIDocumentSource(IRI.create(url)),
                    new OWLOntologyLoaderConfiguration()
                            .setReportStackTraces(true));
        } catch (OWLOntologyCreationException e) {
            fail(e.getMessage());
            throw new OWLRuntimeException(e);
        }
    }

    public IRI getIRI(String name) {
        return IRI(uriBase + "#" + name);
    }

    public void addAxiom(OWLOntology ont, OWLAxiom ax) {
        m.addAxiom(ont, ax);
    }

    public void roundTripOntology(OWLOntology ont)
            throws OWLOntologyStorageException, OWLOntologyCreationException {
        roundTripOntology(ont, new RDFXMLOntologyFormat());
    }

    /** Saves the specified ontology in the specified format and reloads it.
     * Calling this method from a test will cause the test to fail if the
     * ontology could not be stored, could not be reloaded, or was reloaded and
     * the reloaded version is not equal (in terms of ontology URI and axioms)
     * with the original.
     * 
     * @param ont
     *            The ontology to be round tripped.
     * @param format
     *            The format to use when doing the round trip. */
    public OWLOntology roundTripOntology(OWLOntology ont,
            OWLOntologyFormat format) throws OWLOntologyStorageException,
            OWLOntologyCreationException {
        StringDocumentTarget target = new StringDocumentTarget();
        OWLOntologyFormat fromFormat = m.getOntologyFormat(ont);
        if (fromFormat instanceof PrefixOWLOntologyFormat
                && format instanceof PrefixOWLOntologyFormat) {
            PrefixOWLOntologyFormat fromPrefixFormat = (PrefixOWLOntologyFormat) fromFormat;
            PrefixOWLOntologyFormat toPrefixFormat = (PrefixOWLOntologyFormat) format;
            toPrefixFormat.copyPrefixesFrom(fromPrefixFormat);
        }
        if (format instanceof RDFOntologyFormat) {
            ((RDFOntologyFormat) format).setAddMissingTypes(false);
        }
        m.saveOntology(ont, format, target);
        handleSaved(target, format);
        OWLOntology ont2 = OWLManager.createOWLOntologyManager()
                .loadOntologyFromOntologyDocument(
                        new StringDocumentSource(target),
                        new OWLOntologyLoaderConfiguration()
                                .setReportStackTraces(true));
        equal(ont, ont2);
        return ont2;
    }

    @Test
    public void checkVerify() {
        OWLDataProperty t = df.getOWLDataProperty(IRI.create("urn:test#t"));
        Set<OWLAxiom> ax1 = new HashSet<OWLAxiom>();
        ax1.add(df.getOWLDataPropertyAssertionAxiom(t,
                df.getOWLAnonymousIndividual(), df.getOWLLiteral("test1")));
        ax1.add(df.getOWLDataPropertyAssertionAxiom(t,
                df.getOWLAnonymousIndividual(), df.getOWLLiteral("test2")));
        Set<OWLAxiom> ax2 = new HashSet<OWLAxiom>();
        ax2.add(df.getOWLDataPropertyAssertionAxiom(t,
                df.getOWLAnonymousIndividual(), df.getOWLLiteral("test1")));
        ax2.add(df.getOWLDataPropertyAssertionAxiom(t,
                df.getOWLAnonymousIndividual(), df.getOWLLiteral("test2")));
        assertFalse(ax1.equals(ax2));
        assertTrue(AbstractOWLAPITestCase.verifyErrorIsDueToBlankNodesId(ax1,
                ax2));
    }

    @SuppressWarnings("unused")
    protected boolean isIgnoreDeclarationAxioms(OWLOntologyFormat format) {
        return true;
    }

    @SuppressWarnings("unused")
    protected void handleSaved(StringDocumentTarget target,
            OWLOntologyFormat format) {
        // System.out.println(target.toString());
    }

    protected OWLOntology loadOntologyFromString(String input)
            throws OWLOntologyCreationException {
        OWLOntology ontology = OWLManager.createOWLOntologyManager()
                .loadOntologyFromOntologyDocument(
                        new StringDocumentSource(input));
        return ontology;
    }

    protected OWLOntology loadOntologyFromString(StringDocumentSource input)
            throws OWLOntologyCreationException {
        OWLOntology ontology = OWLManager.createOWLOntologyManager()
                .loadOntologyFromOntologyDocument(input);
        return ontology;
    }

    protected OWLOntology loadOntologyFromString(StringDocumentTarget input)
            throws OWLOntologyCreationException {
        OWLOntology ontology = OWLManager.createOWLOntologyManager()
                .loadOntologyFromOntologyDocument(
                        new StringDocumentSource(input));
        return ontology;
    }

    protected OWLOntology loadOntologyStrict(String o)
            throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
        config.setStrict(true);
        return manager.loadOntologyFromOntologyDocument(
                new StringDocumentSource(o), config);
    }
}
