package Rusile.common.util;

public final class Pair<T, U> {

    public Pair(T first, U second) {
        this.second = second;
        this.first = first;
    }

    public final T first;
    public final U second;

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}