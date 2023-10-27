import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class DiscordBotCommands extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        String[] discordTagsOfPlayers = Arrays.copyOfRange(message, 1, message.length);

        DeceptionGame game = new DeceptionGame(discordTagsOfPlayers);
        game.startGame();

        if (message[0].equals("!mycommand"))
        {
            event.getChannel().sendMessage(DeceptionGame.getListOfPlayers()).queue();
        }


    }
}
