package ru.strozh.stego.view;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 * Фоновый убощик картинок из input
 */
@Singleton
@Startup
public class Cleaner implements Serializable {

    @Inject
    PathController pathController;    

    @Schedule(hour = "*/1", minute = "0", second = "0", persistent = false)
    public void cleanInput() {
        File[] files = Directory.getFiles(pathController.getPath() + "/input/");
        System.out.println("files="+files.length);
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().compareTo("default.jpg") != 0) {
                DateFormat dateFormat = new SimpleDateFormat("HH");
                Calendar cal = Calendar.getInstance();
                int currentHour = Integer.parseInt(dateFormat.format(cal.getTime()));
                if (currentHour - Directory.getFileLifeHour(files[i]) > 1) {
                    files[i].delete();
                }
            }
        }

        System.out.println("Knock knock");
    }
    
    @Schedule(hour = "*/23", minute = "0", second = "0", persistent = false)
    public void cleanOutput() {
        File[] files = Directory.getFiles(pathController.getPath() + "/output/");
        System.out.println("files="+files.length);
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().compareTo("default.jpg") != 0) {
                DateFormat dateFormat = new SimpleDateFormat("HH");
                Calendar cal = Calendar.getInstance();
                int currentHour = Integer.parseInt(dateFormat.format(cal.getTime()));
                if (currentHour - Directory.getFileLifeHour(files[i]) > 23) {
                    files[i].delete();
                }
            }
        }

        System.out.println("Knock knock");
    }

    public Cleaner() {
        System.out.println("Constructor");
    }
}
