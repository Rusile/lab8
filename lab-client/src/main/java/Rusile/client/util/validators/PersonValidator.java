package Rusile.client.util.validators;

import Rusile.common.people.Color;
import Rusile.common.people.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PersonValidator {

    private static String validateName(String name) throws IllegalArgumentException {
        if (name.length() < 5)
            throw new IllegalArgumentException("band_exception.name_length");
        return name;
    }

    private static long validateX(String strX)  throws IllegalArgumentException {
        long x;
        try {
            x = Long.parseLong(strX);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("band_exception.x_number");
        }
        if (x < 0 || x > 416)
            throw new IllegalArgumentException("band_exception.x_range");
        return x;
    }

    private static float validateY(String strY) throws IllegalArgumentException {
        float y;
        try {
            y = Float.parseFloat(strY);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("band_exception.y_number");
        }
        if (y < 0 || y > 416)
            throw new IllegalArgumentException("band_exception.y_range");
        return y;
    }



    private static double validateLocX(String strX) throws IllegalArgumentException {
        double x;
        try {
            x = Double.parseDouble(strX);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("band_exception.x_range");
        }
        return x;
    }

    private static double validateLocY(String strY) throws IllegalArgumentException {
        double y;
        try {
            y = Double.parseDouble(strY);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("band_exception.x_range");
        }
        return y;
    }

    private static int validateLocZ(String strZ) throws IllegalArgumentException {
        int z;
        try {
            z = Integer.parseInt(strZ);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("band_exception.x_range");
        }
        return z;
    }

    public static List<String> validatePerson(String name, String coordinatesX, String coordinatesY, String height,
                                              String locName, String locX, String locY, String locZ) {

        List<String> errorList = new ArrayList<>();

        try {
            validateName(name);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }

        try {
            validateX(coordinatesX);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }
        try {
            validateY(coordinatesY);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }
        try {
            validateHeight(height);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }
        try {
            validateLocX(locX);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }

        try {
            validateLocY(locY);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }

        try {
            validateLocZ(locZ);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }

        try {
            validateName(locName);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }

        return errorList;
    }

    private static void validateHeight(String height) {
        int y;
        try {
            y = Integer.parseInt(height);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("band_exception.x_range");
        }
//        return y;
    }
}
