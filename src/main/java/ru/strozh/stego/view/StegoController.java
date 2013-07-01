package ru.strozh.stego.view;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 */
@ManagedBean(name = "stego")
@SessionScoped
public class StegoController implements Serializable {

    @Inject
    FileUploadController uploadController;
    @Inject
    RedirectController redirectController;
    private BufferedImage img;
    private BufferedImage img2;
    private String imgName = "input.jpg";
    private String urlStr;
    private String text;
    private UploadedFile file;
    private StreamedContent fileStego;
    private String unstegoText = "";
    private String type = "text";

    public StegoController() {
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getUnstegoText() {
        return unstegoText;
    }

    public void setUnstegoText(String unstegoText) {
        this.unstegoText = unstegoText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) throws UnsupportedEncodingException {
        this.text = new String(text.getBytes("ISO-8859-1"), "UTF-8");
    }

    public void setTypeText() {
        type = "text";
    }

    public void setTypeFile() {
        type = "file";
    }

    public void dwnldImg() throws IOException {
        uploadController.downloadImage(urlStr);
        imgName = uploadController.getFileName();

    }

    public void handleFileUpload(FileUploadEvent event) throws FileNotFoundException, FileNotFoundException, IOException {
        uploadController.handleFileUpload(event);
        imgName = uploadController.getFileName();
    }

    public void defaultImage() {
        urlStr = "";
        imgName = "default.jpg";
        text = "";
        unstegoText = "";
    }

    public void redirectToIndex() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/stego");
    }

    public void redirectToStego() throws IOException, GeneralSecurityException {
        if (type.compareTo("file") == 0) {
            if (file != null) {
                uploadController.upload(file);
            }
            imgName = redirectController.redirectToStegoWithFile(imgName, uploadController.getFileName());
        } else {
            System.out.println("Yep");
            imgName = redirectController.redirectToStegoWithText(imgName, text);
        }
    }

    public void redirectToUnStego() throws IOException, GeneralSecurityException {
        System.out.println("imgNameDo=" + imgName);
        redirectController.redirectToUnStego(imgName);
        unstegoText = redirectController.getUnstegoText();
    }

    public StreamedContent getFileStego() {
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/input/" + imgName);
        fileStego = new DefaultStreamedContent(stream, "image/jpg", imgName);
        return fileStego;
    }

    public StreamedContent getFileUnStego() {
        if (redirectController.getResultType().compareTo("text") == 0) {
            InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/input/" + imgName + ".txt");
            fileStego = new DefaultStreamedContent(stream, "application/txt", imgName + ".txt");
        } else {            
            InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/input/" + redirectController.getResultName());
            fileStego = new DefaultStreamedContent(stream, "application/jpg", redirectController.getResultName());
        }
        return fileStego;
    }
}
