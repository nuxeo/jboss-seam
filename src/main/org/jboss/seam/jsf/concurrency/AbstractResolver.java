package org.jboss.seam.jsf.concurrency;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.core.ConversationEntry;
import org.jboss.seam.core.Manager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;

public abstract class AbstractResolver implements ConcurrentRequestResolver {

    protected boolean handleNoResponse(HttpServletResponse response) {
        response.setStatus(204);
        FacesContext.getCurrentInstance().responseComplete();
        return false;
    }

    protected  boolean handleRedirect(ConversationEntry ce, HttpServletResponse response, String url) {
        try {
            if (!url.contains("="+ ce.getId())) {
                StringBuilder builder = new StringBuilder()
                .append(url)
                .append( url.contains("?") ? '&' : '?' )
                .append(Manager.instance().getConversationIdParameter())
                .append('=')
                .append( ce.getId() );
                url = builder.toString();
            }
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
        return true;
    }

    protected void addTransientMessage(StatusMessage.Severity severity, String keyMessage, String defaultMessage) {
        FacesMessages.addTransientMessage(new StatusMessage(severity,keyMessage,defaultMessage,keyMessage,defaultMessage));
    }

}
