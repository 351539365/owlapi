/*
 * This file is part of the OWL API.
 * 
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * 
 * Copyright (C) 2014, Commonwealth Scientific and Industrial Research Organisation
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see http://www.gnu.org/licenses/.
 * 
 * 
 * Alternatively, the contents of this file may be used under the terms of the Apache License,
 * Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable
 * instead of those above.
 * 
 * Copyright 2014, Commonwealth Scientific and Industrial Research Organisation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.semanticweb.owlapi.rio;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.openrdf.model.ValueFactory;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.helpers.RDFParserBase;
import org.semanticweb.owlapi.OWLAPIServiceLoaderModule;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.ReaderDocumentSource;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLRuntimeException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * Parses {@link OWLAPIRDFFormat} parsers straight to Sesame {@link RDFHandler}
 * s.
 * 
 * @author Peter Ansell p_ansell@yahoo.com
 */
public class RioOWLRDFParser extends RDFParserBase {

    // XXX not entirely sure if this is the best location to create an injector;
    // it could be better in the factory, or in the code creating the factory.
    private static final Injector injector = Guice
            .createInjector(new OWLAPIServiceLoaderModule());
    private OWLAPIRDFFormat owlFormat;

    public RioOWLRDFParser(OWLAPIRDFFormat owlFormat) {
        super();
        this.owlFormat = owlFormat;
    }

    /**
     * @param valueFactory
     */
    public RioOWLRDFParser(OWLAPIRDFFormat owlFormat, ValueFactory valueFactory) {
        super(valueFactory);
        this.owlFormat = owlFormat;
    }

    @Override
    public OWLAPIRDFFormat getRDFFormat() {
        return owlFormat;
    }

    @Override
    public void parse(InputStream in, String baseURI) throws IOException,
            RDFParseException, RDFHandlerException {
        OWLOntologyFormat nextFormat = getRDFFormat().getOWLFormat();
        StreamDocumentSource source = new StreamDocumentSource(in,
                IRI.create(baseURI), nextFormat, getRDFFormat()
                        .getDefaultMIMEType());
        render(source);
    }

    /**
     * @param source
     * @throws IOException
     */
    protected void render(OWLOntologyDocumentSource source) throws IOException {
        try {
            Provider<OWLOntologyManager> managerProvider = injector
                    .getProvider(OWLOntologyManager.class);
            OWLOntology ontology = managerProvider.get()
                    .loadOntologyFromOntologyDocument(source);
            new RioRenderer(ontology, getRDFHandler(), getRDFFormat()
                    .getOWLFormat()).render();
        } catch (OWLOntologyCreationException e) {
            throw new OWLRuntimeException(e);
        }
    }

    @Override
    public void parse(Reader reader, String baseURI) throws IOException,
            RDFParseException, RDFHandlerException {
        OWLOntologyFormat nextFormat = getRDFFormat().getOWLFormat();
        ReaderDocumentSource source = new ReaderDocumentSource(reader,
                IRI.create(baseURI), nextFormat, getRDFFormat()
                        .getDefaultMIMEType());
        render(source);
    }
}
