package io.antal.rest;

import java.util.Collection;

/**
 * This is a model object representing the common Nike format for pages responses
 *
 * @param <T> Type of content in the pages of data
 */
public class PagedResponse<T> {

    private final Collection<T> objects;
    private final Pages pages;

    public PagedResponse(Collection<T> objects, Pages pages) {
        this.objects = objects;
        this.pages = pages;
    }

    public static class Pages {
        private final String next;

        public Pages(String next) {
            this.next = next;
        }

        public String getNext() {
            return next;
        }

    }

    public Collection<T> getObjects() {
        return objects;
    }

    public Pages getPages() {
        return pages;
    }
}
