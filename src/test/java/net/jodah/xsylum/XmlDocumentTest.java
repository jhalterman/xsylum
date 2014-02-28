package net.jodah.xsylum;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class XmlDocumentTest extends XmlSearchableTest {
  XmlDocument document;

  @BeforeClass
  protected void beforeClass() throws Exception {
    document = Xsylum.documentFor(XmlDocumentTest.class.getResourceAsStream("document.xml"));
  }

  @Override
  XmlSearchable<?> searchable() {
    return document;
  }

  public void shouldFindValue() throws Exception {
    assertEquals(document.value("//author/text()"), "Gambardella, Matthew");
    assertEquals(document.value("/catalog/book[2]/author/text()"), "Ralls, Kim");
  }

  public void shouldFindCDataValue() throws Exception {
    assertEquals(document.value("//link/text()"),
        "http://www.amazon.com/XML-Developers-Guide-Fabio-Arciniegas/dp/0072126485");
    assertEquals(document.value("/catalog/book[2]/link/text()"),
        "http://www.amazon.com/PowerShell-Deep-Dives-Jeffery-Hicks/dp/1617291315");
  }

  public void shouldFindValues() throws Exception {
    List<String> expected = Arrays.asList("Gambardella, Matthew", "Ralls, Kim", "Bar, Foo",
        "Corets, Eva", "Corets, Eva", "Corets, Eva");
    assertEquals(document.values("//author/text()"), expected);
    assertEquals(document.values("/catalog/book/author/text()"), expected);
    assertEquals(
        document.values("/catalog/book[1 <= position() and position() <= 2]/author/text()"),
        Arrays.asList("Gambardella, Matthew", "Ralls, Kim", "Bar, Foo"));
    assertEquals(document.values("//book/@subid"), Arrays.asList("a", "b", "c", "d", "e"));
  }

  public void shouldFindCDataValues() throws Exception {
    List<String> expected = Arrays.asList(
        "http://www.amazon.com/XML-Developers-Guide-Fabio-Arciniegas/dp/0072126485",
        "http://www.amazon.com/PowerShell-Deep-Dives-Jeffery-Hicks/dp/1617291315",
        "http://www.amazon.com/Learn-Windows-PowerShell-Month-Lunches/dp/1617291080",
        "http://www.amazon.com/Windows-Server-2012-Inside-Out/dp/0735666318",
        "http://www.amazon.com/Group-Policy-Fundamentals-Security-Managed/dp/1118289404");
    assertEquals(document.values("//link/text()"), expected);
    assertEquals(document.values("/catalog/book/link/text()"), expected);
    assertEquals(document.values("/catalog/book[1 <= position() and position() <= 2]/link/text()"),
        Arrays.asList("http://www.amazon.com/XML-Developers-Guide-Fabio-Arciniegas/dp/0072126485",
            "http://www.amazon.com/PowerShell-Deep-Dives-Jeffery-Hicks/dp/1617291315"));
  }

  public void shouldFindValuesAsDoubles() throws Exception {
    List<Double> expected = Arrays.asList(44.95, 5.95, 5.95, 5.95, 5.95);
    assertEquals(document.valuesAsDouble("//price/text()"), expected);
  }

  public void shouldFind() throws Exception {
    assertEquals(document.find("//author").value(), "Gambardella, Matthew");
    assertEquals(document.find("/catalog/book[2]/author").value(), "Ralls, Kim");
  }

  public void shouldFindAll() throws Exception {
    List<XmlElement> authors = document.findAll("//author");
    assertEquals(authors.size(), 6);
    assertEquals(authors.get(0).value(), "Gambardella, Matthew");
    assertEquals(authors.get(1).value(), "Ralls, Kim");
  }

  public void shouldFindWithXpath() throws Exception {
    assertEquals(document.value("//book[@id='bk103']/author/text()"), "Corets, Eva");
  }
}
