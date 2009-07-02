package org.coode.owlapi.rdfxml.parser;

import edu.unika.aifb.rdf.api.syntax.RDFConsumer;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.vocab.*;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.*;
import org.xml.sax.SAXException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Date: 07-Dec-2006<br><br>
 * A parser/interpreter for an RDF graph which represents an OWL ontology.  The
 * consumer interprets triple patterns in the graph to produce the appropriate
 * OWLAPI entities, class expressions and axioms.
 * The parser is based on triple handlers.  A given triple handler handles a specific
 * type of triple.  Generally speaking this is based on the predicate of a triple, for
 * example, A rdfs:subClassOf B is handled by a subClassOf handler.  A handler determines
 * if it can handle a triple in a streaming mode (i.e. while parsing is taking place) or
 * if it can handle a triple after parsing has taken place and the complete graph is in
 * memory.  Once a handler handles a triple, that triple is deemed to have been consumed
 * an is discarded.
 * The parser attempts to consume as many triples as possible while streaming parsing
 * is taking place. Whether or not a triple can be consumed during parsing is determined
 * by installed triple handlers.
 */
public class OWLRDFConsumer implements RDFConsumer {

    public static final Logger logger = Logger.getLogger(OWLRDFConsumer.class.getName());

    private static final Logger tripleProcessor = Logger.getLogger("Triple processor");

    //private Graph graph;

    private OWLOntologyManager owlOntologyManager;

    private URI xmlBase;

    // A call back interface, which is used to check whether a node
    // is anonymous or not.
    private AnonymousNodeChecker anonymousNodeChecker;

    // The set of URIs that are either explicitly typed
    // an an owlapi:Class, or are inferred to be an owlapi:Class
    // because they are used in some triple whose predicate
    // has the domain or range of owlapi:Class
    private Set<URI> owlClassURIs;

    // Same as owlClassURIs but for object properties
    private Set<URI> objectPropertyURIs;

    // Same as owlClassURIs but for data properties
    private Set<URI> dataPropertyURIs;

    // Same as owlClassURIs but for rdf properties
    // things neither typed as a data or object property - bad!
    private Set<URI> propertyURIs;


    // Set of URIs that are typed by non-system types and
    // also owlapi:Thing
    private Set<URI> individualURIs;


    // Same as owlClassURIs but for annotation properties
    private Set<URI> annotationPropertyURIs;

    private Set<URI> annotationURIs;

    private Map<URI, OWLAnnotation> annotationURI2Annotation;


    private Set<URI> ontologyPropertyURIs;


    // URIs that had a type triple to rdfs:DataRange
    private Set<URI> dataRangeURIs;

    // The URI of the first reource that is typed as an ontology
    private URI firstOntologyURI;

    // URIs that had a type triple to owlapi:Ontology
    private Set<URI> ontologyURIs;

    // URIs that had a type triple to owlapi:Restriction
    private Set<URI> restrictionURIs;

    // URIs that had a type triple to rdf:List
    private Set<URI> listURIs;

    // Maps rdf:next triple subjects to objects
    private Map<URI, URI> listRestTripleMap;

    private Map<URI, URI> listFirstResourceTripleMap;

    private Map<URI, OWLLiteral> listFirstLiteralTripleMap;

    private Map<URI, OWLAxiom> reifiedAxiomsMap;

    private Map<URI, Set<OWLAnnotation>> annotationsBySubject;

    // A translator for lists of class expressions (such lists are used
    // in intersections, unions etc.)
    private OptimisedListTranslator<OWLClassExpression> classExpressionListTranslator;

    // A translator for individual lists (such lists are used in
    // object oneOf constructs)
    private OptimisedListTranslator<OWLIndividual> individualListTranslator;

    private OptimisedListTranslator<OWLObjectPropertyExpression> objectPropertyListTranslator;

    private OptimisedListTranslator<OWLLiteral> constantListTranslator;

    private OptimisedListTranslator<OWLDataPropertyExpression> dataPropertyListTranslator;

    private OptimisedListTranslator<OWLDataRange> dataRangeListTranslator;

    private OptimisedListTranslator<OWLFacetRestriction> faceRestrictionListTranslator;

    // Handlers for built in types
    private Map<URI, BuiltInTypeHandler> builtInTypeTripleHandlers;

    // Handlers for build in predicates
    private Map<URI, TriplePredicateHandler> predicateHandlers;

    private Map<URI, AbstractLiteralTripleHandler> skosTripleHandlers;

    // Handlers for general literal triples (i.e. triples which
    // have predicates that are not part of the built in OWL/RDFS/RDF
    // vocabulary.  Such triples either constitute annotationURIs of
    // relationships between an individual and a data literal (typed or
    // untyped)
    private List<AbstractLiteralTripleHandler> literalTripleHandlers;

    // Handlers for general resource triples (i.e. triples which
    // have predicates that are not part of the built in OWL/RDFS/RDF
    // vocabulary.  Such triples either constitute annotationURIs or
    // relationships between an individual and another individual.
    private List<AbstractResourceTripleHandler> resourceTripleHandlers;

    private Set<OWLAnnotation> pendingAnnotations = new HashSet<OWLAnnotation>();

    private Map<URI, Set<URI>> annotatedAnonSource2AnnotationMap = new HashMap<URI, Set<URI>>();

    /**
     * The ontology that the RDF will be parsed into
     */
    private OWLOntology ontology;

    private RDFXMLOntologyFormat rdfxmlOntologyFormat;

    private OWLDataFactory dataFactory;

    private ClassExpressionTranslatorSelector classExpressionTranslatorSelector;

    private OWLAxiom lastAddedAxiom;

    private Map<URI, URI> synonymMap;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    // SWRL Stuff

    private Set<URI> swrlRules;

    private Set<URI> swrlIndividualPropertyAtoms;

    private Set<URI> swrlDataValuedPropertyAtoms;

    private Set<URI> swrlClassAtoms;

    private Set<URI> swrlDataRangeAtoms;

    private Set<URI> swrlBuiltInAtoms;

    private Set<URI> swrlVariables;

    private Set<URI> swrlSameAsAtoms;

    private Set<URI> swrlDifferentFromAtoms;

    private URIProvider uriProvider;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    public OWLRDFConsumer(OWLOntologyManager owlOntologyManager, OWLOntology ontology, AnonymousNodeChecker checker) {
        classExpressionTranslatorSelector = new ClassExpressionTranslatorSelector(this);
        this.owlOntologyManager = owlOntologyManager;
        this.ontology = ontology;
        this.dataFactory = owlOntologyManager.getOWLDataFactory();
        this.anonymousNodeChecker = checker;
        owlClassURIs = CollectionFactory.createSet();
        objectPropertyURIs = CollectionFactory.createSet();
        dataPropertyURIs = CollectionFactory.createSet();
        individualURIs = CollectionFactory.createSet();
        annotationPropertyURIs = CollectionFactory.createSet();
        annotationPropertyURIs.addAll(OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTIES);
        annotationURIs = new HashSet<URI>();
        annotationURI2Annotation = new HashMap<URI, OWLAnnotation>();
        annotationsBySubject = new HashMap<URI, Set<OWLAnnotation>>();
        ontologyPropertyURIs = CollectionFactory.createSet();
        ontologyPropertyURIs.add(OWLRDFVocabulary.OWL_PRIOR_VERSION.getURI());
        ontologyPropertyURIs.add(OWLRDFVocabulary.OWL_BACKWARD_COMPATIBLE_WITH.getURI());
        ontologyPropertyURIs.add(OWLRDFVocabulary.OWL_INCOMPATIBLE_WITH.getURI());

        addDublinCoreAnnotationURIs();
        dataRangeURIs = CollectionFactory.createSet();
        propertyURIs = CollectionFactory.createSet();
        restrictionURIs = CollectionFactory.createSet();
        ontologyURIs = CollectionFactory.createSet();
        listURIs = CollectionFactory.createSet();
        listFirstLiteralTripleMap = CollectionFactory.createMap();
        listFirstResourceTripleMap = CollectionFactory.createMap();
        listRestTripleMap = CollectionFactory.createMap();
        reifiedAxiomsMap = CollectionFactory.createMap();
        classExpressionListTranslator = new OptimisedListTranslator<OWLClassExpression>(this, new ClassExpressionListItemTranslator(this));
        individualListTranslator = new OptimisedListTranslator<OWLIndividual>(this, new IndividualListItemTranslator(this));
        constantListTranslator = new OptimisedListTranslator<OWLLiteral>(this, new TypedConstantListItemTranslator(this));
        objectPropertyListTranslator = new OptimisedListTranslator<OWLObjectPropertyExpression>(this, new ObjectPropertyListItemTranslator(this));

        dataPropertyListTranslator = new OptimisedListTranslator<OWLDataPropertyExpression>(this, new DataPropertyListItemTranslator(this));

        dataRangeListTranslator = new OptimisedListTranslator<OWLDataRange>(this, new DataRangeListItemTranslator(this));

        faceRestrictionListTranslator = new OptimisedListTranslator<OWLFacetRestriction>(this, new OWLFacetRestrictionListItemTranslator(this));

        builtInTypeTripleHandlers = CollectionFactory.createMap();
        setupTypeTripleHandlers();
        setupPredicateHandlers();

        // General literal triples - i.e. triples which have a predicate
        // that is not a built in URI.  Annotation properties get precedence
        // over data properties, so that if we have the statement a:A a:foo a:B and a:foo
        // is typed as both an annotation and data property then the statement will be
        // translated as an annotation on a:A
        literalTripleHandlers = new ArrayList<AbstractLiteralTripleHandler>();
        literalTripleHandlers.add(new GTPAnnotationLiteralHandler(this));
        literalTripleHandlers.add(new GTPDataPropertyAssertionHandler(this));
        literalTripleHandlers.add(new TPFirstLiteralHandler(this));

        // General resource/object triples - i.e. triples which have a predicate
        // that is not a built in URI.  Annotation properties get precedence
        // over object properties, so that if we have the statement a:A a:foo a:B and a:foo
        // is typed as both an annotation and data property then the statement will be
        // translated as an annotation on a:A
        resourceTripleHandlers = new ArrayList<AbstractResourceTripleHandler>();
        resourceTripleHandlers.add(new GTPAnnotationResourceTripleHandler(this));
        resourceTripleHandlers.add(new GTPObjectPropertyAssertionHandler(this));

        dataRangeURIs.addAll(XSDVocabulary.ALL_DATATYPES);
        dataRangeURIs.add(OWLRDFVocabulary.RDFS_LITERAL.getURI());
        dataRangeURIs.addAll(OWLDatatypeVocabulary.getDatatypeURIs());

        swrlRules = new HashSet<URI>();
        swrlIndividualPropertyAtoms = new HashSet<URI>();
        swrlDataValuedPropertyAtoms = new HashSet<URI>();
        swrlClassAtoms = new HashSet<URI>();
        swrlDataRangeAtoms = new HashSet<URI>();
        swrlBuiltInAtoms = new HashSet<URI>();
        swrlVariables = new HashSet<URI>();
        swrlSameAsAtoms = new HashSet<URI>();
        swrlDifferentFromAtoms = new HashSet<URI>();

        owlClassURIs.add(OWLRDFVocabulary.OWL_THING.getURI());
        owlClassURIs.add(OWLRDFVocabulary.OWL_NOTHING.getURI());

        objectPropertyURIs.add(OWLRDFVocabulary.OWL_TOP_OBJECT_PROPERTY.getURI());
        objectPropertyURIs.add(OWLRDFVocabulary.OWL_BOTTOM_OBJECT_PROPERTY.getURI());

        dataPropertyURIs.add(OWLRDFVocabulary.OWL_TOP_DATA_PROPERTY.getURI());
        dataPropertyURIs.add(OWLRDFVocabulary.OWL_BOTTOM_DATA_PROPERTY.getURI());


        setupSynonymMap();
        setupSinglePredicateMaps();

        setupSKOSTipleHandlers();
    }

    public void setURIProvider(URIProvider uriProvider) {
        this.uriProvider = uriProvider;
    }

    private void addSingleValuedResPredicate(OWLRDFVocabulary v) {
        Map<URI, URI> map = CollectionFactory.createMap();
        singleValuedResTriplesByPredicate.put(v.getURI(), map);
    }


    private void addDublinCoreAnnotationURIs() {
        for (URI uri : DublinCoreVocabulary.ALL_URIS) {
            annotationPropertyURIs.add(uri);
        }
    }


    private void setupSinglePredicateMaps() {
        addSingleValuedResPredicate(OWL_ON_PROPERTY);
        addSingleValuedResPredicate(OWL_SOME_VALUES_FROM);
        addSingleValuedResPredicate(OWL_ALL_VALUES_FROM);
//        addSingleValuedResPredicate(OWL_ONE_OF);
        addSingleValuedResPredicate(OWL_ON_CLASS);
        addSingleValuedResPredicate(OWL_ON_DATA_RANGE);
//        addSingleValuedResPredicate(OWL_INTERSECTION_OF);
//        addSingleValuedResPredicate(OWL_UNION_OF);
//        addSingleValuedResPredicate(OWL_COMPLEMENT_OF);
    }


    private void setupSynonymMap() {
        // We can load legacy ontologies by providing synonyms for built in vocabulary
        // where the vocabulary has simply changed (e.g. DAML+OIL -> OWL)

        synonymMap = CollectionFactory.createMap();
        // Legacy protege-owlapi representation of QCRs
        synonymMap.put(URI.create(Namespaces.OWL + "valuesFrom"), OWL_ON_CLASS.getURI());

        // Intermediate OWL 2 spec
        synonymMap.put(OWL_SUBJECT.getURI(), OWL_ANNOTATED_SOURCE.getURI());
        synonymMap.put(OWL_PREDICATE.getURI(), OWL_ANNOTATED_PROPERTY.getURI());
        synonymMap.put(OWL_OBJECT.getURI(), OWL_ANNOTATED_TARGET.getURI());

        // Preliminary OWL 1.1 Vocab
        synonymMap.put(URI.create(Namespaces.OWL + "cardinalityType"), OWL_ON_CLASS.getURI());
        synonymMap.put(URI.create(Namespaces.OWL + "dataComplementOf"), OWL_COMPLEMENT_OF.getURI());
        synonymMap.put(OWL_ANTI_SYMMETRIC_PROPERTY.getURI(), OWL_ASYMMETRIC_PROPERTY.getURI());

        synonymMap.put(OWL_DATA_RANGE.getURI(), OWL_DATATYPE.getURI());

        // DAML+OIL -> OWL
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#subClassOf"), RDFS_SUBCLASS_OF.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#imports"), OWL_IMPORTS.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#range"), RDFS_RANGE.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#hasValue"), OWL_HAS_VALUE.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#type"), RDF_TYPE.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#domain"), RDFS_DOMAIN.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#versionInfo"), OWL_VERSION_INFO.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#comment"), RDFS_COMMENT.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#onProperty"), OWL_ON_PROPERTY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#toClass"), OWL_ALL_VALUES_FROM.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#hasClass"), OWL_SOME_VALUES_FROM.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#Restriction"), OWL_RESTRICTION.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#Class"), OWL_CLASS.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#Thing"), OWL_THING.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#Nothing"), OWL_NOTHING.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#minCardinality"), OWL_MIN_CARDINALITY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#cardinality"), OWL_CARDINALITY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#maxCardinality"), OWL_MAX_CARDINALITY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#inverseOf"), OWL_INVERSE_OF.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#samePropertyAs"), OWL_EQUIVALENT_PROPERTY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#hasClassQ"), OWL_ON_CLASS.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#cardinalityQ"), OWL_CARDINALITY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#maxCardinalityQ"), OWL_MAX_CARDINALITY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#minCardinalityQ"), OWL_MIN_CARDINALITY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#complementOf"), OWL_COMPLEMENT_OF.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#unionOf"), OWL_UNION_OF.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#intersectionOf"), OWL_INTERSECTION_OF.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#label"), RDFS_LABEL.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#ObjectProperty"), OWL_OBJECT_PROPERTY.getURI());
        synonymMap.put(URI.create("http://www.daml.org/2001/03/daml+oil#DatatypeProperty"), OWL_DATA_PROPERTY.getURI());
        setupLegacyOWLSpecStuff();
    }


    /**
     * There may be some ontologies floating about that use early versions
     * of the OWL 1.1 vocabulary.  We can map early versions of the vocabulary
     * to the current OWL 1.1 vocabulary.
     */
    private void setupLegacyOWLSpecStuff() {
        for (OWLRDFVocabulary v : OWLRDFVocabulary.values()) {
            addLegacyMapping(v);
        }
        for (OWLFacet v : OWLFacet.values()) {
            synonymMap.put(URI.create(Namespaces.OWL.toString() + v.getShortName()), v.getIRI().toURI());
            synonymMap.put(URI.create(Namespaces.OWL11.toString() + v.getShortName()), v.getIRI().toURI());
            synonymMap.put(URI.create(Namespaces.OWL2.toString() + v.getShortName()), v.getIRI().toURI());
        }
        for (OWLFacet v : OWLFacet.values()) {
            synonymMap.put(URI.create(Namespaces.OWL2.toString() + v.getShortName()), v.getIRI().toURI());
        }

    }


    private void addLegacyMapping(OWLRDFVocabulary v) {
        // Map OWL11 to OWL
        // Map OWL2 to OWL
        synonymMap.put(URI.create(Namespaces.OWL2.toString() + v.getShortName()), v.getURI());
        synonymMap.put(URI.create(Namespaces.OWL11.toString() + v.getShortName()), v.getURI());
    }

    private void setupSKOSTipleHandlers() {
        skosTripleHandlers = new HashMap<URI, AbstractLiteralTripleHandler>();
        addSKOSTripleHandler(SKOSVocabulary.ALTLABEL);
        addSKOSTripleHandler(SKOSVocabulary.CHANGENOTE);
        addSKOSTripleHandler(SKOSVocabulary.COMMENT);
        addSKOSTripleHandler(SKOSVocabulary.DEFINITION);
        addSKOSTripleHandler(SKOSVocabulary.DOCUMENT);
        addSKOSTripleHandler(SKOSVocabulary.EDITORIALNOTE);
        addSKOSTripleHandler(SKOSVocabulary.HIDDENLABEL);
        addSKOSTripleHandler(SKOSVocabulary.PREFLABEL);
        addSKOSTripleHandler(SKOSVocabulary.SCOPENOTE);
    }

    private void addSKOSTripleHandler(SKOSVocabulary dataPredicate) {
        skosTripleHandlers.put(dataPredicate.getURI(), new SKOSDataTripleHandler(this, dataPredicate.getURI()));
    }

    public OWLOntology getOntology() {
        return ontology;
    }


    public RDFXMLOntologyFormat getOntologyFormat() {
        return rdfxmlOntologyFormat;
    }


    public void setOntologyFormat(RDFXMLOntologyFormat format) {
        this.rdfxmlOntologyFormat = format;
    }


    private void addBuiltInTypeTripleHandler(BuiltInTypeHandler handler) {
        builtInTypeTripleHandlers.put(handler.getTypeURI(), handler);
    }


    private void setupTypeTripleHandlers() {
        addBuiltInTypeTripleHandler(new TypeAntisymmetricPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeAsymmetricPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeClassHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeDatatypeHandler(this));
        addBuiltInTypeTripleHandler(new TypeFunctionalDataPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeFunctionalObjectPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeFunctionalPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeInverseFunctionalPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeIrreflexivePropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeObjectPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeReflexivePropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeSymmetricPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeTransitivePropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeRestrictionHandler(this));
        addBuiltInTypeTripleHandler(new TypeObjectRestrictionHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataRestrictionHandler(this));
        addBuiltInTypeTripleHandler(new TypeListHandler(this));
        addBuiltInTypeTripleHandler(new TypeAnnotationPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeDeprecatedClassHandler(this));
        addBuiltInTypeTripleHandler(new TypeDataRangeHandler(this));
        addBuiltInTypeTripleHandler(new TypeAllDifferentHandler(this));
        addBuiltInTypeTripleHandler(new TypeOntologyHandler(this));
        addBuiltInTypeTripleHandler(new TypeNegativeObjectPropertyAssertionHandler(this));
        addBuiltInTypeTripleHandler(new TypeNegativeDataPropertyAssertionHandler(this));
        addBuiltInTypeTripleHandler(new TypeAxiomHandler(this));
        addBuiltInTypeTripleHandler(new TypeRDFPropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeRDFSClassHandler(this));
        addBuiltInTypeTripleHandler(new TypeSelfRestrictionHandler(this));
        addBuiltInTypeTripleHandler(new TypePropertyHandler(this));
        addBuiltInTypeTripleHandler(new TypeAllDisjointClassesHandler(this));
        addBuiltInTypeTripleHandler(new TypeAllDisjointPropertiesHandler(this));
        addBuiltInTypeTripleHandler(new TypeNamedIndividualHandler(this));
        addBuiltInTypeTripleHandler(new TypeAnnotationHandler(this));

        addBuiltInTypeTripleHandler(new TypeSWRLAtomListHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLBuiltInAtomHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLBuiltInHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLClassAtomHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLDataRangeAtomHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLDataValuedPropertyAtomHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLDifferentIndividualsAtomHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLImpHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLIndividualPropertyAtomHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLSameIndividualAtomHandler(this));
        addBuiltInTypeTripleHandler(new TypeSWRLVariableHandler(this));

        addBuiltInTypeTripleHandler(new SKOSConceptTripleHandler(this));
    }


    private void addPredicateHandler(TriplePredicateHandler predicateHandler) {
        predicateHandlers.put(predicateHandler.getPredicateURI(), predicateHandler);
    }


    private void setupPredicateHandlers() {
        predicateHandlers = CollectionFactory.createMap();
        addPredicateHandler(new TPDataPropertDomainHandler(this));
        addPredicateHandler(new TPDataPropertyRangeHandler(this));
        addPredicateHandler(new TPDifferentFromHandler(this));
        addPredicateHandler(new TPDisjointDataPropertiesHandler(this));
        addPredicateHandler(new TPDisjointObjectPropertiesHandler(this));
        addPredicateHandler(new TPDisjointUnionHandler(this));
        addPredicateHandler(new TPDisjointWithHandler(this));
        addPredicateHandler(new TPEquivalentClassHandler(this));
        addPredicateHandler(new TPEquivalentDataPropertyHandler(this));
        addPredicateHandler(new TPEquivalentObjectPropertyHandler(this));
        addPredicateHandler(new TPEquivalentPropertyHandler(this));
        addPredicateHandler(new TPObjectPropertyDomainHandler(this));
        addPredicateHandler(new TPObjectPropertyRangeHandler(this));
        addPredicateHandler(new TPPropertyDomainHandler(this));
        addPredicateHandler(new TPPropertyRangeHandler(this));
        addPredicateHandler(new TPSameAsHandler(this));
        addPredicateHandler(new TPSubClassOfHandler(this));
        addPredicateHandler(new TPSubDataPropertyOfHandler(this));
        addPredicateHandler(new TPSubObjectPropertyOfHandler(this));
        addPredicateHandler(new TPSubPropertyOfHandler(this));
        addPredicateHandler(new TPTypeHandler(this));
        addPredicateHandler(new TPInverseOfHandler(this));
        addPredicateHandler(new TPDistinctMembersHandler(this));
        addPredicateHandler(new TPImportsHandler(this));
        addPredicateHandler(new TPIntersectionOfHandler(this));
        addPredicateHandler(new TPUnionOfHandler(this));
        addPredicateHandler(new TPComplementOfHandler(this));
        addPredicateHandler(new TPOneOfHandler(this));
        addPredicateHandler(new TPOnPropertyHandler(this));
        addPredicateHandler(new TPSomeValuesFromHandler(this));
        addPredicateHandler(new TPAllValuesFromHandler(this));
        addPredicateHandler(new TPRestHandler(this));
        addPredicateHandler(new TPFirstResourceHandler(this));
        addPredicateHandler(new TPDeclaredAsHandler(this));
        addPredicateHandler(new TPHasKeyHandler(this));
        addPredicateHandler(new TPVersionIRIHandler(this));
        addPredicateHandler(new TPPropertyChainAxiomHandler(this));
        addPredicateHandler(new TPAnnotatedSourceHandler(this));

        addPredicateHandler(new SKOSObjectTripleHandler(this, SKOSVocabulary.BROADER));
        addPredicateHandler(new SKOSObjectTripleHandler(this, SKOSVocabulary.NARROWER));
        addPredicateHandler(new SKOSObjectTripleHandler(this, SKOSVocabulary.RELATED));
        addPredicateHandler(new SKOSObjectTripleHandler(this, SKOSVocabulary.HASTOPCONCEPT));
        addPredicateHandler(new SKOSObjectTripleHandler(this, SKOSVocabulary.SEMANTICRELATION));


    }


    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }


    // We cache URIs to save memory!!
    private Map<String, URI> uriMap = CollectionFactory.createMap();

    int currentBaseCount = 0;

    /**
     * Gets any annotations that were translated since the last call of this method (calling
     * this method clears the current pending annotations)
     * @return The set (possibly empty) of pending annotations.
     */
    public Set<OWLAnnotation> getPendingAnnotations() {
        if (!pendingAnnotations.isEmpty()) {
            Set<OWLAnnotation> annos = new HashSet<OWLAnnotation>(pendingAnnotations);
            pendingAnnotations.clear();
            return annos;
        }
        else {
            return Collections.emptySet();
        }
    }

    public void setPendingAnnotations(Set<OWLAnnotation> annotations) {
        pendingAnnotations.clear();
        pendingAnnotations.addAll(annotations);
    }


    private URI getURI(String s) {
        URI uri = null;
        if (uriProvider != null) {
            uri = uriProvider.getURI(s);
        }
        if (uri != null) {
            return uri;
        }
        uri = uriMap.get(s);
        if (uri == null) {
            uri = URI.create(s);
            uriMap.put(s, uri);
        }
        return uri;
    }

    public void importsClosureChanged() {
        for (OWLOntology ont : owlOntologyManager.getImportsClosure(ontology)) {
            annotationPropertyURIs.addAll(ont.getAnnotationURIs());
        }
    }


    /**
     * Checks whether a node is anonymous.
     * @param uri The URI of the node to be checked.
     * @return <code>true</code> if the node is anonymous, or
     *         <code>false</code> if the node is not anonymous.
     */
    protected boolean isAnonymousNode(URI uri) {
        return anonymousNodeChecker.isAnonymousNode(uri);
    }


    protected void addAxiom(OWLAxiom axiom) {
        try {
            owlOntologyManager.applyChange(new AddAxiom(ontology, axiom));
            lastAddedAxiom = axiom;
        }
        catch (OWLOntologyChangeException e) {
            logger.severe(e.getMessage());
        }
        // Consider recording which entity URIs have been used here. This
        // might make translation of "dangling entities" faster (at the expense
        // of memory).
    }

    protected void applyChange(OWLOntologyChange change) {
        try {
            owlOntologyManager.applyChange(change);
        }
        catch (OWLOntologyChangeException e) {
            logger.severe(e.getMessage());
        }
    }

    protected void setOntologyID(OWLOntologyID ontologyID) {
        applyChange(new SetOntologyID(ontology, ontologyID));
    }

    protected void addOntologyAnnotation(OWLAnnotation annotation) {
        applyChange(new AddOntologyAnnotation(getOntology(), annotation));
    }

    protected void addImport(OWLImportsDeclaration declaration) {
        applyChange(new AddImport(ontology, declaration));
    }

    public OWLAxiom getLastAddedAxiom() {
        return lastAddedAxiom;
    }


    protected void addOWLClass(URI uri) {
        owlClassURIs.add(uri);
    }


    protected void addOWLObjectProperty(URI uri) {
        objectPropertyURIs.add(uri);
    }


    protected void addIndividual(URI uri) {
        individualURIs.add(uri);
    }


    protected boolean isIndividual(URI uri) {
        return individualURIs.contains(uri);
    }


    protected void addRDFProperty(URI uri) {
        propertyURIs.add(uri);
    }


    protected boolean isRDFProperty(URI uri) {
        return propertyURIs.contains(uri);
    }


    protected void addOWLDataProperty(URI uri) {
        dataPropertyURIs.add(uri);
    }


    protected void addOWLDatatype(URI uri) {
        dataRangeURIs.add(uri);
    }


    public void addOWLDataRange(URI uri) {
        dataRangeURIs.add(uri);
    }


    protected void addRestriction(URI uri) {
        restrictionURIs.add(uri);
    }


    protected void addAnnotationProperty(URI uri) {
        annotationPropertyURIs.add(uri);
        if (rdfxmlOntologyFormat != null) {
            rdfxmlOntologyFormat.addAnnotationURI(uri);
        }
    }

    protected void addAnnotationURI(URI uri) {
        annotationURIs.add(uri);
    }


    public boolean isRestriction(URI uri) {
        return restrictionURIs.contains(uri);
    }


    protected boolean isClass(URI uri) {
        if (owlClassURIs.contains(uri)) {
            return true;
        }
        else {
            for (OWLOntology ont : owlOntologyManager.getImportsClosure(ontology)) {
                if (ont.containsClassReference(uri)) {
                    return true;
                }
            }
        }
        return false;
    }


    protected boolean isObjectPropertyOnly(URI uri) {
        if (uri == null) {
            return false;
        }
        if (dataPropertyURIs.contains(uri)) {
            return false;
        }
        if (objectPropertyURIs.contains(uri)) {
            return true;
        }
        else {
            boolean containsObjectPropertyReference = false;
            for (OWLOntology ont : owlOntologyManager.getImportsClosure(ontology)) {
                if (ont.containsDataPropertyReference(uri)) {
                    dataPropertyURIs.add(uri);
                    return false;
                }
                else if (ont.containsObjectPropertyReference(uri)) {
                    containsObjectPropertyReference = true;
                    objectPropertyURIs.add(uri);
                }
            }
            return containsObjectPropertyReference;
        }
    }


    protected boolean isDataPropertyOnly(URI uri) {
        // I don't like the fact that the check to see if something
        // is a data property only includes a check to make sure that
        // it is not an annotation property.  I think the OWL spec should
        // be altered
        if (objectPropertyURIs.contains(uri)) {
            return false;
        }
        if (dataPropertyURIs.contains(uri)) {
            return true;
        }
        else {
            boolean containsDataPropertyReference = false;
            for (OWLOntology ont : owlOntologyManager.getImportsClosure(ontology)) {
                if (ont.containsObjectPropertyReference(uri)) {
                    return false;
                }
                else if (ont.containsDataPropertyReference(uri)) {
                    containsDataPropertyReference = true;
                }
            }
            return containsDataPropertyReference;
        }
    }


    protected boolean isOntologyProperty(URI uri) {
        return ontologyPropertyURIs.contains(uri);
    }


    protected boolean isAnnotationProperty(URI uri) {
        if (annotationPropertyURIs.contains(uri)) {
            return true;
        }
        for (OWLOntology ont : owlOntologyManager.getImportsClosure(ontology)) {
            if (!ont.equals(ontology)) {
                for (OWLAnnotationProperty prop : ont.getReferencedAnnotationProperties()) {
                    annotationPropertyURIs.add(prop.getURI());
                }
//                if (ont.getAnnotationURIs().contains(uri)) {
//                     Cache URI
//                    annotationPropertyURIs.addAll(ont.getAnnotationURIs());
//                    return annotationPropertyURIs.contains(uri);
//                } else {
//                    OWLOntologyFormat format = owlOntologyManager.getOntologyFormat(ont);
//                    if (format instanceof RDFXMLOntologyFormat) {
//                        RDFXMLOntologyFormat rdfFormat = (RDFXMLOntologyFormat) format;
//                        annotationPropertyURIs.addAll(rdfFormat.getAnnotationURIs());
//                        return annotationPropertyURIs.contains(uri);
//                    }
//                }
            }
        }
        return false;
    }


    protected boolean isOntology(URI uri) {
        return ontologyURIs.contains(uri);
    }


    public OWLOntologyManager getOWLOntologyManager() {
        return owlOntologyManager;
    }

    /**
     * Records an annotation of an anonymous node (either an annotation of an annotation, or an annotation
     * of an axiom for example)
     * @param annotatedAnonSource The source that the annotation annotates
     * @param annotationMainNode  The annotations
     */
    public void addAnnotatedSource(URI annotatedAnonSource, URI annotationMainNode) {
        Set<URI> annotationMainNodes = annotatedAnonSource2AnnotationMap.get(annotatedAnonSource);
        if (annotationMainNodes == null) {
            annotationMainNodes = new HashSet<URI>();
            annotatedAnonSource2AnnotationMap.put(annotatedAnonSource, annotationMainNodes);
        }
        annotationMainNodes.add(annotationMainNode);
    }

    /**
     * Gets the main nodes of annotations that annotated the specified source
     * @param source The source (axiom or annotation main node)
     * @return The set of main nodes that annotate the specified source
     */
    public Set<URI> getAnnotatedSourceAnnotationMainNodes(URI source) {
        Set<URI> mainNodes = annotatedAnonSource2AnnotationMap.get(source);
        if (mainNodes != null) {
            return mainNodes;
        }
        else {
            return Collections.emptySet();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Helper methods for creating entities
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    protected OWLClass getOWLClass(URI uri) {
        return getDataFactory().getOWLClass(uri);
    }


    protected OWLObjectProperty getOWLObjectProperty(URI uri) {
        return getDataFactory().getOWLObjectProperty(uri);
    }


    protected OWLDataProperty getOWLDataProperty(URI uri) {
        return getDataFactory().getOWLDataProperty(uri);
    }


    protected OWLIndividual getOWLIndividual(URI uri) {
        if (isAnonymousNode(uri)) {
            return getDataFactory().getOWLAnonymousIndividual(uri.toString());
        }
        else {
            return getDataFactory().getOWLNamedIndividual(uri);
        }
    }


    protected void consumeTriple(URI subject, URI predicate, URI object) {
        isTriplePresent(subject, predicate, object, true);
    }


    protected void consumeTriple(URI subject, URI predicate, OWLLiteral con) {
        isTriplePresent(subject, predicate, con, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // SWRL Stuff
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    protected void addSWRLRule(URI uri) {
        swrlRules.add(uri);
    }


    protected boolean isSWRLRule(URI uri) {
        return swrlRules.contains(uri);
    }


    protected void addSWRLIndividualPropertyAtom(URI uri) {
        swrlIndividualPropertyAtoms.add(uri);
    }


    protected boolean isSWRLIndividualPropertyAtom(URI uri) {
        return swrlIndividualPropertyAtoms.contains(uri);
    }


    protected void addSWRLDataPropertyAtom(URI uri) {
        swrlDataValuedPropertyAtoms.add(uri);
    }


    protected boolean isSWRLDataValuedPropertyAtom(URI uri) {
        return swrlDataValuedPropertyAtoms.contains(uri);
    }


    protected void addSWRLClassAtom(URI uri) {
        swrlClassAtoms.add(uri);
    }


    protected boolean isSWRLClassAtom(URI uri) {
        return swrlClassAtoms.contains(uri);
    }


    protected void addSWRLSameAsAtom(URI uri) {
        swrlSameAsAtoms.add(uri);
    }


    protected boolean isSWRLSameAsAtom(URI uri) {
        return swrlSameAsAtoms.contains(uri);
    }


    protected void addSWRLDifferentFromAtom(URI uri) {
        swrlDifferentFromAtoms.add(uri);
    }


    protected boolean isSWRLDifferentFromAtom(URI uri) {
        return swrlDifferentFromAtoms.contains(uri);
    }


    protected void addSWRLDataRangeAtom(URI uri) {
        swrlDataRangeAtoms.add(uri);
    }


    protected boolean isSWRLDataRangeAtom(URI uri) {
        return swrlDataRangeAtoms.contains(uri);
    }


    protected void addSWRLBuiltInAtom(URI uri) {
        swrlBuiltInAtoms.add(uri);
    }


    protected boolean isSWRLBuiltInAtom(URI uri) {
        return swrlBuiltInAtoms.contains(uri);
    }


    protected void addSWRLVariable(URI uri) {
        swrlVariables.add(uri);
    }


    protected boolean isSWRLVariable(URI uri) {
        return swrlVariables.contains(uri);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    ////
    ////  RDFConsumer implementation
    ////
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private long t0;


    public void handle(URI subject, URI predicate, URI object) {
        if (predicate.equals(OWLRDFVocabulary.RDF_TYPE.getURI())) {
            BuiltInTypeHandler typeHandler = builtInTypeTripleHandlers.get(object);
            if (typeHandler != null) {
                typeHandler.handleTriple(subject, predicate, object);
                // Consumed the triple - no further processing
                return;
            }
            else {
                addIndividual(subject);
            }
        }

        AbstractResourceTripleHandler handler = predicateHandlers.get(predicate);
        if (handler != null) {
            if (handler.canHandle(subject, predicate, object)) {
                handler.handleTriple(subject, predicate, object);
            }
            return;
        }
    }


    public void handle(URI subject, URI predicate, OWLLiteral object) {
        for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
            if (handler.canHandle(subject, predicate, object)) {
                handler.handleTriple(subject, predicate, object);
                break;
            }
        }
    }


    private static void printTriple(Object subject, Object predicate, Object object, PrintWriter w) {
        w.append(subject.toString());
        w.append(" -> ");
        w.append(predicate.toString());
        w.append(" -> ");
        w.append(object.toString());
        w.append("\n");
    }


    private void dumpRemainingTriples() {
        if (!logger.isLoggable(Level.FINE)) {
            return;
        }
        StringWriter sw = new StringWriter();
        PrintWriter w = new PrintWriter(sw);

        for (URI predicate : singleValuedResTriplesByPredicate.keySet()) {
            Map<URI, URI> map = singleValuedResTriplesByPredicate.get(predicate);
            for (URI subject : map.keySet()) {
                URI object = map.get(subject);
                printTriple(subject, predicate, object, w);
            }
        }

        for (URI predicate : singleValuedLitTriplesByPredicate.keySet()) {
            Map<URI, OWLLiteral> map = singleValuedLitTriplesByPredicate.get(predicate);
            for (URI subject : map.keySet()) {
                OWLLiteral object = map.get(subject);
                printTriple(subject, predicate, object, w);
            }
        }

        for (URI subject : new ArrayList<URI>(resTriplesBySubject.keySet())) {
            Map<URI, Set<URI>> map = resTriplesBySubject.get(subject);
            for (URI predicate : new ArrayList<URI>(map.keySet())) {
                Set<URI> objects = map.get(predicate);
                for (URI object : objects) {
                    printTriple(subject, predicate, object, w);
                }
            }
        }
        for (URI subject : new ArrayList<URI>(litTriplesBySubject.keySet())) {
            Map<URI, Set<OWLLiteral>> map = litTriplesBySubject.get(subject);
            for (URI predicate : new ArrayList<URI>(map.keySet())) {
                Set<OWLLiteral> objects = map.get(predicate);
                for (OWLLiteral object : objects) {
                    printTriple(subject, predicate, object, w);
                }
            }
        }
        w.flush();
        logger.fine(sw.getBuffer().toString());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Debug stuff

    private int count = 0;


    private void incrementTripleCount() {
        count++;
        if (tripleProcessor.isLoggable(Level.FINE) && count % 10000 == 0) {
            tripleProcessor.fine("Parsed: " + count + " triples");
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void startModel(String string) throws SAXException {
        count = 0;
        t0 = System.currentTimeMillis();
    }


    /**
     * This is where we do all remaining parsing
     */
    public void endModel() throws SAXException {
//        try {
        uriMap.clear();

        tripleProcessor.fine("Total number of triples: " + count);
        RDFXMLOntologyFormat format = getOntologyFormat();
        if (format != null) {
            format.setNumberOfTriplesProcessedDuringLoading(count);
        }

        // Do we need to change the ontology URI?
        if (!ontologyURIs.contains(ontology.getOntologyID().getOntologyIRI().toURI())) {
            if (ontologyURIs.size() == 1) {
                applyChange(new SetOntologyID(ontology, new OWLOntologyID(IRI.create(firstOntologyURI))));
            }
            else {
                if (ontologyURIs.isEmpty()) {
                    if (xmlBase == null) {
                        logger.fine("There are no resources which are typed as ontologies.  Cannot determine the URI of the ontology being parsed - using physical URI.");
                    }
                    else {
                        logger.fine("There are no resources which are typed as ontologies.  Cannot determine the URI of the ontology being parsed - using xml:base.");
                        applyChange(new SetOntologyID(ontology, new OWLOntologyID(IRI.create(xmlBase))));
                    }
                }
                else {
                    logger.fine("There are multiple resources which are typed as ontologies.  Using the first encountered ontology URI.");
                    applyChange(new SetOntologyID(ontology, new OWLOntologyID(IRI.create(firstOntologyURI))));
                }
            }
        }


        if (tripleProcessor.isLoggable(Level.FINE)) {
            tripleProcessor.fine("Loaded " + ontology.getOntologyID());
        }

        // First mop up any rules triples
        SWRLRuleTranslator translator = new SWRLRuleTranslator(this);
        for (URI ruleURI : swrlRules) {
            translator.translateRule(ruleURI);
        }


        // We need to mop up all remaining triples.  These triples will be in the
        // triples by subject map.  Other triples which reside in the triples by
        // predicate (single valued) triple aren't "root" triples for axioms.  First
        // we translate all system triples and then go for triples whose predicates
        // are not system/reserved vocabulary URIs to translate these into ABox assertions
        // or annotationURIs
        for (URI subject : new ArrayList<URI>(resTriplesBySubject.keySet())) {
            Map<URI, Set<URI>> map = resTriplesBySubject.get(subject);
            if (map == null) {
                continue;
            }
            for (URI predicate : new ArrayList<URI>(map.keySet())) {
                Set<URI> objects = map.get(predicate);
                if (objects == null) {
                    continue;
                }
                for (URI object : new ArrayList<URI>(objects)) {
                    handle(subject, predicate, object);
                }
            }
        }

        // TODO: TIDY UP!  This is a copy and paste hack!!
        // Now for the ABox assertions and annotationURIs
        for (URI subject : new ArrayList<URI>(resTriplesBySubject.keySet())) {
            Map<URI, Set<URI>> map = resTriplesBySubject.get(subject);
            if (map == null) {
                continue;
            }
            for (URI predicate : new ArrayList<URI>(map.keySet())) {
                Set<URI> objects = map.get(predicate);
                if (objects == null) {
                    continue;
                }
                for (URI object : new ArrayList<URI>(objects)) {
                    for (AbstractResourceTripleHandler resTripHandler : resourceTripleHandlers) {
                        if (resTripHandler.canHandle(subject, predicate, object)) {
                            resTripHandler.handleTriple(subject, predicate, object);
                            break;
                        }
                    }
                }
            }
        }


        for (URI subject : new ArrayList<URI>(litTriplesBySubject.keySet())) {
            Map<URI, Set<OWLLiteral>> map = litTriplesBySubject.get(subject);
            if (map == null) {
                continue;
            }
            for (URI predicate : new ArrayList<URI>(map.keySet())) {
                Set<OWLLiteral> objects = map.get(predicate);
                for (OWLLiteral object : new ArrayList<OWLLiteral>(objects)) {
                    handle(subject, predicate, object);
                }
            }
        }

//        translateDanglingEntities();

        dumpRemainingTriples();
//        }
//        catch (OWLException e) {
//            throw new SAXException(e);
//        }
        cleanup();
    }


    private Set<URI> getAnnotationsForSubject(URI subject) {
        Set<URI> result = new HashSet<URI>();
        for (URI annoURI : annotationURIs) {
            URI annoURISubject = getResourceObject(annoURI, OWL_SUBJECT.getURI(), false);
            if (annoURISubject != null && annoURISubject.equals(subject)) {
                result.add(annoURI);
            }
        }
        return result;
    }

//    private OWLAnnotation translateAnnotation(URI node) {
//        OWLAnnotation anno = annotationURI2Annotation.get(node);
//        if (anno != null) {
//            return anno;
//        }
//        URI subject = getResourceObject(node, OWL_SUBJECT.getURI(), true);
//        URI predicate = getResourceObject(node, OWL_PREDICATE.getURI(), true);
//        Object object = getResourceObject(node, OWL_OBJECT.getURI(), true);
//        if (object == null) {
//            object = getLiteralObject(node, OWL_OBJECT.getURI(), true);
//        }
//        getResourceObject(node, predicate, true);
//        // We now have to get any annotations on this annotation
//        Set<OWLAnnotation> translatedAnnotations = new HashSet<OWLAnnotation>();
//        for (URI annotationURI : getAnnotationsForSubject(node)) {
//            OWLAnnotation annotation = translateAnnotation(annotationURI);
//            translatedAnnotations.add(annotation);
//        }
//
//
//        if (object == null) {
//            System.out.println("NULL");
//            return null;
//        }
//        else {
//            OWLAnnotationValue value = translateAnnotationValue(object);
//            OWLAnnotation annotation = getDataFactory().getOWLAnnotation(getDataFactory().getOWLAnnotationProperty(predicate), value, translatedAnnotations);
//            System.out.println("Translated annotation: " + annotation);
//            return annotation;
//        }
//
//
//    }


    private OWLAnnotationValue translateAnnotationValue(Object object) {
        OWLAnnotationValue value;
        if (object instanceof URI) {
            URI uri = (URI) object;
            if (isAnonymousNode(uri)) {
                value = getDataFactory().getOWLAnonymousIndividual(uri.toString());
            }
            else {
                value = getDataFactory().getIRI(uri);
            }

        }
        else if (object instanceof OWLLiteral) {
            value = (OWLLiteral) object;
        }
        else {
            throw new RuntimeException("Unknown type of annotation value: " + object);
        }
        return value;
    }

    private void translateDanglingEntities() {
        owlClassURIs.remove(OWLRDFVocabulary.OWL_THING.getURI());
        owlClassURIs.remove(OWLRDFVocabulary.OWL_NOTHING.getURI());
        for (URI clsURI : owlClassURIs) {
            if (!isAnonymousNode(clsURI)) {
                OWLClass cls = getDataFactory().getOWLClass(clsURI);
                addDeclarationIfNecessary(cls);
            }
        }
        for (URI propURI : objectPropertyURIs) {
            if (!isAnonymousNode(propURI)) {
                OWLObjectProperty prop = getDataFactory().getOWLObjectProperty(propURI);
                addDeclarationIfNecessary(prop);
            }
        }
        for (URI propURI : dataPropertyURIs) {
            OWLDataProperty prop = getDataFactory().getOWLDataProperty(propURI);
            addDeclarationIfNecessary(prop);
        }
        // We don't need to do this with individuals, since there is no
        // such things a x rdf:type OWLIndividual
    }


    private void addDeclarationIfNecessary(OWLEntity entity) {
        OWLOntology ont = getOntology();
        if (!ont.containsEntityReference(entity)) {
            boolean ref = false;
            for (OWLOntology o : getOWLOntologyManager().getImportsClosure(ont)) {
                if (!o.equals(ont)) {
                    if (ref = o.containsEntityReference(entity)) {
                        break;
                    }
                }
            }
            if (!ref) {
                addAxiom(getDataFactory().getOWLDeclarationAxiom(entity));
            }
        }
    }


    private void cleanup() {
        owlClassURIs.clear();
        objectPropertyURIs.clear();
        dataPropertyURIs.clear();
        dataRangeURIs.clear();
        restrictionURIs.clear();
        listFirstLiteralTripleMap.clear();
        listFirstResourceTripleMap.clear();
        listRestTripleMap.clear();
        translatedClassExpression.clear();
        listURIs.clear();
        resTriplesBySubject.clear();
        litTriplesBySubject.clear();
        singleValuedLitTriplesByPredicate.clear();
        singleValuedResTriplesByPredicate.clear();
    }


    public void addModelAttribte(String string, String string1) throws SAXException {
    }


    public void includeModel(String string, String string1) throws SAXException {

    }


    public void logicalURI(String string) throws SAXException {

    }


    public URI checkForSynonym(URI original) {
        URI synonymURI = synonymMap.get(original);
        if (synonymURI != null) {
            return synonymURI;
        }
        return original;
    }

    public void statementWithLiteralValue(String subject, String predicate, String object, String lang, String datatype) throws SAXException {
        incrementTripleCount();
        URI subjectURI = getURI(subject);
        URI predicateURI = getURI(predicate);
        predicateURI = checkForSynonym(predicateURI);
        handleStreaming(subjectURI, predicateURI, object, datatype, lang);
    }


    public void statementWithResourceValue(String subject, String predicate, String object) throws SAXException {
        incrementTripleCount();
        URI subjectURI = getURI(subject);
        URI predicateURI = getURI(predicate);
        predicateURI = checkForSynonym(predicateURI);
        URI objectURI = checkForSynonym(getURI(object));
        handleStreaming(subjectURI, predicateURI, objectURI);
    }


    private int addCount = 0;


    /**
     * Called when a resource triple has been parsed.
     * @param subject   The subject of the triple that has been parsed
     * @param predicate The predicate of the triple that has been parsed
     * @param object    The object of the triple that has been parsed
     */
    private void handleStreaming(URI subject, URI predicate, URI object) {
        if (predicate.equals(RDF_TYPE.getURI())) {
            BuiltInTypeHandler handler = builtInTypeTripleHandlers.get(object);
            if (handler != null) {
                if (handler.canHandleStreaming(subject, predicate, object)) {
                    handler.handleTriple(subject, predicate, object);
                    // Consumed the triple - no further processing
                    return;
                }
            }
            else {
                // Individual?
                addIndividual(subject);
            }
        }
        AbstractResourceTripleHandler handler = predicateHandlers.get(predicate);
        if (handler != null) {
            if (handler.canHandleStreaming(subject, predicate, object)) {
                handler.handleTriple(subject, predicate, object);
                return;
            }
        }
//        if(addCount < 10000) {
//            if(!predicate.equals(OWLRDFVocabulary.OWL_ON_PROPERTY.getIRI())) {
//                addCount++;
//            }

//        }
        // Not consumed, so add the triple
        addTriple(subject, predicate, object);
    }

    private void handleStreaming(URI subject, URI predicate, String literal, String datatype, String lang) {
        // Convert all literals to OWLConstants
        OWLLiteral con = getOWLConstant(literal, datatype, lang);
        AbstractLiteralTripleHandler skosHandler = skosTripleHandlers.get(predicate);
        if (skosHandler != null) {
            skosHandler.handleTriple(subject, predicate, con);
            return;
        }
        for (AbstractLiteralTripleHandler handler : literalTripleHandlers) {
            if (handler.canHandleStreaming(subject, predicate, con)) {
                handler.handleTriple(subject, predicate, con);
                return;
            }
        }
        addTriple(subject, predicate, con);
    }


    /**
     * A convenience method to obtain an <code>OWLConstant</code>
     * @param literal  The literal - must NOT be <code>null</code>
     * @param datatype The data type - may be <code>null</code>
     * @param lang     The lang - may be <code>null</code>
     * @return The <code>OWLConstant</code> (either typed or untyped depending on the params)
     */
    private OWLLiteral getOWLConstant(String literal, String datatype, String lang) {
        if (datatype != null) {
            return dataFactory.getOWLTypedLiteral(literal, dataFactory.getOWLDatatype(getURI(datatype)));
        }
        else {
            return dataFactory.getOWLStringLiteral(literal, lang);
        }
    }


    public OWLDataRange translateDataRange(URI uri) {
        URI oneOfObject = getResourceObject(uri, OWL_ONE_OF.getURI(), true);
        if (oneOfObject != null) {
            Set<OWLLiteral> literals = translateToConstantSet(oneOfObject);
            Set<OWLTypedLiteral> typedConstants = new HashSet<OWLTypedLiteral>(literals.size());
            for (OWLLiteral con : literals) {
                if (con.isTyped()) {
                    typedConstants.add((OWLTypedLiteral) con);
                }
                else {
                    typedConstants.add(getDataFactory().getOWLTypedLiteral(con.getLiteral(), getDataFactory().getOWLDatatype(XSDVocabulary.STRING.getURI())));
                }
            }
            return getDataFactory().getOWLDataOneOf(typedConstants);
        }
        URI intersectionOfObject = getResourceObject(uri, OWL_INTERSECTION_OF.getURI(), true);
        if (intersectionOfObject != null) {
            Set<OWLDataRange> dataRanges = translateToDataRangeSet(intersectionOfObject);
            return getDataFactory().getOWLDataIntersectionOf(dataRanges);
        }
        URI unionOfObject = getResourceObject(uri, OWL_UNION_OF.getURI(), true);
        if (unionOfObject != null) {
            Set<OWLDataRange> dataRanges = translateToDataRangeSet(unionOfObject);
            return getDataFactory().getOWLDataUnionOf(dataRanges);
        }
        // The plain complement of triple predicate is in here for legacy reasons
        URI complementOfObject = getResourceObject(uri, OWL_DATATYPE_COMPLEMENT_OF.getURI(), true);
        if (complementOfObject == null) {
            complementOfObject = getResourceObject(uri, OWL_COMPLEMENT_OF.getURI(), true);
        }
        if (complementOfObject != null) {
            OWLDataRange operand = translateDataRange(complementOfObject);
            return getDataFactory().getOWLDataComplementOf(operand);
        }

        URI onDatatypeObject = getResourceObject(uri, OWL_ON_DATA_TYPE.getURI(), true);
        if (onDatatypeObject == null) {
            onDatatypeObject = getResourceObject(uri, OWL_ON_DATA_RANGE.getURI(), true);
        }
        if (onDatatypeObject != null) {
            OWLDatatype restrictedDataRange = (OWLDatatype) translateDataRange(onDatatypeObject);

            // Consume the datatype type triple
            getResourceObject(uri, RDF_TYPE.getURI(), true);
            // Now we have to get the restricted facets - there is some legacy translation code here... the current
            // spec uses a list of triples where the predicate is a facet and the object a literal that is restricted
            // by the facet.  Originally, there just used to be multiple facet-"facet value" triples

            Set<OWLFacetRestriction> restrictions = new HashSet<OWLFacetRestriction>();

            URI facetRestrictionList = getResourceObject(uri, OWL_WITH_RESTRICTIONS.getURI(), true);
            if (facetRestrictionList != null) {
                restrictions = translateToFacetRestrictionSet(facetRestrictionList);
            }
            else {
                // Try the legacy encoding
                for (IRI facetIRI : OWLFacet.FACET_IRIS) {
                    OWLLiteral val;
                    while ((val = getLiteralObject(uri, facetIRI.toURI(), true)) != null) {
                        restrictions.add(dataFactory.getOWLFacetRestriction(OWLFacet.getFacet(facetIRI), val));
                    }
                }
            }


            return dataFactory.getOWLDatatypeRestriction(restrictedDataRange, restrictions);
        }
        return getDataFactory().getOWLDatatype(uri);
    }


    public OWLDataPropertyExpression translateDataPropertyExpression(URI uri) {
        return dataFactory.getOWLDataProperty(uri);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Basic node translation - translation of entities
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Map<URI, OWLObjectPropertyExpression> translatedProperties = new HashMap<URI, OWLObjectPropertyExpression>();

    public OWLObjectPropertyExpression translateObjectPropertyExpression(URI mainNode) {
        OWLObjectPropertyExpression prop = translatedProperties.get(mainNode);
        if (prop != null) {
            return prop;
        }
        addOWLObjectProperty(mainNode);
        if (!isAnonymousNode(mainNode)) {
            // Simple object property
            prop = getDataFactory().getOWLObjectProperty(mainNode);
            translatedProperties.put(mainNode, prop);
            return prop;
        }
        else {
            // Inverse of a property expression

            URI inverseOfObject = getResourceObject(mainNode, OWL_INVERSE_OF.getURI(), true);
            if (inverseOfObject == null) {
                throw new IllegalStateException("Attempting to translate inverse property (anon property), but inverseOf triple is missing (" + mainNode + ")");
            }
            OWLObjectPropertyExpression otherProperty = translateObjectPropertyExpression(inverseOfObject);
            prop = getDataFactory().getOWLObjectInverseOf(otherProperty);
            translatedProperties.put(mainNode, prop);
            return prop;
        }
    }


    public OWLIndividual translateIndividual(URI node) {
        return getOWLIndividual(node);
    }

    /**
     * Translates the annotation on a main node.  Triples whose subject is the specified main node and whose subject
     * is typed an an annotation property (or is a built in annotation property) will be translated to annotation on
     * this main node.
     * @param mainNode The main node
     * @return The set of annotations on the main node
     */
    public Set<OWLAnnotation> translateAnnotations(URI mainNode) {
        // Are we the subject of an annotation?  If so, we need to ensure that the annotations annotate us.  This
        // will only happen if we are an annotation!
        Set<OWLAnnotation> annosOnMainNodeAnnotations = new HashSet<OWLAnnotation>();
        Set<URI> annotationMainNodes = getAnnotatedSourceAnnotationMainNodes(mainNode);
        if(!annotationMainNodes.isEmpty()) {
            for(URI annotationMainNode : annotationMainNodes) {
                annosOnMainNodeAnnotations.addAll(translateAnnotations(annotationMainNode));
            }
        }

        Set<OWLAnnotation> mainNodeAnnotations = new HashSet<OWLAnnotation>();
        Set<URI> predicates = getPredicatesBySubject(mainNode);
        for (URI predicate : predicates) {
            if (isAnnotationProperty(predicate)) {
                URI resVal = getResourceObject(mainNode, predicate, true);
                while (resVal != null) {
                    OWLAnnotationProperty prop = getDataFactory().getOWLAnnotationProperty(predicate);
                    OWLAnnotationValue val;
                    if (isAnonymousNode(resVal)) {
                        val = getDataFactory().getOWLAnonymousIndividual(resVal.toString());
                    }
                    else {
                        val = IRI.create(resVal);
                    }
                    mainNodeAnnotations.add(getDataFactory().getOWLAnnotation(prop, val, annosOnMainNodeAnnotations));
                    resVal = getResourceObject(mainNode, predicate, true);
                }
                OWLLiteral litVal = getLiteralObject(mainNode, predicate, true);
                while (litVal != null) {
                    OWLAnnotationProperty prop = getDataFactory().getOWLAnnotationProperty(predicate);
                    mainNodeAnnotations.add(getDataFactory().getOWLAnnotation(prop, litVal, annosOnMainNodeAnnotations));
                    litVal = getLiteralObject(mainNode, predicate, true);
                }
            }
        }
        return mainNodeAnnotations;
    }

    private Map<URI, OWLClassExpression> translatedClassExpression = new HashMap<URI, OWLClassExpression>();


    public OWLClassExpression translateClassExpression(URI mainNode) {
        if (!isAnonymousNode(mainNode)) {
            return getDataFactory().getOWLClass(mainNode);
        }
        OWLClassExpression desc = translatedClassExpression.get(mainNode);
        if (desc == null) {
            ClassExpressionTranslator translator = classExpressionTranslatorSelector.getClassExpressionTranslator(mainNode);
            if (translator != null) {
                desc = translator.translate(mainNode);
                translatedClassExpression.put(mainNode, desc);
                restrictionURIs.remove(mainNode);
            }
            else {
                return getDataFactory().getOWLClass(mainNode);
            }
        }
        return desc;
    }


    public OWLClassExpression getClassExpressionIfTranslated(URI mainNode) {
        return translatedClassExpression.get(mainNode);
    }


    public List<OWLObjectPropertyExpression> translateToObjectPropertyList(URI mainNode) {
        return objectPropertyListTranslator.translateList(mainNode);
    }

    public List<OWLDataPropertyExpression> translateToDataPropertyList(URI mainNode) {
        return dataPropertyListTranslator.translateList(mainNode);
    }

    public Set<OWLClassExpression> translateToClassExpressionSet(URI mainNode) {
        return classExpressionListTranslator.translateToSet(mainNode);
    }


    public Set<OWLLiteral> translateToConstantSet(URI mainNode) {
        return constantListTranslator.translateToSet(mainNode);
    }


    public Set<OWLIndividual> translateToIndividualSet(URI mainNode) {
        return individualListTranslator.translateToSet(mainNode);
    }

    public Set<OWLDataRange> translateToDataRangeSet(URI mainNode) {
        return dataRangeListTranslator.translateToSet(mainNode);
    }

    public Set<OWLFacetRestriction> translateToFacetRestrictionSet(URI mainNode) {
        return faceRestrictionListTranslator.translateToSet(mainNode);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Set<URI> getPredicatesBySubject(URI subject) {
        Set<URI> uris = new HashSet<URI>();
        Map<URI, Set<URI>> predObjMap = resTriplesBySubject.get(subject);
        if (predObjMap != null) {
            uris.addAll(predObjMap.keySet());
        }
        Map<URI, Set<OWLLiteral>> predObjMapLit = litTriplesBySubject.get(subject);
        if (predObjMapLit != null) {
            uris.addAll(predObjMapLit.keySet());
        }
        return uris;
    }

    public URI getResourceObject(URI subject, URI predicate, boolean consume) {
        Map<URI, URI> subjPredMap = singleValuedResTriplesByPredicate.get(predicate);
        if (subjPredMap != null) {
            URI obj = subjPredMap.get(subject);
            if (consume) {
                subjPredMap.remove(subject);
            }
            return obj;
        }
        Map<URI, Set<URI>> predObjMap = resTriplesBySubject.get(subject);
        if (predObjMap != null) {
            Set<URI> objects = predObjMap.get(predicate);
            if (objects != null) {
                if (!objects.isEmpty()) {
                    URI object = objects.iterator().next();
                    if (consume) {
                        objects.remove(object);
                    }
                    if (objects.isEmpty()) {
                        predObjMap.remove(predicate);
                        if (predObjMap.isEmpty()) {
                            resTriplesBySubject.remove(subject);
                        }
                    }
                    return object;
                }
            }
        }
        return null;
    }


    public OWLLiteral getLiteralObject(URI subject, URI predicate, boolean consume) {
        Map<URI, OWLLiteral> subjPredMap = singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap != null) {
            OWLLiteral obj = subjPredMap.get(subject);
            if (consume) {
                subjPredMap.remove(subject);
            }
            return obj;
        }
        Map<URI, Set<OWLLiteral>> predObjMap = litTriplesBySubject.get(subject);
        if (predObjMap != null) {
            Set<OWLLiteral> objects = predObjMap.get(predicate);
            if (objects != null) {
                if (!objects.isEmpty()) {
                    OWLLiteral object = objects.iterator().next();
                    if (consume) {
                        objects.remove(object);
                    }
                    if (objects.isEmpty()) {
                        predObjMap.remove(predicate);
                    }
                    return object;
                }
            }
        }
        return null;
    }


    public boolean isTriplePresent(URI subject, URI predicate, URI object, boolean consume) {
        Map<URI, URI> subjPredMap = singleValuedResTriplesByPredicate.get(predicate);
        if (subjPredMap != null) {
            URI obj = subjPredMap.get(subject);
            if (consume) {
                subjPredMap.remove(subject);
            }
            return obj != null;
        }
        Map<URI, Set<URI>> predObjMap = resTriplesBySubject.get(subject);
        if (predObjMap != null) {
            Set<URI> objects = predObjMap.get(predicate);
            if (objects != null) {
                if (objects.contains(object)) {
                    if (consume) {
                        objects.remove(object);
                        if (objects.isEmpty()) {
                            predObjMap.remove(predicate);
                            if (predObjMap.isEmpty()) {
                                resTriplesBySubject.remove(subject);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }


    public boolean isTriplePresent(URI subject, URI predicate, OWLLiteral object, boolean consume) {
        Map<URI, OWLLiteral> subjPredMap = singleValuedLitTriplesByPredicate.get(predicate);
        if (subjPredMap != null) {
            OWLLiteral obj = subjPredMap.get(subject);
            if (consume) {
                subjPredMap.remove(subject);
            }
            return obj != null;
        }
        Map<URI, Set<OWLLiteral>> predObjMap = litTriplesBySubject.get(subject);
        if (predObjMap != null) {
            Set<OWLLiteral> objects = predObjMap.get(predicate);
            if (objects != null) {
                if (objects.contains(object)) {
                    if (consume) {
                        objects.remove(object);
                        if (objects.isEmpty()) {
                            predObjMap.remove(predicate);
                            if (predObjMap.isEmpty()) {
                                litTriplesBySubject.remove(subject);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }


    public boolean hasPredicate(URI subject, URI predicate) {
        Map<URI, URI> resPredMap = singleValuedResTriplesByPredicate.get(predicate);
        if (resPredMap != null) {
            return resPredMap.containsKey(subject);
        }
        Map<URI, OWLLiteral> litPredMap = singleValuedLitTriplesByPredicate.get(predicate);
        if (litPredMap != null) {
            return litPredMap.containsKey(subject);
        }
        Map<URI, Set<URI>> resPredObjMap = resTriplesBySubject.get(subject);
        if (resPredObjMap != null) {
            boolean b = resPredObjMap.containsKey(predicate);
            if (b) {
                return true;
            }
        }
        Map<URI, Set<OWLLiteral>> litPredObjMap = litTriplesBySubject.get(subject);
        if (litPredObjMap != null) {
            return litPredObjMap.containsKey(predicate);
        }
        return false;
    }


    public boolean hasPredicateObject(URI subject, URI predicate, URI object) {
        Map<URI, URI> predMap = singleValuedResTriplesByPredicate.get(predicate);
        if (predMap != null) {
            URI objectURI = predMap.get(subject);
            if (objectURI == null) {
                return false;
            }
            return objectURI.equals(object);
        }
        Map<URI, Set<URI>> predObjMap = resTriplesBySubject.get(subject);
        if (predObjMap != null) {
            Set<URI> objects = predObjMap.get(predicate);
            if (objects != null) {
                return objects.contains(object);
            }
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    public void addList(URI uri) {
        listURIs.add(uri);
    }


    public boolean isList(URI uri, boolean consume) {
        if (consume) {
            return listURIs.remove(uri);
        }
        else {
            return listURIs.contains(uri);
        }
    }


    public void addRest(URI subject, URI object) {
        listRestTripleMap.put(subject, object);
    }


    public void addFirst(URI subject, URI object) {
        listFirstResourceTripleMap.put(subject, object);
    }


    public URI getFirstResource(URI subject, boolean consume) {
        if (consume) {
            return listFirstResourceTripleMap.remove(subject);
        }
        else {
            return listFirstResourceTripleMap.get(subject);
        }
    }


    public OWLLiteral getFirstLiteral(URI subject) {
        return listFirstLiteralTripleMap.get(subject);
    }


    public URI getRest(URI subject, boolean consume) {
        if (consume) {
            return listRestTripleMap.remove(subject);
        }
        else {
            return listRestTripleMap.get(subject);
        }
    }


    public void addFirst(URI subject, OWLLiteral object) {
        listFirstLiteralTripleMap.put(subject, object);
    }


    public void addOntology(URI uri) {
        if (ontologyURIs.isEmpty()) {
            firstOntologyURI = uri;
        }
        ontologyURIs.add(uri);
    }


    public void addReifiedAxiom(URI axiomURI, OWLAxiom axiom) {
        reifiedAxiomsMap.put(axiomURI, axiom);
    }


    public boolean isAxiom(URI uri) {
        return reifiedAxiomsMap.containsKey(uri);
    }


    public OWLAxiom getAxiom(URI uri) {
        return reifiedAxiomsMap.get(uri);
    }


    public boolean isDataRange(URI uri) {
        return dataRangeURIs.contains(uri);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////  Triple Stuff
    //////
    //////
    //////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
        Originally we had a special Triple class, which was specialised into ResourceTriple and
        LiteralTriple - this was used to store triples.  However, with very large ontologies this
        proved to be inefficient in terms of memory usage.  Now we just store raw subjects, predicates and
        object directly in varous maps.
    */

    // Resource triples

    // Subject, predicate, object

    private Map<URI, Map<URI, Set<URI>>> resTriplesBySubject = CollectionFactory.createMap();

    // Predicate, subject, object
    private Map<URI, Map<URI, URI>> singleValuedResTriplesByPredicate = CollectionFactory.createMap();

    // Literal triples
    private Map<URI, Map<URI, Set<OWLLiteral>>> litTriplesBySubject = CollectionFactory.createMap();

    // Predicate, subject, object
    private Map<URI, Map<URI, OWLLiteral>> singleValuedLitTriplesByPredicate = CollectionFactory.createMap();


    public void addTriple(URI subject, URI predicate, URI object) {
        Map<URI, URI> subjObjMap = singleValuedResTriplesByPredicate.get(predicate);
        if (subjObjMap != null) {
            subjObjMap.put(subject, object);
        }
        else {
            Map<URI, Set<URI>> map = resTriplesBySubject.get(subject);
            if (map == null) {
                map = CollectionFactory.createMap();
                resTriplesBySubject.put(subject, map);
            }
            Set<URI> objects = map.get(predicate);
            if (objects == null) {
                objects = new FakeSet();
                map.put(predicate, objects);
            }
            objects.add(object);
        }
    }


    public void addTriple(URI subject, URI predicate, OWLLiteral con) {
        Map<URI, OWLLiteral> subjObjMap = singleValuedLitTriplesByPredicate.get(predicate);
        if (subjObjMap != null) {
            subjObjMap.put(subject, con);
        }
        else {
            Map<URI, Set<OWLLiteral>> map = litTriplesBySubject.get(subject);
            if (map == null) {
                map = CollectionFactory.createMap();
                litTriplesBySubject.put(subject, map);
            }
            Set<OWLLiteral> objects = map.get(predicate);
            if (objects == null) {
                objects = new FakeSet();
                map.put(predicate, objects);
            }
            objects.add(con);
        }
    }


    public void setXMLBase(String base) {
        this.xmlBase = URI.create(base);
    }


    private static class FakeSet<O> extends ArrayList<O> implements Set<O> {

        public FakeSet() {
        }


        public FakeSet(Collection<? extends O> c) {
            super(c);
        }
    }
}
