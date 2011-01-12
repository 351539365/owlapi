package org.semanticweb.owlapi.api.test;

import junit.framework.TestCase;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;


public class TurtleUTF8BOMTest extends TestCase{
	public void testLoadingUTF8BOM() {
		try {
		IRI uri = IRI.create(getClass().getResource("/ttl-with-bom.ttl" ).toURI());
		OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(uri);
		}catch (Exception e) {
			e.printStackTrace(System.out);
			fail(e.getMessage());
		}
	}
	
}
