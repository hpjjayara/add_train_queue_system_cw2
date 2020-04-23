import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class PassengerQueue {
    private int first;
    private int last;
    private int maxStayInQueue;
    private int size;
    private Passenger[] queueArray;
    private int number;
    private int maxLength;

    public PassengerQueue() {
        size = 42;
        queueArray = new Passenger[size];
        first = last = -1;
        number = 0;
        maxStayInQueue = 0;
        maxLength = 0;
    }

    public Passenger[] addPassenger(Passenger[] waitingRoom) {
        // check if waiting room has passengers
        boolean empty = true;
        for (int i = 0; i < waitingRoom.length; i++) {
            if (waitingRoom[i] != null) {
                empty = false;
                break;
            }
        }

        if (!empty) {
            // show waiting queue in GUI
            Stage stage = new Stage();
            VBox vbox = new VBox(20);
            vbox.setStyle("-fx-padding: 10;");
            Scene scene = new Scene(vbox, 850, 500);
            stage.setScene(scene);
            stage.setTitle("Add to the trainQueue ");

            GridPane gridPaneSeat = new GridPane();
            gridPaneSeat.setVisible(true);

            int col = 0;
            int row = 0;

            for (int i = 0; i < waitingRoom.length; i++) {
                if (row != 0 && row % 5 == 0)
                    col++;

                gridPaneSeat.setVgap(10);
                gridPaneSeat.setHgap(40);

                if (waitingRoom[i] != null) {
                    TextField taResult = new TextField();
                    taResult.setText(waitingRoom[i].getName());
                    taResult.setEditable(false);
                    gridPaneSeat.add(taResult, row % 5, col);
                    row++;
                }
            }

            vbox.getChildren().add(gridPaneSeat);
            stage.setScene(scene);
            stage.showAndWait();

            // Add to train queue from waiting room
            int randomNumber = getRandomNumberInRange(1, 6);
            int noAddToQueue = 0;
            for (int i = 0; i < randomNumber; i++) {
                boolean isAdd = false;

                for (int j = 0; j < waitingRoom.length; j++) {
                    if (waitingRoom[j] != null) {
                        this.add(waitingRoom[j]);
                        noAddToQueue++;
                        waitingRoom[j].display();
                        waitingRoom[j] = null;
                        isAdd = true;
                        break;
                    }
                }

                if (!isAdd) {
                    System.out.println("Waiting room is empty");
                    break;
                }

            }
            System.out.println("Number of passengers add to the queue from waiting room: "+noAddToQueue);
        } else {
            System.out.println("Waiting room is empty");
        }
        return waitingRoom;
    }

    // add passenger to queue
    private void add(Passenger next) {
        if (isFull()) {
            System.out.println("Train queue is full.");
        } else {
            last = (last + 1) % size;
            queueArray[last] = next;
            number++;

            if (first == -1) {
                first = last;
            }
        }
    }

    // view queue
    public void viewQueue() {
        Stage stage = new Stage();
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(vbox, 1350, 350);
        stage.setScene(scene);
        stage.setTitle("View the trainQueue ");

        GridPane gridPaneSeat = new GridPane();
        int index = first;
        Map<Integer, String> passengerListMap = new HashMap<>();
        if (index != -1) {
            while (index != (last + 1) % size) {
                Passenger p = queueArray[index];
                passengerListMap.put(p.getSeatNumber(), p.getName());
                index = (index + 1) % size;
            }
        }

        gridPaneSeat.setVisible(true);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                int arrayNumber = i * 6 + j;
                TextField seat = new TextField();
                seat.setEditable(false);
                if (passengerListMap.containsKey(arrayNumber + 1)) {
                    seat.setText((i * 6 + j + 1) + " : " + passengerListMap.get(arrayNumber + 1));
                    seat.setStyle("-fx-background-color: #00ff00");
                } else {
                    seat.setText((i * 6 + j + 1) + " : Empty");
                    seat.setStyle("-fx-background-color: #ff4d4d"); //empty seats are red
                }

                gridPaneSeat.setVgap(10);
                gridPaneSeat.setHgap(40);
                gridPaneSeat.add(seat, j, i + 3);
            }

        }
        vbox.getChildren().add(gridPaneSeat);
        stage.setScene(scene);
        stage.showAndWait();
    }

    // delete
    public void delete(String nameDelete) {
        int i = first;
        int index = -1;
        int l = last + 1;

        while (i <= last) {
            if (queueArray[i].getName().trim().equals(nameDelete.trim())) {
                index = i;
                break;
            }
            i = (i + 1) % size;
        }
        if (index != -1) {
            // reorder array such that passenger need to be deleted come to first
            if (index != first) {
                Passenger toDelete = queueArray[index];

                Boolean isReOrder = false;
                int next;
                int node = index;
                while (!isReOrder) {
                    if (node == 0) {
                        next = (size - 1) % size;
                    } else {
                        next = (node - 1) % size;
                    }
                    queueArray[node] = queueArray[next];

                    if (next == first) {
                        isReOrder = true;
                    }
                    node = next;
                }
                queueArray[first] = toDelete;
            }
            Passenger deleted = this.remove();
            deleted.display();
        } else {
            System.out.println("Cannot find Passenger for given name in the queue.");
        }
    }

    // remove first passenger from the queue
    public Passenger remove() {
        if (isEmpty())
            return null;
        Passenger passengerRemove = queueArray[first];
        queueArray[first] = null;
        first = (first + 1) % size;
        number--;
        return passengerRemove;
    }

    // store data to file
    public void storeDataToFile() {
        //write to file : "data"
        if (!isEmpty()) {
            try {
                File file = new File("src/trainQueueData.txt");
                FileOutputStream fos = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(fos);
                int index = first;
                while (index != (last + 1) % size) {
                    Passenger p = queueArray[index];
                    pw.println(p.getName() + "," + p.getSeconds() + "," + p.getSeatNumber());
                    index = (index + 1) % size;
                }

                pw.flush();
                pw.close();
                fos.close();
            } catch (Exception e) {
                System.out.println("Error in file writing");
            }
        } else {
            System.out.println("Empty Train Queue");
        }
    }

    // load data to queue from file
    public void loadDataFromFile() {
        //read from file
        try {
            List<Integer> seatNumberList = new ArrayList<>();
            int index = first;
            if (index != -1) {
                while (index != (last + 1) % size) {
                    Passenger p = queueArray[index];
                    seatNumberList.add(p.getSeatNumber());
                    index = (index + 1) % size;
                }
            }

            File toRead = new File("src/trainQueueData.txt");
            FileInputStream fis = new FileInputStream(toRead);

            Scanner sc = new Scanner(fis);

            //read data from file line by line:
            String currentLine;
            while (sc.hasNextLine()) {
                currentLine = sc.nextLine();

                String[] passenger = currentLine.split(",");
                if(!seatNumberList.contains(Integer.parseInt(passenger[2]))){
                    String[] name = passenger[0].split(" ");

                    Passenger newPassenger = new Passenger();
                    if (name.length != 2 && name.length > 0) {
                        newPassenger.setName(name[0], "");
                    } else {
                        newPassenger.setName(name[0], name[1]);
                    }

                    newPassenger.setSecondsInQueue(Integer.parseInt(passenger[1]));
                    newPassenger.setSeatNumber(Integer.parseInt(passenger[2]));
                    this.add(newPassenger);
                }

            }
            fis.close();
        } catch (Exception e) {
            System.out.println("Error in Read Data");
        }
    }

    // run simulation
    public void runSimulation() {
        if (isEmpty() || first == -1) {
            System.out.println("Queue is empty");
            return;
        }
        int index = first;
        int max = -1;
        int min = -1;
        int totalTime = 0;
        int queueSize = getLength();
        Map<Integer, ArrayList<Passenger>> mapPassenger = new HashMap<>();
        while (index != (last + 1) % size) {
            Passenger p = queueArray[index];
            int secInQueue = getRandomNumberInRange(1, 6) + getRandomNumberInRange(1, 6) + getRandomNumberInRange(1, 6);
            p.setSecondsInQueue(secInQueue);
            if (max < secInQueue || max == -1) {
                max = secInQueue;
            }
            if (min > secInQueue || min == -1) {
                min = secInQueue;
            }
            totalTime = totalTime + secInQueue;
            if (mapPassenger.containsKey(secInQueue)) {
                ArrayList<Passenger> pL = mapPassenger.get(secInQueue);
                pL.add(p);
                mapPassenger.put(secInQueue, pL);
            } else {
                ArrayList<Passenger> pL = new ArrayList<>();
                pL.add(p);
                mapPassenger.put(secInQueue, pL);
            }

            index = (index + 1) % size;
        }

        if (maxLength < queueSize) {
            maxLength = queueSize;
        }
        if (maxStayInQueue < max) {
            maxStayInQueue = max;
        }
        float avg = (float) totalTime / queueSize;

        Map<Integer, ArrayList<Passenger>> reverseSortedMap = new TreeMap<>(Collections.reverseOrder());
        reverseSortedMap.putAll(mapPassenger);

        // Display the TreeMap which is naturally sorted
        for (Map.Entry<Integer, ArrayList<Passenger>> entry : reverseSortedMap.entrySet()) {
            for (Passenger p : entry.getValue()) {
                this.delete(p.getName());
            }
        }

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        Stage stage = new Stage();
        stage.setY(primaryScreenBounds.getMinY());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.setWidth(650);
        stage.setTitle("Run Simulation");

        Scene scene = new Scene(new Group());

        final Label titleLabel = new Label("Report");
        titleLabel.setStyle("-fx-font-weight: bold;");
        titleLabel.setPadding(new Insets(16));
        titleLabel.setFont(new Font("Arial", 20));
        final Label maxLengthLabel = new Label("Maximum length of the queue : " + getMaxStay());
        final Label maxLabel = new Label("Maximum waiting time :" + max);
        final Label minLabel = new Label("Minimum waiting time :" + min);
        final Label avgLabel = new Label("Average waiting time :" + avg + "\n");

        TableView<Passenger> table = new TableView<>();
        table.setEditable(false);

        TableColumn firstNameCol = new TableColumn("Name");
        firstNameCol.setMinWidth(300);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Passenger, String>("name"));

        TableColumn seatNumberCol = new TableColumn("Seat Number");
        seatNumberCol.setMinWidth(100);
        seatNumberCol.setCellValueFactory(new PropertyValueFactory<Passenger, String>("seatNumber"));

        TableColumn secondsInQueueCol = new TableColumn("Seconds In Queue");
        secondsInQueueCol.setMinWidth(200);
        secondsInQueueCol.setCellValueFactory(new PropertyValueFactory<Passenger, String>("seconds"));

        for (Map.Entry<Integer, ArrayList<Passenger>> entry : reverseSortedMap.entrySet()) {
            for (Passenger p : entry.getValue()) {
                table.getItems().add(p);
            }
        }

        table.getColumns().addAll(firstNameCol, seatNumberCol, secondsInQueueCol);

        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.41)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(titleLabel, maxLengthLabel, maxLabel, minLabel, avgLabel, table);


        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.showAndWait();
    }

    // random number generation
    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    //check queue is empty
    public boolean isEmpty() {
        return getLength() == 0;
    }

    //check queue is full
    public boolean isFull() {
        return getLength() == size;
    }

    // get length of queue
    public int getLength() {
        return number;
    }

    // get maxlength in queue
    public int getMaxStay() {
        return this.maxLength;
    }

}






