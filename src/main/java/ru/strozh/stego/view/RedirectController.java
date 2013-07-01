package ru.strozh.stego.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.Date;
import javax.faces.context.FacesContext;
import ru.jonnygold.stego.CryptoStegoReader;
import ru.jonnygold.stego.CryptoStegoWriter;
import ru.jonnygold.stego.FileStegoCover;
import ru.jonnygold.stego.Readeble;
import ru.jonnygold.stego.Secret;
import ru.jonnygold.stego.StegoReader;
import ru.jonnygold.stego.StegoWriter;
import ru.jonnygold.stego.Writeble;

public class RedirectController implements Serializable {

    private BufferedImage img;
    private String unstegoText;

    public String getUnstegoText() {
        return unstegoText;
    }

    public String redirectToStegoWithText(String imgName, String text) throws IOException, GeneralSecurityException {
        System.out.println("imgName=" + imgName);
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(path + "/input/" + imgName);

        System.out.println("Получили файл");

        f = ImageToBmp.saveImageToBmp(f, path);

        FileStegoCover fileStegoCover = new FileStegoCover(f);

        System.out.println("Получили stegoCover");

        Writeble wr = new CryptoStegoWriter(new StegoWriter(fileStegoCover));

        System.out.println("Получили cryptoStegoWriter");

        wr.write(new Secret(Secret.Type.TEXT, imgName.getBytes(), text.getBytes()));

        System.out.println("Записали в память");

//        f = fileStegoCover.getFile();

        System.out.println("Записали в файл");


//        // Считываем картинку
//        img = ImageIO.read(f);
//
//        Signal signal = ImageProcessor.getSignal(img, 0);
//
//        // Создаём стего
//        Stego stego = StegoTools.createStego(signal);
//
//        /**
//         * * Пишем в стего **
//         */
//        StegoWriter sw = new StegoWriter(stego);
//        sw.write(new Secret(Secret.Type.TEXT, text.getBytes()));
//
//        BufferedImage img_1 = img;
//        ImageProcessor.setBand(img_1, signal, 0);
//
//        imgName = Long.toString(new Date().getTime());
//        imgName = imgName.concat(".bmp");
//        String to = path + "/input/" + imgName; //?добавить расширение
//        ImageIO.write(img_1, "bmp", new File(to));
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/stego/stego.xhtml");

        imgName = Long.toString(new Date().getTime());
        imgName = imgName.concat(f.getName());
        String to = path + "/input/" + imgName; //?добавить расширение        

        File outputFile = new File(to);
        byte[] bytes = Files.readAllBytes(f.toPath());
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();

        FacesContext.getCurrentInstance().getExternalContext().redirect("/stego/stego.xhtml");
        return imgName;
    }

    public String redirectToStegoWithFile(String imgName, String fileName) throws IOException, GeneralSecurityException {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(path + "/input/" + imgName);
        File stegoFile = new File(path + "/input/" + fileName);
         byte[] bFile = new byte[(int) stegoFile.length()];

        f = ImageToBmp.saveImageToBmp(f, path);

        FileStegoCover fileStegoCover = new FileStegoCover(f);
        Writeble wr = new CryptoStegoWriter(new StegoWriter(fileStegoCover));
        wr.write(new Secret(Secret.Type.FILE, fileName.getBytes(), bFile));
        
        imgName = Long.toString(new Date().getTime());
        imgName = imgName.concat(f.getName());
        String to = path + "/input/" + imgName; 

        File outputFile = new File(to);
        byte[] bytes = Files.readAllBytes(f.toPath());
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();

        FacesContext.getCurrentInstance().getExternalContext().redirect("/stego/stego.xhtml");
        return imgName;
    }

    public void redirectToUnStego(String imgName) throws IOException, GeneralSecurityException {

        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(path + "/input/" + imgName);

        FileStegoCover fileStegoCover = new FileStegoCover(f);
        Readeble readeble = new CryptoStegoReader(new StegoReader(fileStegoCover));

        Secret secret = readeble.read();

        unstegoText = new String(secret.getData());

        System.out.println("Начинаем запись пдф");

        File outText = new File(path + "/input/" + imgName + ".txt");
        outText.createNewFile();

        System.out.println("Продолжаем запись пдф");

        FileOutputStream outputStream = new FileOutputStream(outText);

        outputStream.write(secret.getData());
        outputStream.flush();
        outputStream.close();

        System.out.println("Конец запись пдф");

        FacesContext.getCurrentInstance().getExternalContext().redirect("/stego/unstego.xhtml");

    }
}
