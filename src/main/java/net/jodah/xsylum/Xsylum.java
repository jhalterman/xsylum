package net.jodah.xsylum;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * XML parsing for the sane.
 * 
 * @author Jonathan Halterman
 */
public final class Xsylum {
  private Xsylum() {
  }

  public static Document documentFor(byte[] xml) throws XsylumException {
    return documentFor(new ByteArrayInputStream(xml));
  }

  public static Document documentFor(File file) throws XsylumException {
    try {
      return createBuilderFactory().parse(file);
    } catch (Exception e) {
      throw new XsylumException(e, "Failed to create document from file %s", file.getName());
    }
  }

  public static Document documentFor(InputStream inputStream) throws XsylumException {
    try {
      return createBuilderFactory().parse(inputStream);
    } catch (Exception e) {
      throw new XsylumException(e, "Failed to create document from InputStream");
    }
  }

  public static Document documentFor(String xml) throws XsylumException {
    return documentFor(new InputSource(new StringReader(xml)).getByteStream());
  }

  public static XmlElement elementFor(byte[] xml) throws XsylumException {
    return new XmlElement(documentFor(xml).getDocumentElement());
  }

  public static XmlElement elementFor(File file) throws XsylumException, IOException {
    return new XmlElement(documentFor(file).getDocumentElement());
  }

  public static XmlElement elementFor(InputStream inputStream) throws XsylumException {
    return new XmlElement(documentFor(inputStream).getDocumentElement());
  }

  public static XmlElement elementFor(String xml) throws XsylumException {
    return new XmlElement(documentFor(xml).getDocumentElement());
  }

  private static DocumentBuilder createBuilderFactory() throws ParserConfigurationException {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setValidating(false);
    return documentBuilderFactory.newDocumentBuilder();
  }
}
