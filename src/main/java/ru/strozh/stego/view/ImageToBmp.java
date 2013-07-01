package ru.strozh.stego.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 */
public class ImageToBmp {

    public ImageToBmp() {
    }

    public static File saveImageToBmp(File f, String path) throws IOException {
        BufferedImage image = ImageIO.read(f);

        String imgName = f.getName().concat(".bmp");
        String to = path + "/input/" + imgName; //?добавить расширение
        ImageIO.write(image, "bmp", new File(to));

        File newFile = new File(to);

        return newFile;

    }
}
