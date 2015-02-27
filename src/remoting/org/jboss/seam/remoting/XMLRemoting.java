package org.jboss.seam.remoting;

import org.dom4j.io.SAXReader;

public class XMLRemoting
{
   /**
    * Get safe SaxReader with doctype feature disabled
    * @see http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
    * @return
    * @throws Exception
    */
   public static SAXReader getSafeSaxReader() throws Exception
   {
      SAXReader xmlReader = new SAXReader();
      xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      return xmlReader;
   }
}
