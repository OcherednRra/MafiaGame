import io.mafialike.baseclasses.Card;
import io.mafialike.baseclasses.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    public static boolean sendImage(JDA api, String userID, String file_name)
    {
        User apiUser = api.getUserById(userID);
        if (apiUser == null)
        {
            return false;
        }

        try
        {
            apiUser.openPrivateChannel()
                    .flatMap(channel ->
                    {
                        String path = String.format("src\\main\\resources\\temp\\%s", file_name);
                        FileUpload fileUpload = FileUpload.fromData(new File(path), "file.png");

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

    public static boolean createCardHandsImage(JDA api, ArrayList<Player> playerList)
    {
        if (playerList.isEmpty())
        {
            return false;
        }

        Player[] playerArray = new Player[playerList.size()];
        playerList.toArray(playerArray);

        try {
            for (Player player : playerArray) {
                ImagesJoin.createImage(player.getClueHand(), player.getName() + "_clue.png");
                sendImage(api, player.getID(), player.getName() + "_clue.png");

                ImagesJoin.createImage(player.getWeaponHand(), player.getName() + "_weapon.png");
                sendImage(api, player.getID(), player.getName() + "_weapon.png");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}