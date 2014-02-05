package net.jodah.xsylum;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class XmlDocumentTest {
  XmlDocument document;

  @BeforeClass
  protected void beforeClass() throws Exception {
    document = Xsylum.documentFor(XmlDocumentTest.class.getResourceAsStream("document.xml"));
  }

  public void shouldFindValue() throws Exception {
    assertEquals(document.findValue("//author/text()"), "Gambardella, Matthew");
    assertEquals(document.findValue("/catalog/book[2]/author/text()"), "Ralls, Kim");
  }

  public void shouldFindValues() throws Exception {
    List<String> expected = Arrays.asList("Gambardella, Matthew", "Ralls, Kim", "Corets, Eva",
        "Corets, Eva", "Corets, Eva");
    assertEquals(document.findValues("//author/text()"), expected);
    assertEquals(document.findValues("/catalog/book/author/text()"), expected);
    assertEquals(
        document.findValues("/catalog/book[1 <= position() and position() <= 2]/author/text()"),
        Arrays.asList("Gambardella, Matthew", "Ralls, Kim"));
  }

  public void shouldFindValuesAsDoubles() throws Exception {
    List<Double> expected = Arrays.asList(44.95, 5.95, 5.95, 5.95, 5.95);
    assertEquals(document.findValuesAsDoubles("//price/text()"), expected);
  }

  public void shouldFind() throws Exception {
    assertEquals(document.find("//author").value(), "Gambardella, Matthew");
    assertEquals(document.find("/catalog/book[2]/author").value(), "Ralls, Kim");
  }

  public void shouldFindAll() throws Exception {
    List<XmlElement> authors = document.findAll("//author");
    assertEquals(authors.size(), 5);
    assertEquals(authors.get(0).value(), "Gambardella, Matthew");
    assertEquals(authors.get(1).value(), "Ralls, Kim");
  }
}
