package org.semanticweb.owlapi.api.test.alternate;

import org.semanticweb.owlapi.apibinding.configurables.ThreadSafeOWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 01-Jul-2010
 */
public class MultiImportsTestCase extends AbstractOWLAPITestCase {
	public void testImports() {
		try {
			OWLOntologyManager manager = ThreadSafeOWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI
					.create("http://purl.obolibrary.org/obo/iao.owl"));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail(e.getMessage());
			
		}
	}
}
