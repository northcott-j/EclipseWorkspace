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

// Tolerance is based on expected based on expected result - Relevant vs Absolute 

interface IShape { 
	double area();
	double distanceToOrigin();
	boolean isBiggerThan();
}

/*
// Represents a 2-d point (in Cartesian coordinates)
class CartPt {
	int x;
	int y;

	CartPt(int x, int y);	
	this.x = x;
	this.y = y;
}

class Circle implements IShape {
	double radius;
	// Center of the circle
	int x;
	int y;
	Circle(double radius, int x, int y) { //<--Initializes fields
		this.radius = radius;
		this.x = x;
		this.y = y;
	}
   /* Template:
   	* Fields
   	*...this.radius... --- double
   	*...this.x...      --- int
   	*...this.y...      --- int
   	* Methods
   	*...this.area()             --- double
   	*...this.distanceToOrigin() --- double
   	*...this.isBiggerThan()     --- boolean
   	*Methods of fields
   	* 
   	*/

/*	public double area() { 
		return Math.PI * this.radius * this.radius;
	}

	// Returns the distance to the origin from the nearest edge 
	public double distanceToOrigin() { 
		return Math.sqrt(this.x * this.x + this.y * this.y) - this.radius;
	}

	public boolean isBiggerThan(IShape other) {
		return this.area() > other.area();
	}
		 Parameters
		 * other                       --- IShape
		 * Methods on parameters
		 * other.area()                --- double
		 * other.distanceToOrigin()    --- double
		 * other.isBiggerThan(IShape)  --- boolean
		
}

class Square implements IShape {
	double sideLen;
	// represents the top left
	int x;
	int y;

	Square(double sideLen, int x, int y) {
		this.sideLen = sideLen;
		this.x = x;
		this.y = y;
	}
	public double area() {
		return this.sideLen * this.sideLen;
	}

	public double distanceToOrigin() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	public boolean isBiggerThan() {
		return this.area() > other.area();
	} 
	
}

class ExamplesShape {
	IShape c1 = new Circle(5, 6, 8);
	IShape c2 = new Circle(10, 3, 4);

	boolean testArea(Tester t) {
		t.checkInexact(this.c1.area(), 78.5, 0.001);
	}

	boolean testDistanceToOrigin(Tester t) {
		return t.checkInexact(this.c1.distanceToOrigin(), 5 , 0.001)
			&& t.checkInexact(this.c2.distanceToOrigin()), -5, 0.001);
	}

	boolean testIsBiggerThan(Tester t) {
		return t.checkExpect(this.c1.isBiggerThan(this.c2), false)
			&& t.checkExpect(this.c2.isBiggerThan(this.c1), true);
	}
}*/

//----------------------------------------------------------------------------->
// Version 2 with Abstraction

// Represents a 2-d point (in Cartesian coordinates)
class CartPt {
	int x;
	int y;

	CartPt(int x, int y);	
	this.x = x;
	this.y = y;

	double distanceToOrigin() {
		return Math.sqrt(this.x * this.x + this.y *this.y);
	}
}

class Circle implements IShape {
	double radius;
	// Center of the circle
	CartPt center;
	Circle(double radius, CartPt center) { //<--Initializes fields
		this.radius = radius;
		this.center = center;
	}
   /* Template:
   	* Fields
   	*...this.radius... --- double
	*...this.center... --- CartPt
   	* Methods
   	*...this.area()             --- double
   	*...this.distanceToOrigin() --- double
   	*...this.isBiggerThan()     --- boolean
   	*Methods of fields  
   	*...this.center.distanceToOrigin() --- double
   	*/

	public double area() { 
		return Math.PI * this.radius * this.radius;
	}

	// Returns the distance to the origin from the nearest edge 
	public double distanceToOrigin() { 
		return this.center.distanceToOrigin() - this.radius;
	}

	public boolean isBiggerThan(IShape other) {
		return this.area() > other.area();
	}
		/* Parameters
		 * other                       --- IShape
		 * Methods on parameters
		 * other.area()                --- double
		 * other.distanceToOrigin()    --- double
		 * other.isBiggerThan(IShape)  --- boolean
		*/
}

class Square implements IShape {
	double sideLen;
	// represents the top left
	CartPt topLeft;

	Square(double sideLen, CartPt topLeft) {
		this.sideLen = sideLen;
		this.topLeft = topLeft;
	}
	public double area() {
		return this.sideLen * this.sideLen;
	}

	public double distanceToOrigin() {
		return this.topLeft.distanceToOrigin();
	}

	public boolean isBiggerThan() {
		return this.area() > other.area();
	} 
	
}



class Combo implements IShape {
	IShape shape1;
	ISHape shape2;

	Combo(IShape shape1, IShape shape2) {
		this.shape1 = shape1;
		this.shape2 = shape2;
	}
		public double area() {
		return this.shape1.area() + this.shape2.area();
	}

	public double distanceToOrigin() {
		return Math.min(this.shape1.distanceToOrigin(), this.shape2.distanceToOrigin());
	}

	public boolean isBiggerThan() {
		return this.area() > other.area();
	} 
}



class ExamplesShape {
	IShape c1 = new Circle(5, new CartPt(6, 8));
	IShape c2 = new Circle(10, new CartPt(3, 4));

	boolean testArea(Tester t) {
		t.checkInexact(this.c1.area(), 78.5, 0.001);
	}

	boolean testDistanceToOrigin(Tester t) {
		return t.checkInexact(this.c1.distanceToOrigin(), 5 , 0.001)
			&& t.checkInexact(this.c2.distanceToOrigin()), -5, 0.001);
	}

	boolean testIsBiggerThan(Tester t) {
		return t.checkExpect(this.c1.isBiggerThan(this.c2), false)
			&& t.checkExpect(this.c2.isBiggerThan(this.c1), true);
	}
}


//===========================================================================================>
// 1/22/15

// Lists - specifically methods

class Book {
	String name
	String author
	int    price

	Book(String name, String author, int price) {
		this.title  = name;
		this.author = author;
		this.price  = price;
	}

	// does this book have the given title?
	boolean hasTitle(String title) {
		return this.name.equals(title);
	}

	// is this book cheaper than the given book
	boolean isCheaperThan(Book other) {
		return !other.isCheaperThanPrice(this.price);
	}

	// is this books price cheaper than the given price
	boolean isCheaperThanPrice(double price) {
		return this.price < price;
	}

	// Above methods follow a bouncing method passing off information from List all the way to Book
}

// represents a list of books
interface ILoBook {
	// returns the length of this list of books
	int length();

	// does a book contain a given title
	boolean containsBookTitle(String title);
	// returns the list of books in this list with the given title
	ILoBook allBooksWithTitle(String title);
	// returns a new list with books sorted by increasing price
	ILoBook sortByPrice();
	// insert the given book into this sorted list of books
	ILoBook insert(Book book);
}

// returns an empty list of books
class MtLoBook implements ILoBook {
	MtLoBook() { }
// returns the length of this empty list of books
	public int length() {
		return 0;
	}

// does this empty list contain a book of the given title
	public boolean containsBookTitle(String title) {
		return false;
	}

// returns empty list as there are no books in an empty list
	public ILoBook allBooksWithTitle(String title) {
		return this;
	}

// returns a sorted empty list
	public ILoBook sortByPrice() {
		return this;
	}

// insert the given book into this empty sorted list of books
	public ILoBook insert(Book book) {
		return new ConsLoBook(book, this);
	}
}

class ConsLoBook implements ILoBook {
	Book first;
	ILoBook rest;
	ConsLoBook(Book first, ILoBook rest) {
		this.first = first;
		this.rest = rest;
	}
	/* TEMPLATE:
	* Fields
	* this.first --- Book
	* this.rest --- ILoBook
	*
	* Methods
	* this.length() --- int
	* this.containsBookTitle(String) --- boolean
	* this.allBooksWithTitle(String) --- ILoBook
	*
	* Methods of fields
	* this.first.discount(double)--- double
	* this.first.discountedBook(double) --- Book
	* this.first.hasTitle(String) --- boolean
	* this.rest.length() --- int
	* this.rest.containsBookTitle(String) --- boolean
	* this.rest.allBooksWithTitle(String) --- ILoBook
	* this.rest.sortByPrice() --- ILoBook
	*/

	// return the length of this non-empty list of books
	public int length() {
		return 1 + this.rest.length();
	}

// does this non-empty list of books contain a book of a given title
	// if is an expression -> it does stuff
	// statements return stuff
	public boolean containsBookTitle(String title) {
	/*	if (this.first.hasTitle(title)) {
			return true;
		}
		else {
			return this.rest.containsBookTitle(title);
		}*/
		return this.first.hasTitle(title) || this.rest.containsBookTitle(title);
		//     ^
		//     |
		// has standard subject verb object
	}

	// return list of books that match the given title in this list
	public ILoBook allBooksWithTitle(String title) {
		if (this.first.hasTitle(title)) {
			return new ConsLoBook(this.first, this.rest.allBooksWithTitle(title));
		}
		else {
			return this.rest.allBooksWithTitle(title));
		}
	}

// returns sorted list of books contained in this list by increasing price
	public ILoBook sortByPrice() {
		this.rest.sortByPrice().insert(this.first);
	}

// insert this given book into this non-empty list of sorted books
	public ILoBook insert(Book book) {
		if (this.first.isCheaperThan(book)) {
			return new ConsLoBook(this.first, this.rest.insert(book));
		}
		else {
			return new ConsLoBook(book, this);
		}
	}
}

class ExamplesBooks {

	Book hp = new Book("HP", "JKR", 1200);
	Book bible = new Book("Bilbe", "Jesus", 1000);

	ILoBook list1 = new ConsLoBook(this.hp, 
						new ConsLoBook(this.hp, 
							new ConsLoBook(this.hp, new MtLoBook())))

	boolean testLength(Tester t) {
		return t.checkExpect(this.list1.length(), 3);
	}

	//==============================================================================>
	//1/26/15 && 1/29/15

	/*
Class person is-a IAT
	String name;
	int yob;
	boolean isMale;
	IAT dad;
	IAT mom;
Ex. Person("Cynthia", 1960, false, this.david, new Unknown());
Class unknown is-a IAT

Methods on IATs:
int  furthestKnownGen()
int  furthestGenInclusive()
boolean isWellFormed()

boolean isOlderThan (int yob); <- This gives mom and dad our age instead of taking theirs
int numFemaleAreOver40()
String ancNames()
	*/

// class Unknown
public int furthestGenInclusive() {
    return 0;
}

public int furthestKnownGen() {
    return 0;
}

public boolean isWellFormed() {
    return true; //<- To satisfy and in People field 
}

public boolean isOlderThan(int yob) {return true;}

public String ancNames() {
	return "";
}

public String ancNamesHelp(String rest) {
	return rest; //<--returns the collector 
}


// class Person
public int furthestGenInclusive() {
    return 1 + Math.max(this.dad.furthestGenInclusive(),
                        this.mom.furthestGenInclusive());
}

public int furthestKnownGen() {
    return Math.max(this.dad.furthestGenInclusive(),
                    this.mom.furthestGenInclusive());
// OR
    return this.furthestGenInclusive() - 1; //<- Subtract myself
}

// This will not work because:
/*
Should be > not <
Unknown gives a bunch of issues
Can't access fields of field
*/
public boolean isWellFormed() {
    return (this.yob < this.dad.yob)
        && (this.yob < this.mom.yob)
        &&  this.dad.isWellFormed()
        &&  this.mom.isWellFormed();
}

//This will work
//Uses a helper function to pass my yob to my paretns and so on
public boolean isWellFormed2() {
    return this.dad.isOlderThan(this.yob)
        && this.mom.isOlderThan(this.yob)
        &&  this.dad.isWellFormed()
        &&  this.mom.isWellFormed();
}

public boolean isOlderThan(int yob) {
    return this.yob < yob;
}

// exclusive
public int numFemaleAreOver40() {
    return this.mom.numFemaleAreOver40Help() +
           this.dad.numFemaleAreOver40Help();
}

// inclusive
public int numFemaleAreOver40Help() {
    if(2015 - this.yob > 40 && !this.isMale) {
        return 1 + this.mom.numFemaleAreOver40Help() +
                   this.dad.numFemaleAreOver40Help();
    } else {
        return this.mom.numFemaleAreOver40Help() +
               this.dad.numFemaleAreOver40Help();
    }
}

// create a list of all ancesters starting from paternal to maternal
// this won't work because Unknown calls an extra comma Dwight , ,

public String ancNames() {
	return this.name.append(",")
					.append(this.dad.ancNames()).append(",") //<-- remove this makes Dwight,
					.append(this.mom.ancNames());
}

// Redo with if statements to guard against over shoot
public String ancNames() {
	// LOCAL
	String momNames = this.mom.ancNames(),
	String dadNames =this.dad.ancNames();

	if (momNames.equals("") && dadNames.equals("")) {
		return this.name;
	}
	else if(momNames.equals("")) {
		return this.name.append(", ")
						.append(dadNames);
	}
	else if(dadNames.equals("")) {
		return this.name.append(", ")
						.append(momNames);
	}
	else {return this.name + ", " + momNames + ", " + dadNames}
}

/*
							^
	                        |
	               Redo this with helper

*/

// even faster with a help that kept track of remaining data
// collects data from one side of the family and passes it to the rest
public String ancNamesHelp(String rest) {
	String momNames = this.mom.ancNamesHelp(rest) //<--Moms + Rest
	String dadNames = this.dad.ancNamesHelp(momNames) //<--Dads + Moms + Rest

	if (dadNames.equals("")) {
		return this.name;
	} 
	else this.name.append(", ")
				  .append(dadNames); //<--Me + Dads + Moms + Rest
}

// redo ancNames using above
public String ancNames() {
	return this.ancNamesHelp("");
}

//===================================================================================>
// 2/4/15

interface IAT {
	IAT youngerIAT(IAT other);
	IAT youngerIATHelp(IAT other, int otherYob); //<-- pass YOB while you can
	IAT youngestParent();
	IAT youngestGrandparent();
	IAT youngestGreatGP();

	// ABSTRACTION of above three
	IAT youngestAncInGen(int gensAgo);
}

//Unknown
public IAT youngerIAT(IAT other) {
	return other;
	// if other is an unknown, I should return unknown else it's a person and return person
}

public IAT youngerIATHelp(IAT other, int otherYob) {
	return other;
}

public IAT youngestParent() {
	return this;
}

public IAT youngestGrandparent() {
	return this;
}

public IAT youngestGreatGP() {
	return this.
}

// ABSTRACTION of above

public IAT youngestAncInGen(int gensAgo) {
	return this;
}

//Person
public IAT youngerIAT(IAT other) {
	return other.youngerIATHelp(this, this.yob); //<-- has access to yob so it passes it to the helper
}

public IAT youngerIATHelp(IAT other, int otherYob) {
	return if (otherYob > this.yob) {
		return other;
	}
	else {
		return this;
	}
}

public IAT youngestParent() {
	return his.dad.youngestIAT(this.mom);
}

public IAT youngestGrandparent() {
	return this.dad.youngestParent().youngerIAT(this.mom.youngestParent());
}

public IAT youngestGreatGP() {
	return this.dad.youngestGrandparent().youngerIAT(this.mom.youngestGrandparent());
}

// ABSTRACTION of above

public IAT youngestAncInGen(int gensAgo) {
	if (gensAgo == 0) {
		return this;
	}
	else {
		return this.dad.youngestAncInGen(gensAgo - 1).youngerIAT(this.mom.youngestAncInGen(gensAgo - 1));
	}
}

// Example of abstracted youngestGrandparent()
public IAT youngestGrandparent() {
	return this.youngestAncInGen(2);
}

// gensAgo is an accumulator parameter that bottoms out when zero
// Lecture 8 has practice problems with accumulators

//=======================================================================================>
// 2/5/15

// IShape
double area();
boolean isBiggerThan(IShape that);
double distanceToOrigin();
IShape grow(double factor);

// Circle
class Circle extends AShape {
	double radius;

	//CartPt center;
	//String color;
		//Inherited 

	Circle(double radius, CartPt center, String color) {
		super(center, color); //Super must be first!!
		this.radius = radius;
	}

	public double distanceToOrigin() {                        /*Overrides inherited distanceToOrigin*/
		return this.loc.distanceToOrigin() - this.radius;             /* looks at closest method  */
			/* ---------super()---------- */
	}
}

/*
OVERLOAD = two different methods with the same name but different parameters in the same class
OVERRIDE = two different methods with the same name and same parameters in two different classes
*/

// Square
class Square extends Rect {
	double side;

	//CartPt topLeft;
	//String color;
		//Inherited from Rect
/*
	Square(double side, CartPt topLeft, String color) {
			................
			  ...........
	}

	<Previous constructor>
*/
	Square(double side, CartPt topLeft, String color) {
		super(side, side, topLeft, color);
	}

/*
	public double area() {return this.side * this.siide;}

	public IShape grow(double factor) {
		return new Square(this.side * factor, this.topLeft, this.color);
	}

	public boolean isBiggerThan(IShape that) {
		return this.area() > that.area();
	}

	<No longer needed as Rect now fulfills all methods>
*/
	public IShape grow(double factor) {
		return new Square(factor * this.width, //or this.length)
						  this.loc, this.color);
	}
}

/*
ABSTRACTION METHOD --->>> Create a helper class such as CartPt to do distanceToOrigin 
Sharing Promises(Interfaces)
Sharing Helpers(Delegation)
Sharing Fields/Methods (Abstract class)
*/

// Rect EXTENDS Shape (Concrete class isn't abstract)

// Extends shows inheritance
	// One class inherits from another gets all of:
		// The fields and methods

class Rect extends AShape {
	double length;
	double width;
//  loc field means topLeft in Rect

	//CartPt topLeft;
	//String color;
	// Now inherited from shape

	Rect(double length, double width, CartPt topLeft, String color) {
	// Takes fields missing from inherited for the constructor 
		super(topLeft, color); //<-- Equals super constructor AShape(CartPt loc, String color)
		this.length = length;
		this.width = width;
	}

	public double area() {
		return this.length * this.width;
	}

// Needs to be overridden by Square so we don't lose the fact that it's a Square
	public IShape grow(double factor) {
		return new Rect(factor * this.length, factor * this.width, this.loc, this.color);
	}
}

// Combo
IShape first;
IShape second;

/*
Abstract can't be made directly
Used to gather code in a common place
Allows for recycle 
*/

abstract class AShape implements IShape {
	CartPt loc;
	String color;

	AShape(CartPt loc, String color) {
		this.loc = loc;
		this.color = color;
	}

	abstract public double area();
//     ^
//     |
// Delay promise to interface until the class is extended 

	public boolean isBiggerThan(IShape that) {
		return this.area() > that.area();
	}

	public double distanceToOrigin() {
		return this.loc.distanceToOrigin();
	}
}

// Still need an interface because Combo still follows IShape not AShape
// Interfaces are signature and promise things to do
// Abstracts are laziness to prevent repeat code
// Thick triangle is-a
	// Filled is extends
	// Open is an interface (implements)

//============================================================================>

//2/11/15

// Interfaces can have fields but they're constants
interface ITetrisPiece {
int GAME_HEIGHT = 40;

}

abstract class ATetra implements ITetrisPiece {
	IColor color;
	Posn rotCenter;
	int rotAngle;

	ATetra(IColor color, Posn rotCenter, int rotAngle) {
		this.color = color;
		this.rotCenter = rotCenter;
		this.rotAngle = rotAngle;
		if(!this.inBounds()) {
			throw new IllegalArgumentException("Bad pos!");
		}

	// Does not belong in ITetrisPiece
	// Fundamental to how they work
		abstract boolean inBounds(); 
	}

	ATetra(Color color, int x, int rotAngle) {
		this(color, new Posn(x, GAME_HEIGHT), rotAngle);
// This overloads previous constructor 
// This is extra code		
/*		this.color = color;
		this.rotCenter = new Posn(x, Game_HEIGHT);
		this.rotAngle = rotAngle;*/
	}
}

class PieceO extends ATetra{



}

class PieceL extends ATetra{
	PieceL(Color color, Posn rotCenter, int rotAngle) {
		super(color, rotCenter, rotAngle);
	}
}

// Error checking and testing for exceptions
/*
* Expected Exception
* String name of the class
* arguments to constructor
*/
return t.checkConstructorException(new IllegalArgumentException("Bad pos!"),
							"PieceL",
							new Red(),
							new Posn(45,6),
							33);

//===================================================================================>
// 2/12/15
/*
EQUALITY:

PRIMITIVES:
int: i1 == i2
boolean: b1 == b2
double: Math.abs(d1-d2) < 0.0000001
String: s1.equals(s2)

COMPLEX DATA:
Posn: p1.x == p2.x && p1.y == p2.y
Books: *Check fields using primitive types*

*/

//IShape 
boolean sameShape(IShape that);
boolean isSquare();
Square asSquare();

boolean isCirlce();
Circle asCircle();

boolean isRect();
Rect asRect();

//Circle is-a IShape
int rad;

/*public boolean sameShape(IShape that) {
	// Type Casting - pretend this is a that
	// Runtime error
	// ClassCastException when passing the wrong class
	if(that instanceof Circle) { // Instanceof is special 
		Circle cthat = (Circle)that; //Overrides Java type - guarentees it's a Circle
		return this.rad == cthat.rad;
	}
	else {
		return false;
	}
}
*/ //Removed instanceof

public boolean sameShape(IShape that) {
	if(that.isCirlce()) {
		Circle cthat = that.asCircle();
		return this.rad == cthat.rad;
	}
	else {
		return false;
	}
}

public boolean isCircle() {return true;}
public Circle asCircle() {return this;}

public boolean isRect() {return false;}
public Rect asRect() {throw new ClassCastException("Circle isn't a Rect");}
//Rect is-a IShape
int w, h;

public boolean sameShape(IShape that) {
	if(that instanceof Rect) {
		Rect rthat = (Rect)that;
		return (this.w == rthat.w) &&
			   (this.h == rthat.h);
	}
	else {
		return false;
	}
}

//Square is-a Rect

//===================================================================================>
// 2/18/15
// EQUALITY

class Circle implements IShape {
	int radius;

// Returns whether this Circle is the same as the given IShape
// instanceof + casting equality method
	public boolean sameShape(IShape that) {
		if(that instanceof Circle) {
		Circle cthat = (Circle)that;
		return this.radius == cthat.radius;
	}
	else {
		return false;
	}
}

// This is safe-casting 
class Square extends Rect {

	public boolean isSquare {
		return true;
	}

	public boolean isRect {
		return false;
	}

	public boolean isCircle {
		return false;
	}

	public Square asSquare {
		return this;
	}

	public Rect asRect {
		throw new ClassCastException("Not a Rect - call isRect first");
	}
}

// ============================================================>
// CONTRAST Above
// This is called Double-dispatch
// It's nice because it's all local - to fix square put it in square

interface IShape {
	boolean sameCircle(Circle);
	boolean sameRect(Rect);
	boolean sameTriange(Triangle);

	boolean sameShape(IShape);
}

abstract class AShape implements IShape {
	public abstract boolean sameShape(IShape);

	public boolean sameCircle(Circle that) {
		return false;
	}

	public boolean sameRect(Rect that) {
		return false;
	}

	public boolean sameTriangle(Triangle that) {
		return false;
	}

	public boolean sameSquare(Square that) {
		return false;
	}

}

class Circle extends AShape {

	public boolean sameCircle(Circle that) {
		return this.radius == that.radius;
	} 

/*	Don't need - inherited
	public boolean sameRect(Rect that) {
		return false;
	}

	public boolean sameTriange(Triangle that) {
		return false;
	}*/

	public boolean sameShape(IShape that) {
		// I know that this is a Circle
		return that.sameCircle(this);
	}
}

class Rect extends AShape {

	public boolean sameRect(Rect that) {
		return this.width == that.width &&
			   this.height == that.height;
	}

/*	Don't need - inherited
	public boolean sameCircle(Circle that) {
		return false;
	}

	public boolean sameTriange(Triangle that) {
		return false;
	}*/

	public boolean sameShape(IShape that) {
		// I know that this is a Rect
		return that.sameRect(this);
	}


}

class Traingle extends AShape {

/*	Don't need - inherited 
	public boolean sameRect(Rect that) {
		return false;
	}

	public boolean sameCircle(Circle that) {
		return false;
	}*/

	public boolean sameTriange(Triangle that) {
		return //Check all fields;
	}

	public boolean sameShape(IShape that) {
		return that.sameTriange(this);
	}
}

class Square extends Rect {

	public boolean sameSquare(Square that) {
		return this.sidelength == that.sidelength;
	}

	public boolean sameShape(IShape that) {
		return that.sameSquare(this);
	}

	public boolean sameRect(Rect that) {
		return false;
	}
}

class ExamplesEquality {
	IShape s1 = new Rect(3, 3);
	IShape s2 = new Square(3);

	s2.sameShape(s1) = false;
	s1.sameShape(s2) = ....;


/*
s1 is a Rect
s2.sameRect(s1);
s2 extends Rect and inherits sameRect
-- Uh oh
Override inherited sameRect in square
*/
}

//=====================================================================>
// 2/19/15

// Runner
/*
String name
int pos
int time in seconds
int age
boolean isFemale
*/

/*
ILoRunner:
Runner winner();
ILoRunner topN(int n);
ILoRunner femaleRunners()--> don't need;
ILoRunner filter(IRunnerPred p);
*/

// MtLoRunner

// This method is clunky
ILoRunner filter(IRunnerPred p) {
	return this;
}


// ConsLoRunner

/*// This method is clunky
ILoRunner runnersUnder(int age) {
	if(this.first.age < age) {
		new ConsLoRunner(this.first, this.rest.runnersUnder(age));
	}
	else {
		return this.rest.runnersUnder(age);
	}
}*/

// This is method 2.0
ILoRunner filter(IRunnerPred p) {
	if(p.apply(this.first)) {
		new ConsLoRunner(this.first, this.rest.filter(p));
	}
	else {
		return this.rest.filter(p);
	}
}

interface IRunnerPred {
	boolean apply(Runner r);
}

// Represent functions that rip apart data
class RunnerIsFemale implements IRunnerPred {
	public boolean apply(Runner r) {
		return r.isFemale;
	}
}

// Represent functions that rip apart data
class RunnersOverN implements IRunnerPred {
	int N;

	RunnersOverN(int N) {
		this.N = N;
	}

	public boolean apply(Runner t) {
		return r.age > this.N;
	}
}

class ExamplesMarathon {
	ILoRunner m1 = ...;

// These are called function objects = lambda 
	marathon.filter(new RunnerIsFemale());
	marathon.filter(new RunnerOverN(40));
}

// =================================================================> 
// 2/23/15

/*
SNELL 168 6pm-9pm

3 Q's:
	- Helpers and recursion 
	- 10 Pages
NO TEMPLATES
YES PURPOSE STATEMENTS
YES TESTS
YES OPEN NOTES
NO CONSTRUCTORS
COMMENT WHERE METHODS GO

YES BAD CASTING (instaceof)
YES SAFE CASTING 

NO DOUBLE DISPATCH

MIDTERM SHORTCUTS:
  makeList(4, 5, 6, 7) instead of new ConsLoItem()
  1 + 2 ----> 3
  */

  // in ILoRunner
ILoRunner filter(IRunnerPred p);
// ILoRunner sortByTime(); <---- Going to get annoying
ILoRunner sortBy(IRunnerComperator comp);
ILoRunner insert(Runner r, IRunnerComperator comp);

interface IRunnerComperator {
	int compare(Runner r1, Runner r2);
}

class RunnesByTime implements IRunnerComperator {
	public int compare(Runner r1, Runner r2) {
		return r1.time - r2.time;	
	}
}

// in MtLoRunner
public ILoRunner sortBy(IRunnerComperator comp) {
	return this;
}

public ILoRunner insert(Runner r, IRunnerComperator comp) {
	return new ConsLoRunner(r, this);
}

// in ConsLoRunner
public ILoRunner sortBy(IRunnerComperator comp) {
	return this.sortBy(comp).insert(this.first, comp);
}

public ILoRunner insert(Runner r, IRunnerComperator comp) {
	if(comp.compare(r, this.first) <= 0) {
		return new ConsLoRunner(r, this);
	}
	else {
		return new ConsLoRunner(this.first, this.rest.insert(r, comp));
	}
}

// Turns OnlyWomen into OnlyMen
class Not implements IRunnerPred {
	IRunnerPred p;

	public boolean apply(Runner r) {
		return !this.p.apply(r);
	}
}

class AndThen implements IRunnerComperator {
	IRunnerComperator c1;
	IRunnerComperator c2;

	public int compare(Runner r1, Runner r2) {
		int ans1 = this.c1.compare(r1,r2);

		if(ans1 != 0) {
			return ans1;
		}
		else {
			return this.c2.compare(r1, r2);
		}
	}
}

// ============================================================================>
// 2/25/15
// Theres a class called Random that can be made  
// r.nextInt() -> int
// r.nextInt(6) -> int from 0 - 5
// new Random([seed can go here]) -> same seed equals same random number
// CANNOT CREATE A LIST OF INTS DOUBLES OR BOOLEANS 
// OBJECTS NOT PRIMITIVES 
// Integer(), Double(), Boolean()

interface IPred<X> {
	boolean apply(X x);
}

interface IComperator<X> {
	int compare(X x1, X x2); 
}

interface IFunc<A, R> {
	<R> apply(A arg);
}

interface IFunc2<A1, A2, R> {
	R apply(A1 arg1, A2 arg);
}

//Listof X <-- that's a type variable
// Parametric or generic interface
// Normally use <T> not <X> for Type 
interface IList<X> {
	int len();
	IList<X> filter(IPred<X> pred);
	IList<X> sortBy(IComparetor<X> comp);
	<Y> /*<-- Declards type for Y*/ IList<Y> map(IFunc<X, Y> func);
	<Y> <Y> foldr(IFunc2<X, Y, Y> func, Y base) 	
}

class Empty<X> implements IList<X> {
	Empty() { }
	
	public int len() {
		return 0;
	}
	
	public <Y> IList<Y> map(IFunc<X, Y> func) {
		return new Empty<Y>();
	}	
}

class Cons<X> implements IList<X> {
	X first;
	IList<X> rest;
	
	Cons(X first, IList<X> rest) {
		this.first = first;
		this.rest = rest;
	}
	
	public int len() {
		return 1 + this.rest.len();
	}
	
	public <Y> IList<Y> map(IFunc <X, Y> func) {
		return new Cons<Y>(func.apply(this.first), this.rest.map(func);
	}
}

class ExamplesParameterized {
	IList test = new Cons<String>("hello",
													new Cons<String>("world",
																new Empty<String>))
}


// ===================================================================>
// 2/26/15
// How to implement Function Objects for Union Data (Interfaces not Classes)
// Each object takes a turn being the subject and the object ergo Double Dispatch
// Meant to patch in behavior later on

/*
OVERVIEW:
Call map of Shape2Perims on a list of Shapes
Shape2Perim calls ACCPEPT on the first shape and passes a IShapeVisitor (this case to check perim)
Shape class applies func (ShapePerim) applyTo[the class object] with a param of this object
The func is a ShapePerimVisior class which has methods applyTo[this class object] that calculate perimeter 

*/

interface IShape {
	double area();
	// Assume this is in the library
	// Normally called accept
	<T> T beAppliedToBy(IShapeFunc<T> f);  
}

interface IShapFunc<T> { // IShapeVisitor<T> knows how to visit IShapes
	T applyToCircle(Circle c);
	T applyToRect(Rect t);
	T applyToSquare(Square t);
	// Normally all called visit and are overloaded
}

interface IList<T> {
	<U> IList<U> map(IFunc<T, U> func);
}

interface IFunc<A, R> {
	R apply(A arg);
}

// This is useable by Map
// Ask the Shape what shape are you?
// This now goes to the IShape classes
class Shape2Perim implements IFunc<IShape, Double> {
	public Double apply(IShape s) {
		return s.beAppliedToBy(new ShapePerim());	
	}
}

// This uses Double Dispatch to apply it to shapes
// This receives a this and a method call and it applies it 
// No confusion 
class ShapePerim implements IShapeFunc<Double> { // Normally called ShapePerimVisitor	
	public Double applyToCircle(Circle c) {
		return 2 * Math.pi * c.radius;	
	}
	
	public Double applyToRect(Rect r) {
		return 2 * (r.w + r.h);	
	}
	
	public Double applyToSquare(Square s) {
		return 4 * s.w	;
	}
}


// IShape classes are asked what are they by Shape2Perim
// Pass on themselves to ShapePerim to their specific method
// Double is calculated

class Circle implements IShape {
	public <T> beAppliedToBy(IShapeFunc<T> func) {
		return func.applyToCircle(this);
	}
}

class Rect implements IShape {
	public <T> beAppliedToBy(IShapeFunc<T> func) {
		return func.applyToRect(this);
	}
}

class Square implements IShape {
	public <T> beAppliedToBy(IShapeFunc<T> func) {
		return func.applyToSquare(this);
	}
}

class ExamplesShape {
	IList<IShape> shapes = ....;
	IList<Double> perims = shapes.map(new Shape2Perim());
}

// ================================================================>
// 3/2/15
/*
Book =
String title
int price
Author author

Author =
String name
int yob
Book book
*/

class Book {
	/*
	FIELDS
	*/
	
	Book(String title, int price, Author author) {
		this.title = title;
		this.price = price;
		this.author = author;
		author.book = this;
	} 
}

//BAD
Book htdp = new Book("HtDP", 0, 
										  new Author("MP", 1950, 
										  new Book("HtDP", 0, ....)));
										  
Book htdp2 = new Book("HtDP", 0,
												new Author("MF", 1950, null))
			 this.htdp.author.book = this.htdp; // Assignment Statement = the power of generative recursion
			 // An Assignment Statement is a mutation 
			 // This brings up the idea of aliasing 
			 
// =====================================================================>
// 3/4/15

// Sidenote for lab
interface IListVisitor<T, R> {
	R visitCons(Cons<T> cons);
}

// MUTATION - create cycles of data
Book b1 = new Book("HP7", 20.99, null);
Author a1 = new Author("Rowling", 1966, b1);
					b1.author = a1; // Takes place of null

Book b2 = new Book("HP7", 20.99, null);
Author a2 = new Author("Rowling", 1966, b2);
     b2.author = b2;

// Now how do we do equality?
// If a method has a side effect, need purpose and affect
class Counter {
	int value;
	Counter() {
		this.vaule = 0;
	
	// Tick counter up by one = purpose
	// EFFECT: Changes this.value
	// No effect = no modify
	// Effect = modify just this
	int next() {
		this.value = this.value + 1;
		return this.value; 
	}
}

class Examples {
	Counter c1 = new Counter();
	Counter c2 = new Counter();
	// EFFECT initializes the data
	// No return - no types
	void initializeCounters() {
		this.c1 = new Counter(5);
		this.c2 = new Counter(5);
	}
/*	Counter x1 -> 1
	Counter x2 -> 1
	Counter x3 -> 2
	Counter x4 -> x1
THE ABOVE ARE COUNTEREXAMPLES */

// To test give counter an initialVal
// And define within method to give finite lifetimes
//SETUP -> TWEAK -> TEST
// Initialize test state, modify state, test side effects happens

boolean testCounter(Tester t) {
		// Counter c = new Counter();
		// Use initializeCounters() instead
		return t.checkExpect(c.next(), c.next());
		// False because I've stashed a hidden field in the second c
		}

	boolean testCounterS(Counter c1, Counter c2, Tester t) {
		return t.checkExpect(c1.next(), c2.next());
		// See counterexamples
		}
		
		// TESTS NOW RETURN VOID AND NO NEED FOR RETURN
	}
}