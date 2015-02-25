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

CS 2800 Homework 5 - Spring 2015

This homework is done in groups. The rules are:

 * ALL group members must submit the homework file (this file)

 * The file submitted must be THE SAME for all group members (we
   use this to confirm that alleged group members agree to be
   members of that group)

 * You must list the names of ALL group members below using the
   format below. If you fail to follow instructions, that costs
   us time and it will cost you points, so please read carefully.


Names of ALL group members: Raj Narajan, Victor Magierski, Jonathan Northcott

Note: There will be a 10 pt penalty if your names do not follow 
this format.

You should modify the marked portions of this file in accordance
with the instructions in comments like these. Please delete or
comment out any parts you haven't completed by turn-in
time. Before submitting, make sure that the extension of this
file remains .txt.

|#

#|

Question 1: Applying a substitution.

Below you are given a set of ACL2 terms and substitutions. Recall
that a substitution is a list of 2-element lists, where the first
element of each list is a variable and the second an
expression. Also, variables can appear only once as a left
element of a pair in any substitution. For example, the
substitution ((y (cons a b)) (x m)) maps y to (cons a b) and x to
m. For each term/substitution pair below, show what you get when
you apply the substitution to the term (i.e., when you
instantiate the term using the given substitution). 

a. (app x y)
   ((x (app b a)) (y (list w)))
   
   (app (app b a) (list w))

b. (app x 'y)
   ((x (cons a nil)) (y (cons c nil)))
   
   (app (cons a nil) 'y)

c. (* (+ x (/ y (len z))) (+ (len z) 12))
   ((x (+ a b)) (y (* a b)) (z '(3 4)))

   (* (+ a b) (/ (* a b) (len '(3 4))) (+ (len '(3 4)) 12))
   
d. (cons (rev2 (app x y)) z)
   ((x (list 3)) (y (rev2 b)) (z (app b c)))
   
   (cons (rev2 (app (list 3) (rev2 b))) (app b c))

e. (or (endp x) (listp (app x y)))
   ((x nil) (y (list (first a))) )
   
   (or (endp nil) (listp (app nil (list (first a)))))

f. (equal (+ (+ (len x) (len y)) (len z)) (len (cons 'z (app x y))))
   ((x '(1 2)) (y '(3 4)) (z '(5)))
   
   (equal (+ (+ (len '(1 2)) (len '(3 4)) (len '(5))) (len (cons 'z (app '(1 2) '(3 4))))))


Question 2: Finding a substitution, if it exists.
For each pair of ACL2 terms, give a substitution that instantiates the first to
the second. If no substitution exists write "None".

a. (in a b)
   (in (first x) (app x y)) 
   
   ((a (first x)) (b (app x y)))

b. (app (rev2 x) y)
   (app b (rev2 c))
   
   none

c. (app (rev2 a) (list b))
   (app (rev2 (cons y (list (first x)))) (list (+ (len (rest x)) z)))

   ((a (cons y (list (first x)))) (b (+ (len (rest x)) z)))
   
d. (add x y)
   (expt 2 3)
   
   none

e. (app x (app w nil))
   (app c (app a b))

   ((x c) (w a) (nil b))
   
f. Here fib-n is a function that takes one argument
   (+ (+ (/ z w) (- x (+ x 2))) (- u 4))
   (+ (+ (/ (fib-n 10) (- (fib-n 10) (+ (fib-n 10) 2)))  (- y (+ y 2))) (- y 4))

   ((z (fib-n 10)) (w (- (fib-n 10))) (x (fib-n 10)) (u y))
   
g. (app x x)
   (app w z)

   none
   
h. (cons z (app (list z)  x))
   (cons (- (expt 3 4) w) (app (list (- (expt 3 4) w)) (rev2 (list 3))))
   
   ((z (- (expt 3 4) w)) (x (rev2 (list 3))))

|#

#|

Questions 3 to 6 ask for equational proofs about ACL2
programs. When writing your equational reasoning be sure to
justify each step in the style shown in class, eg.

 (sum (app x y))
= { Def app, (consp x) }
 (sum (cons (first x) (app (rest x) y)))

You can use basic arithmetic facts for free, but in the 
justification write "arithmetic", e.g.,

 (first x) + (sum (rest x)) + (sum y) + 0
= { Arithmetic }
 (sum (rest x)) + (sum y) + (first x)

Notice also that it is OK to use infix notation (like x+y+z) for
arithmetic operators instead of the standard prefix notation
(like (+ x (+ y z)) or (+ x y z))

Here are the common defintions used for questions 3 to 6:

(defunc len2 (x)
  :input-contract (listp x)
  :output-contract (natp (len2 x))
  (if (endp x)
      0
    (+ 1 (len2 (rest x)))))

(defunc app2 (x y)
  :input-contract (and (listp x) (listp y))
  :output-contract (listp (app2 x y))
  (if (endp x)
      y
    (cons (first x) (app2 (rest x) y))))

(defunc rev2 (x)
  :input-contract (listp x) 
  :output-contract (listp (rev2 x))
  (if (endp x)
      nil
    (app2 (rev2 (rest x)) (list (first x)))))

Recall that for each of the defunc's above we have both a
definitional axiom, and a contract theorem. For example for len2,
we have the definitional axiom:

(implies (listp x)
         (equal (len2 x)
                (if (endp x)
                    0
                  (+ 1 (len2 (rest x))))))

The contract theorem is:

(implies (listp x)
         (natp (len2 x)))

You can use definitional axioms and contract theorems for free,
i.e., you don't have to prove anything.

By the way ACL2s can prove all of these theorems trivially.

For the rest of your homework, you can assume that the following
is a theorem

(implies (and (listp x) 
              (listp y))
         (equal (len2 (app2 x y))
                (+ (len2 x) (len2 y))))

                
Question 3:

a. Prove the following using equational reasoning:

(implies (and (listp x) 
              (listp y))
         (equal (len2 (app2 x y)) 
                (len2 (app2 y x))))

C1: (listp x)
C2: (listp y)
Theorem 1: (implies (and (listp x) 
                         (listp y))
         (equal (len2 (app2 x y))
                (+ (len2 x) (len2 y))))
                
                
                
         (len2 (app2 x y))

         ={Theorem 1, C1, C2}
          (len2 x) + (len2 y)
         
         ={Arithmetic}
         (len2 y) + (len2 x)
         
         ={Theorem 1, C1, C2}
          (len2 (app y x))
                
                
b. Given that x and y are lists prove the following using
equational reasoning: 

(implies (and (listp x) 
              (listp y))
         (implies (equal (len2 (app2 x y)) 0)
                  (equal (len2 (app2 y x)) 0)))

BTW, you can assume 3a is a theorem whether or not you proved it.


C1. (listp x)
C2. (listp y)
T1. (implies (and (listp x) 
                  (listp y))
         (equal (len2 (app2 x y)) 
                (len2 (app2 y x))))
                
                
                (equal (len2 (app2 x y)) 0)
                ={T1, C1, C2}
                (equal (len2 (app2 y x) 0))
                
|#

#|
Question 4 :

Consider the following definition:

(defunc fib (n)
  :input-contract (natp n)
  :output-contract (natp (fib n))
  (cond ((equal n 0) 0)
        ((equal n 1) 1)
        (t (+ (fib (- n 1))
              (fib (- n 2))))))

Prove the following using equational reasoning:

(implies (natp n)
         (and (implies (equal n 0)
                       (natp (fib n)))
              (implies (and (not (equal n 0))
                            (equal n 1))
                       (natp (fib n)))
              (implies (and (not (equal n 0))
                            (not (equal n 1))
                            (natp (fib (- n 1)))
                            (natp (fib (- n 2))))
                       (natp (fib n)))))

C1. (natp n)
C2. (natp (fib n)) {from output contract of (fib n)}

(natp (fib n))

={C1, C2}
true


|#

#|

Question 5: Prove:

(implies (listp x) 
     (and (implies (endp x)
                   (equal (len2 (rev2 x))
                          (len2 x)))
              (implies (and (not (endp x))
                          (equal (len2 (rev2 (rest x)))
                                 (len2 (rest x))))
               (equal (len2 (rev2 x))
                      (len2 x)))))

           
C1. (listp x)
C2. (endp x)
C3. (not (endp x))
C4. (equal (len2 (rev2 (rest x)))
           (len2 (rest x)))
Derived Contexts:
C5. x = nil {C1, C2}

We show that 

(implies (and (not (endp x))
                          (equal (len2 (rev2 (rest x)))
                                 (len2 (rest x))))
               (equal (len2 (rev2 x))
                      (len2 x))) 
                      
is false.
                      
                      
                (implies (and (not (endp x))
                              (equal (len2 (rev2 (rest x)))
                                     (len2 (rest x))))
                   (equal (len2 (rev2 x))
                          (len2 x)))
                      
                      = {C5}
   LHS:            (and (not (endp nil))
                        (equal (len2 (rev2 (rest x)))
                               (len2 (rest x))))
                      = {Definition of endp, def. of not}
                    (and nil
                        (equal (len2 (rev2 (rest x)))
                               (len2 (rest x))))
                      = {nil /\ x = nil}
                   nil   
                
                 { def. of implies}
                 
                 true
                 

              
|#

#|

Question 6: Prove:

(implies (and (listp x) 
              (listp y))
         (equal (len2 (rev2 (app2 x y)))
                (len2 (app2 (rev2 y) (rev2 x)))))

You can assume that the following is a theorem:

(implies (listp x) 
         (equal (len2 (rev2 x)) 
                (len2 x)))
                

C1. (listp x)
C2. (listp y)
T1. (implies (listp x) 
             (equal (len2 (rev2 x)) 
                    (len2 x)))
T2. (implies (and (listp x) 
                  (listp y))
         (equal (len2 (app2 x y))
                (+ (len2 x) (len2 y))))
                
                
                    (len2 (rev2 (app2 x y))) = (len2 (app2 (rev2 y) (rev2 x)))
                    
                    RHS: 
                    (len2 (app2 (rev2 y) (rev2 x)))
                    
                    ={T2, C1, C2}
                    (len2 (rev2 y)) + (len2 (rev2 x))
                    
                    ={Arithmetic}
                    (len2 (rev2 x)) + (len2 (rev2 y))
                    
                    ={T1, C1, C2}
                    (len2 x) + (len2 y)
                    
                    ={T2}
                    (len2 (app2 x y))
                    
                    ={C1, C2, Output Contract of app2, T1}
                    (len2 (rev2 (app2 x y)))
                    
                    = LHS
                    
|#

#|
Question 7:

Consider the following  definitions:

(defunc mapcons (a x)
  :input-contract (listp x)
  :output-contract (listp (mapcons a x))
  (if (endp x)
      nil
    (cons (list a (first x))
          (mapcons a (rest x)))))

(defunc cart-prod (x y)
  :input-contract (and (listp x) (listp y))
  :output-contract (listp (cart-prod x y))
  (if (endp x)
      nil
    (app2 (mapcons (first x) y) 
          (cart-prod (rest x) y))))

Prove the following:

(implies (and (listp x)
              (listp y))
         (and (implies (endp x)
                       (equal (len2 (cart-prod x y))
                              (* (len2 x) (len2 y))))
              (implies (and (not (endp x))
                            (equal (len2 (cart-prod (rest x) y))
                                   (* (len2 (rest x)) (len2 y))))
                       (equal (len2 (cart-prod x y))
                              (* (len2 x) (len2 y)))))))

You can assume that the following is a theorem:

(implies (and (listp x)
              (listp y))
         (equal (len2 (mapcons a x))
                (len2 x)))
           
                
Part 1: 
we prove that (implies (endp x)
                       (equal (len2 (cart-prod x y))
                              (* (len2 x) (len2 y))))
is true.

C1. (listp x)
C2. (listp y)
C3. (endp x)
Derived Context:
C4. x = nil {C1, C3}

              (len2 (cart-prod x y)) = (len2 x) * (len2 y)
              
              ={Def Cart-prod, C3}
              (len2 nil)     = (len2 nil) * (len2 y)
              
              ={Def of len2}
              0 = 0 * (len2 y)
              
              ={arithmetic}
              0 = 0
              
              ={Def of =}
              True

Part 2:
We prove that (implies (and (not (endp x))
                            (equal (len2 (cart-prod (rest x) y))
                                   (* (len2 (rest x)) (len2 y))))
                       (equal (len2 (cart-prod x y))
                              (* (len2 x) (len2 y))))
                              
is true.

C1. (not (endp x))
C2. (equal (len2 (cart-prod (rest x) y))
           (* (len2 (rest x)) (len2 y)))
Derived Context:
C3. (consp x) {C1, Definition of endp}
T1. (implies (and (listp x)
                  (listp y))
         (equal (len2 (mapcons a x))
                (len2 x)))              {Given}
T2. (implies (and (listp x) 
                  (listp y))
         (equal (len2 (app2 x y))
                (+ (len2 x) (len2 y)))) {Given}

                
                     (equal (len2 (cart-prod x y))
                            (* (len2 x) (len2 y)))
                   
                    ={Def. of cart-prod, C1}
                   (len2 (app2 (mapcons (first x) y)
                               (cart-prod (rest x) y))) = (len2 x) * (len2 y)
                    
                    ={T2}
                    (len2 (mapcons (first x) y)) + (len2 (cart-prod (rest x) y)   = (len2 x) * (len2 y)
                    
                    ={C2}
                    (len2 (mapcons (first x) y)) + (len2 (rest x)) * (len2 y) = (len2 x) * (len2 y)
                    
                    ={T1, Input Contract of Len2 means y must be a list}
                    (len2 y) + (len2 (rest x)) * (len2 y) = (len2 x) * (len2 y)
                    
                    ={arithmetic}
                    (len2 y) * (1 + (len2 (rest x)))  =   (len2 x) * (len2 y)
                    
                    ={Definition of len2}
                    (len2 y) * (len2 x)  = (len2 x) * (len2 y)
                    
                    ={Propositional Logic}
                    True
                    
                    
We showed that the left hand side of 

(implies (and (listp x)
              (listp y))
         (and (implies (endp x)
                       (equal (len2 (cart-prod x y))
                              (* (len2 x) (len2 y))))
              (implies (and (not (endp x))
                            (equal (len2 (cart-prod (rest x) y))
                                   (* (len2 (rest x)) (len2 y))))
                       (equal (len2 (cart-prod x y))
                              (* (len2 x) (len2 y)))))))
                              

is equivalent to 

(implies (and (listp x) (listp y))  
         (and t t))
         
We now will prove that this is true:

C1. (listp x)
C2. (listp y)

     ={Def of implies, C1, C2}
     
     (and t t ) => (and t t)
     
     {definition of and, def of implies}
     
     ------
    ||true||
     ------
     
                    
                    
|#
