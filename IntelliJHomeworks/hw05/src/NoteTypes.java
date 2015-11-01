/**
 * The types of notes in the Music Editor
 * Created by Jonathan on 11/1/2015.
 */
public enum NoteTypes {
  C ("C"), CSharp ("C#"), D ("D"), DSharp ("D#"), E ("E"), F ("F"),
  FSharp ("F#"), G ("G"), GSharp ("G#"), A ("A"), ASharp ("A#"), B ("B");

  private final String noteAsString;
  NoteTypes(String noteAsString) {
    this.noteAsString = noteAsString;
  }

  /**
   * Represents the enum as a string
   * @return the string note
   */
  @Override
  public String toString() {
    return noteAsString;
  }
}
