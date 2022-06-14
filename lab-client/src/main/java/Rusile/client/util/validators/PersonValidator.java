package Rusile.client.util.validators;

import Rusile.common.people.Color;
import Rusile.common.people.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PersonValidator {

    private static String validateName(String name) throws IllegalArgumentException {
        if (name.length() < 5)
            throw new IllegalArgumentException("person_exception.name_lenght");
        return name;
    }

    private static long validateX(String strX)  throws IllegalArgumentException {
        long x;
        try {
            x = Long.parseLong(strX);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("person_exception.coordX_number");
        }
        if (x < 0 || x > 416)
            throw new IllegalArgumentException("person_exception.coordX_range");
        return x;
    }

    private static float validateY(String strY) throws IllegalArgumentException {
        float y;
        try {
            y = Float.parseFloat(strY);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("person_exception.coordY_number");
        }
        if (y < 0 || y > 416)
            throw new IllegalArgumentException("person_exception.coordY_range");
        return y;
    }

    private static int validateHeight(String strHeight) throws IllegalArgumentException {
        int height;
        try {
            height = Integer.parseInt(strHeight);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("person_exception.height_number");
        }
        if (height < 0 || height > 250)
            throw new IllegalArgumentException("person_exception.height_range");
        return height;
    }

    private static double validateLocX(String strX) throws IllegalArgumentException {
        double x;
        try {
            x = Double.parseDouble(strX);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("person_exception.locX_number");
        }
        return x;
    }

    private static double validateLocY(String strY) throws IllegalArgumentException {
        double y;
        try {
            y = Double.parseDouble(strY);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("person_exception.locY_number");
        }
        return y;
    }

    private static int validateLocZ(String strZ) throws IllegalArgumentException {
        int z;
        try {
            z = Integer.parseInt(strZ);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("person_exception.locZ_number");
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
}
