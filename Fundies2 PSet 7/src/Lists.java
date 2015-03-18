// Assignment 7
// Melagrano Alex
// alex3232
// Jonathan Northcott
// nrthcoj

import tester.Tester;

// Interface for IListVisitor functions
interface IListVisitor<A, R> {
    // Visit method for the Cons type
    Cons<R> visit(Cons<A> i);
    // Visit method for the Empty type
    Empty<R> visit(Empty<A> i);
}

// Map Visitor for lists
class MapVisitor<A, R> implements IListVisitor<A, R> {
    IFunc<A, R> func;

    MapVisitor(IFunc<A, R> func) {
        this.func = func;
    }  

    // Visit method for Cons type
    public Cons<R> visit(Cons<A> i) {
        return new Cons<R>(this.func.apply(i.first), i.rest.accept(this));
    }

    // Visit method for Empty type
    public Empty<R> visit(Empty<A> i) {
        return new Empty<R>();
    }
}

// Filter Visitor for lists
class FilterVisitor<A> implements IListVisitor<A, A> {
    IFunc<A, Boolean> func;

    FilterVisitor(IFunc<A, Boolean> func) {
        this.func = func;
    }  

    // Visit method for Cons type
    public Cons<A> visit(Cons<A> i) {
        
        if (this.func.apply(i.first)) {
            return new Cons<A>(i.first, i.rest.accept(this));
        }
        else {
            return (Cons<A>) i.rest.accept(this);
        }
    }

    // Visit method for Empty type
    public Empty<A> visit(Empty<A> i) {
        return new Empty<A>();
    }
}

//Class for books
class Book {
    String title;

    Book(String title) {
        this.title = title;
    }
}

// Interface for function objects
interface IFunc<A, R> {
    R apply(A first);
}

class BookTitle implements IFunc<Book, String> {
    public String apply(Book first) {
        return first.title;
    }
}

class SameBookTitle implements IFunc<Book, Boolean> {
    String title;

    SameBookTitle(String title) {
        this.title = title;
    }

    public Boolean apply(Book b) {
        return b.title.equals(this.title);
    }
}

// Represents a list
interface IList<T> {
    // Appends this IList to given list
    IList<T> append(IList<T> list);
    // Map given function object over this list
    <R> IList<R> map(IFunc<T, R> func);
    // Filters this list using given function object
    IList<T> filter(IFunc<T, Boolean> func);
    // Applies the visitor to the IList
    <R> IList<R> accept(IListVisitor<T, R> func);
}

// Cons class
class Cons<T> implements IList<T> {
    T first;
    IList<T> rest;

    Cons(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    // Appends given list to this list
    public IList<T> append(IList<T> list) {
        return new Cons<T>(this.first, this.rest.append(list));
    }

    // Map given function object over this list
    public <R> IList<R> map(IFunc<T, R> func) {
        return new Cons<R>(func.apply(this.first), this.rest.map(func));
    }

    // Filters this list using given function object
    public IList<T> filter(IFunc<T, Boolean> func) {
        if (func.apply(this.first)) {
            return new Cons<T>(this.first, this.rest.filter(func));
        }
        else {
            return this.rest.filter(func);
        }
    }

    // applies the visitor to the list
    public <R> IList<R> accept(IListVisitor<T, R> func) {
        return func.visit(this);
    }
}


// Empty case for Cons
class Empty<T> implements IList<T> {

    // Appends given list to this empty list
    public IList<T> append(IList<T> list) {
        return list;
    }

    // Map given function object over this empty list
    public <R> IList<R> map(IFunc<T, R> func) {
        return new Empty<R>();
    }

    // Filters this empty list using given function object
    public IList<T> filter(IFunc<T, Boolean> func) {
        return new Empty<T>();
    }

    // applies the visitor to the constant
    public <R> IList<R> accept(IListVisitor<T, R> func) {
        return func.visit(this);
    }
}


class ExamplesVisitors {
    Book hp = new Book("Harry Potter");
    Book b = new Book("Bible");
    IList<Book> mt = new Empty<Book>();
    IList<Book> bl1 = new Cons<Book>(this.hp, new Cons<Book>(this.b, this.mt));  
    IList<Book> bl2 = new Cons<Book>(this.b, bl1);
    IFunc<Book, String> bookTitle = new BookTitle();
    IFunc<Book, Boolean> hpots = new SameBookTitle("Harry Potter");
    IFunc<Book, Boolean> bible = new SameBookTitle("Bible");
    MapVisitor<Book, String> mapBook2TitleVisitor = new MapVisitor<Book, String>(bookTitle);
    FilterVisitor<Book> filterBookTitleVisitorHP = new FilterVisitor<Book>(hpots);
    FilterVisitor<Book> filterBookTitleVisitorBib = new FilterVisitor<Book>(bible);

    boolean testMap(Tester t) {
        return t.checkExpect(this.mt.map(new BookTitle()), new Empty<String>()) &&
                t.checkExpect(this.bl1.map(new BookTitle()), 
                        new Cons<String>("Harry Potter", 
                                new Cons<String>("Bible", new Empty<String>())));  
    }

    boolean testFilter(Tester t) {
        return t.checkExpect(this.mt.filter(new SameBookTitle("Harry Potter")), 
                new Empty<Book>()) &&
                t.checkExpect(this.bl2.filter(new SameBookTitle("Bible")), 
                        new Cons<Book>(this.b, new Cons<Book>(this.b, this.mt)));
    }

    void testVisitorMap(Tester t) {
        t.checkExpect(bl1.map(bookTitle), bl1.accept(mapBook2TitleVisitor));
        t.checkExpect(mt.map(bookTitle), mt.accept(mapBook2TitleVisitor));
    }
    
    void testVisitorFilter(Tester t) {
        t.checkExpect(mt.filter(hpots), mt.accept(filterBookTitleVisitorHP));
        t.checkExpect(bl2.filter(bible), bl2.accept(filterBookTitleVisitorBib));
    }
}