package serv.constants;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Constants {
    public static String BOOK = "book";
    public static String AUTHOR = "author";
    public static String TITLE = "title";
    public static String ID = "id";
    public static String ISBN = "isbn";
}
