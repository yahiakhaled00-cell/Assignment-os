import java.util.concurrent.CountDownLatch;
public class Patient extends Thread {

    private final int id;
    private final WaitingRoom waitingRoom;

    private final CountDownLatch treatmentFinished = new CountDownLatch(1);

    
    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

   public int getPatientId() { 
    return id;
}

    
    public void finishTreatment() {
        treatmentFinished.countDown();
        
    }

    @Override
    public void run() {
        try{
            Hospital.log("P" + id + " arrived.");
            waitingRoom.addPatient(this);

            treatmentFinished.await();
            Hospital.log("P" + id + " left.");
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            Hospital.log("P" + id + " was interrupted");
        }
    }
}