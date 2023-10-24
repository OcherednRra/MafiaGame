import java.util.Scanner;

public class App {
    public static void main(String[] args)
    {
        DeceptionGame game = new DeceptionGame(8);
        game.startGame();

        System.out.println(game.getListOfPlayers());
    }
}
