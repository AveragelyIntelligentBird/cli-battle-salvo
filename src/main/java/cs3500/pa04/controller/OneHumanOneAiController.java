package cs3500.pa04.controller;

import cs3500.pa04.controller.lockbox.ShotsLockboxWriter;
import cs3500.pa04.controller.viewctrl.ViewController;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.game.PlayerBoardsState;
import cs3500.pa04.model.player.AiPlayer;
import cs3500.pa04.model.player.HumanPlayer;

/**
 * Implementation of controller for running an interactive session with one human player and one AI
 */
public class OneHumanOneAiController extends AbstractLocalController {
  private final ShotsLockboxWriter userShotsLockbox;

  /**
   * Constructor for one-player interactive sessions with LocalController
   *
   * @param viewController viewController for UI
   * @param playerA Interactive human player to play in this session
   * @param gameStateA state of PlayerA's boards
   * @param userShotsLockbox lockbox to transfer input to PlayerA
   * @param playerB Non-interactive ai player to play session
   * @param gameStateB state of PlayerB's boards
   */
  public OneHumanOneAiController(ViewController viewController,
                                 HumanPlayer playerA, PlayerBoardsState gameStateA,
                                 ShotsLockboxWriter userShotsLockbox,
                                 AiPlayer playerB, PlayerBoardsState gameStateB) {
    super(viewController, playerA, gameStateA, playerB, gameStateB);
    this.userShotsLockbox = userShotsLockbox;
  }

  /**
   * Preprocesses players before the salvo stage
   *
   * @throws Exception when critical io error occurs, handled by Driver
   */
  protected void preProcPlayers() throws Exception {
    viewController.displayPlayerPovBoards(
        gameStateA.getPlayerBoardState(),
        gameStateA.getOpponentBoardState());

    int numShots = gameStateA.numberOfAllowedShots();
    userShotsLockbox.recieveUserShots(viewController.getNumShots(numShots));
  }

  /**
   * Wraps the game and displays end of game message
   *
   * @throws Exception when critical io error occurs, handled by Driver
   */
  protected void wrapTheGame() throws Exception {
    if (gameStateA.playerHasSurvivingShips() && !gameStateB.playerHasSurvivingShips()) {
      viewController.displayEndOfGameMsg(GameResult.WIN);
    } else if (!gameStateA.playerHasSurvivingShips() && gameStateB.playerHasSurvivingShips()) {
      viewController.displayEndOfGameMsg(GameResult.LOSE);
    } else {
      viewController.displayEndOfGameMsg(GameResult.DRAW);
    }
  }
}
