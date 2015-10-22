
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the Strict Coin Game
 */
public final class StrictCoinGameModel implements CoinGameModel {

  private ArrayList<Boolean> board;
  private ArrayList<Integer> coins;
  private ArrayList<String> players;
  private String priorPlayer;

  /*
  * INVARIANTS:
  * - Must start with at least one player (protected by builder)
  * - Players cannot have the same String name (protected by builder)
  * - Make sure order of play can't be changed
  *
   */

  /**
   * Constructor for a Strict Coin Game
   *
   * @param board true if there is a coin and false if not
   * @param coins list of indices where there are coins
   * @param players list of unique player names
   * @param priorPlayer the player who went the prior turn
   */

  private StrictCoinGameModel(ArrayList<Boolean> board, ArrayList<Integer> coins,
                              ArrayList<String> players, String priorPlayer) {
    this.board = board;
    this.coins = coins;
    this.players = players;
    this.priorPlayer = priorPlayer;
  }

  /**
   * accessor for the coins ArrayList)
   *
   * @return the coins field
   */
  public ArrayList<Integer> getCoins() {
    return this.coins;
  }

  /**
   * Gets the size of the board (the number of squares)
   *
   * @return the board size
   */
  @Override
  public int boardSize() {
    return this.board.size();
  }

  /**
   * Gets the number of coins.
   *
   * @return the number of coins
   */
  @Override
  public int coinCount() {
    int count = 0;
    for (boolean x : board) {
      if (x) {
        count += 1;
      }
    }
    return count;
  }

  /**
   * Gets the (zero-based) position of coin number {@code coinIndex}.
   *
   * @param coinIndex which coin to look up
   * @return the coin's position
   * @throws IllegalArgumentException if there is no coin with the requested index
   */
  @Override
  public int getCoinPosition(int coinIndex) {
    try {
      this.coins.get(coinIndex);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("No coin at this index");
    }
    return this.coins.get(coinIndex);
  }

  /**
   * Returns whether the current game is over. The game is over if there are no valid moves.
   *
   * @return whether the game is over
   */
  @Override
  public boolean isGameOver() {
    for (int i = 0; i < this.coinCount(); i += 1) {
      if (!this.board.get(i)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the move of a coin from an original location {@code coinIndex}
   * to a new position {@code newPosition} is a valid move.
   *
   * @param coinIndex   which coin to move (numbered from the left)
   * @param newPosition where to move it to
   * @return whether the proposed move is valid
   */

  /**
   * Moves coin number {@code coinIndex} to position {@code newPosition}. Throws {@code
   * IllegalMoveException} if the requested move is illegal, which can happen in several ways: <p>
   * <ul> <li>There is no coin with the requested index. <li>The new position is occupied by another
   * coin. <li>There is some other reason the move is illegal, as specified by the concrete game
   * class. </ul> <p> Note that {@code coinIndex} refers to the coins as numbered from 0 to {@code
   * coinCount() - 1}, not their absolute position on the board. However, coins have no identity, so
   * if one coin passes another, their indices are exchanged. The leftmost coin is always coin 0,
   * the next leftmost is coin 1, and so on.
   *
   * @param coinIndex   which coin to move (numbered from the left)
   * @param newPosition where to move it to
   * @throws IllegalMoveException the move is illegal
   */
  @Override
  public void move(int coinIndex, int newPosition) {
    if (!this.isValidMove(coinIndex, newPosition)) {
      throw new IllegalMoveException("Not a proper move");
    }

    int oldPos = this.getCoinPosition(coinIndex);
    board.set(oldPos, false);
    board.set(newPosition, true);
    coins.set(coinIndex, newPosition);
    Collections.sort(this.coins);
    this.nextTurn();
  }

  /**
   * Overrides the builtin toString method.  Makes a string representation of the board with 'O' for
   * a coin and '-' for empty space.
   *
   * @return string representaion of the board
   */

  @Override
  public String toString() {
    String result = "";
    for (Boolean b : this.board) {
      if (b) {
        result += "O";
      } else {
        result += "-";
      }
    }
    return result;
  }

  /**
   * Checks if the move of a coin from an original location {@code coinIndex} to a new position
   * {@code newPosition} is a valid move.
   *
   * @param coinIndex which coin to move (numbered from the left)
   * @param newPos    where to move it to
   * @return whether the proposed move is valid
   */
  boolean isValidMove(int coinIndex, int newPos) {
    return coinIndex == 0 && newPos < this.getCoinPosition(coinIndex) && newPos >= 0 ||
            newPos < this.getCoinPosition(coinIndex) && newPos > getCoinPosition(coinIndex - 1);
  }

  @Override
  public String getWinner() {
    if (!this.isGameOver()) {
      throw new IllegalStateException("Game isn't over");
    }
    return this.priorPlayer;
  }

  /**
   * Checks to see if name is unique
   *
   * @param name the name to be checked
   * @return true if name is unique
   * @throws IllegalArgumentException if name isn't unique
   */

  private Boolean validPlayer(String name) {
    if (this.players.contains(name)) {
      throw new IllegalArgumentException(name + " is already taken");
    }
    else {
      return true;
    }
  }

  @Override
  public void addPlayers(int numOfTurns, String... names) {
    if (numOfTurns < 0 || numOfTurns > this.players.size() - 1) {
      throw new IllegalArgumentException("Your # of turns doesn't make sense");
    }
    if (numOfTurns == 0) {
      for (String n : names) {
        if (this.validPlayer(n)) {
          this.players.add(n);
        }
      }
    }
    else {
      for (String n : names) {
        this.validPlayer(n);
      }
      this.players.addAll(numOfTurns, Arrays.asList(names));
    }
  }

  @Override
  public String whoseTurn() {
    return this.players.get(0);
  }

  /**
   * Modifies the game to reflect a new turn
   *
   * SIDE EFFECTS: priorPlayer gets the name of who just moved the next person in players gets moved
   * to the back
   */
  private void nextTurn() {
    this.priorPlayer = this.players.get(0);
    this.players.remove(0);
    this.players.add(priorPlayer);
  }

  /**
   * Constructs a builder for configuring and then creating a game model
   * instance. 
   *
   * @return the new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builds a {@link StrictCoinGameModel}, allowing the client to configure parameters. This is an
   * instance of the <em>builder pattern</em>.
   */
  public static final class Builder {
    private String template = "--OO--";
    private ArrayList<Boolean> board = this.stringToArrayList(template);
    private ArrayList<Integer> coins = this.boardToCoins(template);
    private ArrayList<String> players = new ArrayList<>();

    /**
     * Given a string representation of the CoinGameModel converts it into an ArrayList of Booleans
     * with true representing a coin and false representing nothing
     *
     * @param str the representing the CoinGameModel
     * @return ArrayList representation of the board
     * @throws IllegalArgumentException if {@code str} has an improper character
     */
    private ArrayList<Boolean> stringToArrayList(String str) {
      ArrayList<Boolean> result = new ArrayList();
      for (int i = 0; i < str.length(); i += 1) {
        if (str.charAt(i) == '-') {
          result.add(false);
        } else if (str.charAt(i) == 'O') {
          result.add(true);
        } else {
          throw new IllegalArgumentException("Improper coin game");
        }
      }
      return result;
    }

    /**
     * Creates a list of the positions of the coins in the board ArrayList
     *
     * @return an ArrayList of Integers to represent coins
     */
    private ArrayList<Integer> boardToCoins(String str) {
      ArrayList<Integer> result = new ArrayList();
      int i = 0;
      for (char c : str.toCharArray()) {
        if (c == 'O') {
          result.add(new Integer(i));
        }
        i += 1;
      }
      return result;
    }

    /**
     * Adds name to this Builder and returns it
     *
     * @param names are the players to be added. If no names are specified, a default is made
     * @throws IllegalArgumentException if a name is already in the list
     */

    public Builder addPlayers(String... names) {
      this.players.clear();
      for (String n : names) {
        // If a name is repeated, throw an illegal argument exception
        if (this.players.contains(n)) {
          throw new IllegalArgumentException("Name " + n + " is already taken");
        } else {
          // If everything is all good, add name
          this.players.add(n);
        }
      }
      return this;
    }

    /**
     * Changes the game board
     *
     * @param template is the new board template
     * @return this Builder with the new Template
     */
    public Builder changeBoard(String template) {
      this.template = template;
      this.board = this.stringToArrayList(template);
      this.coins = this.boardToCoins(template);
      return this;
    }

    /**
     * Builds a StrictCoinGameModel
     *
     * @return (@code StrictCoinGameModel) with either default fields or modified options
     */
    public StrictCoinGameModel build() {
      // If no names are added, make a default player
      if (this.players.size() == 0) {
        this.players.add("DefaultPlayer");
      }
      return new StrictCoinGameModel(this.board, this.coins,
              this.players, this.players.get(0));
    }
  }
}
