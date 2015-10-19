package cs3500.hw02;

/**
 * Lax coin game model concrete class
 */
public class LaxCoinGameModel extends AbstractCoinGameModel {
  public LaxCoinGameModel(String board) {
    super(board);
  }


  /**
   * Checks to see whether or not the concrete Model is strict
   *
   * @return a boolean false because this is lax
   */
  protected boolean isStrict() {
    return false;
  }


}
