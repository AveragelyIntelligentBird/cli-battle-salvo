package cs3500.pa04;

import cs3500.pa04.controller.GameController;
import cs3500.pa04.controller.ProxyController;
import cs3500.pa04.controller.json.GameType;
import cs3500.pa04.controller.viewctrl.CliViewController;
import cs3500.pa04.controller.viewctrl.ViewController;
import cs3500.pa04.model.game.PlayerBoards;
import cs3500.pa04.model.player.HuntDestroyAiPlayer;
import cs3500.pa04.view.AppendableOutCh;
import cs3500.pa04.view.GameView;
import cs3500.pa04.view.ReadableInCh;
import cs3500.pa04.view.UiReader;
import cs3500.pa04.view.UiWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    UiReader input = new ReadableInCh(new InputStreamReader(System.in));
    UiWriter output = new AppendableOutCh(System.out);
    GameView view = new GameView(input, output);
    ViewController viewController = new CliViewController(view);

    PlayerBoards aiBoards = new PlayerBoards(new Random().nextInt());
    HuntDestroyAiPlayer huntDestroyAi = new HuntDestroyAiPlayer("Hunt Destroy AI", aiBoards,
        new Random().nextInt());

    try {
      Socket server = new Socket("0.0.0.0", 35001);
      GameController runner =
          new ProxyController(viewController, huntDestroyAi, GameType.SINGLE, server);

      runner.run();
    } catch (IOException e) {
      System.out.println("Failed to establish the connection."
          + " Shutting down the application...");
    } catch (Exception e) {
      System.out.println("Unexpected error encountered when running the program."
          + " Shutting down the application...");
    }
  }
}