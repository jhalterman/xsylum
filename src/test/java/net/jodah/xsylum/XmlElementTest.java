package net.jodah.xsylum;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class XmlElementTest extends XmlSearchableTest {
  XmlElement element;

  @BeforeClass
  protected void beforeClass() throws Exception {
    element = Xsylum.elementFor(XmlElementTest.class.getResourceAsStream("document.xml"));
  }

  @Override
  XmlSearchable<?> searchable() {
    return element;
  }

  public void shouldGetAsText() {
    assertEquals(element.get("book").get("author").value(), "Gambardella, Matthew");
  }

  public void shouldGetAsDouble() {
    assertEquals(element.get("book").get("price").valueAsDouble(), 44.95);
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
    assertEquals(children.get(0).children().size(), 8);
  }

  public void shouldFindValue() throws Exception {
    XmlElement book = element.get("book");
    assertEquals(book.value(".//author/text()"), "Gambardella, Matthew");
    assertEquals(book.value("./author/text()"), "Gambardella, Matthew");
  }

  public void shouldFindCDataValue() throws Exception {
    assertEquals(element.find("//link").value(),
        "http://www.amazon.com/XML-Developers-Guide-Fabio-Arciniegas/dp/0072126485");
    assertEquals(element.find("/catalog/book[2]/link").value(),
        "http://www.amazon.com/PowerShell-Deep-Dives-Jeffery-Hicks/dp/1617291315");
  }

  public void shouldFindValues() throws Exception {
    List<String> expected = Arrays.asList("Ralls, Kim", "Bar, Foo");
    XmlElement book = element.find("//book[2]");
    assertEquals(book.values(".//author/text()"), expected);
  }

  public void shouldFind() throws Exception {
    XmlElement book = element.get("book");
    assertEquals(book.find(".//author").value(), "Gambardella, Matthew");
    assertEquals(book.find("./author").value(), "Gambardella, Matthew");
  }

  public void shouldGetValueAsEnum() throws Exception {
    BookType bookType = element.get("book").get("type").valueAsEnum(BookType.class);
    assertEquals(bookType, BookType.hardcover);
  }
}
