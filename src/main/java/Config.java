import java.io.*;
import java.util.Properties;

public class Config
{
    private static final Properties config = readConfig();

    private static Properties readConfig()
    {
        var properties = new Properties();

        try
        {
            properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        }
        catch (IOException e) {throw new RuntimeException("Error reading configuration file");}

        return properties;
    }

    public static String getBotToken()
    {
        return config.getProperty("BOT_TOKEN");
    }

    public static void main(String[] args)
    {
        System.out.println("=== Simple Config class testing ===");
        System.out.println("BOT_TOKEN = " + Config.getBotToken());
    }
}