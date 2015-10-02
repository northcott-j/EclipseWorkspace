import java.util.ArrayList;

/**
 *
 * Represents a sequence of stock purchases
 *
 * DOES NOT PROTECT AGAINST MUTATION LIKE PREVIOUSLY THOUGHT
 * */

class Sequence {
    // Each purchase is represented by a String
    ArrayList<String> order;

    public Sequence() {
        this.order = new ArrayList<>();
    }

    Boolean isSubseq(Sequence subseq) {
        // Marks the index after which the next item must be found
        int fencepost;
        // Creates an instance to be mutated without affecting the field
        ArrayList<String> workingSet = this.order;
        for (int i = 0 ; i < subseq.order.size() ; i += 1) {
            if (workingSet.indexOf(subseq.order.get(i)) == -1) {
                // means an element in subseq isn't in the main list
                return false;
            }
            else {
                // Sets fencepost to found occurrence of i
                fencepost = workingSet.indexOf(subseq.order.get(0));
                // Removes irrelevant strings up to fencepost
                workingSet.subList(0, fencepost + 1).clear();
            }
        }
        // If for loop exits without returning false, subseq is a subseq of order
        return true;
    }
}
