package io.mafialike.baseclasses;

import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DeceptionGame {

    private static final int clueCardsNumber = getFilesNumber("src/main/resources/images/clue");
    private static final int weaponCardsNumber = getFilesNumber("src/main/resources/images/weapon");

    ArrayList<User> listOfUsers;

    int playersNumber;
    ArrayList<String> roleList = new ArrayList<>();
    ArrayList<Player> playersList = new ArrayList<>();
    ArrayList<User> userList;
    static ArrayList<Card> inGameCardsList = new ArrayList<>();
    String gameMode;

    public DeceptionGame(ArrayList<User> userList) {
        clearTempFiles();
        this.userList = userList;
    }

    public void startGame() {
//        if (this.userList.size() < 4) {
//            throw new IllegalArgumentException("Недостаточно игроков");
//        }

        inGameCardsList.clear();
        setGameMode();
        setRolesList();
        createPlayers();
    }

    private void createPlayers() {
        this.playersList.clear();
        for (int i = 0; i < playersNumber; i ++)
            playersList.add(
                    new Player(
                            userList.get(i).getName(),
                            this.roleList.get(i),
                            userList.get(i).getId()
                    )
            );
    }

    private void setRolesList() {
        ArrayList<String> roleList = new ArrayList<>();

        roleList.add("criminologist");
        roleList.add("killer");

        if (playersNumber > 5)
        {
            roleList.add("accomplice");
            roleList.add("witness");
        }

        for (int i = 0; i < playersNumber - (playersNumber > 5 ? 4 : 2); i++)
            roleList.add("investigator");

        Collections.shuffle(roleList);

        this.roleList = roleList;
    }

    private static int getFilesNumber(String path) {
        File file = new File(path);
        return Objects.requireNonNull(file.listFiles()).length;
    }

    public void clearTempFiles() {
        Path dir = Paths.get("src/main/resources/temp");
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
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

    public void setGameMode()
    {
        this.gameMode = (playersNumber <= 5) ? "standard" : "extended";
    }

    public String getGameMode() {
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

    public ArrayList<Player> getPlayersList()
    {
        return playersList;
    }
}