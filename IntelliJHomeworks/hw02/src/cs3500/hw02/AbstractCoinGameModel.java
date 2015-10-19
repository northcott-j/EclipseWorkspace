package cs3500.hw02;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents the Abstract version of a Coin Game Model
 */
abstract class AbstractCoinGameModel implements CoinGameModel {
  /**
   * Constructs a {@link CoinGameModel} in a manner selected by concrete subclasses of this class.
   *
   * @param board the coin board as a string
   * @param coinLocs locations of coins in the string
   * @return the new {@code CoinGameModel}
   */
  private String board;
  private ArrayList<Integer> coinLocs;

  protected AbstractCoinGameModel(String board) {
    ensureBoard(board);
    this.board = board;
    this.coinLocs = findCoins(board);
  }


  /**
   * Checks to see whether or not the concrete class is strict
   *
   * @return boolean to show true or false
   */
  protected abstract boolean isStrict();

  /**
   * Finds all of the coins in the string
   *
   * @param board the board string template
   * @return an ArrayList of integers of coin locations
   */
  private ArrayList<Integer> findCoins(String board) {
    ArrayList<Integer> coinLocs = new ArrayList<>();
    for (Integer i = 0; i < board.length(); i += 1) {
      if (board.charAt(i) == 'O') {
        coinLocs.add(i);
      }
    }
    return coinLocs;
  }

  /**
   * Makes sure the board String is valid
   *
   * @param board the board string template
   * @throws IllegalArgumentException if board is invalid
   */
  private void ensureBoard(String board) {
    if (board.equals("")) {
      throw new IllegalArgumentException("Must be at least '-' in length");
    }
    for (int i = 0; i < board.length(); i += 1) {
      if (board.charAt(i) == 'O' || board.charAt(i) == '-') {

      } else {
        throw new IllegalArgumentException("Not a valid board");
      }
    }
  }


  /**
   * Gets the size of the board (the number of squares)
   *
   * @return the board size
   */
  @Override
  public int boardSize() {
    // Counts items in the string - must be - or O
    return this.board.length();
  }

  /**
   * Gets the number of coins.
   *
   * @return the number of coins
   */
  @Override
  public int coinCount() {
    return this.coinLocs.size();
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
    if (coinIndex >= this.coinCount() || coinIndex < 0) {
      throw new IllegalArgumentException("No coin at this index");
    }

    return coinLocs.get(coinIndex);
  }

  /**
   * Returns whether the current game is over. The game is over if there are no valid moves.
   *
   * @return whether the game is over
   */
  @Override
  public boolean isGameOver() {
   /*
    * Creates the final state of the coin Array
    * Coins indices must start at 0 and be in order
    * up to the number of coins - 1 because this means
    * all coins are to the left of the board without space
    * to move
    * */
    ArrayList<Integer> finalCoinState = new ArrayList<>();
    for (Integer i = 0; i < this.coinCount(); i += 1) {
      finalCoinState.add(i);
    }
    return
            // Checks if there are more than 0 coins
            this.coinCount() == 0 ||
                    // Coins must match final Coin State
                    this.coinLocs.equals(finalCoinState);

  }

  /**
   * @param coinIndex   index of coin being moved
   * @param newPosition the position the coin is being moved to
   * @return a boolean true if the move is valid
   * @throws IllegalMoveException if move is invalid
   */

  private boolean validMove(int coinIndex, int newPosition) {
    // Checks for valid coinIndex
    try {
      this.coinLocs.get(coinIndex);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalMoveException("Invalid coinIndex");
    }
    // Out of bounds check
    if (newPosition < 0 || newPosition > this.boardSize()) {
      throw new IllegalMoveException("This move is out of bounds");
    }
    // Checks to see if coinLocs already contains a coin at newPosition
    else if (this.coinLocs.indexOf(newPosition) != -1) {
      throw new IllegalMoveException("Already a coin at that loc");
    }
    // Strict Model case
    else if (this.isStrict()) {
      // Checks to see if coin is skipping another

      // Is the new position to the right of the coin?
      if (newPosition > this.getCoinPosition(coinIndex)) {
        for (int i = coinIndex; i <= newPosition; i += 1) {
          // If new position is greater that a coin to the right of moving coin
          if (newPosition > this.getCoinPosition(i)) {
            throw new IllegalMoveException("Skipping a coin");
          }
        }
      } else {
        // For coins to the left of the coin
        for (int i = 0; i < coinIndex; i += 1) {
          // Is new position to the left of another coin
          if (newPosition < this.getCoinPosition(i)) {
            throw new IllegalMoveException("Skipping a coin");
          }
        }
      }
      return true;
    } else {
      return true;
    }
  }


  /**
   * Moves coin number {@code coinIndex} to position {@code newPosition}. Throws {@code
   * IllegalMoveException} if the requested move is illegal, which can happen in several ways:
   *
   * <ul> <li>There is no coin with the requested index.
   * <li>The new position is occupied by another coin.
   * <li>Can't be placed out of bounds</li>
   * <li> Strict model game can't skip over a coin</li>
   * </ul>
   *
   * Note that {@code coinIndex} refers to the coins as
   * numbered from 0 to {@code coinCount() - 1},
   * not their absolute position on the board.
   * However, coins have no identity, so if one coin
   * passes another, their indices are exchanged. The leftmost coin is always coin 0, the next
   * leftmost is coin 1, and so on.
   *
   * @param coinIndex   which coin to move (numbered from the left)
   * @param newPosition where to move it to
   * @throws IllegalMoveException the move is illegal
   */
  @Override
  public void move(int coinIndex, int newPosition) {
    if (this.validMove(coinIndex, newPosition)) {
      // Removes old coin
      this.coinLocs.remove(coinIndex);
      // Adds in newPosition as a coin
      this.coinLocs.add(newPosition);
      // Makes sure coin indices are correct by sorting them
      Collections.sort(coinLocs);
      // Update board string
      String newBoard = "";
      for (int i = 0; i < this.boardSize(); i += 1) {
        if (this.coinLocs.contains(i)) {
          newBoard = newBoard + "O";
        } else {
          newBoard = newBoard + "-";
        }
      }
      this.board = newBoard;
    }
  }

  /**
   * Returns the Concrete game model as a string
   *
   * @return string representing board
   */
  @Override
  public String toString() {
    return this.board;
  }
}
