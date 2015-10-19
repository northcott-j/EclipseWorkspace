////
//// DO NOT MODIFY THIS FILE
////
//// You don't need to submit it or even run it, but you should make sure it
//// compiles. Further explanation appears below.
////

import org.junit.Test;

import cs3500.hw02.CoinGameModel;
import cs3500.hw02.StrictCoinGameModel;
import cs3500.hw02.LaxCoinGameModel;
import static org.junit.Assert.*;

/**
 * This class is provided to check that your code implements the expected API.
 * If your code compiles with an unmodified version of this class, then it
 * very likely will also compile with the tests that we use to evaluate
 * your code.
 */
public class Hw02TypeChecks {
  CoinGameModel strict1 = new StrictCoinGameModel("-OOO-");
  CoinGameModel strict2 = new StrictCoinGameModel("---");
  CoinGameModel strict3 = new StrictCoinGameModel("OOO");
  CoinGameModel strict4 = new StrictCoinGameModel("OO-");
  CoinGameModel strict5 = new StrictCoinGameModel("O-O");

  CoinGameModel lax1 = new LaxCoinGameModel("-OO-");
  CoinGameModel lax2 = new LaxCoinGameModel("OO--");

// ensureBoard tests
  @Test
  public void ensureBoard1() {
    assertEquals(8,
            strict1.boardSize() + strict1.coinCount());
  }

  @Test(expected = IllegalArgumentException.class)
  // Exception case
  public void ensureBoard2() {
    CoinGameModel fail = new StrictCoinGameModel("");
  }

  @Test(expected = IllegalArgumentException.class)
  // Exception case
  public void ensureBoard3() {
    CoinGameModel fail = new StrictCoinGameModel("--xx");
  }

  // boardSize() test
  @Test
  // Regular Template
  public void boardSize1() {
    assertEquals(5,
            strict1.boardSize());
  }

  @Test
  //No coins
  public void boardSize2() {
    assertEquals(3,
            strict2.boardSize());
  }
  @Test
  // All coins
  public void boardSize3() {
    assertEquals(3,
            strict3.boardSize());
  }

  // coinCount() test
  @Test
  // Random board
  public void coinCount1() {
    assertEquals(3,
            strict1.coinCount());
  }

  @Test
  // No coins
  public void coinCount2() {
    assertEquals(0,
            strict2.coinCount());
  }

  @Test
  // All coins
  public void coinCount3() {
    assertEquals(3,
            strict3.coinCount());
  }

  // Test getCoinPosition
  @Test(expected = IllegalArgumentException.class)
  // Exception case
  public void coinPosition1() {
    strict1.getCoinPosition(3);
  }

  @Test(expected = IllegalArgumentException.class)
  // Exception case
  public void coinPosition2() {
    strict1.getCoinPosition(-1);
  }

  @Test
  // Valid case
  public void coinPosition3() {
    assertEquals(strict1.getCoinPosition(0), 1);
  }

  // isGameOver tests
  @Test
  // All coins
  public void isGameOver1() {
    assertTrue(strict3.isGameOver());
  }

  @Test
  // Not over
  public void isGameOver2() {
    assertFalse(strict1.isGameOver());
  }

  @Test
  // One space left
  public void isGameOver3() {
    assertTrue(strict4.isGameOver());
  }

  @Test
  // No coins
  public void isGameOver4() {
    assertTrue(strict2.isGameOver());
  }

  // Move test
  @Test
  // Strict move
  public void move1() {
    strict5.move(1, 1);
    assertEquals(strict4.toString(), strict5.toString());
  }

  @Test
  // Lax move
  public void move2() {
    lax1.move(1, 0);
    assertEquals(lax1.toString(), lax2.toString());
  }

  @Test(expected = CoinGameModel.IllegalMoveException.class)
  // Exception case - invalid coin
  public void move3() {
    strict1.move(-1, 0);
  }

  @Test(expected = CoinGameModel.IllegalMoveException.class)
  // Exception case - out of bounds left
  public void move4() {
    strict1.move(0, -1);
  }

  @Test(expected = CoinGameModel.IllegalMoveException.class)
  // Exception case - out of bounds right
  public void move5() {
    strict1.move(0, 5);
  }

  @Test(expected = CoinGameModel.IllegalMoveException.class)
  // Exception case - coins on top of each other
  public void move6() {
    strict1.move(1, 1);
  }

  @Test(expected = CoinGameModel.IllegalMoveException.class)
  // Exception case - skipping a coin
  public void move7() {
    strict1.move(1, 0);
  }
}
