import tester.*;

import java.util.*;             // Gives us Arrays

import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.impworld.*;      // the abstract World class and the big-bang library
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
            if  (p.isNear(t.location)) {
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

    // Recursive method to form terrain cells
    ArrayList<ArrayList<Cell>> terrainCells(ArrayList<ArrayList<Cell>> array, 
            int oX, int oY, int width, String quad) {

        if (width > 1) {
            ArrayList<ArrayList<Cell>> temp = new ArrayList<ArrayList<Cell>>();
            temp = this.quadAlgo(array, oX, oY, width, quad);
            temp = this.quadAlgo(temp, oX + width, oY, width, quad);
            temp = this.quadAlgo(temp, oX + width, oY + width, width, quad);
            temp = this.quadAlgo(temp, oX, oY + width, width, quad);
            temp = terrainCells(temp, oX, oY, width / 2, "left");
            temp = terrainCells(temp, oX + width, oY, width / 2, "right");
            temp = terrainCells(temp, oX + width, oY + width, width / 2, "bright");
            temp = terrainCells(temp, oX, oY + width, width / 2, "bleft");
            return temp;
        }
        else {
            return array;
        }
    }

    // Helper for terrainCells - calculates points
    ArrayList<ArrayList<Cell>> quadAlgo(ArrayList<ArrayList<Cell>> array, int oX, int oY,
            int width, String quad) {
        //double rand = Math.random() * ForbiddenIslandWorld.ISLAND_SIZE;
        Random rand = new Random();
        double tL = array.get(oY).get(oX).height;
        double tR = array.get(oY).get(oX + width).height;
        double bR = array.get(oY + width).get(oX + width).height;
        double bL = array.get(oY + width).get(oX).height;
        // If it's the left quadrant coordinates
        if (quad.equals("left")) {
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
        // If it's the right quadrant coordinates
        else if (quad.equals("right")) {
            // Top
            array.get(oY).get(oX + width / 2).height = 
                    (rand.nextDouble() - rand.nextDouble()) * width + ((tL + tR) / 2);
            // Right
            array.get(oY + width / 2).get(oX + width).height = 
                    (rand.nextDouble() - rand.nextDouble()) * width + ((tR + bR) / 2);
            // Bottom
            array.get(oY + width).get(oX + width / 2).height = 
                    (rand.nextDouble() - rand.nextDouble()) * width + ((bL + bR) / 2);
            // Middle
            array.get(oY + width / 2).get(oX + width / 2).height =
                    (rand.nextDouble() - rand.nextDouble()) * width + ((tL + tR + bL + bR) / 4);
            return array;

        }
        // If it's the bottom right quadrant coordinates
        else if (quad.equals("bright")) {
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
        // If it's the bottom left quadrant coordinates
        else if (quad.equals("bleft")) {
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
        else {
            return array;
        }

    }

    // Fixes Cell connections for an ArrayList<ArrayList<Cell>>
    ArrayList<ArrayList<Cell>> fixConnections(ArrayList<ArrayList<Cell>> cells, String type) {

        int limit = 0;

        if(type.equals("terrain")) {
            limit = 1;
        }

        for(int y = limit ; y < ForbiddenIslandWorld.ISLAND_SIZE ; y+= 1) {
            for(int x = limit ; x < ForbiddenIslandWorld.ISLAND_SIZE ; x+=1) {
                Cell c = cells.get(y).get(x);
                // Left bounds
                if (c.x == 0 && c.isCell()) {
                    c.left = c;
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);
                }
                // Right bounds
                else if (c.x == (ForbiddenIslandWorld.ISLAND_SIZE - 1) && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = c;
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);                    
                }
                // Bottom bounds
                else if (c.y == (ForbiddenIslandWorld.ISLAND_SIZE - 1) && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = c;                    
                }
                // Top bounds
                else if (c.y == 0 && c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = c;
                    c.bottom = cells.get(y + 1).get(x);                    
                }
                // All other cells
                else if (c.isCell()) {
                    c.left = cells.get(y).get(x - 1);
                    c.right = cells.get(y).get(x + 1);
                    c.top = cells.get(y - 1).get(x);
                    c.bottom = cells.get(y + 1).get(x);                        
                }
            }

        }
        return cells;
    }

    // Generates the cells for a Mountain
    ArrayList<ArrayList<Cell>> formCells(ArrayList<ArrayList<Double>> heights) {

        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>(); 

        for(int y = 0 ; y < ForbiddenIslandWorld.ISLAND_SIZE ; y+= 1) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for(int x = 0 ; x < ForbiddenIslandWorld.ISLAND_SIZE ; x+= 1) {
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
        return cells;
    }

    // Draws last 8 keys
    TextImage drawKeys(ArrayList<String> keys) {

        String checkKeys = "";
        int keySize = keys.size() - 1;
        if (keySize >= 7) {
            for(int k = 7 ; k >= 0 ; k-=1) {
                checkKeys = checkKeys + keys.get(k);
            }
        }
        else {
            for(int k = keySize ; k >= 0 ; k-=1) {
                checkKeys = checkKeys + keys.get(k);
            }
        }
        return new TextImage(new Posn(ForbiddenIslandWorld.ISLAND_SIZE * 5, 50), 
                checkKeys, 20 , 1, 
                Color.red);
    }
}

// Class for cheats
class Cheats {

    // CHEATS ===============================================>
    // Sets engineer build count to given number 
    String resetEngineer = "12345678";
    // Turns water to wine
    String waterToWine = "h2o2wine";
    // Turns on God Mode
    String godMode = "sgodvals";
    // Sets Build Engineer to Unlimited
    String buildInfinity = "infinidy";
    // Redraws Pilot and Heli
    String alienInv = "alieninv";
    // Hides HUD
    String toggleDisp = "hidehuds";
    // Creates a new set of targets 
    String shuffle = "shuffles";
    // Freeze game
    String freeze = "fweezwld";
    // Speeds up flooding 
    String floodFast = "floodqwk";
    // Slows down flooding 
    String floodSlow = "floodslw";
    boolean cheatEntered(ArrayList<String> keys) { 
        // Index range of key array
        int keySize = keys.size() - 1;
        // Gets last eight keys (length of cheats)
        String checkKeys = "";
        if (keySize >= 7) {
            for(int k = 7 ; k >= 0 ; k-=1) {
                checkKeys = checkKeys + keys.get(k);
            }
        }
        return checkKeys.equals(godMode) || checkKeys.equals(resetEngineer) ||
                checkKeys.equals(waterToWine) || checkKeys.equals(buildInfinity) ||
                checkKeys.equals(alienInv) || checkKeys.equals(toggleDisp) ||
                checkKeys.equals(shuffle) || checkKeys.equals(freeze) ||
                checkKeys.equals(floodFast) || checkKeys.equals(floodSlow);
    }

    // Finds what cheat was entered and executes it
    void whatCheat(ArrayList<String> keys) {
        // Gets last eight keys (length of cheats)
        String checkKeys = "";
        for(int k = 7 ; k >= 0 ; k-=1) {
            checkKeys = checkKeys + keys.get(k);
        }
        // SIDE EFFECT:
        // Changes GOD_MODE boolean flag
        ForbiddenIslandWorld.keys.clear();
        if (checkKeys.equals(godMode)) {
            ForbiddenIslandWorld.GOD_MODE = !ForbiddenIslandWorld.GOD_MODE;
        }
        // SIDE EFFECT:
        // Changes engineersLeft variable to original
        else if (checkKeys.equals(resetEngineer)) {
            ForbiddenIslandWorld.engineersLeft = ForbiddenIslandWorld.ENGINEER;
        }
        // SIDE EFFECT:
        // Changes WINE boolean flag
        else if (checkKeys.equals(waterToWine)) {
            ForbiddenIslandWorld.WINE = !ForbiddenIslandWorld.WINE;
        }
        // SIDE EFFECT:
        // Changes engineersLeft to infinity (9999)
        else if (checkKeys.equals(buildInfinity)) {
            ForbiddenIslandWorld.engineersLeft = 9999;
        }
        // SIDE EFFECT:
        // Changes ALIEN boolean flag
        else if (checkKeys.equals(alienInv)) {
            ForbiddenIslandWorld.ALIENS = !ForbiddenIslandWorld.ALIENS;
        }
        // SIDE EFFECT:
        // Changes HUD boolean flag
        else if (checkKeys.equals(toggleDisp)) {
            ForbiddenIslandWorld.HUD = !ForbiddenIslandWorld.HUD;
        }
        // SIDE EFFECT:
        // Changes SHUFFLE boolean flag
        else if (checkKeys.equals(shuffle)) {
            ForbiddenIslandWorld.SHUFFLE = !ForbiddenIslandWorld.SHUFFLE;
        }
        // SIDE EFFECT:
        // Changes FROZEN boolean flag
        else if (checkKeys.equals(freeze)) {
            ForbiddenIslandWorld.FROZEN = !ForbiddenIslandWorld.FROZEN;
        }
        // SIDE EFFECT:
        // Changes FLOOD_SPEED
        else if (checkKeys.equals(floodFast)) {
            if (!(ForbiddenIslandWorld.FLOOD_SPEED == 2)) {
                ForbiddenIslandWorld.FLOOD_SPEED-=2;
            }
        }
        // SIDE EFFECT:
        // Changes FLOOD_SPEED
        else if (checkKeys.equals(floodSlow)) {
            ForbiddenIslandWorld.FLOOD_SPEED+=2;
        }
    }
}

// Represents all ILists (which are iterable)
interface IList<T> extends Iterable<T>{
    // Inserts item into given list
    IList<T> insert(T t);
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

    // Inserts item to front of given list
    public IList<T> insert(T t) {
        return new Cons<T>(t, this);
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
        if (p.x < 10 || p.x > ForbiddenIslandWorld.ISLAND_SIZE * 10 - 10 &&
                p.y < 10 || p.y> ForbiddenIslandWorld.ISLAND_SIZE * 10 - 10) {
            return false;
        }
        else if(p.x <= (int)cellList.first.x * 10 + 5 && p.x >= (int)cellList.first.x * 10 - 5 && 
                p.y <= (int)cellList.first.y * 10 + 5 && p.y >= (int)cellList.first.y * 10 - 5) {
            return !cellList.first.isFlooded;
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

    Cell(double height, int x, int y, Cell left, Cell top,
            Cell right, Cell bottom, boolean isFlooded) {
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
            int colorNudge = 10 + (int)(this.height * 2);
            return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, 
                    new Color((int)Math.min(255, this.height + colorNudge), 
                            (int)Math.min(255, 140 + colorNudge),
                            (int)Math.min(255, this.height + colorNudge)));
        }
        // For cells below sea level but not flooded
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
        // For cells that are flooded
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


    OceanCell(double height, int x, int y, Cell left, Cell top, 
            Cell right, Cell bottom, boolean isFlooded) {

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
        if (ForbiddenIslandWorld.WINE) {
            return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, Color.red);
        }
        else {
            return new RectangleImage(new Posn(this.x * 10, this.y * 10), 10, 10, Color.blue);
        }

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
    Person moveOnKey(String ke, IList<Cell> board, boolean scuba) {
        // Left key move
        if (ke.equals("left") && (this.legalLocation("left", board) || 
                ForbiddenIslandWorld.GOD_MODE || scuba)) {
            location.x = location.x - 10;
            return this;
        }
        // Right key move
        else if (ke.equals("right") && (this.legalLocation("right", board) || 
                ForbiddenIslandWorld.GOD_MODE || scuba)) {
            location.x = location.x + 10;
            return this;
        }
        // Up key move
        else if (ke.equals("up") && (this.legalLocation("up", board) || 
                ForbiddenIslandWorld.GOD_MODE || scuba)) {
            location.y = location.y - 10;
            return this;
        }
        // Down key move
        else if (ke.equals("down") && (this.legalLocation("down", board) || 
                ForbiddenIslandWorld.GOD_MODE || scuba)) {
            location.y = location.y + 10;
            return this;
        }
        // Key recorder
        else if (!(ke.equals("left") || ke.equals("up") || ke.equals("down") || 
                ke.equals("right") || ke.equals("b"))) {
            ForbiddenIslandWorld.keys.add(0, ke);
            return this;
        }
        // All other cases
        else {
            return this;
        }
    }

    // Draws the pilot
    WorldImage draw() {
        if (!ForbiddenIslandWorld.ALIENS) {
            return new FromFileImage(this.location, "pilot-icon.png");   
        }
        else {
            return new FromFileImage(this.location, "alien.png"); 
        }
    }

    // Checks if a person is in the vicinity of the given posn
    boolean isNear(Posn p) {
        return this.location.x > p.x - 8 &&
                this.location.x < p.x + 8 &&
                this.location.y > p.y - 8 &&
                this.location.y < p.y + 8;
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
        return new FromFileImage(location, "target.png");
    }

    // Makes a list of 5 randomly placed targets
    public IList<Target> makeList() {
        IList<Target> output = new Empty<Target>();

        while(output.size() < 6) {
            Target t = new Target(new Posn((int)(Math.random() * 540),
                    (int)(Math.random() * 640)));

            if(Math.abs(ForbiddenIslandWorld.ISLAND_SIZE/2 * 10 - t.location.x) +
                    Math.abs(ForbiddenIslandWorld.ISLAND_SIZE/2 * 10 - t.location.y) < 300) {

                output = output.insert(t);
            }
        }
        return output;
    }
}

// Represents the helicopter, which is the end game
class HelicopterTarget extends Target {

    HelicopterTarget(Posn location) {
        super(location);
    }

    // Draws the helicopter image
    WorldImage draw() {
        if (!ForbiddenIslandWorld.ALIENS) {
            return new FromFileImage(this.location, "helicopter.png");   
        }
        else {
            return new FromFileImage(this.location, "space-ship.png"); 
        }
    }
}


//Represents the Scuba Gear a player can use
class ScubaTarget extends Target {

    ScubaTarget(Posn location) {
        super(location);
    }

    // Draws the Scuba Gear
    WorldImage draw() {

        return new FromFileImage(this.location, "scuba.png");
    }

    ScubaTarget randomLocation(IList<Cell> board) {
        ScubaTarget temp = 
                new ScubaTarget(new Posn((int)(320 * Math.random()), 
                        (int)(320 * Math.random())));

        if(board.legalLocation(temp.location)) {
            return temp;
        }
        else {
            return this.randomLocation(board);
        }
    }

    // Checks if Scuba has been located
    boolean found(Person p) {

        if(p.isNear(this.location)) {

            return true;
        }
        else {
            return false;
        }
    }
}

class ForbiddenIslandWorld extends World {

    // Represents the Island Size
    static int ISLAND_SIZE = 64;
    // Has the game started yet?
    static boolean INITIALIZED = false;
    // Checks if end condition has been met
    static boolean ENDED = false;
    // Checks if game has been paused
    static boolean PAUSED = false;
    // Checks if game has been won
    static boolean WON = false;
    // Sets the number of engineer moves
    static  int ENGINEER = 5;
    // Variable to count down to zero with keypress
    static int engineersLeft = ENGINEER;
    // Toggles God Mode
    static boolean GOD_MODE = false;
    // Turns water to wine
    static boolean WINE = false;
    // Alien Invasion
    static boolean ALIENS = false;
    // Freeze the world
    static boolean FROZEN = false;
    // Draw HUD
    static boolean HUD = false;
    // Speed that Forbidden Island Floods
    static int FLOOD_SPEED = 10;
    // Does game need to shuffle targets?
    static boolean SHUFFLE = false;
    // Array of keypresses
    static ArrayList<String> keys = new ArrayList<String>();


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
    // Shows whether or not you have scuba gear
    boolean hasScuba;
    // Represents the helicopter (Final target)
    HelicopterTarget heli; 
    // Represents the Scuba Gear
    ScubaTarget scuba;
    // Checks if you are using Scuba Gear
    boolean useScuba;
    // Times usage of Scuba gear
    int scubaTimer;

    // Constructor that automatically sets timer at 0
    ForbiddenIslandWorld(IList<Cell> board, Person person, IList<Target> targets) {
        this.board = board;
        this.waterHeight = 2;
        this.person = person;
        this.timer = 0;
        this.targets = targets;
        this.heli = new HelicopterTarget(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5));
        this.hasScuba = false;
        this.scuba = new ScubaTarget(new Posn(0,0));
        this.useScuba = false;
        this.scubaTimer = 5;
    }
    // Constructor that can take a value in for the timer
    ForbiddenIslandWorld(IList<Cell> board, Person person, int timer, IList<Target> targets) {
        this.board = board;
        this.waterHeight = 2;
        this.person = person;
        this.timer = timer;
        this.targets = targets;
        this.heli = new HelicopterTarget(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5));
        this.hasScuba = false;
        this.scuba = new ScubaTarget(new Posn(0,0));
        this.useScuba = false;
        this.scubaTimer = 5;
    }

    // Sets the conditions for each restart
    void init() {

        Target t = new Target(new Posn(0,0));
        targets = t.makeList();
        timer = 0;
        hasScuba = false;
        useScuba = false;
        scuba = scuba.randomLocation(board);
        person = new Person(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5));
        ENDED = false;
        WON = false;
        this.scubaTimer = 5;
        engineersLeft = ENGINEER;

    }

    // Floods the island
    // SIDE EFFECT:
    // Board is changed to flooded output
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
        Target t = new Target(new Posn(0,0));
        ITargetListIterator itli = new ITargetListIterator(targets);
        Cheats cheats = new Cheats();
        // Checks if game hasn't started yet and user has read first statement
        if(!INITIALIZED && keys.size() == 0) {
            // Don't do anything
        }
        // Checks to see if game hasn't started
        else if (!INITIALIZED) {
            this.board = this.generateMountainCellsTerrain();
            INITIALIZED = true;
            person = new Person(new Posn(320, 320));
            scuba = scuba.randomLocation(board);
        }
        // Checks if end condition has been met
        else if ((itli.flooded(board) || person.flooded(board)) && !GOD_MODE && !useScuba) {
            ENDED = true;
            board = new Empty<Cell>();
        }
        // Checks if game is paused
        else if (PAUSED) {
            // Stops the if statement from going on
        }
        else if (SHUFFLE) {
            this.targets = t.makeList();
            SHUFFLE = false;
        }
        else if (cheats.cheatEntered(keys)) {
            cheats.whatCheat(keys);
        }
        else if (FROZEN) {
            // Stops the if statement from going on
        }
        // Checks the see if the player has won
        else if (!targets.isCons() && person.isNear(heli.location)) {
            WON = true;
        }
        // Adds to countup timer
        else if ((timer + 1) % FLOOD_SPEED == 0) {

            ICellListIterator icli = new ICellListIterator(board);
            board = icli.flood(waterHeight);    
            timer = timer + 1;
        }

        else if (useScuba && scubaTimer <= 0) {

            useScuba = false;
            timer = timer + 1;
        }
        else if (useScuba) {
            timer = timer + 1;
            scubaTimer = scubaTimer - 1;
        }
        else {
            timer = timer + 1;
        }

    }

    // Moves Person for each key event or returns this new Island type
    //EFFECT : Changes board based on chosen Island type
    public void onKeyEvent(String ke) {
        // Returns regular mountain
        if (ke.equals("m") && !PAUSED) {
            this.board = this.generateMountainCells();
            init();
        }
        // Returns random mountain
        else if (ke.equals("r") && !PAUSED) {
            this.board = this.generateMountainCellsRandom();
            init();
        }
        // Engineers up pilot's 5x5 region
        else if(ke.equals("b") && !PAUSED && engineersLeft > 0) {
            this.board = this.engineerCells();
            engineersLeft-=1;
        }
        // Generates random terrain mountain
        else if(ke.equals("t") && !PAUSED) {
            this.board = this.generateMountainCellsTerrain();
            init();
        }
        // Pauses the game
        else if(ke.equals("p")) {
            PAUSED = !PAUSED;
        }
        // Represents backspace for cheat codes
        else if(ke.equals("`")) {
            if (keys.size() == 0) {

            }
            else { 
                keys.remove(0);
            }
        }
        // Activates Scuba Gear (with "/")
        else if(ke.equals("/") && hasScuba == true && scubaTimer >= 0) {
            useScuba = true;
        }
        // Moves the player if not paused
        else if (!PAUSED) {
            person = this.person.moveOnKey(ke, board, useScuba);
            ITargetListIterator itli = new ITargetListIterator(targets);
            targets = itli.foundTargets(person);

            // Checks if Scuba gear has been claimed
            if(scuba.found(person)) {
                hasScuba = true;
            }
        }
    }

    // Draws game HUD
    WorldImage drawHUD() {
        // Center axis for HUD
        int cAlign = ISLAND_SIZE * 8;
        //Builds left
        TextImage engLeftDraw = new TextImage(new Posn(cAlign, 50), 
                "Builds Left: " + Integer.toString(engineersLeft), 15 , 1, 
                Color.white);
        // God mode on?
        TextImage godModeDraw = new TextImage(new Posn(cAlign, 75), 
                "God Mode: " + Boolean.toString(GOD_MODE), 15 , 1, 
                Color.white);
        // Is it an alien invasion?
        TextImage alienDraw = new TextImage(new Posn(cAlign, 100), 
                "Aliens: " + Boolean.toString(ALIENS), 15 , 1, 
                Color.white);
        // Is the game frozen?
        TextImage frozenDraw = new TextImage(new Posn(cAlign, 125), 
                "Game Frozen: " + Boolean.toString(FROZEN), 15 , 1, 
                Color.white);
        // At how many ticks does the water flood?
        TextImage floodSpeedDraw = new TextImage(new Posn(cAlign, 150), 
                "Flood Speed: " + Integer.toString(FLOOD_SPEED), 15 , 1, 
                Color.white);

        return engLeftDraw.overlayImages(godModeDraw).overlayImages(alienDraw
                ).overlayImages(frozenDraw).overlayImages(floodSpeedDraw); 

    }

    // Makes an image of the given world for each tick
    public WorldImage makeImage() {
        ArrayUtils arrayUtil = new ArrayUtils();

        // Represents the current keys pressed
        TextImage keysImage = arrayUtil.drawKeys(keys);

        // Represents the amount of time spent playing the game
        TextImage time = new TextImage(new Posn(50, 50), 
                Integer.toString(this.timer), 20 , 1, 
                Color.red);

        // Represents the amount of time spent playing the game
        TextImage scubaCountDown = new TextImage(new Posn(450, 50), 
                "Scuba Timer: " + Integer.toString(this.scubaTimer), 10 , 1, 
                Color.red);

        // Represents GameHUD
        WorldImage gameHUD = this.drawHUD();
        // Produces new IList of Cells
        IListIterator<Cell> ili = new IListIterator<Cell>(board);
        // Draws common background
        WorldImage background = 
                ili.draw().overlayImages(person.draw()).overlayImages(
                        time).overlayImages(keysImage);
        // Draws helicopter
        WorldImage heliDraw = heli.draw();

        ITargetListIterator itli = new ITargetListIterator(targets);

        // Returns end image
        if (ENDED) {
            return new FromFileImage(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5), "drowning.jpeg");
        }
        else if (!INITIALIZED) {
            WorldImage text = new TextImage(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5), 
                    "READ README.rtf",
                    39, 1, Color.red);

            WorldImage text2 = new TextImage(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 6), 
                    "(Press ENTER to start)",
                    39, 1, Color.red);
            return text.overlayImages(text2);
        }
        // Draws black paused screen
        else if (PAUSED) {
            WorldImage cover =
                    new RectangleImage(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5),
                            ISLAND_SIZE * 10, ISLAND_SIZE * 10, Color.black);
            cover = new OverlayImages(itli.draw(background), cover);
            WorldImage text = new TextImage(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5), 
                    "PAUSED", 
                    39, 1, Color.white);
            return new OverlayImages(cover, text).overlayImages(gameHUD);
        }
        // Draws winner screen
        else if (WON) {
            return new FromFileImage(new Posn(ISLAND_SIZE * 5, ISLAND_SIZE * 5), "winner.jpg");
        }
        // Draws world
        else if (HUD) {
            return new OverlayImages(itli.draw(background), heliDraw).overlayImages(gameHUD);
        }
        // Draws world if scuba has been found
        else if (hasScuba){

            return itli.draw(background).overlayImages(heliDraw).overlayImages(scubaCountDown);

        }        
        // Draws world
        else {

            return itli.draw(background).overlayImages(heliDraw).overlayImages(scuba.draw());
        }
    }

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

        ArrayUtils arrayUtils = new ArrayUtils();
        cells = arrayUtils.formCells(heights);
        cells = arrayUtils.fixConnections(cells, "m");
        return arrayUtils.flatten(cells);
    }



    // Generates the lists of lists of cells for Mountain world
    IList<Cell> generateMountainCellsTerrain() {
        if (ISLAND_SIZE % 2 == 0) {
            ISLAND_SIZE = ISLAND_SIZE + 1;
        }
        else {

        }
        // Generates heights for Mountain
        ArrayList<ArrayList<Double>> heights = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();
        ArrayUtils arrayUtils = new ArrayUtils();

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

        cells = arrayUtils.formCells(heights);

        cells.get(0).get(0).height = 0;
        cells.get(0).get(ISLAND_SIZE - 1).height = 0;
        cells.get(ISLAND_SIZE - 1).get(0).height = 0;
        cells.get(ISLAND_SIZE - 1).get(ISLAND_SIZE - 1).height = 0;

        cells.get(ISLAND_SIZE / 2).get(ISLAND_SIZE / 2).height = ISLAND_SIZE;

        cells.get(0).get(ISLAND_SIZE / 2).height = 1;
        cells.get(ISLAND_SIZE - 1).get(ISLAND_SIZE / 2).height = 1;
        cells.get(0).get(ISLAND_SIZE - 1).height = 1;
        cells.get(ISLAND_SIZE / 2).get(ISLAND_SIZE - 1).height = 1;

        cells = arrayUtils.terrainCells(cells, 0, 0, ISLAND_SIZE / 2, "left");

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
        cells = arrayUtils.fixConnections(cells, "terrain");
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

        ArrayUtils arrayUtils = new ArrayUtils();
        // Fixes Cell Connections
        cells = arrayUtils.formCells(heights);
        cells = arrayUtils.fixConnections(cells, "r");
        return arrayUtils.flatten(cells);
    }

    // Engineers cells to elevate
    IList<Cell> engineerCells() {
        ICellListIterator icli = new ICellListIterator(board);
        return icli.elevate(person);
    }
}
//Examples for the ForbiddenIslandGame
class ExamplesFIGame {

 
 
 Cell c1 = new Cell(5.0, 320, 320, false);
 IList<Cell> ilc = new Cons<Cell>(c1, new Empty<Cell>());
 Target target = new Target(new Posn(0,0));
 Target target2 = new Target(new Posn(40,40));

 
 
 ForbiddenIslandWorld f1 = new ForbiddenIslandWorld(new Empty<Cell>(),
         new Person(new Posn(320,320)), target.makeList());

 Person p = new Person(new Posn(12,10));
 Posn pos1 = new Posn(10,9);
 Posn pos2 = new Posn(120, 5);
 Posn pos3 = new Posn(299, 299);
 Posn pos4 = new Posn(293, 293);
 Cell c2 = new Cell(5.0, 20, 20, false);
 Cell c3 = new Cell(5.0, 30, 30, true);
 Cell c4 = new Cell(5.0, 40, 40, false);
 ArrayList<Cell> cellArray = new ArrayList<Cell>();
 
 IList<Cell> cellList = new Cons<Cell>(c1, new Cons<Cell>(c2, 
         new Cons<Cell>(c3, new Cons<Cell>(c4, new Empty<Cell>()))));
 IList<Cell> emptyCellList = new Empty<Cell>();
 IList<Target> targetList = new Cons<Target>(target, new Cons<Target>(target2, 
         new Empty<Target>()));
 IList<Double> doubleList = new Cons<Double>(2.0, new Cons<Double>(5.4,
         new Cons<Double>(12.1, new Empty<Double>())));
 
 ICellListIterator icli = new ICellListIterator(cellList);    
 IListIterator<Cell> ili = new IListIterator<Cell>(cellList);
 IListIterator<Cell> ili2 = new IListIterator<Cell>(emptyCellList);
 ITargetListIterator itli = new ITargetListIterator(targetList);
 ArrayListIterator<Cell> ali = new ArrayListIterator<Cell>(cellArray);
 
 
 void init() {

     cellList = new Cons<Cell>(c1, new Cons<Cell>(c2, 
             new Cons<Cell>(c3, new Cons<Cell>(c4, new Empty<Cell>()))));
     emptyCellList = new Empty<Cell>();
     targetList = new Cons<Target>(target, new Cons<Target>(target2, 
             new Empty<Target>()));
     doubleList = new Cons<Double>(2.0, new Cons<Double>(5.4,
             new Cons<Double>(12.1, new Empty<Double>())));
     

     icli = new ICellListIterator(cellList);    
     ili = new IListIterator<Cell>(cellList);
     ili2 = new IListIterator<Cell>(emptyCellList);
     itli = new ITargetListIterator(targetList);
     ali = new ArrayListIterator<Cell>(cellArray);
 }
 
 
 // Test the isNear() Method
 void testIsNear(Tester t) {
     init();
     t.checkExpect(p.isNear(pos1), true);
     t.checkExpect(p.isNear(pos2), false);
 }
 // Tests the size method
 void testSize(Tester t) {
     init();
     t.checkExpect(doubleList.size(), 3);
 }
 
 // Tests the Legal Location Method
 void testLegalLocation(Tester t) {
     init();
     t.checkExpect(cellList.legalLocation(pos1), true);
     t.checkExpect(cellList.legalLocation(pos2), true);
     t.checkExpect(cellList.legalLocation(pos3), false);
     t.checkExpect(cellList.legalLocation(pos4), true);
     
     
 }
 
 // Tests the makeList method 
 void testMakeList(Tester t) {
     init();
     t.checkExpect(target.makeList().size(), 6);
     t.checkExpect(target2.makeList().size(), 6);
     
 }
 
 void testIterator(Tester t) {
     init();
     
     cellArray.add(c1);
     cellArray.add(c2);
     cellArray.add(c3);
     
     
     t.checkExpect(ili.next(), c1);
     t.checkExpect(ili.hasNext(), true);
     t.checkExpect(ili2.hasNext(), false);
     t.checkExpect(itli.hasNext(), true);
     t.checkExpect(itli.next(), target);
     t.checkExpect(ali.hasNext(), true);
     t.checkExpect(ali.next(), c1);
     t.checkExpect(icli.hasNext(), true);
     t.checkExpect(icli.next(), c1);
 }
 
 void testFlood(Tester t) {
     init();
     t.checkExpect(icli.flood(4).size(), cellList.size());
 }
 
/*   
 void runGame(){
     this.f1.bigBang(640, 640, .5);
 }
*/
 
}