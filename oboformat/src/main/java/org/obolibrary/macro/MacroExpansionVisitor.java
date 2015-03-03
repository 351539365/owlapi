package org.obolibrary.macro;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OntologyAxiomPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author cjm TODO - allow use of prefixes */
public class MacroExpansionVisitor {

    protected static final Logger LOG = LoggerFactory
            .getLogger(MacroExpansionVisitor.class.getName());
    private final OWLOntology inputOntology;
    private final OWLOntologyManager manager;
    private final Visitor visitor;
    protected ManchesterSyntaxTool manchesterSyntaxTool;
    protected boolean shouldTransferAnnotations = false;
    private final boolean shouldAddExpansionMarker;
    protected Set<OWLAnnotation> extraAnnotations;

    public MacroExpansionVisitor(OWLOntology ontology) {
        this(ontology, AbstractMacroExpansionVisitor.EMPTY_ANNOTATIONS, false,
                false);
    }

    /**
     * @param ontology
     * @param shouldAddExpansionMarker
     */
    public MacroExpansionVisitor(OWLOntology ontology,
            boolean shouldAddExpansionMarker) {
        this(ontology, AbstractMacroExpansionVisitor.EMPTY_ANNOTATIONS, false,
                shouldAddExpansionMarker);
    }

    public MacroExpansionVisitor(OWLOntology ontology,
            boolean shouldTransferAnnotations, boolean shouldAddExpansionMarker) {
        this(ontology, AbstractMacroExpansionVisitor.EMPTY_ANNOTATIONS,
                shouldTransferAnnotations, shouldAddExpansionMarker);
    }

    /**
     * @param inputOntology
     *        inputOntology
     * @param extraAnnotations
     *        extra annotations to add
     * @param shouldTransferAnnotations
     * @param shouldAddExpansionMarker
     */
    public MacroExpansionVisitor(OWLOntology inputOntology,
            Set<OWLAnnotation> extraAnnotations,
            boolean shouldTransferAnnotations, boolean shouldAddExpansionMarker) {
        this.inputOntology = inputOntology;
        this.extraAnnotations = extraAnnotations;
        this.shouldTransferAnnotations = shouldTransferAnnotations;
        this.shouldAddExpansionMarker = shouldAddExpansionMarker;
        visitor = new Visitor(inputOntology, shouldAddExpansionMarker);
        manchesterSyntaxTool = new ManchesterSyntaxTool(inputOntology);
        manager = inputOntology.getOWLOntologyManager();
    }

    public MacroExpansions getMacroExpansions() {
        return new MacroExpansions();
    }

    /** @return ontology with expanded macros */
    public OWLOntology expandAll() {
        MacroExpansions macroExpansions = new MacroExpansions();
        Set<OWLAxiom> newAxioms = macroExpansions.getNewAxioms();
        Set<OWLAxiom> rmAxioms = macroExpansions.getRmAxioms();
        manager.addAxioms(inputOntology, newAxioms);
        manager.removeAxioms(inputOntology, rmAxioms);
        return inputOntology;
    }

    private class MacroExpansions {

        private Set<OWLAxiom> newAxioms = new HashSet<OWLAxiom>();
        private Set<OWLAxiom> rmAxioms = new HashSet<OWLAxiom>();

        public MacroExpansions() {
            for (OWLSubClassOfAxiom axiom : inputOntology
                    .getAxioms(AxiomType.SUBCLASS_OF)) {
                OWLAxiom newAxiom = visitor.visit(axiom);
                replaceIfDifferent(axiom, newAxiom);
            }
            for (OWLEquivalentClassesAxiom axiom : inputOntology
                    .getAxioms(AxiomType.EQUIVALENT_CLASSES)) {
                OWLAxiom newAxiom = visitor.visit(axiom);
                replaceIfDifferent(axiom, newAxiom);
            }
            for (OWLClassAssertionAxiom axiom : inputOntology
                    .getAxioms(AxiomType.CLASS_ASSERTION)) {
                OWLAxiom newAxiom = visitor.visit(axiom);
                replaceIfDifferent(axiom, newAxiom);
            }
            for (OWLAnnotationAssertionAxiom axiom : inputOntology
                    .getAxioms(AxiomType.ANNOTATION_ASSERTION)) {
                if (expand(axiom)) {
                    rmAxioms.add(axiom);
                }
            }
        }

        private void replaceIfDifferent(OWLAxiom ax, OWLAxiom exAx) {
            if (!ax.equals(exAx)) {
                newAxioms.add(exAx);
                rmAxioms.add(ax);
            }
        }

        public Set<OWLAxiom> getNewAxioms() {
            return newAxioms;
        }

        public Set<OWLAxiom> getRmAxioms() {
            return rmAxioms;
        }

        @SuppressWarnings("null")
        private boolean expand(OWLAnnotationAssertionAxiom axiom) {
            OWLAnnotationProperty prop = axiom.getProperty();
            String expandTo = visitor.expandAssertionToMap.get(prop.getIRI());
            HashSet<OWLAxiom> declarations = new HashSet<OWLAxiom>();
            boolean expandedSomething = false;
            try {
                if (expandTo != null) {
                    Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>(
                            extraAnnotations);
                    if (shouldAddExpansionMarker) {
                        annotations.add(visitor.getExpansionMarkerAnnotation());
                    }
                    if (shouldTransferAnnotations()) {
                        annotations.addAll(axiom.getAnnotations());
                    }
                    // when expanding assertions, the axiom is an annotation
                    // assertion,
                    // and the value may be not be explicitly declared. If it is
                    // not,
                    // we assume it is a class
                    IRI axValIRI = (IRI) axiom.getValue();
                    OWLClass axValClass = visitor.dataFactory
                            .getOWLClass(axValIRI);
                    if (inputOntology.getDeclarationAxioms(axValClass)
                            .isEmpty()) {
                        OWLDeclarationAxiom declarationAxiom = visitor.dataFactory
                                .getOWLDeclarationAxiom(axValClass, annotations);
                        declarations.add(declarationAxiom);
                        newAxioms.add(declarationAxiom);
                        manager.addAxiom(inputOntology, declarationAxiom);
                        // we need to sync the MST entity checker with the new
                        // ontology
                        // plus declarations;
                        // we do this by creating a new MST - this is not
                        // particularly
                        // efficient, a better
                        // way might be to first scan the ontology for all
                        // annotation
                        // axioms that will be expanded,
                        // then add the declarations at this point
                        manchesterSyntaxTool = new ManchesterSyntaxTool(
                                inputOntology);
                    }
                    LOG.info("Template to Expand {}", expandTo);
                    expandTo = expandTo.replaceAll("\\?X", manchesterSyntaxTool
                            .getId((IRI) axiom.getSubject()));
                    expandTo = expandTo.replaceAll("\\?Y",
                            manchesterSyntaxTool.getId(axValIRI));
                    LOG.info("Expanding {}", expandTo);
                    try {
                        Set<OntologyAxiomPair> setAxp = manchesterSyntaxTool
                                .parseManchesterExpressionFrames(expandTo);
                        for (OntologyAxiomPair axp : setAxp) {
                            OWLAxiom expandedAxiom = axp.getAxiom();
                            if (shouldTransferAnnotations()) {
                                expandedAxiom = expandedAxiom
                                        .getAnnotatedAxiom(annotations);
                            }
                            newAxioms.add(expandedAxiom);
                            expandedSomething = true;
                        }
                    } catch (Exception ex) {
                        LOG.error(ex.getMessage(), ex);
                    }
                    // TODO:
                }
            } finally {
                manager.removeAxioms(inputOntology, declarations);
            }
            return expandedSomething;
        }
    }

    private class Visitor extends AbstractMacroExpansionVisitor {

        Visitor(OWLOntology inputOntology, boolean shouldAddExpansionMarker) {
            super(inputOntology, shouldAddExpansionMarker);
        }

        @Override
        protected OWLClassExpression expandOWLObjSomeVal(
                OWLClassExpression filler, OWLObjectPropertyExpression p) {
            return expandObject(filler, p);
        }

        @Override
        protected OWLClassExpression expandOWLObjHasVal(OWLObjectHasValue desc,
                OWLIndividual filler, OWLObjectPropertyExpression p) {
            OWLClassExpression result = expandObject(filler, p);
            if (result != null) {
                result = dataFactory.getOWLObjectSomeValuesFrom(
                        desc.getProperty(), result);
            }
            return result;
        }

        @SuppressWarnings("null")
        OWLClassExpression expandObject(Object filler,
                OWLObjectPropertyExpression p) {
            OWLClassExpression result = null;
            IRI iri = ((OWLObjectProperty) p).getIRI();
            IRI templateVal = null;
            if (expandExpressionMap.containsKey(iri)) {
                if (filler instanceof OWLObjectOneOf) {
                    Set<OWLIndividual> inds = ((OWLObjectOneOf) filler)
                            .getIndividuals();
                    if (inds.size() == 1) {
                        OWLIndividual ind = inds.iterator().next();
                        if (ind instanceof OWLNamedIndividual) {
                            templateVal = ((OWLNamedObject) ind).getIRI();
                        }
                    }
                }
                if (filler instanceof OWLNamedObject) {
                    templateVal = ((OWLNamedObject) filler).getIRI();
                }
                if (templateVal != null) {
                    String tStr = expandExpressionMap.get(iri);
                    String exStr = tStr.replaceAll("\\?Y",
                            manchesterSyntaxTool.getId(templateVal));
                    try {
                        result = manchesterSyntaxTool
                                .parseManchesterExpression(exStr);
                    } catch (OWLParserException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }
            return result;
        }
    }

    public boolean shouldTransferAnnotations() {
        return shouldTransferAnnotations;
    }

    public void setShouldTransferAnnotations(boolean shouldTransferAnnotations) {
        this.shouldTransferAnnotations = shouldTransferAnnotations;
    }

    /** Call this method to clear internal references. */
    public void dispose() {
        manchesterSyntaxTool.dispose();
    }
}
