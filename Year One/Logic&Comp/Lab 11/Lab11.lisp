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

CS 2800 Homework 11 - Spring 2015

As noted on Piazza, this homework is optional.

This homework is done in groups. The rules are:

 * ALL group members must submit the homework file (this file)

 * the file submitted must be THE SAME for all group members (we use this
   to confirm that alleged group members agree to be members of that group)

 * You must list the names of ALL group members below, using the given
   format. If you fail to follow these instructions, it costs us time and
   it will cost you points, so please read carefully.

Names of ALL group members: FirstName1 LastName1, FirstName2 LastName2, ...

There will be a 10 pt penalty if your names do not follow this format.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

For (parts of) this homework you will need to use ACL2s.

Technical instructions:

- open this file in ACL2s as hw11.lisp

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

- when done, save your file and submit it as hw11.lisp

- avoid submitting the session file (which shows your interaction with the
  theorem prover). This is not part of your solution. Only submit the lisp
  file.

|#

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; ADMISSIBILITY
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#|

1. Consider the following function definition:

(defunc f (x y z)
  :input-contract (and (natp x) (natp y) (listp z))
  :output-contract (natp (f x y z))
  (cond ((equal x 0) (+ y (len z)))
        ((endp z)    (+ x y))
        ((<= y 1)    (+ x (f (- x 1) 0 (rest z))))
        ((< x y)     (f x (- y 2) z))
        (t           (f x y (rest z)))))

(a) Formulate the contract theorem for the above function definition in
propositional logic. You may use ACL2 expressions such as (equal x 0) as
propositional atoms in this formula. (You do not need to prove anything in
this part.)

...

...

(b) The body contract rule states: "The body contracts of all functions must
  hold, provided the input contract of f holds."

More precisely: for every function call (g arg1 ... argk) occurring in the
body of f, the input contract of g --- applied to the actual arguments arg1
... argk in the call -- holds, provided that (i) the input contract of f
holds, AND (ii) the conditions that lead to that call to g hold.

Formalize this condition in propositional logic, for ALL clauses of the
cond expression. Even if parts of the condition are "trivial", write them
down. (You do not need to prove anything in this part.)

...

...

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

2. Consider the following function definition:

(defunc h (a b c)
  :input-contract (and (natp a) (integerp b) (listp c))
  :output-contract (natp (h a b c))
  (cond ((equal a 0) (len c))
        ((endp c)    (+ a b))
        ((<= b 1)    (+ a (h (- a 1) (+ b 1) (rest c))))
        ((< a b)     (h a (- b 2) c))
        (t           (h a b (rest c)))))

(a) One condition of the Definitional Principle of ACL2s states: "The body
contracts hold under the assumption that the input contract holds."
Formalize this condition as a logical conjecture, but only for the cond
clause guarded by (< a b). Your conjecture must involve the condition that
leads to that cond clause. Do not omit parts that appear to be trivial.

...

(b) Now simplify the conjecture in (a), using propositional reasoning and
basic arithmetic facts. Is the conjecture true? A formal proof is not
required.

...

...

(c) Despite all of this effort, the function is not admitted by ACL2s.
Explain which condition exactly is violated, and give an input that
illustrates this violation.

...

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

3. Consider the following function definition:

(defunc f (x)
  :input-contract (integerp x)
  :output-contract (and (natp (f x)) (evenp (f x)))
  (cond ((> x 5)   (+ 1 (f (- x 1))))
        ((< x -5)  (- 2 (f (+ x 1))))
        ((evenp x) x)
        (t         (+ 3 (f (* 3 x))))))

You may assume that f is a fresh function symbol, and that evenp returns
true whenever its argument is an even integer.

(a) Do the body contracts hold, under the condition that the input contract
holds? If yes, state so, and give a brief justification. If no, provide an
integer input x such that the call (f x) eventually leads to a body
contract violation.

...

(b) Determine the set TERM of all inputs x such that (f x) terminates. If
this set is infinite, describe it suitably. If it is finite, enumerate its
elements. You do not have to prove termination in this problem.

TERM = ...

(c) Does the contract theorem hold on the inputs determined in (b)? If yes,
state so, and give a brief justification. If no, provide an integer input x
identified in (b) such that the call (f x) produces a value that violates
the output contract.

...

|#

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; TERMINATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#|

4. Existence of measure functions.

In order to prove termination of recursive functions, we use the following fact:

(*) "If f has a measure function, then f is terminating."

Now consider the converse of this fact:

(**) "If f has no measure function, then f is non-terminating."

Of course, an implication and its converse are not generally equivalent:
a => b is not equivalent to b => a.

However, let's see whether (**) is true nonetheless. We recall that an
implication *is* equivalent to its contrapositive: b => a is equivalent to
~a => ~b . Since (**) has a lot of negations in it, let us simplify it to
its (equivalent) contrapositive:

(***) "If f is terminating, then f has a measure function."

Make sure you understand why (***) and (**) are logically equivalent.

Now show that (***) is true. Assume that the single-argument function f(x)
is terminating, and DESCRIBE IN ENGLISH a possible measure function m, as
follows:

- given an input x, the measure function must return a natural number on
  input x. What is that natural number?

- explain why the measure is guaranteed to decrease in each recursive call.

You DO NOT have to provide ACL2 code for m. (In fact, you cannot, without
knowing f.) So the one measure function condition that we ignore in this
problem is that m must be admissible. You only have to be able to explain
the two bullet points above.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

5. Consider

(defunc f(x)
  :input-contract (natp x)
  :output-contract (natp (f x))
  (cond ((<= x 1)  0)
        ((evenp x) 1)
        (t         (f x))))

Function evenp returns T whenever its integer argument is even.

(a) Show that this function is non-terminating, by specifying an input x on
which f does not terminate. Briefly explain why not.

...

(b) Now give an alternative argument that this function is non-terminating.
Namely, prove non-termination via "proof by contradiction", as follows.

(b1) Suppose f was terminating. Now use property (***) proved in part 4. By
Modus Ponens, we conclude that there is a measure function m for f. Then m
and f satisfy the condition that the measure decreases in recursive calls.
Write down that complete condition as a formal conjecture, for the only
recursive call that exists in f. Remember what must be part of this
conjecture.

...

(b2) Prove that the conjecture identified in (b1) is not valid no matter
what the measure function m is. Do so by providing a counterexample, and by
explaining the violation. The counterexample must be independent of the
choice of m; in fact it must "work" without knowing m.

If you accomplish this, you have reached a contradiction. Hence, via proof
by contradiction you have shown that f is non-terminating.

...

|#

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Substitutions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#|

6.

(a)

Let sigma1 = ((a b)) and sigma2 = ((b a)). Then, for any formula phi,
(phi|sigma1)|sigma2 = phi. True or false? If true, explain why. If false,
give a counterexample.

...

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(b)

Let sigma = ((a b) (b a)). Then, for any formula phi, (phi|sigma)|sigma
= phi. True or false? If true, explain why. If false, give a
counterexample.

...

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(c)

For any formula phi and any substitution sigma, (phi|sigma)|sigma =
phi|sigma . True or false? If true, explain why. If false, give a
counterexample.

...

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(d)

True or false: For any terms t1 and t2, and any formula phi,
phi|((v1 t1) (v2 t2)) = phi|((v2 t2) (v1 t1))

...

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(e)

Give an example of two formulas f and g and a substitution sigma such that
all of the following hold: (i) f and g are *not* logically equivalent, (ii)
f|sigma = g, and (iii) g|sigma = f.

...

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(f)

Give an example of a formula alpha and a substitution sigma such that the
following statement is *false*: "alpha is valid if and only if the formula
alpha|sigma is valid." Explain why the "if and only if" does not hold.

...

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(g)

Give an example of a formula g such that, for any substitution
sigma, the formula g|sigma is *not* valid.

...

|#

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Induction
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


#|

7. Prove that every number in the sequence 1003, 10013, 100113, ....
0 = 0
1 = 9010
2 = 90100
3 = 901000
is divisible by 17.

To this end, write an ACL2 function seq : Nat -> Nat that, for a natural
number i, produces the ith number in the above sequence. That is:



Now prove:

(natp i) => 17|(seq i)

The notation "17|x" denotes the fact that x is divisible by 17. To
demonstrate that a number x is divisible by 17, you have to show that x can
be written in the form x = 17 * y , for some natural number y.

|#
(defunc expt (a b)
  :input-contract (and (natp a) (natp b))
  :output-contract (natp (expt a b))
  (if (equal b 0) 1 (* a (expt a (- b 1)))))

(check= (expt 10 2) 100)
(check= (expt 10 4) 10000)

(defunc seq (x) 
  :input-contract (natp x)
  :output-contract (natp (seq x))
  (if (equal x 0) 1003
      (+ (* (expt 10 x) 901) (seq (- x 1)))))


(check= (seq 0) 1003)
(check= (seq 1) 10013)
(check= (seq 2) 100113)
(check= (seq 3) 1001113)
(check= (integerp (/ (seq 3) 17)) t)

(test? (implies (natp i) (integerp (/ (seq i) 17)))) 
(test? (implies (and (natp x) (natp y) (natp z) (not (equal z 0)) (integerp (/ x z))
       (integerp (/ (* x y) z)))))#|ACL2s-ToDo-Line|#

                                                               

#|
PHI = (integerp (/ (seq x) 17))

If seq is divisible by 17, the quotient will be an integer

INDUCTION SCHEME FOR SEQ:
I.
(not (natp x)) => (integerp (/ (seq x) 17))
C1. (natp x)

nil => true

II.
(natp x) ^ (equal x 0) => (integerp (/ (seq x) 17))
C1. (natp x)
C2. (equal x 0)

      ={Evaluate seq, C2
      
(integerp (/ 1003 17))

true

III.
INDUCTION INTERESTING CASE of SEQ:
(natp x) ^ ~(equal x 0) ^ phi|((x (- x 1))) => (integerp (/ (seq x) 17))

C1. (natp x)
C2. ~(equal x 0)
C3. (natp (- x 1)) => (integerp (/ (seq (- x 1)) 17))
C4. (natp (- x 1) {C1, C2
C5. (integerp (/ (seq (- x 1)) 17)) {MP, C4
C6. (natp 17) {Def. of natp
C7. (natp (* (expt 10 x) 901)) {def. expt, multiplication, (natp 901)
C8. (not (equal 17 0)) {def. equals
C9. (integerp (/ 901 17)) {def. integer, arithmetic
C10. (integerp (/ (* (expt 10 x) 901) 17)) {T2, oc. expt, (natp 901) C6, C8, C9, MP

T1. (implies (and (natp x) (natp y) (natp z) (not (equal z 0)) (integerp (/ x z)) 
                                                               (integerp (/ y z))))
             (integerp (/ (+ x y) z)))) 

;; If two numbers are divisible by z, then their sum is divisible by z
    
T2. (implies (and (natp x) (natp y) (natp z) (not (equal z 0)) (integerp (/ x z))
             (integerp (/ (* x y) z)))))
             
;; If x is divisible by z, then the product of x and y is divisible by z
             

RHS:
      ={def. seq, C2
    
(integerp (/ (+ (* (expt 10 x) 901) (seq (- x 1))) 17))

      ={Arithmetic, T1, C7, oc. of seq, C6, C8
      
(integerp (/ (* (expt 10 x) 901) 17)) ^ (integerp (/ (seq (- x 1)) 17)) =>
(integerp (/ (+ (* (expt 10 x) 901) (seq (- x 1))) 17))

      ={C5, C10, MP

(integerp (/ (+ (* (expt 10 x) 901) (seq (- x 1))) 17))

true.
      

|#
#|

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Fibonacci -- for the last time!
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

8. The following problem is more difficult than previous problems. Consider
it a mini challenge.

Recall the definitions from HW10:

(defunc fib (n)
  :input-contract (natp n)
  :output-contract (natp (fib n))
  (if (<= n 1)
    n
    (+ (fib (- n 1))
       (fib (- n 2)))))

(defunc fib-t (n a b)
  :input-contract (and (natp n) (natp a) (natp b))
  :output-contract (natp (fib-t n a b))
  (if (equal n 0)
    b
    (fib-t (- n 1) (+ a b) a)))

(defunc fib* (n)
  :input-contract (natp n)
  :output-contract (natp (fib* n))
  (fib-t n 1 0))

The goal is now to prove that fib and fib* indeed return the same value.

(a) Devise a lemma eps that relates fib-t and fib. The lemma should have
the form

eps: (posp n) /\ (natp a) /\ (natp b) => (fib-t n a b) = ...

where ... is an expression involving fib. The ... should not contain any
call to fib-t.

Hint: note that the lhs contains (posp n) : the lemma assumes that n>0. The reason
is that the rhs may involve a call to fib on an argument other than n.

...

(b) Prove eps by induction.

Hint: you may you have to /design/ a function that gives rise to a suitable
induction scheme. In that case, briefly state why your function is
admissible.

...

(c) Prove the following theorem phi by equational reasoning:

phi: (natp n) => (fib* n) = (fib n)

Hint: you will need lemma eps. But note that the lemma is formulated for
n > 0. This means that the special case n=0 must be treated separately --
the lemma is useless in this case.

...

|#