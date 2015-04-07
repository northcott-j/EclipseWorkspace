import tester.*;

import java.util.*;             // Gives us Arrays
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.impworld.*;      // the abstract World class and the big-bang library
import javalib.colors.*;        // Predefined colors (Red, Green, Yellow, Blue, Black, White)
import javalib.worldcanvas.*;   // generic world canvas/background
import java.awt.Color;          // Represents Colors


//Represents the ability to produce a sequence of values // of type T, one at a time 
interface Iterator<T> {
    // Does this sequence have at least one more value? 
    boolean hasNext();
    // Get the next value in this sequence // EFFECT: Advance the iterator to the subsequent value 
    T next();
    // EFFECT: Remove the item just returned by next() 
    // NOTE: This method may not be supported by every iterator; ignore it for now 
    void remove();
}

//Represents anything that can be iterated over 
interface Iterable<T> {
    //Returns an iterator over this collection 
    Iterator<T> iterator();
}

// Iterates over a list of Arrays
class ArrayListIterator<T> implements Iterator<T> {

    ArrayList<T> aList;
    int nextIdx;

    ArrayListIterator(ArrayList<T> aList) {
        this.aList = aList;
        this.nextIdx = 0;

    }
    // Checks if there's a next item in the ArrayList iterable
    public boolean hasNext() {
        return nextIdx < this.aList.size();

    }
    // Returns the next item in an Arraylist iterable
    public T next() {

        return aList.get(nextIdx);
    }
    // Throws an exception if you try to remove from an ArrayList
    public void remove() {
        throw new UnsupportedOperationException("You can't do that!");
    }
}

// Iterates over an IList
class IListIterator<T> implements Iterator<T> {
    
    IList<T> list;
    IListIterator(IList<T> list) {
        this.list = list;
    }
    // Checks if the IList iterable has a next item
    public boolean hasNext() {
        return this.list.isCons();
    }
    // Outputs the next item in the Ilist iterable
    public T next() {

        Cons<T> itemsAsCons = this.list.asCons();
        T answer = itemsAsCons.first;
        this.list = itemsAsCons.rest;
        return answer;
    }
    // Throws an exception if you try to remove an item from the iterable
    public void remove() {
        throw new UnsupportedOperationException("Don't do this!");
    }
    // Draws a given list of cells  
    public WorldImage draw() {
        WorldImage boardImage1 = new RectangleImage(new Posn(0,0), 1, 1, Color.white);
        WorldImage boardImage2 = new RectangleImage(new Posn(0,0), 1, 1, Color.white);
        int count = 0;
        while(this.hasNext()) {
            // Gets next Cell
            Cell c = (Cell)this.next();
            WorldImage cellImage = c.draw();
            if (count % 2 == 0) {
            boardImage1 = new OverlayImages(boardImage1, cellImage);
            }
            else {
                boardImage2 = new OverlayImages(boardImage2, cellImage);
            }
            count +=1;
        }
        return new OverlayImages(boardImage1, boardImage2);
    }
}

// Iterates over an Ilist<Cell> 
class ICellListIterator implements Iterator<Cell> {
    
    IList<Cell> list;
    ICellListIterator(IList<Cell> list) {
        this.list = list;
    }

    // Checks if the iterable has a next item
    public boolean hasNext() {
        return this.list.isCons();
    }
    // Outputs the iterable's current item
    public Cell next() {

        Cons<Cell> itemsAsCons = this.list.asCons();
        Cell answer = itemsAsCons.first;
        //this.list = itemsAsCons.rest;
        return answer;
    }
    // Throws an exception if you try to remove an item from an iterable
    public void remove() {
        throw new UnsupportedOperationException("Don't do this!");
    }
    // Floods the island when called (by 2 units)
    public IList<Cell> flood(int waterHeight) {

        IList<Cell> output = new Empty<Cell>();
        while(this.hasNext()) {

            Cell cell = this.next();

            if (waterHeight >= cell.height && (cell.left.isFlooded || 
                    cell.top.isFlooded || 
                    cell.bottom.isFlooded || 
                    cell.right.isFlooded)) {
                
                cell.height = cell.height - waterHeight;
                cell.isFlooded = true;
                output = output.insert(this.next());
                this.list = this.list.asCons().rest;
                
            }
            else {
                cell.height = cell.height - waterHeight;
                output = output.insert(this.next());
                this.list = this.list.asCons().rest;
            }
        }

        return output;
    }
    // Elevates the 5 x 5 cell radius around a pilot
    public IList<Cell> elevate(Person p) {
        IList<Cell> output = new Empty<Cell>();
        
        while(this.hasNext()) {
            Cell cell = this.next();

            if(p.location.x <= (int)cell.x * 10 + 25 && 
                    p.location.x >= (int)cell.x * 10 - 25 && 
                    p.location.y <= (int)cell.y * 10 + 25 &&
                    p.location.y >= (int)cell.y * 10 - 25) {

                cell.height = cell.height + 2;
                cell.isFlooded = false;
                output = output.insert(cell);
                this.list = this.list.asCons().rest;
            }
            else {
                output = output.insert(cell);
                this.list = this.list.asCons().rest;
            }
        }

        return output;
    }
}

// Iterates over an IList<Target>
class ITargetListIterator implements Iterator<Target> {
    
    IList<Target> list;
    ITargetListIterator(IList<Target> list) {
        this.list = list;
    }
    
    // Checks if there is another item in the iterable
    public boolean hasNext() {
        return this.list.isCons();
    }
    // Returns the current item in the iterable
    public Target next() {

        Cons<Target> itemsAsCons = this.list.asCons();
        Target answer = itemsAsCons.first;
        //this.list = itemsAsCons.rest;
        return answer;
    }
    // Throws an exception if someone tries to remove from the interable
    public void remove() {
        throw new UnsupportedOperationException("Don't do this!");
    }
    
    // Draws a list of targets
    public WorldImage draw(WorldImage world) {


        WorldImage background = new RectangleImage(new Posn(0,0), 1, 1, Color.white);

        while(this.hasNext()) {

            Target next = this.next();
            WorldImage nextImage = next.draw();
            background = background.overlayImages(nextImage);
            list = this.list.asCons().rest;
            
            
        }
        
        return new OverlayImages(world, background);
    }
    // Checks to see if the pilot has found any targets
    IList<Target> foundTargets(Person p) {
        
        IList<Target> output = new Empty<Target>();
        while(this.hasNext()) {
            
            Target t = this.next();
            if  (t.location.x >= p.location.x - 5 && t.location.x <= p.location.x + 5 &&
                    t.location.y >= p.location.y - 5 && t.location.y <= p.location.y + 5) {
                this.list = this.list.asCons().rest;
            // Removes target from list    
            }
            
            else {
                output = output.insert(t);
                this.list = this.list.asCons().rest;
            }
            
        }
        return output;
        
    }
    // Checks if any targets are flooded
    boolean flooded(IList<Cell> board) {
        
        boolean b = false;
        
        while(this.hasNext()) {
            
            if(!board.legalLocation(this.next().location)) {
                b = true;
            }
            this.list = this.list.asCons().rest;
            
        }
        return b;
    }
    
}

// ArrayUtils class
class ArrayUtils {

    IList<Cell> flatten(ArrayList<ArrayList<Cell>> array) {
        IList<Cell> board = new Empty<Cell>();
        for(int y = 0 ; y < ForbiddenIslandWorld.ISLAND_SIZE ; y+=1){
            for(int x = 0 ; x < ForbiddenIslandWorld.ISLAND_SIZE ; x+=1) {
                Cell cell = array.get(y).get(x);
                board = board.insert(cell);
            }
        }
        return board;
    }
    

    ArrayList<ArrayList<Cell>> terrainCells(ArrayList<ArrayList<Cell>> array, int oX, int oY, int width) {

        if (width > 1) {
            ArrayList<ArrayList<Cell>> temp = new ArrayList<ArrayList<Cell>>();
            temp = this.quadAlgo(array, oX, oY, width);
            temp = this.quadAlgo(temp, oX + width, oY, width);
            temp = this.quadAlgo(temp, oX + width, oY + width, width);
            temp = this.quadAlgo(temp, oX, oY + width, width);
            temp = terrainCells(temp, oX, oY, width / 2);
            temp = terrainCells(temp, oX + width, oY, width / 2);
            temp = terrainCells(temp, oX + width, oY + width, width / 2);
            temp = terrainCells(temp, oX, oY + width, width / 2);
            return temp;
        }
        else {
            return array;
        }
    }

    ArrayList<ArrayList<Cell>> quadAlgo(ArrayList<ArrayList<Cell>> array, int oX, int oY, int width) {
        //double rand = Math.random() * ForbiddenIslandWorld.ISLAND_SIZE;
        Random rand = new Random();
        double tL = array.get(oY).get(oX).height;
        double tR = array.get(oY).get(oX + width).height;
        double bR = array.get(oY + width).get(oX + width).height;
        double bL = array.get(oY + width).get(oX).height;

        // Top
        array.get(oY).get(oX + width / 2).height = 
                (rand.nextDouble() - rand.nextDouble()) * width + ((tL + tR) / 2);
        // Right
        array.get(oY + width / 2).get(oX + width).height = 
                (rand.nextDouble() - rand.nextDouble()) * width + ((tR + bR) / 2);
        // Bottom
        array.get(oY + width).get(oX + width / 2).height = 
                (rand.nextDouble() - rand.nextDouble()) * width + ((bL + bR) / 2);
        // Left
        array.get(oY + width / 2).get(oX).height = 
                (rand.nextDouble() - rand.nextDouble()) * width + ((tL + bL) / 2);
        // Middle
        array.get(oY + width / 2).get(oX + width / 2).height =
                (rand.nextDouble() - rand.nextDouble()) * width + ((tL + tR + bL + bR) / 4);
        return array;
    }

}

// interface to represent functions
interface IFunc<A, R> {
    R apply(A func);
}

// Interface to represent predicates
interface IPred<P> {
    boolean apply(P pred);
}

// Represents all ILists (which are iterable)
interface IList<T> extends Iterable<T>{

    // Inserts item into given list
    IList<T> insert(T t);
    // Appends two lists
    IList<T> append(IList<T> l);
    // Accepts a given visitor
    <R> R accept(IListVisitor<T, R> visitor);
    // Returns the size of the list
    int size();
    // Is this a cons?
    boolean isCons();
    // Make this a cons
    Cons<T> asCons();
    // Checks if attempted move location is legal
    boolean legalLocation(Posn p);

}

// Represents List visitors
interface IListVisitor<T, R> {
    R visitM(Empty<T> m);
    R visitC(Cons<T> c);

}

// Represents an empty list
class Empty<T> implements IList<T> {

    // Appends two given lists
    public IList<T> append(IList<T> l) {
        return l;
    }

    // Accepts a visitor function object
    public <R> R accept(IListVisitor<T, R> visitor) {
        return visitor.visitM(this);
    }

    // Inserts item into front of this list
    public IList<T> insert(T t) {
        return new Cons<T>(t, this);
    }

    // Returns the size of the list
    public int size() {
        return 0;
    }
    // Is this a cons?
    public boolean isCons() {
        return false;
    }
    // Make this a cons
    public Cons<T> asCons() {
        throw new UnsupportedOperationException("Not a Cons!");
    }

    // Iterator for an ConsList
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }
    // Checks if given location is allowed
    public boolean legalLocation(Posn p) {
        return true;
    }
}

// Represents a cons List
class Cons<T> implements IList<T> {
    T first;
    IList<T> rest;

    Cons(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    // Appends two lists
    public IList<T> append(IList<T> l) {
        return new Cons<T>(this.first, this.rest.append(l));
    }

    // Inserts item to front of given list
    public IList<T> insert(T t) {
        return new Cons<T>(t, this);
    }

    // Accepts a visitor function object
    public <R> R accept(IListVisitor<T, R> visitor) {
        return visitor.visitC(this);
    }

    // Returns the size of the list
    public int size() {
        return 1 + this.rest.size();
    }
    // Is this a cons?
    public boolean isCons() {
        return true;
    }

    // Make this a cons
    public Cons<T> asCons() {
        return this;
    }

    // Iterator for an ConsList
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }
    
    // Checks if given location is not flooded
    public boolean legalLocation(Posn p) {
        
        // Turns IList<Cell> into Cons<Cell>
        Cons<Cell> cellList = (Cons<Cell>)this;
        if(p.x <= (int)cellList.first.x * 10 + 5 && p.x >= (int)cellList.first.x * 10 - 5 && 
                p.y <= (int)cellList.first.y * 10 + 5 && p.y >= (int)cellList.first.y * 10 - 5) {
            return !cellList.first.isFlooded;
            // return false;
        }
        else {
            return this.rest.legalLocation(p);
        }
    }
}



// Represents a single square of the game area
class Cell {
    // represents absolute height of this cell, in feet
    double height;
    // In logical coordinates, with the origin at the top-left corner of the scren
    int x, y;
    // the four adjacent cells to this one
    Cell left, top, right, bottom;
    // reports whether this cell is flooded or not
    boolean isFlooded;

    Cell(double height, int x, int y, Cell left, Cell top, Cell right, Cell bottom, boolean isFlooded) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.isFlooded = isFlooded;
    }

    Cell(double height, int x, int y, boolean isFlooded) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = this;
        this.top = this;
        this.right = this;
        this.bottom = this;
        this.isFlooded = isFlooded;
    }
    
    // Checks to see if this is a land cell
    boolean isCell() {
        return true;
    }

    // Draws cell image
    WorldImage draw() {
        if (this.height > 0 && !isFlooded) {
            return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                    new Color((int)Math.min(255, this.height * ForbiddenIslandWorld.RGB_INC), 
                    255,
                    (int)Math.min(255, this.height * ForbiddenIslandWorld.RGB_INC)));
        }
        else if (!isFlooded) {
            
            if(this.height < -8) {
                return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                        new Color(170, 0, 0));
            }
            else if(this.height < -6) {
                return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                        new Color(0, 130, 0));
            }
            else if(this.height < -4)  {
                return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                        new Color(0, 170, 0));
            }
            else {
                return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                        new Color(0, 195, 0));
            
            }
        }
            
        
        else {
            
            
            if(this.height < -12) {
                return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                        new Color(0, 0, 0));
            }
            else if(this.height < -8) {
                return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                        new Color(0, 0, 50));
            }
            else if(this.height < -4)  {
                return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                        new Color(0, 0, 100));
            }
            else {
                return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                        new Color(0, 195, 150));
            
            }
            
        }
    }

}

// Represents an Oceancell
class OceanCell extends Cell {


    OceanCell(double height, int x, int y, Cell left, Cell top, Cell right, Cell bottom, boolean isFlooded) {

        super(height, x, y, left, top, right, bottom, isFlooded);
        this.isFlooded = true;
    }

    OceanCell(double height, int x, int y, boolean isFlooded) {
        super(height, x, y, isFlooded);
        this.isFlooded = true;
    }
    
    // Checks to see if this is a land cell
    boolean isCell() {
        return false;
    }
    
    // Draws the given cell
    WorldImage draw() {
        return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, Color.blue);
    }
}

class Person {
    
    Posn location;
    
    Person(Posn location) {
        this.location = location;
    }
    
    // Checks if given move is allowed
    boolean legalLocation(String ke, IList<Cell> board) {
        if (ke.equals("left")) {
            Posn newPosnL = new Posn(location.x - 10, location.y);
            return board.legalLocation(newPosnL);
        }
        else if(ke.equals("right")) {
            Posn newPosnR = new Posn(location.x + 10, location.y);
            return board.legalLocation(newPosnR);
        }
        else if(ke.equals("up")) {
            Posn newPosnU = new Posn(location.x, location.y - 10);
            return board.legalLocation(newPosnU);
        }
        else {
            Posn newPosnD = new Posn(location.x, location.y + 10);
            return board.legalLocation(newPosnD);
        }
    }
    
    // Moves a person based on the given key
    Person moveOnKey(String ke, IList<Cell> board) {
        if (ke.equals("left") && this.legalLocation("left", board)) {
            location.x = location.x - 10;
            return this;
        }
        else if (ke.equals("right") && this.legalLocation("right", board)) {
            location.x = location.x + 10;
            return this;
        }
        else if (ke.equals("up") && this.legalLocation("up", board)) {
            location.y = location.y - 10;
            return this;
        }
        else if (ke.equals("down") && this.legalLocation("down", board)) {
            location.y = location.y + 10;
            return this;
        }
        else {
            return this;
        }
    }
    
    // Draws the pilot
    WorldImage draw() {
        WorldImage person = new FromFileImage(this.location, "pilot-icon.png");
        
        return person;
    }
    
    // Checks if the person is on a flooded cell
    boolean flooded(IList<Cell> board) {
        
        return !board.legalLocation(this.location);
        
        
    }

}

class Target {
   Posn location;
   
   Target(Posn location) {
       this.location = location;
       
   }
   
   // Draws the individual target
   WorldImage draw() {
       
       return new CircleImage(location, 5, Color.black);
       
   }
   
   // Makes a list of 5 randomly placed targets
   public IList<Target> makeList() {

           
           IList<Target> output = new Empty<Target>();
           
           while(output.size() < 6) {

               Target t = new Target(new Posn((int)(Math.random() * 540),
                       (int)(Math.random() * 640)));
               
               
               if(Math.abs(ForbiddenIslandWorld.ISLAND_SIZE/2 * 10 - t.location.x) +
                       Math.abs(ForbiddenIslandWorld.ISLAND_SIZE/2 * 10 - t.location.y) < 300){

               output = output.insert(t);
               }
           }
           
           return output;
       }
   
}

// Represents the helicopter, which is the end game
class HelicopterTarget extends Target {
    
    Posn location;
    
    HelicopterTarget(Posn location) {
        super(location);
    }
    
    // Draws the helicopter image
    WorldImage draw() {
        
        return new FromFileImage(this.location, "helicopter.png");   
    }
}


class ForbiddenIslandWorld extends World {

    // Represents the Island Size
    static int ISLAND_SIZE = 64;
    // Represents a range of colors to be used to show Island cell height
    static final int RGB_INC = 255 / (ISLAND_SIZE / 2);
    // Has the game started yet?
    static boolean INITIALIZED = false;
    // Checks if end condition has been met
    static boolean ENDED = false;
    // Checks if game has been paused
    static boolean PAUSED = false;

    // All the cells of the game, including the ocean
    IList<Cell> board;
    // the current height of the ocean
    int waterHeight;
    // Represents a person
    Person person;
    // Represents OnTick timer
    int timer;
    // Represents the targets left available in the game
    IList<Target> targets;

    ForbiddenIslandWorld(IList<Cell> board, Person person, IList<Target> targets) {
       this.board = board;
       this.waterHeight = 2;
       this.person = person;
       this.timer = 0;
       this.targets = targets;
    }
    
    ForbiddenIslandWorld(IList<Cell> board, Person person, int timer, IList<Target> targets) {
        this.board = board;
        this.waterHeight = 2;
        this.person = person;
        this.timer = timer;
        this.targets = targets;
     }
    
    // Floods the island
    void floodIsland() {
        
        //Eventually becomes the outputed list
        IList<Cell> output = new Empty<Cell>();

        //EFFECT : Adds each modified cell to the output list
        for(int count = 0 ; count < this.board.size() - 1 ; count+=1) {
            Cons<Cell> tempB = (Cons<Cell>)this.board;
            Cell cell = tempB.first;
            if (waterHeight >= cell.height && (cell.left.isFlooded || 
                                               cell.top.isFlooded || 
                                               cell.bottom.isFlooded || 
                                               cell.right.isFlooded)) {
                cell.height -= waterHeight;
                cell.isFlooded = true;
                output.insert(cell);
                tempB = (Cons<Cell>)tempB.rest;
            }
            else {
                cell.height -= waterHeight;
                output.insert(cell);
                tempB = (Cons<Cell>)tempB.rest;
            }
            
            this.board = output;
        }
    }
    
    // Changes the world for every tick
    public void onTick() {
        
        ITargetListIterator itli = new ITargetListIterator(targets);
        // Checks if game hasn't started yet
        if(!INITIALIZED) {
            this.board = this.generateMountainCellsTerrain();
            INITIALIZED = true;
            int tempTimer = timer + 1;
            timer = tempTimer;
            person = new Person(new Posn(320, 320));
        }
        // Checks if end condition has been met
        else if (itli.flooded(board) || person.flooded(board)) {
            ENDED = true;
            board = new Empty<Cell>();
        }
        // Checks if game is paused
        else if (PAUSED) {
            // Stops the if statement from going on
        }
        // Adds to countup timer
        else if ((timer + 1) % 10 == 0) {

            ICellListIterator icli = new ICellListIterator(board);
            board = icli.flood(waterHeight);    
            //this.floodIsland();
            timer = timer + 1;
        }

        else {
            timer = timer + 1;
        }
        
    }
    
    // Moves Person for each key event or returns this new Island type
    //EFFECT : Changes board based on chosen Island type
    public void onKeyEvent(String ke) {
        
        Target t = new Target(new Posn(0,0));
        // Returns regular mountain
        if (ke.equals("m") && !PAUSED) {
            this.board = this.generateMountainCells();
            timer = 0;
            targets = t.makeList();
            person = new Person(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5));
            ENDED = false;
        }
        // Returns random mountain
        else if (ke.equals("r") && !PAUSED) {
            this.board = this.generateMountainCellsRandom();
            targets = t.makeList();
            timer = 0;
            person = new Person(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5));
            ENDED = false;
        }
        // Engineers up pilot's 5x5 region
        else if(ke.equals("b") && !PAUSED) {
            this.board = this.engineerCells();
        }
        // Generates random terrain mountain
        else if(ke.equals("t") && !PAUSED) {
            this.board = this.generateMountainCellsTerrain();
            timer = 0;
            targets = t.makeList();
            person = new Person(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5));
            ENDED = false;
        }
        // Pauses the game
        else if(ke.equals("p")) {
            PAUSED = !PAUSED;
        }
        // Moves the player if not paused
        else if (!PAUSED) {
            person = this.person.moveOnKey(ke, board);
            ITargetListIterator itli = new ITargetListIterator(targets);
            targets = itli.foundTargets(person);
        }

        
    }
    
    // Makes an image of the given world for each tick
    public WorldImage makeImage() {

        // Represents the amount of time spent playing the game
        TextImage time = new TextImage(new Posn(50, 50), 
                Integer.toString(this.timer), 20 , 1, 
                Color.red);

        // Produces new IList of Cells
        IListIterator<Cell> ili = new IListIterator<Cell>(board);
        
        WorldImage background = (ili.draw()).overlayImages(person.draw()).overlayImages(time);
                
        ITargetListIterator itli = new ITargetListIterator(targets);
        
        // Returns end image
        if (ENDED) {
            return new FromFileImage(new Posn(200, 200), "drowning.jpeg");
        }
        // Draws black paused screen
        else if (PAUSED) {
            WorldImage cover =
                    new RectangleImage(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5),
                            ISLAND_SIZE * 10, ISLAND_SIZE * 10, Color.black);
            cover = new OverlayImages(itli.draw(background), cover);
            WorldImage text = new TextImage(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5), "PAUSED", 39, 1, Color.white);
            return new OverlayImages(cover, text);
        }
        // Draws world
        else {
            return itli.draw(background);
            // return background;
        }

      
    }
    
    // Generates the lists of lists of cells for Mountain world

    // Generates the lists of lists of cells for Mountain world
    IList<Cell> generateMountainCells() {
        if (ISLAND_SIZE % 2 == 0) {
            
        }
        else {
            ISLAND_SIZE = ISLAND_SIZE - 1;
        }
        // Generates heights for Mountain
        ArrayList<ArrayList<Double>> heights = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>(); 
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            ArrayList<Double> row = new ArrayList<Double>();
            for(int x = 0 ; x < ISLAND_SIZE  ; x+= 1) {
                int manHeight = Math.abs((ISLAND_SIZE / 2) - x) + Math.abs((ISLAND_SIZE / 2) - y);
                row.add((double)(ISLAND_SIZE / 2) - manHeight);
            }
            heights.add(row);
        }
        // Generates the cells for a Mountain
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for(int x = 0 ; x < ISLAND_SIZE ; x+= 1) {
                double cellHeight = heights.get(y).get(x);
                if (cellHeight <= 0) {
                    OceanCell ocean = new OceanCell(cellHeight, x, y, true);
                    row.add(ocean);
                }
                else {
                    Cell land = new Cell(cellHeight, x, y, false);
                    row.add(land);
                }
            }
            cells.add(row);
        }
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            for(int x = 0 ; x < ISLAND_SIZE ; x+=1) {
                Cell c = cells.get(y).get(x);
                if (c.x == 0 && c.isCell()) {
                    c.left = c;
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);
                }
                else if (c.x == (ISLAND_SIZE - 1) && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = c;
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);                    
                }
                else if (c.y == (ISLAND_SIZE - 1) && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = c;                    
                }
                else if (c.y == 0 && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = c;
                    c.bottom = cells.get(y + 1).get(x);                    
                }

                else if (c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);                        
                }
            }
        }

        ArrayUtils arrayUtils = new ArrayUtils();
        return arrayUtils.flatten(cells);
    }



    // Generates the lists of lists of cells for Mountain world
    IList<Cell> generateMountainCellsTerrain() {
        if (ISLAND_SIZE % 2 == 0) {
            ISLAND_SIZE = ISLAND_SIZE + 1;
        }
        else {
            
        }
        // CAN BE REWRITTEN IN TERMS OF MOUNTAIN
        // Generates heights for Mountain
        ArrayList<ArrayList<Double>> heights = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>(); 
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            ArrayList<Double> row = new ArrayList<Double>();
            for(int x = 0 ; x < ISLAND_SIZE  ; x+= 1) {
                int manHeight = Math.abs((ISLAND_SIZE / 2) - x) + Math.abs((ISLAND_SIZE / 2) - y);
                if (manHeight >= 32) {
                    row.add((double)(ISLAND_SIZE / 2) - manHeight);
                }
                else {
                    row.add(Math.abs((Math.random() * (double)(ISLAND_SIZE / 2) - manHeight)));
                }
            }
            heights.add(row);
        }
        // Generates the cells for a Mountain
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for(int x = 0 ; x < ISLAND_SIZE ; x+= 1) {
                double cellHeight = heights.get(y).get(x);
                if (cellHeight <= 0) {
                    OceanCell ocean = new OceanCell(cellHeight, x, y, true);
                    row.add(ocean);
                }
                else {
                    Cell land = new Cell(cellHeight, x, y, false);
                    row.add(land);
                }
            }
            cells.add(row);
        }

        cells.get(0).get(0).height = 0;
        cells.get(0).get(ISLAND_SIZE - 1).height = 0;
        cells.get(ISLAND_SIZE - 1).get(0).height = 0;
        cells.get(ISLAND_SIZE - 1).get(ISLAND_SIZE - 1).height = 0;

        cells.get(ISLAND_SIZE / 2).get(ISLAND_SIZE / 2).height = ISLAND_SIZE;

        cells.get(0).get(ISLAND_SIZE / 2).height = 1;
        cells.get(ISLAND_SIZE - 1).get(ISLAND_SIZE / 2).height = 1;
        cells.get(0).get(ISLAND_SIZE - 1).height = 1;
        cells.get(ISLAND_SIZE / 2).get(ISLAND_SIZE - 1).height = 1;

        ArrayUtils arrayUtils = new ArrayUtils();
        cells = arrayUtils.terrainCells(cells, 0, 0, ISLAND_SIZE / 2);

        // Generates the cells for a Mountain
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            for(int x = 0 ; x < ISLAND_SIZE ; x+= 1) {
                Cell cell = cells.get(y).get(x);
                if (cell.height <= 0) {
                    OceanCell ocean = new OceanCell(cell.height, x, y, true);
                    cells.get(y).set(x, ocean);
                }
                else {
                    Cell land = new Cell(cell.height, cell.x, cell.y, false);
                    cells.get(y).set(x, land);
                }
            }
        }

        // Fixes Cell connections
        for(int y = 1 ; y < ISLAND_SIZE ; y+= 1) {
            for(int x = 1 ; x < ISLAND_SIZE ; x+=1) {
                Cell c = cells.get(y).get(x);
                if (c.x == 0 && c.isCell()) {
                    c.left = c;
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);
                }
                else if (c.x == (ISLAND_SIZE - 1) && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = c;
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);                    
                }
                else if (c.y == (ISLAND_SIZE - 1) && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = c;                    
                }
                else if (c.y == 0 && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = c;
                    c.bottom = cells.get(y + 1).get(x);                    
                }

                else if (c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);                        
                }
            }
        }


        return arrayUtils.flatten(cells);
    }


    // Generates the lists of lists of cells for Mountain world
    IList<Cell> generateMountainCellsRandom() {
        if (ISLAND_SIZE % 2 == 0) {
            
        }
        else {
            ISLAND_SIZE = ISLAND_SIZE - 1;
        }
        // CAN BE REWRITTEN IN TERMS OF MOUNTAIN
        // Generates heights for Mountain
        ArrayList<ArrayList<Double>> heights = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>(); 
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            ArrayList<Double> row = new ArrayList<Double>();
            for(int x = 0 ; x < ISLAND_SIZE  ; x+= 1) {
                int manHeight = Math.abs((ISLAND_SIZE / 2) - x) + Math.abs((ISLAND_SIZE / 2) - y);

                if (manHeight >= 32) {
                    row.add((double)(ISLAND_SIZE / 2) - manHeight);
                }
                else {
                    row.add(Math.abs((Math.random() * (double)(ISLAND_SIZE / 2) - manHeight)));
                }
            }
            heights.add(row);
        }
        // Generates the cells for a Mountain
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for(int x = 0 ; x < ISLAND_SIZE ; x+= 1) {
                double cellHeight = heights.get(y).get(x);
                if (cellHeight <= 0) {
                    OceanCell ocean = new OceanCell(cellHeight, x, y, true);
                    row.add(ocean);
                }
                else {
                    Cell land = new Cell(cellHeight, x, y, false);
                    row.add(land);
                }
            }
            cells.add(row);
        }
        // Fixes Cell connections
        for(int y = 0 ; y < ISLAND_SIZE ; y+= 1) {
            for(int x = 0 ; x < ISLAND_SIZE ; x+=1) {
                Cell c = cells.get(y).get(x);
                if (c.x == 0 && c.isCell()) {
                    c.left = c;
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);
                }
                else if (c.x == (ISLAND_SIZE - 1) && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = c;
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);                    
                }
                else if (c.y == (ISLAND_SIZE - 1) && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = c;                    
                }
                else if (c.y == 0 && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = c;
                    c.bottom = cells.get(y + 1).get(x);                    
                }

                else if (c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);                        
                }
            }
        }

        ArrayUtils arrayUtils = new ArrayUtils();
        return arrayUtils.flatten(cells);
    }

    
    
    // Engineers cells to elevate
    IList<Cell> engineerCells() {
        ICellListIterator icli = new ICellListIterator(board);
        return icli.elevate(person);
    }
    
    /*
    public WorldEnd worldEnds() {
        
        ITargetListIterator itli = new ITargetListIterator(targets);
        
        if (itli.flooded(board) || person.flooded(board)) {
            return new WorldEnd(true, 
                    new FromFileImage(new Posn(200, 200), "drowning.jpeg"));
        
        }
        else {
            return new WorldEnd(false, this.makeImage());
        }
    }

*/
    
}
// Examples for the ForbiddenIslandGame
class ExamplesFIGame {

    Cell c1 = new Cell(5.0, 320, 320, false);
    IList<Cell> ilc = new Cons<Cell>(c1, new Empty<Cell>());
    Target t = new Target(new Posn(0,0));
    ForbiddenIslandWorld f1 = new ForbiddenIslandWorld(new Empty<Cell>(),
            new Person(new Posn(320,320)), t.makeList());
    {
        this.f1.bigBang(640, 640, .5);
    }
}