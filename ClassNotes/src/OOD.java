// 9/18/15

// A publication is either a Book or an Article
// A Book has:
// FIELDS:
// - author, year, title, pages, isbn, genre, publisher, loc
// A Book is a (make-book String PosInt String PosInt String String String String)
(define-struct book (author year title pages isbn genre pub loc))
// An Article has:
// - title, author, journal, year, issue, pages, vol
// An Article is a (make-article String String String PosInt PosInt String PosInt)
(define-struct article (title author journal year issue pages vol))

(define rushdie
	(make-book "Salman Rushdie" 1980 "Midnight's Children" 350 "5" "historical fantasy" "Jonathan Cape" "London"))
(define turing 
	(make-article "Computing machinery and intelligence" "A. M. Turing" "Mind" 1950 59 "dunno" 236))

// CHECK EXPECTS HERE

// TEMPLATE
(define (process-pub pub)
	(cond
		[(book? pub) ... (book-title pub) ...]
		[(article? pub) ... (article-pages pub) ...]))

// cite-apa: Renders the given publication in APA citation format
// cite-apa: Publication -> String
(define (cite-apa pub) (format "~a (~a). ~a. ~a: ~a" ...arguments))
(define (cite-apa pub) ...)

//////////////////////TRY TWO///////////////////////////////////////

// A Publication is something that can:
/// -- be cited as apa or mla
// A Publication is a CitationFormat -> String

//////////////////////TRY THREE/////////////////////////////////////
/*
* Represents a citation
*/
interface Publication {

	/**
	* Formats the current citation in APA style
	*@return the formatted citation (purpose statement of return value)
	*/
	String cite-apa();
	/**
	* Formats the current citation in MLA style
	*@return the formatted citation (purpose statement of return value)
	*/
	String cite-mla();
}

class Book {
	//Represents a Book that can be cited
	
}

// 10/2/15
/*
static means at compile time
gonna belong to a class not object


*/

// On Fields
private int widgetId;
private static int widgetIdCounter;

// On Methods

public void getWidgetId();
public static void resetWidgetId();

// Able to have a static class

Outer private is visible to Nested
Nested private is visible to Outer
Nested classes can enforce invarients
Also .create(), never need to know obj name - think abstract class

When should we use static:
Fields - one variable for the whole class rather than one per object
		 CONSTANTS
Methods - factory methods - don't depend on an instance
Classes - really tight associations i.g. List.iterator 


/*
ARRAYS:
t[] is a mutable, fixed-length, constant time indexed, sequence of values of type t
*/

int[] array1 = new int[] { 2, 4, 5, 8 };
int[] array2 = new int[64];
int[] array3 = { 2, 4, 6, 8 };
assertEquals( 2, array1[0] );
array1[1] = -78; //Mutation

// ALIASING
int[] a1 = new int[16];
int[] a2 = new int[16];
int[] a3 = a1;
// Check equals
assertArrayEquals( a1, a3 );

/*
USe arrays if API has it
Ensure fixed length
Efficiency, especially when implementing higher level data structures
*/

// CHARACTERS
/*
\ is the escape character
*/

// Primitive types vs Reference types
/*

PRIMS:
boolean
byte
short
char
int
float
long
double

Boxed - obj:
Class with single field of prim types
Boolean
Byte
Short
Character
Integer
Float
Long
Double
*/

/*
SWITCH
ints
character
string
*/

// switch (c) {
// 	case 's':
// 		//method
// 		break;
// 	case 'S':
// 		//method
// 		break;
// 	default:
// 		unrecognized();
// }

// Enumerations
/*

- a Special kind of class
- Creates a finite set of named values
- Represents a small, fixed set

*/

enum Status {
	Playing, Stalemate, Won
}

enum Binop {
	Add, Sub, Mul, Div, Pow
}

double performBinop(Binop op, double a, double b) {
	switch (op) {
		case Add: return a + b;
		case Sub: return a - b;
		default: something;
	}
}

enum UsCoin {
	Penny(1), Nickel(5);

	private final int cents;
	public int getCentsValue() {
		return cents;
	}

	UsCoin(int cents) {
		this.cents = cents;
	}
}

// 10/6/15
/*

Two kinds of exceptions: checkable and unchecked
unchecked -> error or runtimeexception
checked -> Exception should be planned for

try and catch methdos
Don't catch and ignore or catch and release
*/

// 10/9/15
// Encapsulation
/*

Final protects from change 
Safety properties - bad things never happen
Liveness properties - good things eventually happen
Use private final and rely entirely on the interface
Principle of least privilege - do whatever with as little as possible

private - same class only
defualt - and everything else in the package
protected and subclasses
public - the entire world

CLASS INVARIANTS
final gets rid of mutablity problem
Constructor can enforce input information

A class invariant is a logical statement about the state of an object
that is ensured by the constructors and preserved by the methods

Comment and Javadoc INVARIANTS
*/

// 10/13/15

/*

Want one constuctor with extreme flexibility and is safe / convenient

Two classes: Easy to build and then easy to use

USE A NESTED CLASS BECAUSE TIGHTLY INTERTWINED
Make constructor of upper level class private

patterns:
BUILDER
FACTORY
SINGLETON
*/

/**
* helps us build a connectNmodel
*/
public status class Builder {
	private int width, height goal;
	private ArrayList<String> players;
	public Builder() {
		this.width = 7;
		this.height = 6;
		this.goal = 4;
		this.players = new ArrayList<String>();
		this.players.add("Red");
		this.players.add("Black");
	}

	public ConnectNModel build() {
		return new ConnectNModel(this.width, this.height, this.goal, this.players.toArray(new String[0]));
	}

	public Builder setWidth(int w) {
		this.width = w;
		return this;
	}

	public Builder setHeight(int h) {
		this.height = h;
		return this;
	}

	public Builder setGoal(int g) {
		this.goal = g;
		return this;
	}

	public Builder addPlayer(String name) {
		this.players.add(name);
		return this;
	}

	public Builder clearPlayers() {
		this.players.clear();
		return this;
	}

	public Builder setPlayers(String... names) {
		this.players.clear();
		this.players.addAll(Arrays.asList(names));
		return this;
	}
}

public class Test {
	void testBuilder() {
		ConnectNModel.Builder b =
			new ConnectNModel.Builder()
				.setGoal(5)
				.setHeight(10)
				.setWidth(8);
	ConnectN m1 = b.build();
	}
}

// Combine code from several libraries 

/*
Make a class that extends one and implements the other
CALLED ADAPTERS
*/