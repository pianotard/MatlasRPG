public interface Interactable {

    public Player getPlayer();
    public Keyboard getKeyboard();
    public AbstractMap getMap();
    public WindowManager getWindowManager();
    public java.util.List<AbstractMob> findTargetsForPlayer(double radius);
    public java.util.Collection<Rectangle> getBorders();
    public void refreshOpenWindows();
    public void refreshPlayer();
    public void handleMovement(String key, double x, double y);
    public void toggleUtil(String key);
    public void runAbility(String key);
}
