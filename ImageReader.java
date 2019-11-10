public class ImageReader implements Runnable {
    
    public ImageReader() {}

    @Override
    public void run() {
        SlimeAttack.initImages();
        BowmanAttack.initImages();
    }
}
