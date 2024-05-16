import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GuiDinosaur extends Dinosaur {
    private Image image;
    private float scale;

    public GuiDinosaur(String filename) {
        super(filename);
        try {
            this.image = ImageIO.read(new File((this.filename.replace(".txt", ".png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scale = getWidth() / (float) image.getWidth(null);
        setHeight((int) (image.getHeight(null) * scale));
    }
   
    public Image getImage()
    {
        return image;
    }

    public float getScale()
    {
        return scale;
    }

    private static ArrayList<GuiDinosaur> dinosaurs;

    public static ArrayList<GuiDinosaur> getDinosaurs()
    {
        if (dinosaurs == null)
        {
            dinosaurs = new ArrayList<GuiDinosaur>();
            for (File file : new File(PATH_PREFIX).listFiles())
            {
                if (file.getName().endsWith(".txt"))
                {
                    dinosaurs.add(new GuiDinosaur(PATH_PREFIX + file.getName()));
                }
            }
        }
        return dinosaurs;
    }
}
