import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;
import java.awt.font.ImageGraphicAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DiscordBot extends ListenerAdapter
{
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

                    for (int i = 0; i < DeceptionGame.getListOfPlayers().size(); i++)
                        ImagesJoin.createCardHandsImage(DeceptionGame.getListOfPlayers(), userIdList, event, bot);

                    discordTagsOfPlayers.clear();
                    DeceptionGame.listOfPlayers.clear();
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
