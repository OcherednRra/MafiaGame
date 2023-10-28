import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImagesJoin
{
    public static void createImage(ArrayList<DeceptionGame.Card> cardsHand) throws IOException{

        ArrayList<BufferedImage> imgs = new ArrayList<>();

        for (DeceptionGame.Card card : cardsHand)
        {
            imgs.add(ImageIO.read(new File("src\\main\\java\\" + card.type + "\\" + card.title)));
        }

        int totalWidth = 0;
        int maxHeight = 0;
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

        ImageIO.write(combined, "PNG", new File("src\\main\\java\\temp\\temp.png"));
    }

    public static BufferedImage getFile(String file) throws IOException
    {
        return ImageIO.read(new File("src\\main\\java\\" + file));
    }
}