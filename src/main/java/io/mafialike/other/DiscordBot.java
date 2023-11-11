package io.mafialike.other;

import io.mafialike.baseclasses.DeceptionGame;
import io.mafialike.baseclasses.Player;
import io.mafialike.image.HandImageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.mafialike.image.HandImageCreator.createClueHandImage;
import static io.mafialike.image.HandImageCreator.createWeaponHandImage;

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
                // test
//                methodTest(event);
//                break;
        }
    }

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        // Получите эмодзи реакции

        String reactionEmote = event.getReaction().getEmoji().asUnicode().getAsCodepoints();
            switch (reactionEmote) {
                case "U+31U+fe0fU+20e3" -> System.out.println("Пользователь нажал 1️⃣"); // 1️⃣
                case "U+32U+fe0fU+20e3" -> System.out.println("Пользователь нажал 2️⃣"); // 2️⃣
                case "U+33U+fe0fU+20e3" -> System.out.println("Пользователь нажал 3️⃣"); // 3️⃣
                case "U+34U+fe0fU+20e3" -> System.out.println("Пользователь нажал 4️⃣"); // 4️⃣
                default -> System.out.println("Пользователь нажал другую реакцию");
            }
    }

    private static void methodTest(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Player testPlayer = new Player(user.getName(), "criminalist", user.getId());

        String fileName;

        fileName = String.format("%s_%s.png", testPlayer.getName(), "clue");
        createClueHandImage(testPlayer, fileName);
        sendHandImage(testPlayer, "clue");

        fileName = String.format("%s_%s.png", testPlayer.getName(), "weapon");
        createWeaponHandImage(testPlayer, fileName);
        sendHandImage(testPlayer, "weapon");
    }


    private static void startGame(MessageReceivedEvent event) {
        ArrayList<User> usersList = new ArrayList<>(event.getMessage().getMentions().getUsers());
        sendMessageToChannel(event, usersList.toString());

        game = new DeceptionGame(usersList);
        game.startGame();

        try {
            for (Player player : game.getPlayersList()) {
                api.retrieveUserById(player.getID()).queue(user -> {
                    user.openPrivateChannel().queue(channel -> {
                        channel.sendMessage("Session: " + LocalDate.now().format(DateTimeFormatter.ofPattern("HH:mm"))).queue();
                    });
                });

                createClueHandImage(player, player.getName() + "_clue.png");
                sendHandImage(player, CLUE);

                createWeaponHandImage(player, player.getName() + "_weapon.png");
                sendHandImage(player, WEAPON);
            }
        } catch (Exception e) {
            sendMessageToChannel(event, "❌ Error sending messages");
        } finally {
            sendMessageToChannel(event, "✅ Messages have been successfully sent");
        }

    }

    private static void sendMessageToChannel(MessageReceivedEvent event, String message) {
        event.getChannel().sendMessage(message).queue();
    }

    public static void sendHandImage(Player player, String handType) {
        try
        {
            api.retrieveUserById(player.getID()).queue(user -> {
                user.openPrivateChannel().queue(channel -> {
                    FileUpload fileUpload = FileUpload.fromData(new File(getFilePath(player, handType)), "file.png");

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setImage("attachment://file.png");

                    Emoji emoji1 = Emoji.fromUnicode("\u0031\uFE0F\u20E3");
                    Emoji emoji2 = Emoji.fromUnicode("\u0032\uFE0F\u20E3");
                    Emoji emoji3 = Emoji.fromUnicode("\u0033\uFE0F\u20E3");
                    Emoji emoji4 = Emoji.fromUnicode("\u0034\uFE0F\u20E3");

                    channel.sendFiles(fileUpload).setEmbeds((embedBuilder.build())).queue(msg -> {
                        msg.addReaction(emoji1).queue();
                        msg.addReaction(emoji2).queue();
                        msg.addReaction(emoji3).queue();
                        msg.addReaction(emoji4).queue();
                    });

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
