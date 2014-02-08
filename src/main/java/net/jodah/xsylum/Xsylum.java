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

  public static XmlDocument documentFor(byte[] xml) throws XsylumException {
    return documentFor(new ByteArrayInputStream(xml));
  }

  public static XmlDocument documentFor(File file) throws XsylumException {
    return new XmlDocument(documentForInternal(file));
  }

  public static XmlDocument documentFor(InputStream inputStream) throws XsylumException {
    return new XmlDocument(documentForInternal(inputStream));
  }

  public static XmlDocument documentFor(String xml) throws XsylumException {
    return documentFor(new ByteArrayInputStream(xml.getBytes()));
  }

  public static XmlElement elementFor(byte[] xml) throws XsylumException {
    return new XmlElement(documentForInternal(new ByteArrayInputStream(xml)).getDocumentElement());
  }

  public static XmlElement elementFor(File file) throws XsylumException, IOException {
    return new XmlElement(documentForInternal(file).getDocumentElement());
  }

  public static XmlElement elementFor(InputStream inputStream) throws XsylumException {
    return new XmlElement(documentForInternal(inputStream).getDocumentElement());
  }

  public static XmlElement elementFor(String xml) throws XsylumException {
    return new XmlElement(documentForInternal(
        new InputSource(new StringReader(xml)).getByteStream()).getDocumentElement());
  }

  private static DocumentBuilder createBuilderFactory() throws ParserConfigurationException {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setValidating(false);
    return documentBuilderFactory.newDocumentBuilder();
  }

  private static Document documentForInternal(File file) throws XsylumException {
    try {
      return createBuilderFactory().parse(file);
    } catch (Exception e) {
      throw new XsylumException(e, "Failed to create document from file %s", file.getName());
    }
  }

  private static Document documentForInternal(InputStream inputStream) throws XsylumException {
    try {
      return createBuilderFactory().parse(inputStream);
    } catch (Exception e) {
      throw new XsylumException(e, "Failed to create document from InputStream");
    }
  }
}
