package cs3500.pa04.controller;

import cs3500.pa04.controller.viewctrl.ViewController;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.PlayerBoardsState;
import cs3500.pa04.model.game.ShipType;
import cs3500.pa04.model.player.Player;
import java.util.ArrayList;
import java.util.Map;

/**
 * Core controller class, facilitates interaction between model and view
 */
public abstract class AbstractLocalController implements GameController {
  protected final ViewController viewController;
  protected final Player playerA; // Is interactive in interactive sessions
  protected final PlayerBoardsState gameStateA;
  protected final Player playerB;
  protected final PlayerBoardsState gameStateB;

  /**
   * Constructor for non-interactive sessions with LocalController
   *
   * @param viewController viewController for UI
   * @param playerA One of the two players to play the non-interactive session
   * @param gameStateA state of PlayerA's boards
   * @param playerB One of the two players to play the non-interactive session
   * @param gameStateB state of PlayerB's boards
   */
  public AbstractLocalController(ViewController viewController,
                                 Player playerA, PlayerBoardsState gameStateA,
                                 Player playerB, PlayerBoardsState gameStateB) {
    this.viewController = viewController;
    this.playerA = playerA;
    this.gameStateA = gameStateA;
    this.playerB = playerB;
    this.gameStateB = gameStateB;
  }

  /**
   * Main game runner, repeats Salvo stage until one of the players no longer can play
   *
   * @throws Exception when critical io error occurs, handled by Driver
   */
  public void run() throws Exception {
    setupPlayers();

    while (gameStateA.playerHasSurvivingShips() && gameStateB.playerHasSurvivingShips()) {
      preProcPlayers();

      ArrayList<Coord> playerAshots = (ArrayList<Coord>) playerA.takeShots();
      ArrayList<Coord> playerBshots = (ArrayList<Coord>) playerB.takeShots();

      ArrayList<Coord> succPlayerAshots = (ArrayList<Coord>) playerB.reportDamage(playerAshots);
      ArrayList<Coord> succPlayerBshots = (ArrayList<Coord>) playerA.reportDamage(playerBshots);

      playerA.successfulHits(succPlayerAshots);
      playerB.successfulHits(succPlayerBshots);
    }

    wrapTheGame();
  }

  /**
   * Sets up the boards and prepares players for the game
   *
   * @throws Exception when critical io error occurs, handled by Driver
   */
  private void setupPlayers() throws Exception {
    viewController.displayWelcomeMessage();
    int[] boardDims = viewController.getBoardDimensions();
    Map<ShipType, Integer> specs = viewController.getShipSpecifications();

    playerA.setup(boardDims[0], boardDims[1], specs);
    playerB.setup(boardDims[0], boardDims[1], specs);
  }

  /**
   * Preprocesses players before the salvo stage
   */
  abstract void preProcPlayers() throws Exception;

  /**
   * Wraps the game and displays end of game message
   *
   * @throws Exception when critical io error occurs, handled by Driver
   */
  abstract void wrapTheGame() throws Exception;

}
