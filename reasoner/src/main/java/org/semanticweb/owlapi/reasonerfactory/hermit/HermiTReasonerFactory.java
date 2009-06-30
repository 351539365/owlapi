package org.semanticweb.owlapi.reasonerfactory.hermit;

import org.semanticweb.owlapi.inference.OWLReasoner;
import org.semanticweb.owlapi.inference.OWLReasonerFactory;
import org.semanticweb.owlapi.inference.OWLReasonerException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasonerfactory.OWLReasonerSetupException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

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
 * Date: 06-Dec-2008
 *
 * A reasoner factory for creating an instance of the HermiT reasoner.
 */
public class HermiTReasonerFactory implements OWLReasonerFactory {

    private Constructor constructor;

    private static final String REASONER_CLASS_NAME = "org.semanticweb.HermiT.Reasoner";

    public HermiTReasonerFactory() {
        try {
            Class clsHermitReasoner = Class.forName(REASONER_CLASS_NAME);
            constructor = clsHermitReasoner.getDeclaredConstructor(OWLOntologyManager.class);
            constructor.setAccessible(true);

        } catch (ClassNotFoundException e) {
            throw new OWLReasonerSetupException(this, e);
        } catch (NoSuchMethodException e) {
            throw new OWLReasonerSetupException(this, e);
        }
    }

    public String getReasonerName() {
        return "HermiT";
    }



    public OWLReasoner createReasoner(OWLOntologyManager manager, Set<OWLOntology> ontologies) throws OWLReasonerSetupException{
        try {
            OWLReasoner reasoner = (OWLReasoner) constructor.newInstance(manager);
            reasoner.loadOntologies(ontologies);
            return reasoner;

        } catch (InstantiationException e) {
            throw new OWLReasonerSetupException(this, e);
        } catch (IllegalAccessException e) {
            throw new OWLReasonerSetupException(this, e);
        } catch (InvocationTargetException e) {
            throw new OWLReasonerSetupException(this, e);
        } catch (OWLReasonerException e) {
            throw new OWLReasonerSetupException(this, e);
        }
    }
}
