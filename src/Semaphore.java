public class Semaphore {

    private int permits;

    public Semaphore(int permits) {
        if (permits < 0)
            throw new IllegalArgumentException("permits must be >= 0");
        this.permits = permits;
    }

    public synchronized void acquire() throws InterruptedException {
        while (permits <= 0) {
            wait();
        }
        permits--;
    }

    public synchronized void release() {
        permits++;
        notifyAll();
    }

    public synchronized boolean tryAcquire(long timeoutMillis) throws InterruptedException {
        if (timeoutMillis < 0)
            throw new IllegalArgumentException("timeout must be >= 0");

        long remainingNanos = timeoutMillis * 1_000_000L;
        long deadline = System.nanoTime() + remainingNanos;

        while (permits <= 0) {
            remainingNanos = deadline - System.nanoTime();
            if (remainingNanos <= 0) {
                return false;
            }
            wait(remainingNanos / 1_000_000L, (int) (remainingNanos % 1_000_000L));
        }
        permits--;
        return true;
    }
}