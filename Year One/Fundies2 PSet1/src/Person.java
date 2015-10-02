// Assignment 1
// Northcott Jonathan
// nrthcoj
// Lowen Zach
// zlowen

class Person {
    String name;
    int yob;
    String state;
    boolean citizen;

    Person(String name, int yob, String state, boolean citizen) {
        this.name = name;
        this.yob = yob;
        this.state = state;
        this.citizen = citizen;
    }
}

class ExamplesPerson {
    Person jackie = new Person("Jackie Robinson", 
                               1920,
                               "NY",
                               true);
    
    Person golda = new Person("Golda Meir",
                              1930,
                              "MA",
                              false);
    
    Person zach = new Person("Zach Lowen",
                             1996,
                             "MA",
                             false);
}