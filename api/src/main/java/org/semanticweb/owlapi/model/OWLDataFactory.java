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
package org.semanticweb.owlapi.model;

import static org.semanticweb.owlapi.util.OWLAPIPreconditions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.providers.LiteralProvider;
import org.semanticweb.owlapi.model.providers.OWLVocabularyProvider;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

/**
 * An interface for creating entities, class expressions and axioms.
 * 
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group
 * @since 2.0.0
 */
public interface OWLDataFactory extends SWRLDataFactory, OWLEntityProvider,
        OWLEntityByTypeProvider, OWLAnonymousIndividualProvider,
        OWLAnonymousIndividualByIdProvider, OWLVocabularyProvider,
        LiteralProvider {

    /**
     * Gets the inverse of an object property.
     * 
     * @param property
     *        The property of which the inverse will be returned
     * @return The inverse of the specified object property
     */
    @Nonnull
    OWLObjectInverseOf getOWLObjectInverseOf(
            @Nonnull OWLObjectPropertyExpression property);

    @Override
    default OWLLiteral
            getOWLLiteral(String lexicalValue, OWL2Datatype datatype) {
        checkNotNull(datatype, "datatype cannot be null");
        return getOWLLiteral(lexicalValue, getOWLDatatype(datatype));
    }

    // Data ranges
    /**
     * Gets an OWLDataOneOf <a href=
     * "http://www.w3.org/TR/2008/WD-owl2-syntax-20081202/#Enumeration_of_Literals"
     * >(see spec)</a>
     * 
     * @param values
     *        The set of values that the data one of should contain.
     * @return A data one of that enumerates the specified set of values
     */
    @Nonnull
    OWLDataOneOf getOWLDataOneOf(@Nonnull Set<? extends OWLLiteral> values);

    /**
     * Gets an OWLDataOneOf <a href=
     * "http://www.w3.org/TR/2008/WD-owl2-syntax-20081202/#Enumeration_of_Literals"
     * >(see spec)</a>
     * 
     * @param values
     *        The set of values that the data one of should contain. Cannot be
     *        null or contain null values.
     * @return A data one of that enumerates the specified set of values
     */
    @Nonnull
    default OWLDataOneOf getOWLDataOneOf(@Nonnull OWLLiteral... values) {
        checkIterableNotNull(values, "values cannot be null", true);
        return getOWLDataOneOf(CollectionFactory.createSet(values));
    }

    /**
     * Gets an OWLDataComplementOf <a href=
     * "http://www.w3.org/TR/2008/WD-owl2-syntax-20081202/#Complement_of_Data_Range"
     * >(see spec)</a>
     * 
     * @param dataRange
     *        The datarange to be complemented.
     * @return An OWLDataComplementOf of the specified data range
     */
    @Nonnull
    OWLDataComplementOf getOWLDataComplementOf(@Nonnull OWLDataRange dataRange);

    /**
     * OWLDatatypeRestriction <a
     * href="http://www.w3.org/TR/owl2-syntax/#Datatype_Restrictions">see
     * spec</a>
     * 
     * @param dataType
     *        datatype for the restriction
     * @param facetRestrictions
     *        facet restrictions. Cannot contain nulls.
     * @return an OWLDatatypeRestriction for the specified data type and
     *         restrictions
     */
    @Nonnull
    OWLDatatypeRestriction getOWLDatatypeRestriction(
            @Nonnull OWLDatatype dataType,
            @Nonnull Set<OWLFacetRestriction> facetRestrictions);

    /**
     * OWLDatatypeRestriction <a
     * href="http://www.w3.org/TR/owl2-syntax/#Datatype_Restrictions">see
     * spec</a>
     * 
     * @param dataType
     *        datatype for the restriction
     * @param facet
     *        facet for restriction
     * @param typedLiteral
     *        literal for facet.
     * @return an OWLDatatypeRestriction with given value for the specified
     *         facet
     */
    @Nonnull
    OWLDatatypeRestriction getOWLDatatypeRestriction(
            @Nonnull OWLDatatype dataType, @Nonnull OWLFacet facet,
            @Nonnull OWLLiteral typedLiteral);

    /**
     * @param dataType
     *        datatype for the restriction
     * @param facetRestrictions
     *        facet restrictions. Cannot contain nulls.
     * @return an OWLDatatypeRestriction for the specified data type and
     *         restrictions
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeRestriction(
            @Nonnull OWLDatatype dataType,
            @Nonnull OWLFacetRestriction... facetRestrictions) {
        checkIterableNotNull(facetRestrictions,
                "facetRestrictions cannot be null", true);
        return getOWLDatatypeRestriction(dataType,
                CollectionFactory.createSet(facetRestrictions));
    }

    /**
     * Creates a datatype restriction on xsd:integer with a minInclusive facet
     * restriction
     * 
     * @param minInclusive
     *        The value of the min inclusive facet restriction that will be
     *        applied to the {@code xsd:integer} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:integer}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MIN_INCLUSIVE} facet
     *         value specified by the {@code minInclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMinInclusiveRestriction(
            int minInclusive) {
        return getOWLDatatypeRestriction(getIntegerOWLDatatype(),
                OWLFacet.MIN_INCLUSIVE, getOWLLiteral(minInclusive));
    }

    /**
     * Creates a datatype restriction on xsd:integer with a maxInclusive facet
     * restriction
     * 
     * @param maxInclusive
     *        The value of the max inclusive facet restriction that will be
     *        applied to the {@code xsd:integer} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:integer}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MAX_INCLUSIVE} facet
     *         value specified by the {@code maxInclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMaxInclusiveRestriction(
            int maxInclusive) {
        return getOWLDatatypeRestriction(getIntegerOWLDatatype(),
                OWLFacet.MAX_INCLUSIVE, getOWLLiteral(maxInclusive));
    }

    /**
     * Creates a datatype restriction on xsd:integer with min and max inclusive
     * facet restrictions
     * 
     * @param minInclusive
     *        The value of the max inclusive facet restriction that will be
     *        applied to the {@code xsd:integer} datatype.
     * @param maxInclusive
     *        The value of the max inclusive facet restriction that will be
     *        applied to the {@code xsd:integer} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:integer}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MIN_INCLUSIVE} facet
     *         value specified by the {@code minInclusive} parameter and a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MAX_INCLUSIVE} facet
     *         value specified by the {@code maxInclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMinMaxInclusiveRestriction(
            int minInclusive, int maxInclusive) {
        return getOWLDatatypeRestriction(
                getIntegerOWLDatatype(),
                getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE,
                        getOWLLiteral(minInclusive)),
                getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, maxInclusive));
    }

    /**
     * Creates a datatype restriction on xsd:integer with a minExclusive facet
     * restriction
     * 
     * @param minExclusive
     *        The value of the min exclusive facet restriction that will be
     *        applied to the {@code xsd:integer} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:integer}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MIN_EXCLUSIVE} facet
     *         value specified by the {@code minExclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMinExclusiveRestriction(
            int minExclusive) {
        return getOWLDatatypeRestriction(getIntegerOWLDatatype(),
                OWLFacet.MIN_EXCLUSIVE, getOWLLiteral(minExclusive));
    }

    /**
     * Creates a datatype restriction on xsd:integer with a maxExclusive facet
     * restriction
     * 
     * @param maxExclusive
     *        The value of the max exclusive facet restriction that will be
     *        applied to the {@code xsd:integer} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:integer}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MAX_EXCLUSIVE} facet
     *         value specified by the {@code maxExclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMaxExclusiveRestriction(
            int maxExclusive) {
        return getOWLDatatypeRestriction(getIntegerOWLDatatype(),
                OWLFacet.MAX_EXCLUSIVE, getOWLLiteral(maxExclusive));
    }

    /**
     * Creates a datatype restriction on xsd:integer with min and max exclusive
     * facet restrictions
     * 
     * @param minExclusive
     *        The value of the max exclusive facet restriction that will be
     *        applied to the {@code xsd:integer} datatype.
     * @param maxExclusive
     *        The value of the max exclusive facet restriction that will be
     *        applied to the {@code xsd:integer} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:integer}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MIN_EXCLUSIVE} facet
     *         value specified by the {@code minExclusive} parameter and a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MAX_EXCLUSIVE} facet
     *         value specified by the {@code maxExclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMinMaxExclusiveRestriction(
            int minExclusive, int maxExclusive) {
        return getOWLDatatypeRestriction(
                getIntegerOWLDatatype(),
                getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE,
                        getOWLLiteral(minExclusive)),
                getOWLFacetRestriction(OWLFacet.MAX_EXCLUSIVE, maxExclusive));
    }

    /**
     * Creates a datatype restriction on xsd:double with a minInclusive facet
     * restriction
     * 
     * @param minInclusive
     *        The value of the min inclusive facet restriction that will be
     *        applied to the {@code xsd:double} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:double}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MIN_INCLUSIVE} facet
     *         value specified by the {@code minInclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMinInclusiveRestriction(
            double minInclusive) {
        return getOWLDatatypeRestriction(getDoubleOWLDatatype(),
                OWLFacet.MIN_INCLUSIVE, getOWLLiteral(minInclusive));
    }

    /**
     * Creates a datatype restriction on xsd:double with a maxInclusive facet
     * restriction
     * 
     * @param maxInclusive
     *        The value of the max inclusive facet restriction that will be
     *        applied to the {@code xsd:double} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:double}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MAX_INCLUSIVE} facet
     *         value specified by the {@code maxInclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMaxInclusiveRestriction(
            double maxInclusive) {
        return getOWLDatatypeRestriction(getDoubleOWLDatatype(),
                OWLFacet.MAX_INCLUSIVE, getOWLLiteral(maxInclusive));
    }

    /**
     * Creates a datatype restriction on xsd:double with min and max inclusive
     * facet restrictions
     * 
     * @param minInclusive
     *        The value of the max inclusive facet restriction that will be
     *        applied to the {@code xsd:double} datatype.
     * @param maxInclusive
     *        The value of the max inclusive facet restriction that will be
     *        applied to the {@code xsd:double} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:double}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MIN_INCLUSIVE} facet
     *         value specified by the {@code minInclusive} parameter and a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MAX_INCLUSIVE} facet
     *         value specified by the {@code maxInclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMinMaxInclusiveRestriction(
            double minInclusive, double maxInclusive) {
        return getOWLDatatypeRestriction(
                getDoubleOWLDatatype(),
                getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE,
                        getOWLLiteral(minInclusive)),
                getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, maxInclusive));
    }

    /**
     * Creates a datatype restriction on xsd:double with a minExclusive facet
     * restriction
     * 
     * @param minExclusive
     *        The value of the min exclusive facet restriction that will be
     *        applied to the {@code xsd:double} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:double}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MIN_EXCLUSIVE} facet
     *         value specified by the {@code minExclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMinExclusiveRestriction(
            double minExclusive) {
        return getOWLDatatypeRestriction(getDoubleOWLDatatype(),
                OWLFacet.MIN_EXCLUSIVE, getOWLLiteral(minExclusive));
    }

    /**
     * Creates a datatype restriction on xsd:double with a maxExclusive facet
     * restriction
     * 
     * @param maxExclusive
     *        The value of the max exclusive facet restriction that will be
     *        applied to the {@code xsd:double} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:double}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MAX_EXCLUSIVE} facet
     *         value specified by the {@code maxExclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMaxExclusiveRestriction(
            double maxExclusive) {
        return getOWLDatatypeRestriction(getDoubleOWLDatatype(),
                OWLFacet.MAX_EXCLUSIVE, getOWLLiteral(maxExclusive));
    }

    /**
     * Creates a datatype restriction on xsd:double with min and max exclusive
     * facet restrictions
     * 
     * @param minExclusive
     *        The value of the max exclusive facet restriction that will be
     *        applied to the {@code xsd:double} datatype.
     * @param maxExclusive
     *        The value of the max exclusive facet restriction that will be
     *        applied to the {@code xsd:double} datatype
     * @return An {@code OWLDatatypeRestriction} that restricts the
     *         {@code xsd:double}
     *         {@link org.semanticweb.owlapi.model.OWLDatatype} with a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MIN_EXCLUSIVE} facet
     *         value specified by the {@code minExclusive} parameter and a
     *         {@link org.semanticweb.owlapi.vocab.OWLFacet#MAX_EXCLUSIVE} facet
     *         value specified by the {@code maxExclusive} parameter.
     */
    @Nonnull
    default OWLDatatypeRestriction getOWLDatatypeMinMaxExclusiveRestriction(
            double minExclusive, double maxExclusive) {
        return getOWLDatatypeRestriction(
                getDoubleOWLDatatype(),
                getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE,
                        getOWLLiteral(minExclusive)),
                getOWLFacetRestriction(OWLFacet.MAX_EXCLUSIVE, maxExclusive));
    }

    /**
     * @param facet
     *        facet for restriction.
     * @param facetValue
     *        literal for restriction.
     * @return an OWLFacetRestriction on specified facet and value
     */
    @Nonnull
    OWLFacetRestriction getOWLFacetRestriction(@Nonnull OWLFacet facet,
            @Nonnull OWLLiteral facetValue);

    /**
     * @param facet
     *        facet for restriction.
     * @param facetValue
     *        facet value
     * @return an OWLFacetRestriction on specified facet and value
     */
    @Nonnull
    default OWLFacetRestriction getOWLFacetRestriction(@Nonnull OWLFacet facet,
            int facetValue) {
        return getOWLFacetRestriction(facet, getOWLLiteral(facetValue));
    }

    /**
     * @param facet
     *        facet for restriction
     * @param facetValue
     *        facet value.
     * @return an OWLFacetRestriction on specified facet and value
     */
    @Nonnull
    default OWLFacetRestriction getOWLFacetRestriction(@Nonnull OWLFacet facet,
            double facetValue) {
        return getOWLFacetRestriction(facet, getOWLLiteral(facetValue));
    }

    /**
     * @param facet
     *        facet for restriction
     * @param facetValue
     *        facet value.
     * @return an OWLFacetRestriction on specified facet and value
     */
    @Nonnull
    default OWLFacetRestriction getOWLFacetRestriction(@Nonnull OWLFacet facet,
            float facetValue) {
        return getOWLFacetRestriction(facet, getOWLLiteral(facetValue));
    }

    /**
     * @param dataRanges
     *        data ranges for union. Cannot be null or contain nulls.
     * @return an OWLDataUnionOf on the specified dataranges
     */
    @Nonnull
    OWLDataUnionOf getOWLDataUnionOf(
            @Nonnull Set<? extends OWLDataRange> dataRanges);

    /**
     * @param dataRanges
     *        data ranges for union. Cannot be null or contain nulls.
     * @return an OWLDataUnionOf on the specified dataranges
     */
    @Nonnull
    default OWLDataUnionOf getOWLDataUnionOf(
            @Nonnull OWLDataRange... dataRanges) {
        checkIterableNotNull(dataRanges, "dataRanges cannot be null", true);
        return getOWLDataUnionOf(CollectionFactory.createSet(dataRanges));
    }

    /**
     * @param dataRanges
     *        data ranges for intersection. Cannot be null or contain nulls.
     * @return an OWLDataIntersectionOf on the specified dataranges
     */
    @Nonnull
    OWLDataIntersectionOf getOWLDataIntersectionOf(
            @Nonnull Set<? extends OWLDataRange> dataRanges);

    /**
     * @param dataRanges
     *        data ranges for intersection. Cannot be null or contain nulls.
     * @return an OWLDataIntersectionOf on the specified dataranges
     */
    @Nonnull
    default OWLDataIntersectionOf getOWLDataIntersectionOf(
            @Nonnull OWLDataRange... dataRanges) {
        checkIterableNotNull(dataRanges, "dataRange cannot be nulls", true);
        return getOWLDataIntersectionOf(CollectionFactory.createSet(dataRanges));
    }

    // Class Expressions
    /**
     * @param operands
     *        class expressions for intersection. Cannot be null or contain
     *        nulls.
     * @return an OWLObjectIntersectionOf on the specified operands
     */
    @Nonnull
    OWLObjectIntersectionOf getOWLObjectIntersectionOf(
            @Nonnull Set<? extends OWLClassExpression> operands);

    /**
     * @param operands
     *        class expressions for intersection. Cannot be null or contain
     *        nulls.
     * @return an OWLObjectIntersectionOf on the specified operands
     */
    @Nonnull
    default OWLObjectIntersectionOf getOWLObjectIntersectionOf(
            @Nonnull OWLClassExpression... operands) {
        checkIterableNotNull(operands, "operands cannot be null", true);
        return getOWLObjectIntersectionOf(CollectionFactory.createSet(operands));
    }

    // Data restrictions
    /**
     * Gets an OWLDataSomeValuesFrom restriction
     * 
     * @param property
     *        The property that the restriction acts along.
     * @param dataRange
     *        The data range that is the filler.
     * @return An OWLDataSomeValuesFrom restriction that acts along the
     *         specified property and has the specified filler
     */
    @Nonnull
    OWLDataSomeValuesFrom getOWLDataSomeValuesFrom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLDataRange dataRange);

    /**
     * @param property
     *        The property that the restriction acts along.
     * @param dataRange
     *        The data range that is the filler.
     * @return An OWLDataAllValuesFrom restriction that acts along the specified
     *         property and has the specified filler
     */
    @Nonnull
    OWLDataAllValuesFrom getOWLDataAllValuesFrom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLDataRange dataRange);

    /**
     * @param cardinality
     *        Cannot be negative
     * @param property
     *        The property that the restriction acts along.
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLDataExactCardinality getOWLDataExactCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLDataPropertyExpression property);

    /**
     * @param cardinality
     *        Cannot be negative
     * @param property
     *        The property that the restriction acts along.
     * @param dataRange
     *        data range for restricition
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLDataExactCardinality getOWLDataExactCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLDataRange dataRange);

    /**
     * @param cardinality
     *        Cannot be negative
     * @param property
     *        The property that the restriction acts along.
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLDataMaxCardinality getOWLDataMaxCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLDataPropertyExpression property);

    /**
     * @param cardinality
     *        Cannot be negative
     * @param property
     *        The property that the restriction acts along.
     * @param dataRange
     *        data range for restriction
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLDataMaxCardinality getOWLDataMaxCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLDataRange dataRange);

    /**
     * @param cardinality
     *        Cannot be negative
     * @param property
     *        The property that the restriction acts along.
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLDataMinCardinality getOWLDataMinCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLDataPropertyExpression property);

    /**
     * @param cardinality
     *        Cannot be negative.
     * @param property
     *        The property that the restriction acts along.
     * @param dataRange
     *        data range for restriction
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLDataMinCardinality getOWLDataMinCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLDataRange dataRange);

    /**
     * @param property
     *        The property that the restriction acts along.
     * @param value
     *        value for restriction
     * @return a HasValue restriction with specified property and value
     */
    @Nonnull
    OWLDataHasValue getOWLDataHasValue(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLLiteral value);

    /**
     * @param operand
     *        class expression to complement
     * @return the complement of the specified argument
     */
    @Nonnull
    OWLObjectComplementOf getOWLObjectComplementOf(
            @Nonnull OWLClassExpression operand);

    /**
     * @param values
     *        indivudals for restriction. Cannot be null or contain nulls.
     * @return a OneOf expression on specified individuals
     */
    @Nonnull
    OWLObjectOneOf getOWLObjectOneOf(
            @Nonnull Set<? extends OWLIndividual> values);

    /**
     * @param individuals
     *        indivudals for restriction. Cannot be null or contain nulls.
     * @return a OneOf expression on specified individuals
     */
    @Nonnull
    default OWLObjectOneOf getOWLObjectOneOf(
            @Nonnull OWLIndividual... individuals) {
        checkIterableNotNull(individuals, "individuals cannot be null", true);
        return getOWLObjectOneOf(CollectionFactory.createSet(individuals));
    }

    // Object restrictions
    /**
     * @param property
     *        The property that the restriction acts along.
     * @param classExpression
     *        the class expression for the restriction
     * @return an AllValuesFrom on specified property and class expression
     */
    @Nonnull
    OWLObjectAllValuesFrom getOWLObjectAllValuesFrom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression classExpression);

    /**
     * Gets an OWLObjectSomeValuesFrom restriction
     * 
     * @param property
     *        The object property that the restriction acts along.
     * @param classExpression
     *        The class expression that is the filler.
     * @return An OWLObjectSomeValuesFrom restriction along the specified
     *         property with the specified filler
     */
    @Nonnull
    OWLObjectSomeValuesFrom getOWLObjectSomeValuesFrom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression classExpression);

    /**
     * @param cardinality
     *        Cannot be negative.
     * @param property
     *        The property that the restriction acts along.
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLObjectExactCardinality getOWLObjectExactCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLObjectPropertyExpression property);

    /**
     * @param cardinality
     *        Cannot be negative.
     * @param property
     *        The property that the restriction acts along.
     * @param classExpression
     *        class expression for restriction
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLObjectExactCardinality getOWLObjectExactCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression classExpression);

    /**
     * @param cardinality
     *        Cannot be negative.
     * @param property
     *        The property that the restriction acts along.
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLObjectMinCardinality getOWLObjectMinCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLObjectPropertyExpression property);

    /**
     * @param cardinality
     *        Cannot be negative.
     * @param property
     *        The property that the restriction acts along.
     * @param classExpression
     *        class expression for restriction
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLObjectMinCardinality getOWLObjectMinCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression classExpression);

    /**
     * @param cardinality
     *        Cannot be negative.
     * @param property
     *        The property that the restriction acts along.
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLObjectMaxCardinality getOWLObjectMaxCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLObjectPropertyExpression property);

    /**
     * @param cardinality
     *        Cannot be negative.
     * @param property
     *        The property that the restriction acts along.
     * @param classExpression
     *        class expression for restriction
     * @return an ExactCardinality on the specified property
     */
    @Nonnull
    OWLObjectMaxCardinality getOWLObjectMaxCardinality(
            @Nonnegative int cardinality,
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression classExpression);

    /**
     * @param property
     *        The property that the restriction acts along.
     * @return a ObjectHasSelf class expression on the specified property
     */
    @Nonnull
    OWLObjectHasSelf getOWLObjectHasSelf(
            @Nonnull OWLObjectPropertyExpression property);

    /**
     * @param property
     *        The property that the restriction acts along.
     * @param individual
     *        individual for restriction
     * @return a HasValue restriction with specified property and value
     */
    @Nonnull
    OWLObjectHasValue getOWLObjectHasValue(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLIndividual individual);

    /**
     * @param operands
     *        class expressions for union
     * @return a class union over the specified arguments
     */
    @Nonnull
    OWLObjectUnionOf getOWLObjectUnionOf(
            @Nonnull Set<? extends OWLClassExpression> operands);

    /**
     * @param operands
     *        class expressions for union
     * @return a class union over the specified arguments
     */
    @Nonnull
    default OWLObjectUnionOf getOWLObjectUnionOf(
            @Nonnull OWLClassExpression... operands) {
        checkIterableNotNull(operands, "operands cannot be null", true);
        return getOWLObjectUnionOf(CollectionFactory.createSet(operands));
    }

    // Axioms
    /**
     * Gets a declaration for an entity
     * 
     * @param owlEntity
     *        The declared entity.
     * @return The declaration axiom for the specified entity.
     */
    @Nonnull
    default OWLDeclarationAxiom getOWLDeclarationAxiom(
            @Nonnull OWLEntity owlEntity) {
        return getOWLDeclarationAxiom(owlEntity, Collections.emptySet());
    }

    /**
     * Gets a declaration with zero or more annotations for an entity
     * 
     * @param owlEntity
     *        The declared entity.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return The declaration axiom for the specified entity which is annotated
     *         with the specified annotations
     */
    @Nonnull
    OWLDeclarationAxiom getOWLDeclarationAxiom(@Nonnull OWLEntity owlEntity,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    // Class Axioms
    /**
     * @param subClass
     *        sub class
     * @param superClass
     *        super class
     * @return a subclass axiom with no annotations
     */
    @Nonnull
    default OWLSubClassOfAxiom getOWLSubClassOfAxiom(
            @Nonnull OWLClassExpression subClass,
            @Nonnull OWLClassExpression superClass) {
        return getOWLSubClassOfAxiom(subClass, superClass,
                Collections.emptySet());
    }

    /**
     * @param subClass
     *        sub class
     * @param superClass
     *        super class
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a subclass axiom with specified annotations
     */
    @Nonnull
    OWLSubClassOfAxiom getOWLSubClassOfAxiom(
            @Nonnull OWLClassExpression subClass,
            @Nonnull OWLClassExpression superClass,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param classExpressions
     *        equivalent classes. Cannot be null or contain nulls.
     * @return an equivalent classes axiom with specified operands and no
     *         annotations
     */
    @Nonnull
    default OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(
            @Nonnull Set<? extends OWLClassExpression> classExpressions) {
        return getOWLEquivalentClassesAxiom(classExpressions,
                Collections.emptySet());
    }

    /**
     * @param classExpressions
     *        equivalent classes. Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an equivalent classes axiom with specified operands and
     *         annotations
     */
    @Nonnull
    OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(
            @Nonnull Set<? extends OWLClassExpression> classExpressions,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param classExpressions
     *        equivalent classes. Cannot be null or contain nulls.
     * @return an equivalent classes axiom with specified operands and no
     *         annotations
     */
    @Nonnull
    default OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(
            @Nonnull OWLClassExpression... classExpressions) {
        checkIterableNotNull(classExpressions,
                "classExpressions cannot be null", true);
        return getOWLEquivalentClassesAxiom(CollectionFactory
                .createSet(classExpressions));
    }

    /**
     * @param clsA
     *        one class for equivalence
     * @param clsB
     *        one class for equivalence
     * @return an equivalent classes axiom with specified operands and no
     *         annotations (special case with only two operands)
     */
    @Nonnull
    default OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(
            @Nonnull OWLClassExpression clsA, @Nonnull OWLClassExpression clsB) {
        return getOWLEquivalentClassesAxiom(clsA, clsB, Collections.emptySet());
    }

    /**
     * @param clsA
     *        one class for equivalence
     * @param clsB
     *        one class for equivalence
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an equivalent classes axiom with specified operands and
     *         annotations (special case with only two operands)
     */
    @Nonnull
    default OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(
            @Nonnull OWLClassExpression clsA, @Nonnull OWLClassExpression clsB,
            @Nonnull Set<? extends OWLAnnotation> annotations) {
        return getOWLEquivalentClassesAxiom(
                CollectionFactory.createSet(clsA, clsB), annotations);
    }

    /**
     * @param classExpressions
     *        Disjoint classes. Cannot be null or contain nulls.
     * @return a disjoint class axiom with no annotations
     */
    @Nonnull
    default OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(
            @Nonnull Set<? extends OWLClassExpression> classExpressions) {
        return getOWLDisjointClassesAxiom(classExpressions,
                Collections.emptySet());
    }

    /**
     * @param classExpressions
     *        Disjoint classes. Cannot be null or contain nulls.
     * @return a disjoint class axiom with no annotations
     */
    @Nonnull
    default OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(
            @Nonnull OWLClassExpression... classExpressions) {
        checkIterableNotNull(classExpressions,
                "classExpressions cannot be null", true);
        return getOWLDisjointClassesAxiom(CollectionFactory
                .createSet(classExpressions));
    }

    /**
     * @param classExpressions
     *        Disjoint classes. Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a disjoint class axiom with annotations
     */
    @Nonnull
    OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(
            @Nonnull Set<? extends OWLClassExpression> classExpressions,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param owlClass
     *        left hand side of the axiom.
     * @param classExpressions
     *        right hand side of the axiom. Cannot be null or contain nulls.
     * @return a disjoint union axiom
     */
    @Nonnull
    default OWLDisjointUnionAxiom getOWLDisjointUnionAxiom(
            @Nonnull OWLClass owlClass,
            @Nonnull Set<? extends OWLClassExpression> classExpressions) {
        return getOWLDisjointUnionAxiom(owlClass, classExpressions,
                Collections.emptySet());
    }

    /**
     * @param owlClass
     *        left hand side of the axiom. Cannot be null.
     * @param classExpressions
     *        right hand side of the axiom. Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a disjoint union axiom with annotations
     */
    @Nonnull
    OWLDisjointUnionAxiom getOWLDisjointUnionAxiom(@Nonnull OWLClass owlClass,
            @Nonnull Set<? extends OWLClassExpression> classExpressions,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    // Object property axioms
    /**
     * @param subProperty
     *        sub property
     * @param superProperty
     *        super property
     * @return a subproperty axiom
     */
    @Nonnull
    default OWLSubObjectPropertyOfAxiom getOWLSubObjectPropertyOfAxiom(
            @Nonnull OWLObjectPropertyExpression subProperty,
            @Nonnull OWLObjectPropertyExpression superProperty) {
        return getOWLSubObjectPropertyOfAxiom(subProperty, superProperty,
                Collections.emptySet());
    }

    /**
     * @param subProperty
     *        sub Property
     * @param superProperty
     *        super Property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a subproperty axiom with annotations
     */
    @Nonnull
    OWLSubObjectPropertyOfAxiom getOWLSubObjectPropertyOfAxiom(
            @Nonnull OWLObjectPropertyExpression subProperty,
            @Nonnull OWLObjectPropertyExpression superProperty,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param chain
     *        Chain of properties. Cannot be null or contain nulls.
     * @param superProperty
     *        super property
     * @return a subproperty chain axiom
     */
    @Nonnull
    default OWLSubPropertyChainOfAxiom getOWLSubPropertyChainOfAxiom(
            @Nonnull List<? extends OWLObjectPropertyExpression> chain,
            @Nonnull OWLObjectPropertyExpression superProperty) {
        return getOWLSubPropertyChainOfAxiom(chain, superProperty,
                Collections.emptySet());
    }

    /**
     * @param chain
     *        Chain of properties. Cannot be null or contain nulls.
     * @param superProperty
     *        super property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a subproperty chain axiom
     */
    @Nonnull
    OWLSubPropertyChainOfAxiom getOWLSubPropertyChainOfAxiom(
            @Nonnull List<? extends OWLObjectPropertyExpression> chain,
            @Nonnull OWLObjectPropertyExpression superProperty,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @return an equivalent properties axiom with specified properties
     */
    @Nonnull
    default
            OWLEquivalentObjectPropertiesAxiom
            getOWLEquivalentObjectPropertiesAxiom(
                    @Nonnull Set<? extends OWLObjectPropertyExpression> properties) {
        return getOWLEquivalentObjectPropertiesAxiom(properties,
                Collections.emptySet());
    }

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an equivalent properties axiom with specified properties and
     *         annotations
     */
    @Nonnull
    OWLEquivalentObjectPropertiesAxiom getOWLEquivalentObjectPropertiesAxiom(
            @Nonnull Set<? extends OWLObjectPropertyExpression> properties,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @return an equivalent properties axiom with specified properties
     */
    @Nonnull
    default OWLEquivalentObjectPropertiesAxiom
            getOWLEquivalentObjectPropertiesAxiom(
                    @Nonnull OWLObjectPropertyExpression... properties) {
        checkIterableNotNull(properties, "properties cannot be null", true);
        return getOWLEquivalentObjectPropertiesAxiom(CollectionFactory
                .createSet(properties));
    }

    /**
     * @param propertyA
     *        property A
     * @param propertyB
     *        property B
     * @return an equivalent properties axiom with specified properties
     */
    @Nonnull
    default OWLEquivalentObjectPropertiesAxiom
            getOWLEquivalentObjectPropertiesAxiom(
                    @Nonnull OWLObjectPropertyExpression propertyA,
                    @Nonnull OWLObjectPropertyExpression propertyB) {
        return getOWLEquivalentObjectPropertiesAxiom(propertyA, propertyB,
                Collections.emptySet());
    }

    /**
     * @param propertyA
     *        property A
     * @param propertyB
     *        property B
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an equivalent properties axiom with specified properties and
     *         annotations
     */
    @Nonnull
    default OWLEquivalentObjectPropertiesAxiom
            getOWLEquivalentObjectPropertiesAxiom(
                    @Nonnull OWLObjectPropertyExpression propertyA,
                    @Nonnull OWLObjectPropertyExpression propertyB,
                    @Nonnull Set<? extends OWLAnnotation> annotations) {
        return getOWLEquivalentObjectPropertiesAxiom(
                CollectionFactory.createSet(propertyA, propertyB), annotations);
    }

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @return a disjoint object properties axiom with specified properties
     */
    @Nonnull
    default
            OWLDisjointObjectPropertiesAxiom
            getOWLDisjointObjectPropertiesAxiom(
                    @Nonnull Set<? extends OWLObjectPropertyExpression> properties) {
        return getOWLDisjointObjectPropertiesAxiom(properties,
                Collections.emptySet());
    }

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @return a disjoint object properties axiom with specified properties
     */
    @Nonnull
    default OWLDisjointObjectPropertiesAxiom
            getOWLDisjointObjectPropertiesAxiom(
                    @Nonnull OWLObjectPropertyExpression... properties) {
        checkIterableNotNull(properties, "properties cannot be null", true);
        return getOWLDisjointObjectPropertiesAxiom(CollectionFactory
                .createSet(properties));
    }

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a disjoint object properties axiom with specified properties and
     *         annotations
     */
    @Nonnull
    OWLDisjointObjectPropertiesAxiom getOWLDisjointObjectPropertiesAxiom(
            @Nonnull Set<? extends OWLObjectPropertyExpression> properties,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param forwardProperty
     *        forward Property
     * @param inverseProperty
     *        inverse Property
     * @return an inverse object property axiom
     */
    @Nonnull
    default OWLInverseObjectPropertiesAxiom getOWLInverseObjectPropertiesAxiom(
            @Nonnull OWLObjectPropertyExpression forwardProperty,
            @Nonnull OWLObjectPropertyExpression inverseProperty) {
        return getOWLInverseObjectPropertiesAxiom(forwardProperty,
                inverseProperty, Collections.emptySet());
    }

    /**
     * @param forwardProperty
     *        forward Property
     * @param inverseProperty
     *        inverse Property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an inverse object property axiom with annotations
     */
    @Nonnull
    OWLInverseObjectPropertiesAxiom getOWLInverseObjectPropertiesAxiom(
            @Nonnull OWLObjectPropertyExpression forwardProperty,
            @Nonnull OWLObjectPropertyExpression inverseProperty,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @param classExpression
     *        class Expression
     * @return an object property domain axiom
     */
    @Nonnull
    default OWLObjectPropertyDomainAxiom getOWLObjectPropertyDomainAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression classExpression) {
        return getOWLObjectPropertyDomainAxiom(property, classExpression,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param classExpression
     *        class Expression
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an object property domain axiom with annotations
     */
    @Nonnull
    OWLObjectPropertyDomainAxiom getOWLObjectPropertyDomainAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression classExpression,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @param range
     *        range
     * @return an object property range axiom
     */
    @Nonnull
    default OWLObjectPropertyRangeAxiom getOWLObjectPropertyRangeAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression range) {
        return getOWLObjectPropertyRangeAxiom(property, range,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param range
     *        range
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an object property range axiom with annotations
     */
    @Nonnull
    OWLObjectPropertyRangeAxiom getOWLObjectPropertyRangeAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLClassExpression range,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @return a functional object property axiom
     */
    @Nonnull
    default OWLFunctionalObjectPropertyAxiom
            getOWLFunctionalObjectPropertyAxiom(
                    @Nonnull OWLObjectPropertyExpression property) {
        return getOWLFunctionalObjectPropertyAxiom(property,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a functional object property axiom with annotations
     */
    @Nonnull
    OWLFunctionalObjectPropertyAxiom getOWLFunctionalObjectPropertyAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @return an inverse functional object property axiom
     */
    @Nonnull
    default OWLInverseFunctionalObjectPropertyAxiom
            getOWLInverseFunctionalObjectPropertyAxiom(
                    @Nonnull OWLObjectPropertyExpression property) {
        return getOWLInverseFunctionalObjectPropertyAxiom(property,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an inverse functional object property axiom with annotations
     */
    @Nonnull
    OWLInverseFunctionalObjectPropertyAxiom
            getOWLInverseFunctionalObjectPropertyAxiom(
                    @Nonnull OWLObjectPropertyExpression property,
                    @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @return a reflexive object property axiom
     */
    @Nonnull
    default OWLReflexiveObjectPropertyAxiom getOWLReflexiveObjectPropertyAxiom(
            @Nonnull OWLObjectPropertyExpression property) {
        return getOWLReflexiveObjectPropertyAxiom(property,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a reflexive object property axiom with annotations
     */
    @Nonnull
    OWLReflexiveObjectPropertyAxiom getOWLReflexiveObjectPropertyAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @return an irreflexive object property axiom
     */
    @Nonnull
    default OWLIrreflexiveObjectPropertyAxiom
            getOWLIrreflexiveObjectPropertyAxiom(
                    @Nonnull OWLObjectPropertyExpression property) {
        return getOWLIrreflexiveObjectPropertyAxiom(property,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an irreflexive object property axiom with annotations
     */
    @Nonnull
    OWLIrreflexiveObjectPropertyAxiom getOWLIrreflexiveObjectPropertyAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @return a symmetric property axiom
     */
    @Nonnull
    default OWLSymmetricObjectPropertyAxiom getOWLSymmetricObjectPropertyAxiom(
            @Nonnull OWLObjectPropertyExpression property) {
        return getOWLSymmetricObjectPropertyAxiom(property,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a symmetric property axiom with annotations
     */
    @Nonnull
    OWLSymmetricObjectPropertyAxiom getOWLSymmetricObjectPropertyAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param propertyExpression
     *        property Expression
     * @return an asymmetric object property axiom on the specified argument
     */
    @Nonnull
    default OWLAsymmetricObjectPropertyAxiom
            getOWLAsymmetricObjectPropertyAxiom(
                    @Nonnull OWLObjectPropertyExpression propertyExpression) {
        return getOWLAsymmetricObjectPropertyAxiom(propertyExpression,
                Collections.emptySet());
    }

    /**
     * @param propertyExpression
     *        property Expression
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an asymmetric object property axiom on the specified argument
     *         with annotations
     */
    @Nonnull
    OWLAsymmetricObjectPropertyAxiom getOWLAsymmetricObjectPropertyAxiom(
            @Nonnull OWLObjectPropertyExpression propertyExpression,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @return a transitive object property axiom on the specified argument
     */
    @Nonnull
    default OWLTransitiveObjectPropertyAxiom
            getOWLTransitiveObjectPropertyAxiom(
                    @Nonnull OWLObjectPropertyExpression property) {
        return getOWLTransitiveObjectPropertyAxiom(property,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a transitive object property axiom on the specified argument with
     *         annotations
     */
    @Nonnull
    OWLTransitiveObjectPropertyAxiom getOWLTransitiveObjectPropertyAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    // Data property axioms
    /**
     * @param subProperty
     *        sub Property
     * @param superProperty
     *        super Property
     * @return a subproperty axiom
     */
    @Nonnull
    default OWLSubDataPropertyOfAxiom getOWLSubDataPropertyOfAxiom(
            @Nonnull OWLDataPropertyExpression subProperty,
            @Nonnull OWLDataPropertyExpression superProperty) {
        return getOWLSubDataPropertyOfAxiom(subProperty, superProperty,
                Collections.emptySet());
    }

    /**
     * @param subProperty
     *        sub Property
     * @param superProperty
     *        super Property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a subproperty axiom with annotations
     */
    @Nonnull
    OWLSubDataPropertyOfAxiom getOWLSubDataPropertyOfAxiom(
            @Nonnull OWLDataPropertyExpression subProperty,
            @Nonnull OWLDataPropertyExpression superProperty,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @return an equivalent data properties axiom
     */
    @Nonnull
    default
            OWLEquivalentDataPropertiesAxiom
            getOWLEquivalentDataPropertiesAxiom(
                    @Nonnull Set<? extends OWLDataPropertyExpression> properties) {
        return getOWLEquivalentDataPropertiesAxiom(properties,
                Collections.emptySet());
    }

    /**
     * @param properties
     *        properties
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an equivalent data properties axiom with annotations
     */
    @Nonnull
    OWLEquivalentDataPropertiesAxiom getOWLEquivalentDataPropertiesAxiom(
            @Nonnull Set<? extends OWLDataPropertyExpression> properties,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @return an equivalent data properties axiom
     */
    @Nonnull
    default OWLEquivalentDataPropertiesAxiom
            getOWLEquivalentDataPropertiesAxiom(
                    @Nonnull OWLDataPropertyExpression... properties) {
        checkIterableNotNull(properties, "properties cannot be null", true);
        return getOWLEquivalentDataPropertiesAxiom(CollectionFactory
                .createSet(properties));
    }

    /**
     * @param propertyA
     *        property A
     * @param propertyB
     *        property B
     * @return an equivalent data properties axiom
     */
    @Nonnull
    default OWLEquivalentDataPropertiesAxiom
            getOWLEquivalentDataPropertiesAxiom(
                    @Nonnull OWLDataPropertyExpression propertyA,
                    @Nonnull OWLDataPropertyExpression propertyB) {
        return getOWLEquivalentDataPropertiesAxiom(propertyA, propertyB,
                Collections.emptySet());
    }

    /**
     * @param propertyA
     *        property A
     * @param propertyB
     *        property B
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an equivalent data properties axiom with annotations
     */
    @Nonnull
    default OWLEquivalentDataPropertiesAxiom
            getOWLEquivalentDataPropertiesAxiom(
                    @Nonnull OWLDataPropertyExpression propertyA,
                    @Nonnull OWLDataPropertyExpression propertyB,
                    @Nonnull Set<? extends OWLAnnotation> annotations) {
        return getOWLEquivalentDataPropertiesAxiom(
                CollectionFactory.createSet(propertyA, propertyB), annotations);
    }

    /**
     * @param dataProperties
     *        Cannot be null or contain nulls.
     * @return a disjoint data properties axiom with specified properties
     */
    @Nonnull
    default OWLDisjointDataPropertiesAxiom getOWLDisjointDataPropertiesAxiom(
            @Nonnull OWLDataPropertyExpression... dataProperties) {
        checkIterableNotNull(dataProperties, "properties cannot be null", true);
        return getOWLDisjointDataPropertiesAxiom(CollectionFactory
                .createSet(dataProperties));
    }

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @return a disjoint data properties axiom with specified properties
     */
    @Nonnull
    default OWLDisjointDataPropertiesAxiom getOWLDisjointDataPropertiesAxiom(
            @Nonnull Set<? extends OWLDataPropertyExpression> properties) {
        return getOWLDisjointDataPropertiesAxiom(properties,
                Collections.emptySet());
    }

    /**
     * @param properties
     *        Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a disjoint data properties axiom with specified properties and
     *         annotations
     */
    @Nonnull
    OWLDisjointDataPropertiesAxiom getOWLDisjointDataPropertiesAxiom(
            @Nonnull Set<? extends OWLDataPropertyExpression> properties,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @param domain
     *        domain
     * @return a data property domain axiom
     */
    @Nonnull
    default OWLDataPropertyDomainAxiom getOWLDataPropertyDomainAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLClassExpression domain) {
        return getOWLDataPropertyDomainAxiom(property, domain,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param domain
     *        domain
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a data property domain axiom with annotations
     */
    @Nonnull
    OWLDataPropertyDomainAxiom getOWLDataPropertyDomainAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLClassExpression domain,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @param owlDataRange
     *        data range
     * @return a data property range axiom
     */
    @Nonnull
    default OWLDataPropertyRangeAxiom getOWLDataPropertyRangeAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLDataRange owlDataRange) {
        return getOWLDataPropertyRangeAxiom(property, owlDataRange,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param owlDataRange
     *        data range
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a data property range axiom with annotations
     */
    @Nonnull
    OWLDataPropertyRangeAxiom getOWLDataPropertyRangeAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLDataRange owlDataRange,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @return a functional data property axiom
     */
    @Nonnull
    default OWLFunctionalDataPropertyAxiom getOWLFunctionalDataPropertyAxiom(
            @Nonnull OWLDataPropertyExpression property) {
        return getOWLFunctionalDataPropertyAxiom(property,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a functional data property axiom with annotations
     */
    @Nonnull
    OWLFunctionalDataPropertyAxiom getOWLFunctionalDataPropertyAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    // Data axioms
    /**
     * @param ce
     *        class expression
     * @param properties
     *        Cannot be null or contain nulls.
     * @return a hasKey axiom on given arguments
     */
    @Nonnull
    default OWLHasKeyAxiom getOWLHasKeyAxiom(@Nonnull OWLClassExpression ce,
            @Nonnull Set<? extends OWLPropertyExpression> properties) {
        return getOWLHasKeyAxiom(ce, properties, Collections.emptySet());
    }

    /**
     * @param ce
     *        class expression
     * @param properties
     *        Cannot be null or contain nulls.
     * @return a hasKey axiom on given arguments
     */
    @Nonnull
    default OWLHasKeyAxiom getOWLHasKeyAxiom(@Nonnull OWLClassExpression ce,
            @Nonnull OWLPropertyExpression... properties) {
        checkIterableNotNull(properties, "properties cannot be null", true);
        return getOWLHasKeyAxiom(ce, CollectionFactory.createSet(properties));
    }

    /**
     * @param ce
     *        class expression
     * @param objectProperties
     *        Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a hasKey axiom on given arguments and annotations
     */
    @Nonnull
    OWLHasKeyAxiom getOWLHasKeyAxiom(@Nonnull OWLClassExpression ce,
            @Nonnull Set<? extends OWLPropertyExpression> objectProperties,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param datatype
     *        data type
     * @param dataRange
     *        data Range
     * @return a datatype definition axiom
     */
    @Nonnull
    default OWLDatatypeDefinitionAxiom getOWLDatatypeDefinitionAxiom(
            @Nonnull OWLDatatype datatype, @Nonnull OWLDataRange dataRange) {
        return getOWLDatatypeDefinitionAxiom(datatype, dataRange,
                Collections.emptySet());
    }

    /**
     * @param datatype
     *        data type
     * @param dataRange
     *        data Range
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a datatype definition axiom with annotations
     */
    @Nonnull
    OWLDatatypeDefinitionAxiom getOWLDatatypeDefinitionAxiom(
            @Nonnull OWLDatatype datatype, @Nonnull OWLDataRange dataRange,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    // Assertion (Individual) axioms
    /**
     * @param individuals
     *        Cannot be null or contain nulls.
     * @return a same individuals axiom with specified individuals
     */
    @Nonnull
    default OWLSameIndividualAxiom getOWLSameIndividualAxiom(
            @Nonnull Set<? extends OWLIndividual> individuals) {
        return getOWLSameIndividualAxiom(individuals, Collections.emptySet());
    }

    /**
     * @param individual
     *        individual
     * @return a same individuals axiom with specified individuals
     */
    @Nonnull
    default OWLSameIndividualAxiom getOWLSameIndividualAxiom(
            @Nonnull OWLIndividual... individual) {
        checkIterableNotNull(individual, "individuals cannot be null", true);
        Set<OWLIndividual> inds = new HashSet<>();
        inds.addAll(Arrays.asList(individual));
        return getOWLSameIndividualAxiom(inds);
    }

    /**
     * @param individuals
     *        Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a same individuals axiom with specified individuals and
     *         annotations
     */
    @Nonnull
    OWLSameIndividualAxiom getOWLSameIndividualAxiom(
            @Nonnull Set<? extends OWLIndividual> individuals,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param individuals
     *        Cannot be null or contain nulls.
     * @return a different individuals axiom with specified individuals
     */
    @Nonnull
    default OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(
            @Nonnull Set<? extends OWLIndividual> individuals) {
        return getOWLDifferentIndividualsAxiom(individuals,
                Collections.emptySet());
    }

    /**
     * @param individuals
     *        Cannot be null or contain nulls.
     * @return a different individuals axiom with specified individuals
     */
    @Nonnull
    default OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(
            @Nonnull OWLIndividual... individuals) {
        checkIterableNotNull(individuals, "individuals cannot be null", true);
        return getOWLDifferentIndividualsAxiom(CollectionFactory
                .createSet(individuals));
    }

    /**
     * @param individuals
     *        Cannot be null or contain nulls.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a different individuals axiom with specified individuals and
     *         annotations
     */
    @Nonnull
    OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(
            @Nonnull Set<? extends OWLIndividual> individuals,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param classExpression
     *        class Expression
     * @param individual
     *        individual
     * @return a class assertion axiom
     */
    @Nonnull
    default OWLClassAssertionAxiom getOWLClassAssertionAxiom(
            @Nonnull OWLClassExpression classExpression,
            @Nonnull OWLIndividual individual) {
        return getOWLClassAssertionAxiom(classExpression, individual,
                Collections.emptySet());
    }

    /**
     * @param classExpression
     *        class Expression
     * @param individual
     *        individual
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a class assertion axiom with annotations
     */
    @Nonnull
    OWLClassAssertionAxiom getOWLClassAssertionAxiom(
            @Nonnull OWLClassExpression classExpression,
            @Nonnull OWLIndividual individual,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @param individual
     *        individual
     * @param object
     *        object
     * @return an object property assertion
     */
    @Nonnull
    default OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLIndividual individual, @Nonnull OWLIndividual object) {
        return getOWLObjectPropertyAssertionAxiom(property, individual, object,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param individual
     *        individual
     * @param object
     *        object
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an object property assertion with annotations
     */
    @Nonnull
    OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(
            @Nonnull OWLObjectPropertyExpression property,
            @Nonnull OWLIndividual individual, @Nonnull OWLIndividual object,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param object
     *        object
     * @return a negative property assertion axiom on given arguments
     */
    @Nonnull
    default OWLNegativeObjectPropertyAssertionAxiom
            getOWLNegativeObjectPropertyAssertionAxiom(
                    @Nonnull OWLObjectPropertyExpression property,
                    @Nonnull OWLIndividual subject,
                    @Nonnull OWLIndividual object) {
        return getOWLNegativeObjectPropertyAssertionAxiom(property, subject,
                object, Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param object
     *        object
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a negative property assertion axiom on given arguments with
     *         annotations
     */
    @Nonnull
    OWLNegativeObjectPropertyAssertionAxiom
            getOWLNegativeObjectPropertyAssertionAxiom(
                    @Nonnull OWLObjectPropertyExpression property,
                    @Nonnull OWLIndividual subject,
                    @Nonnull OWLIndividual object,
                    @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param object
     *        object
     * @return a data property assertion
     */
    @Nonnull
    default OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLIndividual subject, @Nonnull OWLLiteral object) {
        return getOWLDataPropertyAssertionAxiom(property, subject, object,
                Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param object
     *        object
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a data property assertion with annotations
     */
    @Nonnull
    OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLIndividual subject, @Nonnull OWLLiteral object,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param value
     *        value
     * @return a data property assertion
     */
    @Nonnull
    default OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLIndividual subject, int value) {
        return getOWLDataPropertyAssertionAxiom(property, subject,
                getOWLLiteral(value), Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param value
     *        value
     * @return a data property assertion
     */
    @Nonnull
    default OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLIndividual subject, double value) {
        return getOWLDataPropertyAssertionAxiom(property, subject,
                getOWLLiteral(value), Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param value
     *        value
     * @return a data property assertion
     */
    @Nonnull
    default OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLIndividual subject, float value) {
        return getOWLDataPropertyAssertionAxiom(property, subject,
                getOWLLiteral(value), Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param value
     *        value
     * @return a data property assertion
     */
    @Nonnull
    default OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLIndividual subject, boolean value) {
        return getOWLDataPropertyAssertionAxiom(property, subject,
                getOWLLiteral(value), Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param value
     *        value
     * @return a data property assertion
     */
    @Nonnull
    default OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(
            @Nonnull OWLDataPropertyExpression property,
            @Nonnull OWLIndividual subject, @Nonnull String value) {
        return getOWLDataPropertyAssertionAxiom(property, subject,
                getOWLLiteral(value), Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param object
     *        object
     * @return a negative property assertion axiom on given arguments
     */
    @Nonnull
    default OWLNegativeDataPropertyAssertionAxiom
            getOWLNegativeDataPropertyAssertionAxiom(
                    @Nonnull OWLDataPropertyExpression property,
                    @Nonnull OWLIndividual subject, @Nonnull OWLLiteral object) {
        return getOWLNegativeDataPropertyAssertionAxiom(property, subject,
                object, Collections.emptySet());
    }

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param object
     *        object
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a negative property assertion axiom on given arguments with
     *         annotations
     */
    @Nonnull
    OWLNegativeDataPropertyAssertionAxiom
            getOWLNegativeDataPropertyAssertionAxiom(
                    @Nonnull OWLDataPropertyExpression property,
                    @Nonnull OWLIndividual subject, @Nonnull OWLLiteral object,
                    @Nonnull Set<? extends OWLAnnotation> annotations);

    // Annotations
    /**
     * Gets an annotation
     * 
     * @param property
     *        the annotation property.
     * @param value
     *        The annotation value.
     * @return The annotation on the specified property with the specified value
     */
    @Nonnull
    default OWLAnnotation getOWLAnnotation(
            @Nonnull OWLAnnotationProperty property,
            @Nonnull OWLAnnotationValue value) {
        return getOWLAnnotation(property, value, Collections.emptySet());
    }

    /**
     * Gets an annotation
     * 
     * @param property
     *        the annotation property.
     * @param value
     *        The annotation value.
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls. nulls.
     * @return The annotation on the specified property with the specified value
     */
    @Nonnull
    OWLAnnotation getOWLAnnotation(@Nonnull OWLAnnotationProperty property,
            @Nonnull OWLAnnotationValue value,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    // Annotation axioms
    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param value
     *        value
     * @return an annotation assertion axiom
     */
    @Nonnull
    default OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(
            @Nonnull OWLAnnotationProperty property,
            @Nonnull OWLAnnotationSubject subject,
            @Nonnull OWLAnnotationValue value) {
        return getOWLAnnotationAssertionAxiom(property, subject, value,
                Collections.emptySet());
    }

    /**
     * @param subject
     *        subject
     * @param annotation
     *        annotation
     * @return an annotation assertion axiom
     */
    @Nonnull
    OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(
            @Nonnull OWLAnnotationSubject subject,
            @Nonnull OWLAnnotation annotation);

    /**
     * @param property
     *        property
     * @param subject
     *        subject
     * @param value
     *        value
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an annotation assertion axiom - with annotations
     */
    @Nonnull
    OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(
            @Nonnull OWLAnnotationProperty property,
            @Nonnull OWLAnnotationSubject subject,
            @Nonnull OWLAnnotationValue value,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param subject
     *        subject
     * @param annotation
     *        annotation
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an annotation assertion axiom - with annotations
     */
    @Nonnull
    OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(
            @Nonnull OWLAnnotationSubject subject,
            @Nonnull OWLAnnotation annotation,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * Gets an annotation assertion that specifies that an IRI is deprecated.
     * The annotation property is owl:deprecated and the value of the annotation
     * is {@code "true"^^xsd:boolean}. (See <a href=
     * "http://www.w3.org/TR/2009/REC-owl2-syntax-20091027/#Annotation_Properties"
     * >Annotation Properties</a> in the OWL 2 Specification
     * 
     * @param subject
     *        The IRI to be deprecated.
     * @return The annotation assertion that deprecates the specified IRI.
     */
    @Nonnull
    OWLAnnotationAssertionAxiom getDeprecatedOWLAnnotationAssertionAxiom(
            @Nonnull IRI subject);

    /**
     * @param importedOntologyIRI
     *        imported ontology
     * @return an imports declaration
     */
    @Nonnull
    OWLImportsDeclaration getOWLImportsDeclaration(
            @Nonnull IRI importedOntologyIRI);

    /**
     * @param prop
     *        prop
     * @param domain
     *        domain
     * @return an annotation property domain assertion
     */
    @Nonnull
    default OWLAnnotationPropertyDomainAxiom
            getOWLAnnotationPropertyDomainAxiom(
                    @Nonnull OWLAnnotationProperty prop, @Nonnull IRI domain) {
        return getOWLAnnotationPropertyDomainAxiom(prop, domain,
                Collections.emptySet());
    }

    /**
     * @param prop
     *        prop
     * @param domain
     *        domain
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an annotation property domain assertion with annotations
     */
    @Nonnull
    OWLAnnotationPropertyDomainAxiom getOWLAnnotationPropertyDomainAxiom(
            @Nonnull OWLAnnotationProperty prop, @Nonnull IRI domain,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param prop
     *        prop
     * @param range
     *        range
     * @return an annotation property range assertion
     */
    @Nonnull
    default OWLAnnotationPropertyRangeAxiom getOWLAnnotationPropertyRangeAxiom(
            @Nonnull OWLAnnotationProperty prop, @Nonnull IRI range) {
        return getOWLAnnotationPropertyRangeAxiom(prop, range,
                Collections.emptySet());
    }

    /**
     * @param prop
     *        prop
     * @param range
     *        range
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return an annotation property range assertion with annotations
     */
    @Nonnull
    OWLAnnotationPropertyRangeAxiom getOWLAnnotationPropertyRangeAxiom(
            @Nonnull OWLAnnotationProperty prop, @Nonnull IRI range,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /**
     * @param sub
     *        sub property
     * @param sup
     *        super property
     * @return a sub annotation property axiom with specified properties
     */
    @Nonnull
    default OWLSubAnnotationPropertyOfAxiom getOWLSubAnnotationPropertyOfAxiom(
            @Nonnull OWLAnnotationProperty sub,
            @Nonnull OWLAnnotationProperty sup) {
        return getOWLSubAnnotationPropertyOfAxiom(sub, sup,
                Collections.emptySet());
    }

    /**
     * @param sub
     *        sub property
     * @param sup
     *        super property
     * @param annotations
     *        A set of annotations. Cannot be null or contain nulls.
     * @return a sub annotation property axiom with specified properties and
     *         annotations
     */
    @Nonnull
    OWLSubAnnotationPropertyOfAxiom getOWLSubAnnotationPropertyOfAxiom(
            @Nonnull OWLAnnotationProperty sub,
            @Nonnull OWLAnnotationProperty sup,
            @Nonnull Set<? extends OWLAnnotation> annotations);

    /** Empty all caches */
    void purge();
}
