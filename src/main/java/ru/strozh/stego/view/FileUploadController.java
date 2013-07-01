package ru.strozh.stego.view;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "fileUpload")
@SessionScoped
public class FileUploadController implements Serializable {

    private UploadedFile file;
    private String fileName = "input.jpg";
    private String fileUrl;
    private URL url;
    private BufferedImage img;

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void handleFileUpload(FileUploadEvent event) throws FileNotFoundException, FileNotFoundException, IOException {
        file = event.getFile();
        if (file != null) {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            fileName = Long.toString(new Date().getTime());
            fileName = fileName.concat(file.getFileName());
            OutputStream outputStream = new FileOutputStream(path + "/input/" + fileName);
            try {
                outputStream.write(file.getContents());
            } catch (Exception e) {
                System.out.println("error=" + e);
            }
            outputStream.close();

        }
    }

    public void upload(UploadedFile f) throws FileNotFoundException, IOException {
        if (f != null) {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            fileName = Long.toString(new Date().getTime());
            fileName = fileName.concat(f.getFileName());
            OutputStream outputStream = new FileOutputStream(path + "/input/" + fileName);
            try {
                outputStream.write(f.getContents());
            } catch (Exception e) {
                System.out.println("error=" + e);
            }
            outputStream.close();

        }
    }

    public void downloadImage(String urlStr) throws IOException {
        fileUrl = urlStr;
        url = new URL(fileUrl);
        URL imageURL = new URL(fileUrl);
        img = ImageIO.read(imageURL);

        System.out.println(img);
        System.out.println("ok! image=" + url.getFile());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(img, "bmp", outputStream);

        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        fileName = Long.toString(new Date().getTime());

        String to = path + "/input/" + fileName; //?добавить расширение

        ImageIO.write(img, "bmp", new File(to));
        outputStream.close();
        System.out.println("imgName=" + fileName);
    }
}
