import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Implementing the Music Editor Interface Created by Jonathan on 11/1/2015.
 */
public class MusicEditorImpl implements MusicEditorModel {
  // The current beat of this MusicEditorImpl
  private int curBeat;
  // Tempo for the music
  private int tempo;
  // The Array of notes for each beat
  private ArrayList<ArrayList<AbstractNote>> musicalArray;
  // The deepest note
  private NoteTypes lowNote;
  // The highest note
  private NoteTypes highNote;
  // The lowest octave
  private int lowOctave;
  // The highest octave
  private int highOctave;

  /**
   * musicalArray starts empty and can be changed either by adding a printedscore or by individually
   * adding notes
   */
  // TODO: Going to need to implement a Builder later
  MusicEditorImpl() {
    curBeat = 0;
    musicalArray = new ArrayList<>();
    // The below assignments are defaults
    tempo = 1;
  }

  protected Note makeNote(NoteTypes note, int octave, int startBeat, int endBeat) {
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
  private boolean validChange(AbstractNote potChange) {
    if (potChange.getType() == null) {
      throw new IllegalArgumentException("Can't have null NoteType");
    }
    for (int i = potChange.getStartBeat(); i <= potChange.getEndBeat(); i += 1) {
      // If index is out of bounds, that beat doesn't exist yet so a new beat is added
      while (i >= this.musicalArray.size()) {
        this.musicalArray.add(new ArrayList<AbstractNote>());
      }
      for (AbstractNote n : this.musicalArray.get(i)) {
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
  public void changeNoteStart(AbstractNote note, int startBeat) {
    int origStart = note.getStartBeat();
    note.changeStart(startBeat);
    // Is the startBeat earlier than the current start
    if (startBeat < origStart) {
      for (int i = startBeat; i < origStart; i += 1) {
        for (AbstractNote n : this.musicalArray.get(i)) {
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
      for (int i = origStart; i < startBeat; i += 1) {
        this.musicalArray.get(i).remove(note);
      }
    }
  }

  /**
   * Trims empty Arrays at the end of the piece when end is changed or notes are removed
   */
  private void trimEnd() {
    int index = this.scoreLength() - 1;
    while (this.musicalArray.get(index).size() == 0 && index > 0) {
      if (index < 0) {
        throw new IllegalStateException("This method was called when it shouldn't have been");
      }
      this.musicalArray.remove(index);
      index -= 1;
    }
  }

  @Override
  public void changeNoteEnd(AbstractNote note, int endBeat) {
    int origEnd = note.getEndBeat();
    note.changeEnd(endBeat);
    // Is the new end later than the current end
    if (endBeat > origEnd) {
      for (int i = endBeat; i > origEnd; i -= 1) {
        while (i >= this.scoreLength()) {
          this.musicalArray.add(new ArrayList<AbstractNote>());
        }
        for (AbstractNote n : this.musicalArray.get(i)) {
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
      for (int i = origEnd; i > endBeat; i -= 1) {
        this.musicalArray.get(i).remove(note);
        this.trimEnd();
      }
    }
  }

  /**
   * Computes the note range of the piece
   */
  private void updateRange() {
    int highOctave = -1;
    int highNote = -1;
    int lowOctave = -1;
    int lowNote = -1;
    for (ArrayList<AbstractNote> a : this.musicalArray) {
      for (AbstractNote n : a) {
        if (n.getOctave() >= highOctave && n.getType().noteOrder() > highNote) {
          highOctave = n.getOctave();
          highNote = n.getType().noteOrder();
        }
        if (n.getOctave() <= lowOctave && n.getType().noteOrder() < lowNote) {
          lowOctave = n.getOctave();
          lowNote = n.getType().noteOrder();
        }
      }
    }

    if (highOctave != -1 && highNote != -1) {
      this.highOctave = highOctave;
      this.highNote = NoteTypes.valueLookup(highNote);
    }
    if (lowOctave != -1 && lowNote != -1) {
      this.lowOctave = lowOctave;
      this.lowNote = NoteTypes.valueLookup(lowNote);
    }
  }

  @Override
  public void changeNoteOctave(AbstractNote note, int octave) {
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
    this.updateRange();
  }

  @Override
  public void changeNoteType(AbstractNote note, NoteTypes newType) {
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
    this.updateRange();
  }

  @Override
  public void changeNoteVol(AbstractNote note, int volume) {
    note.changeVolume(volume);
  }

  @Override
  public AbstractNote getNote(NoteTypes type, int octave, int beat) {
    try {
      this.musicalArray.get(beat);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("No such note");
    }
    // Gets the Array of notes at a certain beat
    ArrayList<AbstractNote> beatNotes = this.musicalArray.get(beat);
    for (AbstractNote n : beatNotes) {
      // If the octave and type are the same at this beat, it is the note we are looking for
      // because duplicate same notes are not allowed at this point
      if (type == n.getType() && octave == n.getOctave()) {
        return n;
      }
    }
    throw new IllegalArgumentException("No such note");
  }

  @Override
  public void addNote(AbstractNote note) {
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
    // Is there not a high note?
    if (this.highNote == null) {
      this.highNote = note.getType();
      this.highOctave = note.getOctave();
    }
    // Is there not a low note?
    if (this.lowNote == null) {
      this.lowNote = note.getType();
      this.lowOctave = note.getOctave();
    }
    // Is the new note a higher octave?
    if (note.getOctave() > this.highOctave) {
      this.highOctave = note.getOctave();
      this.highNote = note.getType();
      // Is the new note a lower octave?
    } else if (note.getOctave() < this.lowOctave) {
      this.lowOctave = note.getOctave();
      this.lowNote = note.getType();
      // Are they the same octave and the notetype is higher?
    } else if (note.getOctave() == this.highOctave &&
            note.getType().noteOrder() > this.highNote.noteOrder()) {
      this.highNote = note.getType();
    }
    // Are they the same octave and the notetype is lower?
    else if (note.getOctave() == this.lowOctave &&
            note.getType().noteOrder() < this.highNote.noteOrder()) {
      this.lowNote = note.getType();
    }
  }

  @Override
  public int scoreLength() {
    return this.musicalArray.size();
  }

  @Override
  public int getCurBeat() {
    return this.curBeat;
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public void setTempo(int newTempo) {
    if (newTempo <= 0) {
      throw new IllegalArgumentException("Not a valid tempo");
    }
    this.tempo = newTempo;
  }

  @Override
  public ArrayList<Collection<AbstractNote>> returnScore() {
    ArrayList<Collection<AbstractNote>> shield = new ArrayList<>();
    shield.addAll(this.musicalArray);
    return shield;
  }

  @Override
  public void changeCurBeat(int newBeat) {
    if (newBeat < 0) {
      throw new IllegalArgumentException("Invalid beat");
    }
    if (newBeat > this.scoreLength()) {
      throw new IllegalStateException("No more music at this beat");
    }
    this.curBeat = newBeat;
  }

  @Override
  public void deleteNote(AbstractNote note) {
    for (int i = note.getStartBeat(); i <= note.getEndBeat(); i += 1) {
      this.musicalArray.get(i).remove(note);
    }
    this.trimEnd();
    this.updateRange();
  }

  @Override
  public Collection<AbstractNote> playMusic() {
    this.changeCurBeat(curBeat + 1);
    try {
      this.musicalArray.get(curBeat - 1);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalStateException("No more music");
    }
    ArrayList<AbstractNote> shield = new ArrayList<>();
    shield.addAll(this.musicalArray.get(curBeat - 1));
    return shield;
  }

  /**
   * Builds the upper note range in the printout
   *
   * @return the String of notes in the range
   */
  private StringBuilder noteRange() {
    // Leading space to push it over to fit numbers
    StringBuilder acc = new StringBuilder();
    // The string count of the number of beats in the song
    int length = Integer.toString(this.scoreLength()).length();
    while (length >= 0) {
      acc.append(" ");
      length -= 1;
    }
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
    for (ArrayList<AbstractNote> a : this.musicalArray) {
      // Has to create a placeholder line with spaces in order to insert by index
      StringBuilder newLine = new StringBuilder();
      for (int i = indxRange + 10; i > 1; i -= 1) {
        newLine.append(" ");
      }
      // Starts the line by adding beat number
      newLine.replace(
              Integer.toString(this.scoreLength()).length() -
                      Integer.toString(beatNumber).length(),
              Integer.toString(this.scoreLength()).length(), Integer.toString(beatNumber));
      // adds each node to the new line represented by an X
      for (AbstractNote n : a) {
        String nodeLoc = n.toString();
        if (beatNumber == n.getStartBeat()) {
          newLine.replace(rangeMap.get(nodeLoc), rangeMap.get(nodeLoc) + 1, "X");
        } else {
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
  public String debugOutput() {
    return this.toString();
  }

  @Override
  public void consecutiveScore(ArrayList<Collection<AbstractNote>> secondScore) {
    // Push secondScore's notes start and end beats by the length of this musicalArray
    for (int i = 0; i < secondScore.size(); i += 1) {
      for (AbstractNote n : secondScore.get(i)) {
        if (n.getStartBeat() == i) {
          n.changeEnd(n.getEndBeat() + this.scoreLength());
          n.changeStart(n.getStartBeat() + this.scoreLength());
          this.addNote(n);
        }
      }
    }

  }
}
