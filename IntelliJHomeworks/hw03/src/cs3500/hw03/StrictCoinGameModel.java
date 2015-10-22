package cs3500.hw03;

import java.util.LinkedList;
import java.util.Queue;

/**
 * You don't need to implement this class or any concrete subclasses
 * for pset03.
 */
public final class StrictCoinGameModel implements CoinGameModel {
  // (Exercise 2) Declare the fields needed to support the methods in
  // the interface you’ve designed:

  private Queue<String> players;
  private String priorPlayer;

  // (Exercise 3) Describe, as precisely as you can, your
  // representation’s class invariants:

  /*
  * INVARIANTS:
  * - Must start with at least one player (protected by builder)
  * - Players cannot have the same String name (protected by builder)
  * - Make sure order of play can't be changed
  *
   */

  // (Exercise 4) Describe your constructor API here by filling in
  // whatever arguments you need and writing good Javadoc. (You may
  // declare any combination of constructors and static factory
  // methods that you like, but you need not get fancy.)
  /**
   * Constructs [fill in comprehensive and clear Javadoc here]
   * - a Builder is used to add an extra level
   * - The builder constructor can take any number of Strings
   *    - Strings represent players
   * - the Builder.build() method constructs a StrictCoinModelGame protecting against the
   *   latter invariants.
   *
   * @param players represented as a Queue in order to guarantee order of turns
   * @param priorPlayer is the last player who made a move making them the winner if the game ends
   * @throws [same deal for exceptions]
   */
  protected StrictCoinGameModel(Queue<String> players, String priorPlayer) {
    // You don't need to implement this constructor.
    throw new UnsupportedOperationException("no need to implement this");
  }

  // You don't need to implement any methods or constructors. However,
  // if you want to make sure your code compiles, you could have your
  // IDE generate stubs for all the missing methods. This would also
  // allow you to make sure that your tests in StrictCoinGameModelTest
  // actually type check and compile against this class (though you
  // don’t need to make them pass, because you don’t need to implement
  // StrictCoinGameModel’s methods).


  @Override
  public int boardSize() {
    return 0;
  }

  @Override
  public int coinCount() {
    return 0;
  }

  @Override
  public int getCoinPosition(int coinIndex) {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public void move(int coinIndex, int newPosition) {

  }

  @Override
  public String getWinner() {
    return null;
  }

  @Override
  public void addPlayers(String... players) {

  }

  @Override
  public String whoseTurn() {
    return null;
  }

  /**
   * Modifies the game to reflect a new turn
   *
   * SIDE EFFECTS:
   * priorPlayer gets the name of who just moved
   * the next person in players gets moved to the back
   */
  private void nextTurn() {

  }

  /**
   * Builds a {@link StrictCoinGameModel}, allowing the client to configure
   * parameters. This is an instance of the <em>builder pattern</em>.
   */
  public static final class Builder {

    Queue<String> players = new LinkedList<>();

    /**
     * Constructor for a Builder
     *
     * @param names are the players to be added. If no names are specified, a default is made
     * @throws IllegalArgumentException if a name is already in the list
     */

    public Builder(String... names) {
      // If no names are added, make a default player
      if (names.length == 0) {
        players.add("DefaultPlayer");
      }
      else {
        for (String n : names) {
          // If a name is repeated, throw an illegal argument exception
          if (players.contains(n)) {
            throw new IllegalArgumentException("Name" + n + "is already taken");
          }
          else {
            // If everything is all good, add name
            players.add(n);
          }
        }
      }
    }

    public StrictCoinGameModel build() {
      return new StrictCoinGameModel(players, players.peek());
    }
  }
}
