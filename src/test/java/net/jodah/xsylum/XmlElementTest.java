package net.jodah.xsylum;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class XmlElementTest {
  XmlElement element;

  @BeforeClass
  protected void beforeClass() throws Exception {
    element = Xsylum.elementFor(XmlElementTest.class.getResourceAsStream("document.xml"));
  }

  public void shouldGetAsText() {
    assertEquals(element.get("book").get("author").value(), "Gambardella, Matthew");
  }

  public void shouldGetAsDouble() {
    assertEquals(element.get("book").get("price").valueAsDouble(), 44.95);
  }

  public void shouldGetNodesByName() {
    List<XmlElement> books = element.getAll("book");
    assertEquals(books.size(), 5);
    for (XmlElement b : books)
      assertEquals(b.name(), "book");
    XmlElement author = books.get(0).get("author");
    assertEquals(author.value(), "Gambardella, Matthew");
  }

  public void shouldGetNodeText() {
    assertEquals(element.get("book").get("author").value(), "Gambardella, Matthew");
  }

  public void shouldGetAttributes() throws Exception {
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("id", "bk101");
    expected.put("subid", "a");
    assertEquals(element.get("book").attributes(), expected);
  }

  public void shouldGetChildren() {
    List<XmlElement> children = element.children();
    assertEquals(children.size(), 5);
    for (XmlElement c : children)
      assertEquals(c.name(), "book");
    assertEquals(children.get(0).children().size(), 6);
  }

  public void shouldFindValue() throws Exception {
    XmlElement book = element.get("book");
    assertEquals(book.value(".//author/text()"), "Gambardella, Matthew");
    assertEquals(book.value("./author/text()"), "Gambardella, Matthew");
  }

  public void shouldFind() throws Exception {
    XmlElement book = element.get("book");
    assertEquals(book.find(".//author").value(), "Gambardella, Matthew");
    assertEquals(book.find("./author").value(), "Gambardella, Matthew");
  }
}
