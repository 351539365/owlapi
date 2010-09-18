package uk.ac.manchester.cs.owl.owlapi.alternateimpls;

/*
 * Copyright (C) 2010, University of Manchester
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

import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.owlapi.ChangeAxiomVisitor;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedObjectReferenceAdder;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedObjectReferenceAdderImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedObjectReferenceRemover;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedObjectReferenceRemoverImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

public class LockingOWLOntologyImpl extends OWLOntologyImpl {
    public LockingOWLOntologyImpl(OWLOntologyManager manager, OWLOntologyID ontologyID) {
        super(manager, ontologyID);
        this.internals = new LockingOWLOntologyInternals();
    }
    
    protected OWLAxiomVisitor getAxiomVisitor(boolean add) {
		SyncChangeAxiomVisitor toReturn =new SyncChangeAxiomVisitor(internals, add, ((LockingOWLOntologyInternals)internals).getAxiomTypeLock());
		return toReturn;
	}
    
    protected OWLNamedObjectReferenceAdder getReferenceAdder() {
    	return new SyncOWLNamedObjectReferenceAdderImpl(internals,((LockingOWLOntologyInternals)internals).getAxiomTypeLock());
    }
    
    protected OWLNamedObjectReferenceRemover getReferenceRemover() {
    	return new SyncOWLNamedObjectReferenceRemoverImpl(internals, ((LockingOWLOntologyInternals)internals).getAxiomTypeLock());
    }
}
