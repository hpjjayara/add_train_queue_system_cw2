import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.application.Application;
import javafx.stage.Stage;

import static javafx.application.Platform.exit;

public class TrainStation extends Application {
    private Passenger[] waitingRoom;
    private PassengerQueue trainQueue;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }

    private void addToWaitingRoom(Scanner input) {
        try {
            System.out.println("Enter route: \n 1. Colombo to Badulla\n 2. Badulla to Colombo");
            String routeS = input.next();

            int route = Integer.parseInt(routeS);
            String routeOption = "";
            if (route == 1) {
                routeOption = "Colombo to Badulla";
            } else if (route == 2) {
                routeOption = "Badulla to Colombo";
            }

            File toRead = new File("C:\\Users\\user\\IdeaProjects\\New\\CourseWork\\src\\data.txt");
            FileInputStream fis = new FileInputStream(toRead);

            Scanner sc = new Scanner(fis);

            Map<Integer, Passenger> waP = new HashMap<>();
            //read data from file line by line:
            String currentLine;
            int i = 0;
            while (sc.hasNextLine()) {
                currentLine = sc.nextLine();
                String[] str = currentLine.split(",");
                String bookedDate = str[3];
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateNow = new Date();

                if (bookedDate.equals(dateFormat.format(dateNow)) && routeOption.equals(str[2].trim())) {
                    Passenger passenger = new Passenger();
                    String[] name = str[0].split(" ");
                    if (name.length == 2 && name.length > 0) {
                        passenger.setName(name[0].trim(), name[1].trim());
                    } else {
                        passenger.setName(name[0].trim(), "");
                    }

                    passenger.setSeatNumber(Integer.parseInt(str[1]));
                    waP.put(i, passenger);
                    i++;
                }
            }
            fis.close();
            waitingRoom = new Passenger[waP.size()];
            for (int j = 0; j < waP.size(); j++) {
                waitingRoom[j] = waP.get(j);
            }

        } catch (Exception e) {
            System.out.println("Error in Read Data");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner sc = new Scanner(System.in);
        addToWaitingRoom(sc);
        String option = "z";
        trainQueue = new PassengerQueue();

        while (!option.equalsIgnoreCase("Q")) { //select the options
            System.out.println("\n<<------Welcome to Train Booking System !!!------>>");
            System.out.println("Enter \"A\" to add a passenger :");
            System.out.println("Enter \"V\" to view all the trainQueue :");
            System.out.println("Enter \"D\" to delete passenger from the trainQueue :");
            System.out.println("Enter \"S\" to store trainQueue data to file :");
            System.out.println("Enter \"L\" to load data from file :");
            System.out.println("Enter \"R\" running the simulation details :");
            System.out.println("Enter \"Q\" quite the programme :");

            option = sc.next();

            switch (option) {
                case "A":
                case "a":
                    try {
                        System.out.println("Add a passenger:");
                        this.waitingRoom = trainQueue.addPassenger(waitingRoom);
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;
                case "V":
                case "v":
                    try {
                        System.out.println("View the train queue");
                        trainQueue.viewQueue();
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;
                case "D":
                case "d":
                    try {
                        System.out.println("Delete Booking\nEnter Passenger\'s first name");
                        String firstName = sc.next();
                        System.out.println("Enter Passenger\'s surname");
                        String surName = sc.next();
                        String nameDelete = firstName.trim() + " " + surName.trim();
                        trainQueue.delete(nameDelete);
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;
                case "S":
                case "s":
                    try {
                        System.out.println("Store Data to File");
                        trainQueue.storeDataToFile();
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;
                case "L":
                case "l":
                    try {
                        System.out.println("Load Data from File");
                        trainQueue.loadDataFromFile();
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;
                case "R":
                case "r":
                    try {
                        System.out.println("Run Simulation");
                        trainQueue.runSimulation();
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;
                case "Q":
                case "q":
                    System.out.println("quit the program");
                    exit();
                    break;
            }
        }
    }
}




