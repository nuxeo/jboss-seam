/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Anahide Tchertchian
 */
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
     * @return true if the normal processing can continue, false to trigger
     *         exception management.
     */
    public boolean handleConcurrentRequest(ConversationEntry ce,
            HttpServletRequest request, HttpServletResponse response);

}
