package net.jodah.xsylum;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlDocument extends XmlSearchable<Document> {
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

  @Override
  public XmlElement get(String tagName) {
    NodeList nodeList = source.getElementsByTagName(tagName);
    return nodeList.getLength() == 0 ? null : new XmlElement((Element) nodeList.item(0));
  }

  @Override
  public List<XmlElement> getAll(String tagName) {
    NodeList nodeList = source.getElementsByTagName(tagName);
    List<XmlElement> result = new ArrayList<XmlElement>(nodeList.getLength());
    for (int i = 0; i < nodeList.getLength(); i++)
      result.add(new XmlElement((Element) nodeList.item(i)));
    return result;
  }
}
