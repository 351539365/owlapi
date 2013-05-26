package org.semanticweb.owlapi.contract;

import static org.mockito.Mockito.mock;

import java.util.Set;

import org.coode.owlapi.rdfxml.parser.*;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.api.test.Factory;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.RDFOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import org.xml.sax.SAXException;

@SuppressWarnings({ "unused", "javadoc", "unchecked" })
public class ContractRdfxmlParserTest {
    @Test
    public void shouldTestAbstractClassExpressionTranslator() throws Exception {
        AbstractClassExpressionTranslator testSubject0 = new AbstractClassExpressionTranslator(
                Utils.mockOWLRDFConsumer()) {
            @Override
            public boolean matchesStrict(IRI mainNode) {
                return false;
            }

            @Override
            public boolean matchesLax(IRI mainNode) {
                return false;
            }

            @Override
            public OWLClassExpression translate(IRI mainNode) {
                return Factory.getFactory().getOWLClass(mainNode);
            }

        };
        boolean result0 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result1 = testSubject0.getConsumer();
        result1.addTriple(Utils.fakeiri1,
                OWLRDFVocabulary.OWL_ON_PROPERTY.getIRI(),
                Utils.fakeiri2);
        String result2 = testSubject0.toString();

        boolean result4 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result5 = testSubject0.matchesStrict(Utils.fakeiri1);
    }

    @Test
    public void shouldTestAbstractLiteralTripleHandler() throws Exception {
        AbstractLiteralTripleHandler testSubject0 = new AbstractLiteralTripleHandler(
                Utils.mockOWLRDFConsumer()) {
            @Override
            public void handleTriple(IRI subject, IRI predicate, OWLLiteral object) {}

            @Override
            public boolean canHandle(IRI subject, IRI predicate, OWLLiteral object) {
                return false;
            }

            @Override
            public boolean canHandleStreaming(IRI subject, IRI predicate,
                    OWLLiteral object) {
                return false;
            }
        };
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiriproperty,
                mock(OWLLiteral.class));
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestAbstractNamedEquivalentClassAxiomHandler() throws Exception {
        AbstractNamedEquivalentClassAxiomHandler testSubject0 = new AbstractNamedEquivalentClassAxiomHandler(
                Utils.mockOWLRDFConsumer(), Utils.fakeiri2) {
            @Override
            protected OWLClassExpression translateEquivalentClass(IRI mainNode) {
                return Factory.getFactory().getOWLClass(mainNode);
            }
        };
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiriproperty, Utils.fakeiri2);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiriproperty,
                Utils.fakeiri2);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1,
                Utils.fakeiriproperty, Utils.fakeiri2);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri2);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestAbstractResourceTripleHandler() throws Exception {
        AbstractResourceTripleHandler testSubject0 = new AbstractResourceTripleHandler(
                Utils.mockOWLRDFConsumer()) {
            @Override
            public void handleTriple(IRI subject, IRI predicate, IRI object)
                    throws UnloadableImportException {}

            @Override
            public boolean canHandleStreaming(IRI subject, IRI predicate, IRI object)
                    throws UnloadableImportException {
                return false;
            }

            @Override
            public boolean canHandle(IRI subject, IRI predicate, IRI object) {
                return false;
            }
        };
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestAbstractTripleHandler() throws Exception {
        AbstractTripleHandler testSubject0 = new AbstractTripleHandler(
                Utils.mockOWLRDFConsumer());
        OWLRDFConsumer result0 = testSubject0.getConsumer();
        String result1 = testSubject0.toString();
    }

    @Test
    public void shouldTestInterfaceAnonymousNodeChecker() throws Exception {
        AnonymousNodeChecker testSubject0 = mock(AnonymousNodeChecker.class);
        boolean result0 = testSubject0.isAnonymousNode(Utils.fakeiri1);
        boolean result1 = testSubject0.isAnonymousNode("");
        boolean result2 = testSubject0.isAnonymousSharedNode("");
    }

    @Test
    public void shouldTestBuiltInTypeHandler() throws Exception {
        BuiltInTypeHandler testSubject0 = new BuiltInTypeHandler(
                Utils.mockOWLRDFConsumer(), Utils.fakeiri1) {
            @Override
            public void handleTriple(IRI subject, IRI predicate, IRI object)
                    throws UnloadableImportException {}
        };
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestClassExpressionListItemTranslator() throws Exception {
        ClassExpressionListItemTranslator testSubject0 = new ClassExpressionListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Ignore
    @Test
    public void shouldTestInterfaceClassExpressionTranslator() throws Exception {
        // only here to guarantee the interface does not change
        ClassExpressionTranslator testSubject0 = mock(ClassExpressionTranslator.class);
        boolean result0 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
    }

    @Test
    public void shouldTestDataAllValuesFromTranslator() throws Exception {
        OWLRDFConsumer c = Utils.mockOWLRDFConsumer();

        DataAllValuesFromTranslator testSubject0 = new DataAllValuesFromTranslator(
c);

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataCardinalityTranslator() throws Exception {
        DataCardinalityTranslator testSubject0 = new DataCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataHasValueTranslator() throws Exception {
        DataHasValueTranslator testSubject0 = new DataHasValueTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataMaxCardinalityTranslator() throws Exception {
        DataMaxCardinalityTranslator testSubject0 = new DataMaxCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataMaxQualifiedCardinalityTranslator() throws Exception {
        DataMaxQualifiedCardinalityTranslator testSubject0 = new DataMaxQualifiedCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataMinCardinalityTranslator() throws Exception {
        DataMinCardinalityTranslator testSubject0 = new DataMinCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataMinQualifiedCardinalityTranslator() throws Exception {
        DataMinQualifiedCardinalityTranslator testSubject0 = new DataMinQualifiedCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataPropertyListItemTranslator() throws Exception {
        DataPropertyListItemTranslator testSubject0 = new DataPropertyListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataQualifiedCardinalityTranslator() throws Exception {
        DataQualifiedCardinalityTranslator testSubject0 = new DataQualifiedCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataRangeListItemTranslator() throws Exception {
        DataRangeListItemTranslator testSubject0 = new DataRangeListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestDataSomeValuesFromTranslator() throws Exception {
        OWLRDFConsumer mockOWLRDFConsumer = Utils.mockOWLRDFConsumer();
        mockOWLRDFConsumer.addTriple(Utils.fakeiri1,
                OWLRDFVocabulary.OWL_ON_PROPERTY.getIRI(),
                OWLRDFVocabulary.OWL_ON_PROPERTY.getIRI());
        DataSomeValuesFromTranslator testSubject0 = new DataSomeValuesFromTranslator(
                mockOWLRDFConsumer);

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestGTPAnnotationLiteralHandler() throws Exception {
        GTPAnnotationLiteralHandler testSubject0 = new GTPAnnotationLiteralHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestGTPAnnotationResourceTripleHandler() throws Exception {
        GTPAnnotationResourceTripleHandler testSubject0 = new GTPAnnotationResourceTripleHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestGTPDataPropertyAssertionHandler() throws Exception {
        GTPDataPropertyAssertionHandler testSubject0 = new GTPDataPropertyAssertionHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestGTPLiteralTripleHandler() throws Exception {
        GTPLiteralTripleHandler testSubject0 = new GTPLiteralTripleHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestGTPObjectPropertyAssertionHandler() throws Exception {
        GTPObjectPropertyAssertionHandler testSubject0 = new GTPObjectPropertyAssertionHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestGTPResourceTripleHandler() throws Exception {
        GTPResourceTripleHandler testSubject0 = new GTPResourceTripleHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestHasKeyListItemTranslator() throws Exception {
        HasKeyListItemTranslator testSubject0 = new HasKeyListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestIndividualListItemTranslator() throws Exception {
        IndividualListItemTranslator testSubject0 = new IndividualListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestInterfaceIRIProvider() throws Exception {
        IRIProvider testSubject0 = mock(IRIProvider.class);
        IRI result0 = testSubject0.getIRI("");
    }

    @Test
    public void shouldTestInterfaceListItemTranslator() throws Exception {
        ListItemTranslator<OWLObject> testSubject0 = mock(ListItemTranslator.class);

    }

    @Test
    public void shouldTestNamedClassTranslator() throws Exception {
        NamedClassTranslator testSubject0 = new NamedClassTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectAllValuesFromTranslator() throws Exception {
        ObjectAllValuesFromTranslator testSubject0 = new ObjectAllValuesFromTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectCardinalityTranslator() throws Exception {
        ObjectCardinalityTranslator testSubject0 = new ObjectCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectComplementOfTranslator() throws Exception {
        ObjectComplementOfTranslator testSubject0 = new ObjectComplementOfTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectHasSelfTranslator() throws Exception {
        ObjectHasSelfTranslator testSubject0 = new ObjectHasSelfTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectHasValueTranslator() throws Exception {
        ObjectHasValueTranslator testSubject0 = new ObjectHasValueTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectMaxCardinalityTranslator() throws Exception {
        ObjectMaxCardinalityTranslator testSubject0 = new ObjectMaxCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectMaxQualifiedCardinalityTranslator() throws Exception {
        ObjectMaxQualifiedCardinalityTranslator testSubject0 = new ObjectMaxQualifiedCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectMinCardinalityTranslator() throws Exception {
        ObjectMinCardinalityTranslator testSubject0 = new ObjectMinCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectMinQualifiedCardinalityTranslator() throws Exception {
        ObjectMinQualifiedCardinalityTranslator testSubject0 = new ObjectMinQualifiedCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectPropertyListItemTranslator() throws Exception {
        ObjectPropertyListItemTranslator testSubject0 = new ObjectPropertyListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectQualifiedCardinalityTranslator() throws Exception {
        ObjectQualifiedCardinalityTranslator testSubject0 = new ObjectQualifiedCardinalityTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestObjectSomeValuesFromTranslator() throws Exception {
        ObjectSomeValuesFromTranslator testSubject0 = new ObjectSomeValuesFromTranslator(
                Utils.mockOWLRDFConsumer());

        boolean result2 = testSubject0.matchesLax(Utils.fakeiri1);
        boolean result3 = testSubject0.matchesStrict(Utils.fakeiri1);
        boolean result4 = testSubject0.matches(Utils.fakeiri1, Mode.LAX);
        OWLRDFConsumer result5 = testSubject0.getConsumer();
        String result6 = testSubject0.toString();
    }

    @Test
    public void shouldTestOptimisedListTranslator() throws Exception {
        OptimisedListTranslator<OWLObject> testSubject0 = new OptimisedListTranslator<OWLObject>(
                Utils.mockOWLRDFConsumer(), mock(ListItemTranslator.class)) {};

        String result2 = testSubject0.toString();
    }

    @Test
    public void shouldTestOWLFacetRestrictionListItemTranslator() throws Exception {
        OWLFacetRestrictionListItemTranslator testSubject0 = new OWLFacetRestrictionListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestOWLObjectPropertyExpressionListItemTranslator()
            throws Exception {
        OWLObjectPropertyExpressionListItemTranslator testSubject0 = new OWLObjectPropertyExpressionListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestOWLRDFConsumer() throws Exception {
        OWLRDFConsumer testSubject0 = new OWLRDFConsumer(Utils.getMockOntology(),
                mock(AnonymousNodeChecker.class), new OWLOntologyLoaderConfiguration());
        testSubject0.handle(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        testSubject0.handle(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        testSubject0.addFirst(Utils.fakeiri1, Utils.fakeiri1);
        testSubject0.addFirst(Utils.fakeiri1, mock(OWLLiteral.class));
        OWLOntologyManager result0 = testSubject0.getOWLOntologyManager();
        Set<IRI> result1 = testSubject0.getOntologies();
        OWLOntology result2 = testSubject0.getOntology();
        testSubject0.addAxiom(Utils.fakeiri1);
        RDFOntologyFormat result3 = testSubject0.getOntologyFormat();
        testSubject0.setOntologyFormat(mock(RDFOntologyFormat.class));
        OWLOntologyLoaderConfiguration result4 = testSubject0.getConfiguration();
        OWLDataFactory result5 = testSubject0.getDataFactory();
        testSubject0.addOntology(Utils.fakeiri1);
        testSubject0.addTriple(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        testSubject0.addTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);

        IRI result7 = testSubject0.getResourceObject(Utils.fakeiri1, Utils.fakeiri1,
                false);
        IRI result8 = testSubject0.getResourceObject(Utils.fakeiri1,
                OWLRDFVocabulary.OWL_ALL_DIFFERENT, false);
        OWLLiteral result9 = testSubject0.getLiteralObject(Utils.fakeiri1,
                OWLRDFVocabulary.OWL_ALL_DIFFERENT, false);
        OWLLiteral result10 = testSubject0.getLiteralObject(Utils.fakeiri1,
                Utils.fakeiri1, false);
        boolean result11 = testSubject0.isRestriction(Utils.fakeiri1);
        boolean result12 = testSubject0.isClassExpression(Utils.fakeiri1);
        boolean result13 = testSubject0.isDataRange(Utils.fakeiri1);
        boolean result14 = testSubject0.isParsedAllTriples();
        testSubject0.importsClosureChanged();
        testSubject0.setIRIProvider(mock(IRIProvider.class));
        testSubject0.setExpectedAxioms(0);
        Set<OWLAnnotation> result15 = testSubject0.getPendingAnnotations();
        testSubject0.setPendingAnnotations(Utils.mockSet(mock(OWLAnnotation.class)));
        OWLAxiom result16 = testSubject0.getLastAddedAxiom();
        testSubject0.addClassExpression(Utils.fakeiri1, false);
        testSubject0.addObjectProperty(Utils.fakeiri1, false);
        testSubject0.addDataProperty(Utils.fakeiri1, false);
        testSubject0.addDataRange(Utils.fakeiri1, false);
        testSubject0.addAnnotatedSource(Utils.fakeiri1, Utils.fakeiri1);
        Set<IRI> result17 = testSubject0.getAnnotatedSourceAnnotationMainNodes(IRI
                .create("urn:aFake"));
        boolean result18 = testSubject0.isTriplePresent(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class), false);
        boolean result19 = testSubject0.isTriplePresent(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1, false);

        testSubject0.startModel("");
        testSubject0.endModel();
        testSubject0.addModelAttribte("", "");
        testSubject0.includeModel("", "");
        testSubject0.logicalURI("");
        IRI result22 = testSubject0.getSynonym(Utils.fakeiri1);
        testSubject0.statementWithLiteralValue("", "", "", "", "");
        testSubject0.statementWithResourceValue("", "", "");

        Set<IRI> result29 = testSubject0.getPredicatesBySubject(Utils.fakeiri1);
        Set<IRI> result30 = testSubject0.getResourceObjects(Utils.fakeiri1,
                Utils.fakeiri1);
        Set<OWLLiteral> result31 = testSubject0.getLiteralObjects(
Utils.fakeiri1,
                Utils.fakeiri1);

        boolean result37 = testSubject0.hasPredicate(Utils.fakeiri1, Utils.fakeiri1);
        testSubject0.addRest(Utils.fakeiri1, Utils.fakeiri1);
        IRI result38 = testSubject0.getFirstResource(Utils.fakeiri1, false);
        OWLLiteral result39 = testSubject0.getFirstLiteral(Utils.fakeiri1);
        IRI result40 = testSubject0.getRest(Utils.fakeiri1, false);
        boolean result41 = testSubject0.isAxiom(Utils.fakeiri1);
        String result42 = testSubject0.toString();
    }

    @Test
    public void shouldTestOWLRDFParserException() throws Exception {
        OWLRDFParserException testSubject0 = new OWLRDFParserException();
        OWLRDFParserException testSubject1 = new OWLRDFParserException("");
        OWLRDFParserException testSubject2 = new OWLRDFParserException("",
                new RuntimeException());
        OWLRDFParserException testSubject3 = new OWLRDFParserException(
                new RuntimeException());
        String result0 = testSubject0.getMessage();
        int result1 = testSubject0.getLineNumber();
        int result2 = testSubject0.getColumnNumber();
        Throwable result4 = testSubject0.getCause();
        String result6 = testSubject0.toString();
        String result7 = testSubject0.getLocalizedMessage();
    }

    @Test
    public void shouldTestOWLRDFXMLParserException() throws Exception {
        OWLRDFXMLParserException testSubject0 = new OWLRDFXMLParserException("");
        OWLRDFXMLParserException testSubject1 = new OWLRDFXMLParserException("",
                new RuntimeException());
        OWLRDFXMLParserException testSubject2 = new OWLRDFXMLParserException(
                new RuntimeException());
        String result0 = testSubject0.getMessage();
        int result1 = testSubject0.getLineNumber();
        int result2 = testSubject0.getColumnNumber();
        Throwable result4 = testSubject0.getCause();
        String result6 = testSubject0.toString();
        String result7 = testSubject0.getLocalizedMessage();
    }

    @Test
    public void shouldTestOWLRDFXMLParserMalformedNodeException() throws Exception {
        OWLRDFXMLParserMalformedNodeException testSubject0 = new OWLRDFXMLParserMalformedNodeException(
                new RuntimeException());
        OWLRDFXMLParserMalformedNodeException testSubject1 = new OWLRDFXMLParserMalformedNodeException(
                "", new RuntimeException());
        OWLRDFXMLParserMalformedNodeException testSubject2 = new OWLRDFXMLParserMalformedNodeException(
                "");
        String result0 = testSubject0.getMessage();
        int result1 = testSubject0.getLineNumber();
        int result2 = testSubject0.getColumnNumber();
        Throwable result4 = testSubject0.getCause();
        String result6 = testSubject0.toString();
        String result7 = testSubject0.getLocalizedMessage();
    }

    @Test
    public void shouldTestOWLRDFXMLParserSAXException() throws Exception {
        OWLRDFXMLParserSAXException testSubject0 = new OWLRDFXMLParserSAXException(
                mock(SAXException.class));
        String result0 = testSubject0.getMessage();
        int result1 = testSubject0.getLineNumber();
        int result2 = testSubject0.getColumnNumber();
        Throwable result4 = testSubject0.getCause();
        String result6 = testSubject0.toString();
        String result7 = testSubject0.getLocalizedMessage();
    }

    @Test
    public void shouldTestRDFXMLParserFactory() throws Exception {
        RDFXMLParserFactory testSubject0 = new RDFXMLParserFactory();
        OWLParser result0 = testSubject0.createParser(Utils.getMockManager());
        String result1 = testSubject0.toString();
    }

    @Test
    public void shouldTestSKOSClassTripleHandler() throws Exception {
        SKOSClassTripleHandler testSubject0 = new SKOSClassTripleHandler(
                Utils.mockOWLRDFConsumer(), SKOSVocabulary.ALTLABEL);
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestSWRLRuleTranslator() throws Exception {
        SWRLRuleTranslator testSubject0 = new SWRLRuleTranslator(
                Utils.mockOWLRDFConsumer());

        String result0 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPAllValuesFromHandler() throws Exception {
        TPAllValuesFromHandler testSubject0 = new TPAllValuesFromHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPAnnotatedPropertyHandler() throws Exception {
        TPAnnotatedPropertyHandler testSubject0 = new TPAnnotatedPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPAnnotatedSourceHandler() throws Exception {
        TPAnnotatedSourceHandler testSubject0 = new TPAnnotatedSourceHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPAnnotatedTargetHandler() throws Exception {
        TPAnnotatedTargetHandler testSubject0 = new TPAnnotatedTargetHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPComplementOfHandler() throws Exception {
        TPComplementOfHandler testSubject0 = new TPComplementOfHandler(
                Utils.mockOWLRDFConsumer());
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPDatatypeComplementOfHandler() throws Exception {
        TPDatatypeComplementOfHandler testSubject0 = new TPDatatypeComplementOfHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPDeclaredAsHandler() throws Exception {
        TPDeclaredAsHandler testSubject0 = new TPDeclaredAsHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPDifferentFromHandler() throws Exception {
        TPDifferentFromHandler testSubject0 = new TPDifferentFromHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPDisjointUnionHandler() throws Exception {
        TPDisjointUnionHandler testSubject0 = new TPDisjointUnionHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPDisjointWithHandler() throws Exception {
        TPDisjointWithHandler testSubject0 = new TPDisjointWithHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPDistinctMembersHandler() throws Exception {
        TPDistinctMembersHandler testSubject0 = new TPDistinctMembersHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPEquivalentClassHandler() throws Exception {
        TPEquivalentClassHandler testSubject0 = new TPEquivalentClassHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPEquivalentPropertyHandler() throws Exception {
        TPEquivalentPropertyHandler testSubject0 = new TPEquivalentPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPFirstLiteralHandler() throws Exception {
        TPFirstLiteralHandler testSubject0 = new TPFirstLiteralHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                mock(OWLLiteral.class));
        OWLRDFConsumer result2 = testSubject0.getConsumer();
        String result3 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPFirstResourceHandler() throws Exception {
        TPFirstResourceHandler testSubject0 = new TPFirstResourceHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPHasKeyHandler() throws Exception {
        TPHasKeyHandler testSubject0 = new TPHasKeyHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPHasValueHandler() throws Exception {
        TPHasValueHandler testSubject0 = new TPHasValueHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPImportsHandler() throws Exception {
        TPImportsHandler testSubject0 = new TPImportsHandler(Utils.mockOWLRDFConsumer());
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPIntersectionOfHandler() throws Exception {
        TPIntersectionOfHandler testSubject0 = new TPIntersectionOfHandler(
                Utils.mockOWLRDFConsumer());
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPInverseOfHandler() throws Exception {
        TPInverseOfHandler testSubject0 = new TPInverseOfHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.setAxiomParsingMode(false);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.isAxiomParsingMode();
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPOnClassHandler() throws Exception {
        TPOnClassHandler testSubject0 = new TPOnClassHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPOnDataRangeHandler() throws Exception {
        TPOnDataRangeHandler testSubject0 = new TPOnDataRangeHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPOneOfHandler() throws Exception {
        TPOneOfHandler testSubject0 = new TPOneOfHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPOnPropertyHandler() throws Exception {
        TPOnPropertyHandler testSubject0 = new TPOnPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPPropertyChainAxiomHandler() throws Exception {
        TPPropertyChainAxiomHandler testSubject0 = new TPPropertyChainAxiomHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPPropertyDisjointWithHandler() throws Exception {
        TPPropertyDisjointWithHandler testSubject0 = new TPPropertyDisjointWithHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPPropertyDomainHandler() throws Exception {
        TPPropertyDomainHandler testSubject0 = new TPPropertyDomainHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPPropertyRangeHandler() throws Exception {
        TPPropertyRangeHandler testSubject0 = new TPPropertyRangeHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPRestHandler() throws Exception {
        TPRestHandler testSubject0 = new TPRestHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPSameAsHandler() throws Exception {
        TPSameAsHandler testSubject0 = new TPSameAsHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPSomeValuesFromHandler() throws Exception {
        TPSomeValuesFromHandler testSubject0 = new TPSomeValuesFromHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPSubClassOfHandler() throws Exception {
        TPSubClassOfHandler testSubject0 = new TPSubClassOfHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPSubPropertyOfHandler() throws Exception {
        TPSubPropertyOfHandler testSubject0 = new TPSubPropertyOfHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPTypeHandler() throws Exception {
        TPTypeHandler testSubject0 = new TPTypeHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getPredicateIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPUnionOfHandler() throws Exception {
        TPUnionOfHandler testSubject0 = new TPUnionOfHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTPVersionIRIHandler() throws Exception {
        TPVersionIRIHandler testSubject0 = new TPVersionIRIHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }


    @Test
    public void shouldTestInterfaceTriplePatternMatcher() throws Exception {
        TriplePatternMatcher testSubject0 = mock(TriplePatternMatcher.class);
        boolean result0 = testSubject0.matches(Utils.mockOWLRDFConsumer(),
 Utils.fakeiri1);
        OWLObject result1 = testSubject0.createObject(Utils.mockOWLRDFConsumer());
    }

    @Test
    public void shouldTestTriplePredicateHandler() throws Exception {
        TriplePredicateHandler testSubject0 = new TriplePredicateHandler(
                Utils.mockOWLRDFConsumer(), Utils.fakeiri1) {
            @Override
            public void handleTriple(IRI subject, IRI predicate, IRI object)
                    throws UnloadableImportException {}

            @Override
            public boolean canHandleStreaming(IRI subject, IRI predicate, IRI object)
                    throws UnloadableImportException {
                return false;
            }
        };
        IRI result0 = testSubject0.getPredicateIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result3 = testSubject0.getConsumer();
        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeAllDifferentHandler() throws Exception {
        TypeAllDifferentHandler testSubject0 = new TypeAllDifferentHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getTypeIRI();
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeAllDisjointClassesHandler() throws Exception {
        TypeAllDisjointClassesHandler testSubject0 = new TypeAllDisjointClassesHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result1 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result2 = testSubject0.getTypeIRI();
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeAnnotationHandler() throws Exception {
        TypeAnnotationHandler testSubject0 = new TypeAnnotationHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeAnnotationPropertyHandler() throws Exception {
        TypeAnnotationPropertyHandler testSubject0 = new TypeAnnotationPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeAsymmetricPropertyHandler() throws Exception {
        TypeAsymmetricPropertyHandler testSubject0 = new TypeAsymmetricPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeAxiomHandler() throws Exception {
        TypeAxiomHandler testSubject0 = new TypeAxiomHandler(Utils.mockOWLRDFConsumer());
        TypeAxiomHandler testSubject1 = new TypeAxiomHandler(Utils.mockOWLRDFConsumer(),
                Utils.fakeiri1);
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeClassHandler() throws Exception {
        TypeClassHandler testSubject0 = new TypeClassHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeDataPropertyHandler() throws Exception {
        TypeDataPropertyHandler testSubject0 = new TypeDataPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeDataRangeHandler() throws Exception {
        TypeDataRangeHandler testSubject0 = new TypeDataRangeHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeDatatypeHandler() throws Exception {
        TypeDatatypeHandler testSubject0 = new TypeDatatypeHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypedConstantListItemTranslator() throws Exception {
        TypedConstantListItemTranslator testSubject0 = new TypedConstantListItemTranslator(
                Utils.mockOWLRDFConsumer());

        String result4 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeDeprecatedClassHandler() throws Exception {
        TypeDeprecatedClassHandler testSubject0 = new TypeDeprecatedClassHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeDeprecatedPropertyHandler() throws Exception {
        TypeDeprecatedPropertyHandler testSubject0 = new TypeDeprecatedPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeFunctionalPropertyHandler() throws Exception {
        TypeFunctionalPropertyHandler testSubject0 = new TypeFunctionalPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeInverseFunctionalPropertyHandler() throws Exception {
        TypeInverseFunctionalPropertyHandler testSubject0 = new TypeInverseFunctionalPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeIrreflexivePropertyHandler() throws Exception {
        TypeIrreflexivePropertyHandler testSubject0 = new TypeIrreflexivePropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeListHandler() throws Exception {
        TypeListHandler testSubject0 = new TypeListHandler(Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeNamedIndividualHandler() throws Exception {
        TypeNamedIndividualHandler testSubject0 = new TypeNamedIndividualHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeNegativeDataPropertyAssertionHandler() throws Exception {
        TypeNegativeDataPropertyAssertionHandler testSubject0 = new TypeNegativeDataPropertyAssertionHandler(
                Utils.mockOWLRDFConsumer());
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeNegativePropertyAssertionHandler() throws Exception {
        TypeNegativePropertyAssertionHandler testSubject0 = new TypeNegativePropertyAssertionHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeObjectPropertyHandler() throws Exception {
        TypeObjectPropertyHandler testSubject0 = new TypeObjectPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeOntologyHandler() throws Exception {
        TypeOntologyHandler testSubject0 = new TypeOntologyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeOntologyPropertyHandler() throws Exception {
        TypeOntologyPropertyHandler testSubject0 = new TypeOntologyPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypePropertyHandler() throws Exception {
        TypePropertyHandler testSubject0 = new TypePropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeRDFSClassHandler() throws Exception {
        TypeRDFSClassHandler testSubject0 = new TypeRDFSClassHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeReflexivePropertyHandler() throws Exception {
        TypeReflexivePropertyHandler testSubject0 = new TypeReflexivePropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeRestrictionHandler() throws Exception {
        TypeRestrictionHandler testSubject0 = new TypeRestrictionHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSelfRestrictionHandler() throws Exception {
        TypeSelfRestrictionHandler testSubject0 = new TypeSelfRestrictionHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLAtomListHandler() throws Exception {
        TypeSWRLAtomListHandler testSubject0 = new TypeSWRLAtomListHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLBuiltInAtomHandler() throws Exception {
        TypeSWRLBuiltInAtomHandler testSubject0 = new TypeSWRLBuiltInAtomHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLBuiltInHandler() throws Exception {
        TypeSWRLBuiltInHandler testSubject0 = new TypeSWRLBuiltInHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLClassAtomHandler() throws Exception {
        TypeSWRLClassAtomHandler testSubject0 = new TypeSWRLClassAtomHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLDataRangeAtomHandler() throws Exception {
        TypeSWRLDataRangeAtomHandler testSubject0 = new TypeSWRLDataRangeAtomHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLDataValuedPropertyAtomHandler() throws Exception {
        TypeSWRLDataValuedPropertyAtomHandler testSubject0 = new TypeSWRLDataValuedPropertyAtomHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLDifferentIndividualsAtomHandler() throws Exception {
        TypeSWRLDifferentIndividualsAtomHandler testSubject0 = new TypeSWRLDifferentIndividualsAtomHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLImpHandler() throws Exception {
        TypeSWRLImpHandler testSubject0 = new TypeSWRLImpHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLIndividualPropertyAtomHandler() throws Exception {
        TypeSWRLIndividualPropertyAtomHandler testSubject0 = new TypeSWRLIndividualPropertyAtomHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLSameIndividualAtomHandler() throws Exception {
        TypeSWRLSameIndividualAtomHandler testSubject0 = new TypeSWRLSameIndividualAtomHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSWRLVariableHandler() throws Exception {
        TypeSWRLVariableHandler testSubject0 = new TypeSWRLVariableHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        IRI result0 = testSubject0.getTypeIRI();
        boolean result1 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        boolean result2 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeSymmetricPropertyHandler() throws Exception {
        TypeSymmetricPropertyHandler testSubject0 = new TypeSymmetricPropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }

    @Test
    public void shouldTestTypeTransitivePropertyHandler() throws Exception {
        TypeTransitivePropertyHandler testSubject0 = new TypeTransitivePropertyHandler(
                Utils.mockOWLRDFConsumer());
        testSubject0.handleTriple(Utils.fakeiri1, Utils.fakeiri1, Utils.fakeiri1);
        boolean result0 = testSubject0.canHandleStreaming(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result1 = testSubject0.getTypeIRI();
        boolean result2 = testSubject0.canHandle(Utils.fakeiri1, Utils.fakeiri1,
                Utils.fakeiri1);
        IRI result3 = testSubject0.getPredicateIRI();
        testSubject0.inferTypes(Utils.fakeiri1, Utils.fakeiri1);
        OWLRDFConsumer result4 = testSubject0.getConsumer();
        String result5 = testSubject0.toString();
    }
}
