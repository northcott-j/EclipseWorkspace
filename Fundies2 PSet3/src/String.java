// CS 2510 Spring 2015
// Assignment 3
// * Insert Names Here *

import tester.Tester;

// to represent a list of Strings
interface ILoString {
    // combine all Strings in this list into one
    String combine();
    // Returns a list of sorted strings
    ILoString sort();
    // Inserts String into list of strings
    ILoString insert(String that);
    // Checks if this list is sorted
    boolean isSorted();
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
    MtLoString(){}
    
    // combine all Strings in this list into one
    public String combine() {
       return "";
    }  
    
    public ILoString sort() {
        return this;
    }
    
    public ILoString insert(String that) {
        return new ConsLoString(that, this);
    }
    
    public boolean isSorted() {
        return true;
    }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
    String first;
    ILoString rest;
    
    ConsLoString(String first, ILoString rest) {
        this.first = first;
        this.rest = rest;  
    }
    
    /*
     TEMPLATE
     FIELDS:
     ... this.first ...         -- String
     ... this.rest ...          -- ILoString
     
     METHODS
     ... this.combine() ...     -- String
     
     METHODS FOR FIELDS
     ... this.first.concat(String) ...        -- String
     ... this.first.compareTo(String) ...     -- int
     ... this.rest.combine() ...              -- String
     
     */
    
    // combine all Strings in this list into one
    public String combine() {
        return this.first.concat(this.rest.combine());
    }
    
    public ILoString insert(String that) {
        if (this.first.compareTo(that.toLowerCase()) < 0) {
            return new ConsLoString(this.first, this.rest.insert(that));
        }
        else {
            return new ConsLoString(that, this);
        }
    }
    
    public ILoString sort() {
        return this.rest.sort().insert(this.first);
    }
    
    public boolean isSorted() {
        return this.equals(this.sort());
    }
    
   
    
}

// to represent examples for lists of strings
class ExamplesStrings {
    
    ILoString mary = new ConsLoString("Mary ",
                    new ConsLoString("had ",
                        new ConsLoString("a ",
                            new ConsLoString("little ",
                                new ConsLoString("lamb.", new MtLoString())))));
    ILoString sorted = new ConsLoString("a ",
            new ConsLoString("had ",
                    new ConsLoString("lamb.",
                        new ConsLoString("little ",
                            new ConsLoString("Mary ", new MtLoString())))));
    
    // test the method combine for the lists of Strings
    boolean testCombine(Tester t) {
        return 
            t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
    }
    
    boolean testSort(Tester t) {
        return 
            t.checkExpect(this.mary.sort(), this.sorted) &&
            t.checkExpect(this.sorted.sort(), this.sorted);
    }
    
    boolean testIsSorted(Tester t) {
        return t.checkExpect(this.mary.isSorted(), false) &&
               t.checkExpect(new MtLoString().isSorted(), true) &&
               t.checkExpect(this.sorted.isSorted(), true);
    }
}


