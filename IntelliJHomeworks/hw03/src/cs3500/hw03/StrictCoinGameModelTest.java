package cs3500.hw03;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for StrictCoinGameModel
 * Created by Jonathan on 10/19/2015.
 */
public class StrictCoinGameModelTest {
  StrictCoinGameModel.Builder noNames = new StrictCoinGameModel.Builder();
  StrictCoinGameModel.Builder oneName = new StrictCoinGameModel.Builder("Joe");
  StrictCoinGameModel.Builder manyNames = new StrictCoinGameModel.Builder("Sally", "Joe", "James");

  StrictCoinGameModel defaultGame = noNames.build();
  StrictCoinGameModel modelOne = oneName.build();
  StrictCoinGameModel modelTwo = manyNames.build();

  /*
  * TEST:
  * - Invariant Protection
  * - Whose turn
  * - Who won, error and normal
  * - Add players
  *
   */

  // Invariant Tests
  @Test(expected = IllegalArgumentException.class)
  // Same name
  public void sameNameTest() {
    StrictCoinGameModel.Builder dupNames = new StrictCoinGameModel.Builder("Joe", "Joe");
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

  // getWinner() Tests
  @Test(expected = IllegalStateException.class)
  // Not over
  public void getWinner1() {
    modelOne.getWinner();
  }

  @Test
  public void getWinner2() {
    // Moves until game winning state
    assertEquals("Sally",
            modelTwo.getWinner());
  }

  // addPlayers() Tests
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
  }
}
