import tester.Tester;

// Assignment 8
// Melagrano Alex
// alex3232
// Jonathan Northcott
// nrthcoj

//Represents a boolean-valued question over values of type T
interface IPred<T> {
    boolean apply(T t);
}

class GetNode<T> implements IPred<T> {
    Node<T> match;
    
    GetNode(Node<T> match) {
        this.match = match;
    }
    
    public boolean apply(T t) {
        return this.match.data.equals(t);
    }
        
}

abstract class ANode<T> {
    ANode<T> next;
    ANode<T> prev;

    // Returns size of Deque
    int size() {
        if(this.next instanceof Sentinel) {
            return 0;
        }
        else {
            return 1 + this.next.size();
        }
    }

    // VARIOIS SIDE EFFECTS: see sub-comments
    // Updates fields of the new node
    void updateNode(ANode<T> Next, ANode<T> Prev, String where) {
        // SIDE EFFEFCT:
        // the next field becomes the oldNext
        // the prev field becomes the Sentinel
        // the prev field of the next field refers back to this
        if (where.equals("head")) {
            this.next = Next;
            this.prev = Prev;
            this.next.prev = this;
        }
        // SIDE EFFEFCT:
        // the next field becomes the Sentinel
        // the prev field becomes the oldPrev
        // the next field of the prev field refers back to this
        else {
            this.next = Next;
            this.prev = Prev;
            this.prev.next = this;
        }
    }
    
    abstract ANode<T> find(IPred<T> t);
    
    // Removes given Node
    void removeNode(ANode<T> remove) {
        if (remove instanceof Sentinel) {
            return;
        }
        else {
            this.find(new GetNode<T>((Node<T>) remove)).next.removeFromTail();
        }
    }
}

class Sentinel<T> extends ANode<T> {

    Sentinel() {
        this.next = this;
        this.prev = this;
    }

    // SIDE EFFECT:
    // Adds given T to front by
    // mutating Sentinel next
    void addAtHead(T add) {
        ANode<T> oldNext = this.next;
        this.next = new Node<T>(add);
        this.next.updateNode(oldNext, this, "head");
    }

    // SIDE EFFECT:
    // Adds given T to end by
    // mutating Sentinel prev
    void addAtTail(T add) {
        ANode<T> oldPrev = this.prev;
        this.prev = new Node<T>(add);
        this.prev.updateNode(this, oldPrev, "tail");
    }

    // SIDE EFFECT:
    // Removes first Node by
    // mutating Sentinel next and
    // the new Next's prev to refer to this
    void removeFromHead() {
        if (this.next instanceof Sentinel) {
            throw new RuntimeException("Can't remove Sentinel");
        }
        else {
            this.next = this.next.next;
            this.next.prev = this;
        }
    }

    // SIDE EFFECT:
    // Removes last Node by
    // mutating Sentinel prev and
    // the new Prev's next to refer to this
    void removeFromTail() {
        if (this.prev instanceof Sentinel) {
            throw new RuntimeException("Can't remove Sentinel");
        }
        else {
            this.prev = this.prev.prev;
            this.prev.next = this;
        }
    }
    
    // Finds first node that fits IPred
    ANode<T> find(IPred<T> pred) {
        if (this.next instanceof Sentinel) {
            return this;
        }
        else {
            return this.next.find(pred);
        }
    }
}

class Node<T> extends ANode<T> {
    T data;

    Node(T data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }

    Node(T data, ANode<T> next, ANode<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
        this.next.prev = this;
        this.prev.next = this;
        if((this.next == null) || (this.prev == null)) {
            throw new IllegalArgumentException("Can't access fields of null");
        }
    }
    
    // Finds first node that fits IPred
    ANode<T> find(IPred<T> pred) {
        if (pred.apply(this.data)) {
            return this;
        }
        else {
            return this.next.find(pred);
        }
    }
}

class Deque<T> {
    Sentinel<T> header;

    Deque() {
        this.header = new Sentinel<T>();
    }

    Deque(Sentinel<T> header) {
        this.header = header;
    }

    // Returns size of Deque
    int size() {
        return this.header.size();
    }

    // Adds given T to front
    void addAtHead(T add) {
        this.header.addAtHead(add);
    }

    // Adds given T to end
    void addAtTail(T add) {
        this.header.addAtTail(add);
    }

    // Removes first Node
    void removeFromHead() {
        this.header.removeFromHead();
    }

    // Removes last Node
    void removeFromTail() {
        this.header.removeFromTail();
    }
    
    // Finds first node that fits IPred
    ANode<T> find(IPred<T> pred) {
        return this.header.find(pred);
    }
    
    // Removes given Node
    void removeNode(ANode<T> remove) {
        this.header.removeNode(remove);
    }
}

class ExamplesDeque {
    Sentinel<String> sent1 = new Sentinel<String>();
    Sentinel<String> sent2 = new Sentinel<String>();
    Sentinel<String> sent3 = new Sentinel<String>();
    Node<String> holdr = new Node<String>("Place Holder");

    Node<String> node1 = new Node<String>("abc", holdr, sent1);
    Node<String> node2 = new Node<String>("bcd", holdr, node1);
    Node<String> node3 = new Node<String>("cde", holdr, node2);
    Node<String> node4 = new Node<String>("def", sent1, node3);

    Node<String> node5 = new Node<String>("duck", holdr, sent2);
    Node<String> node6 = new Node<String>("knife", holdr, node5);
    Node<String> node7 = new Node<String>("carrot", holdr, node6);
    Node<String> node8 = new Node<String>("puck", holdr, node7);
    Node<String> node9 = new Node<String>("zip", sent2, node8);

    Node<String> node10 = new Node<String>("cap", sent3, sent3);

    Deque<String> deque1 = new Deque<String>();
    Deque<String> deque2 = new Deque<String>(sent1);
    Deque<String> deque3 = new Deque<String>(sent2);
    Deque<String> deque4 = new Deque<String>(sent3);

    void testSize(Tester t) {
        t.checkExpect(deque1.size(), 0);
        t.checkExpect(deque2.size(), 4);
        t.checkExpect(deque3.size(), 5);
    }

    void testRemoveFromHead(Tester t) {
        // Check for exceptions

        Sentinel<String> sent1 = new Sentinel<String>();
        Sentinel<String> sent2 = new Sentinel<String>();

        Node<String> node1 = new Node<String>("bcd", holdr, sent1);
        Node<String> node2 = new Node<String>("cde", holdr, node1);
        Node<String> node3 = new Node<String>("def", sent1, node2);

        Node<String> node4 = new Node<String>("cap", sent2, sent2);


        Deque<String> chkdeq2 = new Deque<String>(sent1);
        Deque<String> chkdeq4 = new Deque<String>();     

        // Mutate
        this.deque2.removeFromHead();
        this.deque4.removeFromHead();

        t.checkExpect (deque2, chkdeq2);
        t.checkExpect(deque4, chkdeq4);

        // Undo mutation
        this.deque2.addAtHead("abc");
        this.deque4.addAtHead("cap");
    }

    void testRemoveFromTail(Tester t) {
        // Check for exceptions

        Sentinel<String> sent1 = new Sentinel<String>();
        Sentinel<String> sent2 = new Sentinel<String>();

        Node<String> node1 = new Node<String>("abc", holdr, sent1);
        Node<String> node2 = new Node<String>("bcd", holdr, node1);
        Node<String> node3 = new Node<String>("cde", sent1, node2);

        Node<String> node4 = new Node<String>("cap", sent2, sent2);


        Deque<String> chkdeq2 = new Deque<String>(sent1);
        Deque<String> chkdeq4 = new Deque<String>();     

        // Mutate
        this.deque2.removeFromTail();
        this.deque4.removeFromTail();

        t.checkExpect (deque2, chkdeq2);
        t.checkExpect(deque4, chkdeq4);

        // Undo mutation
        this.deque2.addAtTail("def");
        this.deque4.addAtTail("cap");
    }

    void testAddAtHead(Tester t) {
        Sentinel<String> sent1 = new Sentinel<String>();
        Sentinel<String> sent2 = new Sentinel<String>();

        Node<String> node1 = new Node<String>("ab", sent1, sent1);
        Node<String> node2 = new Node<String>("ab", holdr, sent2);
        Node<String> node3 = new Node<String>("cap", sent2, node2);

        Deque<String> chkdeq1 = new Deque<String>(sent1);
        Deque<String> chkdeq4 = new Deque<String>(sent2);     

        // Mutate
        this.deque1.addAtHead("ab");
        this.deque4.addAtHead("ab");

        t.checkExpect (deque1, chkdeq1);
        t.checkExpect(deque4, chkdeq4);

        // Undo mutation
        this.deque1.removeFromHead();
        this.deque4.removeFromHead();
    }

    void testAddAtTail(Tester t) {
        Sentinel<String> sent1 = new Sentinel<String>();
        Sentinel<String> sent2 = new Sentinel<String>();

        Node<String> node1 = new Node<String>("ab", sent1, sent1);
        Node<String> node2 = new Node<String>("cap", holdr, sent2);
        Node<String> node3 = new Node<String>("ab", sent2, node2);

        Deque<String> chkdeq1 = new Deque<String>(sent1);
        Deque<String> chkdeq4 = new Deque<String>(sent2);     

        // Mutate
        this.deque1.addAtTail("ab");
        this.deque4.addAtTail("ab");

        t.checkExpect (deque1, chkdeq1);
        t.checkExpect(deque4, chkdeq4);

        // undo mutation
        this.deque1.removeFromTail();
        this.deque4.removeFromTail();
    }
}