package net.jodah.xsylum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XPathSearchable<T> {
  protected final T source;

  XPathSearchable(T source) {
    this.source = source;
  }

  /**
   * Returns the first element matching the {@code expression}, else null if none can be found.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public XmlElement find(String expression) throws XPathExpressionException {
    XPathExpression expr = XPathFactory.newInstance().newXPath().compile(expression);
    Node node = (Node) expr.evaluate(source, XPathConstants.NODE);
    return node != null && node.getNodeType() == Node.ELEMENT_NODE ? new XmlElement((Element) node)
        : null;
  }

  /**
   * Returns all elements that match the {@code expression}, else empty list if none can be found.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<XmlElement> findAll(String expression) throws XPathExpressionException {
    XPathExpression expr = XPathFactory.newInstance().newXPath().compile(expression);
    NodeList nodeList = (NodeList) expr.evaluate(source, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      return Collections.emptyList();

    List<XmlElement> elements = new ArrayList<XmlElement>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE)
        elements.add(new XmlElement((Element) node));
    }

    return elements;
  }

  /**
   * Finds the value for the XPath {@code expression} ending in text().
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public String findValue(String expression) throws XPathExpressionException {
    XPathExpression expr = XPathFactory.newInstance().newXPath().compile(expression);
    return expr.evaluate(source);
  }

  /**
   * Finds the value for the XPath {@code expression} ending in text() as a boolean. Returns true
   * for "true", "1", "yes", "y", else returns false.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public boolean findValueAsBoolean(String expression) throws XPathExpressionException {
    String value = findValue(expression).toLowerCase();
    if ("true".equals(value) || "1".equals(value) || "yes".equals(value) || "y".equals(value))
      return true;
    return false;
  }

  /**
   * Finds the value for the XPath {@code expression} ending in text() as a double. Returns 0 if
   * value cannot be parsed to a double.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public double findValueAsDouble(String expression) throws XPathExpressionException {
    String value = findValue(expression);
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
   * Finds the value for the XPath {@code expression} ending in text() as an integer. Returns 0 if
   * value cannot be parsed to an int.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public int findValueAsInt(String expression) throws XPathExpressionException {
    String value = findValue(expression);
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
   * Finds the values for the XPath {@code expression} ending in text().
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<String> findValues(String expression) throws XPathExpressionException {
    XPathExpression expr = XPathFactory.newInstance().newXPath().compile(expression);
    NodeList nodeList = (NodeList) expr.evaluate(source, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      return Collections.emptyList();

    List<String> values = new ArrayList<String>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.TEXT_NODE)
        values.add(node.getNodeValue());
    }

    return values;
  }
}
