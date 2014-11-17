package org.semanticweb.owlapi.api.test.swrl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asList;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics
 *         Research Group, Date: 04/04/2014
 */
@SuppressWarnings({ "javadoc", "null" })
public class SWRLAtomOrderingRoundTripTestCase {

    @Nonnull
    private final Set<SWRLAtom> body = new LinkedHashSet<>();
    @Nonnull
    private final Set<SWRLAtom> head = new LinkedHashSet<>();
    @Nonnull
    private SWRLRule rule;

    @Before
    public void setUp() {
        OWLDataFactory dataFactory = new OWLDataFactoryImpl();
        PrefixManager pm = new DefaultPrefixManager(null, null,
                "http://stuff.com/A/");
        OWLClass clsA = Class("A", pm);
        OWLClass clsB = Class("B", pm);
        OWLClass clsC = Class("C", pm);
        OWLClass clsD = Class("D", pm);
        OWLClass clsE = Class("E", pm);
        SWRLVariable varA = dataFactory
                .getSWRLVariable("http://other.com/A/VarA");
        SWRLVariable varB = dataFactory
                .getSWRLVariable("http://other.com/A/VarA");
        SWRLVariable varC = dataFactory
                .getSWRLVariable("http://other.com/A/VarA");
        body.add(dataFactory.getSWRLClassAtom(clsC, varA));
        body.add(dataFactory.getSWRLClassAtom(clsB, varB));
        body.add(dataFactory.getSWRLClassAtom(clsA, varC));
        head.add(dataFactory.getSWRLClassAtom(clsE, varA));
        head.add(dataFactory.getSWRLClassAtom(clsD, varA));
        rule = dataFactory.getSWRLRule(body, head);
    }

    @Test
    public void shouldPreserveOrderingInRDFXMLRoundTrip() throws Exception {
        roundTrip(new RDFXMLDocumentFormat());
    }

    private void roundTrip(@Nonnull OWLDocumentFormat ontologyFormat)
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = man.createOntology();
        man.addAxiom(ont, rule);
        StringDocumentTarget documentTarget = new StringDocumentTarget();
        man.saveOntology(ont, ontologyFormat, documentTarget);
        OWLOntologyManager man2 = OWLManager.createOWLOntologyManager();
        OWLOntology ont2 = man2
                .loadOntologyFromOntologyDocument(new StringDocumentSource(
                        documentTarget.toString(), "string:ontology",
                        ontologyFormat, null));
        Set<SWRLRule> rules = ont2.getAxioms(AxiomType.SWRL_RULE);
        assertThat(rules.size(), is(1));
        SWRLRule parsedRule = rules.iterator().next();
        assertThat(parsedRule, is(equalTo(rule)));
        List<SWRLAtom> originalBody = new ArrayList<>(body);
        List<SWRLAtom> parsedBody = asList(parsedRule.body());
        assertThat(parsedBody, is(equalTo(originalBody)));
        List<SWRLAtom> originalHead = new ArrayList<>(head);
        List<SWRLAtom> parsedHead = asList(parsedRule.head());
        assertThat(originalHead, is(equalTo(parsedHead)));
    }

    @Test
    public void shouldPreserveOrderingInTurtleRoundTrip()
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        roundTrip(new TurtleDocumentFormat());
    }

    @Test
    public void shouldPreserveOrderingInManchesterSyntaxRoundTrip()
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        roundTrip(new ManchesterSyntaxDocumentFormat());
    }

    @Test
    public void shouldPreserveOrderingInOWLXMLRoundTrip()
            throws OWLOntologyCreationException, OWLOntologyStorageException {
        roundTrip(new OWLXMLDocumentFormat());
    }
}
