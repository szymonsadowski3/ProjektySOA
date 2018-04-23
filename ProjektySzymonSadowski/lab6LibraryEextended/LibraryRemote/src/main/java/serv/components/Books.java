package serv.components;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.Serializable;
import java.util.List;

@Singleton
@Startup
public class Books implements Serializable {

    private List<Book> bookList;

    @EJB
    private BookXmlParser bookXmlParser;

    @PostConstruct
    public void setupBooks() {
        bookList = bookXmlParser.getBookList();
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
