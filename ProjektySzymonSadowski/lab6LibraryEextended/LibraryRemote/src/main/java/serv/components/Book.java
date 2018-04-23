package serv.components;

import java.io.Serializable;

public class Book implements Serializable {

    private static final long serialVersionUID = 1190476516911661470L;

    private int id;
    private String author;
    private String title;
    private String isbn;

    private boolean borrowed = false;
    private boolean reserved = false;

    public Book(int id, String author, String titleEng, String isbn) {
        this.id = id;
        this.author = author;
        this.title = titleEng;
        this.isbn = isbn;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String toString() {
        return title + ", " + author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
