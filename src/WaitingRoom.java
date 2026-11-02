import java.util.Queue;
import java.util.ArrayDeque;


public class WaitingRoom {
    private 
    Queue<Patient> patients;
    int maxNumber;

    public WaitingRoom(int capacity) {
        this.maxNumber = capacity;
        patients = new ArrayDeque<>(capacity);
    }

    
    public void addPatient(Patient newPatient) throws InterruptedException {
        if (isFullRoom()) {
            System.out.println("Waiting room is Full");
        }
        else {
           patients.offer(newPatient);
        }
    }

   
    public Patient removePatient() throws InterruptedException {
        if (isEmptyRoom()) {
            System.out.println("Waiting room is empty");
            return null;
        } else {
            Patient removedPatient = patients.poll();
            return removedPatient;
        }
    }
    

    private boolean isFullRoom() {
        if (patients.size() == maxNumber) {
            return true;
        } else {
            return false;
        }
    }


    private boolean isEmptyRoom() {
        if (patients.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private int getCurrentPatientNum() {
        return patients.size();
    }

    public void sendBack(Patient p) {
        
    }
}