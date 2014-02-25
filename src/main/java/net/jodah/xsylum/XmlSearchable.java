package net.jodah.xsylum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XmlSearchable<T> {
  protected final T source;

  static interface Converter<V> {
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

    Converter<Long> longConverter = new Converter<Long>() {
      @Override
      public Long convert(String value) {
        try {
          return Long.parseLong(value);
        } catch (NumberFormatException e) {
          return 0L;
        }
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

    V convert(String value);
  };

  XmlSearchable(T source) {
    this.source = source;
  }

  static <V extends Enum<V>> Converter<V> enumConverterFor(final Class<V> targetType) {
    return new Converter<V>() {
      @Override
      public V convert(String value) {
        try {
          return Enum.valueOf(targetType, value);
        } catch (Exception e) {
          return null;
        }
      }
    };
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
   * Returns the first child element that matches the {@code tagName}, else null.
   */
  public abstract XmlElement get(String tagName);

  /**
   * Returns all child elements that match the {@code tagName} else empty List.
   */
  public abstract List<XmlElement> getAll(String tagName);

  /**
   * Finds the value for the XPath {@code expression}.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public String value(String expression) throws XPathExpressionException {
    return XPathFactory.newInstance().newXPath().compile(expression).evaluate(source);
  }

  /**
   * Finds the value for the XPath {@code expression} as a boolean. Returns true for "true", "1",
   * "yes", "y", else returns false.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public boolean valueAsBoolean(String expression) throws XPathExpressionException {
    return Converter.booleanConverter.convert(value(expression).toLowerCase());
  }

  /**
   * Finds the value for the XPath {@code expression} as a double. Returns 0 if value cannot be
   * parsed to a double.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public double valueAsDouble(String expression) throws XPathExpressionException {
    return Converter.doubleConverter.convert(value(expression));
  }

  /**
   * Finds the value for the XPath {@code expression} as an Enum of type {@code V}. Returns null if
   * value cannot be parsed to an Enum of type {@code V}.
   * 
   * @param <V> enum type
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public <V extends Enum<V>> V valueAsEnum(String expression, Class<V> targetEnum)
      throws XPathExpressionException {
    return enumConverterFor(targetEnum).convert(value(expression));
  }

  /**
   * Finds the value for the XPath {@code expression} as an integer. Returns 0 if value cannot be
   * parsed to an int.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public int valueAsInt(String expression) throws XPathExpressionException {
    return Converter.intConverter.convert(value(expression));
  }

  /**
   * Finds the value for the XPath {@code expression} as a long. Returns 0 if value cannot be parsed
   * to a long.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public long valueAsLong(String expression) throws XPathExpressionException {
    return Converter.longConverter.convert(value(expression));
  }

  /**
   * Finds the values for the XPath {@code expression}.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<String> values(String expression) throws XPathExpressionException {
    return valuesInternal(expression, null);
  }

  /**
   * Finds the values for the XPath {@code expression} as booleans. Returns true for "true", "1",
   * "yes", "y", else returns false.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<Boolean> valuesAsBoolean(String expression) throws XPathExpressionException {
    return valuesInternal(expression, Converter.booleanConverter);
  }

  /**
   * Finds the values for the XPath {@code expression} as doubles. Returns 0 for values that cannot
   * be parsed to a double.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<Double> valuesAsDouble(String expression) throws XPathExpressionException {
    return valuesInternal(expression, Converter.doubleConverter);
  }

  /**
   * Finds the values for the XPath {@code expression} as Enums of type {@code V}. Returns null for
   * values that cannot be parsed to an Enum of type {@code V}.
   * 
   * @param <V> enum type
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public <V extends Enum<V>> List<V> valuesAsEnum(String expression, Class<V> targetEnum)
      throws XPathExpressionException {
    return valuesInternal(expression, enumConverterFor(targetEnum));
  }

  /**
   * Finds the values for the XPath {@code expression} as integers. Returns 0 for values that cannot
   * be parsed to an int.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<Integer> valuesAsInt(String expression) throws XPathExpressionException {
    return valuesInternal(expression, Converter.intConverter);
  }

  /**
   * Finds the values for the XPath {@code expression} ending in text() as longs. Returns 0 for
   * values that cannot be parsed to a long.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  public List<Long> valuesAsLong(String expression) throws XPathExpressionException {
    return valuesInternal(expression, Converter.longConverter);
  }

  /**
   * Finds the values for the XPath {@code expression}.
   * 
   * @throws XPathExpressionException if the {@code expression} is invalid
   */
  @SuppressWarnings("unchecked")
  <V> List<V> valuesInternal(String expression, Converter<V> converter)
      throws XPathExpressionException {
    XPathExpression expr = XPathFactory.newInstance().newXPath().compile(expression);
    NodeList nodeList = (NodeList) expr.evaluate(source, XPathConstants.NODESET);
    if (nodeList.getLength() == 0)
      return Collections.emptyList();

    List<V> values = new ArrayList<V>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      String value = null;
      if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.ATTRIBUTE_NODE)
        value = node.getNodeValue();
      else if (node.getNodeType() == Node.CDATA_SECTION_NODE)
        value = ((CharacterData) node).getData();

      // Convert value
      if (value != null)
        values.add(converter == null ? (V) value : converter.convert(value));
    }

    return values;
  }
}
