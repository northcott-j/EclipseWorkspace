
// Assignment 9
// Alex Melagrano
// alex3232
// Zach Lowen
// $$$$$$$$$$

// imported jars

import java.awt.Color;
import java.util.ArrayList;

import tester.Tester;
//import javalib.impworld.*;
import javalib.colors.*;
import javalib.worldimages.*;
import javalib.impworld.*;



// IList skeleton; either empty or non-empty
interface IList<T> {
    // checks if this IList<T> is a cons
    boolean isCons();
    // casts this IList<T> as a cons
    public Cons<T> asCons();
    // counts this list
    int length();
}

// represents an empty list
class Empty<T> implements IList<T> {
    // checks if this IList<T> is a cons
    public boolean isCons() {
        return false;
    }
    // casts this IList<T> as a cons
    public Cons<T> asCons() {
        return null;
    }
    // counts this list
    public int length() {
        return 0;
    }
}

// represents a non-empty list
class Cons<T> implements IList<T> {
    T data;
    IList<T> rest;

    Cons(T data, IList<T> rest) {
        this.data = data;
        this.rest = rest;
    }
    // checks if this IList<T> is a cons
    public boolean isCons() {
        return true;
    }
    // casts this IList<T> as a cons
    public Cons<T> asCons() {
        return this;
    }
    // counts this list
    public int length() {
        return 1 + rest.length();
    }
}

//Represents the ability to produce a sequence of values
//of type T, one at a time
interface Iterator<T> {
    // Does this sequence have at least one more value?
    boolean hasNext();
    // Get the next value in this sequence
    // EFFECT: Advance the iterator to the subsequent value
    T next();
    // EFFECT: Remove the item just returned by next()
    // NOTE: This method may not be supported by every iterator; ignore it for now
    void remove();
}

class IListIterator<T> implements Iterator<T> {
    IList<T> items;
    IListIterator(IList<T> items) {
        this.items = items;
    }
    // In IListIterator
    public boolean hasNext() {
      return this.items.isCons();
    }
    // In IListIterator
    public T next() {
        Cons<T> itemsAsCons = this.items.asCons();
        T answer = itemsAsCons.data;
        this.items = itemsAsCons.rest;
        return answer;
    }
    public void remove() {
        throw new UnsupportedOperationException("Don't do this!");
    }
}

class TargetIterator implements Iterator<Target> {
    IList<Target> items;
    
    TargetIterator(IList<Target> items) {
        this.items = items;
    }
    // In IListIterator
    public boolean hasNext() {
      return this.items.isCons();
    }
    // In IListIterator
    public Target next() {
        Cons<Target> itemsAsCons = this.items.asCons();
        Target answer = itemsAsCons.data;
        this.items = itemsAsCons.rest;
        return answer;
    }
    public void remove() {
        throw new UnsupportedOperationException("Don't do this!");
    }
}


// Represents a single square of the game area
class Cell {
    // represents absolute height of this cell, in feet
    double height;
    // In logical coordinates, with the origin at the top-left corner of the screen
    int x, y;
    // the four adjacent cells to this one
    Cell left, top, right, bottom;
    // reports whether this cell is flooded or not
    boolean isFlooded;
    // constructor for initial state of the cell
    Cell(double height, int x, int y, boolean isFlooded) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.isFlooded = isFlooded;
    }
    // constructor for fully fledged cell
    Cell(double height, int x, int y, Cell left, Cell top, Cell right, Cell bottom, 
            boolean isFlooded) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.isFlooded = isFlooded;
    }

    // checks if this cell is submerged
    boolean submerged() {
        return this.isFlooded;
    }
    // checks if any of this cell's neighbors are flooded
    boolean floodedSide() {
        return (this.left.isFlooded) ||
                (this.top.isFlooded) ||
                (this.right.isFlooded) ||
                (this.bottom.isFlooded);
    }
    // checks if any of this cell's neighbors are flooded
    boolean landSide() {
        return !(this.left instanceof OceanCell) &&
                !(this.top instanceof OceanCell) &&
                !(this.right instanceof OceanCell) &&
                !(this.bottom instanceof OceanCell);
    }
    // creates a new rectangle image based on the data from this cell
    RectangleImage cellToRekt(double waterHeight) {
        // represents the size of a block
        int size = ForbiddenIslandWorld.BLOCK_SIZE;
        // helps place the block at the correct pinhole posn
        int center = size/2;
        // calculates the color of a standard non-ocean cell
        int r = (int) Math.min(255, (this.height * 10) - (waterHeight * 10));
        int g = 255;
        int b = (int) Math.min(255, (this.height * 10) - (waterHeight * 10));
        // calculates the color of a flooded non-ocean cell
        int x = 0;
        int y = 0;
        int z = (int) Math.max(0, 255 - (waterHeight - this.height) * 10); 
        // calculates the color of a non-ocean cell below waterLevel
        int c = (int) Math.min(255, (waterHeight * 10) - (this.height * 10));
        int d = (int) Math.max(0, 255 - (waterHeight - this.height) * 10);
        int e = 0;

        // for ocean cells
        if (this.height == 0) {
            return new RectangleImage(new Posn(this.x * size + center, this.y * size + center), 
                    size, size, new Blue());
        }
        // for flooded non-ocean cells
        else if ((this.height < waterHeight) && this.floodedSide()) {
            return new RectangleImage(new Posn(this.x * size + center, this.y * size + center), 
                    size, size, new Color(x, y, z));
        }
        // for non-ocean, non-flooded cells below waterHeight
        else if (this.height < waterHeight) {
            return new RectangleImage(new Posn(this.x * size + center, this.y * size + center), 
                    size, size, new Color(c, d, e));
        }
        else
        {
            // for standard non-ocean cells
            return new RectangleImage(new Posn(this.x * size + center, this.y * size + center), 
                    size, size, new Color(r, g, b));
        }
    }
}

// represents a flooded cell
class OceanCell extends Cell {
    // constructor for initial state of the cell
    OceanCell(double height, int x, int y, boolean isFlooded) {
        super(height, x, y, isFlooded);
    }
}

// class for the Forbidden Island Game
class ForbiddenIslandWorld extends World {
    // All the cells of the game, including the ocean
    IList<Cell> board;
    // the current height of the ocean
    double waterHeight;
    // the player controlled by the user
    Pilot player;
    // the heights of the game as an array
    ArrayList<ArrayList<Double>> heightArray;
    // the cells of the game as an array
    ArrayList<ArrayList<Cell>> boardArray;
    // the list of targets in the game
    TargetIterator targets;
    // counts the number of player movements
    int movements;
    // counts down the number of ticks before the island is submerged
    int countdown;
    
    // creates a new FIW
    ForbiddenIslandWorld() {
    }

    // Defines an int constant
    static final int ISLAND_SIZE = 65;
    // defines an int constant
    static final int BLOCK_SIZE = 10;
    // defines an int constant
    static final double ISLAND_HEIGHT = ISLAND_SIZE / 2.0;
    // defines the center posn
    Posn center = (new Posn(ISLAND_SIZE * BLOCK_SIZE, ISLAND_SIZE * BLOCK_SIZE));

    // ========= TERRAIN GENERATION ========= //

    // computes the manhattan distance from the center
    int manhattanDist(int x, int y) {
        return (Math.abs(x - (ISLAND_SIZE / 2)) + Math.abs(y - (ISLAND_SIZE / 2)));
    }

    // creates the ArrayList<ArrayList<Double>> of heights representing a regular mountain
    void mountainHeight() {
        // initializes the Array
        ArrayList<ArrayList<Double>> heightRows = 
                new ArrayList<ArrayList<Double>>(); 

        for(int i = 0; i < ISLAND_SIZE; i +=1) {
            ArrayList<Double> heightColumn = new ArrayList<Double>();
            for(int k = 0; k < ISLAND_SIZE; k +=1) {
                heightColumn.add((ISLAND_SIZE / 2.0 - manhattanDist(i, k)));
            }
            heightRows.add(heightColumn);
        } 
        this.heightArray = heightRows;
    }
    // creates the ArrayList<ArrayList<Double>> of heights representing a random mountain
    void randomHeight() {
        // initializes the Array
        ArrayList<ArrayList<Double>> heightRows = 
                new ArrayList<ArrayList<Double>>(); 

        for(int i = 0; i < ISLAND_SIZE; i +=1) {
            ArrayList<Double> heightColumn = new ArrayList<Double>();
            for(int k = 0; k < ISLAND_SIZE; k +=1) {
                if (manhattanDist(i, k) < (ISLAND_SIZE / 2.0)) {
                heightColumn.add(1.0 + (int) (Math.random() * ISLAND_HEIGHT / 2));
                }
                else 
                {
                    heightColumn.add(0.0);
                }
            }
            heightRows.add(heightColumn);
        } 
        this.heightArray = heightRows;
    }

    // creates the ArrayList<ArrayList<Cell>> representing the cells without neighbors
    void cellArray() {
        // initializes the Array of cells
        ArrayList<ArrayList<Cell>> cellsRows = new ArrayList<ArrayList<Cell>>(); 

        for(int i = 0 ; i < ISLAND_SIZE ; i +=1) {
            ArrayList<Cell> cellColumn = new ArrayList<Cell>();
            for(int k = 0 ; k < ISLAND_SIZE ; k +=1) {
                double height = this.heightArray.get(i).get(k);
                // stores the value of the cell at this point
                
                if (height > 0) {
                    cellColumn.add(new Cell(height, i, k, height < this.waterHeight));
                }
                else 
                {
                    cellColumn.add(new OceanCell(0, i, k, true));
                }
            }           
            cellsRows.add(cellColumn);
        } 
        this.boardArray = cellsRows;
    }
    // mutates the cell data to include left and top
    void leftTop() {
        for(ArrayList<Cell> x : this.boardArray) {
            for(Cell y : x) {
                // for the top left corner
                if (this.boardArray.indexOf(x) == 0 && x.indexOf(y) == 0) {
                    y.left = y;
                    y.top = y;
                }
                // when the cell is on the left edge
                else if (this.boardArray.indexOf(x) == 0) {
                    y.left = y;
                    y.top = this.boardArray.get(this.boardArray.indexOf(x)).get(x.indexOf(y) - 1);
                }
                // when the cell is on the top edge
                else if (x.indexOf(y) == 0) {
                    y.left = this.boardArray.get(this.boardArray.indexOf(x) - 1).get(x.indexOf(y));
                    y.top = y;
                }
                // when the cell is not on an edge
                else
                { 
                    y.left = this.boardArray.get(this.boardArray.indexOf(x) - 1).get(x.indexOf(y));
                    y.top = this.boardArray.get(this.boardArray.indexOf(x)).get(x.indexOf(y) - 1);
                }
            } 
        } 
    }
    // mutates the cell data to include right and bottom
    void rightBottom() {
        for(ArrayList<Cell> x : this.boardArray) {
            for(Cell y : x) {
                // for the bottom right corner
                if (this.boardArray.indexOf(x) + 1 == ISLAND_SIZE && x.indexOf(y) + 1 == ISLAND_SIZE) {
                    y.right = y;
                    y.bottom = y;
                }
                // when the cell is on the right edge
                if (this.boardArray.indexOf(x) + 1 == ISLAND_SIZE) {
                    y.right = y;
                    y.bottom = this.boardArray.get(this.boardArray.indexOf(x)).get(x.indexOf(y));
                }
                // when the cell is on the bottom edge
                else if (x.indexOf(y) + 1 == ISLAND_SIZE) {
                    y.right = this.boardArray.get(this.boardArray.indexOf(x) + 1).get(x.indexOf(y));
                    y.bottom = y;
                }
                // when the cell is not on an edge
                else
                { 
                    y.right = this.boardArray.get(this.boardArray.indexOf(x) + 1).get(x.indexOf(y));
                    y.bottom = this.boardArray.get(this.boardArray.indexOf(x)).get(x.indexOf(y));
                }
            } 
        } 
    }
    // mutates the entire board of cells to include their neighboring cells
    void terrainArray() {
        this.leftTop();
        this.rightBottom();
    }
    // converts the ArrayList<ArrayList<Cell>> from terrainArray() to an IList<Cell>,
    // and updates the board field of ForbiddenIslandWorld
    void makeBoard() {
        // initializes the overall list accumulator
        IList<Cell> acc = new Empty<Cell>();
        
        for(ArrayList<Cell> x : this.boardArray) {
            for(Cell y : x) {
                acc = (new Cons<Cell>(y, acc));
            }     
        } 
        this.board = acc;
    }
    // updates the board each tick by checking the flood's effect
    void checkFlood() {
        
        for(ArrayList<Cell> x : this.boardArray) {
            for(Cell y : x) {
                if (y.height < this.waterHeight && y.floodedSide()) {
                    y.isFlooded = true;
                }
            }     
        } 
    }
     
    // ========= DRAWING/GAME METHODS ========= //

    // initializes the pilot
    void initPilot() {
        
        // creates the random values
        int randX = ((int) (Math.random() * ForbiddenIslandWorld.ISLAND_SIZE));
        int randY = ((int) (Math.random() * ForbiddenIslandWorld.ISLAND_SIZE));
        if ((Math.abs(randX - (ISLAND_SIZE / 2)) + Math.abs(randY - (ISLAND_SIZE / 2))) < 32); {
//                !this.boardArray.get(randX).get(randY).isFlooded) {
//            this.initPilot();
            this.player = new Pilot(randX, randY, 0);
        }
        
    }
    // initializes a target's position
    Posn initTarget() {
        // creates the random values
        int randX = (int) (Math.random() * ForbiddenIslandWorld.ISLAND_SIZE);
        int randY = (int) (Math.random() * ForbiddenIslandWorld.ISLAND_SIZE);
        
        while (!this.boardArray.get(randX).get(randY).isFlooded) {
            this.initTarget();
        }
        return new Posn(randX, randY);
    }
    // initializes the targets
    void initTargets() {
        // stores the value of the TargetIterator
        TargetIterator targets = this.targets;
        
        while (targets.hasNext()) {
            // first value in the cells list
            Target first = targets.next();
            // initializes position of target
            Posn center = this.initTarget();
            
            first.x = center.x;
            first.y = center.y;          
        }
    }
    // checks if the player caught a helicopter piece
    void gotPiece() {
        // stores the value of the TargetIterator
        TargetIterator targets = this.targets;
        
        while (targets.hasNext()) {
            // first value in the cells list
            Target first = targets.next();
            // stores the player's
            if (first.x == this.player.x && first.y == this.player.y) {
                first.isCaught = true;
                this.player.pieces = this.player.pieces + 1;
            }
        }
    }
    // checks if any of the helicopter parts have been submerged
    boolean submergedParts() {
        
        while (this.targets.hasNext()) {
            Target first = this.targets.next();
            ArrayList<Cell> column = this.boardArray.get(first.x);
            Cell cell = column.get(first.y);
            return (cell instanceof OceanCell); 
        }
        return true;
    }
    // updates the world on key events
    public void onKeyEvent(String ke) {
        ArrayList<ArrayList<Cell>> cellArray = this.boardArray;
        int x = this.player.x;
        int y = this.player.y;
        // updates the pilot based on the key event
        if (ke.equals("left") && !cellArray.get(x - 1).get(y).isFlooded) {       
            this.player.x = x - 1;        
            // updates the move counter
            this.movements = this.movements + 1;
        }       
        else if (ke.equals("up") && !cellArray.get(x).get(y - 1).isFlooded) {  
            this.player.y = y - 1; 
            // updates the move counter
            this.movements = this.movements + 1;
        }
        else if (ke.equals("right") && !cellArray.get(x + 1).get(y).isFlooded) {  
            this.player.x = x + 1;   
            // updates the move counter
            this.movements = this.movements + 1;
        } 
        else if (ke.equals("down") && !cellArray.get(x).get(y + 1).isFlooded) {
            this.player.y = y + 1; 
            // updates the move counter
            this.movements = this.movements + 1;
        }
        else if (ke.equals("m")) {
            this.waterHeight = 0;
            this.countdown = 10 * (ISLAND_SIZE / 2);
            this.movements = 0;
            this.mountainHeight();
            this.cellArray(); 
            this.terrainArray(); 
            this.makeBoard(); 
        }
        else if (ke.equals("r")) {
            this.waterHeight = 0;
            this.countdown = 10 * (ISLAND_SIZE / 2);
            this.movements = 0;
            this.randomHeight();
            this.cellArray(); 
            this.terrainArray(); 
            this.makeBoard(); 
        }
    }
    // draws the move count
    public WorldImage movementText() {
        return new TextImage(new Posn(60, 20), 
                "Move count : " + Integer.toString(this.movements),
                new Red());
    }
    // draws the move count
    public WorldImage countdownText() {
        return new TextImage(new Posn(60, 40), 
                "Ticks left : " + Integer.toString(this.countdown),
                new Red());
    }
    // produces an image of the current world's targets
    public WorldImage drawTargets() {
        // stores the value of the TargetIterator
        TargetIterator targets = this.targets;
        // accumulator image
        WorldImage targetImage = new RectangleImage(new Posn(0, 0), 1, 
                1, new Blue());

        while (targets.hasNext()) {
            // first value in the cells list
            Target first = targets.next();
            if (first.count == 0) {
                targetImage = targetImage.overlayImages(first.drawTarget());
            }
            else
            {
                HelicopterTarget heli = (HelicopterTarget) first;
                targetImage = targetImage.overlayImages(heli.drawHelicopterTarget());
            }
        }
        return targetImage;
    }  
    // produces an image of the current world's terrain
    public WorldImage drawTerrain() {
        // initializes the world's board
        this.makeBoard();
        // stores the value of the IList<Cell>
        IList<Cell> cells = this.board;
        // accumulator images, to distribute the processing load
        WorldImage terrain1 = new RectangleImage(new Posn(0, 0), 1, 1, new Blue());
        WorldImage terrain2 = new RectangleImage(new Posn(0, 0), 1, 1, new Blue());
        WorldImage terrain3 = new RectangleImage(new Posn(0, 0), 1, 1, new Blue());
        WorldImage terrain4 = new RectangleImage(new Posn(0, 0), 1, 1, new Blue());
        // assigns the block to its proper accumulator image
        int count = 0;
        
        for (int i = 0; i < board.length(); i +=1) {
            // first value in the cells list
            Cell first = cells.asCons().data;
            if (count == 0) {
            terrain1 = terrain1.overlayImages(first.cellToRekt(this.waterHeight));
            count = count + 1;
            }
            else if (count == 1) {
                terrain2 = terrain2.overlayImages(first.cellToRekt(this.waterHeight));
                count = count + 1;
            }
            else if (count == 2) {
                terrain3 = terrain3.overlayImages(first.cellToRekt(this.waterHeight));
                count = count + 1;
            }
            else if (count == 3) {
                terrain4 = terrain4.overlayImages(first.cellToRekt(this.waterHeight));
                count = 0;
            }
            cells = cells.asCons().rest;

        }
        return new OverlayImages(terrain1, new OverlayImages(terrain2, 
                new OverlayImages(terrain3, terrain4)));
    }
    // makes an image of the extra text
    public WorldImage drawText() {
        return new OverlayImages(this.movementText(), this.countdownText());
    }
    // produces an image of the current world
    public WorldImage makeImage() {
        return new OverlayImages(this.drawTerrain(), 
                new OverlayImages(this.drawTargets(), 
                        new OverlayImages(this.player.drawPilot(),
                                this.drawText())));
    }
    // creates a new updated world each tick
    public void onTick() {
        // update the water height
        waterHeight = this.waterHeight + 0.1;
        // makes the updated terrain
        this.checkFlood(); 
        // checks if the player has caught a helicopter piece
  //      this.gotPiece();
        // counts down the number of ticks left
        this.countdown = this.countdown - 1;

    }
    // checks if the game has ended
//    public WorldEnd worldEnds() {
//        
//        if (this.player.pieces == 4) {
//            return new WorldEnd(true, new FromFileImage(center, "win-icon.jpg"));
//        }
    
//        else if (this.sunkenParts()) {
//            return new WorldEnd(true, new FromFileImage(center, "rekt-icon.jpg"));
//        }
//    }
}

// represents a Pilot (player): uses a host cell for its coordinates and movement
class Pilot {
    int x;
    int y;
    int pieces;
    
    Pilot(int x, int y, int pieces) {
        this.x = x;
        this.y = y;
        this.pieces = pieces;
    }

    // creates an image for the pilot
    WorldImage drawPilot() {
        int size = ForbiddenIslandWorld.BLOCK_SIZE;
        // computes the coordinates of the pilot
        int a = x * size + size / 2;
        int b = y * size + size / 2;
        return new FromFileImage(new Posn(a, b), "pilot-icon.png");
    }
}

// represents a generic target
class Target {
    int x;
    int y;
    int count;
    boolean isCaught;
    
    Target(int x, int y, int count, boolean isCaught) {
        this.x = x;
        this.y = y;
        this.count = count;
        this.isCaught = isCaught;
    }
    
    // creates an image for the pilot
    WorldImage drawTarget() {
        int size = ForbiddenIslandWorld.BLOCK_SIZE;
        // computes the coordinates of the target
        int a = x * size + size / 2;
        int b = y * size + size / 2;
        return new FromFileImage(new Posn(a, b), "target-icon.png");
    }
}
// represents the helicopter target
class HelicopterTarget extends Target {

    HelicopterTarget(int x, int y, int count, boolean isCaught) {
        super(x, y, count, isCaught);

    }
    // creates an image for the pilot
    WorldImage drawHelicopterTarget() {
        int size = ForbiddenIslandWorld.ISLAND_SIZE;
        int block = ForbiddenIslandWorld.BLOCK_SIZE;        
        // computes the coordinates of the helicopter
        int a = block * size / 2;
        int b = block * size / 2;
        return new FromFileImage(new Posn(a, b), "helicopter-icon.png");
    }
}

class ExamplesMethods {
    // initializes the world's terrain
    ForbiddenIslandWorld initWorld = new ForbiddenIslandWorld();
    Pilot pilot1 = new Pilot(30, 30, 0);
    IList<Target> targetsList = new Cons<Target>(new HelicopterTarget(32, 32, 3, false), 
            new Cons<Target>(new Target(20, 20, 0, false), 
                    new Cons<Target>(new Target(45, 45, 0, false), 
                    new Cons<Target>(new Target(32, 16, 0, false), new Empty<Target>()))));
    TargetIterator targets1 = new TargetIterator(targetsList);
//    Cell land = new Cell(2.0, 16, 32, false);
//    Cell ocean = new Cell(-1.0, 5, 5, true);
    // constants
    int island = ForbiddenIslandWorld.ISLAND_SIZE;
    int block = ForbiddenIslandWorld.BLOCK_SIZE;
    int worldSize = island * block;
//
//        // tests the manhattanDist method
//        void testManhattanDist(Tester t) {
//            t.checkExpect(world1.manhattanDist(30, 32), 2);
//            t.checkExpect(world1.manhattanDist(24, 31), 9);
//            t.checkExpect(world1.manhattanDist(56, 3), 53);
//            t.checkExpect(world1.manhattanDist(50, 50), 36);
//        }
//        // tests the cellToRekt method
//        void testCellToRekt(Tester t) {
//            t.checkExpect(land.cellToRekt(world1.waterHeight), new RectangleImage(new Posn(160, 320), 
//                    10, 10, new Color(20, 255, 20)));
//            t.checkExpect(ocean.cellToRekt(world1.waterHeight), new RectangleImage(new Posn(50, 50), 
//                    10, 10, new Blue()));
//    
//        }
        // tests
//        void test(Tester t) {
//           world2.terrain;
//        }
        {
            // initializes initWorld's fields
            initWorld.movements = 0;
            initWorld.waterHeight = 0;
            initWorld.countdown = 320;
            initWorld.mountainHeight();
            initWorld.cellArray(); 
            initWorld.terrainArray(); 
            initWorld.makeBoard(); 
         //   initWorld.initPilot();
            initWorld.player = pilot1;
            initWorld.targets = targets1;
         //   initWorld.initTarget();
            
            this.initWorld.bigBang(worldSize, worldSize, 0.1);
        }
}