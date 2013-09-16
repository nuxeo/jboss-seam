/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 */

package org.jboss.seam.jsf.concurrency;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.ConversationEntry;
import org.jboss.seam.international.StatusMessage.Severity;

/**
 *
 * Default implementation to handle concurrent requests against the same Seam Conversation.
 * This component can be overridden using the standard Seam override system.
 *
 * @since 5.7.3
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
@Scope(ScopeType.STATELESS)
@Name(ConcurrentRequestResolver.NAME)
@Install(precedence = FRAMEWORK)
@BypassInterceptors
public class NuxeoConcurrentRequestResolver extends AbstractResolver implements
        ConcurrentRequestResolver {

    public boolean handleConcurrentRequest(ConversationEntry ce, HttpServletRequest request,
            HttpServletResponse response) {

        if (request.getMethod().equalsIgnoreCase("get")) {
            // should flag request to skip apply method bindings
            // XXX
            // let's try to continue
            addTransientMessage(Severity.WARN, "org.nuxeo.seam.concurrent.unsaferun", "This page may be not up to date, an other concurrent requests is still running");
            return true;
        } else if (request.getMethod().equalsIgnoreCase("post")) {
            String url = request.getQueryString();
            System.out.println(url);
            url = "http://127.0.0.1:8080/nuxeo/error_test_web/error_index.faces?"; // XXX
            System.out.println(url);
            addTransientMessage(Severity.WARN,"org.nuxeo.seam.concurrent.skip", "Your request was not processed because you already have a requests in processing.");

            // XXX should Mark Request for No Tx Commit !

            return handleRedirect(ce, response, url);
        } else {
            return handleNoResponse(response);
        }
    }

}
