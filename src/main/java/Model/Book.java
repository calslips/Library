package Model;

import java.util.Objects;

public class Book extends Object{
   // static int nextId = 1;
    private int bookId;
    private String title;
    private String author;
    private int signedOutBy;

    public Book(){
    }



    public Book(String author, String title){
       // bookId = Book.nextId++;
        this.author = author;
        this.title = title;
        signedOutBy = 0;
    }

    public Book(int bookId, String author, String title){
        this.bookId = bookId;
        this.author = author;
        this.title = title;
        signedOutBy = 0;
    }

    public Book(int bookId, String author, String title, int signedOutBy){
        this.bookId = bookId;
        this.author = author;
        this.title = title;
        this.signedOutBy = signedOutBy;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
       this.bookId = bookId;
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

    public int getSignedOutBy() {
        return signedOutBy;
    }

    public void setSignedOutBy(int userId) {
        this.signedOutBy = userId;
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
