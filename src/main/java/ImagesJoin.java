import io.mafialike.baseclasses.Card;
import io.mafialike.baseclasses.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ImagesJoin
{
    public static void createImage(ArrayList<Card> cardsHand, String file_name) throws IOException{

        ArrayList<BufferedImage> imgs = new ArrayList<>();

        int totalWidth = 0;
        int maxHeight = 0;

        for (Card card : cardsHand)
        {
            imgs.add(ImageIO.read(new File("src\\main\\resources\\images\\" + card.getType() + "\\" + card.getTitle())));
        }

        for (BufferedImage img : imgs)
        {
            totalWidth += img.getWidth();
            maxHeight = Math.max(maxHeight, img.getHeight());
        }

        BufferedImage combined = new BufferedImage(totalWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        int currentWidth = 0;
        for (BufferedImage img : imgs)
        {
            g.drawImage(img, currentWidth, 0, null);
            currentWidth += img.getWidth();
        }

        ImageIO.write(combined, "PNG", new File("src\\main\\resources\\temp\\" + file_name));
    }

    public static void sendImage(MessageReceivedEvent event, String file_name, User user, JDA api)
    {
        Objects.requireNonNull(api.getUserById(user.getId())).openPrivateChannel()
                .flatMap(channel -> channel.sendFiles(FileUpload.fromData(new File("src\\main\\resources\\temp\\" + file_name), "file.png"))
                        .setEmbeds(new EmbedBuilder().setImage("attachment://file.png").build())).queue();
    }

    public static void createCardHandsImage(ArrayList<Player> listOfPlayers, ArrayList<User> usersIdList, MessageReceivedEvent event, JDA api)
    {
        try {
            for (int i = 0; i < listOfPlayers.size(); i++)
            {
                ImagesJoin.createImage(listOfPlayers.get(i).getClueHand(), listOfPlayers.get(i).getName() + "ClueHand.png");
                sendImage(event, listOfPlayers.get(i).getName() + "ClueHand.png", usersIdList.get(i), api);
                ImagesJoin.createImage(listOfPlayers.get(i).getWeaponHand(), listOfPlayers.get(i).getName() + "WeaponHand.png");
                sendImage(event, listOfPlayers.get(i).getName() + "WeaponHand.png", usersIdList.get(i), api);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}