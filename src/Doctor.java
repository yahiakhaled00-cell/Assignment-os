import java.util.Random;
public class Doctor extends Thread {

    private int doctorId;
    private WaitingRoom waitingRoom;
    private TreatmentRooms treatmentRooms;
    private Random random;

    public Doctor(int doctorId, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms) {
        this.doctorId = doctorId;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.random = new Random();
    }


    private void treatPatient(Patient patient)
        throws InterruptedException{
        int duration = random.nextInt(5) + 1;

        Hospital.log("Doctor " + doctorId + " started treating a patient for " + duration + " seconds.");
        Thread.sleep(duration * 1000L);
        patient.finishTreatment();
        Hospital.log("Doctor " + doctorId + " finished treating a patient.");
    }

        @Override
        public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Patient patient = null;

            try {
                patient = waitingRoom.take();
                Hospital.log("Doctor " + doctorId + " took a patient from the waiting room.");
                boolean roomAcquired = treatmentRooms.acquireRoom();
                if (roomAcquired) {

                    try {
                        treatPatient(patient);
                        patient = null;
                    }
                    finally {
                        treatmentRooms.releaseRoom();
                        Hospital.log("Doctor " + doctorId + " released the treatment room.");
                    }
                }
                else {
                    Hospital.log("Doctor " + doctorId + " could not get a treatment room within 3 seconds.");
                    waitingRoom.sendBack(patient);
                    patient = null;
                }
            }
            catch (InterruptedException e) {
                if (patient != null) {
                    waitingRoom.sendBack(patient);
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
        Hospital.log("Doctor " + doctorId + " stopped working.");
    }
}

