import java.awt.Color;
import java.util.*;

import tester.*;
import javalib.impworld.*;
import javalib.colors.*;
import javalib.worldimages.*;

// Array Utility
class ArrayUtil {
    // Draws nodes
    WorldImage makeImage(ArrayList<ArrayList<Node>> nodes) {
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
                WorldImage nodeImage = n.draw();
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
    }

    // Draws node
    WorldImage draw() {
        int halfCell = MazeGame.CELLSIZE / 2;
        int screenX = x * MazeGame.CELLSIZE;
        int screenY = y * MazeGame.CELLSIZE;
        Posn topLeft = new Posn(screenX - halfCell, screenY - halfCell);
        Posn topRight = new Posn(screenX + halfCell, screenY - halfCell);
        Posn btmLeft = new Posn(screenX - halfCell, screenY + halfCell);
        Posn btmRight = new Posn(screenX + halfCell, screenY + halfCell);

        WorldImage base = new RectangleImage(new Posn(screenX, screenY), MazeGame.CELLSIZE, MazeGame.CELLSIZE, Color.white);
        WorldImage left = new LineImage(topLeft, btmLeft, Color.black);
        WorldImage top = new LineImage(topLeft, topRight, Color.black);
        WorldImage right = new LineImage(topRight, btmRight, Color.black);
        WorldImage bottom = new LineImage(btmLeft, btmRight, Color.black);

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

    Node from;
    String direction;
    Node to;
    int weight;

    Edge(Node from, String direction, Node to) {

        this.from = from;
        this.direction = direction;
        this.to = to;
        this.weight = (int)(Math.random() * 1000);

    }

    public int compareTo(Object comparesto) {
        int compareweight = ((Edge)comparesto).weight;
        return this.weight - compareweight;
    }
}

// Class for the Main Game
class MazeGame extends World {
    static final int WIDTH = 10;
    static final int HEIGHT = 20;
    static final int CELLSIZE = 20;
    static boolean INITIALIZED = false;
    static boolean STEPBYSTEP = false;
    static String TARGETID = "9,19"; /*((Integer)(WIDTH - 1)).toString() + "," + 
                             ((Integer)(HEIGHT - 1)).toString();*/
    ArrayList<ArrayList<Node>> nodes;
    ArrayList<Edge> worklist;
    ArrayList<Edge> usedEdges;
    HashMap<String, String> hashMap;

    
    MazeGame() {
        this.nodes = new ArrayList<ArrayList<Node>>();
        this.worklist = new ArrayList<Edge>();
        this.usedEdges = new ArrayList<Edge>();
        this.hashMap = new HashMap<String, String>();
    }

    // Draws World
    public WorldImage makeImage() {
        if (!INITIALIZED) {
            return new RectangleImage(new Posn(0, 0), 10, 10, Color.WHITE);
        }
        else {
            ArrayUtil arrayUtil = new ArrayUtil();
            WorldImage world = arrayUtil.makeImage(nodes);
            world.movePinhole((CELLSIZE * 6) / 10, (CELLSIZE * 6) / 10);
            WorldImage start = new RectangleImage(new Posn(CELLSIZE / 2 + 1, CELLSIZE / 2 + 1),
                    CELLSIZE - 1, CELLSIZE - 1, Color.GREEN);
            WorldImage finish = new RectangleImage(new Posn(CELLSIZE * WIDTH - (CELLSIZE - 2)/2,
                    CELLSIZE * HEIGHT - (CELLSIZE - 2)/2), CELLSIZE - 1, CELLSIZE - 1, Color.RED);
            return world.overlayImages(finish).overlayImages(start);

        }
    }

    // Handles Key entries
    public void onKeyEvent(String ke) {
        if (ke.equals("r")) {
            INITIALIZED = false;
        }
        else if (ke.equals("s")) { 
            STEPBYSTEP = true;
        }
        else if (ke.equals("i")) {
            STEPBYSTEP = false;
        }
    }
    
    // Represents what the world does on each tick
    public void onTick() {
        if (!INITIALIZED) {
            this.generateNodes();
            this.generateWorklist();
            // Initialize hashMap.
            this.generateHashMap();
            INITIALIZED = true;
        }
        else if (worklist.size() > 0 && !STEPBYSTEP) {
            while (worklist.size() > 0) {
                this.kruskalAlgo();
                this.connectNodes();    
            }
        }
        else if (worklist.size() > 0 && STEPBYSTEP) {
            this.kruskalAlgo();
            this.connectNodes();
        }
        else {
            INITIALIZED = true;
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
    void generateWorklist() {
        ArrayList<Edge> edgeList = new ArrayList<Edge>();
        for (int y = 0 ; y < HEIGHT ; y += 1) {
            for (int x = 0 ; x < WIDTH ; x += 1) {
                // Bottom corner has no edges from itself
                if (x == WIDTH - 1 && y == HEIGHT - 1) {

                }
                // If on far right
                else if (x == WIDTH - 1) {
                    Edge bottom = new Edge(nodes.get(y).get(x), "bottom", nodes.get(y + 1).get(x));
                    edgeList.add(bottom);
                }
                // If on bottom row
                else if (y == HEIGHT - 1) {
                    Edge right = new Edge(nodes.get(y).get(x), "right", nodes.get(y).get(x + 1));
                    edgeList.add(right);
                }
                else {
                    Edge right = new Edge(nodes.get(y).get(x), "right", nodes.get(y).get(x + 1));
                    Edge bottom = new Edge(nodes.get(y).get(x), "bottom", nodes.get(y + 1).get(x));
                    edgeList.add(right);
                    edgeList.add(bottom);
                }
            }
        }
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
        this.usedEdges = edgesInTree;
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

    // Depth-first Search
    int depthSearch() {
        HashMap<String, Edge> cameFromEdge = new HashMap<String, Edge>();
        Queue<Node> toDo = new LinkedList<Node>();
        HashMapUtil<String, Edge> hashMapUtil = new HashMapUtil<String, Edge>();
        toDo.add(nodes.get(0).get(0));
        int size = toDo.size();
        String returnString = "I f*cked up somewhere.";
        
        while (!toDo.isEmpty()) {
            Node next = toDo.poll();
            String nextID = next.nodeID();
            if (cameFromEdge.containsKey(nextID)) {

            }
            else if (nextID.equals(TARGETID)) {
                //hashMapUtil.reconstruct(cameFromEdge, next);
                returnString = "I DID IT";
            }
            else {
                if (!next.bottom.equals(this)) {
                    toDo.add(next.bottom);
                    Edge e = new Edge(next, "bottom", next.bottom);
                    cameFromEdge.put(next.bottom.nodeID(), e);
                }
                if (!next.top.equals(this)) {
                    toDo.add(next.top);
                    Edge e = new Edge(next, "top", next.top);
                    cameFromEdge.put(next.top.nodeID(), e);

                }
                if (!next.right.equals(this)) {
                    toDo.add(next.right);
                    Edge e = new Edge(next, "right", next.right);
                    cameFromEdge.put(next.right.nodeID(), e);
                }
                if (!next.left.equals(this)) {
                    toDo.add(next.left);
                    Edge e = new Edge(next, "left", next.left);
                    cameFromEdge.put(next.left.nodeID(), e);
                }
            }
        }
        return size;
    }
}


// Class for examples
class ExamplesMaze {
    MazeGame mg = new MazeGame();
    MazeGame runMaze = new MazeGame();
    HashMapUtil<String, String> hashMapUtil = new HashMapUtil<String, String>();

    void init() {
        mg.generateNodes();
        mg.generateWorklist();
        mg.generateHashMap();
        while (mg.worklist.size() > 0) {
            mg.kruskalAlgo();
            mg.connectNodes();
        }
        mg.generateWorklist();
    }

    // Tests Generate Nodes
    void testGenerateNodes(Tester t) {
        this.init();
        t.checkExpect(mg.nodes.get(1).size(), 20);
        //t.checkExpect(mg.nodes.get(1).get(1), new Node(1, 1));
    }

    // Tests Generate Worklist
    void testGenerateWorklist(Tester t) {
        this.init();
        t.checkExpect(mg.worklist.size(), (19 * 19 * 2) + 38);
        t.checkExpect(mg.worklist.get(0).weight, 0);
    }

    // Tests Kruskals Algo
    void testKruskal(Tester t) {
        this.init();
        t.checkExpect(mg.usedEdges.size(), 10);
    }

    // Tests HashMap
    void testHashMap(Tester t) {
        this.init();
        t.checkExpect(mg.hashMap.get("0,0"), "0011");
        t.checkExpect(mg.hashMap.get("2,9"), "0");
        t.checkExpect(hashMapUtil.findRoot(mg.hashMap, "2,9"), hashMapUtil.findRoot(mg.hashMap, "1,0"));
        t.checkExpect(hashMapUtil.findRoot(mg.hashMap, "3,9"), hashMapUtil.findRoot(mg.hashMap, "2,0"));
        t.checkExpect(hashMapUtil.findRoot(mg.hashMap, "4,9"), hashMapUtil.findRoot(mg.hashMap, "3,0"));
        t.checkExpect(hashMapUtil.findRoot(mg.hashMap, "5,9"), hashMapUtil.findRoot(mg.hashMap, "4,0"));

    }
    
    // Tests Depth Search
    void testDepth(Tester t) {
        this.init();
        t.checkExpect(mg.depthSearch(), "Yay");
    }


/*    {
        this.runMaze.bigBang(MazeGame.CELLSIZE * MazeGame.WIDTH + MazeGame.CELLSIZE / 2, 
                MazeGame.CELLSIZE * MazeGame.HEIGHT + MazeGame.CELLSIZE / 2, .001);
    }*/
}