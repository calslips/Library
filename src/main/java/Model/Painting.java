package Model;

import java.util.Objects;

public class Painting extends Object{
    private int paintingId;
    private String title;
    private int authorFkey;
    private int yearMade;

    public Painting(){

    }

    public Painting(int paintingId, String title, int authorFkey, int yearMade) {
        this.paintingId = paintingId;
        this.title = title;
        this.authorFkey = authorFkey;
        this.yearMade = yearMade;
    }

    public int getPaintingId() {
        return paintingId;
    }

    public void setPaintingId(int paintingId) {
        this.paintingId = paintingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorFkey() {
        return authorFkey;
    }

    public void setAuthorFkey(int authorFkey) {
        this.authorFkey = authorFkey;
    }

    public int getYearMade() {
        return yearMade;
    }

    public void setYearMade(int yearMade) {
        this.yearMade = yearMade;
    }

    @Override
    public String toString() {
        return "Painting{" +
                "paintingId=" + paintingId +
                ", title='" + title + '\'' +
                ", authorFkey=" + authorFkey +
                ", yearMade=" + yearMade +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Painting painting = (Painting) o;
        return paintingId == painting.paintingId && authorFkey == painting.authorFkey && yearMade == painting.yearMade && Objects.equals(title, painting.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paintingId, title, authorFkey, yearMade);
    }
}
