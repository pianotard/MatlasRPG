public class ImageReader implements Runnable {
    
    public ImageReader() {}

    @Override
    public void run() {
        SlimeAttack.initImages();
        System.out.println("draw slimes");
        BowmanAttack.initImages();
        System.out.println("draw arrows");
        MageAttack.initImages();
        System.out.println("draw tracing arrows");
    }
}
