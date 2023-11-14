package io.mafialike.other;

import io.mafialike.baseclasses.DeceptionGame;
import io.mafialike.baseclasses.Player;
import io.mafialike.image.HandImageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.mafialike.image.HandImageCreator.*;

public class DiscordBot extends ListenerAdapter
{
    private static int CLUE_NUM;
    private static int WEAPON_NUM;
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
        if (event.getAuthor().isBot()) return;

        switch (command) {
            case "!start":
                if (!IS_GAME_ON) startGame(event);
                IS_GAME_ON = true;
                break;
            case "!play":
                Guild guild = event.getGuild();
                VoiceChannel channel = guild.getVoiceChannelsByName("совместная мастурбация", true).get(0); // Это получит первый голосовой канал с именем "music"
                AudioManager manager = guild.getAudioManager();
                manager.setSendingHandler(new MySendHandler()); // MySendHandler должен быть вашей реализацией AudioSendHandler
                manager.openAudioConnection(channel);
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
                int num = 0;
                if (count == 2 && (!IS_WEAPON_SELECTED || !IS_CLUE_SELECTED)) {
                    switch (reaction.getEmoji().asUnicode().getAsCodepoints()) {
                        case "U+31U+fe0fU+20e3" -> num = 1;
                        case "U+32U+fe0fU+20e3" -> num = 2;
                        case "U+33U+fe0fU+20e3" -> num = 3;
                        case "U+34U+fe0fU+20e3" -> num = 4;
                        default -> System.out.println("User select other emoji!");
                    }

                    if (handType.equals("clue") && !IS_CLUE_SELECTED) {
                        IS_CLUE_SELECTED = true;
                        CLUE_NUM = num;
                    } else if (handType.equals("weapon") && !IS_WEAPON_SELECTED){
                        IS_WEAPON_SELECTED = true;
                        WEAPON_NUM = num;
                    }
                }
            }

            if (WEAPON_NUM != 0 && CLUE_NUM != 0) {
                String clue = DeceptionGame.getKiller().getClueHand().get(CLUE_NUM-1).getTitle();
                String weapon = DeceptionGame.getKiller().getWeaponHand().get(WEAPON_NUM-1).getTitle();
                createClueAndWeaponImage(clue, weapon);

                sendPrivateMessage(DeceptionGame.getKiller(), "Главная Улика и Орудие Убийства:");
                sendHandImage(DeceptionGame.getKiller(), "-");


                sendPrivateMessage(DeceptionGame.getCriminologist(), "Главная Улика и Орудие Убийства:");
                sendHandImage(DeceptionGame.getCriminologist(), "-");

                if (DeceptionGame.getAccomplice() != null) {
                    sendPrivateMessage(DeceptionGame.getAccomplice(), "Главная Улика и Орудие Убийства:");
                    sendHandImage(DeceptionGame.getAccomplice(), "-");
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
                String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                sendPrivateMessage(player, "Session: " + time + "\nRole: " + player.getRole());

                createClueHandImage(player, player.getName() + "_clue.png");
                createWeaponHandImage(player, player.getName() + "_weapon.png");

                String killer;
                String accomplice;
                switch (player.getRole()) {
                    case "criminologist" -> {
                        // отправить ник убийцы и сообщника, а также карты, которые выбрал убийца
                        killer = DeceptionGame.getKiller().getName();
                        accomplice = DeceptionGame.getAccomplice() != null ? "@" + DeceptionGame.getAccomplice().getName() : "-";
                        sendPrivateMessage(player, "Убийца: " + killer + "\nСообщник: " + accomplice);
                    }
                    case "killer" -> {
                        // отправить ник сообщника и дать выбрать карты
                        accomplice = DeceptionGame.getAccomplice() != null ? "@" + DeceptionGame.getAccomplice().getName() : "-";
                        sendPrivateMessage(player, "Сообщник: " + accomplice);
                    }
                    case "accomplice" -> {
                        // отправить ник убийцы и прислать выбраные карты
                        killer = DeceptionGame.getKiller().getName();
                        sendPrivateMessage(player, "Убийца: " + killer);
                    }
                    case "witness" -> {
                        // отправить ник убийцы и сообщника (в случайном порядке)
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(DeceptionGame.getKiller().getName());
                        if (DeceptionGame.getAccomplice() != null) {
                            temp.add(DeceptionGame.getAccomplice().getName());
                            Collections.shuffle(temp);
                        }
                        sendPrivateMessage(player, "Подозреваемые: " + String.join(" ", temp));
                    }
                }

                sendPrivateMessage(player, player.getRoleDescription());
                sendHandImage(player, "clue");

                sendHandImage(player, "weapon");
            }
        } catch (Exception e) {
            sendMessageToChannel(event, "❌ Error sending messages");
        } finally {
            sendMessageToChannel(event, "✅ Messages have been successfully sent");
        }
    }

    private static void sendPrivateMessage(Player player, String message) {
        api.retrieveUserById(player.getID()).queue(user -> {
            user.openPrivateChannel().queue(channel -> {
                channel.sendMessage(message).queue();
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

                        if (player.getRole().equals("killer")  && !handType.equals("-")) {
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
        else if (handType.equals("-"))
            return "src/main/resources/temp/clueAndWeapon.png";
        else
            throw new IllegalArgumentException("Invalid hand type: " + handType);
    }
}
