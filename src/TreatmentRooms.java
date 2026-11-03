
public class TreatmentRooms {
    Semaphore treatRooms ;
 

    public TreatmentRooms(int rooms) {
        treatRooms = new Semaphore(rooms);
    }

   
    public boolean tryAcquiringRoom() throws InterruptedException {
        return treatRooms.tryAcquire(3000);
    }

    public void releaseRoom() {
        treatRooms.release();
    }
}