# lambico [![Build Status](https://travis-ci.org/lambico/lambico.svg?branch=master)](https://travis-ci.org/lambico/lambico)

With Lambico you can add DAOs to your entities just writing only their interfaces, with no implementation.

For example:

```java
@Dao(entity=Person.class)
public interface PersonDao extends GenericDao<Person, Long> {
}
```

You don't need to write anything else, your DAO will be instrumented at runtime with all the methods you can expect: read, create, delete, store, findAll, etc.

If you need your specific query methods, you can simply add them to your interface. For example:

```java
@Dao(entity=Person.class)
public interface PersonDao extends GenericDao<Person, Long> {
  List<Person> findByFirstName(String firstName);
}
```

The `findByFirstName` method will be instrumented exactly as you expect, querying the Person entity for all instances having the `firstName` property equal to the passed parameter.

For more information, read the [Lambico Reference Guide](http://doc.lambico.org/reference/html/lambico-reference-guide.html).

# A complete example

For evaluating Lambico, You can try and examine a simple but complete example. At present the example is provided:

  1. from the [examples source code repository](http://code.google.com/p/lambico/source/checkout?repo=examples) (it's a Maven project)
  1. from the download area, as a [Maven project](http://lambico.googlecode.com/files/consoleSpringHibernate-1.0-SNAPSHOT-project.zip)
  1. from the download area, as an [Eclipse project](http://lambico.googlecode.com/files/consoleSpringHibernate-eclipse.zip)
  
Get the type of project you prefer, build it, and execute the `org.lambico.example.consolespringhibernate.app.UseBookDAO` class.

## Trying the example with Maven

  1. Checkout the example [from the repository](http://code.google.com/p/lambico/source/checkout?repo=examples), or get it [from the download area](http://lambico.googlecode.com/files/consoleSpringHibernate-1.0-SNAPSHOT-project.zip);
  1. go to the main directory of the project (where there is the `pom.xml` file);
  1. Build the project, typing:
    `mvn package`
  1. Run the application, typing:
    `java -jar target/consoleSpringHibernate-1.0-SNAPSHOT-executeMe.jar`

## Trying the example with Eclipse

  1. Get the example [from the download area](http://lambico.googlecode.com/files/consoleSpringHibernate-eclipse.zip);
  1. Import the project in the Eclipse workspace;
  1. Run the `org.lambico.example.consolespringhibernate.app.UseBookDAO` class as a Java application.

## What will the example do?

The example will populate an in-memory HSQLDB database with the data of some books. Then it will query the database for all books written by the "Lambico team", and it will print the result on the screen.

Following the main class of the example:

```java
package org.lambico.example.consolespringhibernate.app;

import java.util.List;
import javax.annotation.Resource;

import org.lambico.example.consolespringhibernate.dao.BookDao;
import org.lambico.example.consolespringhibernate.po.Book;
import org.lambico.example.consolespringhibernate.util.ApplicationContextHolder;

public class UseBookDAO {

    @Resource
    private BookDao bookDao;

    public UseBookDAO() {
        ApplicationContextHolder.autowireBeanProperties(this);
    }

    public static void main(String[] args) {
        UseBookDAO app = new UseBookDAO();
        Book myBook = new Book("Lambico Team", "Lambico: easy persistence");

        Book myBook2 = new Book("Mario Rossi", "My life");
        Book myBook3 = new Book("Lambico Team", "Lambico: tips & trick");
        Book myBook4 = new Book("The Hitchhiker's Guide to the Galaxy",
                "Douglas Adams", 320);

        app.bookDao.create(myBook);
        app.bookDao.create(myBook2);
        app.bookDao.create(myBook3);
        app.bookDao.create(myBook4);

        System.out.println("Searching for Lambico Team's books...");
        List<Book> myBooks = app.bookDao.findByAuthor("Lambico Team");
        System.out.println(myBooks.size()+" result(s) found!");
        for (Book book : myBooks) {
            System.out.println(book);
        }
    }
}
```

The expected output is:

```
... (some log lines)

Searching for Lambico Team's books...
2 result(s) found!
Book{numPages=0 ; author=Lambico Team ; title=Lambico: easy persistence ; borrower=null}
Book{numPages=0 ; author=Lambico Team ; title=Lambico: tips & trick ; borrower=null}
```
