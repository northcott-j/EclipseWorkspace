import java.util.ArrayList;

/**
 * Implementing the Music Editor Interface
 * Created by Jonathan on 11/1/2015.
 */
public class MusicEditorImpl implements MusicEditorModel {
  // The current beat of this MusicEditorImpl
  int curBeat;
  // The Array of notes for each beat
  ArrayList<ArrayList<Note>> musicalArray;

  /**
   * musicalArray starts empty and can be changed either by adding a printedscore
   * or by individually adding notes
   */
  MusicEditorImpl() {
    curBeat = 0;
    musicalArray = new ArrayList<ArrayList<Note>>();
  }

  @Override
  public Note makeNote(NoteTypes note, int octave, int startBeat, int endBeat) {
    return Note.makeNote(note, octave, startBeat, endBeat);
  }

  @Override
  public void changeNoteStart(Note note, int startBeat) {
    note.changeStart(startBeat);
  }

  @Override
  public void changeNoteEnd(Note note, int endBeat) {
    note.changeEnd(endBeat);
  }

  @Override
  public void changeNoteOctave(Note note, int octave) {
    note.changeOctave(octave);
  }

  @Override
  public void changeNoteType(Note note, NoteTypes newType) {
    note.changeType(newType);
  }

  @Override
  public void changeNoteVol(Note note, int volume) {
    note.changeVolume(volume);
  }

  // TODO: Need to implement all of these methods
  @Override
  public Note getNote(NoteTypes type, int octave, int beat) {
    return null;
  }

  @Override
  public void addNote(Note note) {

  }

  @Override
  public ArrayList<ArrayList<Note>> generateScore(String printedScore) {
    return null;
  }

  @Override
  public ArrayList<ArrayList<Note>> returnScore() {
    return null;
  }

  @Override
  public void changeCurBeat(int newBeat) {

  }

  @Override
  public void deleteNote(Note note) {

  }

  @Override
  public ArrayList<Note> playMusic() {
    return null;
  }

  @Override
  /**
   * Overrides the toString method
   * @return the musical array as a printed text version
   */
  public String toString() {
    return null;
  }

  @Override
  public void simultaneousScore(ArrayList<ArrayList<Note>> secondScore) {

  }

  @Override
  public void consecutiveScore(ArrayList<ArrayList<Note>> secondScore) {

  }
}
// TODO: Need to write tests for all of these
// TODO: Need a README