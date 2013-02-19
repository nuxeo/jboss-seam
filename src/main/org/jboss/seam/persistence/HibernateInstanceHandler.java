package org.jboss.seam.persistence;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.spi.EventSource;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.jboss.seam.security.permission.PermissionManager;

public class HibernateInstanceHandler implements InvocationHandler {

    protected final Object proxied;

    protected HibernateInstanceHandler(Object object) {
        proxied = object;
    }

    public static EntityManager newProxy(EntityManager proxied) {
        return (EntityManager) Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), new Class<?>[] { EntityManager.class,
                Serializable.class }, new HibernateInstanceHandler(proxied));
    }

    public static FullTextEntityManager newProxy(FullTextEntityManager proxied) {
        return (FullTextEntityManager) Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), new Class<?>[] {
                FullTextEntityManager.class, EntityManager.class,
                Serializable.class }, new HibernateInstanceHandler(proxied));
    }


    public static Session newProxy(Session proxied) {
        return (Session) Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), new Class<?>[] { Session.class,
                SessionImplementor.class, EventSource.class },
                new HibernateInstanceHandler(proxied));
    }

    public static FullTextSession newProxy(FullTextSession proxied) {
        return (FullTextSession) Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), new Class<?>[] {
            FullTextSession.class, Session.class,
            SessionImplementor.class, EventSource.class }, new HibernateInstanceHandler(proxied));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        String mname = method.getName();
        if (EntityManager.class.isAssignableFrom(method.getDeclaringClass())) {
            if ("createQuery".equals(mname)) {
                return createQuery((EntityManager) proxied, (String) args[0]);
            } else if ("remove".equals(mname)) {
                return remove((EntityManager) proxied, args[0]);
            }
        }
        return method.invoke(proxied, args);
    }

    protected Object remove(EntityManager proxied, Object object) {
        proxied.remove(object);
        PermissionManager.instance().clearPermissions(object);
        return null;
    }

    protected Query createQuery(EntityManager mgr, String ejbql) {
        if (ejbql.indexOf('#') > 0) {
            QueryParser qp = new QueryParser(ejbql);
            Query query = mgr.createQuery(qp.getEjbql());
            for (int i = 0; i < qp.getParameterValueBindings().size(); i++) {
                query.setParameter(QueryParser.getParameterName(i), qp
                        .getParameterValueBindings().get(i).getValue());
            }
            return query;
        }
        return mgr.createQuery(ejbql);
    }
}
