package io.mafialike.other;

import io.mafialike.baseclasses.DeceptionGame;
import io.mafialike.baseclasses.Player;
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

public class DiscordBot extends ListenerAdapter
{
    private static final String CLUE = "clue";
    private static final String WEAPON = "weapon";

    private static JDA bot;
    public final ArrayList<User> userIdList = new ArrayList<>();
    private boolean isGameOn = false;
    ArrayList<String> discordTagsOfPlayers = new ArrayList<>();

    public static void startBot()
    {
        bot = JDABuilder.createDefault(Config.getBotToken())
                .setActivity(Activity.playing("в спальной комната твоя мама"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        bot.addEventListener(new DiscordBot());
    }

    public static boolean sendHandImage(JDA api, Player player, String handType)
    {
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
            return false;
        }

        return true;
    }

    private static String getFilePath(Player player, String handType)
    {
        if (handType.equals(CLUE) || handType.equals(WEAPON))
            return String.format("src\\main\\resources\\temp\\%s_%s.png", player.getName(), handType);
        else
            throw new IllegalArgumentException("Invalid hand type: " + handType);
    }

    public void onMessageReceived(MessageReceivedEvent event)
    {
        ArrayList<String> message = new ArrayList<>(Arrays.asList(event.getMessage().getContentRaw().split(" ")));

        switch (message.get(0))
        {
            case "!start":
                isGameOn = true;
                event.getChannel().sendMessage(userIdList.size() + "/12").queue();
                break;

            case "!end":
                if (isGameOn)
                {
                    event.getChannel().sendMessage("Набор игроков окончен " + userIdList.size()).queue();
                    for (User user : userIdList)
                    {
                        discordTagsOfPlayers.add(user.getEffectiveName());
                    }

                    DeceptionGame game = new DeceptionGame(userIdList);
                    game.startGame();
                    // это пиздец
                    // это пиздец
                    // это пиздец
                    // это пиздец
                    // это пиздец
                    System.out.println(userIdList.toString());



                    discordTagsOfPlayers.clear();
                    DeceptionGame.getListOfPlayers().clear();
                    userIdList.clear();
                    isGameOn = false;
                }
            case "!join":
                if (isGameOn)
                {
                    userIdList.add(event.getAuthor());
                    event.getChannel().sendMessage(userIdList.size() + "/12").queue();
                }
                break;
        }

    }
}
