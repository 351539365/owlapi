package org.semanticweb.owlapi.api.test.ontology;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeDataVisitor;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;

/** @author Matthew Horridge, Stanford University, Bio-Medical Informatics
 *         Research Group, Date: 22/10/2012 */
@SuppressWarnings("javadoc")
public class OWLOntologyChangeRecordTestCase {
    private OWLOntologyID mockOntologyID;
    private OWLOntologyChangeData<OWLAxiom> mockChangeData;
    private OWLAxiom mockAxiom;

    @Before
    public void setUp() {
        mockOntologyID = new OWLOntologyID();
        mockChangeData = new OWLOntologyChangeData<OWLAxiom>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected String getName() {
                return "test";
            }

            @Override
            public OWLAxiom getItem() {
                return null;
            }

            @Override
            public OWLOntologyChange<OWLAxiom> createOntologyChange(OWLOntology ontology) {
                return null;
            }

            @Override
            public <R, E extends Exception> R accept(
                    OWLOntologyChangeDataVisitor<R, E> visitor) throws E {
                return null;
            }

            @Override
            public Set<OWLEntity> getSignature() {
                return null;
            }
        };
        mockAxiom = mock(OWLAxiom.class);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testNewWithNullOntologyID() {
        new OWLOntologyChangeRecord<OWLAxiom>(null, mockChangeData);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testNewWithNullChangeData() {
        new OWLOntologyChangeRecord<OWLAxiom>(mockOntologyID, null);
    }

    @Test
    public void testEquals() {
        OWLOntologyChangeRecord<OWLAxiom> record1 = new OWLOntologyChangeRecord<OWLAxiom>(
                mockOntologyID, mockChangeData);
        OWLOntologyChangeRecord<OWLAxiom> record2 = new OWLOntologyChangeRecord<OWLAxiom>(
                mockOntologyID, mockChangeData);
        assertEquals(record1, record2);
    }

    @Test
    public void testGettersNotNull() {
        OWLOntologyChangeRecord<OWLAxiom> record = new OWLOntologyChangeRecord<OWLAxiom>(
                mockOntologyID, mockChangeData);
        assertNotNull(record.getOntologyID());
    }

    @Test
    public void testGetterEqual() {
        OWLOntologyChangeRecord<OWLAxiom> record = new OWLOntologyChangeRecord<OWLAxiom>(
                mockOntologyID, mockChangeData);
        assertEquals(mockOntologyID, record.getOntologyID());
        assertEquals(mockChangeData, record.getData());
    }

    @Test(expected = UnknownOWLOntologyException.class)
    public void testCreateOntologyChange() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntologyChangeRecord<OWLAxiom> changeRecord = new OWLOntologyChangeRecord<OWLAxiom>(
                mockOntologyID, mockChangeData);
        changeRecord.createOntologyChange(manager);
    }

    @Test
    public void testCreateOntologyChangeEquals() throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.createOntology();
        OWLOntologyID ontologyID = ontology.getOntologyID();
        AddAxiomData addAxiomData = new AddAxiomData(mockAxiom);
        OWLOntologyChangeRecord<OWLAxiom> changeRecord = new OWLOntologyChangeRecord<OWLAxiom>(
                ontologyID, addAxiomData);
        OWLOntologyChange<OWLAxiom> change = changeRecord.createOntologyChange(manager);
        assertNotNull(change);
        assertEquals(change.getOntology().getOntologyID(), ontologyID);
        assertEquals(mockAxiom, change.getAxiom());
    }
}
