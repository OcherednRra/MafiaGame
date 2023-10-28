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
    private final ArrayList<User> userIdList = new ArrayList<>();
    private boolean isGameOn = false;
    ArrayList<String> discordTagsOfPlayers = new ArrayList<>();

    public static void startBot()
    {
        bot = JDABuilder.createDefault(Config.getBotToken())
                .setActivity(Activity.playing("в спальной комната тво€ мама"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        bot.addEventListener(new DiscordBot());
    }

    public void onMessageReceived(MessageReceivedEvent event)
    {
        ArrayList<String> message = new ArrayList<>(Arrays.asList(event.getMessage().getContentRaw().split(" ")));

        switch (message.get(0))
        {
            case "!mycommand":
                event.getChannel().sendMessage(DeceptionGame.getListOfPlayers().toString()).queue();
                break;
            case "!start_game":
                if (!isGameOn)
                {
                    isGameOn = true;
                    userIdList.clear();
                    userIdList.add(event.getAuthor());
                    event.getChannel().sendMessage(userIdList.size() + "/12").queue();
                }
                else
                {

                    event.getChannel().sendMessage("Ќабор игроков окончен " + userIdList.size()).queue();
                    for (User user : userIdList)
                    {
                        discordTagsOfPlayers.add(user.getEffectiveName());
                    }

                    DeceptionGame game = new DeceptionGame(discordTagsOfPlayers);
                    game.startGame();

                    event.getChannel().sendMessage(DeceptionGame.getListOfPlayers().toString()).queue();
                }
                break;

            case "!createfile":
                try {
                    ImagesJoin.createImage(DeceptionGame.listOfPlayers.get(0).clueHand);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                File file = new File("src\\main\\java\\temp\\temp.png");
                event.getChannel().sendFiles(FileUpload.fromData(file, "file.png"))
                    .setEmbeds(ImagesJoin.buildImage())
                    .queue();

                break;
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
