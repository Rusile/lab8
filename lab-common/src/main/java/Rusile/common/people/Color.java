package Rusile.common.people;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum of colors
 */
public enum Color implements Serializable {
    RED,
    BLACK,
    BLUE,
    ORANGE,
    WHITE,
    BROWN;

    /**
     * Returns comma separated list with the colors.
     * @return RED, BLACK, BLUE, ORANGE, WHITE, BROWN
     */
    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (Color color : values()) {
            nameList.append(color.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
    public static List<Color> arrayOfElements() {
        List<Color> arr = new ArrayList<>(Arrays.asList(Color.values()));
        return arr;
    }

}