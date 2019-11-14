public class Pair<T, U> {
    
    public T first;
    public U second;

    private Pair(T t, U u) {
        this.first = t;
        this.second = u;
    }

    public static <T, U> Pair<T, U> of(T t, U u) {
        return new Pair<T, U>(t, u);
    }
}
