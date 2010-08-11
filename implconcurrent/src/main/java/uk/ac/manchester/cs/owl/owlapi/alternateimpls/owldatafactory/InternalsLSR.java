package uk.ac.manchester.cs.owl.owlapi.alternateimpls.owldatafactory;

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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryInternals;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

public class InternalsLSR implements OWLDataFactoryInternals {
    private Map<IRI, ? extends OWLEntity> classesByURI;
    private Map<IRI, ? extends OWLEntity> objectPropertiesByURI;
    private Map<IRI, ? extends OWLEntity> dataPropertiesByURI;
    private Map<IRI, ? extends OWLEntity> datatypesByURI;
    private Map<IRI, ? extends OWLEntity> individualsByURI;
    private Map<IRI, ? extends OWLEntity> annotationPropertiesByURI;
    private final OWLDataFactory factory;

    public InternalsLSR(OWLDataFactory f) {
        factory = f;
        classesByURI = new HashMap<IRI, OWLEntity>();
        objectPropertiesByURI = new HashMap<IRI, OWLEntity>();
        dataPropertiesByURI = new HashMap<IRI, OWLEntity>();
        datatypesByURI = new HashMap<IRI, OWLEntity>();
        individualsByURI = new HashMap<IRI, OWLEntity>();
        annotationPropertiesByURI = new HashMap<IRI, OWLEntity>();
    }

    private <K extends OWLEntity> OWLEntity unwrap(Map<IRI, K> map, IRI iri, BuildableObjects type) {
        K toReturn = null;
        while (toReturn == null) {
            K r = safeRead(map, iri, type);
            if (r == null) {
                toReturn = (K) type.build(factory, iri);

                safeWrite(map, iri, toReturn, type);
            }
            else {
                toReturn = r;
            }
        }
        return toReturn;
    }

    private <K> K safeRead(Map<IRI, K> map, IRI iri, BuildableObjects type) {
        final Lock lock = type.getLock().readLock();
        lock.lock();
        try {
            return map.get(iri);
        }
        finally {
            lock.unlock();
        }
    }

    private <K> void safeWrite(Map<IRI, K> map, IRI iri, K value, BuildableObjects type) {
        final Lock lock = type.getLock().writeLock();
        lock.lock();
        try {
            map.put(iri, value);
            return;
        }
        finally {
            lock.unlock();
        }
    }

    private enum BuildableObjects {
        OWLCLASS {
            @Override
            OWLEntity build(OWLDataFactory f, IRI iri) {
                return new OWLClassImpl(f, iri);
            }
        },
        OWLOBJECTPROPERTY {
            @Override
            OWLEntity build(OWLDataFactory f, IRI iri) {
                return new OWLObjectPropertyImpl(f, iri);
            }
        },
        OWLDATAPROPERTY {
            @Override
            OWLEntity build(OWLDataFactory f, IRI iri) {
                return new OWLDataPropertyImpl(f, iri);
            }
        },
        OWLNAMEDINDIVIDUAL {
            @Override
            OWLEntity build(OWLDataFactory f, IRI iri) {
                return new OWLNamedIndividualImpl(f, iri);
            }
        },
        OWLDATATYPE {
            @Override
            OWLEntity build(OWLDataFactory f, IRI iri) {
                return new OWLDatatypeImpl(f, iri);
            }
        },
        OWLANNOTATIONPROPERTY {
            @Override
            OWLEntity build(OWLDataFactory f, IRI iri) {
                return new OWLAnnotationPropertyImpl(f, iri);
            }
        };

        abstract OWLEntity build(OWLDataFactory f, IRI iri);

        private final ReadWriteLock buildLock = new ReentrantReadWriteLock();

        ReadWriteLock getLock() {
            return buildLock;
        }
    }

    public OWLClass getOWLClass(IRI iri) {
        return (OWLClass) unwrap(classesByURI, iri, BuildableObjects.OWLCLASS);
    }

    private void clear(Map<?, ?> map, ReadWriteLock lock) {
        final Lock l = lock.writeLock();
        l.lock();
        try {
            map.clear();
        }
        finally {
            l.unlock();
        }
    }

    public void purge() {
        clear(classesByURI, BuildableObjects.OWLCLASS.getLock());
        clear(objectPropertiesByURI, BuildableObjects.OWLOBJECTPROPERTY.getLock());
        clear(dataPropertiesByURI, BuildableObjects.OWLDATAPROPERTY.getLock());
        clear(datatypesByURI, BuildableObjects.OWLDATATYPE.getLock());
        clear(individualsByURI, BuildableObjects.OWLNAMEDINDIVIDUAL.getLock());
        clear(annotationPropertiesByURI, BuildableObjects.OWLANNOTATIONPROPERTY.getLock());
    }

    public OWLObjectProperty getOWLObjectProperty(IRI iri) {
        return (OWLObjectProperty) unwrap(objectPropertiesByURI, iri, BuildableObjects.OWLOBJECTPROPERTY);
    }

    public OWLDataProperty getOWLDataProperty(IRI iri) {
        return (OWLDataProperty) unwrap(dataPropertiesByURI, iri, BuildableObjects.OWLDATAPROPERTY);
    }

    public OWLNamedIndividual getOWLNamedIndividual(IRI iri) {
        return (OWLNamedIndividual) unwrap(individualsByURI, iri, BuildableObjects.OWLNAMEDINDIVIDUAL);
    }

    public OWLDatatype getOWLDatatype(IRI iri) {
        return (OWLDatatype) unwrap(datatypesByURI, iri, BuildableObjects.OWLDATATYPE);
    }

    public OWLAnnotationProperty getOWLAnnotationProperty(IRI iri) {
        return (OWLAnnotationProperty) unwrap(annotationPropertiesByURI, iri, BuildableObjects.OWLANNOTATIONPROPERTY);
    }
}