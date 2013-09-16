package org.jboss.seam.jsf.concurrency;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.core.ConversationEntry;

public interface ConcurrentRequestResolver {

    public static final String NAME = "org.jboss.seam.jsf.concurrency.ConcurrentRequestResolver";

    /**
     * This method should be implemented to resolve the concurrency issue. If
     * for some reasons, no resolution can be found, this method should return
     * false so that Seam standard Exception is used.
     *
     * @param ce
     * @param request
     * @param response
     * @return true if the normal processing can continue, false to trigger exception management.
     */
    public boolean handleConcurrentRequest(ConversationEntry ce, HttpServletRequest request,
            HttpServletResponse response);

}
