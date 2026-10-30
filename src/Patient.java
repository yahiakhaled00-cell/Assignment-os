public class Patient extends Thread {

    private final int id;
    private final WaitingRoom waitingRoom;

    

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

   public int getPatientId() { 
    return id;
}

    
    public void finishTreatment() {
        
    }

    @Override
    public void run() {
        
    }
}