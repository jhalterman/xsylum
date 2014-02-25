package net.jodah.xsylum;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
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

  enum SubId {
    a, b, c, d, e;
  }

  enum BookType {
    hardcover, paperback
  }

  public void shouldGetAttributeAsEnum() throws Exception {
    assertEquals(searchable().get("book").attributeAsEnum("subid", SubId.class), SubId.a);
  }

  public void shouldGetValueAsEnum() throws Exception {
    assertEquals(searchable().get("book").valueAsEnum("type", BookType.class), BookType.hardcover);
  }

  public void shouldGetValuesAsEnum() throws Exception {
    List<BookType> expected = Arrays.asList(BookType.hardcover, BookType.hardcover,
        BookType.paperback, BookType.paperback, BookType.paperback);
    assertEquals(searchable().valuesAsEnum(".//book/type/text()", BookType.class), expected);
  }
}
