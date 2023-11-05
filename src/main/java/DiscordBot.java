import io.mafialike.baseclasses.DeceptionGame;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.ArrayList;
import java.util.Arrays;

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

                    //for (int i = 0; i < DeceptionGame.getListOfPlayers().size(); i++)
                    //{
                        //ImagesJoin.createHandImage(bot, DeceptionGame.getListOfPlayers(), "clue");
                        //ImagesJoin.createHandImage(bot, DeceptionGame.getListOfPlayers(), "weapon");
                    //}

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
