/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.manchester.cs.owl.owlapi.alternateimpls.owldatafactory;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
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
/**
 * @author ignazio
 * locks with weak references used for cache
 */
public class InternalsLWR implements OWLDataFactoryInternals {
    private final WeakHashMap<IRI, WeakReference<? extends OWLEntity>> classesByURI;
    private final WeakHashMap<IRI, WeakReference<? extends OWLEntity>> objectPropertiesByURI;
    private final WeakHashMap<IRI, WeakReference<? extends OWLEntity>> dataPropertiesByURI;
    private final WeakHashMap<IRI, WeakReference<? extends OWLEntity>> datatypesByURI;
    private final WeakHashMap<IRI, WeakReference<? extends OWLEntity>> individualsByURI;
    private final WeakHashMap<IRI, WeakReference<? extends OWLEntity>> annotationPropertiesByURI;
    private final OWLDataFactory factory;
    /**
     * @param f the factory to refer to
     */
    public InternalsLWR(OWLDataFactory f) {
        factory = f;
        classesByURI = new WeakHashMap<IRI, WeakReference<? extends OWLEntity>>();
        objectPropertiesByURI = new WeakHashMap<IRI, WeakReference<? extends OWLEntity>>();
        dataPropertiesByURI = new WeakHashMap<IRI, WeakReference<? extends OWLEntity>>();
        datatypesByURI = new WeakHashMap<IRI, WeakReference<? extends OWLEntity>>();
        individualsByURI = new WeakHashMap<IRI, WeakReference<? extends OWLEntity>>();
        annotationPropertiesByURI = new WeakHashMap<IRI, WeakReference<? extends OWLEntity>>();
    }

    private OWLEntity unwrap(Map<IRI, WeakReference<? extends OWLEntity>> map, IRI iri, BuildableObjects type) {
        OWLEntity toReturn = null;
        while (toReturn == null) {
            WeakReference<? extends OWLEntity> r = safeRead(map, iri, type);
            if (r == null || r.get() == null) {
                toReturn = type.build(factory, iri);
                r = new WeakReference<OWLEntity>(toReturn);
                safeWrite(map, iri, r, type);
            }
            else {
                toReturn = r.get();
            }
        }
        return toReturn;
    }

    private WeakReference<? extends OWLEntity> safeRead(Map<IRI, WeakReference<? extends OWLEntity>> map, IRI iri, BuildableObjects type) {
        final Lock lock = type.getLock().readLock();
        lock.lock();
        try {
            return map.get(iri);
        }
        finally {
            lock.unlock();
        }
    }

    private void safeWrite(Map<IRI, WeakReference<? extends OWLEntity>> map, IRI iri, WeakReference<? extends OWLEntity> value, BuildableObjects type) {
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