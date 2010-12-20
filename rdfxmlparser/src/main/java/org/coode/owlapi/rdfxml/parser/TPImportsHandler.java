package org.coode.owlapi.rdfxml.parser;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.io.RDFOntologyFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 10-Dec-2006<br><br>
 */
public class TPImportsHandler extends TriplePredicateHandler {

    private Set<IRI> schemaImportsIRIs;


    public TPImportsHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_IMPORTS.getIRI());
        schemaImportsIRIs = new HashSet<IRI>();
        for (Namespaces n : Namespaces.values()) {
            String ns = n.toString();
            schemaImportsIRIs.add(IRI.create(ns.substring(0, ns.length() - 1)));
        }
        schemaImportsIRIs.add(IRI.create("http://www.daml.org/rules/proposal/swrlb.owlapi"));
        schemaImportsIRIs.add(IRI.create("http://www.daml.org/rules/proposal/swrl.owlapi"));
    }


    @Override
	public boolean canHandleStreaming(IRI subject, IRI predicate, IRI object) {
        return true;
    }

    @Override
	public void handleTriple(IRI subject, IRI predicate, IRI object) throws UnloadableImportException {

        // NOTE:
        // For backwards compatibility with OWL 1 DL, if G contains an owl:imports triple pointing to an RDF
        // document encoding an RDF graph G' where G' does not have an ontology header, this owl:imports triple
        // is interpreted as an include rather than an import — that is, the triples of G' are included into G and are
        // not parsed into a separate ontology.
        // WHAT A LOAD OF RUBBISH!!


        consumeTriple(subject, predicate, object);
        getConsumer().addOntology(subject);
        getConsumer().addOntology(object);
        if (!schemaImportsIRIs.contains(object)) {
            OWLImportsDeclaration importsDeclaration = getDataFactory().getOWLImportsDeclaration(object);
            getConsumer().addImport(importsDeclaration);
            OWLOntologyManager man = getConsumer().getOWLOntologyManager();
            man.makeLoadImportRequest(importsDeclaration, getConsumer().getConfiguration());


            OWLOntology importedOntology = man.getImportedOntology(importsDeclaration);
            if (importedOntology != null) {
                OWLOntologyFormat importedOntologyFormat = man.getOntologyFormat(importedOntology);
                if (importedOntologyFormat instanceof RDFOntologyFormat) {
                    if (importedOntology.isAnonymous()) {
                        OWLOntologyLoaderConfiguration.MissingOntologyHeaderStrategy missingOntologyHeaderStrategy = getConsumer().getConfiguration().getMissingOntologyHeaderStrategy();
                        boolean includeGraph = missingOntologyHeaderStrategy.equals(OWLOntologyLoaderConfiguration.MissingOntologyHeaderStrategy.INCLUDE_GRAPH);

                        if (includeGraph) {
                            // We should have just included the triples rather than imported them. So,
                            // we remove the imports statement, add the axioms from the imported ontology to
                            // out importing ontology and remove the imported ontology.
                            // WHO EVER THOUGHT THAT THIS WAS A GOOD IDEA?
                            man.applyChange(new RemoveImport(getConsumer().getOntology(), importsDeclaration));

                            for (OWLImportsDeclaration decl : importedOntology.getImportsDeclarations()) {
                                man.applyChange(new AddImport(getConsumer().getOntology(), decl));
                            }
                            for (OWLAnnotation anno : importedOntology.getAnnotations()) {
                                man.applyChange(new AddOntologyAnnotation(getConsumer().getOntology(), anno));
                            }
                            for (OWLAxiom ax : importedOntology.getAxioms()) {
                                getConsumer().addAxiom(ax);
                            }
                            man.removeOntology(importedOntology);
                        }

                    }
                }
            }

            getConsumer().importsClosureChanged();

        }

    }
}
