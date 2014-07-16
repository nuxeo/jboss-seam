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

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.core.ConversationEntry;
import org.jboss.seam.core.Manager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;

public abstract class AbstractResolver implements ConcurrentRequestResolver {

    private static final Log log = LogFactory.getLog(AbstractResolver.class);

    protected boolean handleNoResponse(HttpServletResponse response) {
        response.setStatus(204);
        FacesContext.getCurrentInstance().responseComplete();
        return false;
    }

    protected boolean handleRedirect(ConversationEntry ce,
            HttpServletResponse response, String url) {
        try {
            if (!url.contains("=" + ce.getId())) {
                StringBuilder builder = new StringBuilder().append(url).append(
                        url.contains("?") ? '&' : '?').append(
                        Manager.instance().getConversationIdParameter()).append(
                        '=').append(ce.getId());
                url = builder.toString();
            }
            response.sendRedirect(url);
        } catch (IOException e) {
            log.error(e, e);
        }
        FacesContext.getCurrentInstance().responseComplete();
        return true;
    }

    protected void addTransientMessage(StatusMessage.Severity severity,
            String keyMessage, String defaultMessage) {
        FacesMessages.addTransientMessage(new StatusMessage(severity,
                keyMessage, defaultMessage, keyMessage, defaultMessage));
    }

}
