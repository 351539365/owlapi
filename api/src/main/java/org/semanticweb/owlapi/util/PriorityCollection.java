package org.semanticweb.owlapi.util;

import static org.semanticweb.owlapi.util.OWLAPIPreconditions.checkNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.annotations.SupportsMIMEType;
import org.semanticweb.owlapi.model.MIMETypeAware;
import org.semanticweb.owlapi.model.OWLOntologyFormatFactory;

import com.google.common.collect.Iterators;

/**
 * A collection that is sorted by HasPriority annotation on its members
 * 
 * @author ignazio
 * @param <T>
 *        type of the collection
 */
public class PriorityCollection<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 40000L;
    private List<T> delegate = new ArrayList<T>();

    public int size() {
        return delegate.size();
    }

    /**
     * @param c
     *        collection of elements to set. Existing elements will be removed,
     *        and the priority collection will be sorted by HasPriority.
     */
    public void set(Collection<T> c) {
        clear();
        delegate.addAll(c);
        sort();
    }

    public void set(T... c) {
        clear();
        for (T t : c) {
            delegate.add(0, t);
        }
        sort();
    }

    private void sort() {
        Collections.sort(delegate, new HasPriorityComparator<T>());
    }

    public void add(T... c) {
        for (T t : c) {
            delegate.add(0, t);
        }
        sort();
    }

    public void add(Collection<T> c) {
        for (T t : c) {
            delegate.add(t);
        }
        sort();
    }

    public void remove(T... c) {
        for (T t : c) {
            delegate.remove(t);
        }
    }

    public void clear() {
        delegate.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.unmodifiableIterator(delegate.iterator());
    }

    /**
     * Returns the first item matching the mime type<br>
     * NOTE: The order in which the services are loaded an examined is not
     * deterministic so this method may return different results if the
     * MIME-Type matches more than one item. However, if the default MIME-Types
     * are always unique, the correct item will always be chosen
     * 
     * @param mimeType
     *        A MIME type to return an {@link OWLOntologyFormatFactory} for
     * @return An {@link OWLOntologyFormatFactory} matching the given mime type
     *         or null if none were found.
     */
    public T getByMIMEType(@Nonnull String mimeType) {
        checkNotNull(mimeType, "MIME-Type cannot be null");
        for (T t : delegate) {
            SupportsMIMEType mime = t.getClass().getAnnotation(
                    SupportsMIMEType.class);
            if (mime != null) {
                if (mimeType.equals(mime.defaultMIMEType())) {
                    return t;
                }
                for (String mimeName : mime.supportedMIMEtypes()) {
                    if (mimeType.equals(mimeName)) {
                        return t;
                    }
                }
            } else {
                // if the instance has MIME types associated
                if (t instanceof MIMETypeAware) {
                    MIMETypeAware mimeTypeAware = (MIMETypeAware) t;
                    if (mimeTypeAware.getDefaultMIMEType().equals(mimeType)) {
                        return t;
                    }
                    if (mimeTypeAware.getMIMETypes().contains(mimeType)) {
                        return t;
                    }
                }
            }
        }
        return null;
    }
}
