import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static final String BOT_TOKEN;

    static {
        Properties prop = new Properties();
        try
        {
            prop.load(new FileInputStream("config.properties"));
            BOT_TOKEN = prop.getProperty("BOT_TOKEN");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static String getBotToken()
    {
        return BOT_TOKEN;
    }
}