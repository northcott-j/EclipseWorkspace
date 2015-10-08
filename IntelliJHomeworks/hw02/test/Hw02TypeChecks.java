////
//// DO NOT MODIFY THIS FILE
////
//// You don't need to submit it or even run it, but you should make sure it
//// compiles. Further explanation appears below.
////

import cs3500.hw02.CoinGameModel;
import cs3500.hw02.StrictCoinGameModel;
import cs3500.hw02.LaxCoinGameModel;

/**
 * This class is provided to check that your code implements the expected API.
 * If your code compiles with an unmodified version of this class, then it
 * very likely will also compile with the tests that we use to evaluate
 * your code.
 */
public class Hw02TypeChecks {
  CoinGameModel strict = new StrictCoinGameModel("");
  CoinGameModel lax = new LaxCoinGameModel("");

  private Hw02TypeChecks() { throw new RuntimeException("uninstantiable"); }
}
