// Assignment 7
// Melagrano Alex
// alex3232
// Jonathan Northcott
// nrthcoj

import tester.Tester;

// Interface for IListVisitor functions
interface IListVisitor<A, R> {
    // Visit method for the Cons type
    IList<R> visit(Cons<A> i);
    // Visit method for the Empty type
    IList<R> visit(Empty<A> i);
}

// Map Visitor for lists
class MapVisitor<A, R> implements IListVisitor<A, R> {
    IFunc<A, R> func;

    MapVisitor(IFunc<A, R> func) {
        this.func = func;
    }

    /*Template
     * Fields:
     * IFunc<A, R> func
     * 
     * Methods:
     * Cons<R> visit( ... )
     * Empty<R> visit( ...)
     * 
     * Methods on Fields:
     * this.func.apply( ... )
     * */

    // Visit method for Cons type
    public IList<R> visit(Cons<A> i) {
        return new Cons<R>(this.func.apply(i.first), i.rest.accept(this));
    }

    // Visit method for Empty type
    public IList<R> visit(Empty<A> i) {
        return new Empty<R>();
    }
}

// Filter Visitor for lists
class FilterVisitor<A> implements IListVisitor<A, A> {
    IFunc<A, Boolean> func;

    FilterVisitor(IFunc<A, Boolean> func) {
        this.func = func;
    }  

    /*Template
     * Fields:
     * IFunc<A, Boolean> func
     * 
     * Methods:
     * Cons<R> visit( ... )
     * Empty<R> visit( ...)
     * 
     * Methods on Fields:
     * this.func.apply( ... )
     * */

    // Visit method for Cons type
    public IList<A> visit(Cons<A> i) {

        if (this.func.apply(i.first)) {
            return new Cons<A>(i.first, i.rest.accept(this));
        }
        else {
            return i.rest.accept(this);
        }
    }

    // Visit method for Empty type
    public IList<A> visit(Empty<A> i) {
        return new Empty<A>();
    }
}

//Class for books
class Book {
    String title;

    Book(String title) {
        this.title = title;
    }

    /*Template
     * Fields:
     * String title
     */
}

// Interface for function objects
interface IFunc<A, R> {
    R apply(A first);
}

// IFunc that returns the Book title
class BookTitle implements IFunc<Book, String> {
    /*Template
     * Methods:
     * String apply( ... )
     */
    // Applies this IFunc to the given object
    public String apply(Book first) {
        return first.title;
    }
}

// IFunc that checks to see if given book has the same title as this title
class SameBookTitle implements IFunc<Book, Boolean> {
    String title;

    SameBookTitle(String title) {
        this.title = title;
    }

    /*Template
     * Fields:
     * String title
     * 
     * Methods:
     * boolean apply( ... )
     */

    // Applies this IFunc to the given object
    public Boolean apply(Book b) {
        return b.title.equals(this.title);
    }
}

// IFunc that converts an double to a String
class DblToString implements IFunc<Double, String> {
    /*Template
     * Fields:
     * String title
     * 
     * Methods:
     * String apply( ... )  
     */

    public String apply(Double d) {
        return Double.toString(d);
    }
}

//IFunc that checks to see if given double is less than this double
class LessThanGiven implements IFunc<Double, Boolean> {
    Double given;

    LessThanGiven(Double given) {
        this.given = given;
    }
    /*Template
     * Fields:
     * Double given
     * 
     * Methods:
     * boolean apply( ... )
     */
    public Boolean apply(Double d) {
        return d < this.given;
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

    /*
     * Template:
     * Fields:
     * T First
     * IList<T> rest
     * 
     * Methods:
    IList<T> append( ... );
    IList<R> map( ... );
    IList<T> filter( ... );
    IList<R> accept( ... );
     * 
    Methods on Fields:
    IList<T> this.rest.append( ... );
    IList<R> this.rest.map( ... );
    IList<T> this.rest.filter( ... );
    IList<R> this.rest.accept( ... );*/

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
    /* 
     * Template:
     * Methods:
   IList<T> append( ... );
   IList<R> map( ... );
   IList<T> filter( ... );
   IList<R> accept( ... );
     */
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

// Class for tests
class ExamplesVisitors {
    // Examples of books
    Book hp = new Book("Harry Potter");
    Book b = new Book("Bible");
    // Examples of ILists of Books
    IList<Book> mt = new Empty<Book>();
    IList<Book> bl1 = new Cons<Book>(this.hp, new Cons<Book>(this.b, this.mt));  
    IList<Book> bl2 = new Cons<Book>(this.b, bl1);
    // Examples of ILists of Doubles
    IList<Double> mtd = new Empty<Double>();
    IList<Double> dl1 = new Cons<Double>(.11, new Cons<Double>(.69, mtd));
    IList<Double> dl2 = new Cons<Double>(.666, this.dl1);
    // Examples of IFuncs for Doubles
    IFunc<Double, String> dbl2String = new DblToString();
    IFunc<Double, Boolean> lessThanGiven = new LessThanGiven(.69);
    // Examples of IFuncs for Books
    IFunc<Book, String> bookTitle = new BookTitle();
    IFunc<Book, Boolean> hpots = new SameBookTitle("Harry Potter");
    IFunc<Book, Boolean> bible = new SameBookTitle("Bible");
    // Examples of MapVisitors
    MapVisitor<Book, String> mapBook2TitleVisitor = new MapVisitor<Book, String>(bookTitle);
    MapVisitor<Double, String> mapDblToStringVisitor = new MapVisitor<Double, String>(dbl2String);
    // Examples of FilterVisitors
    FilterVisitor<Book> filterBookTitleVisitorHP = new FilterVisitor<Book>(hpots);
    FilterVisitor<Book> filterBookTitleVisitorBib = new FilterVisitor<Book>(bible);
    FilterVisitor<Double> filterLessThanGiven = new FilterVisitor<Double>(lessThanGiven);

    // Tests the non visiting map
    boolean testMap(Tester t) {
        return t.checkExpect(this.mt.map(new BookTitle()), new Empty<String>()) &&
                t.checkExpect(this.bl1.map(new BookTitle()), 
                        new Cons<String>("Harry Potter", 
                                new Cons<String>("Bible", new Empty<String>())));  
    }

    // Tests the non visiting filter
    boolean testFilter(Tester t) {
        return t.checkExpect(this.mt.filter(new SameBookTitle("Harry Potter")), 
                new Empty<Book>()) &&
                t.checkExpect(this.bl2.filter(new SameBookTitle("Bible")), 
                        new Cons<Book>(this.b, new Cons<Book>(this.b, this.mt)));
    }

    // Tests the visiting Map
    void testVisitorMap(Tester t) {
        t.checkExpect(bl1.map(bookTitle), bl1.accept(mapBook2TitleVisitor));
        t.checkExpect(mt.map(bookTitle), mt.accept(mapBook2TitleVisitor));
        t.checkExpect(dl1.map(dbl2String), dl1.accept(mapDblToStringVisitor));
        t.checkExpect(mtd.map(dbl2String), mtd.accept(mapDblToStringVisitor));
    }

    // Tests the visiting filter
    void testVisitorFilter(Tester t) {
        t.checkExpect(mt.filter(hpots), mt.accept(filterBookTitleVisitorHP));
        t.checkExpect(bl2.filter(bible), bl2.accept(filterBookTitleVisitorBib));
        t.checkExpect(mtd.filter(lessThanGiven), mtd.accept(filterLessThanGiven));
        t.checkExpect(dl2.filter(lessThanGiven), dl2.accept(filterLessThanGiven));
    }
}