package org.semanticweb.owlapi.rio.model.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLStorerFactory;
import org.semanticweb.owlapi.util.PriorityCollection;

/**
 * @author Peter Ansell p_ansell@yahoo.com
 */
@SuppressWarnings("javadoc")
public class OWLOntologyStorerFactoryRegistryTest {

    private static final int EXPECTED_STORERS = 19;

    @Test
    public void setUp() {
        PriorityCollection<OWLStorerFactory> ontologyStorers = OWLManager
                .createOWLOntologyManager().getOntologyStorers();
        assertEquals(EXPECTED_STORERS, ontologyStorers.size());
    }
}
