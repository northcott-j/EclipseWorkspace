import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class for the Music Editor Created by Jonathan on 11/2/2015.
 */
public class MusicEditorTests {
  // makeNote() Tests
  @Test
  public void validNote() {
    MusicEditorImpl e1 = new MusicEditorImpl();
    Note nOne = e1.makeNote(NoteTypes.C, 3, 0, 16);
    assertEquals(NoteTypes.C, nOne.getType());
    assertEquals(3, nOne.getOctave());
    assertEquals(0, nOne.getStartBeat());
    assertEquals(16, nOne.getEndBeat());
  }

  @Test(expected = IllegalArgumentException.class)
  public void improperNote() {
    MusicEditorImpl e1 = new MusicEditorImpl();
    Note nTwo = e1.makeNote(NoteTypes.C, 3, 16, 15);
  }

  @Test
  public void oneBeatNote() {
    MusicEditorImpl e1 = new MusicEditorImpl();
    Note nTwo = e1.makeNote(NoteTypes.C, 3, 16, 16);
  }

  // changeNoteStart() Tests
  @Test
  public void noteStartLonger() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteStart(note, 1);
    assertEquals(1, note.getStartBeat());
    assertEquals(editor.getNote(NoteTypes.CSharp, 3, 1), note);
  }

  @Test
  public void noteStartShorter() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteStart(note, 3);
    assertEquals(3, note.getStartBeat());
    assertEquals(editor.getNote(NoteTypes.CSharp, 3, 3), note);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteStartChangeRemovesNote() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteStart(note, 3);
    editor.getNote(NoteTypes.CSharp, 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteStartOverEnd() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteStart(note, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteStartOverlaps() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 3, 4);
    Note note2 = editor.makeNote(NoteTypes.CSharp, 3, 1, 2);
    editor.addNote(note);
    editor.addNote(note2);
    editor.changeNoteStart(note, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteStartNegative() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteStart(note, -3);
  }

  // changeNoteEnd() Tests
  @Test
  public void noteEndLonger() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteEnd(note, 5);
    assertEquals(5, note.getEndBeat());
    assertEquals(editor.getNote(NoteTypes.CSharp, 3, 5), note);
  }

  @Test
  public void noteEndTrimTest() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 0, 99);
    editor.addNote(note);
    assertEquals(100, editor.scoreLength());
    editor.changeNoteEnd(note, 5);
    assertEquals(6, editor.scoreLength());
  }

  @Test
  public void noteEndShorter() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteEnd(note, 3);
    assertEquals(3, note.getEndBeat());
    assertEquals(editor.getNote(NoteTypes.CSharp, 3, 3), note);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteEndChangeRemovesNote() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteEnd(note, 3);
    editor.getNote(NoteTypes.CSharp, 2, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteEndOverStart() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteEnd(note, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteEndOverlaps() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 3, 4);
    Note note2 = editor.makeNote(NoteTypes.CSharp, 3, 1, 2);
    editor.addNote(note);
    editor.addNote(note2);
    editor.changeNoteEnd(note2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteEndNegative() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteEnd(note, -3);
  }

  // changeNoteOctave() Tests
  @Test
  public void noteOctaveChange() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteOctave(note, 2);
    assertEquals(2, note.getOctave());
    assertEquals(editor.getNote(NoteTypes.CSharp, 2, 3), note);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteOctaveChangeRemoves() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteOctave(note, 2);
    editor.getNote(NoteTypes.CSharp, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteOctaveNegative() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteOctave(note, -3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteOctaveOverlap() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    Note note2 = editor.makeNote(NoteTypes.CSharp, 4, 2, 4);
    editor.addNote(note);
    editor.addNote(note2);
    editor.changeNoteOctave(note, 4);
  }

  // changeNoteType() Tests

  @Test
  public void noteTypeChange() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteType(note, NoteTypes.A);
    assertEquals(editor.getNote(NoteTypes.A, 3, 3), note);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteTypeChangeRemoves() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteType(note, NoteTypes.A);
    editor.getNote(NoteTypes.CSharp, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteTypeOverlaps() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    Note note2 = editor.makeNote(NoteTypes.A, 3, 2, 4);
    editor.addNote(note);
    editor.addNote(note2);
    editor.changeNoteType(note, NoteTypes.A);
  }

  // changeNoteVolume() Tests
  @Test
  public void noteVolumeChange() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteVol(note, 500);
    assertEquals(500, editor.getNote(NoteTypes.CSharp, 3, 3).getVolume());
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteVolumeNegative() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.changeNoteVol(note, -5);
  }

  // getNote() Tests
  @Test
  public void getNoteTest() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    assertEquals(note, editor.getNote(NoteTypes.CSharp, 3, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNoteFail() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    editor.getNote(NoteTypes.CSharp, 2, 3);
  }

  // addNote() Tests
  @Test
  public void addNote() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4);
    editor.addNote(note);
    assertEquals(note, editor.getNote(NoteTypes.CSharp, 3, 2));
    assertEquals(note, editor.getNote(NoteTypes.CSharp, 3, 3));
    assertEquals(note, editor.getNote(NoteTypes.CSharp, 3, 4));
  }

  @Test
  public void addLongNoteIncreasesLength() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10);
    Note note2 = editor.makeNote(NoteTypes.CSharp, 3, 11, 100);
    editor.addNote(note);
    assertEquals(11, editor.scoreLength());
    editor.addNote(note2);
    assertEquals(101, editor.scoreLength());
  }

  @Test(expected = IllegalArgumentException.class)
  public void addNoteOverlap() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10);
    Note note2 = editor.makeNote(NoteTypes.CSharp, 3, 3, 100);
    editor.addNote(note);
    editor.addNote(note2);
  }

  // scoreLength() Tests
  @Test
  public void scoreLengthTest1() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10);
    editor.addNote(note);
    assertEquals(11, editor.scoreLength());
  }

  @Test
  public void scoreLengthTest2() {
    MusicEditorImpl editor = new MusicEditorImpl();
    assertEquals(0, editor.scoreLength());
  }

  // getCurBeat() Tests
  @Test
  public void getCurBeatTest1() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10);
    editor.addNote(note);
    assertEquals(0, editor.getCurBeat());
  }

  @Test
  public void getCurBeatTest2() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10);
    editor.addNote(note);
    editor.playMusic();
    assertEquals(1, editor.getCurBeat());
  }

  // setTempo() Tests
  @Test
  public void setTempo() {
    MusicEditorImpl editor = new MusicEditorImpl();
    editor.setTempo(10);
    assertEquals(10, editor.getTempo());
  }

  @Test(expected = IllegalArgumentException.class)
  public void setTempoFail() {
    MusicEditorImpl editor = new MusicEditorImpl();
    editor.setTempo(-5);
  }

  // returnScore() Tests
  @Test
  public void returnScoreTest() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note n = editor.makeNote(NoteTypes.E, 2, 1, 1);
    ArrayList<ArrayList<Note>> tester = new ArrayList<>();
    ArrayList<Note> emptyBeatLine = new ArrayList<>();
    ArrayList<Note> beatLine = new ArrayList<>();
    beatLine.add(n);
    tester.add(emptyBeatLine);
    tester.add(beatLine);
    editor.addNote(n);
    assertEquals(tester, editor.returnScore());
  }

  // changeCurBeat() Tests
  @Test
  public void changeCurBeatTest() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note n = editor.makeNote(NoteTypes.E, 2, 1, 5);
    editor.addNote(n);
    editor.changeCurBeat(3);
    assertEquals(3, editor.getCurBeat());
  }

  @Test(expected = IllegalArgumentException.class)
  public void changeCurBeatNegative() {
    MusicEditorImpl editor = new MusicEditorImpl();
    editor.changeCurBeat(-3);
  }

  @Test(expected = IllegalStateException.class)
  public void changeCurBeatOutOfBounds() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note n = editor.makeNote(NoteTypes.E, 2, 1, 5);
    editor.addNote(n);
    editor.changeCurBeat(7);
  }

  // deleteNote() Tests
  @Test(expected = IllegalArgumentException.class)
  public void deleteNoteTest() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note n = editor.makeNote(NoteTypes.E, 2, 1, 5);
    editor.addNote(n);
    editor.deleteNote(n);
    editor.getNote(NoteTypes.E, 2, 4);
  }

  // playMusic() Tests
  @Test
  public void playMusic() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note n = editor.makeNote(NoteTypes.E, 2, 0, 5);
    ArrayList<Note> expected = new ArrayList<>();
    expected.add(n);
    editor.addNote(n);
    assertEquals(expected, editor.playMusic());
    assertEquals(1, editor.getCurBeat());
  }

  @Test(expected = IllegalStateException.class)
  public void playMusicFail() {
    MusicEditorImpl editor = new MusicEditorImpl();
    Note n = editor.makeNote(NoteTypes.E, 2, 1, 5);
    editor.changeCurBeat(5);
    editor.playMusic();
    editor.playMusic();
  }

  // consecutiveScore() Tests
  @Test
  public void consecutiveScoreTest() {
    MusicEditorImpl editor = new MusicEditorImpl();
    MusicEditorImpl editor2 = new MusicEditorImpl();
    MusicEditorImpl editor3 = new MusicEditorImpl();
    Note n = editor.makeNote(NoteTypes.E, 2, 1, 5);
    Note n2 = editor.makeNote(NoteTypes.C, 2, 0, 5);
    Note n3 = editor.makeNote(NoteTypes.C, 2, 6, 11);
    editor.addNote(n);
    editor2.addNote(n2);
    editor3.addNote(n);
    editor3.addNote(n3);
    editor.consecutiveScore(editor2.returnScore());
    assertEquals(editor3.debugOutput(), editor.debugOutput());
  }
}
