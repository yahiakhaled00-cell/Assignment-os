public class Patient extends Thread {

    private final int id;
    private final WaitingRoom waitingRoom;

    private final Semaphore signal = new Semaphore(0);
    private volatile boolean treated = false;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    public int getPatientId() {
        return id;
    }

    public void finishTreatment() {
        treated = true;
        signal.release();
    }

    public void returnToWaitingRoom() {
        signal.release();
    }

    @Override
    public void run() {
        try {
            Hospital.log("P" + id + " arrived.");
            while (true) {
                waitingRoom.addPatient(this);   // 
                signal.acquire();               //  
                if (treated) break;             //  

            }
            Hospital.log("P" + id + " left.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Hospital.log("P" + id + " was interrupted");
        }
    }
}