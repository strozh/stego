package ru.strozh.stego.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
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
    private String resultType;
    private String resultName;

    public String getUnstegoText() {
        return unstegoText;
    }

    public String getResultType() {
        return resultType;
    }

    public String getResultName() {
        return resultName;
    }

    public String redirectToStegoWithText(String imgName, String text, String password) throws IOException, GeneralSecurityException {
        System.out.println("imgName=" + imgName);
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(path + "/input/" + imgName);

        System.out.println("Получили файл");

        f = ImageToBmp.saveImageToBmp(f, path);

        FileStegoCover fileStegoCover = new FileStegoCover(f);

        System.out.println("Получили stegoCover");

        Writeble wr;
        if (password != null) {
            CryptoStegoWriter cryptoStegoWriter = new CryptoStegoWriter(new StegoWriter(fileStegoCover));
            cryptoStegoWriter.setPassword(password);
            wr = new CryptoStegoWriter(cryptoStegoWriter);
        } else {
            wr = new CryptoStegoWriter(new StegoWriter(fileStegoCover));
        }

        System.out.println("Получили cryptoStegoWriter");

        wr.write(new Secret(Secret.Type.TEXT, imgName.getBytes(), text.getBytes()));

        System.out.println("Записали в память");

//        f = fileStegoCover.getFile();

        System.out.println("Записали в файл");

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

    public String redirectToStegoWithFile(String imgName, String fileName, String password) throws IOException, GeneralSecurityException {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(path + "/input/" + imgName);
        File stegoFile = new File(path + "/input/" + fileName);
        byte[] bFile = new byte[(int) stegoFile.length()];

        FileInputStream fileInputStream = new FileInputStream(stegoFile);
        fileInputStream.read(bFile);
        fileInputStream.close();

        f = ImageToBmp.saveImageToBmp(f, path);

        FileStegoCover fileStegoCover = new FileStegoCover(f);
        Writeble wr;
        if (password != null) {
            CryptoStegoWriter cryptoStegoWriter = new CryptoStegoWriter(new StegoWriter(fileStegoCover));
            cryptoStegoWriter.setPassword(password);
            wr = new CryptoStegoWriter(cryptoStegoWriter);
        } else {
            wr = new CryptoStegoWriter(new StegoWriter(fileStegoCover));
        }
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

    public void redirectToUnStego(String imgName, String password) throws IOException, GeneralSecurityException {

        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        File f = new File(path + "/input/" + imgName);

        FileStegoCover fileStegoCover = new FileStegoCover(f);

        CryptoStegoReader cryptoStegoReader = new CryptoStegoReader(new StegoReader(fileStegoCover));
        Readeble readeble = new CryptoStegoReader(new StegoReader(fileStegoCover));
        if (password != null) {
            cryptoStegoReader.setPassword(password);
            readeble = new CryptoStegoReader(cryptoStegoReader);
        }

        Secret secret = readeble.read();


        // getAttachment возавращает имя файла (в случае передачи текстового сообщения поле ничего не содержит)
        String fName = path + "/input/" + new String(secret.getAttachment());

        if (fName.compareTo("") != 0) {

            // Создаём новый файл
            File res = new File(fName);
            res.createNewFile();

            // Поток для записи в файл
            FileOutputStream fos = new FileOutputStream(res);

            // Получаем данные (getData()) и пишем их в поток
            fos.write(secret.getData());
            fos.flush();
            fos.close();

            unstegoText = new String(secret.getAttachment());
            resultName = new String(secret.getAttachment());
            resultType = "file";

        } else {

            unstegoText = new String(secret.getData());

            File outText = new File(path + "/input/" + imgName + ".txt");
            outText.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(outText);

            outputStream.write(secret.getData());
            outputStream.flush();
            outputStream.close();

            resultType = "text";

        }

        FacesContext.getCurrentInstance().getExternalContext().redirect("/stego/unstego.xhtml");

    }
}
