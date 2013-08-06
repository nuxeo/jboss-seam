package org.jboss.seam.international;

import static org.jboss.seam.ScopeType.EVENT;
import static org.jboss.seam.annotations.Install.BUILT_IN;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.SeamResourceBundle;

/**
 * Factory for a Map that contains interpolated messages defined in the
 * Seam ResourceBundle.
 * 
 * @see org.jboss.seam.core.SeamResourceBundle
 * 
 * @author Gavin King
 */
@Scope(ScopeType.STATELESS)
@BypassInterceptors
@Name("org.jboss.seam.international.messagesFactory")
@Install(precedence=BUILT_IN)
public class Messages
{
   //TODO: now we have ELResolver, it doesn't *have* to be a Map...
      
   protected Map createMap() 
   {  
	  // AbstractMap uses the implementation of entrySet to perform all its 
	  // operations - for a resource bundle this is very inefficient for keys
      return new AbstractMap<String, String>()
      {
         private java.util.ResourceBundle bundle = SeamResourceBundle.getBundle();
         
         @Override
         public String get(Object key) 
         {
            if (key instanceof String)
            {
               String resourceKey = (String) key;
               String resource=null;
               if (bundle!=null)
               {
                  try
                  {
                     resource = bundle.getString(resourceKey);
                  }
                  catch (MissingResourceException mre)
                  {
                     //Just swallow
                  }
               }
               return resource==null ? resourceKey : resource;
            }
            else
            {
               return null;
            }
         }
         
         @Override
         public Set<Map.Entry<String, String>> entrySet() 
         {
        	Enumeration<String> keys = bundle.getKeys();  
            Map<String, String> map = new HashMap<String, String>();
            while ( keys.hasMoreElements() )
            {
               String key = keys.nextElement();
               map.put( key, get(key) );
            }  
            return Collections.unmodifiableSet(map.entrySet());
         }

         @Override
         public boolean containsKey(Object key)
         {
            return get(key) != null;
         }

         @Override
         public Set<String> keySet()
         {
            Enumeration<String> keys = bundle.getKeys();  
            return new HashSet<String>(Collections.list(keys));
         }

         @Override
         public int size()
         {
            return keySet().size();
         }
         
      };
   }

   /**
    * Create the Map and cache it in the EVENT scope. No need to cache
    * it in the SESSION scope, since it is inexpensive to create.
    * 
    * @return a Map that interpolates messages in the Seam ResourceBundle
    */
   @Factory(value="org.jboss.seam.international.messages", autoCreate=true, scope=EVENT)
   public Map<String, String> getMessages()
   {
      return createMap();
   }
   
   /**
    * @return the message Map instance
    */
   public static Map<String, String> instance()
   {
      if ( !Contexts.isSessionContextActive() )
      {
         throw new IllegalStateException("no event context active");
      }
      return (Map<String, String>) Component.getInstance("org.jboss.seam.international.messages", true);
   }
}