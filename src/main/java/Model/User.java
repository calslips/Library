package Model;

import java.util.Objects;

public class User {
    static int nextId = 1;
    private int userId;
    private String username;
//    private int yearBorn;
//    private String authorNationality;

    public User(String username) {
        userId = User.nextId++;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

//    public void setAuthorId(int authorId) {
//        this.authorId = authorId;
//    }

    public String getUsername() {
        return username;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

//    public int getYearBorn() {
//        return yearBorn;
//    }
//
//    public void setYearBorn(int yearBorn) {
//        this.yearBorn = yearBorn;
//    }
//
//    public String getAuthorNationality() {
//        return authorNationality;
//    }
//
//    public void setAuthorNationality(String authorNationality) {
//        this.authorNationality = authorNationality;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.userId == user.userId && Objects.equals(this.username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
