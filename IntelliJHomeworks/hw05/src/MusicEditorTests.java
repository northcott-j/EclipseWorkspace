import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class for the Music Editor
 * Created by Jonathan on 11/2/2015.
 */
public class MusicEditorTests {
  MusicEditorImpl e1 = new MusicEditorImpl();
  Note n1 = e1.makeNote(NoteTypes.C, 3, 0, 16);
  Note n2 = e1.makeNote(NoteTypes.CSharp, 3, 0, 4);
  Note n3 = e1.makeNote(NoteTypes.C, 3, 17, 19);

  @Test
  public void greatTest() {
    e1.addNote(n1);
    e1.addNote(n2);
    e1.addNote(n3);
    assertEquals("DefaultPlayer",
            e1.toString());
  }
}
