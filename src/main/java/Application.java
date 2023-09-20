import Controller.Controller;
//import DAO.AuthorDAO;
//import DAO.PaintingDAO;
//import Exceptions.PaintingAlreadyExistsException;
//import Model.Painting;
//import Service.AuthorService;
//import Service.PaintingService;
import DAO.UserDAO;
import DAO.BookDAO;
import Model.User;
import Service.UserService;
import Service.BookService;
import Model.Book;
import Util.ConnectionSingleton;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Connection conn = ConnectionSingleton.getConnection();
        UserDAO userDAO = new UserDAO(conn);
        BookDAO bookDAO = new BookDAO(conn);
        UserService userService = new UserService(userDAO);
        BookService bookService = new BookService(bookDAO);

//        PaintingDAO paintingDAO = new PaintingDAO(conn);
//        AuthorDAO authorDAO = new AuthorDAO(conn);
//        AuthorService authorService = new AuthorService(authorDAO);
//        PaintingService paintingService = new PaintingService(paintingDAO, authorService);


        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("1: add new book 2: search for book by author 3: search for book by title " +
                    "4: sign out book " +
                    "5: return book " +
                    "6: create account " +
                    "7: delete account");
            int choice = Integer.parseInt(sc.nextLine());
            if(choice == 1){
                // adding new book
                System.out.println("enter author name");
                String author = sc.nextLine();
                System.out.println("enter book title");
                String title = sc.nextLine();

                Book b = new Book(author, title);
                bookService.addBook(b);

//                try{
//                    paintingService.savePainting(p, author);
//                }catch(PaintingAlreadyExistsException e){
//                    System.out.println("That painting already exists!");
//                }


            }else if(choice == 2){
                // search for book by author
                System.out.println("enter author");
                String author = sc.nextLine();
                List<Book> bookList = bookService.getBooksByAuthor(author);
                System.out.println(bookList);

            }else if(choice ==3) {
                // search for book by title
                System.out.println("enter title");
                String title = sc.nextLine();

                List<Book> bookList = bookService.getBooksByTitle(title);
                System.out.println(bookList);

//                Painting p = new Painting();
//                paintingService.updatePainting(p);

            }else if(choice == 4){
                // sign out book (update signed out by userId)
                System.out.println("enter title");
                String title = sc.nextLine();
                System.out.println("enter author");
                String author = sc.nextLine();
                System.out.println("enter your user id");
                int userId = Integer.parseInt(sc.nextLine());
                System.out.println("enter the book id");
                int bookId = Integer.parseInt(sc.nextLine());
                bookService.signOutBook(title, author, userId, bookId);

            }else if(choice == 5){
                // return book
                System.out.println("enter title");
                String title = sc.nextLine();
                System.out.println("enter author");
                String author = sc.nextLine();
                System.out.println("enter your user id");
                int userId = Integer.parseInt(sc.nextLine());
                System.out.println("enter the book id");
                int bookId = Integer.parseInt(sc.nextLine());
                bookService.returnBook(title, author, bookId);

//                System.out.println("enter year");
//                int yearInput = Integer.parseInt(sc.nextLine());
//                List<Painting> paintingList = paintingService.getPaintingsFromYear(yearInput);
//                System.out.println(paintingList);

            }else if(choice == 6){
                // create user account
                System.out.println("enter username");
                String username = sc.nextLine();

                while (userService.checkUser(username)) {
                    System.out.println("Username taken, choose another one.");
                    username = sc.nextLine();
                }
                User newUser = new User(username);
                userService.createUser(newUser);


//                System.out.println("enter year");
//                int yearInput = Integer.parseInt(sc.nextLine());
//                List<Painting> paintingList = paintingService.getPaintingsBeforeYear(yearInput);
//                System.out.println(paintingList);

            }else if(choice == 7){
                // delete user account
                System.out.println("enter username");
                String username = sc.nextLine();
                userService.deleteUser(username);


//                System.out.println("enter year");
//                int yearInput = Integer.parseInt(sc.nextLine());
//                List<Painting> paintingList = paintingService.getPaintingsBeforeYear(yearInput);
//                System.out.println(paintingList);

            }else{
                System.out.println("invalid choice");
            }
        }

    }
}