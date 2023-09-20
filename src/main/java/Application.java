import Controller.Controller;
import DAO.AuthorDAO;
import DAO.PaintingDAO;
import Exceptions.PaintingAlreadyExistsException;
import Model.Painting;
import Service.AuthorService;
import Service.PaintingService;
import Util.ConnectionSingleton;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Connection conn = ConnectionSingleton.getConnection();
        PaintingDAO paintingDAO = new PaintingDAO(conn);
        AuthorDAO authorDAO = new AuthorDAO(conn);
        AuthorService authorService = new AuthorService(authorDAO);
        PaintingService paintingService = new PaintingService(paintingDAO, authorService);


        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("1: add new painting 2: get paintings by author 3: update the author of painting " +
                    "4: delete painting by title 5: search paintings by year 6: search paintings before year");
            int choice = Integer.parseInt(sc.nextLine());
            if(choice == 1){
                System.out.println("enter painting id");
                int paintingId = Integer.parseInt(sc.nextLine());
                System.out.println("enter title");
                String title = sc.nextLine();
                System.out.println("enter author");
                String author = sc.nextLine();
                System.out.println("enter year made");
                int yearMade = Integer.parseInt(sc.nextLine());
                Painting p = new Painting(paintingId, title, 0, yearMade);

                try{
                    paintingService.savePainting(p, author);
                }catch(PaintingAlreadyExistsException e){
                    System.out.println("That painting already exists!");
                }


            }else if(choice == 2){

                System.out.println("enter author");
                String author = sc.nextLine();
                List<Painting> paintingList = paintingService.getPaintingsByAuthor(author);
                System.out.println(paintingList);

            }else if(choice ==3) {

                System.out.println("enter title");
                String title = sc.nextLine();
                System.out.println("enter author");
                String author = sc.nextLine();
                Painting p = new Painting();
                paintingService.updatePainting(p);

            }else if(choice == 4){

                System.out.println("enter title");
                String title = sc.nextLine();
                paintingService.deletePainting(title);

            }else if(choice == 5){
                System.out.println("enter year");
                int yearInput = Integer.parseInt(sc.nextLine());
                List<Painting> paintingList = paintingService.getPaintingsFromYear(yearInput);
                System.out.println(paintingList);

            }else if(choice == 6){
                System.out.println("enter year");
                int yearInput = Integer.parseInt(sc.nextLine());
                List<Painting> paintingList = paintingService.getPaintingsBeforeYear(yearInput);
                System.out.println(paintingList);

            }else{
                System.out.println("invalid choice");
            }
        }

    }
}