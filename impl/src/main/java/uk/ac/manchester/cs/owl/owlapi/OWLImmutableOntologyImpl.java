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
package uk.ac.manchester.cs.owl.owlapi;

import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.util.OWLAPIPreconditions.checkNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitor;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLPrimitive;
import org.semanticweb.owlapi.model.parameters.AxiomAnnotations;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.model.parameters.Navigation;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.util.OWLAxiomSearchFilter;
import org.semanticweb.owlapi.util.OWLObjectTypeIndexProvider;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.google.common.collect.Iterables;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group
 * @since 2.0.0
 */
public class OWLImmutableOntologyImpl extends OWLAxiomIndexImpl implements
        OWLOntology, Serializable {

    private static final long serialVersionUID = 40000L;
    @Nonnull
    protected OWLOntologyManager manager;
    @Nonnull
    protected OWLDataFactory df;
    @Nonnull
    protected OWLOntologyID ontologyID;

    @Override
    protected int index() {
        return OWLObjectTypeIndexProvider.ONTOLOGY;
    }

    /**
     * @param manager
     *        ontology manager
     * @param ontologyID
     *        ontology id
     */
    public OWLImmutableOntologyImpl(@Nonnull OWLOntologyManager manager,
            @Nonnull OWLOntologyID ontologyID) {
        this.manager = checkNotNull(manager, "manager cannot be null");
        this.ontologyID = checkNotNull(ontologyID, "ontologyID cannot be null");
        df = manager.getOWLDataFactory();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("Ontology(").append(ontologyID).append(") [Axioms: ")
                .append(ints.getAxiomCount()).append(" Logical Axioms: ")
                .append(ints.getLogicalAxiomCount())
                .append("] First 20 axioms: {");
        ints.getAxioms().limit(20).forEach(a -> sb.append(a).append(' '));
        sb.append('}');
        return sb.toString();
    }

    @Override
    public OWLOntologyManager getOWLOntologyManager() {
        return manager;
    }

    @Override
    public void setOWLOntologyManager(OWLOntologyManager manager) {
        this.manager = manager;
    }

    @Override
    public OWLOntologyID getOntologyID() {
        return ontologyID;
    }

    @Override
    public boolean isAnonymous() {
        return ontologyID.isAnonymous();
    }

    @Override
    protected int compareObjectOfSameType(OWLObject object) {
        if (object == this) {
            return 0;
        }
        OWLOntology other = (OWLOntology) object;
        return ontologyID.compareTo(other.getOntologyID());
    }

    @Override
    public boolean isEmpty() {
        return ints.isEmpty();
    }

    @Override
    public <T extends OWLAxiom> int getAxiomCount(AxiomType<T> axiomType) {
        return ints.getAxiomCount(axiomType);
    }

    @Override
    public int getAxiomCount() {
        return ints.getAxiomCount();
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom) {
        return Internals.contains(ints.getAxiomsByType(), axiom.getAxiomType(),
                axiom);
    }

    @Override
    public Stream<OWLAxiom> axioms() {
        return ints.getAxioms();
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends OWLAxiom> Stream<T> axioms(AxiomType<T> axiomType) {
        return (Stream<T>) ints.getAxiomsByType().getValues(axiomType).stream();
    }

    @Override
    public Stream<OWLLogicalAxiom> logicalAxioms() {
        return ints.getLogicalAxioms();
    }

    @Override
    public int getLogicalAxiomCount() {
        return ints.getLogicalAxiomCount();
    }

    @Override
    public <T extends OWLAxiom> int getAxiomCount(AxiomType<T> axiomType,
            Imports imports) {
        return imports.stream(this).mapToInt(o -> o.getAxiomCount(axiomType))
                .sum();
    }

    @Override
    public int getAxiomCount(Imports imports) {
        return imports.stream(this).mapToInt(o -> o.getAxiomCount()).sum();
    }

    private static Stream<OWLAxiom> asAxiomStream(Stream<? extends OWLAxiom> s) {
        return (Stream<OWLAxiom>) s;
    }

    @Override
    public Stream<OWLLogicalAxiom> logicalAxioms(Imports imports) {
        return imports.stream(this).flatMap(o -> o.logicalAxioms());
    }

    @Nonnull
    @Override
    public Set<? super OWLAxiom> getTBoxAxioms(Imports imports) {
        return AxiomType.TBoxAxiomTypes.stream()
                .flatMap(t -> asAxiomStream(axioms(t, imports)))
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<? super OWLAxiom> getABoxAxioms(Imports imports) {
        return AxiomType.ABoxAxiomTypes.stream()
                .flatMap(t -> asAxiomStream(axioms(t, imports)))
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<? super OWLAxiom> getRBoxAxioms(Imports imports) {
        return AxiomType.RBoxAxiomTypes.stream()
                .flatMap(t -> asAxiomStream(axioms(t, imports)))
                .collect(toSet());
    }

    @Override
    public int getLogicalAxiomCount(Imports imports) {
        return imports.stream(this).mapToInt(o -> o.getLogicalAxiomCount())
                .sum();
    }

    @Nonnull
    @Override
    public Stream<OWLAnnotation> annotations() {
        return ints.getOntologyAnnotations();
    }

    @Override
    public Set<OWLClassAxiom> getGeneralClassAxioms() {
        return ints.getGeneralClassAxioms();
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom, Imports imports,
            AxiomAnnotations ignoreAnnotations) {
        return imports.stream(this).anyMatch(
                o -> ignoreAnnotations.contains(o, axiom));
    }

    @Override
    public Set<OWLAxiom> getAxiomsIgnoreAnnotations(@Nonnull OWLAxiom axiom) {
        @SuppressWarnings("unchecked")
        Set<OWLAxiom> filter = ((Stream<OWLAxiom>) axioms(axiom.getAxiomType())
                .filter(ax -> ax.equalsIgnoreAnnotations(axiom)))
                .collect(toSet());
        if (containsAxiom(axiom)) {
            filter.add(axiom);
        }
        return filter;
    }

    @Override
    public boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom) {
        if (containsAxiom(axiom)) {
            return true;
        }
        return axioms(axiom.getAxiomType()).anyMatch(
                ax -> ax.equalsIgnoreAnnotations(axiom));
    }

    @Override
    public Set<OWLAxiom> getAxiomsIgnoreAnnotations(@Nonnull OWLAxiom axiom,
            Imports imports) {
        return imports.stream(this)
                .flatMap(o -> o.getAxiomsIgnoreAnnotations(axiom).stream())
                .collect(toSet());
    }

    @Override
    public boolean containsClassInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(
                o -> o.containsClassInSignature(iri));
    }

    @Override
    public boolean containsObjectPropertyInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(
                o -> o.containsObjectPropertyInSignature(iri));
    }

    @Override
    public boolean containsDataPropertyInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(
                o -> o.containsDataPropertyInSignature(iri));
    }

    @Override
    public boolean containsAnnotationPropertyInSignature(IRI iri,
            Imports imports) {
        boolean result = imports.stream(this).anyMatch(
                o -> o.containsAnnotationPropertyInSignature(iri));
        if (result) {
            return result;
        }
        return checkOntologyAnnotations(df.getOWLAnnotationProperty(iri));
    }

    private boolean checkOntologyAnnotations(OWLAnnotationProperty p) {
        return ints.getOntologyAnnotations().anyMatch(
                ann -> ann.getProperty().equals(p));
    }

    @Override
    public boolean containsIndividualInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(
                o -> o.containsIndividualInSignature(iri));
    }

    @Override
    public boolean containsDatatypeInSignature(IRI iri, Imports imports) {
        return imports.stream(this).anyMatch(
                o -> o.containsDatatypeInSignature(iri));
    }

    @Override
    public Stream<OWLEntity> entitiesInSignature(@Nonnull IRI iri) {
        Predicate<? super OWLEntity> matchIRI = c -> c.getIRI().equals(iri);
        return Stream.of(classesInSignature().filter(matchIRI),
                objectPropertiesInSignature().filter(matchIRI),
                dataPropertiesInSignature().filter(matchIRI),
                individualsInSignature().filter(matchIRI),
                datatypesInSignature().filter(matchIRI),
                annotationPropertiesInSignature().filter(matchIRI)).flatMap(
                x -> x);
    }

    @Override
    public Set<IRI> getPunnedIRIs(Imports imports) {
        Set<IRI> punned = new HashSet<>();
        Set<IRI> test = new HashSet<>();
        imports.stream(this)
                .flatMap(
                        o -> Stream
                                .of(o.classesInSignature(),
                                        o.dataPropertiesInSignature(),
                                        o.objectPropertiesInSignature(),
                                        o.annotationPropertiesInSignature(),
                                        o.datatypesInSignature(),
                                        o.individualsInSignature())
                                .flatMap(s -> s)
                                .filter(e -> test.add(e.getIRI())))
                .forEach(e -> punned.add(e.getIRI()));
        if (punned.isEmpty()) {
            return Collections.emptySet();
        }
        return punned;
    }

    @Override
    public boolean isDeclared(OWLEntity owlEntity) {
        return ints.isDeclared(owlEntity);
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity owlEntity) {
        return entityReferenceChecker.containsReference(owlEntity);
    }

    private final OWLEntityReferenceChecker entityReferenceChecker = new OWLEntityReferenceChecker();

    @Override
    public Stream<OWLEntity> signature() {
        return Stream.of(classesInSignature(), objectPropertiesInSignature(),
                dataPropertiesInSignature(), individualsInSignature(),
                datatypesInSignature(), annotationPropertiesInSignature())
                .flatMap(x -> x);
    }

    @Nonnull
    private static <T> Set<T> asSet(Iterable<T> i) {
        List<T> list = new ArrayList<>();
        Iterables.addAll(list, i);
        return CollectionFactory.copy(list);
    }

    @Override
    public Set<OWLAnonymousIndividual> getAnonymousIndividuals() {
        return asSet(ints.get(OWLAnonymousIndividual.class, OWLAxiom.class)
                .get().keySet());
    }

    @Override
    public Stream<OWLClass> classesInSignature() {
        return ints.get(OWLClass.class, OWLAxiom.class).get().keySet().stream();
    }

    @Override
    public Stream<OWLDataProperty> dataPropertiesInSignature() {
        return ints.get(OWLDataProperty.class, OWLAxiom.class).get().keySet()
                .stream();
    }

    @Override
    public Stream<OWLObjectProperty> objectPropertiesInSignature() {
        return ints.get(OWLObjectProperty.class, OWLAxiom.class).get().keySet()
                .stream();
    }

    @Override
    public Stream<OWLNamedIndividual> individualsInSignature() {
        return ints.get(OWLNamedIndividual.class, OWLAxiom.class).get()
                .keySet().stream();
    }

    @Override
    public Stream<OWLDatatype> datatypesInSignature() {
        return ints.get(OWLDatatype.class, OWLAxiom.class).get().keySet()
                .stream();
    }

    @Override
    public Stream<OWLAnonymousIndividual> referencedAnonymousIndividuals() {
        return ints.get(OWLAnonymousIndividual.class, OWLAxiom.class).get()
                .keySet().stream();
    }

    @Override
    public Stream<OWLAnnotationProperty> annotationPropertiesInSignature() {
        return Stream.concat(
                ints.get(OWLAnnotationProperty.class, OWLAxiom.class,
                        Navigation.IN_SUB_POSITION).get().keySet().stream(),
                ints.getOntologyAnnotations().map(a -> a.getProperty()));
    }

    @Nonnull
    @Override
    public Set<OWLImportsDeclaration> getImportsDeclarations() {
        return ints.getImportsDeclarations().collect(toSet());
    }

    @Override
    public Stream<IRI> directImportsDocuments() {
        return ints.getImportsDeclarations().map(i -> i.getIRI());
    }

    @Override
    public Set<OWLOntology> getImports() {
        return manager.getImports(this);
    }

    @Override
    public Stream<OWLOntology> directImports() {
        return manager.directImports(this);
    }

    @Override
    public Stream<OWLOntology> importsClosure() {
        return getOWLOntologyManager().importsClosure(this);
    }

    // Utility methods for getting/setting various values in maps and sets
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OWLOntology)) {
            return false;
        }
        OWLOntology other = (OWLOntology) obj;
        return ontologyID.equals(other.getOntologyID());
    }

    @Override
    public int hashCode() {
        return ontologyID.hashCode();
    }

    private class OWLEntityReferenceChecker implements OWLEntityVisitor,
            Serializable {

        OWLEntityReferenceChecker() {}

        private static final long serialVersionUID = 40000L;
        private boolean ref;

        public boolean containsReference(@Nonnull OWLEntity entity) {
            ref = false;
            entity.accept(this);
            return ref;
        }

        @Override
        public void visit(@Nonnull OWLClass cls) {
            ref = OWLImmutableOntologyImpl.this.ints
                    .containsClassInSignature(cls);
        }

        @Override
        public void visit(@Nonnull OWLDatatype datatype) {
            ref = OWLImmutableOntologyImpl.this.ints
                    .containsDatatypeInSignature(datatype);
        }

        @Override
        public void visit(@Nonnull OWLNamedIndividual individual) {
            ref = OWLImmutableOntologyImpl.this.ints
                    .containsIndividualInSignature(individual);
        }

        @Override
        public void visit(@Nonnull OWLDataProperty property) {
            ref = OWLImmutableOntologyImpl.this.ints
                    .containsDataPropertyInSignature(property);
        }

        @Override
        public void visit(@Nonnull OWLObjectProperty property) {
            ref = OWLImmutableOntologyImpl.this.ints
                    .containsObjectPropertyInSignature(property);
        }

        @Override
        public void visit(@Nonnull OWLAnnotationProperty property) {
            ref = OWLImmutableOntologyImpl.this.ints
                    .containsAnnotationPropertyInSignature(property);
        }
    }

    @Override
    public Stream<OWLClassAxiom> axioms(OWLClass cls) {
        return ints.get(OWLClass.class, OWLClassAxiom.class).get()
                .values(cls, OWLClassAxiom.class);
    }

    @Override
    public Stream<OWLObjectPropertyAxiom> axioms(
            OWLObjectPropertyExpression property) {
        return Stream.of(asymmetricObjectPropertyAxioms(property),
                reflexiveObjectPropertyAxioms(property),
                symmetricObjectPropertyAxioms(property),
                irreflexiveObjectPropertyAxioms(property),
                transitiveObjectPropertyAxioms(property),
                inverseFunctionalObjectPropertyAxioms(property),
                functionalObjectPropertyAxioms(property),
                inverseObjectPropertyAxioms(property),
                objectPropertyDomainAxioms(property),
                equivalentObjectPropertiesAxioms(property),
                disjointObjectPropertiesAxioms(property),
                objectPropertyRangeAxioms(property),
                objectSubPropertyAxiomsForSubProperty(property))
                .flatMap(x -> x);
    }

    @Override
    public Stream<OWLDataPropertyAxiom> axioms(OWLDataProperty property) {
        return Stream.of(dataPropertyDomainAxioms(property),
                equivalentDataPropertiesAxioms(property),
                disjointDataPropertiesAxioms(property),
                dataPropertyRangeAxioms(property),
                functionalDataPropertyAxioms(property),
                dataSubPropertyAxiomsForSubProperty(property)).flatMap(x -> x);
    }

    @Override
    public Stream<OWLIndividualAxiom> axioms(OWLIndividual individual) {
        return Stream.of(classAssertionAxioms(individual),
                objectPropertyAssertionAxioms(individual),
                dataPropertyAssertionAxioms(individual),
                negativeObjectPropertyAssertionAxioms(individual),
                negativeDataPropertyAssertionAxioms(individual),
                sameIndividualAxioms(individual),
                differentIndividualAxioms(individual)).flatMap(x -> x);
    }

    @Override
    public Stream<OWLDatatypeDefinitionAxiom> axioms(OWLDatatype datatype) {
        return datatypeDefinitions(datatype);
    }

    @Override
    public Stream<OWLAxiom> referencingAxioms(OWLPrimitive owlEntity) {
        if (owlEntity instanceof OWLEntity) {
            return ints.getReferencingAxioms((OWLEntity) owlEntity);
        } else if (owlEntity instanceof OWLAnonymousIndividual) {
            return ints.get(OWLAnonymousIndividual.class, OWLAxiom.class).get()
                    .values((OWLAnonymousIndividual) owlEntity, OWLAxiom.class);
        } else if (owlEntity instanceof IRI) {
            Set<OWLAxiom> axioms = new HashSet<>();
            // axioms referring entities with this IRI, data property assertions
            // with IRI as subject, annotations with IRI as subject or object.
            getEntitiesInSignature((IRI) owlEntity).forEach(
                    e -> axioms.addAll(getReferencingAxioms(e)));
            for (OWLDataPropertyAssertionAxiom ax : getAxioms(AxiomType.DATA_PROPERTY_ASSERTION)) {
                if (OWL2Datatype.XSD_ANY_URI.matches(ax.getObject()
                        .getDatatype())) {
                    if (ax.getObject().getLiteral()
                            .equals(owlEntity.toString())) {
                        axioms.add(ax);
                    }
                }
            }
            for (OWLAnnotationAssertionAxiom ax : getAxioms(AxiomType.ANNOTATION_ASSERTION)) {
                if (ax.getSubject().equals(owlEntity)) {
                    axioms.add(ax);
                } else if (ax.getValue().asLiteral().isPresent()) {
                    Optional<OWLLiteral> lit = ax.getValue().asLiteral();
                    if (OWL2Datatype.XSD_ANY_URI.matches(lit.get()
                            .getDatatype())) {
                        if (lit.get().getLiteral().equals(owlEntity.toString())) {
                            axioms.add(ax);
                        }
                    }
                }
            }
            return axioms.stream();
        } else if (owlEntity instanceof OWLLiteral) {
            Set<OWLAxiom> axioms = new HashSet<>();
            for (OWLDataPropertyAssertionAxiom ax : getAxioms(AxiomType.DATA_PROPERTY_ASSERTION)) {
                if (OWL2Datatype.XSD_ANY_URI.matches(ax.getObject()
                        .getDatatype())) {
                    if (ax.getObject().equals(owlEntity)) {
                        axioms.add(ax);
                    }
                }
            }
            for (OWLAnnotationAssertionAxiom ax : getAxioms(AxiomType.ANNOTATION_ASSERTION)) {
                if (owlEntity.equals(ax.getValue().asLiteral().orElse(null))) {
                    axioms.add(ax);
                }
            }
            return axioms.stream();
        }
        return Stream.empty();
    }

    // OWLAxiomIndex
    @Override
    @SuppressWarnings("unchecked")
    public <A extends OWLAxiom> Stream<A> axioms(@Nonnull Class<A> type,
            @Nonnull Class<? extends OWLObject> explicitClass,
            @Nonnull OWLObject entity, @Nonnull Navigation forSubPosition) {
        Optional<MapPointer<OWLObject, A>> optional = ints.get(
                (Class<OWLObject>) explicitClass, type, forSubPosition);
        if (optional.isPresent()) {
            return optional.get().values(entity, type);
        }
        return axioms(AxiomType.getTypeForClass(type)).filter(
                a -> a.getSignature().contains(entity));
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends OWLAxiom> Collection<T> filterAxioms(
            @Nonnull OWLAxiomSearchFilter filter, @Nonnull Object key,
            Imports imports) {
        return (Set<T>) imports.stream(this)
                .flatMap(o -> o.filterAxioms(filter, key).stream())
                .collect(toSet());
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends OWLAxiom> Collection<T> filterAxioms(
            @Nonnull OWLAxiomSearchFilter filter, @Nonnull Object key) {
        return (Collection<T>) ints.filterAxioms(filter, key);
    }

    @Override
    public boolean contains(@Nonnull OWLAxiomSearchFilter filter,
            @Nonnull Object key) {
        return ints.contains(filter, key);
    }

    @Override
    public boolean contains(@Nonnull OWLAxiomSearchFilter filter,
            @Nonnull Object key, Imports imports) {
        return imports.stream(this).anyMatch(o -> o.contains(filter, key));
    }

    @Override
    public boolean containsDatatypeInSignature(IRI iri) {
        return ints.containsDatatypeInSignature(iri);
    }

    @Override
    public boolean containsClassInSignature(IRI iri) {
        return ints.containsClassInSignature(iri);
    }

    @Override
    public boolean containsObjectPropertyInSignature(IRI iri) {
        return ints.containsObjectPropertyInSignature(iri);
    }

    @Override
    public boolean containsDataPropertyInSignature(IRI iri) {
        return ints.containsDataPropertyInSignature(iri);
    }

    @Override
    public boolean containsAnnotationPropertyInSignature(IRI iri) {
        return ints.containsAnnotationPropertyInSignature(iri);
    }

    @Override
    public boolean containsIndividualInSignature(IRI iri) {
        return ints.containsIndividualInSignature(iri);
    }

    @Override
    public boolean containsReference(OWLEntity entity) {
        return ints.containsReference(entity);
    }
}
