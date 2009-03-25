package org.coode.owlapi.owlxml.renderer;

import org.semanticweb.owl.model.*;
import static org.semanticweb.owl.vocab.OWLXMLVocabulary.*;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.io.OWLXMLOntologyFormat;

import java.util.Set;
import java.util.TreeSet;
import java.net.URI;
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
 * Bio-Health Informatics Group<br>
 * Date: 12-Dec-2006<br><br>
 */
public class OWLXMLObjectRenderer implements OWLObjectVisitor {

    private OWLXMLWriter writer;

    private OWLOntology ontology;


    public OWLXMLObjectRenderer(OWLOntology ontology, OWLXMLWriter writer) {
        this.writer = writer;
        this.ontology = ontology;
    }


    public OWLXMLObjectRenderer(OWLXMLWriter writer) {
        this.writer = writer;
        this.ontology = null;
    }

    public void visit(IRI iri) {
        writer.writeIRIAttribute(iri);
    }

    public void visit(OWLAnonymousIndividual individual) {
        writer.writeStartElement(ANONYMOUS_INDIVIDUAL.getURI());
        writer.writeNodeIDAttribute(individual.getID());
        writer.writeEndElement();
    }

    private void writeAnnotations(OWLAxiom axiom) {
        for(OWLAnnotation anno : axiom.getAnnotations()) {
            anno.accept(this);
        }
    }


    public void visit(OWLOntology ontology) {
        for (OWLImportsDeclaration decl : ontology.getImportsDeclarations()) {
            writer.writeStartElement(IMPORTS.getURI());
            writer.writeTextContent(decl.getURI().toString());
            writer.writeEndElement();
        }
        for (OWLAxiom ax : new TreeSet<OWLAxiom>(ontology.getAxioms())) {
            ax.accept(this);
        }
    }

    public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        writer.writeStartElement(ASYMMETRIC_OBJECT_PROPERTY.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        writer.writeEndElement();
    }

    public void visit(OWLClassAssertionAxiom axiom) {
        writer.writeStartElement(CLASS_ASSERTION.getURI());
        writeAnnotations(axiom);
        axiom.getClassExpression().accept(this);
        axiom.getIndividual().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        writer.writeStartElement(DATA_PROPERTY_ASSERTION.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        axiom.getSubject().accept(this);
        axiom.getObject().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDataPropertyDomainAxiom axiom) {
        writeAnnotations(axiom);
        writer.writeStartElement(DATA_PROPERTY_DOMAIN.getURI());
        axiom.getProperty().accept(this);
        axiom.getDomain().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDataPropertyRangeAxiom axiom) {
        writeAnnotations(axiom);
        writer.writeStartElement(DATA_PROPERTY_RANGE.getURI());
        axiom.getProperty().accept(this);
        axiom.getRange().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLSubDataPropertyOfAxiom axiom) {
        writeAnnotations(axiom);
        writer.writeStartElement(SUB_DATA_PROPERTY_OF.getURI());
        axiom.getSubProperty().accept(this);
        axiom.getSuperProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDeclarationAxiom axiom) {
        writeAnnotations(axiom);
        writer.writeStartElement(DECLARATION.getURI());
        writeAnnotations(axiom);
        axiom.getEntity().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDifferentIndividualsAxiom axiom) {
        writer.writeStartElement(DIFFERENT_INDIVIDUALS.getURI());
        writeAnnotations(axiom);
        render(axiom.getIndividuals());
        writer.writeEndElement();
    }


    public void visit(OWLDisjointClassesAxiom axiom) {
        writer.writeStartElement(DISJOINT_CLASSES.getURI());
        writeAnnotations(axiom);
        render(axiom.getClassExpressions());
        writer.writeEndElement();
    }


    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        writer.writeStartElement(DISJOINT_DATA_PROPERTIES.getURI());
        writeAnnotations(axiom);
        render(axiom.getProperties());
        writer.writeEndElement();
    }


    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        writer.writeStartElement(DISJOINT_OBJECT_PROPERTIES.getURI());
        writeAnnotations(axiom);
        render(axiom.getProperties());
        writer.writeEndElement();
    }


    public void visit(OWLDisjointUnionAxiom axiom) {
        writer.writeStartElement(DISJOINT_UNION.getURI());
        writeAnnotations(axiom);
        axiom.getOWLClass().accept(this);
        writer.writeStartElement(UNION_OF.getURI());
        render(axiom.getClassExpressions());
        writer.writeEndElement();
        writer.writeEndElement();
    }


    public void visit(OWLAnnotationAssertionAxiom axiom) {
        writer.writeStartElement(ANNOTATION_ASSERTION.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        axiom.getSubject().accept(this);
        axiom.getValue().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLEquivalentClassesAxiom axiom) {
        writer.writeStartElement(EQUIVALENT_CLASSES.getURI());
        writeAnnotations(axiom);
        render(axiom.getClassExpressions());
        writer.writeEndElement();
    }


    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        writer.writeStartElement(EQUIVALENT_DATA_PROPERTIES.getURI());
        writeAnnotations(axiom);
        render(axiom.getProperties());
        writer.writeEndElement();
    }


    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        writer.writeStartElement(EQUIVALENT_OBJECT_PROPERTIES.getURI());
        writeAnnotations(axiom);
        render(axiom.getProperties());
        writer.writeEndElement();
    }


    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        writer.writeStartElement(FUNCTIONAL_DATA_PROPERTY.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        writer.writeStartElement(FUNCTIONAL_OBJECT_PROPERTY.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        writer.writeEndElement();
    }
    

    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        writer.writeStartElement(INVERSE_FUNCTIONAL_OBJECT_PROPERTY.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        writer.writeStartElement(INVERSE_OBJECT_PROPERTIES.getURI());
        writeAnnotations(axiom);
        axiom.getFirstProperty().accept(this);
        axiom.getSecondProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        writer.writeStartElement(IRREFLEXIVE_OBJECT_PROPERTY.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        writer.writeStartElement(NEGATIVE_DATA_PROPERTY_ASSERTION.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        axiom.getSubject().accept(this);
        axiom.getObject().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        writer.writeStartElement(NEGATIVE_OBJECT_PROPERTY_ASSERTION.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        axiom.getSubject().accept(this);
        axiom.getObject().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        writer.writeStartElement(OBJECT_PROPERTY_ASSERTION.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        axiom.getSubject().accept(this);
        axiom.getObject().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLSubPropertyChainOfAxiom axiom) {
        writer.writeStartElement(SUB_OBJECT_PROPERTY_OF.getURI());
        writeAnnotations(axiom);
        writer.writeStartElement(SUB_OBJECT_PROPERTY_CHAIN.getURI());
        for (OWLObjectPropertyExpression prop : axiom.getPropertyChain()) {
            prop.accept(this);
        }
        writer.writeEndElement();
        axiom.getSuperProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        writer.writeStartElement(OBJECT_PROPERTY_DOMAIN.getURI());
        axiom.getProperty().accept(this);
        axiom.getDomain().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        writer.writeStartElement(OBJECT_PROPERTY_RANGE.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        axiom.getRange().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLSubObjectPropertyOfAxiom axiom) {
        writer.writeStartElement(SUB_OBJECT_PROPERTY_OF.getURI());
        writeAnnotations(axiom);
        axiom.getSubProperty().accept(this);
        axiom.getSuperProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        writer.writeStartElement(REFLEXIVE_OBJECT_PROPERTY.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLSameIndividualAxiom axiom) {
        writer.writeStartElement(SAME_INDIVIDUALS.getURI());
        writeAnnotations(axiom);
        render(axiom.getIndividuals());
        writer.writeEndElement();
    }


    public void visit(OWLSubClassOfAxiom axiom) {
        writer.writeStartElement(SUB_CLASS_OF.getURI());
        writeAnnotations(axiom);
        axiom.getSubClass().accept(this);
        axiom.getSuperClass().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        writer.writeStartElement(SYMMETRIC_OBJECT_PROPERTY.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        writer.writeStartElement(TRANSITIVE_OBJECT_PROPERTY.getURI());
        writeAnnotations(axiom);
        axiom.getProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLClass desc) {
        writer.writeStartElement(CLASS.getURI());
        writer.writeIRIAttribute(desc.getIRI());
        writer.writeEndElement();
    }


    public void visit(OWLDataAllValuesFrom desc) {
        writer.writeStartElement(DATA_ALL_VALUES_FROM.getURI());
        desc.getProperty().accept(this);
        desc.getFiller().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDataExactCardinality desc) {
        writer.writeStartElement(DATA_EXACT_CARDINALITY.getURI());
        writer.writeCardinalityAttribute(desc.getCardinality());
        desc.getProperty().accept(this);
        if (desc.isQualified()) {
            desc.getFiller().accept(this);
        }
        writer.writeEndElement();
    }


    public void visit(OWLDataMaxCardinality desc) {
        writer.writeStartElement(DATA_MAX_CARDINALITY.getURI());
        writer.writeCardinalityAttribute(desc.getCardinality());
        desc.getProperty().accept(this);
        if (desc.isQualified()) {
            desc.getFiller().accept(this);
        }
        writer.writeEndElement();
    }


    public void visit(OWLDataMinCardinality desc) {
        writer.writeStartElement(DATA_MIN_CARDINALITY.getURI());
        writer.writeCardinalityAttribute(desc.getCardinality());
        desc.getProperty().accept(this);
        if (desc.isQualified()) {
            desc.getFiller().accept(this);
        }
        writer.writeEndElement();
    }


    public void visit(OWLDataSomeValuesFrom desc) {
        writer.writeStartElement(DATA_SOME_VALUES_FROM.getURI());
        desc.getProperty().accept(this);
        desc.getFiller().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDataHasValue desc) {
        writer.writeStartElement(DATA_HAS_VALUE.getURI());
        desc.getProperty().accept(this);
        desc.getValue().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLObjectAllValuesFrom desc) {
        writer.writeStartElement(OBJECT_ALL_VALUES_FROM.getURI());
        desc.getProperty().accept(this);
        desc.getFiller().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLObjectComplementOf desc) {
        writer.writeStartElement(OBJECT_COMPLEMENT_OF.getURI());
        desc.getOperand().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLObjectExactCardinality desc) {
        writer.writeStartElement(OBJECT_EXACT_CARDINALITY.getURI());
        writer.writeCardinalityAttribute(desc.getCardinality());
        desc.getProperty().accept(this);
        if (desc.isQualified()) {
            desc.getFiller().accept(this);
        }
        writer.writeEndElement();
    }


    public void visit(OWLObjectIntersectionOf desc) {
        writer.writeStartElement(OBJECT_INTERSECTION_OF.getURI());
        render(desc.getOperands());
        writer.writeEndElement();
    }


    public void visit(OWLObjectMaxCardinality desc) {
        writer.writeStartElement(OBJECT_MAX_CARDINALITY.getURI());
        writer.writeCardinalityAttribute(desc.getCardinality());
        desc.getProperty().accept(this);
        if (desc.isQualified()) {
            desc.getFiller().accept(this);
        }
        writer.writeEndElement();
    }


    public void visit(OWLObjectMinCardinality desc) {
        writer.writeStartElement(OBJECT_MIN_CARDINALITY.getURI());
        writer.writeCardinalityAttribute(desc.getCardinality());
        desc.getProperty().accept(this);
        if (desc.isQualified()) {
            desc.getFiller().accept(this);
        }
        writer.writeEndElement();
    }


    public void visit(OWLObjectOneOf desc) {
        writer.writeStartElement(OBJECT_ONE_OF.getURI());
        render(desc.getIndividuals());
        writer.writeEndElement();
    }


    public void visit(OWLObjectHasSelf desc) {
        writer.writeStartElement(OBJECT_EXISTS_SELF.getURI());
        desc.getProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLObjectSomeValuesFrom desc) {
        writer.writeStartElement(OBJECT_SOME_VALUES_FROM.getURI());
        desc.getProperty().accept(this);
        desc.getFiller().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLObjectUnionOf desc) {
        writer.writeStartElement(OBJECT_UNION_OF.getURI());
        render(desc.getOperands());
        writer.writeEndElement();
    }


    public void visit(OWLObjectHasValue desc) {
        writer.writeStartElement(OBJECT_HAS_VALUE.getURI());
        desc.getProperty().accept(this);
        desc.getValue().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDataComplementOf node) {
        writer.writeStartElement(DATA_COMPLEMENT_OF.getURI());
        node.getDataRange().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDataOneOf node) {
        writer.writeStartElement(DATA_ONE_OF.getURI());
        render(node.getValues());
        writer.writeEndElement();
    }


    public void visit(OWLDatatype node) {
        writer.writeStartElement(DATATYPE.getURI());
        writer.writeIRIAttribute(node.getIRI());
        writer.writeEndElement();
    }


    public void visit(OWLDatatypeRestriction node) {
        writer.writeStartElement(DATATYPE_RESTRICTION.getURI());
        node.getDatatype().accept(this);
        for (OWLFacetRestriction restriction : node.getFacetRestrictions()) {
            restriction.accept(this);
        }
        writer.writeEndElement();
    }


    public void visit(OWLFacetRestriction node) {
        writer.writeStartElement(FACET_RESTRICTION.getURI());
        writer.writeFacetAttribute(node.getFacet().getURI());
        node.getFacetValue().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLTypedLiteral node) {
        writer.writeStartElement(LITERAL.getURI());
        writer.writeDatatypeAttribute(node.getDatatype().getURI());
        writer.writeTextContent(node.getLiteral());
        writer.writeEndElement();
    }


    public void visit(OWLRDFTextLiteral node) {
        writer.writeStartElement(LITERAL.getURI());
        writer.writeLangAttribute(node.getLang());
        writer.writeTextContent(node.getLiteral());
        writer.writeEndElement();
    }


    public void visit(OWLDataProperty property) {
        writer.writeStartElement(DATA_PROPERTY.getURI());
        writer.writeIRIAttribute(property.getIRI());
        writer.writeEndElement();
    }


    public void visit(OWLObjectProperty property) {
        writer.writeStartElement(OBJECT_PROPERTY.getURI());
        writer.writeIRIAttribute(property.getIRI());
        writer.writeEndElement();
    }


    public void visit(OWLObjectPropertyInverse property) {
        writer.writeStartElement(INVERSE_OBJECT_PROPERTY.getURI());
        property.getInverse().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLNamedIndividual individual) {
        writer.writeStartElement(INDIVIDUAL.getURI());
        writer.writeIRIAttribute(individual.getIRI());
        writer.writeEndElement();
    }

    public void visit(OWLHasKeyAxiom axiom) {
        writer.writeStartElement(HAS_KEY.getURI());
        writeAnnotations(axiom);
        axiom.getClassExpression().accept(this);
        for(OWLPropertyExpression prop : axiom.getPropertyExpressions()) {
            prop.accept(this);
        }
        writer.writeEndElement();
    }

    public void visit(OWLDataIntersectionOf node) {
        writer.writeStartElement(DATA_INTERSECTION_OF.getURI());
        for(OWLDataRange rng : node.getOperands()) {
            rng.accept(this);
        }
        writer.writeEndElement();
    }

    public void visit(OWLDataUnionOf node) {
        writer.writeStartElement(DATA_UNION_OF.getURI());
        for(OWLDataRange rng : node.getOperands()) {
            rng.accept(this);
        }
        writer.writeEndElement();
    }

    public void visit(OWLAnnotationProperty property) {
        writer.writeStartElement(ANNOTATION_PROPERTY.getURI());
        writer.writeIRIAttribute(property.getIRI());
        writer.writeEndElement();
    }

    public void visit(OWLAnnotation annotation) {
        writer.writeStartElement(ANNOTATION.getURI());
        for(OWLAnnotation anno : annotation.getAnnotations()) {
            anno.accept(this);
        }
        annotation.getProperty().accept(this);
        annotation.getValue().accept(this);
        writer.writeEndElement();
    }

    public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
        writer.writeStartElement(ANNOTATION_PROPERTY_DOMAIN.getURI());
        axiom.getProperty().accept(this);
        axiom.getDomain().accept(this);
        writer.writeEndElement();
    }

    public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
        writer.writeStartElement(ANNOTATION_PROPERTY_RANGE.getURI());
        axiom.getProperty().accept(this);
        axiom.getRange().accept(this);
        writer.writeEndElement();
    }

    public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {
        writer.writeStartElement(SUB_ANNOTATION_PROPERTY_OF.getURI());
        axiom.getSubProperty().accept(this);
        axiom.getSuperProperty().accept(this);
        writer.writeEndElement();
    }


    public void visit(OWLDatatypeDefinition axiom) {
        writer.writeStartElement(DATATYPE_DEFINITION.getURI());
        axiom.getDatatype().accept(this);
        axiom.getDataRange().accept(this);
        writer.writeEndElement();
    }


    public void visit(SWRLRule rule) {
    }


    public void visit(SWRLClassAtom node) {
    }


    public void visit(SWRLDataRangeAtom node) {
    }


    public void visit(SWRLObjectPropertyAtom node) {
    }


    public void visit(SWRLDataValuedPropertyAtom node) {
    }


    public void visit(SWRLBuiltInAtom node) {
    }


    public void visit(SWRLAtomDVariable node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }


    public void visit(SWRLAtomIVariable node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }


    public void visit(SWRLAtomIndividualObject node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }


    public void visit(SWRLAtomConstantObject node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }


    public void visit(SWRLDifferentFromAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }


    public void visit(SWRLSameAsAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }


    private void render(Set<? extends OWLObject> objects) {
        for (OWLObject obj : objects) {
            obj.accept(this);
        }
    }

    public static void main(String[] args) {
        try {
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//            OWLOntology ont = man.loadOntologyFromPhysicalURI(URI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl"));
            long t0 = System.currentTimeMillis();
            OWLOntology ont = man.loadOntologyFromPhysicalURI(URI.create("file:/Users/matthewhorridge/ontologies/thesaurus/Thesaurus.owl"));
            long t1 = System.currentTimeMillis();

            System.gc();
            System.gc();
            System.gc();
            Runtime r = Runtime.getRuntime();
            long total = r.totalMemory();
            long free = r.freeMemory();
            System.out.println((total - free) / (1024 * 1024));
            System.out.println("Loaded in " + (t1 - t0));
            man.saveOntology(ont, new OWLXMLOntologyFormat(), URI.create("file:/tmp/out.txt"));
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

    }
}
