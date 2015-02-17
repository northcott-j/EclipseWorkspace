; **************** BEGIN INITIALIZATION FOR ACL2s B MODE ****************** ;
; (Nothing to see here!  Your actual file is after this initialization code);

#|
Pete Manolios
Fri Jan 27 09:39:00 EST 2012
----------------------------

Made changes for spring 2012.


Pete Manolios
Thu Jan 27 18:53:33 EST 2011
----------------------------

The Beginner level is the next level after Bare Bones level.

|#

; Put CCG book first in order, since it seems this results in faster loading of this mode.
#+acl2s-startup (er-progn (assign fmt-error-msg "Problem loading the CCG book.~%Please choose \"Recertify ACL2s system books\" under the ACL2s menu and retry after successful recertification.") (value :invisible))
(include-book "ccg/ccg" :uncertified-okp nil :dir :acl2s-modes :ttags ((:ccg)) :load-compiled-file nil);v4.0 change

;Common base theory for all modes.
#+acl2s-startup (er-progn (assign fmt-error-msg "Problem loading ACL2s base theory book.~%Please choose \"Recertify ACL2s system books\" under the ACL2s menu and retry after successful recertification.") (value :invisible))
(include-book "base-theory" :dir :acl2s-modes)

#+acl2s-startup (er-progn (assign fmt-error-msg "Problem loading ACL2s customizations book.~%Please choose \"Recertify ACL2s system books\" under the ACL2s menu and retry after successful recertification.") (value :invisible))
(include-book "custom" :dir :acl2s-modes :uncertified-okp nil :ttags :all)

;Settings common to all ACL2s modes
(acl2s-common-settings)

#+acl2s-startup (er-progn (assign fmt-error-msg "Problem loading trace-star and evalable-ld-printing books.~%Please choose \"Recertify ACL2s system books\" under the ACL2s menu and retry after successful recertification.") (value :invisible))
(include-book "trace-star" :uncertified-okp nil :dir :acl2s-modes :ttags ((:acl2s-interaction)) :load-compiled-file nil)
(include-book "hacking/evalable-ld-printing" :uncertified-okp nil :dir :system :ttags ((:evalable-ld-printing)) :load-compiled-file nil)

#+acl2s-startup (er-progn (assign fmt-error-msg "Problem setting up ACL2s Beginner mode.") (value :invisible))
;Settings specific to ACL2s Beginner mode.
(acl2s-beginner-settings)

; why why why why 
(acl2::xdoc acl2s::defunc) ; almost 3 seconds

(cw "~@0Beginner mode loaded.~%~@1"
    #+acl2s-startup "${NoMoReSnIp}$~%" #-acl2s-startup ""
    #+acl2s-startup "${SnIpMeHeRe}$~%" #-acl2s-startup "")


(acl2::in-package "ACL2S B")

; ***************** END INITIALIZATION FOR ACL2s B MODE ******************* ;
;$ACL2s-SMode$;Beginner
#|

CS 2800 Homework 4 - Spring 2015

The last problem in this homework requires you to develop a SAT
solver.  All you need to claim the Clay Institute $1M Millennium
prize for solving the P vs NP problem is to either come up with an
efficient algorithm or show that no such algorithm exists!

This homework is done in groups. The rules are:

 * ALL group members must submit the homework file (this file)

 * The file submitted must be THE SAME for all group members (we
   use this to confirm that alleged group members agree to be
   members of that group)

 * You must list the names of ALL group members below using the
   format below. If you fail to follow instructions, that costs
   us time and it will cost you points, so please read carefully.


Names of ALL group members: Raj Narayan, Victor Magierski, Jonathon Northcott

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

For this homework you will need to use ACL2s.

Technical instructions:

- open this file in ACL2s as hw04.lisp

- make sure you are in BEGINNER mode. This is essential! Note that you can
  only change the mode when the session is not running, so set the correct
  mode before starting the session.

- insert your solutions into this file where indicated (usually as "...")

- only add to the file. Do not remove or comment out anything pre-existing.

- make sure the entire file is accepted by ACL2s. In particular, there must
  be no "..." left in the code. If you don't finish all problems, comment
  the unfinished ones out. Comments should also be used for any English
  text that you may add. This file already contains many comments, so you
  can see what the syntax is.

- when done, save your file and submit it as hw04.lisp

- avoid submitting the session file (which shows your interaction with the
  theorem prover). This is not part of your solution. Only submit the lisp
  file!

Instructions for programming problems:

For each function definition, you must provide both contracts and a body.

You must also ALWAYS supply your own tests. This is in addition to the
tests sometimes provided. Make sure you produce sufficiently many new test
cases. This means: cover at least the possible scenarios according to the
data definitions of the involved types. For example, a function taking two
lists should have at least 4 tests: all combinations of each list being
empty and non-empty.

Beyond that, the number of tests should reflect the difficulty of the
function. For very simple ones, the above coverage of the data definition
cases may be sufficient. For complex functions with numerical output, you
want to test whether it produces the correct output on a reasonable
number if inputs.

Use good judgment. For unreasonably few test cases we will deduct points.

We will use ACL2s' check= function for tests. This is a two-argument
function that rejects two inputs that do not evaluate equal. You can think
of check= roughly as defined like this:

(defunc check= (x y)
  :input-contract (equal x y)
  :output-contract (equal (check= x y) t)
  t)

That is, check= only accepts two inputs with equal value. For such inputs, t
(or "pass") is returned. For other inputs, you get an error. If any check=
test in your file does not pass, your file will be rejected.

|#



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Propositional Logic
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#|
We use the following ascii character combinations to represent the Boolean
connectives:

  NOT     ~

  AND     &
  OR      +

  IMPLIES =>

  EQUIV   =
  XOR     <>

The binding powers of these functions are listed from highest to lowest
in the above table. Within one group (no blank line), the binding powers
are equal. This is the same as in class.

|#

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Simplification of formulas
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#|

There are many ways to
represent a formula. For example: 

p + (p => q)) 

is
equivalent to 

true 

For each of the following, try to find the simplest equivalent
formula. By simplest, we mean the one with the least number of
connectives and parentheses. You can use any unary or binary
connective shown above in the propositional logic section.

(A) ~ (~ p & ~ q)


(p + q)


(B) p & (p => q) & (q => r)


p & q & r


(C) (p + q) & (~ p & ~ q)

false

(D) (p & q & r) + (p & q & ~ r) + (p & ~ q & r) + (p & ~ q & ~ r)

p

(E) (p <> q) & (p <> r) & ~ p

~ p & q & r

|#

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Characterization of formulas
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#|

For each of the following formulas, determine if they are valid,
satisfiable, unsatisfiable, or falsifiable. These labels can
overlap (e.g. formulas can be both satisfiable and valid), so
keep that in mind and indicate all characterizations that
apply. In fact, exactly two characterizations always
apply. (Think about why that is the case.) Provide proofs of your
characterizations, using a truth table or simplification
argument (for valid or unsatisfiable formulas) or by exhibiting
assignments that show satisfiability or falsifiability.

(A) false =  (p => (p => q))

p  |  q  | p => q  |  (p => (p => q))   | false =  (p => (p => q))

F  |  F  |  T      |     T              |   F
F  |  T  |  T      |     T              |   F
T  |  F  |  F      |     F              |   T
T  |  T  |  T      |     T              |   F

Satisfiable & Falsifiable


(B) ~(((true & ~ p) & p) => p)

p  | ~p |(true & ~ p)  | ((true & ~ p) & p)  | (((true & ~ p) & p) => p) | ~(((true & ~ p) & p) => p)

F  |  T  | T   |    F     |   T  |  F
F  |  T  | T   |    F     |   T  |  F
T  |  F  | F   |    F     |   T  |  F
T  |  F  | F   |    F     |   T  |  F

Falsifiable & Unsatisfiable

(C) (p => q) = (~ q  & ~ p)

p  |  q  | p => q  | (~ q  & ~ p) | (p => q) = (~ q  & ~ p)

F  |  F  |  T      |     T     |   T
F  |  T  |  T      |     F     |   F
T  |  F  |  F      |     F     |   T
T  |  T  |  T      |     F     |   F

Satisfiable & Falsifiable

(D) [(p & q & r) + (p & q & ~ r)] => (p & q)

p  |  q  | r  | (p & q & r) | (p & q & ~ r) | [(p & q & r) + (p & q & ~ r)] | (p & q)  | [(p & q & r) + (p & q & ~ r)] => (p & q)

F  |  F  |  F  |  F         |   F           |              F                |     F    |    T 
F  |  T  |  F  |  F         |   F           |              F                |     F    |    T 
F  |  F  |  T  |  F         |   F           |              F                |     F    |    T 
F  |  T  |  T  |  F         |   F           |              F                |     F    |    T 
T  |  F  |  F  |  F         |   F           |              F                |     F    |    T 
T  |  T  |  F  |  F         |   T           |              T                |     F    |    F 
T  |  F  |  T  |  F         |   F           |              F                |     T    |    T 
T  |  T  |  T  |  T         |   F           |              T                |     T    |    T 

Satisfiable & Falsifiable

(E) ~ (p <> (~ q & q))

p  |  q  |  r  | ~q | (~ q & q) | (p <> (~ q & q)) | ~ (p <> (~ q & q))

F  |  F  |  F  |  T |   F       |    F             |     T   
F  |  T  |  F  |  F |   F       |    F             |     T   
F  |  F  |  T  |  T |   F       |    F             |     T   
F  |  T  |  T  |  F |   F       |    F             |     T   
T  |  F  |  F  |  T |   F       |    T             |     F   
T  |  T  |  F  |  F |   F       |    T             |     F   
T  |  F  |  T  |  T |   F       |    T             |     F   
T  |  T  |  T  |  F |   F       |    T             |     F   

Satisfiable & Falsifiable

|#

(set-defunc-termination-strictp nil)
(set-defunc-function-contract-strictp nil)
(set-defunc-body-contracts-strictp nil)
(set-defunc-timeout 80)
(acl2s-defaults :set cgen-timeout 2)

#|

A Decision Procedure for Satisfiability

For this exercise, you have to develop a decision procedure for
Boolean satisfiability.  The main function, satisfy, checks
whether a given propositional function is satisfiable. 

|#

; UnaryOp:   '~ means "not"

(defdata UnaryOp '~)

; BinaryOp: '& means "and", '+ means "or", '=> means "implies",
; and '== means "iff". 

(defdata BinaryOp (enum '(& + => ==)))

; PropEx: A Propositional Expression (PropEx) can be a boolean (t
; or nil), a symbol denoting a variable (e.g. 'p or 'q), or a
; list denoting a unary or binary operation. 

(defdata PropEx 
  (oneof boolean 
         symbol 
         (list UnaryOp PropEx) 
         (list PropEx BinaryOp PropEx)))

; IGNORE THESE THEOREMS. USED TO HELP ACL2S REASON
(defthm propex-expand1
  (implies (and (propexp x)
                (not (symbolp x)))
           (equal (second x)
                  (acl2::second x))))

(defthm propex-expand2
  (implies (and (propexp x)
                (not (symbolp x))
                (not (equal (first (acl2::double-rewrite x)) '~)))
           (equal (third (acl2::double-rewrite x))
                  (acl2::third (acl2::double-rewrite x)))))

(defthm propex-expand3
  (implies (and (propexp px)
                (consp px)
                (not (unaryopp (first px))))
           (and (equal (third px)
                       (acl2::third px))
                (equal (second px)
                       (acl2::second px))
                (equal (first px)
                       (acl2::first px)))))

(defthm propex-expand2a
  (implies (and (propexp x)
                (not (symbolp x))
                (not (unaryopp (first (acl2::double-rewrite x)))))
           (equal (third (acl2::double-rewrite x))
                  (acl2::third (acl2::double-rewrite x)))))

(defthm propex-lemma2
  (implies (and (propexp x)
                (not (symbolp x))
                (not (equal (first (acl2::double-rewrite x)) '~)))
           (and (propexp (first x))
                (propexp (acl2::first x))
                (propexp (third x))
                (propexp (acl2::third x)))))

(defthm propex-lemma1
  (implies (and (propexp x)
                (not (symbolp x))
                (equal (first (acl2::double-rewrite x)) '~))
           (and (propexp (second x))
                (propexp (acl2::second x)))))


(defthm first-rest-listp
  (implies (and l (listp l))
           (and (equal (first l)
                       (acl2::first l))
                (equal (rest l)
                       (acl2::rest l)))))

; END IGNORE

; An assignment is a mapping from symbols to booleans
(defdata Assignment (list symbol boolean))

; A list of assignments
(defdata Loa (listof Assignment))

; A list of symbols
(defdata Los (listof symbol))


;; Helper Function
;; Checks if given list contains element
(defunc contains? (e l)
  :input-contract (listp l)
  :output-contract (booleanp (contains? e l))
  (if (endp l)
    nil
    (or (equal (first l) e)
        (contains? e (rest l)))))

(check= (contains? 3 '(4 3 5)) t)
(check= (contains? 5 '(1 2 3 4)) nil)
(check= (contains? 7 '(71 72 73 74)) nil)
(check= (contains? 7 '(5 6 7 8 7 6)) t)

;; Removes doubles in given list
(defunc remdoubles (l)
  :input-contract (listp l)
  :output-contract (listp l)
  (if (endp l)
    l
    (if (contains? (first l) (rest l))
      (remdoubles (rest l))
      (cons (first l) 
            (remdoubles (rest l))))))

(check= (remdoubles '(1 2 3 4)) '(1 2 3 4))
(check= (remdoubles '(1 2 1 2)) '(1 2))
(check= (remdoubles '(1)) '(1))

; get-vars: PropEx Los -> Los
; get-vars returns a list of variables appearing in px,
;   including those in the provided accumulator acc. If acc has
;   no duplicates in it, then the returned list should not have
;   any duplicates either. See the check='s below.
;
; 
; Define get-vars.
; Hint: you can use a helper function


(defunc get-vars (px acc)
 :input-contract (and (PropExp px) (Losp acc))
 :output-contract (Losp (get-vars px acc))
 
 (cond ((booleanp px) acc)
       ((symbolp px) (cond ((endp acc)         (list px))
                           ((contains? px acc) acc)
                           (t                  (cons px acc))))
       ((endp px)           acc)
       ((equal (len px) 1)   (get-vars (first px) acc))
       ((equal (len px) 2)   (get-vars (second px) acc))
       ((equal (len px) 3)   (remdoubles (app (get-vars (first px) acc) 
                                               (get-vars (third px) acc))))))

 
  
; We define perm (permutation) to make the tests more robust. Otherwise, 
; some of the tests below may fail because the order in which variables appear
; does not matter. 

(defunc del (e l)
  :input-contract (listp l)
  :output-contract (listp (del e l))
  (cond ((endp l) l)
        ((equal e (first l)) (rest l))
        (t (cons (first l) (del e (rest l))))))
  
(defunc perm (a b)
  :input-contract (and (listp a) (listp b))
  :output-contract (booleanp (perm a b))
  (cond ((endp a) (endp b))
        ((endp b) nil)
        (t (and (in (first a) b)
                (perm (rest a) (del (first a) b))))))


(check= (perm (get-vars 'A '()) '(A)) t)
(check= (perm (get-vars 'A '(B C)) '(A B C)) t)
(check= (perm (get-vars '(A & B) '()) '(B A)) t)
(check= (perm (get-vars '((A & B) & (C + D)) '()) '(D C B A)) t)
(check= (perm (get-vars '(A & (B + A)) '()) '(B A)) t)
(check= (perm (get-vars '(A & (B + A)) '(B)) '(A B)) t)
(check= (perm (get-vars T '(H)) '(H)) t)
(check= (perm (get-vars T '(H A T)) '(H A T)) t)
(check= (perm (get-vars '(A + C) '(D E F)) '(A C D E F)) t)


; update: PropEx Symbol Boolean -> PropEx
; The update function updates a variable by replacing all instances
; of the variable with the boolean val in the expression px.

;
; Define update


(defunc update (px name val)
 :input-contract (and (PropExp px) (symbolp name) (booleanp val))
 :output-contract (Propexp (update px name val))
 
 (cond ((booleanp px) px)
       ((symbolp px) (if (equal px name)
                       val
                       px))
       ((endp px)                    px)
       ((equal (len px) 1)           (update (first px) name val))
       ((equal (len px) 2)           (list (first px)
                                           (update (second px) name val)))
       ((equal (len px) 3)           (list (update (first px) name val)
                                           (second px)
                                           (update (third px) name val)))))
       
(check= (update T 'A NIL) T)
(check= (update NIL 'A T) NIL)
(check= (update 'A 'B T) 'A)
(check= (update 'A 'A NIL) NIL)
(check= (update '((NIL + A) & (~ B)) 'A T) '((NIL + T) & (~ B)))
(check= (update nil nil nil) nil)
(check= (update '(A + A) 'A T) '(T + T))
(check= (update '(B + C) 'A Nil) '(B + C))

; beval: PropEx -> Booleanp
; beval evaluates a constant boolean expression and return its value.  
; A constant boolean expression is a PropEx with no variables, 
; just booleans and operators.  If the given PropEx does
; contain variables, you can return whatever you like.

(defunc beval (bx)
 :input-contract (PropExp bx)
 :output-contract (Booleanp (beval bx))
 (cond ((booleanp bx) bx)
       ((symbolp bx) nil)
       ((equal (len bx) 2) (not (beval (second bx))))
       ((equal (second bx) '&) (and (beval (first bx))
                                    (beval (third bx))))
       ((equal (second bx) '+) (or (beval (first bx)) 
                                   (beval (third bx))))
       ((equal (second bx) '==) (equal (beval (first bx)) 
                                       (beval (third bx))))
       ((equal (second bx) '=>) (if (beval (first bx))
                                  (beval (third bx))
                                  t))))


(check= (beval T) T)
(check= (beval NIL) NIL)
(check= (beval '(T & NIL)) NIL)
(check= (beval '(T & T)) T)
(check= (beval '(T + NIL)) T)
(check= (beval '(~ T)) NIL)
(check= (beval '((NIL & (~ NIL)) + NIL)) NIL)
(check= (beval '((T & (~ NIL)) + T)) T)
(check= (beval '((NIL & (~ NIL)) + T)) T)
(check= (beval '(T == NIL)) NIL)
(check= (beval '(T == T)) T)
(check= (beval '(NIL == NIL)) T)
(check= (beval '(T == NIL)) NIL)
(check= (beval '(T & (T & NIL))) NIL)
(check= (beval '(T & (NIL + (T & T)))) T)

; The type of result our sat solver returns.

(defdata SatisfyResult (oneof Loa 'unsat))#|ACL2s-ToDo-Line|#


; satisfy-remain: PropExp Los Loa -> SatisfyResult
; satisfy-remain checks a 
;   propositional expression for satisfiability, but also expects a list of
;   variables from the expression (this simplifies the definition) and
;   a list of assignments that have already been made (an accumulator).
;   The idea is that we construct the truth table ``on the fly'' by
;   picking the first variable in vars, assigning it to t, substituting
;   t for px, and recurring. If that gives a satisfying assignment,
;   return it; otherwise, try assigning the var to nil. That's
;   intentionally vague, as this is the interesting part of the exercise.
;   Look at how satisfy-remain is called in satisfy.

(defunc satisfy-remain (px vars assignments)
 :input-contract (and (PropExp px) (Losp vars) (Loap assignments))
 :output-contract (SatisfyResultp (satisfy-remain px vars assignments))
 
 (cond ((booleanp px) (if (equal px T) 
                        assignments
                        'unsat))
       ((symbolp px) (satisfy-remain (beval (update px (first vars) T))
                                     vars
                                     (app (list (first vars) T)
                                          assignments)))
       
       ((equal (len px) 1) (satisfy-remain (first px) 
                                           vars
                                           assignments))
       ((equal (len px) 2) (satisfy-remain (first px) 
                                           vars
                                           assignments))
       ((equal (len px) 3) (satisfy-remain (first px) 
                                           vars
                                           assignments))))
       
        


(check= (satisfy-remain T   '() '((A T))) '((A T)))
(check= (satisfy-remain NIL '() '((A T))) 'unsat)
(check= (perm (satisfy-remain '(A & B) '(A B) '()) '((B T) (A T))) t)
(check= (perm (satisfy-remain '(A + (B & C)) '(A B C) '()) '((A T) (B T) (C T))) t)

; satisfy: PropEx -> SatisfyResult
; This is our decision procedure for satisfiability
; Satisfy attempts to find a set of assignments to the variables in px
;  such that the expression will evaluate to true. If successful, returns
;  an assignment in the form of a list of assignments. If px is unsat,
;  returns 'unsat.  This function just calls satisfy-remain to do the work.

(defunc satisfy (px)
 :input-contract (PropExp px)
 :output-contract (SatisfyResultp (satisfy px))
 (satisfy-remain px (get-vars px '()) '()))

(check= (satisfy 'P) '((P T)))
(check= (satisfy '(~ P)) '((P NIL)))
(check= (satisfy '(P & (~ P))) 'unsat)
(check= (satisfy '(P + (~ P))) '((p t)))
(check= (perm (satisfy '((A + (~ B)) & (~ C))) '((A T) (B T) (C NIL))) t)
(check= (satisfy T) NIL)
(check= (satisfy NIL) 'unsat)
(check= (satisfy '(~ ((a => b) == ((~ b) => (~ a))))) 'unsat)
