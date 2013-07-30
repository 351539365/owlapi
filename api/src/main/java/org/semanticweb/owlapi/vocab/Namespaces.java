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

package org.semanticweb.owlapi.vocab;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 13-Dec-2006<br><br>
 */
public enum Namespaces {

    /**
     * Helper constant to indicate that a namespace is a built in OWL namespace.
     */
    private static final boolean BUILT_IN = true;

    /**
     * Helper constant to indicate that a namespace is not built in.
     */
    private static final boolean NOT_BUILT_IN = false;

    /**
     * Helper constant to indicate that a namespace is currently in use.
     */
    private static final boolean IN_USE = true;

    /**
     * Helper constant to indicate that a namespace is not currently in use.
     */
    private static final boolean NOT_IN_USE = false;

//    OWL2XML("http://www.w3.org/2006/12/owl2-xml#"),

    /**
     * The OWL 2 namespace is here for legacy reasons.
     */
    OWL2("owl2", "http://www.w3.org/2006/12/owl2#", NOT_IN_USE, NOT_BUILT_IN),

    /**legacy*/
    OWL11XML("owl11xml", "http://www.w3.org/2006/12/owl11-xml#", NOT_IN_USE, NOT_BUILT_IN),


    /**
     * The OWL 1.1 namespace is here for legacy reasons.
     */
    OWL11("owl11", "http://www.w3.org/2006/12/owl11#", NOT_IN_USE, NOT_BUILT_IN),

    /**OWL namespace*/
    OWL("owl", "http://www.w3.org/2002/07/owl#", IN_USE, BUILT_IN),
    /**RDFS namespace*/
    RDFS("rdfs", "http://www.w3.org/2000/01/rdf-schema#", IN_USE, BUILT_IN),
    /**RDF namespace*/
    RDF("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", IN_USE, BUILT_IN),
    /**XSD namespace*/
    XSD("xsd", "http://www.w3.org/2001/XMLSchema#", IN_USE, BUILT_IN),
    /**XML namespace*/
    XML("xml", "http://www.w3.org/XML/1998/namespace", IN_USE, NOT_BUILT_IN),
    /**SWRL namespace*/
    SWRL("swrl", "http://www.w3.org/2003/11/swrl#", IN_USE, NOT_BUILT_IN),
    /**SWRLB namespace*/
    SWRLB("swrlb", "http://www.w3.org/2003/11/swrlb#", IN_USE, NOT_BUILT_IN),
    /**SKOS namespace*/
    SKOS("skos", "http://www.w3.org/2004/02/skos/core#", IN_USE, NOT_BUILT_IN);

    final String prefix;
    final String ns;
    final boolean inUse;
    final boolean builtIn;

    Namespaces(String prefix, String ns, boolean inUse, boolean builtIn) {
        this.prefix = prefix;
        this.ns = ns;
        this.inUse = inUse;
        this.builtIn = builtIn;
    }

    /**
     * @return A short, human-readable, prefix name that matches, and expands to the full IRI.
     */
    public String getPrefixName() {
        return prefix;
    }

    /**
     * @return The base IRI which matches the prefix name.
     */
    public String getBaseIRI() {
        return ns;
    }

    /**
     * @return True if this namespace is not obsolete and is currently in active use.
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
     * @return True if this namespace is defined as a core part of the OWL-2 specification.
     */
    public boolean isBuiltIn() {
        return builtIn;
    }

    @Override
    public String toString() {
        return ns;
    }
}
