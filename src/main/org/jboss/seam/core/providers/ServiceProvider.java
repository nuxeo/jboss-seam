package org.jboss.seam.core.providers;

/**
 * Interface used to contribute a service provider to Seam injection
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public interface ServiceProvider {

    public static final String NAME ="org.jboss.seam.extensions.provider";

    Object lookup(String name, Class type, boolean create);

}
