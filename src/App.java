import java.util.Scanner;

public class App {
    public static void main(String[] args)
    {
        DeceptionGame game = new DeceptionGame(8);
        System.out.println(game.getCardsBlackList());
        System.out.println(game.getRandomClueCard());
        System.out.println(game.getRandomClueCard());
        System.out.println(game.getRandomClueCard());
        System.out.println(game.getRandomClueCard());
        System.out.println(game.getCardsBlackList());


    }
}
