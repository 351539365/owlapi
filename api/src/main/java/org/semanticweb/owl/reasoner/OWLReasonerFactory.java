package org.semanticweb.owl.reasoner;

import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLOntology;

/*
 * Copyright (C) 2008, University of Manchester
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
 * Author: Matthew Horridge<br> The University of Manchester<br> Information Management Group<br>
 * Date: 21-Jan-2009
 *
 * <p>
 * An OWLReasonerFactory is a point for creating instances of {@link org.semanticweb.owl.reasoner.OWLReasoner}
 * objects.  A reasoner reasons over a set of ontologies that is defined by an ontology and its imports closure.
 * </p>
 * @see org.semanticweb.owl.reasoner.OWLReasoner
 */
public interface OWLReasonerFactory {

    /**
     * Creates an OWLReasoner that reasons over the imports closure of the specified ontology.  The reasoner will obtain
     * the imports closure from the specified ontology manager.
     * @param manager The manager which will be used to obtain the imports closure of {@code ontology}
     * @param ontology The ontology whose imports closure will be reasoned over by the reasoner that is created.
     * @return The reasoner that reasons over the imports closure of the specified ontology.  Note that calling this
     * method multiple times with the same manager and ontology will return <b>fresh</b> instances of OWLReasoner.
     * @throws NullPointerException if the {@code manager} or {@code ontology} are {@code null}.
     */
    OWLReasoner createReasoner(OWLOntologyManager manager, OWLOntology ontology);


    /**
     * Creates an OWLReasoner that reasons over the imports closure of the specified ontology.  The reasoner will obtain
     * the imports closure from the specified ontology manager.
     * @param manager The manager which will be used to obtain the imports closure of {@code ontology}
     * @param ontology The ontology whose imports closure will be reasoned over by the reasoner that is created.
     * @param config A configuration object that can be used to customise the setup of the reasoner that will be created
     * by calling this method.
     * @return The reasoner that reasons over the imports closure of the specified ontology.  Note that calling this
     * method multiple times with the same manager and ontology will return <b>fresh</b> instances of OWLReasoner.
     * @throws IllegalConfigurationException if the configuration object is inappropriate for reasoners created by this
     * factory, or if there is an illegal setting on the configuration.
     * @throws NullPointerException if any of {@code manager}, {@code ontology} or {@code config} are {@code null}.
     */
    OWLReasoner createReasoner(OWLOntologyManager manager, OWLOntology ontology, OWLReasonerConfiguration config) throws IllegalConfigurationException;



    /**
     * Creates an OWLReasoner that reasons over the imports closure of the specified ontology plus the specified
     * augmenting ontology.  The reasoner will obtain the imports closure from the specified ontology manager.
     * @param manager The manager which will be used to obtain the imports closure of {@code ontology}
     * @param ontology The ontology whose imports closure will be reasoned over by the reasoner that is created.
     * @param config A configuration object that can be used to customise the setup of the reasoner that will be created
     * by calling this method.
     * @param augmentingOntology An ontology that can be used to augment the imports closure of the specified ontology.
     * @return The reasoner that reasons over the imports closure of the specified ontology.  Note that calling this
     * method multiple times with the same manager and ontology will return <b>fresh</b> instances of OWLReasoner.
     * @throws IllegalConfigurationException if the configuration object is inappropriate for reasoners created by this
     * factory, or if there is an illegal setting on the configuration.
     * @throws NullPointerException if any of {@code manager}, {@code ontology}, {@code augmentingOntology} or {@code config} are {@code null}.
     */
    OWLReasoner createReasoner(OWLOntologyManager manager, OWLOntology ontology, OWLOntology augmentingOntology, OWLReasonerConfiguration config) throws IllegalConfigurationException;

}
