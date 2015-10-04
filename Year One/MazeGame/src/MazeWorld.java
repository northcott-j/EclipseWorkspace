import java.awt.Color;
import java.util.*;

import tester.*;
import javalib.impworld.*;
import javalib.colors.*;
import javalib.worldimages.*;

// Array Utility
class ArrayUtil {
    // Draws nodes
    WorldImage makeImage(ArrayList<ArrayList<Node>> nodes, boolean player, 
            boolean gameOver, boolean neon, boolean raceMode) {
        // Divides the recursive overlay images into six different parts
        WorldImage boardImage1 = new RectangleImage(new Posn(0, 0), 1, 1, Color.white);
        WorldImage boardImage2 = new RectangleImage(new Posn(0, 0), 1, 1, Color.white);
        WorldImage boardImage3 = new RectangleImage(new Posn(0, 0), 1, 1, Color.white);
        WorldImage boardImage4 = new RectangleImage(new Posn(0, 0), 1, 1, Color.white);
        WorldImage boardImage5 = new RectangleImage(new Posn(0, 0), 1, 1, Color.white);
        WorldImage boardImage6 = new RectangleImage(new Posn(0, 0), 1, 1, Color.white);
        int count = 0;
        for (int y = 0 ; y < MazeGame.HEIGHT ; y += 1) {
            for (int x = 0 ; x < MazeGame.WIDTH ; x += 1) {
                // Gets next Cell
                Node n = nodes.get(y).get(x);
                WorldImage nodeImage = n.draw(player, gameOver, neon, raceMode);
                // Each if branch designates which accumulator the node image goes onto
                if (count % 6 == 0) {
                    boardImage1 = new OverlayImages(boardImage1, nodeImage);
                }
                else if (count % 6 == 1) {
                    boardImage2 = new OverlayImages(boardImage2, nodeImage);
                }
                else if (count % 6 == 2) {
                    boardImage3 = new OverlayImages(boardImage3, nodeImage);
                }
                else if (count % 6 == 3) {
                    boardImage4 = new OverlayImages(boardImage4, nodeImage);
                }
                else if (count % 6 == 4) {
                    boardImage5 = new OverlayImages(boardImage5, nodeImage);
                }
                else if (count % 6 == 5) {
                    boardImage6 = new OverlayImages(boardImage6, nodeImage);
                }
                count += 1;
            }
        }
        return new OverlayImages(boardImage1,
                new OverlayImages(boardImage2,
                        new OverlayImages(boardImage3,
                                new OverlayImages(boardImage4,
                                        new OverlayImages(boardImage5, boardImage6)))));
    }

}


// Hash Map Utility
class HashMapUtil<T, R> {

    // Returns root value
    T findRoot(HashMap<T, T> map, T start) {
        T rootKey = start;
        while (!rootKey.equals(map.get(rootKey))) {
            rootKey = map.get(rootKey);
        }
        return rootKey;
    }

    // SIDE EFFECT:
    // Mutates boolean flags on the nodes that are on the final path
    // Works backward from Target to start
    void reconstruct(HashMap<String, Edge> map, String start) {
        String backTrack = start;
        Posn startPosn = MazeGame.START_POSITION;
        String startID = ((Integer)startPosn.x).toString() + "," + 
        ((Integer)startPosn.y).toString();
        while (!backTrack.equals(startID)) {
            Edge lastStep = map.get(backTrack);
            lastStep.to.onPath = true;
            lastStep.from.onPath = true;
            backTrack = lastStep.from.nodeID();
        }
    }
}

// Class for a Node
class Node {
    // In logical coordinates, with the origin at the top-left corner of the screen
    int x;
    int y;
    // the four adjacent Nodes to this one
    Node left;
    Node top;
    Node right;
    Node bottom;
    // Already been checked by search
    boolean wasSeen;
    // Search head Node
    boolean searchHead;
    // Node that's part of the final path
    boolean onPath;
    // Is this Node part of the search algorithm?
    boolean searchNode;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.left = this;
        this.top = this;
        this.right = this;
        this.bottom = this;
        this.wasSeen = false;
        this.searchHead = false;
        this.onPath = false;
        this.searchNode = false;
    }

    // Draws node
    WorldImage draw(boolean player, boolean gameOver, boolean neon, boolean raceMode) {
        int halfCell = MazeGame.CELLSIZE / 2;
        int screenX = x * MazeGame.CELLSIZE;
        int screenY = y * MazeGame.CELLSIZE;
        Posn topLeft = new Posn(screenX - halfCell, screenY - halfCell);
        Posn topRight = new Posn(screenX + halfCell, screenY - halfCell);
        Posn btmLeft = new Posn(screenX - halfCell, screenY + halfCell);
        Posn btmRight = new Posn(screenX + halfCell, screenY + halfCell);
        // Base color for each node
        WorldImage base = new RectangleImage(new Posn(screenX, screenY), 
                MazeGame.CELLSIZE, MazeGame.CELLSIZE, Color.white);
        // Draws the lines on the side
        WorldImage left = new LineImage(topLeft, btmLeft, Color.black);
        WorldImage top = new LineImage(topLeft, topRight, Color.black);
        WorldImage right = new LineImage(topRight, btmRight, Color.black);
        WorldImage bottom = new LineImage(btmLeft, btmRight, Color.black);
        
        LineImage horizontal = new LineImage(topRight, btmRight, Color.black);
        WorldImage vertical = new LineImage(btmLeft, btmRight, Color.black);
        
        
        // Option to invert color scheme
        if (neon) {

            base = new RectangleImage(new Posn(screenX, screenY), 
                    MazeGame.CELLSIZE, MazeGame.CELLSIZE, Color.BLACK);
            // Sets the color for the finished path 
            if (onPath && gameOver) {
                base.color = Color.MAGENTA;
            }
            // Sets the color for the searchHead for the player
            else if (this.searchHead && player) {
                base.color = Color.GREEN;            
            }
            // Sets the color for the wasSeen node for the player
            else if (this.wasSeen && player) {
                base.color = Color.YELLOW;
            }
            // Non-player searchHead
            else if (this.searchHead) {
                base.color = Color.WHITE;
            }
            // Non-player searchHead
            else if (this.wasSeen) {
                base.color = new Color(0, 216, 255);   
            }
            
            left = new LineImage(topLeft, btmLeft, Color.white);
            top = new LineImage(topLeft, topRight, Color.white);
            right = new LineImage(topRight, btmRight, Color.white);
            bottom = new LineImage(btmLeft, btmRight, Color.white);
            
        }
        // RaceMode colors
        else if (raceMode) {

            base = new RectangleImage(new Posn(screenX, screenY), 
                    MazeGame.CELLSIZE, MazeGame.CELLSIZE, Color.white);

            if (onPath && gameOver) {
                base.color = Color.BLACK;  
            }
            else if (this.searchHead && this.searchNode) {
                base.color = Color.BLACK;
            }
            else if (this.wasSeen && this.searchNode) {
                base.color = new Color(0, 0, 255);   
            }
            else if (this.searchHead && player) {
                base.color = Color.BLACK;            
            }
            else if (this.wasSeen && player && !this.searchNode) {
                base.color = new Color(242, 0, 0);
            }

        }
        // Normal color scheme -> same purposes as above
        else {
           
            base = new RectangleImage(new Posn(screenX, screenY), 
                    MazeGame.CELLSIZE, MazeGame.CELLSIZE, Color.white);

            if (onPath && gameOver) {
                base.color = Color.BLACK;  
            }
            else if (this.searchHead && player) {
                base.color = Color.BLUE;            
            }
            else if (this.wasSeen && player) {
                base.color = new Color(242, 0, 0);
            }
            else if (this.searchHead) {
                base.color = Color.BLACK;
            }
            else if (this.wasSeen) {
                base.color = new Color(0, 0, 255);   
            }

        }

        if (this.left.equals(this)) {
            
            base = new OverlayImages(base, left);
        }
        if (this.right.equals(this)) {
            base = new OverlayImages(base, right);
        }
        if (this.top.equals(this)) {
            base = new OverlayImages(base, top);
        }
        if (this.bottom.equals(this)) {
            base = new OverlayImages(base, bottom);
        }

        return base;
    }

    // Returns Node ID
    String nodeID() {
        return ((Integer)this.x).toString() + "," + ((Integer)this.y).toString();
    }
}

// Class for Node edges
class Edge implements Comparable {
    
    // Indicates the edge's starting node
    Node from;
    // Represents what direction the edge goes in
    String direction;
    // Indicates the edge's ending node
    Node to;
    // Represents the weight of the edge
    int weight;

    Edge(Node from, String direction, Node to) {

        this.from = from;
        this.direction = direction;
        this.to = to;
        // The weight is randomized to order them for Kruskal's algorithm
        this.weight = (int)(Math.random() * 1000);

    }
    
    // Compares two objects with each other to determine the difference in weight
    public int compareTo(Object comparesto) {
        int compareweight = ((Edge)comparesto).weight;
        return this.weight - compareweight;
    }
}

// Used to pick start and finish points
class PointPicker {
    // Represents the x- and y- values of the point when being picked
    int x;
    int y;

    PointPicker() {
        this.x = 0;
        this.y = 0;
    }

    // Moves the Point Picker
    void keyPress(String ke) {
        if (ke.equals("left")) {
            if (this.x != 0) {
                this.x -= 1;
            }  
        }
        else if (ke.equals("right")) {
            if (MazeGame.WIDTH - 1 != x) {
                this.x += 1;
            }    
        }
        else if (ke.equals("up")) {
            if (this.y != 0) {
                this.y -= 1;
            }   
        }
        else if (ke.equals("down")) {
            if (MazeGame.HEIGHT - 1 != y) {
                this.y += 1;
            } 
        }
        else if (ke.equals("h")) {
            this.setPosition();
        }
        // Automatically sets the two points at regular locations (opposite ends of maze)
        //EFFECT: Mutates the start/end positions for the maze game
        else if (ke.equals("`")) {
            MazeGame.START_POSITION = new Posn(0, 0);
            MazeGame.START_CHOSEN = true;
            MazeGame.TARGETID = 
                    ((Integer)(MazeGame.WIDTH - 1)).toString() + "," +
                            ((Integer)(MazeGame.HEIGHT - 1)).toString();
            MazeGame.END_POSITION = new Posn(MazeGame.WIDTH - 1, MazeGame.HEIGHT - 1);
            MazeGame.END_CHOSEN = true;
            MazeGame.POINTS_CHOSEN = true;
        }
    }

    // Determines where to place the start/finish point based on player key input
    //EFFECT Mutates the start/end points for the maze game
    void setPosition() {
        if (!MazeGame.START_CHOSEN) {
            MazeGame.START_POSITION = new Posn(this.x, this.y);
            MazeGame.START_CHOSEN = true;
        }
        else if (this.x == MazeGame.START_POSITION.x &&
                 this.y == MazeGame.START_POSITION.y) {
            // Start position is set
        }
        else {
            MazeGame.TARGETID = 
                    ((Integer)this.x).toString() + "," +
                            ((Integer)this.y).toString();
            MazeGame.END_POSITION = new Posn(this.x, this.y);
            MazeGame.END_CHOSEN = true;
            MazeGame.POINTS_CHOSEN = true;
        }
    }
}

// Class for the Main Game
class MazeGame extends World {
    // The Width of the board
    static final int WIDTH = 100;
    // The Height of the board
    static final int HEIGHT = 60;
    // Size of each cell in pixels
    static final int CELLSIZE = 10;
    // Has the game been initialized?
    static boolean INITIALIZED = false;
    // Sets whether or not to build the maze step by step 
    static boolean STEPBYSTEP = false;
    // Shows whether or not the game is depth searching
    static boolean DEPTH_SEARCHING = false;
    // Shows whether or not the game is breadth searching
    static boolean BREADTH_SEARCHING = false;
    // Is the start point chosen?
    static boolean START_CHOSEN = false;
    // Is the end point chosen?
    static boolean END_CHOSEN = false;
    // Are both points chosen?
    static boolean POINTS_CHOSEN = false;
    // HashMap for player traversed edges
    HashMap<String, Edge> playerPath = new HashMap<String, Edge>();
    // Starting location
    static Posn START_POSITION = new Posn(0, 0);
    // Ending location
    static Posn END_POSITION = new Posn(0, 0);
    // ID of target node
    static String TARGETID = ((Integer)(WIDTH - 1)).toString() + "," + 
            ((Integer)(HEIGHT - 1)).toString();
    // All the nodes on the board
    ArrayList<ArrayList<Node>> nodes;
    // Worklist of all possible edges
    ArrayList<Edge> worklist;
    // Edges used in the maze 
    ArrayList<Edge> usedEdges;
    // HashMap used to protect against cycles when forming maze
    HashMap<String, String> hashMap;
    // Is the user playing themselves?
    boolean player;
    // Node at the front of the search or path
    Node head;
    // Has the game ended?
    boolean gameOver;
    // Point picker node 
    PointPicker pointPicker;
    // Have the colors been inverted?
    boolean neon;
    // Are the player and search racing?
    boolean raceMode;


    MazeGame() {
        this.nodes = new ArrayList<ArrayList<Node>>();
        this.worklist = new ArrayList<Edge>();
        this.usedEdges = new ArrayList<Edge>();
        this.hashMap = new HashMap<String, String>();
        this.player = false;
        this.head = null;
        this.gameOver = false;
        this.pointPicker = new PointPicker();
        this.neon = false;
        this.raceMode = false;
    }

    // Draws World
    public WorldImage makeImage() {
        // If not initialized, do nothing.
        if (!INITIALIZED) {
            return new RectangleImage(new Posn(0, 0), 10, 10, Color.WHITE);
        }
        // Utility for Arrays
        ArrayUtil arrayUtil = new ArrayUtil();
        // Images for base Nodes
        WorldImage nodeWorld = arrayUtil.makeImage(nodes, player, gameOver, neon, raceMode);
        nodeWorld.movePinhole((CELLSIZE * 6) / 10, (CELLSIZE * 6) / 10);
        // Picker point represented as a Node
        Node pickerNode = new Node(this.pointPicker.x, this.pointPicker.y);
        // Sets color of Picker Node
        pickerNode.wasSeen = true;
        // Draws the start point
        WorldImage start = new RectangleImage(new Posn(START_POSITION.x * CELLSIZE, 
                START_POSITION.y * CELLSIZE),
                CELLSIZE - 1, CELLSIZE - 1, Color.GREEN);
        start.movePinhole((CELLSIZE * 6) / 10, (CELLSIZE * 6) / 10);
        // Draws the finish point
        WorldImage finish = new RectangleImage(new Posn(END_POSITION.x * CELLSIZE, 
                END_POSITION.y * CELLSIZE), CELLSIZE - 1, CELLSIZE - 1, Color.RED);
        finish.movePinhole((CELLSIZE * 6) / 10, (CELLSIZE * 6) / 10);
        // If the points haven't been chosen.
        if (!POINTS_CHOSEN) {
            if (START_CHOSEN) {
                pickerNode.wasSeen = false;
                pickerNode.searchHead = true;
                // Draws the picker Node
                WorldImage picker = pickerNode.draw(player, gameOver, neon, raceMode);
                picker.movePinhole((CELLSIZE * 6) / 10, (CELLSIZE * 6) / 10);
                return nodeWorld.overlayImages(picker).overlayImages(start);
            }
            // If the end point needs to be picked
            else {
                // If neither the start or finish have been picked
                WorldImage picker = pickerNode.draw(player, gameOver, neon, raceMode);
                picker.movePinhole((CELLSIZE * 6) / 10, (CELLSIZE * 6) / 10);
                return nodeWorld.overlayImages(picker);
            }
        }
        // Image for the gameOver
        else if (gameOver) {
            // String for if the game solved itself
            String endText = "The maze was solved.";
            if (player) {
                // If the player solved it
                endText = "A-MAZE-ING JOB!";
            }
            WorldImage gameOverText = new TextImage(new Posn(CELLSIZE * HEIGHT / 2,
                    CELLSIZE * WIDTH / 2), endText, 20, 4, Color.red);
            
            WorldImage gameOverOutline = new RectangleImage(new Posn(CELLSIZE * HEIGHT / 2,
                    CELLSIZE * WIDTH / 2), 250, 50, Color.BLACK);
            
            WorldImage gameOver = new OverlayImages(gameOverOutline, gameOverText);
                        
            return nodeWorld.overlayImages(finish).overlayImages(start).overlayImages(gameOver);
        }
        else {
            return nodeWorld.overlayImages(finish).overlayImages(start);
        }

    }

    // Handles Key entries
    //SIDE EFFECT: Modifies various game fields
    public void onKeyEvent(String ke) {
        // Restarts the game
        // EFFECT: Resets all boolean values
        if (ke.equals("r")) {
            INITIALIZED = false;
            DEPTH_SEARCHING = false;
            this.player = false;
            BREADTH_SEARCHING = false;
            this.gameOver = false;
        }
        // Quits the game
        else if (ke.equals("x") && !POINTS_CHOSEN) {
            System.exit(0);
        }
        // Makes the game render step by step
        // SIDE EFFECT: Changes stepbystep field
        else if (ke.equals("s")) { 
            STEPBYSTEP = true;
        }
        // Makes the game not render step by step
        // SIDE EFFECT: Changes stepbystep field
        else if (ke.equals("i")) {
            STEPBYSTEP = false;
        }
        // Makes the game's colors invert
        // SIDE EFFECT: Changes game's neon field
        else if (ke.equals("n")) {
            
            neon = !neon;
        }
        // Gives the player control of the game
        // SIDE EFFECT: Changes the player-based field
        else if (ke.equals("p")) {
            this.head = nodes.get(START_POSITION.y).get(START_POSITION.x);
            this.player = true;
        }
        // Runs depth-first search on the maze
        // SIDE EFFECT: Changes Depth-search-based fields
        else if (ke.equals("d") && !BREADTH_SEARCHING && !DEPTH_SEARCHING) {
            // FIX BOOLEAN FLAGS ON RESET
            toDo.clear();
            cameFromEdge.clear();
            toDo.add(nodes.get(START_POSITION.y).get(START_POSITION.x));
            DEPTH_SEARCHING = true;
        }
        // Runs breadth-first search on the maze
        // SIDE EFFECT: Changes Breadth-search-based fields
        else if (ke.equals("b") && !DEPTH_SEARCHING && !BREADTH_SEARCHING) {
            // FIX BOOLEAN FLAGS ON RESET
            toDo.clear();
            cameFromEdge.clear();
            toDo.add(nodes.get(START_POSITION.y).get(START_POSITION.x));
            BREADTH_SEARCHING = true;
        }
        // Makes sure the start- and end- points are chosen
        else if (!POINTS_CHOSEN) {
            this.pointPicker.keyPress(ke);
        }
        // Initializes Race Mode
        else if (ke.equals("a")) {
            raceMode = true;
        }
        // Goes all the way back to the point picker phase
        // EFFECT: Resets all boolean values including Points Chosen
        else if (ke.equals("x")) {
            INITIALIZED = false;
            STEPBYSTEP = false;
            DEPTH_SEARCHING = false;
            BREADTH_SEARCHING = false;
            START_CHOSEN = false;
            END_CHOSEN = false;
            POINTS_CHOSEN = false;
            this.player = false;
            this.gameOver = false;
        }
        // Changes player movement based on key presses
        else if (player) {
            // Sets the newest node as seen
            head.wasSeen = true;
            // Moves Player left
            // EFFECT: Modifies the head player-based fields
            if (ke.equals("left")) {
                if (!head.left.nodeID().equals(head.nodeID()) && 
                        !playerPath.containsKey(head.left.nodeID())) {
                    Edge e = new Edge(head, "left", head.left);
                    playerPath.put(head.left.nodeID(), e);
                }

                head.searchNode = false;
                head.searchHead = false;
                head = head.left;
                head.searchHead = true;
            }
            // Moves Player right
            // EFFECT: Modifies the head player-based fields
            else if (ke.equals("right")) {
                if (!head.right.nodeID().equals(head.nodeID()) && 
                        !playerPath.containsKey(head.right.nodeID())) {
                    Edge e = new Edge(head, "right", head.right);
                    playerPath.put(head.right.nodeID(), e);
                }
                head.searchNode = false;
                head.searchHead = false;
                head = head.right;
                head.searchHead = true;
            }
            // Moves Player up
            // EFFECT: Modifies the head player-based fields
            else if (ke.equals("up")) {
                if (!head.top.nodeID().equals(head.nodeID()) && 
                        !playerPath.containsKey(head.top.nodeID())) {
                    Edge e = new Edge(head, "top", head.top);
                    playerPath.put(head.top.nodeID(), e);
                }
                head.searchNode = false;
                head.searchHead = false;
                head = head.top;
                head.searchHead = true;
            }
            // Moves Player down
            // EFFECT: Modifies the head player-based fields
            else if (ke.equals("down")) {
                if (!head.bottom.nodeID().equals(head.nodeID()) && 
                        !playerPath.containsKey(head.bottom.nodeID())) {
                    Edge e = new Edge(head, "bottom", head.bottom);
                    playerPath.put(head.bottom.nodeID(), e);
                }
                head.searchNode = false;
                head.searchHead = false;
                head = head.bottom;
                head.searchHead = true;
            }
        }
    }

    // Represents what the world does on each tick
    public void onTick() {
        
        // Epilepsy Mode:
        // neon = !neon;
        
        // Checks if game has started yet
        // EFFECT: Initializes Game
        if (!INITIALIZED) {
            this.generateNodes();
            this.generateWorklist();
            // Initialize hashMap.
            this.generateHashMap();
            INITIALIZED = true;
        }
        // Makes sure game points are chosen before running    
        else if (!POINTS_CHOSEN) {
            // CAN NOT RUN YET
        }
        // Runs the game, without stepping through maze construction
        else if (worklist.size() > 0 && !STEPBYSTEP) {
            while (worklist.size() > 0) {
                this.kruskalAlgo();
                this.connectNodes();
            }
        }
        // Creates the maze by individually knocking down walls
        // EFFECT: Initializes Maze fields
        else if (worklist.size() > 0 && STEPBYSTEP) {
            this.kruskalAlgo();
            this.connectNodes();
            if (worklist.size() == 1) {
                STEPBYSTEP = false;
            }
        }
        // Begins depth-first search
        else if (DEPTH_SEARCHING) {
            this.search();
        }
        // Begins breath-first search
        else if (BREADTH_SEARCHING) {
            this.search();
        }
        // Checks if player has reached Maze's end point
        else if (this.player && (Integer.toString(head.x) + "," + 
        Integer.toString(head.y)).equals(TARGETID)) {
            gameOver = true;
            HashMapUtil<String, Edge> hashMapUtil = new HashMapUtil<String, Edge>();
            hashMapUtil.reconstruct(playerPath, TARGETID);
        }
    }


    // SIDE EFFECT: 
    // Creates and updates board of nodes
    // Generates the board of Nodes
    void generateNodes() {
        ArrayList<ArrayList<Node>> nodes = new ArrayList<ArrayList<Node>>(); 

        for (int y = 0 ; y < HEIGHT ; y += 1) {
            ArrayList<Node> row = new ArrayList<Node>();
            for (int x = 0 ; x < WIDTH ; x += 1) {
                row.add(new Node(x, y));
            }
            nodes.add(row);
        }
        this.nodes = nodes;
    }

    // SIDE EFFECT:
    // Creates and updates worklist
    // Creates the sorted ArrayList of edges
    // Suppressed Warning just meant to pass Web-cat
    @SuppressWarnings("unchecked")
    void generateWorklist() {
        ArrayList<Edge> edgeList = new ArrayList<Edge>();
        for (int y = 0 ; y < HEIGHT ; y += 1) {
            for (int x = 0 ; x < WIDTH ; x += 1) {
                // Bottom corner has no edges from itself
                if (x == WIDTH - 1 && y == HEIGHT - 1) {
                    // No edges in bottom-right hand corner
                }
                // If on far right
                else if (x == WIDTH - 1) {
                    Edge bottom = new Edge(nodes.get(y).get(x), "bottom", 
                            nodes.get(y + 1).get(x));
                    edgeList.add(bottom);
                }
                // If on bottom row
                else if (y == HEIGHT - 1) {
                    Edge right = new Edge(nodes.get(y).get(x), "right", nodes.get(y).get(x + 1));
                    edgeList.add(right);
                }
                // For all other cases
                else {
                    Edge right = new Edge(nodes.get(y).get(x), "right", nodes.get(y).get(x + 1));
                    Edge bottom = new Edge(nodes.get(y).get(x), "bottom", 
                            nodes.get(y + 1).get(x));
                    edgeList.add(right);
                    edgeList.add(bottom);
                }
            }
        }
        // Sorts the edgelist
        Collections.sort(edgeList);
        this.worklist = edgeList;
    }

    // Generates HashMap
    void generateHashMap() {
        for (int y = 0 ; y < MazeGame.HEIGHT  ; y += 1) {
            for (int x = 0 ; x < MazeGame.WIDTH ; x += 1) {
                Node n = this.nodes.get(y).get(x);
                String id = n.nodeID();
                this.hashMap.put(id, id);
            }
        }
    }
    
    // SIDE EFFECT:
    // Empties World's work list and updates usedEdges
    // Kruskal's Algorithm
    void kruskalAlgo() {
        ArrayList<Edge> edgesInTree = new ArrayList<Edge>();
        HashMapUtil<String, String> hashMapUtil = new HashMapUtil<String, String>();
        int count = 1;
        // Speeds up Step-by-step by doing 4 steps at once
        if (STEPBYSTEP) {
            count = 4;
        }
        for (int x = 0; x < count ; x += 1) {
            if (worklist.size() > 0) {
                Edge nextEdge = worklist.get(0);
                String from = nextEdge.from.nodeID();
                String to = nextEdge.to.nodeID();

                String fromRoot = hashMapUtil.findRoot(hashMap, from);
                String toRoot = hashMapUtil.findRoot(hashMap, to);

                if (fromRoot.equals(toRoot)) {
                    worklist.remove(0);
                }
                else {
                    edgesInTree.add(nextEdge);
                    hashMap.put(fromRoot, to);
                    worklist.remove(0);
                }

            }
            // EFFECT: Sets the usedEdges field with Kruskal's algorithm
            this.usedEdges = edgesInTree;
        }
    }

    // SIDE EFFECT: 
    // Connects Nodes based on usedEdges
    // Fixes node connections 
    void connectNodes() {
        for (Edge e : usedEdges) {
            if (e.direction.equals("right")) {
                e.from.right = e.to;
                e.to.left = e.from;
            }
            else {
                e.from.bottom = e.to;
                e.to.top = e.from;
            }
        }
    }

    // To Do list for Searches
    ArrayList<Node> toDo = new ArrayList<Node>();

    // cameFromEdge list for searches
    HashMap<String, Edge> cameFromEdge = new HashMap<String, Edge>();

    // Preforms depth or breadth search
    void search() {
        HashMapUtil<String, Edge> hashMapUtil = new HashMapUtil<String, Edge>();
        // Ensures that there are more nodes to be checked
        if (!toDo.isEmpty()) {
            Node next = toDo.get(0);
            next.searchHead = false;
            next.wasSeen = true;
            String nextID = next.nodeID();
            toDo.remove(0);
            int addPlace = 0;
            
            if (BREADTH_SEARCHING)  {
                addPlace = toDo.size();
            }
            
            // If the current node is equal to the target ID
            if (nextID.equals(TARGETID)) {
                hashMapUtil.reconstruct(cameFromEdge, nextID);
                DEPTH_SEARCHING = false;
                BREADTH_SEARCHING = false;
                this.gameOver = true;
            }
            // Runs if there are no more nodes to search through
            else {
                if (!next.top.nodeID().equals(this) && 
                        !cameFromEdge.containsKey(next.top.nodeID())) {
                    toDo.add(addPlace, next.top);
                    Edge e = new Edge(next, "top", next.top);
                    cameFromEdge.put(next.top.nodeID(), e);
                    if (BREADTH_SEARCHING) {
                        next.top.searchHead = true;
                        next.top.searchNode = true;
                    }

                }
                if (!next.left.nodeID().equals(this) && 
                        !cameFromEdge.containsKey(next.left.nodeID())) {
                    toDo.add(addPlace, next.left);
                    Edge e = new Edge(next, "left", next.left);
                    cameFromEdge.put(next.left.nodeID(), e);
                    if (BREADTH_SEARCHING) {
                        next.left.searchHead = true;
                        next.left.searchNode = true;
                    }

                }
                if (!next.bottom.nodeID().equals(this) && 
                        !cameFromEdge.containsKey(next.bottom.nodeID())) {
                    toDo.add(addPlace, next.bottom);
                    Edge e = new Edge(next, "bottom", next.bottom);
                    cameFromEdge.put(next.bottom.nodeID(), e);
                    if (BREADTH_SEARCHING) {
                        next.bottom.searchHead = true;
                        next.bottom.searchNode = true;
                    }
                }
                if (!next.right.nodeID().equals(this) && 
                        !cameFromEdge.containsKey(next.right.nodeID())) {
                    toDo.add(addPlace, next.right);
                    Edge e = new Edge(next, "right", next.right);
                    cameFromEdge.put(next.right.nodeID(), e);
                    if (BREADTH_SEARCHING) {
                        next.right.searchHead = true;
                        next.right.searchNode = true;
                    }
                }
                // EFFECT: Sets node fields
                next.searchNode = true;
                toDo.get(0).searchHead = true;
            }
        }
    }
}


// Class for examples
// SOME TESTS MEANT SPECIFICALLY FOR 30 x 30 MAZE
class ExamplesMaze {
    
    MazeGame mg = new MazeGame();
    HashMapUtil<String, String> hashMapUtil = new HashMapUtil<String, String>();
    Node node1 = new Node(1, 1);
    Node node2 = new Node(0, 1);
    Node node3 = new Node(17, 4);
    Node node4 = new Node(17, 5);
    Node node5 = new Node(270, 40);
    Node node6 = new Node(271, 40);
    Node node7 = new Node(1, 74);
    Edge edge1 = new Edge(node1, "left", node2);
    Edge edge2 = new Edge(node3, "up", node4);
    Edge edge3 = new Edge(node5, "right", node6);
    Edge edge4 = new Edge(node6, "left", node5);
    PointPicker pointpicker = new PointPicker();
    PointPicker pointpicker2 = new PointPicker();

    
    
    // Initializes the test worlds
    void init() {
        mg.generateNodes();
        mg.generateWorklist();
        mg.generateHashMap();
        while (mg.worklist.size() > 0) {
            mg.kruskalAlgo();
            mg.connectNodes();
        }
        mg.generateWorklist();
        edge1.weight = 2;
        edge2.weight = 9;
        edge3.weight = 5;
        edge4.weight = 9;
        
    }

/*    // Tests Generate Nodes
    void testGenerateNodes(Tester t) {
        this.init();
        t.checkExpect(mg.nodes.get(1).size(), 30);
        t.checkExpect(mg.nodes.get(10).size(), 30);

        //t.checkExpect(mg.nodes.get(1).get(1), new Node(1, 1));
    }

    // Tests Generate Worklist
    void testGenerateWorklist(Tester t) {
        this.init();
        t.checkExpect(mg.worklist.size(), 1740);
        t.checkExpect(mg.worklist.get(0).weight, 0);
        t.checkExpect(mg.worklist.get(10).weight == 0, false);
        t.checkExpect(mg.worklist.get(20).weight > 0, true);

    }

    // Tests Kruskals Algo
    void testKruskal(Tester t) {
        this.init();
        t.checkExpect(mg.usedEdges.size(), 0);
    }

    // Tests HashMap
    void testHashMap(Tester t) {
        this.init();
        t.checkExpect(hashMapUtil.findRoot(mg.hashMap, "2,9"),
                hashMapUtil.findRoot(mg.hashMap, "1,0"));
        t.checkExpect(hashMapUtil.findRoot(mg.hashMap, "3,9"),
                hashMapUtil.findRoot(mg.hashMap, "2,0"));
        t.checkExpect(hashMapUtil.findRoot(mg.hashMap, "4,9"),
                hashMapUtil.findRoot(mg.hashMap, "3,0"));
        t.checkExpect(hashMapUtil.findRoot(mg.hashMap, "5,9"),
                hashMapUtil.findRoot(mg.hashMap, "4,0"));

    }
    
    // Tests edge's compareTo method
    void testCompareTo(Tester t) {
        
        init();
        
        t.checkExpect(edge1.compareTo(edge2), -7);
        t.checkExpect(edge2.compareTo(edge3), 4);
        t.checkExpect(edge3.compareTo(edge1), 3);
        t.checkExpect(edge1.compareTo(edge3), -3);
        t.checkExpect(edge4.compareTo(edge2), 0);
        t.checkExpect(edge4.compareTo(edge4), 0);

        
    }
    
    // Tests the Node ID Method
    void testNodeID(Tester t) {
        
        t.checkExpect(node1.nodeID(), "1,1");
        t.checkExpect(node2.nodeID(), "0,1");
        t.checkExpect(node3.nodeID(), "17,4");
        t.checkExpect(node4.nodeID(), "17,5");
        t.checkExpect(node5.nodeID(), "270,40");
        t.checkExpect(node6.nodeID(), "271,40");
        t.checkExpect(node3.nodeID() == node7.nodeID(), false);

    }
    
    // Tests that a Maze Game correctly initializes
    void testInitialMazeGame(Tester t) {
        
        init();
        
        t.checkExpect(runMaze.gameOver, false);
        t.checkExpect(runMaze.player, false);
        t.checkExpect(runMaze.worklist.size(), 0);
        
    }
    // Tests that the game runs correctly on each tick
    void testTickMazeGame(Tester t) {
        
        init();
        runMaze.onTick();
        
        t.checkExpect(runMaze.gameOver, false);
        t.checkExpect(runMaze.player, false);
        t.checkExpect(runMaze.worklist.size(), 1740);
        t.checkExpect(runMaze.usedEdges.size(), 0);
        t.checkExpect(runMaze.nodes.size(), runMaze.HEIGHT);
        t.checkExpect(runMaze.nodes.get(0).size(), runMaze.WIDTH);
        
    }
    
    // Tests that the point picker responds to keypresses correctly
    void testPointPicker(Tester t) {
        
        pointpicker.keyPress("right");
        
        t.checkExpect(pointpicker.x == 1, true);
        
        pointpicker.keyPress("down");
        
        t.checkExpect(pointpicker.y == 1);
        
        pointpicker2.keyPress("left");
        pointpicker2.keyPress("up");
        
        t.checkExpect(pointpicker2.x == 0, true);
        
        
    }

    // Tests that a node is correctly initializes
    void testNode(Tester t) {
        
        t.checkExpect(node1.wasSeen, false);
        t.checkExpect(node2.searchHead, false);
    }
    */
    
 /*   // Tested the onKey method (ran into issues with the Tester library)
    void testonKey(Tester t) {
        
        init();
        runMaze.onKeyEvent("p");
        
        
        t.checkExpect(runMaze.gameOver, false);
        t.checkExpect(runMaze.player, true);
        
    }
    
    void testonKeyR(Tester t) {
        
        init();
        runMaze.onKeyEvent("p");
        runMaze.gameOver = true;
        runMaze.onKeyEvent("r");
        
        t.checkExpect(runMaze.gameOver, false);
        t.checkExpect(runMaze.player, false);
        t.checkExpect(runMaze.usedEdges.size(), 0);
    }

   */ 
    
    // RUNS GAME
    // WILL MAKE SOME TESTS NOT PROPERlY RUN
    public static void main(String [] args) {
        MazeGame runMaze = new MazeGame();
        runMaze.bigBang(MazeGame.CELLSIZE * MazeGame.WIDTH + MazeGame.CELLSIZE / 2, 
                MazeGame.CELLSIZE * MazeGame.HEIGHT + MazeGame.CELLSIZE / 2, .01);
    }


}
