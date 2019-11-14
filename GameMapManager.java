public class GameMapManager {
    
    private java.util.Map<String, AbstractMap> maps = new java.util.HashMap<>();

    public GameMapManager() {
        this.maps.put("test_map", new TestMap());
        this.maps.put("test_map_2", new TestMapTwo());
    }

    public AbstractMap get(String key) {
        if (this.maps.keySet().contains(key)) {
            return this.maps.get(key);
        }
        System.out.println("no such map!");
        return null;
    }
}
