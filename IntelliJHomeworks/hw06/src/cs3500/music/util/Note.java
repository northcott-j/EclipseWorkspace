/**
 * Represents a concrete Note and its information Created by Jonathan on 11/1/2015.
 */
final class Note extends AbstractNote {

  private Note(NoteTypes type, int octave, int startBeat, int endBeat, int volume) {
    super(type, octave, startBeat, endBeat, volume);
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
  static Note makeNote(NoteTypes note, int octave, int startBeat, int endBeat) {
    if (octave < 0 || octave > 10 || startBeat < 0 || endBeat < 0 || endBeat < startBeat ||
            startBeat > endBeat || note == null) {
      throw new IllegalArgumentException("Invalid Note");
    }
    // Volume has been set to a default of 1 for now
    return new Note(note, octave, startBeat, endBeat, 1);
  }

  /**
   * This note as a string
   *
   * @return Combines note type and octave
   */
  @Override
  public String toString() {
    return this.type.toString() + Integer.toString(this.octave);
  }

  /**
   * Overrides the equals method
   *
   * @param other object (hopefully a note to be checked)
   * @return true or false if it matches
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof AbstractNote) {
      AbstractNote otherNote = (AbstractNote) other;
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

}
