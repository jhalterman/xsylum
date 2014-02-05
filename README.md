# Xsylum

*XML parsing for the sane.*

## Introduction

Why XML? Why indeed. 

But let's say you're working with some legacy services where XML is the thing. And you discover that the available XML parser APIs are bad. Really bad. What do you? Maybe you use Xsylum, that's what.

Xsylum is a dead simple simple wrapper around the Java XML parser API. It's mostly intended for read operations, opting to traverse nodes on demand (when searching children) instead of eagerly building a separate DOM.

## Setup

`mvn install` (for now, unless anyone else cares enough about XML to send this thing to central)

## Usage

```java
XmlElement element = Xsylum.elementFor(xmlFile);
List<XmlElement> children = element.children();
List<XmlElement> books = element.getAll("book");
XmlElement book = element.get("book");
Map<String, String> attrs = book.attributes();
String value = book.value();
```

## License

Copyright 2014 Jonathan Halterman - Released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).