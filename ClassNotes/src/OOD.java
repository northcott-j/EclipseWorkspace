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