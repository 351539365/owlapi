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
 * Copyright 2011, The University of Manchester
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

package org.semanticweb.owlapi.binaryowl.owlobject;

import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.binaryowl.lookup.LookupTable;
import org.semanticweb.owlapi.model.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/04/2012
 */
public class OWLHasKeyAxiomSerializer extends AbstractAxiomSerializer<OWLHasKeyAxiom> {

    @Override
    protected void writeAxiom(OWLHasKeyAxiom axiom, LookupTable lookupTable, DataOutput dataOutput) throws IOException {
        writeTypeMarkedObject(axiom.getClassExpression(), lookupTable, dataOutput);
        writeTypedMarkedObjectSet(axiom.getObjectPropertyExpressions(), lookupTable, dataOutput);
        writeTypedMarkedObjectSet(axiom.getDataPropertyExpressions(), lookupTable, dataOutput);
    }

    @Override
    protected OWLHasKeyAxiom readAxiom(LookupTable lookupTable, DataInput dataInput, OWLDataFactory dataFactory, Set<OWLAnnotation> annotations) throws IOException, BinaryOWLParseException {
        OWLClassExpression classExpression = readTypeMarkedObject(lookupTable, dataInput, dataFactory);
        Set<OWLObjectPropertyExpression> objectPropertyExpressions = readTypeMarkedObjectSet(lookupTable, dataInput, dataFactory);
        Set<OWLDataPropertyExpression> dataPropertyExpressions = readTypeMarkedObjectSet(lookupTable, dataInput, dataFactory);
        Set<OWLPropertyExpression<?, ?>> expressions = new HashSet<OWLPropertyExpression<?, ?>>();
        expressions.addAll(objectPropertyExpressions);
        expressions.addAll(dataPropertyExpressions);
        return dataFactory.getOWLHasKeyAxiom(classExpression, expressions, annotations);
    }
}
