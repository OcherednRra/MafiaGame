import java.util.Scanner;

public class App {
    public static void main(String[] args)
    {
        DeceptionGame game = new DeceptionGame("whovian", "OcherednRra", "yourwaifu");
        game.startGame();

        System.out.println(game.getListOfPlayers());
    }
}
