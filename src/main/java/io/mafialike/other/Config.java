package io.mafialike.other;

import io.mafialike.baseclasses.DeceptionGame;
import io.mafialike.baseclasses.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config
{
    private static final Properties config = readConfig();

    private static Properties readConfig()
    {
        var properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Error reading configuration file");
        }
        return properties;
    }

    public static String getBotToken()
    {
        return config.getProperty("BOT_TOKEN");
    }

    public static String getTestUserId()
    {
        return config.getProperty("TEST_USER_ID");
    }

    public static String getDescriptionOf(String role) {
        String description = null;
        switch (role) {
            case "criminologist" -> description = "CRIMINOLOGIST_DESCRIPTION";
            case "killer" -> description = "KILLER_DESCRIPTION";
            case "accomplice" -> description = "ACCOMPLICE_DESCRIPTION";
            case "witness" -> description = "WITNESS_DESCRIPTION";
            case "investigator" -> description = "INVESTIGATOR_DESCRIPTION";
        }
        return config.getProperty(description);
    }

    public static void main(String[] args)
    {
        System.out.println("=== Simple io.mafialike.other.Config class testing ===");
        System.out.println("BOT_TOKEN = " + Config.getBotToken());
    }
}