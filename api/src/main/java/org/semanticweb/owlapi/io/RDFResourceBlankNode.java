package org.semanticweb.owlapi.io;

import static org.semanticweb.owlapi.util.OWLAPIPreconditions.*;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.NodeID;

/** Anonymous node implementation */
public class RDFResourceBlankNode extends RDFResource {
    private final IRI resource;

    /** Create an RDFResource that is anonymous
     * 
     * @param resource
     *            The IRI of the resource */
    public RDFResourceBlankNode(@Nonnull IRI resource) {
        this.resource = checkNotNull(resource, "resource cannot be null");
    }

    /** Create an RDFResource that is anonymous
     * 
     * @param anonId
     *            the number at the end of the anon IRI */
    public RDFResourceBlankNode(int anonId) {
        this(NodeID.nodeId(anonId));
    }

    @Override
    public boolean isLiteral() {
        return false;
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RDFResourceBlankNode)) {
            return false;
        }
        RDFResourceBlankNode other = (RDFResourceBlankNode) o;
        return resource.equals(other.resource);
    }

    @Override
    public String toString() {
        return resource.toString();
    }

    @Override
    public IRI getIRI() {
        return resource;
    }

    @Override
    public IRI getResource() {
        return resource;
    }
}
