import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MafiaLikeBot {
    public static void main(String[] args)
    {
//        DeceptionGame game = new DeceptionGame("whovian", "OcherednRra", "yourwaifu");
//        game.startGame();
        JDA bot = JDABuilder.createDefault("MTE2NjgyMjg2MzA2MDM1NzI3MA.GPnHIu.DEtH8vV4xCeiQ3fZ28fJxiqbfKSQb1ApIv_8yQ")
                .setActivity(Activity.playing("� �������� ������� ���� ����"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        bot.addEventListener(new DiscordBot());

    }


//        System.out.println(game.getListOfPlayers());
}
