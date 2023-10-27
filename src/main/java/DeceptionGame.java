import java.io.File;
import java.util.*;

public class DeceptionGame {

    int playersNumber;
    ArrayList<String> discordTagsOfPlayers;
    ArrayList<String> roles = new ArrayList<>();
    static ArrayList<Player> listOfPlayers = new ArrayList<>();
    String gameMode;
    static int clueCardsNumber = getClueCardsNumber();
    static int weaponCardsNumber = getWeaponCardsNumber();
    int randomCard;
    static Random random = new Random();
    static ArrayList<Card> cardsBlackList = new ArrayList<>();

    DeceptionGame(String... players)
    {
        this.playersNumber = players.length;
        this.discordTagsOfPlayers = new ArrayList<>(Arrays.asList(players));
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

    public static class Player
    {
        String name;
        String role;
        ArrayList<Card> clueHand = new ArrayList<>();
        ArrayList<Card> weaponHand = new ArrayList<>();

        Player(String name, String role)
        {
            this.name = name;
            this.role = role;
            for (int i = 0; i < 4; i++)
            {
                this.clueHand.add(getRandomClueCard());
                this.weaponHand.add(getRandomWeaponCard());
            }
        }

        public String toString()
        {
            return "\nÈãðîê " + this.name + " - " + this.role + ".\n" +
                    "Clue: " + this.clueHand.toString() + "\n" +
                    "Weapon: " + this.weaponHand.toString() + "\n";
        }
    }

    public Player createPlayer(String name, String role)
    {
        return new Player(name, role);
    }

    public static Card getRandomClueCard()
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

    public static Card getRandomWeaponCard()
    {
        Card card;
        do
        {
            card = new Card(random.nextInt(weaponCardsNumber) + 1 + ".png", "weapon");
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
        this.roles.add("criminologist");
        this.roles.add("killer");

        if (playersNumber > 5)
        {
            this.roles.add("accomplice");
            this.roles.add("witness");
        }

        for (int i = 0; i < playersNumber - (playersNumber > 5 ? 4 : 2); i++)
            this.roles.add("investigator");

        return (playersNumber <= 5) ? "standard" : "extended";
    }

    public String getGameMode()
    {
        return "Game Mode: " + gameMode + "\n"
                + "Number of players: " + playersNumber;
    }

    public static int getClueCardsNumber()
    {
        return Objects.requireNonNull(new File("src\\main\\java\\clue").listFiles()).length;
    }

    public static int getWeaponCardsNumber()
    {
        return Objects.requireNonNull(new File("src\\main\\java\\weapon").listFiles()).length;
    }

    public static String getListOfPlayers()
    {
        return  listOfPlayers.toString();
    }

    public void startGame()
    {
        this.gameMode = this.setGameMode();
        Collections.shuffle(this.roles);

        for (int i = 0; i < playersNumber; i ++)
            listOfPlayers.add(new Player(this.discordTagsOfPlayers.get(i), this.roles.get(i)));
    }
}