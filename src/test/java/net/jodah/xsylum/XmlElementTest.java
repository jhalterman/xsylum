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
    assertEquals(element.get("book").get(0).get("author").get(0).value(), "Gambardella, Matthew");
  }

  public void shouldGetAsDouble() {
    assertEquals(element.get("book").get(0).get("price").get(0).valueAsDouble(), 44.95);
  }

  public void shouldGetNodesByName() {
    List<XmlElement> books = element.get("book");
    assertEquals(books.size(), 5);
    for (XmlElement b : books)
      assertEquals(b.name(), "book");
    XmlElement author = books.get(0).get("author").get(0);
    assertEquals(author.value(), "Gambardella, Matthew");
  }

  public void shouldGetNodeText() {
    assertEquals(element.get("book").get(0).get("author").get(0).value(), "Gambardella, Matthew");
  }

  public void shouldGetAttributes() throws Exception {
    Map<String, String> expected = new HashMap<String, String>();
    expected.put("id", "bk101");
    expected.put("subid", "a");
    assertEquals(element.get("book").get(0).attributes(), expected);
  }

  public void shouldGetChildren() {
    List<XmlElement> children = element.children();
    assertEquals(children.size(), 5);
    for (XmlElement c : children)
      assertEquals(c.name(), "book");
    assertEquals(children.get(0).children().size(), 6);
  }
}
