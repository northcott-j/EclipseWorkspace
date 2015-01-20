// 1/14/15
//Int and doubles for numbers -> Booleans -> Structured data
//Struct aligns to classes 


// -------------------------------------------------------------->
//Structure in racket looks like this in Java
class Ship {
//  Fields

	// Should be seperate lines
	//          |
	//          V
    String name, status;
    int length;
    
//  Constructor:  
    Ship(String name, String status, int length) {
    this.name   = name;
    this.status = status;
    this.length = length;
    
    }
}

// Examples of ships are in their own class
class ExampleShip {
    Ship titanic = new Ship("Titanic", "Sunk" 420);
    Ship xikar   = new Ship("Xikar", "Atlantic" 10);
}
// --------------------------------------------------------------->

// Unions and itemizations are harder to do
// Courses at a university are one of: Lecture, Class etc.
// Each type of course has sub catagories like class size and location

// 							SHAPE

// 		Circle			    Square			Triangle

//      color               color            color
//      area()              area()           area()  <----Common 
//      perim()             perim()          perim() <----Behaviors 
//      center              center           corner1     

/*RACKET: (define (area aShape)
				(cond
					[(circle? aShape)  ...]
					[(square? aShape)  ...]
					[(triangle? aShape)...]))*/

// Circle square and triangle are classes
// Need to tell java that all of the above are shapes
// Defined as what the objects can do rather than what they are
// An interface is a list of all of the methods that classes are going to provide
// 			Purpose statement or signature 

// 							STATION

// 		 Bus			    TStop	 	   	 Rail Station

//    int Bus#            String line      
//    String location     String location   String location
//                        double fare       double fare 

// Tell java that they all relate to station
// is-a arrow has a open triangle end
// a-Bus is-a STATION etc.     

interface IStation { ... }

//------------------------------------------------------------->
// V1 where it can only contain a single line
class TStopV1 implements IStation {
	String line;
	String location;
	double fare;

	TStopV1(String line, String location, double fare) {
		this.line     = line;
		this.location = location;
		this.fare     = fare;
	}
}

// Examples are of Stations not TStop because they're all Stations
class ExampleIStationV1 {  
  // THIS                   THIS does not match
  //  |                      |
  //UNION    INSTANCE      CLASS
	IStation kenmore = new TStop("Green Line", "Kenmore", 2.65);
}

//------------------------------------------------------------>

// List of Strings class used to build them
interface ILoString { ... }
class ConsLoString implements ILoString {
	String    first;
	ILoString rest;

	ConsLoString(String first, ILoString rest) {
		this.first = first;
		this.rest = rest;
	}
}

class MtLoString implements ILoString {}

//------------------------------------------------------------>
// V2 where TStop can contain more than one line
// Change String to ILoString because it can be empty or full
class TStopV2 implements IStation {
	ILoString line;
	String location;
	double fare;

	TStopV2(ILoString line, String location, double fare) {
		this.line     = line;
		this.location = location;
		this.fare     = fare;
	}
}

class ExampleIStationV2 {  
//                              This was modified to utilize the new LoStrings
	IStation kenmore = new TStop(new ConsLoString("Green", new MtLoString()), "Kenmore", 2.65);
}
//------------------------------------------------------------>
class ExampleIStationV3 {  
//            Pulls out green only to simplify things 
	ILoString greenOnly = new ConsLoString("Green", new MtLoString());
	IStation kenmore = new TStop(this.greenOnly, "Kenmore", 2.65);
}

//=================================================================>>>>>>
// 1/15/15

class Book {
	String title
	String author
	int    price

	Book(String title, String author, int price) {
		this.title  = title;
		this.author = author;
		this.price  = price;
	}

// JAVA TEMPLATE
/*
Fields
	this.price - int                 NO METHODS ON FIELDS
	this.title - string
	this.author - string

Methods
this.salePrice(int) - int
this.onSale(int) - Book
this.isCheaperThan(Book) - Boolean
*/

// Want a funtion that gives the sale price of a Book

/*RACKET:
;; sale-price : a-book integer -> number
;; sale-price computes the sale price of the book given peprcent
(define (sale-price a-book sale)
			(* (book-price a -book)
			   (- 1 (/ sale 100))))*/

// JAVA SIGNATURE actually does something
// Returns the sale price of this Book for the given sale
	int salePrice (int sale) {

	//return this.price * (1 - (sale / 100)); THIS WON'T WORK JAVA DOESN'T DO MATH
		return this.price - (this.price * sale) / 100 
	}

// Returns a new Book that is this book, on sale by the given sale
// This asks salePrice for help to compute the new price 
	Book onSale (int sale) {
		return new Book(this.title, this.author, this.salePrice(sale));
	}	

//Returns true if this book is cheaper than the given book
	boolean isCheaperThan(Book b){
		return this.price < b.price;
	}	
}

class ExamplesBooks {

	Book hp = new Book("HP", "JKR", 1200);
	Book bible = new Book("Bilbe", "Jesus", 1000);

// Check salePrice and onSale
	boolean testSalePrice(Tester t) {
			return t.checkExpect(this.hp.salePrice(50), 600)  
			&&     t.checkExpect(this.hp.onSale(50),             //&& is the and that links both together (short circuits)
				   				 new Book("HP", "JKR", 600));    
		}

	boolean testIsCheaperThan(Tester t) {
		return t.checkExpect(this.lotr.isCheaperThan(this.lotf), false)&&
			   t.checkExpect(this.lotf.isCheaperThan(this.lotr), true);
	}
}
//--------------------------------------------------------------------------------------------------------------->
/*
							IShape
						 double area() 
			//Need to declare this in the interface

		circle  							Square
		Posn center							Posn topleft
		double radius						double side
		double area() <---these delete--->	double area()
*/

interface IShape {
	double area();
}

class Circle implements IShape {
	Posn center;
	double radius;

	Circle(Posn center, double radius){
		this.center = center;
		this.radius = radius;
	}

public //only ever use public when declaring a method shared in interfaces 
	double area() {
		return Math.PI * this.radius * this.radius;
	}
/*
RACKET:
(define (area a-shape)
	(cond
		[(circle? a-shape) (* pi (circle-radius a-shape) (circle-radius a-shape))]
		[(square? a-shape) ... ])	
)
*/
}

class ExampleShapes{
	IShape circle1 = new Circle(new Posn(3,4), 5);

	boolean testArea(Tester t) {
		return t.checkInexact(this.circle1./*<--- the dot is = to the cond because it knows circle (Dynamic Dispatch)*/
			                               area, 78.5, 0.001);
	} 
}


//================================================================================================================================>>\
// 1/21/15

