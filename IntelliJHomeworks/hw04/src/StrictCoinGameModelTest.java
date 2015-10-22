import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for StrictCoinGameModel Created by Jonathan on 10/19/2015.
 */
public class StrictCoinGameModelTest {
  StrictCoinGameModel defaultGame = StrictCoinGameModel.builder().build();
  StrictCoinGameModel modelOne = StrictCoinGameModel.builder().addPlayers("Joe").build();
  StrictCoinGameModel modelTwo = StrictCoinGameModel.builder()
          .addPlayers("Sally", "Joe", "James").build();
  CoinGameModel strict1 = StrictCoinGameModel.builder().changeBoard("-OOO-").build();
  CoinGameModel strict2 = StrictCoinGameModel.builder().changeBoard("---").build();
  CoinGameModel strict3 = StrictCoinGameModel.builder().changeBoard("OOO").build();
  CoinGameModel strict4 = StrictCoinGameModel.builder().changeBoard("OO-").build();
  CoinGameModel strict5 = StrictCoinGameModel.builder().changeBoard("O-O").build();

  // Invariant Tests
  @Test(expected = IllegalArgumentException.class)
  // Same name
  public void sameNameTest() {
    StrictCoinGameModel.Builder dupNames =
            new StrictCoinGameModel.Builder().addPlayers("Joe", "Joe");
  }

  @Test
  public void oneNameTest() {
    assertEquals("DefaultPlayer",
            defaultGame.whoseTurn());
  }

  // whoseTurn() Tests
  @Test
  public void whoseTurn1() {
    assertEquals("Joe",
            modelOne.whoseTurn());
  }

  @Test
  public void whoseTurn2() {
    assertEquals("Sally",
            modelTwo.whoseTurn());
  }


// TODO: Write getWinner() Tests
/*  // getWinner() Tests
  @Test(expected = IllegalStateException.class)
  // Not over
  public void getWinner1() {
    modelOne.getWinner();
  }

  @Test
  public void getWinner2() {
    assertEquals("Sally",
            modelTwo.getWinner());
  }*/

  // TODO: Write addPlayers() Tests
/*  // addPlayers() Tests
  @Test
  public void addPlayers1() {
    modelOne.addPlayers();
    assertEquals("Joe",
            modelOne.whoseTurn());
  }

  @Test
  public void addPlayers2() {
    modelTwo.addPlayers("Jack", "Kevin");
    assertEquals("Sally",
            modelTwo.whoseTurn());
  }

  @Test(expected = IllegalArgumentException.class)
  // Same name
  public void addPlayers3() {
    modelOne.addPlayers("Joe");
  }*/


  // ensureBoard tests
  @Test
  public void ensureBoard1() {
    assertEquals(8,
            strict1.boardSize() + strict1.coinCount());
  }

  @Test(expected = IllegalArgumentException.class)
  // Exception case
  public void ensureBoard3() {
    CoinGameModel fail = StrictCoinGameModel.builder().changeBoard("--xx").build();
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

  // TODO: Fix Game over
/*  // isGameOver tests
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
  }*/

  // Move test
  @Test
  // Strict move
  public void move1() {
    strict5.move(1, 1);
    assertEquals(strict4.toString(), strict5.toString());
  }


  @Test(expected = IllegalArgumentException.class)
  // Exception case - invalid coin
  public void move3() {
    strict1.move(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
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
