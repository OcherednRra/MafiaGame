package io.mafialike.image;

import io.mafialike.baseclasses.Card;
import io.mafialike.baseclasses.Player;
import io.mafialike.other.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HandImageCreator
{
    private static final String CLUE = "clue";
    private static final String WEAPON = "weapon";

    public static void createClueHandImage(Player player, String fileName)
    {
        createHandImage(player, CLUE, fileName);
    }

    public static void createWeaponHandImage(Player player, String fileName)
    {
        createHandImage(player, WEAPON, fileName);
    }

    private static void createHandImage(Player player, String handType, String fileName)
    {
        if (player == null)
            throw new IllegalArgumentException("Player cannot be null");

        try
        {
            ArrayList<BufferedImage> imgs = getImages(getHand(player, handType));

            BufferedImage combined = new BufferedImage(getTotalWidth(imgs), getMaxHeight(imgs), BufferedImage.TYPE_INT_ARGB);

            int currentWidth = 0;

            Graphics g = combined.getGraphics();
            for (BufferedImage img : imgs)
            {
                g.drawImage(img, currentWidth, 0, null);
                currentWidth += img.getWidth();
            }

            writeImage(combined, fileName);

        } catch (IOException e)
        {
            System.err.println("Failed to create image: " + e.getMessage());
        }
    }

    public static void writeImage(BufferedImage combined, String fileName) throws IOException
    {
        String path = "src/main/resources/temp/" + fileName;
        try (OutputStream out = new FileOutputStream(path)) {
            ImageIO.write(combined, "PNG", out);
        }
    }


    private static int getTotalWidth(ArrayList<BufferedImage> imgs)
    {
        return imgs.stream().mapToInt(BufferedImage::getWidth).sum();
    }

    private static int getMaxHeight(ArrayList<BufferedImage> imgs)
    {
        return Collections.max(imgs, Comparator.comparing(BufferedImage::getHeight)).getHeight();
    }

    private static ArrayList<Card> getHand(Player player, String handType)
    {
        if (handType.equals(CLUE))
            return player.getClueHand();
        else if (handType.equals(WEAPON))
            return player.getWeaponHand();
        else
            throw new IllegalArgumentException("Invalid hand type: " + handType);
    }

    private static ArrayList<BufferedImage> getImages(ArrayList<Card> hand) throws IOException
    {
        ArrayList<BufferedImage> imgs = new ArrayList<>();

        for (Card card : hand)
        {
            String path = String.format("/images/%s/%s", card.getType(), card.getTitle());
            try (InputStream in = HandImageCreator.class.getResourceAsStream(path))
            {
                assert in != null;
                imgs.add(ImageIO.read(in));
            }
        }

        return imgs;
    }

    public static void main(String[] args)
    {

        Player testPlayer = new Player("testName", "test_role", Config.getTestUserId());

        testPlayer.setClueHand();
        testPlayer.setWeaponHand();

        String fileName;

        fileName = String.format("%s_%s.png", testPlayer.getName(), "clue");
        HandImageCreator.createClueHandImage(testPlayer, fileName);

        fileName = String.format("%s_%s.png", testPlayer.getName(), "weapon");
        HandImageCreator.createWeaponHandImage(testPlayer, fileName);
    }
}
