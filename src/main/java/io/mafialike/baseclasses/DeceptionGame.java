package io.mafialike.baseclasses;

import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.util.*;

public class DeceptionGame {

    private static final int clueCardsNumber = Objects.requireNonNull(new File("src\\main\\resources\\images\\clue").listFiles()).length;
    private static final int weaponCardsNumber = Objects.requireNonNull(new File("src\\main\\resources\\images\\weapon").listFiles()).length;
    private static final ArrayList<Card> inGameCardsList = new ArrayList<>();

    ArrayList<User> listOfUsers;

    int playersNumber;
    ArrayList<String> discordTagsOfPlayers = new ArrayList<>();
    ArrayList<String> idsOfPlayers = new ArrayList<>();
    ArrayList<String> roles = new ArrayList<>();
    static ArrayList<Player> listOfPlayers = new ArrayList<>();
    String gameMode;

    public DeceptionGame(ArrayList<User> listOfUsers)
    {
        this.listOfUsers = listOfUsers;

        for (User user : listOfUsers)
        {
            this.discordTagsOfPlayers.add(user.getEffectiveName());
            this.idsOfPlayers.add(user.getId());
        }
    }

    public static ArrayList<Card> getInGameCardsList()
    {
        return inGameCardsList;
    }

    public int getPlayersNumber()
    {
        return this.listOfUsers.size();
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

    public static int getWeaponCardsNumber()
    {
        return weaponCardsNumber;
    }

    public static int getClueCardsNumber()
    {
        return clueCardsNumber;
    }

    public static ArrayList<Player> getListOfPlayers()
    {
        return listOfPlayers;
    }

    public void startGame()
    {
        this.gameMode = this.setGameMode();
        Collections.shuffle(this.roles);

        for (int i = 0; i < playersNumber; i ++)
            listOfPlayers.add(new Player(this.discordTagsOfPlayers.get(i), this.roles.get(i), this.idsOfPlayers.get(i)));
    }
}