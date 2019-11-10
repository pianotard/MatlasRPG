public abstract class PausableRunner implements Runnable {
    
    protected void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}