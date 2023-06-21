package sk.stuba.fei.uim.vsa.pr1a;

import sk.stuba.fei.uim.vsa.pr1a.utility.FromKeyboard;
import sk.stuba.fei.uim.vsa.pr1a.entities.*;

import javax.security.auth.kerberos.KerberosKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class App {

    public App() throws ParseException {
        CarParkService app = new CarParkService();
        app.createCarPark("Parkovisko Ikea", "Ivanská cesta 16, 82104, Bratislava" ,2);
        app.createCarPark("Aupark", "Jozova cesta, 82104, Bratislava" ,2);
        app.createCarPark("Bory", "Andrejina 16, 82104, Bratislava" ,3);

        app.createCarParkFloor(1L,"3P");
        app.createCarParkFloor(2L,"1P");
        app.createCarParkFloor(2L,"2P");
        app.createCarParkFloor(2L,"3P");
        app.createCarParkFloor(3L,"3P");

        app.createParkingSpot(2L,"3P", "246");
        app.createParkingSpot(2L,"3P", "247");
        app.createParkingSpot(2L,"3P", "248");
        app.createParkingSpot(1L,"3P", "245");

        System.out.println("WELCOME TO RESERVATION SYSTEM FOR PARKING");

        while(true){
            int choice = FromKeyboard.readInt("Press 0 for customers/ press 1 for developers/ press 2 for exit");
            if (choice==0){
                choice = FromKeyboard.readInt("0 = create new user/ 1 = log existing user");
                if (choice==0){
                    app.createUser(FromKeyboard.readString("First name= "),FromKeyboard.readString("Last name= "), FromKeyboard.readString("email= "));

                }
                else {
                    System.out.println(app.getUsers());
                    Long userId = (long) FromKeyboard.readInt("Write User ID from existing users= ");
                    User user = (User) app.getUser(userId);
                    System.out.println("Welcome " + user.getFirstname());
                    while(true){
                        choice = FromKeyboard.readInt("0 = create car/1 = show your cars/2 = create reservations/ 3 = end reservations/4 = show reservations /5 = availability of Car Parks /6 = log off");
                        if (choice == 0){
                            //create car
                            app.createCar(user.getId(), FromKeyboard.readString("Brand="), FromKeyboard.readString("Model=" ),
                                    FromKeyboard.readString("Color= "),FromKeyboard.readString("vehicleRegistrationPlate"));
                            System.out.println("Car created, press any key to continue");
                            Scanner s = new Scanner(System.in);
                            s.nextLine();
                            //enter
                        }
                        if (choice == 1) {
                            System.out.println(app.getCars(userId));
                            System.out.println("Press any key to continue");
                            Scanner s = new Scanner(System.in);
                            s.nextLine();
                        }
                        if (choice == 2) {
                            //create reservation
                            System.out.println("Available Car Parks: ");
                            System.out.println(app.getCarParks());
                            Long carParkId = (long) FromKeyboard.readInt("Select Car Park Id = ");
                            CarPark carPark = (CarPark) app.getCarPark(carParkId);
                            System.out.println("Available Parking spots: ");
                            System.out.println(app.getAvailableParkingSpots(carPark.getName()));
                            Long spotId = (long)FromKeyboard.readInt("Select spot ID for your reservation= ");
                            System.out.println(app.getCars(userId));
                            Long carId = (long)FromKeyboard.readInt("Select car ID of your car");
                            System.out.println(app.createReservation(spotId,carId));
                            System.out.println("Reservation created, press any key to continue");
                            Scanner s = new Scanner(System.in);
                            s.nextLine();
                        }
                        if (choice == 3) {
                            //end reservation
                            System.out.println("Your active reservations: ");
                            System.out.println(app.getMyReservations(userId));
                            Long reservationId = (long)FromKeyboard.readInt("Select ID of reservation you want to end");
                            char choice2 = FromKeyboard.readChar("Do you want to use your coupon? y/n");
                            if (choice2=='n'){
                                System.out.println(app.endReservation(reservationId));
                                System.out.println("Reservation ended, press any key to continue");
                            }
                            if (choice2=='y'){
                                System.out.println("Your coupons");
                                System.out.println(app.getCoupons(userId));
                                Long couponId = (long) FromKeyboard.readInt("Selest ID of coupon you want to use, or 0 if you dont want use any");
                                if (couponId==0){
                                    System.out.println(app.endReservation(reservationId));
                                    System.out.println("Reservation ended, press any key to continue");
                                    continue;
                                }
                                System.out.println(app.endReservation(reservationId,couponId));
                                System.out.println("Reservation ended, press any key to continue");
                            }

                            Scanner s = new Scanner(System.in);
                            s.nextLine();

                        }
                        if (choice == 4) {
                            System.out.println("Check reservations for specific spot and for specific day" );
                            String sDate1 = FromKeyboard.readString("Write date (format is dd/MM/yyyy)  = ");
                            Date date1  =new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                            System.out.println(date1.toString());
                            System.out.println(app.getReservations((long)FromKeyboard.readInt("Parking spot ID= "),date1));
                            System.out.println("Press any key to continue");
                            Scanner s = new Scanner(System.in);
                            s.nextLine();
                        }
                        if (choice == 5) {
                            System.out.println("Available Car Parks: ");
                            System.out.println(app.getCarParks());
                            Long carParkId = (long) FromKeyboard.readInt("Select Car Park Id = ");
                            CarPark carPark = (CarPark) app.getCarPark(carParkId);
                            System.out.println("Available= "+ app.getAvailableParkingSpots(carPark.getName()));
                            System.out.println("Occupied= " + app.getOccupiedParkingSpots(carPark.getName()));
                            System.out.println("Press any key to continue");
                            Scanner s = new Scanner(System.in);
                            s.nextLine();
                        }
                        if (choice == 6) {
                            break;
                        }
                    }
                }
                choice=0;
            }
            if (choice==1){
                System.out.println("Welcome developer, there are not all CRUD functions involved because this assigment tests our JPA skills, not CLI\n" +
                        "But customers have got all necessary functions for using this app");
                choice = FromKeyboard.readInt("0= Create coupon/ 1= Add coupon to user/ 2= Back");
                if (choice == 0){
                    app.createDiscountCoupon(FromKeyboard.readString("Name of coupon: "),FromKeyboard.readInt("Amount of discount in % = "));
                    System.out.println("Coupon created");
                }
                if (choice == 1){
                    app.giveCouponToUser((long)FromKeyboard.readInt("Put ID of coupon you want to connect to"),(long) FromKeyboard.readInt("Put ID of user who will get coupon"));
                    System.out.println("Coupon added");
                }
                if(choice==2){
                    continue;
                }
                choice =0;
            }
            if (choice==2) {
                break;
            }

        }
    }
}

