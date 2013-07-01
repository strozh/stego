package ru.strozh.stego.view;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 */
@ManagedBean
@ApplicationScoped
@Startup
public class PathController implements Serializable {

    private static String path;

    public PathController() {
    }

    public void start() {
        path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Path=" + path);
    }

    public String getPath() {
        return path;
    }
}
