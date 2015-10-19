package cs3500.hw02;

/**
 * Strict coin game model concrete class
 */
public class StrictCoinGameModel extends AbstractCoinGameModel {
  public StrictCoinGameModel(String board) {
    super(board);
  }


  /**
   * Checks to see whether or not the concrete Model is strict
   *
   * @return a boolean true because this is strict
   */
  protected boolean isStrict() {
    return true;
  }

}
