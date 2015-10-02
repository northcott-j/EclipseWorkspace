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

/*Outer private is visible to Nested
Nested private is visible to Outer
Nested classes can enforce invarients
Also .create(), never need to know obj name - think abstract class

When should we use static:
Fields - one variable for the whole class rather than one per object
		 CONSTANTS
Methods - factory methods - don't depend on an instance
Classes - really tight associations i.g. List.iterator 
*/

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



*/