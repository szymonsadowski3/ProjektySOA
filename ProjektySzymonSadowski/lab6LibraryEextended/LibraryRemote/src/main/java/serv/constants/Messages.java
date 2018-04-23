package serv.constants;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Messages {
    public static String BORROW_SUCCESS = "You succesfully borrowed book";
    public static String ALREADY_BORROWED = "Book is already borrowed";
    public static String ALREADY_RESERVED = "Book is already reserved";
    public static String BOOK_NOT_FOUND = "Book was not found in library";
    public static String BOOK_RETURNED = "You succesfully returned book";
    public static String BOOK_AVAILABLE = "Book is available...";
    public static String SUCCESS_RESERVED = "You sucessfully reserved book";
}
