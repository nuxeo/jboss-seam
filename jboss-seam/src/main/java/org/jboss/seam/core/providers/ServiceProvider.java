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
package org.jboss.seam.core.providers;

/***
 * Interface used to contribute a service provider to Seam injection
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
public interface ServiceProvider {

    public static final String NAME = "org.jboss.seam.extensions.provider";

    Object lookup(String name, Class type, boolean create);

}
