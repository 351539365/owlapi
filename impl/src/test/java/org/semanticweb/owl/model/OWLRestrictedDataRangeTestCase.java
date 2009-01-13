package org.semanticweb.owl.model;

import org.semanticweb.owl.util.OWLDataUtil;
import org.semanticweb.owl.vocab.OWLRestrictedDataRangeFacetVocabulary;

import java.util.Set;
/*
 * Copyright (C) 2006, University of Manchester
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
 * Bio-Health Informatics Group
 * Date: 25-Oct-2006
 */
public class OWLRestrictedDataRangeTestCase extends AbstractOWLDataFactoryTest {


    public void testCreation() throws Exception {
        OWLDataRange rng = getOWLDataFactory().getOWLDataType(createURI());
        OWLTypedConstant facetValue = getOWLDataFactory().getOWLTypedConstant("3", getOWLDataFactory().getOWLDataType(
                createURI()));
        Set<OWLDataRangeFacetRestriction> restrictions = OWLDataUtil.getFacetRestrictionSet(getOWLDataFactory(), OWLRestrictedDataRangeFacetVocabulary.MAX_EXCLUSIVE, facetValue);
        OWLDataRangeRestriction restRng = getOWLDataFactory().getOWLDataRangeRestriction(rng, restrictions);

        assertNotNull(restRng);
    }



    public void testEqualsPositive() throws Exception {
        OWLDataRange rng = getOWLDataFactory().getOWLDataType(createURI());
        OWLTypedConstant facetValue = getOWLDataFactory().getOWLTypedConstant("3", getOWLDataFactory().getOWLDataType(
                createURI()));
        Set<OWLDataRangeFacetRestriction> restrictions = OWLDataUtil.getFacetRestrictionSet(getOWLDataFactory(), OWLRestrictedDataRangeFacetVocabulary.MAX_EXCLUSIVE, facetValue);

        OWLDataRangeRestriction restRngA = getOWLDataFactory().getOWLDataRangeRestriction(rng,
                                                                                        restrictions);
        OWLDataRangeRestriction restRngB = getOWLDataFactory().getOWLDataRangeRestriction(rng,
                                                                                        restrictions);
        assertEquals(restRngA, restRngB);
    }


    public void testEqualsNegative() throws Exception {
        OWLDataRange rng = getOWLDataFactory().getOWLDataType(createURI());
        OWLTypedConstant facetValue = getOWLDataFactory().getOWLTypedConstant("3", getOWLDataFactory().getOWLDataType(
                createURI()));
        Set<OWLDataRangeFacetRestriction> restrictionsA = OWLDataUtil.getFacetRestrictionSet(getOWLDataFactory(), OWLRestrictedDataRangeFacetVocabulary.MAX_EXCLUSIVE, facetValue);
        Set<OWLDataRangeFacetRestriction> restrictionsB = OWLDataUtil.getFacetRestrictionSet(getOWLDataFactory(), OWLRestrictedDataRangeFacetVocabulary.MIN_INCLUSIVE, facetValue);

        OWLDataRangeRestriction restRngA = getOWLDataFactory().getOWLDataRangeRestriction(rng,
                                                                                        restrictionsA);
        OWLDataRangeRestriction restRngB = getOWLDataFactory().getOWLDataRangeRestriction(rng,
                                                                                        restrictionsB);
        assertNotEquals(restRngA, restRngB);
    }


    public void testHashCode() throws Exception {
        OWLDataRange rng = getOWLDataFactory().getOWLDataType(createURI());
        OWLTypedConstant facetValue = getOWLDataFactory().getOWLTypedConstant("3", getOWLDataFactory().getOWLDataType(
                createURI()));
        Set<OWLDataRangeFacetRestriction> restrictions = OWLDataUtil.getFacetRestrictionSet(getOWLDataFactory(), OWLRestrictedDataRangeFacetVocabulary.MAX_EXCLUSIVE, facetValue);

        OWLDataRangeRestriction restRngA = getOWLDataFactory().getOWLDataRangeRestriction(rng, restrictions);
        OWLDataRangeRestriction restRngB = getOWLDataFactory().getOWLDataRangeRestriction(rng, restrictions);
        assertEquals(restRngA.hashCode(), restRngB.hashCode());
    }
}
