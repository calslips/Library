package Model;

import java.util.Objects;

public class Book extends Object{
    static int nextId = 1;
    private int bookId;
    private String title;
    private String author;
    private int signedOutBy;

    public Book(String author, String title){
        bookId = Book.nextId++;
        this.author = author;
        this.title = title;
        signedOutBy = 0;
    }

//    public Book(int bookId, String title, int authorFkey) {
//        this.bookId = bookId;
//        this.title = title;
//        this.authorFkey = authorFkey;
//    }

    public int getBookId() {
        return bookId;
    }

//    public void setPaintingId(int paintingId) {
//        this.paintingId = paintingId;
//    }

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

//    public int getYearMade() {
//        return yearMade;
//    }

//    public void setYearMade(int yearMade) {
//        this.yearMade = yearMade;
//    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", signedOutBy=" + signedOutBy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId &&
                Objects.equals(author, book.author) &&
                Objects.equals(title, book.title);
//                && signedOutBy == book.signedOutBy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, author);
    }
}
