// 1/14/15
//Int and doubles for numbers -> Booleans -> Structured data
//Struct aligns to classes 
//Structure in racket looks like this in Java

class Ship {
//  Fields
    String name;
    int length;
    
//  Constructor:  
    Ship(String name, int length) {
    this.name = name;
    this.lenght = length;
    
    }
}

// Examples of ships are in their own class
class ExampleShip {
    Ship titanic = new Ship("Titanic", 420);
    Ship xikar   = new Ship("Xikar", 10);
}