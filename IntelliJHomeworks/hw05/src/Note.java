import java.util.Objects;

/**
 * Represents a note and its information Created by Jonathan on 11/1/2015.
 */
class Note {
  private NoteTypes type;
  private int octave;
  private int startBeat;
  private int endBeat;
  private int volume;

  /**
   * @param type      is the type of note that it is
   * @param octave    is the pitch of the note
   * @param startBeat is the beat the note starts on
   * @param endBeat   is the beat the note ends on
   * @param volume    the volume of the node (not used right now)
   */
  private Note(NoteTypes type, int octave, int startBeat, int endBeat, int volume) {
    this.type = type;
    this.octave = octave;
    this.startBeat = startBeat;
    this.endBeat = endBeat;
    this.volume = volume;
  }

  /**
   * Creates a note and enforces invariants
   *
   * @param note      is the type of note
   * @param octave    is the octave or pitch of the note
   * @param startBeat is the start beat of the note
   * @param endBeat   is the end beat of the note
   * @return the new note
   * @throws IllegalArgumentException if arguments don't make a proper note
   */
  // TODO: Implement volume later on when it is important
  static final Note makeNote(NoteTypes note, int octave, int startBeat, int endBeat) {
    if (octave < 0 || octave > 10 || startBeat < 0 || endBeat < 0 || endBeat < startBeat ||
            startBeat > endBeat) {
      throw new IllegalArgumentException("Invalid Note");
    }
    // Volume has been set to a default of 1 for now
    return new Note(note, octave, startBeat, endBeat, 1);
  }

  /**
   * Returns the type of the note
   * @return returns the NoteType of this note
   */
  NoteTypes getType() {
    return this.type;
  }

  /**
   * Returns the note's octave
   * @return the octave of this note
   */
  int getOctave() {
    return this.octave;
  }

  /**
   * Returns the startBeat of the note
   * @return the startBeat of this note
   */
  int getStartBeat() {
    return this.startBeat;
  }

  /**
   * Returns the end beat of the note
   * @return the endBeat of this note
   */
  int getEndBeat() {
    return this.endBeat;
  }

  /**
   * Returns the volume of the note
   * @return the volume of this note
   */
  int getVolume() {
    return this.volume;
  }

  /**
   * Changes the startBeat of this note
   * @param newStart the new startBeat
   * @throws IllegalArgumentException if startBeat is invalid
   */
  void changeStart(int newStart) {
    if (newStart < 0 || newStart >= endBeat) {
      throw new IllegalArgumentException("Invalid startBeat");
    }
    this.startBeat = newStart;
  }

  /**
   * Changes the endBeat of this note
   * @param newEnd the new endBeat
   * @throws IllegalArgumentException if invalid endBeat
   */
  void changeEnd(int newEnd) {
    if (newEnd < 0 || newEnd <= startBeat) {
      throw new IllegalArgumentException("Invalid endBeat");
    }
    this.endBeat = newEnd;
  }

  /**
   * Changes the type of the note
   * @param newType the new type for this note
   */
  void changeType(NoteTypes newType) {
    this.type = newType;
  }

  /**
   * Changes the octave of this note
   * @param newOctave the new octave
   * @throws IllegalArgumentException if the octave is invalid
   */
  void changeOctave(int newOctave) {
    if (newOctave < 0 || newOctave > 10) {
      throw new IllegalArgumentException("Invalid octave");
    }
    this.octave = newOctave;
  }

  /**
   * Changes the volume of this note
   * @param newVolume the new volume
   * @throws IllegalArgumentException if the volume is invalid
   */
  void changeVolume(int newVolume) {
    if (newVolume < 0) {
      throw new IllegalArgumentException("Invalid volume");
    }
    this.volume = newVolume;
  }

  /**
   * This note as a string
   * @return Combines note type and octave
   */
  @Override
  public String toString() {
    return this.type.toString() + Integer.toString(this.octave);
  }

  /**
   * Overrides the equals method
   * @param other object (hopefully a note to be checked)
   * @return true or false if it matches
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Note) {
      Note otherNote = (Note) other;
      return this.octave == otherNote.octave &&
              this.startBeat == otherNote.startBeat &&
              this.endBeat == otherNote.endBeat &&
              this.type == otherNote.type &&
              this.volume == otherNote.volume;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return 31 * (octave + startBeat + endBeat + type.hashCode() + volume);
  }

  /**
   * Checks to see if it overlaps another note
   * @param other note to be checked
   * @return true if this note doesn't land on any part of the other note
   */
  boolean overlap(Note other) {
    return this.type == other.type &&
            this.octave == other.octave;
  }
}
