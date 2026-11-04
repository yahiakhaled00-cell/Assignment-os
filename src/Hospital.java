import java.util.Scanner;

public class Hospital {

    private static final Object LOCK = new Object();

    public static void log(String msg) {
        synchronized (LOCK) {
            System.out.println(msg);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Waiting room capacity: ");
        int capacity = sc.nextInt();
        System.out.print("Number of doctors: ");
        int numDoctors = sc.nextInt();
        System.out.print("Number of patients: ");
        int numPatients = sc.nextInt();
        sc.close();

        if (capacity <= 0 || numDoctors <= 0 || numPatients <= 0) {
            System.out.println("All inputs must be positive integers.");
            return;
        }

        WaitingRoom waitingRoom = new WaitingRoom(capacity);
        TreatmentRooms treatmentRooms = new TreatmentRooms(2);

        Doctor[] doctors = new Doctor[numDoctors];
        for (int i = 0; i < numDoctors; i++) {
            doctors[i] = new Doctor(i + 1, waitingRoom, treatmentRooms);
        }

        Patient[] patients = new Patient[numPatients];
        StringBuilder sb = new StringBuilder("Patients arriving:\n");
        for (int i = 0; i < numPatients; i++) {
            patients[i] = new Patient(i + 1, waitingRoom);
            sb.append("P").append(i + 1).append(" ");
        }
        log(sb.toString().trim());

        for (Doctor d : doctors) d.start();
        for (Patient p : patients) {
                p.start();
                Thread.sleep(100);   
            }

        for (Patient p : patients) p.join();

        log("All patients have been treated.");

        for (Doctor d : doctors) d.interrupt();
        for (Doctor d : doctors) d.join();

        log("Simulation completed.");
    }
}