import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.Objects;

public class DiscordBot extends ListenerAdapter {

    private static JDA bot;

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
        String[] message = event.getMessage().getContentRaw().split(" ");
        String[] discordTagsOfPlayers = Arrays.copyOfRange(message, 1, message.length);

        DeceptionGame game = new DeceptionGame(discordTagsOfPlayers);
        game.startGame();

        if (message[0].equals("!mycommand"))
        {
            event.getChannel().sendMessage(DeceptionGame.getListOfPlayers()).queue();
        }
        if (message[0].equals("!private"))
        {
            Objects.requireNonNull(event.getAuthor()).openPrivateChannel().flatMap(channel -> channel.sendMessage("Hello!")).queue();
        }
    }
}
