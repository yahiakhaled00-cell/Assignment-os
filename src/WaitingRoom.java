import java.util.Queue;
import java.util.ArrayDeque;

public class WaitingRoom { 
    private Queue<Patient> patients;
    private Semaphore empty;
    private Semaphore full;
    private  Semaphore mutex;

    public WaitingRoom(int capacity) {
        patients = new ArrayDeque<>(capacity);
        empty = new Semaphore(capacity);
        full = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    
    public void addPatient(Patient newPatient) throws InterruptedException {
        if (!empty.tryAcquire(0)) {
            Hospital.log("The waiting room is full.\nP" + newPatient.getPatientId() + " is waiting outside for an available seat.");
            empty.acquire();
        }
            mutex.acquire();
        try{
            patients.offer(newPatient);
            Hospital.log("P" + newPatient.getPatientId() + " entered the waiting room.");
        } finally {
            mutex.release();
        }
           full.release();
       
    }
   
    public Patient takePatient() throws InterruptedException {
        full.acquire();
        mutex.acquire();

        Patient removedPatient;
        try {
        removedPatient = patients.poll();
    } finally {
        mutex.release();
    }
        empty.release();
        
         return removedPatient;
    }
    
}