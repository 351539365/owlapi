package org.semanticweb.owlapi.api.test.alternate;

/*
 * Copyright (C) 2010, University of Manchester
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
import java.util.Set;

import junit.framework.TestCase;

import org.semanticweb.owlapi.apibinding.configurables.ThreadSafeOWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.owlapi.alternateimpls.test.MultiThreadChecker;
import uk.ac.manchester.cs.owl.owlapi.alternateimpls.test.TestMultithreadCallBack;

public class OwlOntologyMultipleThreadsTest extends TestCase {
	private static class TestCallback implements TestMultithreadCallBack {
		private final OWLOntology o1;
		private final OWLOntology o2;

		TestCallback(OWLOntology o1, OWLOntology o2) {
			this.o1 = o1;
			this.o2 = o2;
		}

		public void execute() throws Exception {
			for (int index = 0; index < 100; index++) {
				o1.isEmpty();
				o1.getAnnotations();
				o1.getSignature(true);
				o1.getSignature(false);
				Set<OWLEntity> entities = o1.getSignature();
				o1.getOWLOntologyManager();
				o1.getOntologyID();
				o1.isAnonymous();
				o1.getDirectImportsDocuments();
				o1.getDirectImports();
				o1.getImports();
				o1.getImportsClosure();
				o1.getImportsDeclarations();
				o1.getAxioms();
				o1.getAxiomCount();
				Set<OWLClass> classes = o1.getClassesInSignature();
				o1.getClassesInSignature(true);
				o1.getClassesInSignature(false);
				Set<OWLObjectProperty> objectProperties = o1
						.getObjectPropertiesInSignature(true);
				o1.getObjectPropertiesInSignature(false);
				o1.getObjectPropertiesInSignature();
				Set<OWLDataProperty> dataProperties = o1
						.getDataPropertiesInSignature();
				o1.getDataPropertiesInSignature(true);
				o1.getDataPropertiesInSignature(false);
				Set<OWLNamedIndividual> individuals = o1
						.getIndividualsInSignature();
				o1.getIndividualsInSignature(true);
				o1.getIndividualsInSignature(false);
				Set<OWLAnonymousIndividual> anonIndividuals = o1
						.getReferencedAnonymousIndividuals();
				o1.getDatatypesInSignature();
				o1.getDatatypesInSignature(true);
				o1.getDatatypesInSignature(false);
				o1.getAnnotationPropertiesInSignature();
				for (OWLObjectProperty o : objectProperties) {
					o1.getAxioms(o);
					o1.containsObjectPropertyInSignature(o.getIRI());
					o1.containsObjectPropertyInSignature(o.getIRI(), true);
					o1.containsObjectPropertyInSignature(o.getIRI(), false);
					o1.getObjectSubPropertyAxiomsForSubProperty(o);
					o1.getObjectSubPropertyAxiomsForSuperProperty(o);
					o1.getObjectPropertyDomainAxioms(o);
					o1.getObjectPropertyRangeAxioms(o);
					o1.getInverseObjectPropertyAxioms(o);
					o1.getEquivalentObjectPropertiesAxioms(o);
					o1.getDisjointObjectPropertiesAxioms(o);
					o1.getFunctionalObjectPropertyAxioms(o);
					o1.getInverseFunctionalObjectPropertyAxioms(o);
					o1.getSymmetricObjectPropertyAxioms(o);
					o1.getAsymmetricObjectPropertyAxioms(o);
					o1.getReflexiveObjectPropertyAxioms(o);
					o1.getIrreflexiveObjectPropertyAxioms(o);
					o1.getTransitiveObjectPropertyAxioms(o);
				}
				for (OWLClass c : classes) {
					o1.getAxioms(c);
					o1.containsClassInSignature(c.getIRI());
					o1.containsClassInSignature(c.getIRI(), true);
					o1.containsClassInSignature(c.getIRI(), false);
					o1.getSubClassAxiomsForSubClass(c);
					o1.getSubClassAxiomsForSuperClass(c);
					o1.getEquivalentClassesAxioms(c);
					o1.getDisjointClassesAxioms(c);
					o1.getDisjointUnionAxioms(c);
					o1.getHasKeyAxioms(c);
					o1.getClassAssertionAxioms(c);
				}
				for (OWLDataProperty p : dataProperties) {
					o1.getAxioms(p);
					o1.containsDataPropertyInSignature(p.getIRI());
					o1.containsDataPropertyInSignature(p.getIRI(), true);
					o1.containsDataPropertyInSignature(p.getIRI(), false);
					o1.getDataSubPropertyAxiomsForSubProperty(p);
					o1.getDataSubPropertyAxiomsForSuperProperty(p);
					o1.getDataPropertyDomainAxioms(p);
					o1.getDataPropertyRangeAxioms(p);
					o1.getEquivalentDataPropertiesAxioms(p);
					o1.getDisjointDataPropertiesAxioms(p);
					o1.getFunctionalDataPropertyAxioms(p);
				}
				for (OWLNamedIndividual i : individuals) {
					o1.getAxioms(i);
					o1.containsIndividualInSignature(i.getIRI());
					o1.containsIndividualInSignature(i.getIRI(), true);
					o1.containsIndividualInSignature(i.getIRI(), false);
					o1.getClassAssertionAxioms(i);
					o1.getDataPropertyAssertionAxioms(i);
					o1.getObjectPropertyAssertionAxioms(i);
					o1.getNegativeObjectPropertyAssertionAxioms(i);
					o1.getNegativeDataPropertyAssertionAxioms(i);
					o1.getSameIndividualAxioms(i);
					o1.getDifferentIndividualAxioms(i);
				}
				for (OWLAnonymousIndividual i : anonIndividuals) {
					o1.getAxioms(i);
				}
				for (AxiomType<?> ax : AxiomType.AXIOM_TYPES) {
					o1.getAxioms(ax);
					o1.getAxioms(ax, true);
					o1.getAxioms(ax, false);
				}
				for (OWLDatatype t : o1.getDatatypesInSignature()) {
					o1.getAxioms(t);
					o1.containsDatatypeInSignature(t.getIRI());
					o1.containsDatatypeInSignature(t.getIRI(), true);
					o1.containsDatatypeInSignature(t.getIRI(), false);
					o1.getDatatypeDefinitions(t);
				}
				for (OWLAnnotationProperty p : o1
						.getAnnotationPropertiesInSignature()) {
					o1.getAxioms(p);
					o1.containsAnnotationPropertyInSignature(p.getIRI());
					o1.containsAnnotationPropertyInSignature(p.getIRI(), true);
					o1.containsAnnotationPropertyInSignature(p.getIRI(), false);
					o1.getSubAnnotationPropertyOfAxioms(p);
					o1.getAnnotationPropertyDomainAxioms(p);
					o1.getAnnotationPropertyRangeAxioms(p);
				}
				for (AxiomType<?> ax : AxiomType.AXIOM_TYPES) {
					o1.getAxiomCount(ax);
					o1.getAxiomCount(ax, true);
					o1.getAxiomCount(ax, false);
				}
				o1.getLogicalAxioms();
				o1.getLogicalAxiomCount();
				for (OWLAxiom ax : o1.getLogicalAxioms()) {
					o1.containsAxiom(ax);
					o1.containsAxiom(ax, true);
					o1.containsAxiom(ax, false);
				}
				for (OWLAxiom ax : o1.getLogicalAxioms()) {
					o1.containsAxiomIgnoreAnnotations(ax);
					o1.containsAxiomIgnoreAnnotations(ax, true);
					o1.containsAxiomIgnoreAnnotations(ax, false);
				}
				for (OWLAxiom ax : o1.getLogicalAxioms()) {
					o1.getAxiomsIgnoreAnnotations(ax);
					o1.getAxiomsIgnoreAnnotations(ax, true);
					o1.getAxiomsIgnoreAnnotations(ax, false);
				}
				o1.getGeneralClassAxioms();
				for (OWLAnonymousIndividual i : anonIndividuals) {
					o1.getReferencingAxioms(i);
				}
				for (OWLEntity e : entities) {
					o1.getReferencingAxioms(e);
					o1.getReferencingAxioms(e, true);
					o1.getReferencingAxioms(e, false);
					o1.getDeclarationAxioms(e);
					o1.containsEntityInSignature(e, true);
					o1.containsEntityInSignature(e, false);
					o1.containsEntityInSignature(e);
					o1.containsEntityInSignature(e.getIRI(), false);
					o1.containsEntityInSignature(e.getIRI(), true);
					o1.getEntitiesInSignature(e.getIRI());
					o1.getEntitiesInSignature(e.getIRI(), false);
					o1.getEntitiesInSignature(e.getIRI(), true);
					o1.isDeclared(e);
					o1.isDeclared(e, true);
					o1.isDeclared(e, false);
					if (e instanceof OWLAnnotationSubject) {
						o1.getAnnotationAssertionAxioms((OWLAnnotationSubject) e);
					}
				}
				Set<OWLAxiom> axioms = o1.getAxioms();
				for (OWLAxiom ax : axioms) {
					o1.getOWLOntologyManager().addAxiom(o2, ax);
					o1.getOWLOntologyManager().removeAxiom(o2, ax);
				}
			}
		}

		public String getId() {
			return "test for " + o1.getClass().getSimpleName();
		}
	}

	public void testLockingOwlOntologyImpl() {
		OWLOntologyManager m = ThreadSafeOWLManager.createOWLOntologyManager();
		OWLOntology o = null;
		try {
			o = m.loadOntologyFromOntologyDocument(IRI.create(getClass()
					.getResource("/koala.owl").toURI()));
			MultiThreadChecker checker = new MultiThreadChecker(10);
			checker.check(new TestCallback(o, m.createOntology()));
			String trace = checker.getTrace();
			System.out.println(trace);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
