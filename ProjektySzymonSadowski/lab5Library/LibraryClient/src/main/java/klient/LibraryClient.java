package klient;

import serv.bean.Library;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@ApplicationScoped
public class LibraryClient implements Serializable {

    @EJB
    private Library libraryBean;

    public Library getLibraryBean() {
        return libraryBean;
    }

    public void setLibraryBean(Library libraryBean) {
        this.libraryBean = libraryBean;
    }

}
