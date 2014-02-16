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
package org.semanticweb.owlapi.rdf.rdfxml.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** State expecting start of RDF text. */
public class StartRDF implements State {
    private final RDFParser parser;

    /** @param parser
     *            parser */
    public StartRDF(RDFParser parser) {
        this.parser = parser;
    }

    @Override
    public void startElement(String namespaceIRI, String localName,
            String qName, Attributes atts) throws SAXException {
        if (!RDFConstants.RDFNS.equals(namespaceIRI)
                || !RDFConstants.ELT_RDF.equals(localName)) {
            throw new RDFParserException("Expecting rdf:RDF element.",
                    parser.m_documentLocator);
        }
        // the logical IRI is the current IRI that we have as the base IRI
        // at this point
        parser.m_consumer.logicalURI(parser.m_baseIRI.toString());
        parser.pushState(new NodeElementList(parser));
    }

    @Override
    public void endElement(String namespaceIRI, String localName, String qName)
            throws SAXException {
        parser.popState();
    }

    @Override
    public void characters(char[] data, int start, int length)
            throws SAXException {
        if (!parser.isWhitespaceOnly(data, start, length)) {
            throw new RDFParserException(
                    "Expecting rdf:rdf element instead of character content.",
                    parser.m_documentLocator);
        }
    }
}
