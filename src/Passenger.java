public class Passenger {
    // Instance Variables
    private String firstName;
    private String surname;
    private int seatNumber;
    private int secondsInQueue;

    public String getName() {
        return this.firstName + " " + this.surname;
    }

    public void setName(String name, String surname) {
        this.surname = surname;
        this.firstName = name;
    }

    public int getSeconds() {
        return this.secondsInQueue;
    }

    public void setSecondsInQueue(int sec) {
        this.secondsInQueue = sec;
    }

    //    Display
    public void display() {
        System.out.println("-------Passenger Details------\nFirst Name : " + this.firstName + '\n' + "Surname : " + this.surname + '\n' + "Seconds In Queue : " + this.secondsInQueue + '\n' + "Seat Number : " + this.seatNumber);
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}