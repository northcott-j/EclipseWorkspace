package cs3500.test;

import cs3500.music.util.AbstractNote;
import cs3500.music.util.MusicEditorImpl;
import cs3500.music.util.NoteTypes;

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
    MusicEditorImpl e1 = MusicEditorImpl.makeEditor();
    AbstractNote nOne = e1.makeNote(NoteTypes.C, 3, 0, 16, 0, 0);
    assertEquals(NoteTypes.C, nOne.getType());
    assertEquals(3, nOne.getOctave());
    assertEquals(0, nOne.getStartBeat());
    assertEquals(16, nOne.getEndBeat());
  }

  @Test(expected = IllegalArgumentException.class)
  public void improperNote() {
    MusicEditorImpl e1 = MusicEditorImpl.makeEditor();
    AbstractNote nTwo = e1.makeNote(NoteTypes.C, 3, 16, 15, 0, 0);
  }

  @Test
  public void oneBeatNote() {
    MusicEditorImpl e1 = MusicEditorImpl.makeEditor();
    AbstractNote nTwo = e1.makeNote(NoteTypes.C, 3, 16, 16, 0, 0);
  }

  // changeNoteStart() Tests
  @Test
  public void noteStartLonger() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteStart(note, 1);
    assertEquals(1, note.getStartBeat());
    assertEquals(editor.getNote(NoteTypes.CSharp, 3, 1), note);
  }

  @Test
  public void noteStartShorter() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteStart(note, 3);
    assertEquals(3, note.getStartBeat());
    assertEquals(editor.getNote(NoteTypes.CSharp, 3, 3), note);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteStartChangeRemovesNote() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteStart(note, 3);
    editor.getNote(NoteTypes.CSharp, 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteStartOverEnd() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteStart(note, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteStartOverlaps() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 3, 4, 0, 0);
    AbstractNote note2 = editor.makeNote(NoteTypes.CSharp, 3, 1, 2, 0, 0);
    editor.addNote(note);
    editor.addNote(note2);
    editor.changeNoteStart(note, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteStartNegative() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteStart(note, -3);
  }

  // changeNoteEnd() Tests
  @Test
  public void noteEndLonger() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteEnd(note, 5);
    assertEquals(5, note.getEndBeat());
    assertEquals(editor.getNote(NoteTypes.CSharp, 3, 5), note);
  }

  @Test
  public void noteEndTrimTest() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 0, 99, 0, 0);
    editor.addNote(note);
    assertEquals(100, editor.scoreLength());
    editor.changeNoteEnd(note, 5);
    assertEquals(6, editor.scoreLength());
  }

  @Test
  public void noteEndShorter() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteEnd(note, 3);
    assertEquals(3, note.getEndBeat());
    assertEquals(editor.getNote(NoteTypes.CSharp, 3, 3), note);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteEndChangeRemovesNote() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteEnd(note, 3);
    editor.getNote(NoteTypes.CSharp, 2, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteEndOverStart() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteEnd(note, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteEndOverlaps() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 3, 4, 0, 0);
    AbstractNote note2 = editor.makeNote(NoteTypes.CSharp, 3, 1, 2, 0, 0);
    editor.addNote(note);
    editor.addNote(note2);
    editor.changeNoteEnd(note2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteEndNegative() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteEnd(note, -3);
  }

  // changeNoteOctave() Tests
  @Test
  public void noteOctaveChange() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteOctave(note, 2);
    assertEquals(2, note.getOctave());
    assertEquals(editor.getNote(NoteTypes.CSharp, 2, 3), note);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteOctaveChangeRemoves() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteOctave(note, 2);
    editor.getNote(NoteTypes.CSharp, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteOctaveNegative() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteOctave(note, -3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteOctaveOverlap() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    AbstractNote note2 = editor.makeNote(NoteTypes.CSharp, 4, 2, 4, 0, 0);
    editor.addNote(note);
    editor.addNote(note2);
    editor.changeNoteOctave(note, 4);
  }

  // changeNoteType() Tests

  @Test
  public void noteTypeChange() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteType(note, NoteTypes.A);
    assertEquals(editor.getNote(NoteTypes.A, 3, 3), note);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteTypeChangeRemoves() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteType(note, NoteTypes.A);
    editor.getNote(NoteTypes.CSharp, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteTypeOverlaps() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    AbstractNote note2 = editor.makeNote(NoteTypes.A, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.addNote(note2);
    editor.changeNoteType(note, NoteTypes.A);
  }

  // changeNoteVolume() Tests
  @Test
  public void noteVolumeChange() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteVol(note, 500);
    assertEquals(500, editor.getNote(NoteTypes.CSharp, 3, 3).getVolume());
  }

  @Test(expected = IllegalArgumentException.class)
  public void noteVolumeNegative() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.changeNoteVol(note, -5);
  }

  // getNote() Tests
  @Test
  public void getNoteTest() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    assertEquals(note, editor.getNote(NoteTypes.CSharp, 3, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNoteFail() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    editor.getNote(NoteTypes.CSharp, 2, 3);
  }

  // addNote() Tests
  @Test
  public void addNote() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 4, 0, 0);
    editor.addNote(note);
    assertEquals(note, editor.getNote(NoteTypes.CSharp, 3, 2));
    assertEquals(note, editor.getNote(NoteTypes.CSharp, 3, 3));
    assertEquals(note, editor.getNote(NoteTypes.CSharp, 3, 4));
  }

  @Test
  public void addLongNoteIncreasesLength() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10, 0, 0);
    AbstractNote note2 = editor.makeNote(NoteTypes.CSharp, 3, 11, 100, 0, 0);
    editor.addNote(note);
    assertEquals(11, editor.scoreLength());
    editor.addNote(note2);
    assertEquals(101, editor.scoreLength());
  }

  @Test(expected = IllegalArgumentException.class)
  public void addNoteOverlap() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10, 0, 0);
    AbstractNote note2 = editor.makeNote(NoteTypes.CSharp, 3, 3, 100, 0, 0);
    editor.addNote(note);
    editor.addNote(note2);
  }

  // scoreLength() Tests
  @Test
  public void scoreLengthTest1() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10, 0, 0);
    editor.addNote(note);
    assertEquals(11, editor.scoreLength());
  }

  @Test
  public void scoreLengthTest2() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    assertEquals(0, editor.scoreLength());
  }

  // getCurBeat() Tests
  @Test
  public void getCurBeatTest1() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10, 0, 0);
    editor.addNote(note);
    assertEquals(0, editor.getCurBeat());
  }

  @Test
  public void getCurBeatTest2() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote note = editor.makeNote(NoteTypes.CSharp, 3, 2, 10, 0, 0);
    editor.addNote(note);
    editor.playMusic();
    assertEquals(1, editor.getCurBeat());
  }

  // setTempo() Tests
  @Test
  public void setTempo() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    editor.setTempo(10);
    assertEquals(10, editor.getTempo());
  }

  @Test(expected = IllegalArgumentException.class)
  public void setTempoFail() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    editor.setTempo(-5);
  }

  // returnScore() Tests
  @Test
  public void returnScoreTest() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote n = editor.makeNote(NoteTypes.E, 2, 1, 1, 0, 0);
    ArrayList<ArrayList<AbstractNote>> tester = new ArrayList<>();
    ArrayList<AbstractNote> emptyBeatLine = new ArrayList<>();
    ArrayList<AbstractNote> beatLine = new ArrayList<>();
    beatLine.add(n);
    tester.add(emptyBeatLine);
    tester.add(beatLine);
    editor.addNote(n);
    assertEquals(tester, editor.returnScore());
  }

  // changeCurBeat() Tests
  @Test
  public void changeCurBeatTest() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote n = editor.makeNote(NoteTypes.E, 2, 1, 5, 0, 0);
    editor.addNote(n);
    editor.changeCurBeat(3);
    assertEquals(3, editor.getCurBeat());
  }

  @Test(expected = IllegalArgumentException.class)
  public void changeCurBeatNegative() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    editor.changeCurBeat(-3);
  }

  @Test(expected = IllegalStateException.class)
  public void changeCurBeatOutOfBounds() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote n = editor.makeNote(NoteTypes.E, 2, 1, 5, 0, 0);
    editor.addNote(n);
    editor.changeCurBeat(7);
  }

  // deleteNote() Tests
  @Test(expected = IllegalArgumentException.class)
  public void deleteNoteTest() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote n = editor.makeNote(NoteTypes.E, 2, 1, 5, 0, 0);
    editor.addNote(n);
    editor.deleteNote(n);
    editor.getNote(NoteTypes.E, 2, 4);
  }

  // playMusic() Tests
  @Test
  public void playMusic() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote n = editor.makeNote(NoteTypes.E, 2, 0, 5, 0, 0);
    ArrayList<AbstractNote> expected = new ArrayList<>();
    expected.add(n);
    editor.addNote(n);
    assertEquals(expected, editor.playMusic());
    assertEquals(1, editor.getCurBeat());
  }

  @Test(expected = IllegalStateException.class)
  public void playMusicFail() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    AbstractNote n = editor.makeNote(NoteTypes.E, 2, 1, 5, 0, 0);
    editor.changeCurBeat(5);
    editor.playMusic();
    editor.playMusic();
  }

  // consecutiveScore() Tests
  @Test
  public void consecutiveScoreTest() {
    MusicEditorImpl editor = MusicEditorImpl.makeEditor();
    MusicEditorImpl editor2 = MusicEditorImpl.makeEditor();
    MusicEditorImpl editor3 = MusicEditorImpl.makeEditor();
    AbstractNote n = editor.makeNote(NoteTypes.E, 2, 1, 5, 0, 0);
    AbstractNote n2 = editor.makeNote(NoteTypes.C, 2, 0, 5, 0, 0);
    AbstractNote n3 = editor.makeNote(NoteTypes.C, 2, 6, 11, 0, 0);
    editor.addNote(n);
    editor2.addNote(n2);
    editor3.addNote(n);
    editor3.addNote(n3);
    editor.consecutiveScore(editor2.returnScore());
    assertEquals(editor3.debugOutput(), editor.debugOutput());
  }
}
