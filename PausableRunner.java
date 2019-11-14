public abstract class PausableRunner implements Runnable {

    protected boolean restart = false;

    protected void restart() {
        this.restart = true;
    }

    protected void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
