import io.mafialike.baseclasses.Card;
import io.mafialike.baseclasses.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import okhttp3.internal.platform.Platform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class ImagesJoin
{
    public static void createImage(ArrayList<Card> cardsHand, String file_name) throws IOException
    {
        ArrayList<BufferedImage> imgs = new ArrayList<>();

        for (Card card : cardsHand)
        {
            String path = String.format("src\\main\\resources\\images\\%s\\%s", card.getType(), card.getTitle());
            imgs.add(ImageIO.read(new File(path)));
        }

        BufferedImage combined = new BufferedImage(imgs.get(0).getWidth(), imgs.get(0).getHeight(), BufferedImage.TYPE_INT_ARGB);

        int currentWidth = 0;
        for (BufferedImage img : imgs)
        {
            combined.getGraphics().drawImage(img, currentWidth, 0, null);
            currentWidth += img.getWidth();
        }

        ImageIO.write(combined, "PNG", new File("src\\main\\resources\\temp\\" + file_name));
    }

    public static boolean sendHandImage(JDA api, Player player, String handType)
    {
        User apiUser = api.getUserById(player.getID());
        if (apiUser == null)
        {
            return false;
        }

        try
        {
            String filePath;
            if (handType.equals("clue") || handType.equals("weapon"))
                filePath = String.format("src\\main\\resources\\temp\\%s_%s.png", player.getName(), handType);
            else
                throw new IllegalArgumentException("Invalid hand type: " + handType);

            apiUser.openPrivateChannel()
                .flatMap(channel ->
                {
                    FileUpload fileUpload = FileUpload.fromData(new File(filePath), "file.png");

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setImage("attachment://file.png");

                    return channel.sendFiles(fileUpload).setEmbeds((embedBuilder.build()));
                }).queue();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean createHandImage(Player player, String handType)
    {
        if (player == null)
        {
            return false;
        }

        try {
            String fileName = String.format("%s_%s.png", player.getName(), handType);

            ArrayList<Card> hand;

            if (handType.equals("clue"))
                hand = player.getClueHand();
            else if (handType.equals("weapon"))
                hand = player.getWeaponHand();
            else
                throw new IllegalArgumentException("Invalid hand type: " + handType);

            ImagesJoin.createImage(hand, fileName);

        }
        catch (IOException e)
        {
            System.err.println("Failed to create or send image: " + e.getMessage());
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        Player testPlayer = new Player("name", "role", "420212610219114498");
        createHandImage(testPlayer, "clue");
    }
}