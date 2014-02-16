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
package org.semanticweb.owlapi.contract;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.metrics.AbstractOWLMetric;
import org.semanticweb.owlapi.metrics.AverageAssertedNamedSuperclassCount;
import org.semanticweb.owlapi.metrics.AxiomCount;
import org.semanticweb.owlapi.metrics.AxiomCountMetric;
import org.semanticweb.owlapi.metrics.AxiomTypeCountMetricFactory;
import org.semanticweb.owlapi.metrics.AxiomTypeMetric;
import org.semanticweb.owlapi.metrics.DLExpressivity;
import org.semanticweb.owlapi.metrics.DoubleValuedMetric;
import org.semanticweb.owlapi.metrics.GCICount;
import org.semanticweb.owlapi.metrics.HiddenGCICount;
import org.semanticweb.owlapi.metrics.ImportClosureSize;
import org.semanticweb.owlapi.metrics.IntegerValuedMetric;
import org.semanticweb.owlapi.metrics.LogicalAxiomCount;
import org.semanticweb.owlapi.metrics.MaximumNumberOfNamedSuperclasses;
import org.semanticweb.owlapi.metrics.NumberOfClassesWithMultipleInheritance;
import org.semanticweb.owlapi.metrics.OWLMetric;
import org.semanticweb.owlapi.metrics.OWLMetricManager;
import org.semanticweb.owlapi.metrics.ObjectCountMetric;
import org.semanticweb.owlapi.metrics.ReferencedClassCount;
import org.semanticweb.owlapi.metrics.ReferencedDataPropertyCount;
import org.semanticweb.owlapi.metrics.ReferencedIndividualCount;
import org.semanticweb.owlapi.metrics.ReferencedObjectPropertyCount;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;

@SuppressWarnings({ "unused", "javadoc", "unchecked" })
public class ContractMetricsTest {
    @Test
    public void shouldTestAbstractOWLMetric() throws OWLException {
        AbstractOWLMetric<Object> testSubject0 = new AbstractOWLMetric<Object>(
                Utils.getMockOntology()) {
            @Override
            public String getName() {
                return "";
            }

            @Override
            protected Object recomputeMetric() {
                return new Object();
            }

            @Override
            protected boolean isMetricInvalidated(
                    List<? extends OWLOntologyChange<?>> changes) {
                return false;
            }

            @Override
            protected void disposeMetric() {}
        };
        Object result1 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result2 = testSubject0.getOntologies();
        OWLOntology result3 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result4 = testSubject0.getManager();
        boolean result5 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
        String result6 = testSubject0.getName();
    }

    @Ignore
    @Test
    public void shouldTestAverageAssertedNamedSuperclassCount()
            throws OWLException {
        AverageAssertedNamedSuperclassCount testSubject0 = new AverageAssertedNamedSuperclassCount(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Double result1 = testSubject0.recomputeMetric();
        Object result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestAxiomCount() throws OWLException {
        AxiomCount testSubject0 = new AxiomCount(Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Object result2 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result3 = testSubject0.getOntologies();
        OWLOntology result4 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result5 = testSubject0.getManager();
        boolean result6 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestAxiomCountMetric() throws OWLException {
        AxiomCountMetric testSubject0 = new AxiomCountMetric(
                Utils.getMockOntology()) {
            @Override
            protected String getObjectTypeName() {
                return "";
            }

            @Override
            protected Set<? extends OWLAxiom> getObjects(OWLOntology ont) {
                return Collections.emptySet();
            }
        };
        Set<? extends OWLAxiom> result0 = testSubject0.getAxioms();
        String result1 = testSubject0.getName();
        Object result2 = testSubject0.recomputeMetric();
        Integer result3 = testSubject0.recomputeMetric();
        Object result5 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result6 = testSubject0.getOntologies();
        OWLOntology result7 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result8 = testSubject0.getManager();
        boolean result9 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestAxiomTypeCountMetricFactory() throws OWLException {
        new AxiomTypeCountMetricFactory();
        Set<OWLMetric<?>> result0 = AxiomTypeCountMetricFactory
                .createMetrics(Utils.getMockOntology());
    }

    @Ignore
    @Test
    public void shouldTestAxiomTypeMetric() throws OWLException {
        AxiomTypeMetric testSubject0 = new AxiomTypeMetric(
                Utils.getMockOntology(), mock(AxiomType.class));
        AxiomType<?> result0 = testSubject0.getAxiomType();
        Set<? extends OWLAxiom> result1 = testSubject0.getAxioms();
        String result2 = testSubject0.getName();
        Object result3 = testSubject0.recomputeMetric();
        Integer result4 = testSubject0.recomputeMetric();
        Object result6 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result7 = testSubject0.getOntologies();
        OWLOntology result8 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result9 = testSubject0.getManager();
        boolean result10 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestDLExpressivity() throws OWLException {
        DLExpressivity testSubject0 = new DLExpressivity(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        String result1 = testSubject0.recomputeMetric();
        Object result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Test
    public void shouldTestDoubleValuedMetric() throws OWLException {
        DoubleValuedMetric testSubject0 = new DoubleValuedMetric(
                Utils.getMockOntology()) {
            @Override
            public String getName() {
                return "";
            }

            @Override
            protected Double recomputeMetric() {
                return 0D;
            }

            @Override
            protected boolean isMetricInvalidated(
                    List<? extends OWLOntologyChange<?>> changes) {
                return false;
            }

            @Override
            protected void disposeMetric() {}
        };
        Object result1 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result2 = testSubject0.getOntologies();
        OWLOntology result3 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result4 = testSubject0.getManager();
        boolean result5 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
        String result6 = testSubject0.getName();
    }

    @Ignore
    @Test
    public void shouldTestGCICount() throws OWLException {
        GCICount testSubject0 = new GCICount(Utils.getMockOntology());
        Set<? extends OWLAxiom> result0 = testSubject0.getAxioms();
        String result1 = testSubject0.getName();
        Object result2 = testSubject0.recomputeMetric();
        Integer result3 = testSubject0.recomputeMetric();
        Object result5 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result6 = testSubject0.getOntologies();
        OWLOntology result7 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result8 = testSubject0.getManager();
        boolean result9 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestHiddenGCICount() throws OWLException {
        HiddenGCICount testSubject0 = new HiddenGCICount(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Object result2 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result3 = testSubject0.getOntologies();
        OWLOntology result4 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result5 = testSubject0.getManager();
        boolean result6 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestImportClosureSize() throws OWLException {
        ImportClosureSize testSubject0 = new ImportClosureSize(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Object result2 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result3 = testSubject0.getOntologies();
        OWLOntology result4 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result5 = testSubject0.getManager();
        boolean result6 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Test
    public void shouldTestIntegerValuedMetric() throws OWLException {
        IntegerValuedMetric testSubject0 = new IntegerValuedMetric(
                Utils.getMockOntology()) {
            @Override
            public String getName() {
                return "";
            }

            @Override
            protected Integer recomputeMetric() {
                return 1;
            }

            @Override
            protected boolean isMetricInvalidated(
                    List<? extends OWLOntologyChange<?>> changes) {
                return false;
            }

            @Override
            protected void disposeMetric() {}
        };
        Object result1 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result2 = testSubject0.getOntologies();
        OWLOntology result3 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result4 = testSubject0.getManager();
        boolean result5 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
        String result6 = testSubject0.getName();
    }

    @Ignore
    @Test
    public void shouldTestLogicalAxiomCount() throws OWLException {
        LogicalAxiomCount testSubject0 = new LogicalAxiomCount(
                Utils.getMockOntology());
        Set<? extends OWLAxiom> result0 = testSubject0.getAxioms();
        String result1 = testSubject0.getName();
        Object result2 = testSubject0.recomputeMetric();
        Integer result3 = testSubject0.recomputeMetric();
        Object result5 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result6 = testSubject0.getOntologies();
        OWLOntology result7 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result8 = testSubject0.getManager();
        boolean result9 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestMaximumNumberOfNamedSuperclasses()
            throws OWLException {
        MaximumNumberOfNamedSuperclasses testSubject0 = new MaximumNumberOfNamedSuperclasses(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Integer result1 = testSubject0.recomputeMetric();
        Object result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestNumberOfClassesWithMultipleInheritance()
            throws OWLException {
        NumberOfClassesWithMultipleInheritance testSubject0 = new NumberOfClassesWithMultipleInheritance(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Integer result1 = testSubject0.recomputeMetric();
        Object result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestObjectCountMetric() throws OWLException {
        ObjectCountMetric<Object> testSubject0 = new ObjectCountMetric<Object>(
                OWLManager.createOWLOntologyManager().createOntology()) {
            @Override
            protected String getObjectTypeName() {
                return "";
            }

            @Override
            protected Set<Object> getObjects(OWLOntology ont) {
                return Collections.emptySet();
            }
        };
        String result0 = testSubject0.getName();
        Object result1 = testSubject0.recomputeMetric();
        Integer result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Test
    public void shouldTestInterfaceOWLMetric() throws OWLException {
        OWLMetric<Object> testSubject0 = mock(OWLMetric.class);
        String result0 = testSubject0.getName();
        Object result1 = testSubject0.getValue();
        testSubject0.dispose();
        OWLOntology result2 = testSubject0.getOntology();
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result3 = testSubject0.getManager();
        boolean result4 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Test
    public void shouldTestOWLMetricManager() throws OWLException {
        OWLMetricManager testSubject0 = new OWLMetricManager(
                new ArrayList<OWLMetric<?>>());
        testSubject0.setOntology(Utils.getMockOntology());
        List<OWLMetric<?>> result1 = testSubject0.getMetrics();
    }

    @Ignore
    @Test
    public void shouldTestReferencedClassCount() throws OWLException {
        ReferencedClassCount testSubject0 = new ReferencedClassCount(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Object result1 = testSubject0.recomputeMetric();
        Integer result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestReferencedDataPropertyCount() throws OWLException {
        ReferencedDataPropertyCount testSubject0 = new ReferencedDataPropertyCount(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Object result1 = testSubject0.recomputeMetric();
        Integer result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestReferencedIndividualCount() throws OWLException {
        ReferencedIndividualCount testSubject0 = new ReferencedIndividualCount(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Object result1 = testSubject0.recomputeMetric();
        Integer result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }

    @Ignore
    @Test
    public void shouldTestReferencedObjectPropertyCount() throws OWLException {
        ReferencedObjectPropertyCount testSubject0 = new ReferencedObjectPropertyCount(
                Utils.getMockOntology());
        String result0 = testSubject0.getName();
        Object result1 = testSubject0.recomputeMetric();
        Integer result2 = testSubject0.recomputeMetric();
        Object result4 = testSubject0.getValue();
        testSubject0.dispose();
        Set<OWLOntology> result5 = testSubject0.getOntologies();
        OWLOntology result6 = testSubject0.getOntology();
        testSubject0.ontologiesChanged(Utils.mockList(mock(AddAxiom.class)));
        testSubject0.setOntology(Utils.getMockOntology());
        OWLOntologyManager result7 = testSubject0.getManager();
        boolean result8 = testSubject0.isImportsClosureUsed();
        testSubject0.setImportsClosureUsed(false);
    }
}
