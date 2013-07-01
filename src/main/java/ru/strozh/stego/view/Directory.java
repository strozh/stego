package ru.strozh.stego.view;

import java.io.File;
import java.text.SimpleDateFormat;
import javax.faces.context.FacesContext;

/**
 *
 */
public class Directory {

    public static File[] getFiles(String path) {
        File folder = new File(path);
        return folder.listFiles();
    }

    public static String[] getFilesNames(String path) {
        File folder = new File(path);
        return folder.list();
    }

    public static int getFileLifeHour(File file) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String s = sdf.format(file.lastModified());
        return Integer.parseInt(s);
    }
}
