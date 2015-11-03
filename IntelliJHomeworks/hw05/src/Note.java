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
  static final Note makeNote(NoteTypes note, int octave, int startBeat, int endBeat) {
    if (octave < 0 || octave > 10 || startBeat < 0 || endBeat < 0 || endBeat < startBeat ||
            startBeat > endBeat) {
      throw new IllegalArgumentException("Invalid Note");
    }
    // Volume has been set to a default of 1 for now
    return new Note(note, octave, startBeat, endBeat, 1);
  }

  //TODO: need to add JavaDoc to all of these methods
  NoteTypes getType() {
    return this.type;
  }

  int getOctave() {
    return this.octave;
  }

  int getStartBeat() {
    return this.startBeat;
  }

  int getEndBeat() {
    return this.endBeat;
  }

  int getVolume() {
    return this.volume;
  }

  void changeStart(int newStart) {
    if (newStart < 0 || newStart >= endBeat) {
      throw new IllegalArgumentException("Invalid startBeat");
    }
    this.startBeat = newStart;
  }

  void changeEnd(int newEnd) {
    if (newEnd < 0 || newEnd <= startBeat) {
      throw new IllegalArgumentException("Invalid endBeat");
    }
    this.endBeat = newEnd;
  }

  void changeType(NoteTypes newType) {
    this.type = newType;
  }

  void changeOctave(int newOctave) {
    if (newOctave < 0 || newOctave > 10) {
      throw new IllegalArgumentException("Invalid octave");
    }
    this.octave = newOctave;
  }

  void changeVolume(int newVolume) {
    if (newVolume < 0) {
      throw new IllegalArgumentException("Invalid volume");
    }
    this.volume = newVolume;
  }

  @Override
  public String toString() {
    return this.type.toString() + Integer.toString(this.octave);
  }

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

  boolean overlap(Note other) {
    return this.type == other.type &&
            this.octave == other.octave;
  }
}
