import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Jonathan on 9/30/2015.
 */
public class SequenceTest extends TestCase {

    Sequence seq1 = new Sequence();
    Sequence seq2 = new Sequence();
    Sequence seq3 = new Sequence();
    Sequence seq4 = new Sequence();

    void seqInitiator() {
        seq1.order.add("Yahoo");
        seq1.order.add("Apple");
        seq1.order.add("Google");
        seq1.order.add("Samsung");
        seq1.order.add("Samsung");
        seq1.order.add("HTC");

        seq2.order.add("Yahoo");
        seq2.order.add("Apple");
        seq2.order.add("Google");
        seq2.order.add("Samsung");

        seq3.order.add("Yahoo");
        seq3.order.add("Apple");
        seq3.order.add("Google");
        seq3.order.add("Sony");

        seq4.order.add("Yahoo");
        seq4.order.add("Google");
        seq4.order.add("Samsung");
        seq4.order.add("HTC");
    }

    @Test
    // In order subseq
    public void testIsSubseq1() {
        seqInitiator();
        assertTrue(seq1.isSubseq(seq2));
    }

    @Test
    // Not a subseq
    public void testIsSubseq2() {
        seqInitiator();
        assertFalse(seq1.isSubseq(seq3));
    }

    @Test
    // Skipped subseq
    public void testIsSubseq3() {
        seqInitiator();
        assertTrue(seq1.isSubseq(seq4));
    }

    @Test
    // seq is subseq of itself
    public void testIsSubseq4() {
        seqInitiator();
        assertTrue(seq1.isSubseq(seq1));
    }
}