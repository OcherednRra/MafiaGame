import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MafiaLikeBot
{
    public static void main(String[] args)
    {
        JDA bot = JDABuilder.createDefault(Config.getBotToken())
                .setActivity(Activity.playing("в спальной комната твоя мама"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        bot.addEventListener(new DiscordBotCommands());

    }
}