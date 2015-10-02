import tester.Tester;

// Assignment 7
// Melagrano Alex
// alex3232
// Northcott Jonathan
// nrthcoj

//represents a single student
class Student {
    String name;
    int id;
    LOC courses;

    Student(String name, int id, LOC courses) {
        this.name = name;
        this.id = id;
        this.courses = courses;
    }
    /*
     * FIELDS :
     * this.name ...
     * this.id ...
     * this.courses ...
     * METHODS :
     * this.sameStudent( ... ) ...
     * this.enroll( ... ) ...
     * this.classmates( ... ) ...
     * METHODS FOR FIELDS :
     * this.courses.sameCourse( ... ) ...
     */

    // checks if this student is the same as the given one
    boolean sameStudent(Student c) {
        return this.name.equals(c.name) &&
                this.id == c.id;                
    }
    // EFFECT: Enrolls this student in the given course;
    // updates the student's list of courses, and the courses list of students
    void enroll(Course c) { 
        this.courses = new ConsLOC(c, this.courses);
        c.students = new ConsLOS(this, c.students);
    }
    // determines whether the given Student is in any of the same classes as this Student
    boolean classmates(Student c) {
        return this.courses.howMany(c) > 0;
    }
}

//represents a single course
class Course {
    String name;
    Instructor prof;
    LOS students;

    Course(String name, Instructor prof, LOS students) {
        this.name = name;
        this.prof = prof;
        this.students = students;
    }
    /*
     * FIELDS :
     * this.name ...
     * this.prof ...
     * this.students ...
     * METHODS :
     * METHODS FOR FIELDS :
     * this.students.contains() ...
     */
    // checks if the given student is enrolled in this course
    boolean courseContains(Student c) {
        return this.students.losContains(c);
    }

}

//represents a list of courses
interface LOC {
    // counts how many times the given student appears in this list
    int howMany(Student c);
    // checks if the given course is within this list
    boolean locContains(Course c);
}
//represents an empty list of courses
class MtLOC implements LOC {
    // counts how many times the given student appears in this list
    public int howMany(Student c) {
        return 0;
    }
    // checks if the given course is within this list
    public boolean locContains(Course c) {
        return false;
    }
}

//represents a non-empty list of courses
class ConsLOC implements LOC {
    Course first;
    LOC rest;

    ConsLOC(Course first, LOC rest) {
        this.first = first;
        this.rest = rest;
    }
    // counts how many times the given student appears in this list
    public int howMany(Student s) {
        if (this.first.courseContains(s)) {
            return 1 + this.rest.howMany(s);
        }
        else
        {
            return this.rest.howMany(s);
        }
    }
    // checks if the given course is within this list
    public boolean locContains(Course c) {
        return this.first.name.equals(c.name) ||
                this.rest.locContains(c);
    }

}

// represents a list of students
interface LOS {
    // checks if the given student is within this list of students
    boolean losContains(Student c);
}

// represents an empty list of students
class MtLOS implements LOS {
    // checks if the given student is within this list of students
    public boolean losContains(Student c) {
        return false;
    }
    
}

// represents a non-empty list of students
class ConsLOS implements LOS {
    Student first;
    LOS rest;
    
    ConsLOS(Student first, LOS rest) {
        this.first = first;
        this.rest = rest;
    }
    // checks if the given student is within this list of students
    public boolean losContains(Student c) {
        return this.first.sameStudent(c) ||
                this.rest.losContains(c);
    }
}


// represents a course instructor/professor
class Instructor {
    String name;
    LOC courses;
    
    Instructor(String name, LOC courses) {
        this.name = name;
        this.courses = courses;
    }
    /*
     * FIELDS :
     * this.name ...
     * this.courses ...
     * METHODS :
     * this.dejavou( ... ) ...
     * METHODS FOR FIELDS :
     * this.courses.howMany( ... ) ...
     */
    
    // determines whether the given Student is in more than one of this Instructor���s Courses
    boolean dejavu(Student c) {
        return this.courses.howMany(c) > 1;
    }

}

// examples and tests
class ExamplesRegistrar {
    // == EXAMPLES ==
    // examples of students
    Student jesus = new Student("Jesus", 1, null);
    Student abraham = new Student("Abraham", 41, null);
    Student job = new Student("Job", 18, null);
    Student noah = new Student("Noah", 23, null);
    Student moses = new Student("Moses", 69, null);
    // examples of lists of students
    LOS mtlos = new MtLOS();
    LOS los1 = new ConsLOS(noah, mtlos);
    LOS los2 = new ConsLOS(job, new ConsLOS(jesus, mtlos));
    LOS los3 = new ConsLOS(abraham, new ConsLOS(moses, new ConsLOS(jesus, mtlos)));
    LOS los4 = new ConsLOS(jesus, new ConsLOS(abraham, new ConsLOS(job, 
            new ConsLOS(moses, mtlos))));
    LOS los5 = new ConsLOS(jesus, mtlos);
    // examples of courses
    Course c1 = new Course("Arch Building", this.god, this.los1);
    Course c2 = new Course("Math", this.pythagoras, this.los2);
    Course c3 = new Course("Public Speaking", this.god, this.los3);
    Course c4 = new Course("Philosophy", this.plato, this.los4);
    Course c5 = new Course("Dejavu", this.god, this.los5);
    // examples of instructors
    LOC mtloc = new MtLOC();
    Instructor god = new Instructor("God", new ConsLOC(c1, new ConsLOC(c3, 
            new ConsLOC(c5, this.mtloc))));
    Instructor pythagoras = new Instructor("Pythagoras", new ConsLOC(c2, this.mtloc));
    Instructor plato = new Instructor("Plato", new ConsLOC(c4, this.mtloc));

    // examples of lists of courses
    LOC loc1 = new ConsLOC(c2, new ConsLOC(c3, new ConsLOC(c4, new ConsLOC(c5, mtloc))));
    LOC loc2 = new ConsLOC(c3, new ConsLOC(c4, mtloc));
    LOC loc3 = new ConsLOC(c2, mtloc);
    LOC loc4 = new ConsLOC(c1, new ConsLOC(c4, mtloc));
    LOC loc5 = new ConsLOC(c3, new ConsLOC(c4, mtloc));
    
    void setData() {
        this.jesus.courses = this.loc1;
        this.abraham.courses = this.loc2;
        this.job.courses = this.loc3;
        this.noah.courses = this.loc4;
        this.moses.courses = this.loc5;
        this.c1.prof = this.god;
        this.c2.prof = this.pythagoras;
        this.c3.prof = this.god;
        this.c4.prof = this.plato;
        this.c5.prof = this.god;
    }
    
    // == TESTS ==
    // tests the sameStudent method
    void testSameStudent(Tester t) {
        t.checkExpect(moses.sameStudent(abraham), false);
        t.checkExpect(job.sameStudent(job), true);
    }    
    // tests the enroll method
    // EFFECT: Sets up the initial conditions for our tests by re-initializing
    // the student and course data, then modifies them
    boolean testEnroll(Tester t) {
        // 1. Set up the initial conditions
        this.moses = new Student("Moses", 69, null);
        this.c1 = new Course("Arch Building", this.god, null);
        this.jesus = new Student("Jesus", 1, null);
        this.c4 = new Course("Philosophy", this.plato, null);
        // 2. Modify them
        this.moses.enroll(c1);
        this.jesus.enroll(c4);
        // 3. Check that the expected changes have occurred
        return t.checkExpect(c1.students.losContains(moses), true) &&
                t.checkExpect(moses.courses.locContains(c1), true) &&
                t.checkExpect(c4.students.losContains(jesus), true) &&
                t.checkExpect(jesus.courses.locContains(c4), true);

    }
    // tests the classmates method
    void testClassmates(Tester t) {
        this.setData();
        t.checkExpect(moses.classmates(abraham), true);
        t.checkExpect(jesus.classmates(job), true);   
        t.checkExpect(abraham.classmates(moses), true);
        t.checkExpect(job.classmates(noah), false);
    }
    
    // tests the losContains method
    void testLOSContains(Tester t) {
        this.setData();
        t.checkExpect(los1.losContains(noah), true);
        t.checkExpect(los2.losContains(abraham), false);
        t.checkExpect(los3.losContains(moses), true);
        t.checkExpect(los4.losContains(noah), false);
        
    }
    // tests the courseContains method
    void testCourseContains(Tester t) {
        this.setData();
        t.checkExpect(c1.courseContains(noah), true);
        t.checkExpect(c2.courseContains(abraham), false);
        t.checkExpect(c3.courseContains(moses), true);
        t.checkExpect(c4.courseContains(noah), false);
        
    }
      
    // tests the howMany method
    boolean testHowMany(Tester t) {
        this.setData();
        return t.checkExpect(loc3.howMany(abraham), 0) &&
                t.checkExpect(loc1.howMany(jesus), 4) && 
                t.checkExpect(loc4.howMany(moses), 1) &&
                t.checkExpect(loc2.howMany(job), 1);
    }
    // tests the locContains method
    boolean testLocContains(Tester t) {
        this.setData();
        return t.checkExpect(loc1.locContains(c1), false) &&
                t.checkExpect(loc2.locContains(c2), false) && 
                t.checkExpect(loc3.locContains(c2), true) &&
                t.checkExpect(loc4.locContains(c4), true);
    }
    // tests the losContains method
    boolean testLosContains(Tester t) {
        this.setData();
        return t.checkExpect(los1.losContains(abraham), false) &&
                t.checkExpect(los2.losContains(moses), false) && 
                t.checkExpect(los3.losContains(abraham), true) &&
                t.checkExpect(los4.losContains(jesus), true);
    }
    // tests the dejavu method
    boolean testDejavu(Tester t) {
        this.setData();
        return t.checkExpect(pythagoras.dejavu(moses), false) &&
                t.checkExpect(god.dejavu(jesus), true);
    }

  
}







