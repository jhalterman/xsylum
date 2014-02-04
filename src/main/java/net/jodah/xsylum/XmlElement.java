package net.jodah.xsylum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A named XML element.
 * 
 * @author Jonathan Halterman
 */
public final class XmlElement {
  private final Element element;

  public XmlElement(Element element) {
    this.element = element;
  }

  /**
   * Get the value of the {@code attribute}.
   * 
   * @throws XsylumException if the {@code attribute} cannot be found
   */
  public String attribute(String attribute) throws XsylumException {
    String value = element.getAttribute(attribute);
    if (value == null)
      throw new XsylumException("Attribute %s does not exist", attribute);
    return value;
  }

  /**
   * Get the value of the {@code attribute} as a boolean. Returns true for "true", "1", "yes", "y",
   * else returns false.
   * 
   * @throws XsylumException if the {@code attribute} cannot be found
   */
  public boolean attributeAsBoolean(String attribute) throws XsylumException {
    String value = attribute(attribute).toLowerCase();
    if ("true".equals(value) || "1".equals(value) || "yes".equals(value) || "y".equals(value))
      return true;
    return false;
  }

  /**
   * Get the value of the {@code attribute} as a double. Returns 0 if value cannot be parsed to a
   * double.
   * 
   * @throws XsylumException if the {@code attribute} cannot be found
   */
  public double attributeAsDouble(String attribute) throws XsylumException {
    String value = attribute(attribute);
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
   * Get the value of the {@code attribute} as an integer. Returns 0 if value cannot be parsed to an
   * int.
   * 
   * @throws XsylumException if the {@code attribute} cannot be found
   */
  public int attributeAsInt(String attribute) throws XsylumException {
    String value = attribute(attribute);
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
   * Builds and returns a map of the element's attributes, else empty map if the element has no
   * attributes.
   */
  public Map<String, String> attributes() {
    if (!element.hasAttributes())
      return Collections.emptyMap();

    NamedNodeMap attributes = element.getAttributes();
    Map<String, String> result = new HashMap<String, String>();
    for (int i = 0; i < attributes.getLength(); i++) {
      Node node = attributes.item(i);
      result.put(node.getNodeName(), node.getNodeValue());
    }

    return result;
  }

  /**
   * Returns the element's children, else empty list if the element has no children.
   */
  public List<XmlElement> children() {
    NodeList children = element.getChildNodes();
    if (children.getLength() == 0)
      return Collections.emptyList();

    List<XmlElement> result = new ArrayList<XmlElement>(children.getLength());
    for (int i = 0; i < element.getChildNodes().getLength(); i++) {
      Node child = element.getChildNodes().item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE)
        result.add(new XmlElement((Element) child));
    }

    return result;
  }

  /**
   * Returns the underlying element.
   */
  public Element element() {
    return element;
  }

  /**
   * Returns the XmlElement at the {@code index}, else null if none can be found or the node at the
   * {@code index} is not an element
   */
  public XmlElement get(int index) {
    Node node = element.getChildNodes().item(index);
    return node.getNodeType() == Node.ELEMENT_NODE ? new XmlElement((Element) node) : null;
  }

  /**
   * Returns all child elements that match the {@code name} else empty List.
   */
  public List<XmlElement> get(String name) {
    List<XmlElement> result = new ArrayList<XmlElement>();
    for (int i = 0; i < element.getChildNodes().getLength(); i++) {
      Node child = element.getChildNodes().item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(name))
        result.add(new XmlElement((Element) child));
    }

    return result;
  }

  /**
   * Returns whether the element contains the {@code attribute}.
   */
  public boolean hasAttribute(String attribute) {
    return element.hasAttribute(attribute);
  }

  /**
   * Returns whether the element contains attributes.
   */
  public boolean hasAttributes() {
    return element.hasAttributes();
  }

  /**
   * Returns whether the element contains any child elements with the {@code name}.
   */
  public boolean hasChild(String name) {
    for (int i = 0; i < element.getChildNodes().getLength(); i++) {
      Node child = element.getChildNodes().item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(name))
        return true;
    }

    return false;
  }

  /**
   * Returns the element's name.
   */
  public String name() {
    return element.getNodeName();
  }

  @Override
  public String toString() {
    return toXml();
  }

  /**
   * Returns a XML representation of the element, including its attributes and value.
   */
  public String toXml() {
    String name = name();
    NodeList children = element.getChildNodes();
    NamedNodeMap attributes = element.getAttributes();
    StringBuilder sb = new StringBuilder("<").append(name);

    if (attributes.getLength() > 0) {
      sb.append(' ');
      for (int i = 0; i < attributes.getLength(); i++) {
        Node n = attributes.item(i);
        if (i > 0)
          sb.append(' ');
        sb.append(n.getNodeName()).append("=").append("\"").append(n.getNodeValue()).append("\"");
      }
    }
    if (children.getLength() == 0)
      sb.append("/>");
    else {
      sb.append('>');
      for (int i = 0; i < children.getLength(); i++) {
        Node child = children.item(i);
        if (child.getNodeType() == Node.ELEMENT_NODE)
          sb.append(new XmlElement((Element) child));
        else if (child.getNodeType() == Node.TEXT_NODE)
          sb.append(child.getNodeValue());
      }
      sb.append("</").append(name).append('>');
    }

    return sb.toString();
  }

  /**
   * Returns the text value of the element.
   */
  public String value() {
    StringBuilder sb = new StringBuilder();
    NodeList children = element.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      if (child.getNodeType() == Node.TEXT_NODE)
        sb.append(child.getNodeValue());
    }
    return sb.toString();
  }

  /**
   * Get the element value as a boolean. Returns true for "true", "1", "yes", "y", else returns
   * false.
   */
  public boolean valueAsBoolean() {
    String value = value().toLowerCase();
    if ("true".equals(value) || "1".equals(value) || "yes".equals(value) || "y".equals(value))
      return true;
    return false;
  }

  /**
   * Get the element value as a double. Returns 0 if value cannot be parsed to a double.
   */
  public double valueAsDouble() {
    try {
      return Double.parseDouble(value());
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
   * Get the element value as an integer. Returns 0 if value cannot be parsed to an integer.
   */
  public int valueAsInt() {
    try {
      return Integer.parseInt(value());
    } catch (NumberFormatException e) {
      return 0;
    }
  }
}
