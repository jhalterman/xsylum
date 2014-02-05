package net.jodah.xsylum;

import org.w3c.dom.Document;

public class XmlDocument extends XPathSearchable<Document> {
  public XmlDocument(Document document) {
    super(document);
  }

  /**
   * Returns the underlying document.
   */
  public Document document() {
    return source;
  }

  /**
   * Returns the root element for the document.
   */
  public XmlElement root() {
    return new XmlElement(source.getDocumentElement());
  }
}
