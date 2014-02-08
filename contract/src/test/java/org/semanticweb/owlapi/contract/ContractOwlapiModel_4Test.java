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
package org.semanticweb.owlapi.contract;

import static org.mockito.Mockito.mock;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitor;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObjectVisitor;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyChangeVisitor;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLOntologyStorer;
import org.semanticweb.owlapi.model.OWLOntologyStorerNotFoundException;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;
import org.semanticweb.owlapi.model.OWLPropertyAxiom;
import org.semanticweb.owlapi.model.OWLPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLPropertyRange;
import org.semanticweb.owlapi.model.OWLPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLQuantifiedDataRestriction;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLQuantifiedRestriction;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiomSetShortCut;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiomShortCut;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLUnaryPropertyAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.RemoveImport;
import org.semanticweb.owlapi.model.RemoveOntologyAnnotation;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLBinaryAtom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataFactory;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObject;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLPredicate;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLUnaryAtom;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.model.SpecificOntologyChangeBroadcastStrategy;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

@SuppressWarnings({ "unused", "javadoc", "unchecked" })
public class ContractOwlapiModel_4Test {
    @Test
    public void shouldTestOWLOntologyStorageException() throws OWLException {
        OWLOntologyStorageException testSubject0 = new OWLOntologyStorageException(
                "");
        new OWLOntologyStorageException("", new RuntimeException());
        new OWLOntologyStorageException(new RuntimeException());
        Throwable result1 = testSubject0.getCause();
        String result4 = testSubject0.getMessage();
        String result5 = testSubject0.getLocalizedMessage();
    }

    @Test
    public void shouldTestInterfaceOWLOntologyStorer() throws OWLException,
            IOException {
        OWLOntologyStorer testSubject0 = mock(OWLOntologyStorer.class);
        boolean result0 = testSubject0
                .canStoreOntology(mock(OWLOntologyFormat.class));
        testSubject0.storeOntology(Utils.getMockOntology(), IRI("urn:aFake"),
                mock(OWLOntologyFormat.class));
        testSubject0.storeOntology(Utils.getMockOntology(),
                mock(OWLOntologyDocumentTarget.class),
                mock(OWLOntologyFormat.class));
    }

    @Test
    public void shouldTestOWLOntologyStorerNotFoundException()
            throws OWLException {
        OWLOntologyStorerNotFoundException testSubject0 = new OWLOntologyStorerNotFoundException(
                mock(OWLOntologyFormat.class));
        Throwable result1 = testSubject0.getCause();
        String result4 = testSubject0.getMessage();
        String result5 = testSubject0.getLocalizedMessage();
    }

    @Test
    public void shouldTestInterfaceOWLProperty() throws OWLException {
        OWLProperty testSubject0 = mock(OWLProperty.class);
        testSubject0.accept(mock(OWLPropertyExpressionVisitor.class));
        Object result0 = testSubject0.accept(Utils.mockPropertyExpression());
        boolean result1 = testSubject0.isAnonymous();
        boolean result16 = testSubject0.isDataPropertyExpression();
        boolean result17 = testSubject0.isObjectPropertyExpression();
        boolean result18 = testSubject0.isOWLTopObjectProperty();
        boolean result19 = testSubject0.isOWLBottomObjectProperty();
        boolean result20 = testSubject0.isOWLTopDataProperty();
        boolean result21 = testSubject0.isOWLBottomDataProperty();
        Set<OWLEntity> result22 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result23 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result24 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result25 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result26 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result27 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result28 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result29 = testSubject0.getDatatypesInSignature();
        boolean result31 = testSubject0.isTopEntity();
        boolean result32 = testSubject0.isBottomEntity();
        testSubject0.accept(mock(OWLEntityVisitor.class));
        Object result36 = testSubject0.accept(Utils.mockEntity());
        boolean result37 = testSubject0.isType(EntityType.CLASS);
        boolean result41 = testSubject0.isBuiltIn();
        EntityType<?> result42 = testSubject0.getEntityType();
        boolean result44 = !testSubject0.isAnonymous();
        if (!testSubject0.isAnonymous()) {
            OWLClass result45 = testSubject0.asOWLClass();
        }
        boolean result46 = testSubject0.isOWLObjectProperty();
        if (testSubject0.isOWLObjectProperty()) {
            OWLObjectProperty result47 = testSubject0.asOWLObjectProperty();
        }
        boolean result48 = testSubject0.isOWLDataProperty();
        if (testSubject0.isOWLDataProperty()) {
            OWLDataProperty result49 = testSubject0.asOWLDataProperty();
        }
        boolean result50 = testSubject0.isOWLNamedIndividual();
        if (testSubject0.isOWLNamedIndividual()) {
            OWLNamedIndividual result51 = testSubject0.asOWLNamedIndividual();
        }
        boolean result52 = testSubject0.isOWLDatatype();
        if (testSubject0.isOWLDatatype()) {
            OWLDatatype result53 = testSubject0.asOWLDatatype();
        }
        boolean result54 = testSubject0.isOWLAnnotationProperty();
        if (testSubject0.isOWLAnnotationProperty()) {
            OWLAnnotationProperty result55 = testSubject0
                    .asOWLAnnotationProperty();
        }
        String result56 = testSubject0.toStringID();
        testSubject0.accept(mock(OWLNamedObjectVisitor.class));
        IRI result57 = testSubject0.getIRI();
    }

    @Test
    public void shouldTestInterfaceOWLPropertyAssertionAxiom()
            throws OWLException {
        OWLPropertyAssertionAxiom<OWLObjectPropertyExpression, OWLIndividual> testSubject0 = mock(OWLPropertyAssertionAxiom.class);
        OWLObjectPropertyExpression result0 = testSubject0.getProperty();
        OWLPropertyAssertionObject result1 = testSubject0.getObject();
        OWLIndividual result2 = testSubject0.getSubject();
        Set<OWLAnnotation> result3 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result4 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result5 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result6 = testSubject0.getAxiomWithoutAnnotations();
        boolean result8 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result9 = testSubject0.isLogicalAxiom();
        boolean result10 = testSubject0.isAnnotationAxiom();
        boolean result11 = testSubject0.isAnnotated();
        AxiomType<?> result12 = testSubject0.getAxiomType();
        boolean result13 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result14 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result25 = testSubject0.isTopEntity();
        boolean result26 = testSubject0.isBottomEntity();
        OWLSubClassOfAxiom result28 = testSubject0.asOWLSubClassOfAxiom();
    }

    @Test
    public void shouldTestInterfaceOWLPropertyAssertionObject()
            throws OWLException {
        OWLPropertyAssertionObject testSubject0 = mock(OWLPropertyAssertionObject.class);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result9 = testSubject0.isTopEntity();
        boolean result10 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLPropertyAxiom() throws OWLException {
        OWLPropertyAxiom testSubject0 = mock(OWLPropertyAxiom.class);
        Set<OWLAnnotation> result0 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result1 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result2 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result3 = testSubject0.getAxiomWithoutAnnotations();
        boolean result5 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result6 = testSubject0.isLogicalAxiom();
        boolean result7 = testSubject0.isAnnotationAxiom();
        boolean result8 = testSubject0.isAnnotated();
        AxiomType<?> result9 = testSubject0.getAxiomType();
        boolean result10 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result11 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result22 = testSubject0.isTopEntity();
        boolean result23 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLPropertyDomainAxiom() throws OWLException {
        OWLPropertyDomainAxiom<OWLObjectPropertyExpression> testSubject0 = mock(OWLPropertyDomainAxiom.class);
        OWLClassExpression result0 = testSubject0.getDomain();
        OWLObjectPropertyExpression result1 = testSubject0.getProperty();
        Set<OWLAnnotation> result2 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result3 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result4 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result5 = testSubject0.getAxiomWithoutAnnotations();
        boolean result7 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result8 = testSubject0.isLogicalAxiom();
        boolean result9 = testSubject0.isAnnotationAxiom();
        boolean result10 = testSubject0.isAnnotated();
        AxiomType<?> result11 = testSubject0.getAxiomType();
        boolean result12 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result13 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result24 = testSubject0.isTopEntity();
        boolean result25 = testSubject0.isBottomEntity();
        OWLSubClassOfAxiom result27 = testSubject0.asOWLSubClassOfAxiom();
    }

    @Test
    public void shouldTestInterfaceOWLPropertyExpression() throws OWLException {
        OWLPropertyExpression testSubject0 = mock(OWLPropertyExpression.class);
        testSubject0.accept(mock(OWLPropertyExpressionVisitor.class));
        Object result0 = testSubject0.accept(Utils.mockPropertyExpression());
        boolean result1 = testSubject0.isAnonymous();
        boolean result16 = testSubject0.isDataPropertyExpression();
        boolean result17 = testSubject0.isObjectPropertyExpression();
        boolean result18 = testSubject0.isOWLTopObjectProperty();
        boolean result19 = testSubject0.isOWLBottomObjectProperty();
        boolean result20 = testSubject0.isOWLTopDataProperty();
        boolean result21 = testSubject0.isOWLBottomDataProperty();
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result31 = testSubject0.isTopEntity();
        boolean result32 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLPropertyExpressionVisitor()
            throws OWLException {
        OWLPropertyExpressionVisitor testSubject0 = mock(OWLPropertyExpressionVisitor.class);
    }

    @Test
    public void shouldTestInterfaceOWLPropertyExpressionVisitorEx()
            throws OWLException {
        OWLPropertyExpressionVisitorEx<OWLObject> testSubject0 = Utils
                .mockPropertyExpression();
    }

    @Test
    public void shouldTestInterfaceOWLPropertyRange() throws OWLException {
        OWLPropertyRange testSubject0 = mock(OWLPropertyRange.class);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result9 = testSubject0.isTopEntity();
        boolean result10 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLPropertyRangeAxiom() throws OWLException {
        OWLPropertyRangeAxiom<OWLObjectPropertyExpression, OWLPropertyRange> testSubject0 = mock(OWLPropertyRangeAxiom.class);
        OWLPropertyRange result0 = testSubject0.getRange();
        OWLObjectPropertyExpression result1 = testSubject0.getProperty();
        Set<OWLAnnotation> result2 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result3 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result4 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result5 = testSubject0.getAxiomWithoutAnnotations();
        boolean result7 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result8 = testSubject0.isLogicalAxiom();
        boolean result9 = testSubject0.isAnnotationAxiom();
        boolean result10 = testSubject0.isAnnotated();
        AxiomType<?> result11 = testSubject0.getAxiomType();
        boolean result12 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result13 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result24 = testSubject0.isTopEntity();
        boolean result25 = testSubject0.isBottomEntity();
        OWLSubClassOfAxiom result27 = testSubject0.asOWLSubClassOfAxiom();
    }

    @Test
    public void shouldTestInterfaceOWLQuantifiedDataRestriction()
            throws OWLException {
        OWLQuantifiedDataRestriction testSubject0 = mock(OWLQuantifiedDataRestriction.class);
        OWLPropertyRange result0 = testSubject0.getFiller();
        OWLDataPropertyExpression result1 = testSubject0.getProperty();
        boolean result2 = testSubject0.isObjectRestriction();
        boolean result3 = testSubject0.isDataRestriction();
        Object result4 = testSubject0.accept(Utils.mockClassExpression());
        testSubject0.accept(mock(OWLClassExpressionVisitor.class));
        boolean result5 = testSubject0.isAnonymous();
        if (!testSubject0.isAnonymous()) {
            OWLClass result6 = testSubject0.asOWLClass();
        }
        ClassExpressionType result8 = testSubject0.getClassExpressionType();
        boolean result9 = testSubject0.isClassExpressionLiteral();
        boolean result10 = testSubject0.isOWLThing();
        boolean result11 = testSubject0.isOWLNothing();
        OWLClassExpression result13 = testSubject0.getObjectComplementOf();
        Set<OWLClassExpression> result14 = testSubject0.asConjunctSet();
        boolean result15 = testSubject0.containsConjunct(Utils.mockAnonClass());
        Set<OWLClassExpression> result16 = testSubject0.asDisjunctSet();
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result26 = testSubject0.isTopEntity();
        boolean result27 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLQuantifiedObjectRestriction()
            throws OWLException {
        OWLQuantifiedObjectRestriction testSubject0 = mock(OWLQuantifiedObjectRestriction.class);
        OWLPropertyRange result0 = testSubject0.getFiller();
        OWLObjectPropertyExpression result1 = testSubject0.getProperty();
        boolean result2 = testSubject0.isObjectRestriction();
        boolean result3 = testSubject0.isDataRestriction();
        Object result4 = testSubject0.accept(Utils.mockClassExpression());
        testSubject0.accept(mock(OWLClassExpressionVisitor.class));
        boolean result5 = testSubject0.isAnonymous();
        if (!testSubject0.isAnonymous()) {
            OWLClass result6 = testSubject0.asOWLClass();
        }
        ClassExpressionType result8 = testSubject0.getClassExpressionType();
        boolean result9 = testSubject0.isClassExpressionLiteral();
        boolean result10 = testSubject0.isOWLThing();
        boolean result11 = testSubject0.isOWLNothing();
        OWLClassExpression result13 = testSubject0.getObjectComplementOf();
        Set<OWLClassExpression> result14 = testSubject0.asConjunctSet();
        boolean result15 = testSubject0.containsConjunct(Utils.mockAnonClass());
        Set<OWLClassExpression> result16 = testSubject0.asDisjunctSet();
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result26 = testSubject0.isTopEntity();
        boolean result27 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLQuantifiedRestriction()
            throws OWLException {
        OWLQuantifiedRestriction<OWLPropertyRange> testSubject0 = mock(OWLQuantifiedRestriction.class);
        OWLPropertyRange result0 = testSubject0.getFiller();
        boolean result2 = testSubject0.isObjectRestriction();
        boolean result3 = testSubject0.isDataRestriction();
        Object result4 = testSubject0.accept(Utils.mockClassExpression());
        testSubject0.accept(mock(OWLClassExpressionVisitor.class));
        boolean result5 = testSubject0.isAnonymous();
        if (!testSubject0.isAnonymous()) {
            OWLClass result6 = testSubject0.asOWLClass();
        }
        ClassExpressionType result8 = testSubject0.getClassExpressionType();
        boolean result9 = testSubject0.isClassExpressionLiteral();
        boolean result10 = testSubject0.isOWLThing();
        boolean result11 = testSubject0.isOWLNothing();
        OWLClassExpression result13 = testSubject0.getObjectComplementOf();
        Set<OWLClassExpression> result14 = testSubject0.asConjunctSet();
        boolean result15 = testSubject0.containsConjunct(Utils.mockAnonClass());
        Set<OWLClassExpression> result16 = testSubject0.asDisjunctSet();
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result26 = testSubject0.isTopEntity();
        boolean result27 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLReflexiveObjectPropertyAxiom()
            throws OWLException {
        OWLReflexiveObjectPropertyAxiom testSubject0 = mock(OWLReflexiveObjectPropertyAxiom.class);
        OWLReflexiveObjectPropertyAxiom result0 = testSubject0
                .getAxiomWithoutAnnotations();
        Set<OWLAnnotation> result1 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result2 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result4 = testSubject0.getAxiomWithoutAnnotations();
        boolean result6 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result7 = testSubject0.isLogicalAxiom();
        boolean result8 = testSubject0.isAnnotationAxiom();
        boolean result9 = testSubject0.isAnnotated();
        AxiomType<?> result10 = testSubject0.getAxiomType();
        boolean result11 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result12 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result23 = testSubject0.isTopEntity();
        boolean result24 = testSubject0.isBottomEntity();
        OWLObjectPropertyExpression result26 = testSubject0.getProperty();
        OWLSubClassOfAxiom result27 = testSubject0.asOWLSubClassOfAxiom();
    }

    @Test
    public void shouldTestInterfaceOWLRestriction() throws OWLException {
        OWLRestriction testSubject0 = mock(OWLRestriction.class);
        boolean result1 = testSubject0.isObjectRestriction();
        boolean result2 = testSubject0.isDataRestriction();
        Object result3 = testSubject0.accept(Utils.mockClassExpression());
        testSubject0.accept(mock(OWLClassExpressionVisitor.class));
        boolean result4 = testSubject0.isAnonymous();
        if (!testSubject0.isAnonymous()) {
            OWLClass result5 = testSubject0.asOWLClass();
        }
        ClassExpressionType result7 = testSubject0.getClassExpressionType();
        boolean result8 = testSubject0.isClassExpressionLiteral();
        boolean result9 = testSubject0.isOWLThing();
        boolean result10 = testSubject0.isOWLNothing();
        OWLClassExpression result12 = testSubject0.getObjectComplementOf();
        Set<OWLClassExpression> result13 = testSubject0.asConjunctSet();
        boolean result14 = testSubject0.containsConjunct(Utils.mockAnonClass());
        Set<OWLClassExpression> result15 = testSubject0.asDisjunctSet();
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result25 = testSubject0.isTopEntity();
        boolean result26 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestOWLRuntimeException() throws OWLException {
        OWLRuntimeException testSubject0 = new OWLRuntimeException();
        new OWLRuntimeException("");
        new OWLRuntimeException("", new RuntimeException());
        new OWLRuntimeException(new RuntimeException());
        Throwable result1 = testSubject0.getCause();
        String result4 = testSubject0.getMessage();
        String result5 = testSubject0.getLocalizedMessage();
    }

    @Test
    public void shouldTestInterfaceOWLSameIndividualAxiom() throws OWLException {
        OWLSameIndividualAxiom testSubject0 = mock(OWLSameIndividualAxiom.class);
        OWLSameIndividualAxiom result0 = testSubject0
                .getAxiomWithoutAnnotations();
        boolean result1 = testSubject0.containsAnonymousIndividuals();
        Set<OWLSameIndividualAxiom> result2 = testSubject0.asPairwiseAxioms();
        Set<OWLIndividual> result3 = testSubject0.getIndividuals();
        List<OWLIndividual> result4 = testSubject0.getIndividualsAsList();
        Set<OWLAnnotation> result5 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result6 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result7 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result8 = testSubject0.getAxiomWithoutAnnotations();
        boolean result10 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result11 = testSubject0.isLogicalAxiom();
        boolean result12 = testSubject0.isAnnotationAxiom();
        boolean result13 = testSubject0.isAnnotated();
        AxiomType<?> result14 = testSubject0.getAxiomType();
        boolean result15 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result16 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result27 = testSubject0.isTopEntity();
        boolean result28 = testSubject0.isBottomEntity();
        Set<OWLSubClassOfAxiom> result30 = testSubject0.asOWLSubClassOfAxioms();
    }

    @Test
    public void shouldTestInterfaceOWLSubAnnotationPropertyOfAxiom()
            throws OWLException {
        OWLSubAnnotationPropertyOfAxiom testSubject0 = mock(OWLSubAnnotationPropertyOfAxiom.class);
        OWLSubAnnotationPropertyOfAxiom result0 = testSubject0
                .getAxiomWithoutAnnotations();
        OWLAnnotationProperty result1 = testSubject0.getSubProperty();
        OWLAnnotationProperty result2 = testSubject0.getSuperProperty();
        Set<OWLAnnotation> result3 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result4 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result5 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result6 = testSubject0.getAxiomWithoutAnnotations();
        boolean result8 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result9 = testSubject0.isLogicalAxiom();
        boolean result10 = testSubject0.isAnnotationAxiom();
        boolean result11 = testSubject0.isAnnotated();
        AxiomType<?> result12 = testSubject0.getAxiomType();
        boolean result13 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result14 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result25 = testSubject0.isTopEntity();
        boolean result26 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLSubClassOfAxiom() throws OWLException {
        OWLSubClassOfAxiom testSubject0 = mock(OWLSubClassOfAxiom.class);
        OWLSubClassOfAxiom result0 = testSubject0.getAxiomWithoutAnnotations();
        OWLClassExpression result1 = testSubject0.getSubClass();
        OWLClassExpression result2 = testSubject0.getSuperClass();
        boolean result3 = testSubject0.isGCI();
        Set<OWLAnnotation> result4 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result5 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result6 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result7 = testSubject0.getAxiomWithoutAnnotations();
        boolean result9 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result10 = testSubject0.isLogicalAxiom();
        boolean result11 = testSubject0.isAnnotationAxiom();
        boolean result12 = testSubject0.isAnnotated();
        AxiomType<?> result13 = testSubject0.getAxiomType();
        boolean result14 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result15 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result26 = testSubject0.isTopEntity();
        boolean result27 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLSubClassOfAxiomSetShortCut()
            throws OWLException {
        OWLSubClassOfAxiomSetShortCut testSubject0 = mock(OWLSubClassOfAxiomSetShortCut.class);
        Set<OWLSubClassOfAxiom> result0 = testSubject0.asOWLSubClassOfAxioms();
    }

    @Test
    public void shouldTestInterfaceOWLSubClassOfAxiomShortCut()
            throws OWLException {
        OWLSubClassOfAxiomShortCut testSubject0 = mock(OWLSubClassOfAxiomShortCut.class);
        OWLSubClassOfAxiom result0 = testSubject0.asOWLSubClassOfAxiom();
    }

    @Test
    public void shouldTestInterfaceOWLSubDataPropertyOfAxiom()
            throws OWLException {
        OWLSubDataPropertyOfAxiom testSubject0 = mock(OWLSubDataPropertyOfAxiom.class);
        OWLSubDataPropertyOfAxiom result0 = testSubject0
                .getAxiomWithoutAnnotations();
        OWLDataPropertyExpression result1 = testSubject0.getSubProperty();
        OWLDataPropertyExpression result2 = testSubject0.getSuperProperty();
        Set<OWLAnnotation> result3 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result4 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result5 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result6 = testSubject0.getAxiomWithoutAnnotations();
        boolean result8 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result9 = testSubject0.isLogicalAxiom();
        boolean result10 = testSubject0.isAnnotationAxiom();
        boolean result11 = testSubject0.isAnnotated();
        AxiomType<?> result12 = testSubject0.getAxiomType();
        boolean result13 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result14 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result25 = testSubject0.isTopEntity();
        boolean result26 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLSubObjectPropertyOfAxiom()
            throws OWLException {
        OWLSubObjectPropertyOfAxiom testSubject0 = mock(OWLSubObjectPropertyOfAxiom.class);
        OWLSubObjectPropertyOfAxiom result0 = testSubject0
                .getAxiomWithoutAnnotations();
        OWLObjectPropertyExpression result1 = testSubject0.getSubProperty();
        OWLObjectPropertyExpression result2 = testSubject0.getSuperProperty();
        Set<OWLAnnotation> result3 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result4 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result5 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result6 = testSubject0.getAxiomWithoutAnnotations();
        boolean result8 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result9 = testSubject0.isLogicalAxiom();
        boolean result10 = testSubject0.isAnnotationAxiom();
        boolean result11 = testSubject0.isAnnotated();
        AxiomType<?> result12 = testSubject0.getAxiomType();
        boolean result13 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result14 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result25 = testSubject0.isTopEntity();
        boolean result26 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLSubPropertyAxiom() throws OWLException {
        OWLSubPropertyAxiom<OWLObjectPropertyExpression> testSubject0 = mock(OWLSubPropertyAxiom.class);
        OWLObjectPropertyExpression result0 = testSubject0.getSubProperty();
        OWLObjectPropertyExpression result1 = testSubject0.getSuperProperty();
        Set<OWLAnnotation> result2 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result3 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result4 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result5 = testSubject0.getAxiomWithoutAnnotations();
        boolean result7 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result8 = testSubject0.isLogicalAxiom();
        boolean result9 = testSubject0.isAnnotationAxiom();
        boolean result10 = testSubject0.isAnnotated();
        AxiomType<?> result11 = testSubject0.getAxiomType();
        boolean result12 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result13 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result24 = testSubject0.isTopEntity();
        boolean result25 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLSubPropertyChainOfAxiom()
            throws OWLException {
        OWLSubPropertyChainOfAxiom testSubject0 = mock(OWLSubPropertyChainOfAxiom.class);
        OWLSubPropertyChainOfAxiom result0 = testSubject0
                .getAxiomWithoutAnnotations();
        OWLObjectPropertyExpression result1 = testSubject0.getSuperProperty();
        List<?> result2 = testSubject0.getPropertyChain();
        boolean result3 = testSubject0.isEncodingOfTransitiveProperty();
        Set<OWLAnnotation> result4 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result5 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result6 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result7 = testSubject0.getAxiomWithoutAnnotations();
        boolean result9 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result10 = testSubject0.isLogicalAxiom();
        boolean result11 = testSubject0.isAnnotationAxiom();
        boolean result12 = testSubject0.isAnnotated();
        AxiomType<?> result13 = testSubject0.getAxiomType();
        boolean result14 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result15 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result26 = testSubject0.isTopEntity();
        boolean result27 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceOWLSymmetricObjectPropertyAxiom()
            throws OWLException {
        OWLSymmetricObjectPropertyAxiom testSubject0 = mock(OWLSymmetricObjectPropertyAxiom.class);
        OWLSymmetricObjectPropertyAxiom result0 = testSubject0
                .getAxiomWithoutAnnotations();
        Set<OWLSubObjectPropertyOfAxiom> result1 = testSubject0
                .asSubPropertyAxioms();
        Set<OWLAnnotation> result2 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result3 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result4 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result5 = testSubject0.getAxiomWithoutAnnotations();
        boolean result7 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result8 = testSubject0.isLogicalAxiom();
        boolean result9 = testSubject0.isAnnotationAxiom();
        boolean result10 = testSubject0.isAnnotated();
        AxiomType<?> result11 = testSubject0.getAxiomType();
        boolean result12 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result13 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result24 = testSubject0.isTopEntity();
        boolean result25 = testSubject0.isBottomEntity();
        OWLObjectPropertyExpression result27 = testSubject0.getProperty();
    }

    @Test
    public void shouldTestInterfaceOWLTransitiveObjectPropertyAxiom()
            throws OWLException {
        OWLTransitiveObjectPropertyAxiom testSubject0 = mock(OWLTransitiveObjectPropertyAxiom.class);
        OWLTransitiveObjectPropertyAxiom result0 = testSubject0
                .getAxiomWithoutAnnotations();
        Set<OWLAnnotation> result1 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result2 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result4 = testSubject0.getAxiomWithoutAnnotations();
        boolean result6 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result7 = testSubject0.isLogicalAxiom();
        boolean result8 = testSubject0.isAnnotationAxiom();
        boolean result9 = testSubject0.isAnnotated();
        AxiomType<?> result10 = testSubject0.getAxiomType();
        boolean result11 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result12 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result23 = testSubject0.isTopEntity();
        boolean result24 = testSubject0.isBottomEntity();
        OWLObjectPropertyExpression result26 = testSubject0.getProperty();
    }

    @Test
    public void shouldTestInterfaceOWLUnaryPropertyAxiom() throws OWLException {
        OWLUnaryPropertyAxiom<OWLObjectPropertyExpression> testSubject0 = mock(OWLUnaryPropertyAxiom.class);
        OWLObjectPropertyExpression result0 = testSubject0.getProperty();
        Set<OWLAnnotation> result1 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result2 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result4 = testSubject0.getAxiomWithoutAnnotations();
        boolean result6 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result7 = testSubject0.isLogicalAxiom();
        boolean result8 = testSubject0.isAnnotationAxiom();
        boolean result9 = testSubject0.isAnnotated();
        AxiomType<?> result10 = testSubject0.getAxiomType();
        boolean result11 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result12 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result23 = testSubject0.isTopEntity();
        boolean result24 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfacePrefixManager() throws OWLException {
        PrefixManager testSubject0 = new DefaultPrefixManager();
        String result0 = testSubject0.getPrefix("");
        IRI result1 = testSubject0.getIRI("");
        Map<String, String> result2 = testSubject0.getPrefixName2PrefixMap();
        Set<String> result3 = testSubject0.getPrefixNames();
        boolean result4 = testSubject0.containsPrefixMapping("");
        String result5 = testSubject0.getDefaultPrefix();
        String result6 = testSubject0.getPrefixIRI(IRI("urn:aFake"));
    }

    @Test
    public void shouldTestRemoveAxiom() throws OWLException {
        RemoveAxiom testSubject0 = new RemoveAxiom(Utils.getMockOntology(),
                mock(OWLAxiom.class));
        testSubject0.accept(mock(OWLOntologyChangeVisitor.class));
        Object result1 = testSubject0.accept(Utils.mockOntologyChange());
        OWLAxiom result2 = testSubject0.getAxiom();
        boolean result3 = testSubject0.isAxiomChange();
        boolean result4 = testSubject0.isImportChange();
        Set<OWLEntity> result5 = testSubject0.getSignature();
        OWLOntology result6 = testSubject0.getOntology();
    }

    public void shouldTestRemoveImport() throws OWLException {
        RemoveImport testSubject0 = new RemoveImport(Utils.getMockOntology(),
                mock(OWLImportsDeclaration.class));
        testSubject0.accept(mock(OWLOntologyChangeVisitor.class));
        Object result1 = testSubject0.accept(Utils.mockOntologyChange());
        OWLAxiom result2 = testSubject0.getAxiom();
        boolean result3 = testSubject0.isAxiomChange();
        boolean result4 = testSubject0.isImportChange();
        OWLImportsDeclaration result5 = testSubject0.getImportDeclaration();
        OWLOntology result6 = testSubject0.getOntology();
    }

    @Test
    public void shouldTestRemoveOntologyAnnotation() throws OWLException {
        RemoveOntologyAnnotation testSubject0 = new RemoveOntologyAnnotation(
                Utils.getMockOntology(), mock(OWLAnnotation.class));
        OWLAnnotation result1 = testSubject0.getAnnotation();
        Object result2 = testSubject0.accept(Utils.mockOntologyChange());
        testSubject0.accept(mock(OWLOntologyChangeVisitor.class));
        boolean result4 = testSubject0.isAxiomChange();
        boolean result5 = testSubject0.isImportChange();
        OWLOntology result6 = testSubject0.getOntology();
    }

    public void shouldTestSetOntologyID() throws OWLException {
        SetOntologyID testSubject0 = new SetOntologyID(Utils.getMockOntology(),
                new OWLOntologyID());
        testSubject0.accept(mock(OWLOntologyChangeVisitor.class));
        Object result1 = testSubject0.accept(Utils.mockOntologyChange());
        OWLAxiom result2 = testSubject0.getAxiom();
        boolean result3 = testSubject0.isAxiomChange();
        boolean result4 = testSubject0.isImportChange();
        OWLOntologyID result5 = testSubject0.getOriginalOntologyID();
        OWLOntologyID result6 = testSubject0.getNewOntologyID();
        OWLOntology result7 = testSubject0.getOntology();
    }

    @Test
    public void shouldTestSpecificOntologyChangeBroadcastStrategy()
            throws OWLException {
        SpecificOntologyChangeBroadcastStrategy testSubject0 = new SpecificOntologyChangeBroadcastStrategy(
                Utils.getMockOntology());
        testSubject0.broadcastChanges(mock(OWLOntologyChangeListener.class),
                Utils.mockList(mock(AddAxiom.class)));
    }

    @Test
    public void shouldTestInterfaceSWRLArgument() throws OWLException {
        SWRLArgument testSubject0 = mock(SWRLArgument.class);
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result0 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result1 = testSubject0.isTopEntity();
        boolean result11 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLAtom() throws OWLException {
        SWRLAtom testSubject0 = mock(SWRLAtom.class);
        SWRLPredicate result0 = testSubject0.getPredicate();
        Collection<SWRLArgument> result1 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result2 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result12 = testSubject0.isTopEntity();
        boolean result13 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLBinaryAtom() throws OWLException {
        SWRLBinaryAtom<SWRLArgument, SWRLArgument> testSubject0 = mock(SWRLBinaryAtom.class);
        SWRLArgument result0 = testSubject0.getFirstArgument();
        SWRLArgument result1 = testSubject0.getSecondArgument();
        SWRLPredicate result2 = testSubject0.getPredicate();
        Collection<SWRLArgument> result3 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result4 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result14 = testSubject0.isTopEntity();
        boolean result15 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLBuiltInAtom() throws OWLException {
        SWRLBuiltInAtom testSubject0 = mock(SWRLBuiltInAtom.class);
        IRI result0 = testSubject0.getPredicate();
        List<SWRLDArgument> result1 = testSubject0.getArguments();
        boolean result2 = testSubject0.isCoreBuiltIn();
        SWRLPredicate result3 = testSubject0.getPredicate();
        Collection<SWRLArgument> result4 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result5 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result15 = testSubject0.isTopEntity();
        boolean result16 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLClassAtom() throws OWLException {
        SWRLClassAtom testSubject0 = mock(SWRLClassAtom.class);
        OWLClassExpression result0 = testSubject0.getPredicate();
        SWRLArgument result1 = testSubject0.getArgument();
        SWRLPredicate result2 = testSubject0.getPredicate();
        Collection<SWRLArgument> result3 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result4 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result14 = testSubject0.isTopEntity();
        boolean result15 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLDArgument() throws OWLException {
        SWRLDArgument testSubject0 = mock(SWRLDArgument.class);
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result0 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result52 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result53 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result54 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result55 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result56 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result57 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result58 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result59 = testSubject0.getDatatypesInSignature();
        boolean result10 = testSubject0.isTopEntity();
        boolean result11 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLDataFactory() throws OWLException {
        SWRLDataFactory testSubject0 = mock(SWRLDataFactory.class);
        SWRLRule result2 = testSubject0.getSWRLRule(
                Utils.mockSet(mock(SWRLAtom.class)),
                Utils.mockSet(mock(SWRLAtom.class)));
        SWRLRule result3 = testSubject0.getSWRLRule(
                Utils.mockSet(mock(SWRLAtom.class)),
                Utils.mockSet(mock(SWRLAtom.class)),
                Utils.mockSet(mock(OWLAnnotation.class)));
        SWRLClassAtom result4 = testSubject0.getSWRLClassAtom(
                Utils.mockAnonClass(), mock(SWRLIArgument.class));
        SWRLDataRangeAtom result5 = testSubject0.getSWRLDataRangeAtom(
                mock(OWLDataRange.class), mock(SWRLDArgument.class));
        SWRLObjectPropertyAtom result6 = testSubject0
                .getSWRLObjectPropertyAtom(Utils.mockObjectProperty(),
                        mock(SWRLIArgument.class), mock(SWRLIArgument.class));
        SWRLDataPropertyAtom result7 = testSubject0.getSWRLDataPropertyAtom(
                mock(OWLDataPropertyExpression.class),
                mock(SWRLIArgument.class), mock(SWRLDArgument.class));
        SWRLBuiltInAtom result8 = testSubject0.getSWRLBuiltInAtom(
                IRI("urn:aFake"), Utils.mockList(mock(SWRLDArgument.class)));
        SWRLVariable result9 = testSubject0.getSWRLVariable(IRI("urn:aFake"));
        SWRLIndividualArgument result10 = testSubject0
                .getSWRLIndividualArgument(mock(OWLIndividual.class));
        SWRLLiteralArgument result11 = testSubject0
                .getSWRLLiteralArgument(mock(OWLLiteral.class));
        SWRLSameIndividualAtom result12 = testSubject0
                .getSWRLSameIndividualAtom(mock(SWRLIArgument.class),
                        mock(SWRLIArgument.class));
        SWRLDifferentIndividualsAtom result13 = testSubject0
                .getSWRLDifferentIndividualsAtom(mock(SWRLIArgument.class),
                        mock(SWRLIArgument.class));
    }

    @Test
    public void shouldTestInterfaceSWRLDataPropertyAtom() throws OWLException {
        SWRLDataPropertyAtom testSubject0 = mock(SWRLDataPropertyAtom.class);
        OWLDataPropertyExpression result0 = testSubject0.getPredicate();
        SWRLArgument result1 = testSubject0.getFirstArgument();
        SWRLArgument result2 = testSubject0.getSecondArgument();
        SWRLPredicate result3 = testSubject0.getPredicate();
        Collection<SWRLArgument> result4 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result5 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result12 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result13 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result14 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result11 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result15 = testSubject0.isTopEntity();
        boolean result16 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLDataRangeAtom() throws OWLException {
        SWRLDataRangeAtom testSubject0 = mock(SWRLDataRangeAtom.class);
        OWLDataRange result0 = testSubject0.getPredicate();
        SWRLArgument result1 = testSubject0.getArgument();
        SWRLPredicate result12 = testSubject0.getPredicate();
        Collection<SWRLArgument> result13 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result14 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result16 = testSubject0.isTopEntity();
        boolean result15 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLDifferentIndividualsAtom()
            throws OWLException {
        SWRLDifferentIndividualsAtom testSubject0 = mock(SWRLDifferentIndividualsAtom.class);
        SWRLArgument result0 = testSubject0.getFirstArgument();
        SWRLArgument result1 = testSubject0.getSecondArgument();
        SWRLPredicate result12 = testSubject0.getPredicate();
        Collection<SWRLArgument> result13 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result11 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result14 = testSubject0.isTopEntity();
        boolean result15 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLIArgument() throws OWLException {
        SWRLIArgument testSubject0 = mock(SWRLIArgument.class);
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result0 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result1 = testSubject0.isTopEntity();
        boolean result11 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLIndividualArgument() throws OWLException {
        SWRLIndividualArgument testSubject0 = mock(SWRLIndividualArgument.class);
        OWLIndividual result0 = testSubject0.getIndividual();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result1 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result11 = testSubject0.isTopEntity();
        boolean result12 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLLiteralArgument() throws OWLException {
        SWRLLiteralArgument testSubject0 = mock(SWRLLiteralArgument.class);
        OWLLiteral result0 = testSubject0.getLiteral();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result1 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result11 = testSubject0.isTopEntity();
        boolean result12 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLObject() throws OWLException {
        SWRLObject testSubject0 = mock(SWRLObject.class);
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result0 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result1 = testSubject0.isTopEntity();
        boolean result11 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLObjectPropertyAtom() throws OWLException {
        SWRLObjectPropertyAtom testSubject0 = mock(SWRLObjectPropertyAtom.class);
        OWLObjectPropertyExpression result0 = testSubject0.getPredicate();
        SWRLObjectPropertyAtom result1 = testSubject0.getSimplified();
        SWRLArgument result12 = testSubject0.getFirstArgument();
        SWRLArgument result13 = testSubject0.getSecondArgument();
        SWRLPredicate result14 = testSubject0.getPredicate();
        Collection<SWRLArgument> result15 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result11 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result16 = testSubject0.isTopEntity();
        boolean result17 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLObjectVisitor() throws OWLException {
        SWRLObjectVisitor testSubject0 = mock(SWRLObjectVisitor.class);
    }

    @Test
    public void shouldTestInterfaceSWRLObjectVisitorEx() throws OWLException {
        SWRLObjectVisitorEx<OWLObject> testSubject0 = Utils.mockSWRLObject();
    }

    @Test
    public void shouldTestInterfaceSWRLPredicate() throws OWLException {
        SWRLPredicate testSubject0 = mock(SWRLPredicate.class);
    }

    @Test
    public void shouldTestInterfaceSWRLRule() throws OWLException {
        SWRLRule testSubject0 = mock(SWRLRule.class);
        SWRLRule result0 = testSubject0.getAxiomWithoutAnnotations();
        SWRLRule result1 = testSubject0.getSimplified();
        Set<SWRLAtom> result2 = testSubject0.getBody();
        Set<SWRLAtom> result3 = testSubject0.getHead();
        Set<SWRLVariable> result4 = testSubject0.getVariables();
        boolean result5 = testSubject0.containsAnonymousClassExpressions();
        Set<OWLClassExpression> result6 = testSubject0.getClassAtomPredicates();
        Set<OWLAnnotation> result7 = testSubject0.getAnnotations();
        Set<OWLAnnotation> result8 = testSubject0
                .getAnnotations(mock(OWLAnnotationProperty.class));
        testSubject0.accept(mock(OWLAxiomVisitor.class));
        Object result9 = testSubject0.accept(Utils.mockAxiom());
        OWLAxiom result10 = testSubject0.getAxiomWithoutAnnotations();
        boolean result12 = testSubject0
                .equalsIgnoreAnnotations(mock(OWLAxiom.class));
        boolean result13 = testSubject0.isLogicalAxiom();
        boolean result14 = testSubject0.isAnnotationAxiom();
        boolean result15 = testSubject0.isAnnotated();
        AxiomType<?> result16 = testSubject0.getAxiomType();
        boolean result17 = testSubject0.isOfType(AxiomType.CLASS_ASSERTION);
        boolean result18 = testSubject0.isOfType(AxiomType.SUBCLASS_OF);
        Set<OWLEntity> result20 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result21 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result22 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result23 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result24 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result25 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result26 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result27 = testSubject0.getDatatypesInSignature();
        boolean result29 = testSubject0.isTopEntity();
        boolean result30 = testSubject0.isBottomEntity();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result32 = testSubject0.accept(Utils.mockSWRLObject());
    }

    @Test
    public void shouldTestInterfaceSWRLSameIndividualAtom() throws OWLException {
        SWRLSameIndividualAtom testSubject0 = mock(SWRLSameIndividualAtom.class);
        SWRLArgument result0 = testSubject0.getFirstArgument();
        SWRLArgument result1 = testSubject0.getSecondArgument();
        SWRLPredicate result12 = testSubject0.getPredicate();
        Collection<SWRLArgument> result13 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result11 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result14 = testSubject0.isTopEntity();
        boolean result15 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLUnaryAtom() throws OWLException {
        SWRLUnaryAtom<SWRLArgument> testSubject0 = mock(SWRLUnaryAtom.class);
        SWRLArgument result0 = testSubject0.getArgument();
        SWRLPredicate result1 = testSubject0.getPredicate();
        Collection<SWRLArgument> result2 = testSubject0.getAllArguments();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result4 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result5 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result6 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result7 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result8 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result9 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result10 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result11 = testSubject0.getDatatypesInSignature();
        boolean result13 = testSubject0.isTopEntity();
        boolean result14 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestInterfaceSWRLVariable() throws OWLException {
        SWRLVariable testSubject0 = mock(SWRLVariable.class);
        IRI result0 = testSubject0.getIRI();
        testSubject0.accept(mock(SWRLObjectVisitor.class));
        Object result1 = testSubject0.accept(Utils.mockSWRLObject());
        Set<OWLEntity> result2 = testSubject0.getSignature();
        testSubject0.accept(mock(OWLObjectVisitor.class));
        Object result3 = testSubject0.accept(Utils.mockObject());
        Set<OWLAnonymousIndividual> result4 = testSubject0
                .getAnonymousIndividuals();
        Set<OWLClass> result5 = testSubject0.getClassesInSignature();
        Set<OWLDataProperty> result6 = testSubject0
                .getDataPropertiesInSignature();
        Set<OWLObjectProperty> result7 = testSubject0
                .getObjectPropertiesInSignature();
        Set<OWLNamedIndividual> result8 = testSubject0
                .getIndividualsInSignature();
        Set<OWLDatatype> result9 = testSubject0.getDatatypesInSignature();
        boolean result11 = testSubject0.isTopEntity();
        boolean result12 = testSubject0.isBottomEntity();
    }

    @Test
    public void shouldTestUnknownOWLOntologyException() throws OWLException {
        UnknownOWLOntologyException testSubject0 = new UnknownOWLOntologyException(
                new OWLOntologyID());
        Throwable result1 = testSubject0.getCause();
        String result4 = testSubject0.getMessage();
        String result5 = testSubject0.getLocalizedMessage();
    }

    public void shouldTestUnloadableImportException() throws OWLException {
        UnloadableImportException testSubject0 = new UnloadableImportException(
                mock(OWLOntologyCreationException.class),
                mock(OWLImportsDeclaration.class));
        OWLImportsDeclaration result0 = testSubject0.getImportsDeclaration();
        OWLOntologyCreationException result1 = testSubject0
                .getOntologyCreationException();
        Throwable result3 = testSubject0.getCause();
        String result6 = testSubject0.getMessage();
        String result7 = testSubject0.getLocalizedMessage();
    }
}
