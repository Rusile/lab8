package Rusile.common.people;

import java.io.Serializable;

/**
 * Enum with countries
 */
public enum Country implements Serializable {
    UNITED_KINGDOM,
    GERMANY,
    SPAIN,
    ITALY,
    JAPAN;

    /**
     * Returns comma separated list with the countries.
     * @return UNITED_KINGDOM, GERMANY, SPAIN, JAPAN
     */
    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (Country countries: values()) {
            nameList.append(countries.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}