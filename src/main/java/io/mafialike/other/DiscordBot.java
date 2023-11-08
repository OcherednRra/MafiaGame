package io.mafialike.other;

import io.mafialike.baseclasses.DeceptionGame;
import io.mafialike.baseclasses.Player;
import io.mafialike.image.HandImageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscordBot extends ListenerAdapter
{
    private static final String CLUE = "clue";
    private static final String WEAPON = "weapon";
    private static boolean IS_GAME_ON = false;

    private static JDA api;
    private static DeceptionGame game;

    public static void startBot() {
        api = JDABuilder.createDefault(Config.getBotToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        api.addEventListener(new DiscordBot());
    }

    public void onMessageReceived(MessageReceivedEvent event)
    {
        String command = Arrays.asList(event.getMessage().getContentRaw().split(" ")).get(0);

        switch (command) {
            case "!start":
                if (!IS_GAME_ON) startGame(event);
                IS_GAME_ON = true;
                break;
            case "!test":
//                methodTest(event);
//                break;
        }
    }

    private static void methodTest(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Player testPlayer = new Player(user.getName(), "criminalist", user.getId());

        String fileName;

        fileName = String.format("%s_%s.png", testPlayer.getName(), "clue");
        HandImageCreator.createClueHandImage(testPlayer, fileName);
        sendHandImage(testPlayer, "clue");

        fileName = String.format("%s_%s.png", testPlayer.getName(), "weapon");
        HandImageCreator.createWeaponHandImage(testPlayer, fileName);
        sendHandImage(testPlayer, "weapon");
    }

    private static void startGame(MessageReceivedEvent event) {
        ArrayList<User> usersList = new ArrayList<>(event.getMessage().getMentions().getUsers());
        event.getChannel().sendMessage(usersList.toString()).queue();

        game = new DeceptionGame(usersList);
        game.startGame();

        System.out.println(game.getPlayersList());
        System.out.println(game.getPlayersList().get(0).getClueHand());

        for (Player player : game.getPlayersList()) {
            sendHandImage(player, "clue");
        }
    }

    public static void sendHandImage(Player player, String handType) {
        try
        {
            api.retrieveUserById(player.getID()).queue(user -> {
                user.openPrivateChannel().queue(channel -> {
                    FileUpload fileUpload = FileUpload.fromData(new File(getFilePath(player, handType)), "file.png");

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setImage("attachment://file.png");

                    channel.sendFiles(fileUpload).setEmbeds((embedBuilder.build())).queue();
                });
            });
        } catch (Exception e)
        {
            System.err.println("Failed to send image: " + e.getMessage());
        }

    }

    private static String getFilePath(Player player, String handType) {
        if (handType.equals(CLUE) || handType.equals(WEAPON))
            return String.format("src\\main\\resources\\temp\\%s_%s.png", player.getName(), handType);
        else
            throw new IllegalArgumentException("Invalid hand type: " + handType);
    }
}
