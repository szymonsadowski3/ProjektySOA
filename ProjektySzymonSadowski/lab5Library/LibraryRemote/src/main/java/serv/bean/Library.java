package serv.bean;

import serv.components.Books;

public interface Library {

    void reserveBook(int bookId);

    void borrowBook(int bookId);

    void returnBook(int bookId);

    Books getBooks();

    String getResultMessage();
}
