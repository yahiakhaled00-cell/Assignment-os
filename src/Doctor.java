import java.util.Random;

public class Doctor extends Thread {

    private final int doctorId;
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final Random random = new Random();

    public Doctor(int doctorId, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms) {
        this.doctorId = doctorId;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
    }

    private void treatPatient(Patient patient) throws InterruptedException {
        int pid = patient.getPatientId();
        Hospital.log("Doctor " + doctorId + " begins treating P" + pid + ".");
        Thread.sleep((random.nextInt(5) + 1) * 1000L);
        Hospital.log("Doctor " + doctorId + " finished treating P" + pid + ".");
    }

    @Override
        public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Patient patient = null;

            try {
                patient = waitingRoom.takePatient();
                int pid = patient.getPatientId();
                Hospital.log("Doctor " + doctorId + " is assigned to P" + pid + ".");
                Hospital.log("Doctor " + doctorId + " is trying to acquire a treatment room.");

                if (treatmentRooms.tryAcquiringRoom()) {
                    Hospital.log("Doctor " + doctorId + " acquired a treatment room.");
                    try {
                        treatPatient(patient);
                    }
                    finally {
                        treatmentRooms.releaseRoom();
                        Hospital.log("Doctor " + doctorId + " released the treatment room.");
                    }
                    Hospital.log("Doctor " + doctorId + " is now available.");
                    patient.finishTreatment();   
                    patient = null;
                }
                else {
                    Hospital.log("Doctor " + doctorId + " could not acquire a treatment room.");
                    Hospital.log("Doctor " + doctorId + " is sending P" + pid
                        + " back to the waiting room.");
                    waitingRoom.addPatient(patient);
                    patient = null;
                    Hospital.log("Doctor " + doctorId + " is now available.");
                }
            }
            catch (InterruptedException e) {
                if (patient != null) {
                    try {
                        waitingRoom.addPatient(patient);
                    }
                    catch (InterruptedException requeueInterrupted) {
                        e.addSuppressed(requeueInterrupted);
                    }
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
        Hospital.log("Doctor " + doctorId + " stopped working.");
    }
}