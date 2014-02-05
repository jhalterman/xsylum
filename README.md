# Xsylum

*XML parsing and DOM traversal for the sane.*

## Introduction

Why XML? Why indeed. 

But let's say you're working with some legacy services where XML is the thing. And you discover that the available XML parser APIs are bad. Really bad. What do you? Maybe you use Xsylum, that's what.

Xsylum is a dead simple simple wrapper around the Java XML parser API. It's mostly intended for read operations, opting to traverse nodes on demand (when searching children) instead of eagerly building a separate DOM.

## Setup

`mvn install` (for now, unless anyone else cares enough about XML to send this thing to central)

## Usage

```java
// Parse some XML to a document
XmlDocument document = Xsylum documentFor(xmlFile);

// Access the root element for the document
XmlElement element = document.root();

// Parse some XML to a root element
XmlElement element = Xsylum.elementFor(xmlFile);

// Access element attributes
Map<String, String> attributes = element.attributes();

// Access child elements
List<XmlElement> children = element.children();

// Search child elements
List<XmlElement> books = element.getAll("book");
XmlElement book = element.get("book");

// Access the value of an element
String value = book.value();
int value = book.get("copies-sold").valueAsInt();

// Find child elements for XPath expressions
XmlElement author = document.find("/catalog/book[2]/author");
List<XmlElement> authors = XmlElement author = document.findAll("//author");

// Find values for XPath expressions
String author = document.findValue("//author/text()");
int copiesSold = book.findValueAsInt(".//copies-sold/text()");
List<String> authors = document.findValues("/catalog/book/author/text()");
List<Integer> allCopiesSold = document.findValuesAsInts("/catalog/book/copies-sold/text()");
```

## License

Copyright 2014 Jonathan Halterman - Released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).