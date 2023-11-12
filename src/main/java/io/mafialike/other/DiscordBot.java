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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.mafialike.image.HandImageCreator.createClueHandImage;
import static io.mafialike.image.HandImageCreator.createWeaponHandImage;

public class DiscordBot extends ListenerAdapter
{
    private static String CLUE;
    private static String WEAPON;
    private static boolean IS_GAME_ON = false;

    private static  ArrayList<String> clueHandImagesIDs = new ArrayList<>();
    private static  ArrayList<String> weaponHandImagesIDs = new ArrayList<>();

    private static JDA api;
    private static DeceptionGame game;

    private static boolean IS_CLUE_SELECTED = false;
    private static boolean IS_WEAPON_SELECTED = false;

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
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
            String handType;
            if (clueHandImagesIDs.contains(message.getId())) {
                handType = "clue";
            } else if (weaponHandImagesIDs.contains(message.getId())) {
                handType = "weapon";
            } else {
                return;
            }

            List<MessageReaction> reactionList = message.getReactions();
            for (MessageReaction reaction : reactionList) {
                int count = reaction.getCount();
                if (count == 2) {
                    switch (reaction.getEmoji().asUnicode().getAsCodepoints()) {
                        case "U+31U+fe0fU+20e3":

                        case "U+32U+fe0fU+20e3":
                            System.out.println("Пользователь нажал 2️⃣ (" + handType + ")"); // 2️⃣
                        case "U+33U+fe0fU+20e3":
                            System.out.println("Пользователь нажал 3️⃣ (" + handType + ")"); // 3️⃣
                        case "U+34U+fe0fU+20e3":
                            System.out.println("Пользователь нажал 4️⃣ (" + handType + ")"); // 4️⃣
                        default:
                            System.out.println("Пользователь нажал другую реакцию");
                    }
                }
            }
        });
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
                        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                        channel.sendMessage("Session: " + time + "\nRole: " + player.getRole()).queue();
                    });
                });

                createClueHandImage(player, player.getName() + "_clue.png");
                createWeaponHandImage(player, player.getName() + "_weapon.png");

                switch (player.getRole()) {
                    case "criminologist":
                        // отправить ник убийцы и сообщника, а также карты, которые выбрал убийца
                    case "killer":
                        // отправить ник сообщника и дать выбрать карты
                    case "accomplice":
                        // отправить ник убийцы и дать выбрать карты
                    case "witness":
                        // отправить ник убийцы и сообщника (в случайном порядке)
                    default:
                        // sendRoleAndDescriptionMessage(player);
                }

                sendRoleAndDescriptionMessage(player);
                sendHandImage(player, "clue");

                sendHandImage(player, "weapon");
            }
        } catch (Exception e) {
            sendMessageToChannel(event, "❌ Error sending messages");
        } finally {
            sendMessageToChannel(event, "✅ Messages have been successfully sent");
        }
    }

    private static void sendRoleAndDescriptionMessage(Player player) {
        api.retrieveUserById(player.getID()).queue(user -> {
            user.openPrivateChannel().queue(channel -> {
                channel.sendMessage(player.getRoleDescription()).queue();
            });
        });
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

                    channel.sendFiles(fileUpload).setEmbeds((embedBuilder.build())).queue(msg -> {
                        if (handType.equals("clue"))
                            clueHandImagesIDs.add(msg.getId());
                        else if (handType.equals("weapon"))
                            weaponHandImagesIDs.add(msg.getId());
                        if (player.getRole().equals("killer")) {
                            msg.addReaction(Emoji.fromUnicode("\u0031\uFE0F\u20E3")).queue();
                            msg.addReaction(Emoji.fromUnicode("\u0032\uFE0F\u20E3")).queue();
                            msg.addReaction(Emoji.fromUnicode("\u0033\uFE0F\u20E3")).queue();
                            msg.addReaction(Emoji.fromUnicode("\u0034\uFE0F\u20E3")).queue();
                        }
                    });

                });
            });
        } catch (Exception e)
        {
            System.err.println("Failed to send image: " + e.getMessage());
        }

    }

    private static String getFilePath(Player player, String handType) {
        if (handType.equals("clue") || handType.equals("weapon"))
            return String.format("src\\main\\resources\\temp\\%s_%s.png", player.getName(), handType);
        else
            throw new IllegalArgumentException("Invalid hand type: " + handType);
    }
}
