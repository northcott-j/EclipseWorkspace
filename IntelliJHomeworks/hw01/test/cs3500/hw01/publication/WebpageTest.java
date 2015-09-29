package cs3500.hw01.publication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WebpageTest {
    Publication ood = new Webpage("CS3500: Object-Oriented Design",
                                  "http://www.ccs.neu.edu/course/cs3500/",
                                  "August 11, 2014");

    @Test
    public void testCiteApa() {
        assertEquals("CS3500: Object-Oriented Design. Retrieved August 11, 2014, "
                        + "from http://www.ccs.neu.edu/course/cs3500/.",
                ood.citeApa());
    }

    @Test
    public void testCiteMla() {
        assertEquals("\"CS3500: Object-Oriented Design.\" Web. August 11, 2014 "
                        + "<http://www.ccs.neu.edu/course/cs3500/>.",
                ood.citeMla());
    }
}
