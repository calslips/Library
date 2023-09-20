package Model;

import java.util.Objects;

public class Author {
    private int authorId;
    private String name;
    private int yearBorn;
    private String authorNationality;

    public Author(int authorId, String name, int yearBorn, String authorNationality) {
        this.authorId = authorId;
        this.name = name;
        this.yearBorn = yearBorn;
        this.authorNationality = authorNationality;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearBorn() {
        return yearBorn;
    }

    public void setYearBorn(int yearBorn) {
        this.yearBorn = yearBorn;
    }

    public String getAuthorNationality() {
        return authorNationality;
    }

    public void setAuthorNationality(String authorNationality) {
        this.authorNationality = authorNationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return authorId == author.authorId && yearBorn == author.yearBorn && Objects.equals(name, author.name) && Objects.equals(authorNationality, author.authorNationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, name, yearBorn, authorNationality);
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", name='" + name + '\'' +
                ", yearBorn=" + yearBorn +
                ", authorNationality='" + authorNationality + '\'' +
                '}';
    }
}
