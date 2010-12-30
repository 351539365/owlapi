package org.semanticweb.owlapi.api.test.alternate;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/*
 * Copyright (C) 2007, University of Manchester
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
 * Date: 10-May-2008<br>
 * <br>
 */
public class AxiomAnnotationsRoundTrippingTestCase extends
		AbstractRoundTrippingTest {
	@Override
	protected OWLOntology createOntology() {
		OWLOntology ont = getOWLOntology("OntA");
		OWLDataFactory factory = getFactory();
		OWLAnnotationProperty prop = factory
				.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		addAxiom(ont, factory.getOWLDeclarationAxiom(prop));
		Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();
		for (int i = 0; i < 2; i++) {
			OWLLiteral lit = getFactory()
					.getOWLLiteral("Annotation " + (i + 1));
			annotations.add(getFactory().getOWLAnnotation(
					getFactory().getOWLAnnotationProperty(
							OWLRDFVocabulary.RDFS_LABEL.getIRI()), lit));
		}
		OWLEntity entity = factory.getOWLNamedIndividual(IRI
				.create("http://www.another.com/ont#peter"));
		addAxiom(ont, getFactory().getOWLDeclarationAxiom(entity));
		OWLAnnotationAssertionAxiom ax = factory
				.getOWLAnnotationAssertionAxiom(prop, entity.getIRI(),
						getFactory().getOWLLiteral("X", "en"), annotations);
		addAxiom(ont, ax);
		return ont;
	}

	@Override  @SuppressWarnings("unused")
	protected void handleSaved(StringDocumentTarget target,
			OWLOntologyFormat format) {
		System.out.println(target);
	}
}
