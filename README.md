# Xsylum

*XML parsing for the sane.*

## Introduction

Stuck with legacy services that still use XML? Suffering from head explosion at the sight of XML parser APIs? Yea, me too. Xsylum provides a sane API on top of the JDK's XML parser API.

**Note:** This thing is not fully featured and doesn't support much DOM manpulation. It's mostly intended for read operations, which keeps the overhead low'ish.

## Setup

`mvn install` (for now, unless anyone else cares enough about XML to send this thing to central)

## Usage

```
XmlElement element = Xsylum.xmlElementFor(xmlFile);
List<XmlElement> children = element.children();
List<XmlElement> books = element.get("books");

XmlElement book = books.get(0);
Map<String, String> attrs = book.attributes();
String value = book.value();
```

## License

Copyright 2014 Jonathan Halterman - Released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).