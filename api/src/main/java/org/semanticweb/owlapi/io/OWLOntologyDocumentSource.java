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
package org.semanticweb.owlapi.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;

/**
 * A document source provides a point for loading an ontology. A document source
 * may provide three ways of obtaining an ontology document:
 * <ol>
 * <li>From a {@link java.io.Reader}
 * <li>From an {@link java.io.InputStream}
 * <li>From an ontology document {@link org.semanticweb.owlapi.model.IRI}
 * </ol>
 * Consumers that use a document source will attempt to obtain a concrete
 * representation of an ontology in the above order. <br>
 * Note that while an ontology document source may appear similar to a SAX input
 * source, an important difference is that the getReader and getInputStream
 * methods return new instances each time the method is called. This allows
 * multiple attempts at loading an ontology.
 * 
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group
 * @since 2.0.0
 */
public interface OWLOntologyDocumentSource {

    /**
     * Select the available input source and, if it is not already a Reader,
     * wrap it in a Reader. This method removes the duplication of code required
     * for each caller to figure out if a reader or an inputstream is available.
     * The returned Reader will be buffered.
     * 
     * @param configuration
     *        loader configuration to use of the reader must be built form the
     *        input IRI
     * @param encoding
     *        character encoding if a new Reader needs to be created.
     * @return A Reader for the input; if no Reader can be obtained, an
     *         OWLOntologyInputSourceException is thrown.
     * @throws OWLOntologyInputSourceException
     *         if an IO related exception is thrown.
     */
    default Reader wrapInputAsReader(
            OWLOntologyLoaderConfiguration configuration, Charset encoding)
            throws OWLOntologyInputSourceException {
        Optional<Reader> reader = getReader();
        if (reader.isPresent()) {
            return new BufferedReader(reader.get());
        }
        Optional<InputStream> input = getInputStream();
        if (input.isPresent()) {
            return new BufferedReader(new InputStreamReader(input.get(),
                    encoding));
        }
        Optional<InputStream> in = DocumentSourceUtils.getInputStream(
                getDocumentIRI(), configuration);
        if (in.isPresent()) {
            return new BufferedReader(new InputStreamReader(in.get(), encoding));
        }
        throw new OWLOntologyInputSourceException(
                "No input reader can be found");
    }

    /**
     * Call #wrapwrapInputAsReader(OWLOntologyLoaderConfiguration, String) with
     * UTF-* as default encoding.
     * 
     * @param configuration
     *        loader configuration to use of the reader must be built form the
     *        input IRI
     * @return A Reader wrapped in an Optional; if no Reader can be obtained,
     *         the result is Optional.absent. @throws
     *         OWLOntologyInputSourceException if an IO related exception is
     *         thrown.
     * @throws OWLOntologyInputSourceException
     *         if an IO related exception is thrown.
     */
    default Reader wrapInputAsReader(
            OWLOntologyLoaderConfiguration configuration)
            throws OWLOntologyInputSourceException {
        return wrapInputAsReader(configuration, Charsets.UTF_8);
    }

    /**
     * Gets a reader which an ontology document can be read from. This method
     * may be called multiple times. Each invocation will return a new
     * {@code Reader}. If there is no reader stream available, returns
     * Optional.absent.
     * 
     * @return A new {@code Reader} which the ontology can be read from, wrapped
     *         in an Optional.
     */
    default Optional<Reader> getReader() {
        return Optional.absent();
    }

    /**
     * If an input stream can be obtained from this document source then this
     * method creates it. This method may be called multiple times. Each
     * invocation will return a new input stream. If there is no input stream
     * available, returns Optional.absent. .
     * 
     * @return A new input stream which the ontology can be read from, wrapped
     *         in an Optional.
     */
    @Nonnull
    default Optional<InputStream> getInputStream() {
        return Optional.absent();
    }

    /**
     * Gets the IRI of the ontology document.
     * 
     * @return An IRI which represents the ontology document IRI
     */
    @Nonnull
    IRI getDocumentIRI();

    /**
     * @return format for the ontology. If none is known, return
     *         Optional.absent.
     */
    @Nullable
    default Optional<OWLDocumentFormat> getFormat() {
        return Optional.absent();
    }

    /**
     * @return MIME type for this source, if one is specified. If none is known,
     *         return Optional.absent.
     */
    default Optional<String> getMIMEType() {
        return Optional.absent();
    }

    /**
     * @return true if at least one of input sream or reader is available for
     *         this source, and no IOExceptions have happened when trying to
     *         read from them.
     */
    boolean canBeLoaded();
}
