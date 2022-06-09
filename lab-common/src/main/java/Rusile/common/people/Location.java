package Rusile.common.people;

import java.io.Serializable;
import java.util.Objects;

/**
 *  Location data class
 */
public class Location implements Serializable {
    private double x;
    private double y;
    private int z;
    private String name; //Поле не может быть null

    /**
     *
     * @param x - X coordinate
     * @param y - Y coordinate
     * @param z - Z coordinate
     * @param name - name of location
     */
    public Location(double x, double y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        Location location = (Location) another;
        return x == location.x && y == location.y && z == location.z && name.equals(location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }

    @Override
    public String toString() {
        return "Location{" +
                "x = " + x +
                ", y = " + y +
                ", z = " + z +
                ", name = '" + name + '\'' +
                '}';
    }

}