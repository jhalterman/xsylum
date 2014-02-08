package net.jodah.xsylum;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

@Test
public abstract class XmlSearchableTest {
  abstract XmlSearchable<?> searchable();

  public void shouldGet() {
    assertEquals(searchable().get("book").get("author").value(), "Gambardella, Matthew");
  }

  public void shouldGetAll() {
    List<XmlElement> books = searchable().getAll("book");
    assertEquals(books.size(), 5);
    for (XmlElement b : books)
      assertEquals(b.name(), "book");
    XmlElement author = books.get(0).get("author");
    assertEquals(author.value(), "Gambardella, Matthew");
  }
}
