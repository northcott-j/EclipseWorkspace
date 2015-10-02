import tester.Tester;

class Book {
    String title;
    String author;
    int price;


    Book(String title, String author, int price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    /*
     * Fields:
     * String title
     * String author
     * int price
     * 
     * */

}

interface IList<T> {
    // this builds a tree from this list
    ABST<T> buildTree(ABST<T> tree);
    // is this list sorted
    boolean isSorted(IComparator<T> comp);
}

class Cons<T> implements IList<T> {
    T first;
    IList<T> rest;

    Cons(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }
    
    /*
     * Fields:
     * T first
     * IList<T> rest
     * 
     * Methods:
     * this.buildTree( ... ) --- ABST<T>
     * this.isSorted( ... ) --- boolean
     * 
     * Methods on Fields:
     * this.rest.buildTree(...) --- ABST<T>
     * this.rest.isSorted(...) --- boolean
     * */
    
    // Builds a tree from the list
    public ABST<T> buildTree(ABST<T> tree) {
        return tree; //STUB
    }
    // Checks to see if the list is sorted
    public boolean isSorted(IComparator<T> comp) {
        return true; //STUB
    }
}

class Empty<T> implements IList<T> {
    
    /* 
     * Methods:
     * this.buildTree( ... ) --- ABST<T>
     * this.isSorted( ... ) --- boolean
     */
    // Builds a tree from the list
    public ABST<T> buildTree(ABST<T> tree) {
        return tree; //STUB
    }
    // Checks to see if the list is sorted
    public boolean isSorted(IComparator<T> book) {
        return true; // STUB
    }
}

interface IComparator<T> {
    // compares two books
    int compare(T item1, T item2);
}

//compares two books by their title
class BooksByTitle implements IComparator<Book> {
    // compares two books
    public int compare(Book book1, Book book2) {
        return book1.title.compareTo(book2.title);
    }
}

//compares two books by their author
class BooksByAuthor implements IComparator<Book> {
    // compares two books
    public int compare(Book book1, Book book2) {
        return book1.author.compareTo(book2.author);
    }
}

//compares two books by their price
class BooksByPrice implements IComparator<Book> {
    // compares two books
    public int compare(Book book1, Book book2) {
        if (book1.price < book2.price) {
            return -1;
        } 
        else {
            if (book1.price == book2.price) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }
}

abstract class ABST<T> {
    IComparator<T> order;

    ABST(IComparator<T> order) {
        this.order = order;
    }
    /*
     * Fields
     * IComparator<T> order
     * 
     * Methods
     * this.insert() --- ABST<T>
     * this.getLeftmost() --- T
     * this.getRight() --- ABST<T>
     * this.sameDataHelp(...) --- boolean
     * this.sameData(...) --- boolean
     * this.sameTreeHelp(...) --- boolean
     * this.sameTree(...) --- boolean
     * this.buildList(...) --- IList<T>
     * */
    // Inserts given item into this ABST
    abstract ABST<T> insert(T item);
    // Gets the left most item
    abstract T getLeftmost();
    // Prunes getLeftmost item
    abstract ABST<T>  getRight();
    // Helps check sameData
    boolean sameDataHelp(Node<T> data) {
        return false;
    }
    // Checks to see if data is the same
    boolean sameData(ABST<T> data) {
        return false;
    }
    // Helps check sameTree
    boolean sameTreeHelp(Node<T> tree) {
        return true;
    }
    // Checks to see if trees are the same
    boolean sameTree(ABST<T> tree) {
        return false;
    }
    // Builds a list of books from tree
    abstract IList<T> buildList(IList<T> list);
    // Checks to see if the ABST is sorted
}


class Leaf<T> extends ABST<T> {

    Leaf(IComparator<T> order) {
        super(order);
    }
    /*
     * Fields
     * IComparator<T> order
     * 
     * Methods
     * this.insert() --- ABST<T>
     * this.getLeftmost() --- T
     * this.getRight() --- ABST<T>
     * this.sameDataHelp(...) --- boolean
     * this.sameData(...) --- boolean
     * this.sameTreeHelp(...) --- boolean
     * this.sameTree(...) --- boolean
     * this.buildList(...) --- IList<T>
     * */
    
    // Inserts given item into this ABST
    ABST<T> insert(T item) {
        return new Node<T>(this.order, item, this, this);
    }
    // Gets the left most item
    T getLeftmost() {
        throw new RuntimeException("No getLeftmost item of an empty tree");
    }

    // Prunes left most item
    ABST<T> getRight() {
        throw new RuntimeException("No right of an empty tree");
    }

    // Builds a list of books from tree
    IList<T> buildList(IList<T> list) {
        return new Empty<T>(); //STUB
    }
}

class Node<T> extends ABST<T> {
    T data;
    ABST<T> left;
    ABST<T> right;

    Node(IComparator<T> order, T data, ABST<T> left, ABST<T> right) {
        super(order);
        this.data = data;
        this.left = left;
        this.right = right;
    }
    
    /*
     * Fields
     * IComparator<T> order
     * T data
     * ABST<T> left
     * ABST<T> right
     * 
     * Methods
     * this.insert() --- ABST<T>
     * this.getLeftmost() --- T
     * this.getRight() --- ABST<T>
     * this.sameDataHelp(...) --- boolean
     * this.sameData(...) --- boolean
     * this.sameTreeHelp(...) --- boolean
     * this.sameTree(...) --- boolean
     * this.buildList(...) --- IList<T>
     * 
     * Methods on Fields
     * this.left.insert() --- ABST<T>
     * this.left.getLeftmost() --- T
     * this.left.getRight() --- ABST<T>
     * this.left.sameDataHelp(...) --- boolean
     * this.left.sameData(...) --- boolean
     * this.left.sameTreeHelp(...) --- boolean
     * this.left.sameTree(...) --- boolean
     * this.left.buildList(...) --- IList<T>
     * this.right.insert() --- ABST<T>
     * this.right.getLeftmost() --- T
     * this.right.getRight() --- ABST<T>
     * this.right.sameDataHelp(...) --- boolean
     * this.right.sameData(...) --- boolean
     * this.right.sameTreeHelp(...) --- boolean
     * this.right.sameTree(...) --- boolean
     * this.right.buildList(...) --- IList<T>
     * */

    // Inserts given item into this ABST
    ABST<T> insert(T item) {
        if (this.order.compare(this.data, item) < 0) {
            return new Node<T>(this.order, this.data, this.left, this.right.insert(item));
        } 
        else {
            if (this.order.compare(this.data, item) == 0) {
                return new Node<T>(this.order, this.data, this.left, this.right.insert(item));
            }
            else {
                return new Node<T>(this.order, this.data, this.left.insert(item), this.right);
            }
        }
    }

    // Gets the left most item
    T getLeftmost() {
        if (this.left instanceof Leaf) {
            return this.data;
        }
        else {
            return this.left.getLeftmost();
        }
    }

    // Prunes left most item
    ABST<T> getRight() {
        if (this.data.equals(this.getLeftmost())) {
            return this.right;
        }
        else {
            return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
        }
    }

    // Helps check sameData
    boolean sameDataHelp(Node<T> data) {
        return this.data.equals(data.data);
    }

    // Checks to see if data is the same
    boolean sameData(ABST<T> data) {
        return data.sameDataHelp(this);
    }

    // Helps check to see if trees are same
    boolean sameTreeHelp(Node<T> tree) {
        return this.order.equals(tree.order) &&
                this.data.equals(tree.data) &&
                this.left.sameTree(tree.left) &&
                this.right.sameTree(tree.right);
    }

    // Checks to see if the trees are the same
    boolean sameTree(ABST<T> tree) {
        return tree.sameTreeHelp(this);
    }

    // Builds a list of books from tree
    IList<T> buildList(IList<T> list) {
        return new Empty<T>(); //STUB
    }
}



class ExamplesABST {
    // examples of books
    Book hp = new Book("Harry Potter", "JKR", 20);
    Book b = new Book("Bible", "Jerry", 666);
    Book phy = new Book("Physics", "Professors", 1000);
    Book sog = new Book("Fifty Shades of Grey", "E L James", 69);
    Book babe = new Book("Babee", "A Pig", 20);
    Book hp2 = new Book("Harry Potter", "jkr", 16);
    IComparator<Book> title = new BooksByTitle();
    IComparator<Book> author = new BooksByAuthor();
    IComparator<Book> price = new BooksByPrice();
    // examples of leaves
    //ABST<Book> abst1 = new ABST(BooksByTitle<Book>);
    ABST<Book> lt = new Leaf<Book>(title);
    ABST<Book> la = new Leaf<Book>(author);
    ABST<Book> lp = new Leaf<Book>(price);
    // examples of nodes
    ABST<Book> t1 = new Node<Book>(title, hp, lt, lt);
    ABST<Book> t2 = new Node<Book>(title, sog, new Node<Book>(title, babe, lt, 
            new Node<Book>(title, b, lt, lt)),
            new Node<Book>(title, hp, lt, new Node<Book>(title, phy, lt, lt)));

    ABST<Book> a1 = new Node<Book>(author, hp, la, la);

    ABST<Book> p1 = new Node<Book>(price, hp, lp, lp);

    void testInsert(Tester t) {

        t.checkExpect(t1.insert(phy), new Node<Book>(title, hp, lt, 
                new Node<Book>(title, phy, lt, lt)));
        t.checkExpect(t1.insert(hp2), new Node<Book>(title, hp, lt, 
                new Node<Book>(title, hp2, lt, lt)));
        t.checkExpect(t1.insert(b), new Node<Book>(title, hp, 
                new Node<Book>(title, b, lt, lt), lt));
        t.checkExpect(t2.insert(phy), new Node<Book>(title, sog, 
                new Node<Book>(title, babe, lt, 
                        new Node<Book>(title, b, lt, lt)),
                        new Node<Book>(title, hp, lt, 
                                new Node<Book>(title, phy, lt, 
                                        new Node<Book>(title, phy, lt, lt)))));

        t.checkExpect(a1.insert(phy), new Node<Book>(author, hp, la, 
                new Node<Book>(author, phy, la, la))); 
        t.checkExpect(a1.insert(hp2), new Node<Book>(author, hp, la, 
                new Node<Book>(author, hp2, la, la)));
        t.checkExpect(a1.insert(babe), new Node<Book>(author, hp, 
                new Node<Book>(author, babe, la, la), la));

        t.checkExpect(p1.insert(b), new Node<Book>(price, hp, lp, 
                new Node<Book>(price, b, lp, lp))); 
        t.checkExpect(p1.insert(babe), new Node<Book>(price, hp, lp, 
                new Node<Book>(price, babe, lp, lp)));
        t.checkExpect(p1.insert(hp2), new Node<Book>(price, hp, 
                new Node<Book>(price, hp2, lp, lp), lp));

    }

    void testGetgetLeftmost(Tester t) {
        t.checkExpect(t2.getLeftmost(), babe);
        // TEST FOR EXCEPTIONS
    }

    void testGetRight(Tester t) {
        t.checkExpect(t2.getRight(), new Node<Book>(title, 
                sog, 
                new Node<Book>(title, b, lt, lt), 
                new Node<Book>(title, hp, lt, 
                        new Node<Book>(title, phy, lt, lt))));
        // TEST FOR EXCEPTIONS
    }
}






