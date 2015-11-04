import java.util.ArrayList;

/**
 * The model interface for the Music Editor
 * Created by Jonathan on 11/1/2015.
 */
public interface MusicEditorModel {
  /**
   * Makes a new note and checks invariants such as:
   *    - octave, startBeat and endBeat are positive
   *    - octave is less than or equal to ten
   *    - startBeat is less than end beat
   *
   * @param note the type of Note to be played
   * @param octave the pitch of the Note to be played
   * @param startBeat the start beat of the Note
   * @param endBeat the end beat of the Note
   * @return a new Note
   * @throws IllegalArgumentException if above variants are broken
   */
  // Leakage warning
  Note makeNote(NoteTypes note, int octave, int startBeat, int endBeat);

  /**
   * Changes the Start beat of a note
   * @param note the note to be changed
   * @param startBeat the new start beat
   * @throws IllegalArgumentException if startBeat is negative or greater than end
   */
  void changeNoteStart(Note note, int startBeat);

  /**
   * Changes the End beat of a note
   * @param note the note to be changed
   * @param endBeat the new end beat
   * @throws  IllegalArgumentException if endBeat is negative or less than start
   */
  void changeNoteEnd(Note note, int endBeat);

  /**
   * Changes the Octave of a note
   * @param note the note to be changed
   * @param octave the new octave
   * @throws IllegalArgumentException if octave < 0 or octave > 10
   */
  void changeNoteOctave(Note note, int octave);

  /**
   * Changes the type of a note
   * @param note the note to be changed
   * @param newType the new type for the note
   * @throws IllegalArgumentException if this causes an overlap
   */
  void changeNoteType(Note note, NoteTypes newType);

  /**
   * Changes the note volume
   * @param note the note to be changed
   * @param volume the new volume for the note
   * @throws IllegalArgumentException if volume is negative
   */
  void changeNoteVol(Note note, int volume);

  /**
   * Gets the note based on type and octave during a certain beat
   * @param type the type of the note
   * @param octave the octave of the note
   * @param beat the beat the note is on
   * @return the Note wanted
   * @throws IllegalArgumentException if no note matching those arguments
   */
  // Leakage warning
  Note getNote(NoteTypes type, int octave, int beat);

  /**
   * Adds a Note into the board
   * @param note the note to be added
   * @throws IllegalArgumentException if it causes an overlap
   */
  void addNote(Note note);

  /**
   * The amount of beats in the array
   * @return the length of the score in beats
   */
  int scoreLength();

  /**
   * Returns the current beat
   * @return the current beat
   */
  int getCurBeat();

  /**
   * Returns the tempo of the score
   * @return the tempo
   */
  int getTempo();

  /**
   * Sets the tempo
   * @throws IllegalArgumentException if tempo is negative or 0
   */
  void setTempo(int newTempo);

  /**
   * Outputs the musical array
   * @return the musical array
   */
  // Leakage warning
  ArrayList<ArrayList<Note>> returnScore();

  /**
   * Updates the current beat of the editor
   * @param newBeat is the new beat to start on
   * @throws IllegalArgumentException if the beat is negative
   */
  void changeCurBeat(int newBeat);

  /**
   * Deletes a certain note from the board
   * @param note is the note to be deleted
   * @throws IllegalArgumentException if no such note
   */
  void deleteNote(Note note);

  /**
   * Outputs information needed to play music and
   * ticks the beat up
   * @return the Array of notes to be played at this beat
   * @throws IllegalStateException no more music
   */
  // Leakage warning
  ArrayList<Note> playMusic();

  /**
   * Outputs the text printout
   * @return String of the board
   */
  String debugOutput();

  // Implement later if needed
/*
  */
/**
   * Adds the two musical arrays together ignoring repeated notes
   * @param secondScore is the music to be combined with current score
   *//*

  void simultaneousScore(ArrayList<ArrayList<Note>> secondScore);
*/

  /**
   * Adds this score to the end of the current
   * @param secondScore is the music to be added at the end
   */
  void consecutiveScore(ArrayList<ArrayList<Note>> secondScore);
}
