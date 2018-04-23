package serv.bean;

import serv.components.Book;
import serv.components.Books;
import serv.constants.Messages;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Remote(Library.class)
public class LibraryBean implements Library, Serializable {

    @EJB
    private Books books;

    private String resultMessage;

    @Override
    public void borrowBook(int bookId) {
        List<Book> matchingBooks = getBookById(bookId);

        if (matchingBooks.size() > 0) {
            Book matchedBook = matchingBooks.get(0);

            if (!matchedBook.isBorrowed()) {
                matchingBooks.get(0).setBorrowed(true);
                resultMessage = Messages.BORROW_SUCCESS;
            } else {
                resultMessage = Messages.ALREADY_BORROWED;
            }
        } else {
            resultMessage = Messages.BOOK_NOT_FOUND;
        }
    }

    private List<Book> getBookById(int bookId) {
        return books.getBookList().stream()
                    .filter(book -> book.getId() == bookId)
                    .collect(Collectors.toList());
    }

    @Override
    public void returnBook(int bookId) {
        List<Book> matchingBooks = getBookById(bookId);

        if (matchingBooks.size() > 0) {
            Book matchedBook = matchingBooks.get(0);

            if (matchedBook.isBorrowed()) {
                matchingBooks.get(0).setBorrowed(false);
                resultMessage = Messages.BOOK_RETURNED;
            } else {
                resultMessage = Messages.BOOK_AVAILABLE;
            }
        } else {
            resultMessage = Messages.BOOK_NOT_FOUND;
        }
    }

    @Override
    public void reserveBook(int bookId) {
        List<Book> matchingBooks = getBookById(bookId);

        if (matchingBooks.size() > 0) {
            Book matchedBook = matchingBooks.get(0);

            if (!matchedBook.isBorrowed() && !matchedBook.isReserved()) {
                matchingBooks.get(0).setReserved(true);
                matchingBooks.get(0).setReserved(true);
                resultMessage = Messages.SUCCESS_RESERVED;
            } else if (matchedBook.isReserved()) {
                resultMessage = Messages.ALREADY_RESERVED;
            } else {
                resultMessage = Messages.ALREADY_BORROWED;
            }
        } else {
            resultMessage = Messages.BOOK_NOT_FOUND;
        }
    }

    @Override
    public Books getBooks() {
        return books;
    }

    @Override
    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
