import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class DeceptionGame {

    int playersNumber;
    String gameMode;
    static int clueCardsNumber = getClueCardsNumber();
    static int weaponCardsNumber = getWeaponCardsNumber();
    int randomCard;
    static Random random = new Random();
    static ArrayList<Card> cardsBlackList = new ArrayList<>();

    DeceptionGame(int players)
    {
        this.playersNumber = players;
        this.gameMode = this.setGameMode();
    }

    public static class Card
    {
        String title;
        String type;
        static Random random = new Random();

        Card(String title, String type) {
            this.title = title;
            this.type = type;
        }

        public Card getRandomWeaponCard()
        {
            return new Card(random.nextInt(DeceptionGame.weaponCardsNumber) + 1 + ".png", "weapon");
        }

        public String toString() {
            return this.type + " card " + this.title;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Card card = (Card) obj;
            return Objects.equals(title, card.title) &&
                    Objects.equals(type, card.type);
        }
    }

    public Card getRandomClueCard()
    {
        Card card;
        do
        {
            card = new Card(random.nextInt(clueCardsNumber) + 1 + ".png", "clue");
        }
        while (cardsBlackList.contains(card));
        cardsBlackList.add(card);
        return card;
    }




    public String getCardsBlackList()
    {
        return cardsBlackList.toString();
    }

    public int getPlayersNumber()
    {
        return playersNumber;
    }

    public String setGameMode()
    {
        if (playersNumber <= 5) return "standard";
        else return "extended";
    }

    public String getGameMode()
    {
        return gameMode;
    }

    public static int getClueCardsNumber()
    {
        return Objects.requireNonNull(new File("src\\clue").listFiles()).length;
    }

    public static int getWeaponCardsNumber()
    {
        return Objects.requireNonNull(new File("src\\weapon").listFiles()).length;
    }

    public void startGame()
    {
        System.out.println("Игра началась!");
    }
}