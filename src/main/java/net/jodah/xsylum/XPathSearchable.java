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

  static interface Converter<T> {
    Converter<Integer> intConverter = new Converter<Integer>() {
      @Override
      public Integer convert(String value) {
        try {
          return Integer.parseInt(value);
        } catch (NumberFormatException e) {
          return 0;
        }
      }
    };

    Converter<Boolean> booleanConverter = new Converter<Boolean>() {
      @Override
      public Boolean convert(String value) {
        value = value.toLowerCase();
        if ("true".equals(value) || "1".equals(value) || "yes".equals(value) || "y".equals(value))
          return true;
        return false;
      }
    };

    Converter<Double> doubleConverter = new Converter<Double>() {
      @Override
      public Double convert(String value) {
        try {
          return Double.parseDouble(value);
        } catch (NumberFormatException e) {
          return 0.0;
        }
      }
    };

    T convert(String value);
  }

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
    return XPathFactory.newInstance().newXPath().compile(expression).evaluate(source);
  }

  /**
   * Finds the value for the XPath {@code expression} ending in text() as a boolean. Returns true
   * for "true", "1", "yes", "y", else returns false.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public boolean findValueAsBoolean(String expression) throws XPathExpressionException {
    return Converter.booleanConverter.convert(findValue(expression).toLowerCase());
  }

  /**
   * Finds the value for the XPath {@code expression} ending in text() as a double. Returns 0 if
   * value cannot be parsed to a double.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public double findValueAsDouble(String expression) throws XPathExpressionException {
    return Converter.doubleConverter.convert(findValue(expression));
  }

  /**
   * Finds the value for the XPath {@code expression} ending in text() as an integer. Returns 0 if
   * value cannot be parsed to an int.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public int findValueAsInt(String expression) throws XPathExpressionException {
    return Converter.intConverter.convert(findValue(expression));
  }

  /**
   * Finds the values for the XPath {@code expression} ending in text().
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<String> findValues(String expression) throws XPathExpressionException {
    return findValuesInternal(expression, null);
  }

  /**
   * Finds the values for the XPath {@code expression} ending in text() as booleans. Returns true
   * for "true", "1", "yes", "y", else returns false.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<Boolean> findValuesAsBooleans(String expression) throws XPathExpressionException {
    return findValuesInternal(expression, Converter.booleanConverter);
  }

  /**
   * Finds the values for the XPath {@code expression} ending in text() as doubles. Returns 0 for
   * values that cannot be parsed to a double.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<Double> findValuesAsDoubles(String expression) throws XPathExpressionException {
    return findValuesInternal(expression, Converter.doubleConverter);
  }

  /**
   * Finds the values for the XPath {@code expression} ending in text() as integers. Returns 0 for
   * values that cannot be parsed to an int.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<Integer> findValuesAsInts(String expression) throws XPathExpressionException {
    return findValuesInternal(expression, Converter.intConverter);
  }

  /**
   * Finds the values for the XPath {@code expression} ending in text().
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  @SuppressWarnings("unchecked")
  public <V> List<V> findValuesInternal(String expression, Converter<V> converter)
      throws XPathExpressionException {
    XPathExpression expr = XPathFactory.newInstance().newXPath().compile(expression);
    NodeList nodeList = (NodeList) expr.evaluate(source, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      return Collections.emptyList();

    List<V> values = new ArrayList<V>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.TEXT_NODE)
        values.add(converter == null ? (V) node.getNodeValue()
            : converter.convert(node.getNodeValue()));
    }

    return values;
  }
}
