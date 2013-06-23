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
package org.semanticweb.owlapi.api.test.baseclasses;

import static org.junit.Assert.*;
import static org.semanticweb.owlapi.api.test.TestUtils.createIRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/** Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group Date: 25-Oct-2006 */
public abstract class AbstractOWLObjectCardinalityRestrictionTestCase extends
        AbstractOWLDataFactoryTest {
    protected abstract OWLObjectCardinalityRestriction createRestriction(
            OWLObjectProperty prop, int cardinality);

    protected abstract OWLObjectCardinalityRestriction createRestriction(
            OWLObjectProperty prop, int cardinality, OWLClassExpression classExpression);

    @Override
    @Test
    public void testCreation() {
        OWLObjectProperty prop = ObjectProperty(createIRI());
        int cardinality = 3;
        OWLObjectCardinalityRestriction restA = createRestriction(prop, cardinality);
        assertNotNull("restriction should not be null", restA);
        OWLClassExpression cls = Class(createIRI());
        OWLObjectCardinalityRestriction restB = createRestriction(prop, cardinality, cls);
        assertNotNull("restriction should not be null", restB);
    }

    @Override
    @Test
    public void testEqualsPositive() {
        OWLObjectProperty prop = ObjectProperty(createIRI());
        int cardinality = 3;
        OWLObjectCardinalityRestriction restA = createRestriction(prop, cardinality);
        OWLObjectCardinalityRestriction restB = createRestriction(prop, cardinality);
        assertEquals(restA, restB);
        OWLClassExpression cls = Class(createIRI());
        OWLObjectCardinalityRestriction restC = createRestriction(prop, cardinality, cls);
        OWLObjectCardinalityRestriction restD = createRestriction(prop, cardinality, cls);
        assertEquals(restC, restD);
    }

    @Override
    @Test
    public void testEqualsNegative() {
        OWLObjectProperty prop = ObjectProperty(createIRI());
        // Different cardinality
        OWLObjectCardinalityRestriction restA = createRestriction(prop, 3);
        OWLObjectCardinalityRestriction restB = createRestriction(prop, 4);
        assertFalse(restA.equals(restB));
        // Different property
        OWLObjectCardinalityRestriction restC = createRestriction(
                ObjectProperty(createIRI()), 3);
        OWLObjectCardinalityRestriction restD = createRestriction(
                ObjectProperty(createIRI()), 3);
        assertFalse(restC.equals(restD));
        // Different filler
        OWLObjectCardinalityRestriction restE = createRestriction(prop, 3,
                Class(createIRI()));
        OWLObjectCardinalityRestriction restF = createRestriction(prop, 3,
                Class(createIRI()));
        assertFalse(restE.equals(restF));
    }

    @Override
    @Test
    public void testHashCode() {
        OWLObjectProperty prop = ObjectProperty(createIRI());
        int cardinality = 3;
        OWLClassExpression cls = Class(createIRI());
        OWLObjectCardinalityRestriction restA = createRestriction(prop, cardinality, cls);
        OWLObjectCardinalityRestriction restB = createRestriction(prop, cardinality, cls);
        assertEquals(restA.hashCode(), restB.hashCode());
    }
}
