package cs3500.pa04.controller;

import cs3500.pa04.controller.viewctrl.ViewController;
import cs3500.pa04.model.game.PlayerBoardsState;
import cs3500.pa04.model.player.AiPlayer;

/**
 * Implementation of controller for running a non-interactive session with two AIs
 */
public class TwoAisController extends AbstractLocalController {
  /**
   * Constructor for non-interactive sessions with LocalController
   *
   * @param viewController viewController for UI
   * @param playerA One of the two players to play the non-interactive session
   * @param gameStateA state of PlayerA's boards
   * @param playerB One of the two players to play the non-interactive session
   * @param gameStateB state of PlayerB's boards
   */
  public TwoAisController(ViewController viewController,
                          AiPlayer playerA, PlayerBoardsState gameStateA,
                          AiPlayer playerB, PlayerBoardsState gameStateB) {
    super(viewController, playerA, gameStateA, playerB, gameStateB);
  }

  /**
   * Preprocesses players before the salvo stage
   */
  protected void preProcPlayers() { }

  /**
   * Wraps the game and displays end of game message
   *
   * @throws Exception when critical io error occurs, handled by Driver
   */
  protected void wrapTheGame() throws Exception {
    if (gameStateA.playerHasSurvivingShips() && !gameStateB.playerHasSurvivingShips()) {
      viewController.displayMsg(playerA.name()
          + " won by destroying all ships of " + playerB.name() + ".");
    } else if (!gameStateA.playerHasSurvivingShips() && gameStateB.playerHasSurvivingShips()) {
      viewController.displayMsg(playerB.name()
          + " won by destroying all ships of " + playerA.name() + ".");
    } else {
      viewController.displayMsg(playerA.name() + " and " + playerB.name()
          + " tied by destroying each other simultaneously.");
    }
  }
}
