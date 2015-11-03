import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementing the Music Editor Interface Created by Jonathan on 11/1/2015.
 */
public class MusicEditorImpl implements MusicEditorModel {
  // The current beat of this MusicEditorImpl
  int curBeat;
  // The Array of notes for each beat
  ArrayList<ArrayList<Note>> musicalArray;
  // The deepest note
  NoteTypes lowNote;
  // The highest note
  NoteTypes highNote;
  // The lowest octave
  int lowOctave;
  // The highest octave
  int highOctave;

  /**
   * musicalArray starts empty and can be changed either by adding a printedscore or by individually
   * adding notes
   */
  MusicEditorImpl() {
    curBeat = 0;
    musicalArray = new ArrayList<ArrayList<Note>>();
    // The below assignments are defaults
    lowNote = NoteTypes.E;
    highNote = NoteTypes.G;
    lowOctave = 2;
    highOctave = 3;
  }

  @Override
  public Note makeNote(NoteTypes note, int octave, int startBeat, int endBeat) {
    return Note.makeNote(note, octave, startBeat, endBeat);
  }

  /**
   * Checks to see if the change being made (adding a note, changing a note etc.) is valid by seeing
   * if it overlaps a note already in the musical array
   *
   * @param potChange the changed note
   * @return true if it is valid
   * @throws IllegalArgumentException if change is invalid
   */
  private boolean validChange(Note potChange) {
    for (int i = potChange.getStartBeat(); i <= potChange.getEndBeat(); i += 1) {
      // If index is out of bounds, that beat doesn't exist yet so a new beat is added
      try {
        this.musicalArray.get(i);
      } catch (IndexOutOfBoundsException e) {
        this.musicalArray.add(new ArrayList<Note>());
      }
      for (Note n : this.musicalArray.get(i)) {
        // Checks every note in the potential notes range to see if they are the same type and
        // octave
        if (potChange.overlap(n)) {
          throw new IllegalArgumentException("Not a valid change");
        }
      }
    }
    return true;
  }

  @Override
  public void changeNoteStart(Note note, int startBeat) {
    int origStart = note.getStartBeat();
    note.changeStart(startBeat);
    // Is the startBeat earlier than the current start
    if (startBeat < note.getStartBeat()) {
      for (int i = startBeat; i < note.getStartBeat(); i += 1) {
        for (Note n : this.musicalArray.get(i)) {
          // Make sure they don't overlap another note
          if (note.overlap(n)) {
            // this reverts the mutation
            note.changeStart(origStart);
            throw new IllegalArgumentException("This change causes an overlap at beat "
                    + Integer.toString(i));
          }
        }
        this.musicalArray.get(i).add(note);
      }
    } else {
      // The new start is later than the current start
      for (int i = note.getStartBeat(); i < startBeat; i += 1) {
        this.musicalArray.get(i).remove(note);
      }
    }
  }

  @Override
  public void changeNoteEnd(Note note, int endBeat) {
    int origEnd = note.getEndBeat();
    note.changeEnd(endBeat);
    // Is the new end later than the current end
    if (endBeat > note.getEndBeat()) {
      for (int i = note.getEndBeat(); i < endBeat; i += 1) {
        for (Note n : this.musicalArray.get(i)) {
          // Checks note range to check if there is an overlap
          if (note.overlap(n)) {
            // Reverts the mutation
            note.changeEnd(origEnd);
            throw new IllegalArgumentException("This change causes an overlap at beat "
                    + Integer.toString(i));
          }
        }
        this.musicalArray.get(i).add(note);
      }
    } else {
      // The end is earlier than the current end
      for (int i = endBeat; i < note.getEndBeat(); i += 1) {
        this.musicalArray.get(i).remove(note);
      }
    }
  }

  @Override
  public void changeNoteOctave(Note note, int octave) {
    // Creates a temporary note
    Note potChange = this.makeNote(note.getType(), octave, note.getStartBeat(), note.getEndBeat());
    // Checks to see if this note was changed, would it overlap anywhere
    if (this.validChange(potChange)) {
      note.changeOctave(octave);
      // Is this a new max high octave?
      if (octave > this.highOctave) {
        this.highOctave = octave;
        this.highNote = note.getType();
      }
      // Is this a new max low octave?
      else if (octave < this.lowOctave) {
        this.lowOctave = octave;
        this.lowNote = note.getType();
      }
    }
  }

  @Override
  public void changeNoteType(Note note, NoteTypes newType) {
    // Creates a temporary note
    Note potChange = this.makeNote(newType, note.getOctave(), note.getStartBeat(),
            note.getEndBeat());
    // Checks to see if change was made, would this note overlap a current one
    if (this.validChange(potChange)) {
      note.changeType(newType);
      // If this note is of the highest octave, is the new note higher than the current max?
      if (newType.noteOrder() > this.highNote.noteOrder() && note.getOctave() == this.highOctave) {
        this.highNote = newType;
      }
      // If this note is of the lowest ovtave, is the new note lower than the current low?
      else if (newType.noteOrder() < this.lowNote.noteOrder() &&
              note.getOctave() == this.lowOctave) {
        this.lowNote = newType;
      }
    }
  }

  @Override
  public void changeNoteVol(Note note, int volume) {
    note.changeVolume(volume);
  }

  @Override
  public Note getNote(NoteTypes type, int octave, int beat) {
    // Gets the Array of notes at a certain beat
    ArrayList<Note> beatNotes = this.musicalArray.get(beat);
    for (Note n : beatNotes) {
      // If the octave and type are the same at this beat, it is the note we are looking for
      // because duplicate same notes are not allowed at this point
      if (type == n.getType() && octave == n.getOctave()) {
        return n;
      }
    }
    throw new IllegalArgumentException("No such note");
  }

  @Override
  public void addNote(Note note) {
    // Checks to see if this note overlaps any others
    try {
      this.validChange(note);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Adding this note causes an overlap");
    }
      // Adds the note in at each beat
      for (int i = note.getStartBeat(); i <= note.getEndBeat(); i += 1) {
        this.musicalArray.get(i).add(note);
      }
  }

  @Override
  //TODO: Need to take a printed score and create a musicalArray
  public ArrayList<ArrayList<Note>> generateScore(String printedScore) {
    return null;
  }

  @Override
  public ArrayList<ArrayList<Note>> returnScore() {
    return this.musicalArray;
  }

  @Override
  public void changeCurBeat(int newBeat) {
    if (newBeat < 0) {
      throw new IllegalArgumentException("Can't have a negative beat");
    }
    this.curBeat = newBeat;
  }

  @Override
  public void deleteNote(Note note) {
    for (int i = note.getStartBeat(); i <= note.getEndBeat(); i += 1) {
      this.musicalArray.get(i).remove(note);
    }
  }

  @Override
  public ArrayList<Note> playMusic() {
    this.changeCurBeat(curBeat + 1);
    return this.musicalArray.get(curBeat - 1);
  }

  /**
   * Builds the upper note range in the printout
   *
   * @return the String of notes in the range
   */
  private StringBuilder noteRange() {
    // Leading space to push it over to fit numbers
    StringBuilder acc = new StringBuilder(" ");
    // String representation of the highest note
    String highestNote = this.highNote.toString() + Integer.toString(this.highOctave) + " ";
    String curNote = "";
    int curNoteVal = this.lowNote.noteOrder();
    int curNoteOct = this.lowOctave;
    while (!curNote.equals(highestNote)) {
      if (curNoteVal > 11) {
        // 11 is the highest note order value
        curNoteVal = 0;
        curNoteOct += 1;
      }
      // Gets the NoteType string name and adds it to the current octave plus a space
      curNote = NoteTypes.valueLookup(curNoteVal).toString()
              + Integer.toString(curNoteOct) + " ";
      acc.append(curNote);
      curNoteVal += 1;
    }
    // adds a new line character before returning it
    return acc.append("\n");
  }

  /**
   * Creates a HashMap mapping Note names to their index in the range StringBuilder
   *
   * @param noteRange StringBuilder range of notes
   * @return HashMap of Note names to index
   */
  private HashMap<String, Integer> createRangeHMap(StringBuilder noteRange) {
    HashMap<String, Integer> map = new HashMap<>();
    // The string name of the highest note
    String highestNote = this.highNote.toString() + Integer.toString(this.highOctave);
    String curNote = "";
    int curNoteVal = this.lowNote.noteOrder();
    int curNoteOct = this.lowOctave;
    while (!curNote.equals(highestNote)) {
      if (curNoteVal > 11) {
        // 11 is the highest note order value
        curNoteVal = 0;
        curNoteOct += 1;
      }
      // Gets the NoteType String name and appends it to the octave number
      curNote = NoteTypes.valueLookup(curNoteVal).toString()
              + Integer.toString(curNoteOct);
      map.put(curNote, noteRange.indexOf(curNote));
      curNoteVal += 1;
    }
    return map;
  }

  @Override
  /**
   * Overrides the toString method
   * @return the musical array as a printed text version
   */
  public String toString() {
    // Gets the note range
    StringBuilder acc = this.noteRange();
    //note Index Range
    int indxRange = acc.length();
    // Creates the hashmap for note indices in range
    HashMap<String, Integer> rangeMap = this.createRangeHMap(acc);
    // Current beat
    int beatNumber = 0;
    for (ArrayList<Note> a : this.musicalArray) {
      // Has to create a placeholder line with spaces in order to insert by index
      StringBuilder newLine = new StringBuilder();
      for (int i = indxRange ; i > 1 ; i -= 1) {
        newLine.append(" ");
      }
      // Starts the line by adding beat number
      newLine.replace(0, Integer.toString(beatNumber).length(), Integer.toString(beatNumber));
      // adds each node to the new line represented by an X
      for (Note n : a) {
        String nodeLoc = n.toString();
        if (beatNumber == n.getStartBeat()) {
          newLine.replace(rangeMap.get(nodeLoc), rangeMap.get(nodeLoc) + 1, "X");
        }
        else {
          newLine.replace(rangeMap.get(nodeLoc), rangeMap.get(nodeLoc) + 1, "|");
        }
      }
      // Add line break
      newLine.append("\n");
      // Adds the completed line to the acc
      acc.append(newLine);
      beatNumber += 1;
    }
    return acc.toString();
  }

  @Override
  // TODO: Need to implement this method
  public void simultaneousScore(ArrayList<ArrayList<Note>> secondScore) {

  }

  @Override
  //TODO: Update all Notes start and end
  public void consecutiveScore(ArrayList<ArrayList<Note>> secondScore) {
    this.musicalArray.addAll(secondScore);
  }
}
// TODO: Need to write tests for all of these
// TODO: Need a README
// TODO: Default beat length to increase the window height
// TODO: Comment out methods not being written