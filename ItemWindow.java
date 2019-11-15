public class ItemWindow extends UtilWindow {
    
    private static final int WIDTH = 300;
    private static final int HEIGHT = 520;

    private Interactable manager;

    public ItemWindow(Interactable manager) {
        super(manager.getPlayer(), "Inventory", 100, 45, WIDTH, HEIGHT);
        this.manager = manager;
    }

    @Override
    public void refresh() {}
}
