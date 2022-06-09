package Rusile.client.util;

import Rusile.common.exception.IncorrectInputInScriptException;
import Rusile.common.exception.NotInBoundsException;
import Rusile.common.exception.NotNullException;
import Rusile.common.exception.WrongNameException;
import Rusile.common.people.*;
import Rusile.common.util.TextWriter;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Asks a user a person's value.
 */

public class ScannerManager {
    public static final String INPUT_INFO = "> ";
    public static final String INPUT_COMMAND = "$ ";
    private final int MAX_X = 416;
    private final int MIN_HEIGHT = 0;
    private final int MAX_HEIGHT = 300;
    private final Pattern patternNumber = Pattern.compile("-?\\d+(\\.\\d+)?");
    private final Pattern patternSymbols = Pattern.compile("^[A-Z][a-z]*(\\\\s(([a-z]{1,3})|(([a-z]+\\\\')?[A-Z][a-z]*)))*$");

    private Scanner userScanner;
    private boolean fileMode;

    public ScannerManager(Scanner scanner) {
        this.userScanner = scanner;
        fileMode = false;
    }

    /**
     * Sets a scanner to scan user input.
     *
     * @param userScanner Scanner to set.
     */
    public void setUserScanner(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * @return Scanner, which uses for user input.
     */
    public Scanner getUserScanner() {
        return userScanner;
    }

    /**
     * Sets studyGroup asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets studyGroup asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }

    public Person askPerson() throws IncorrectInputInScriptException {
        return new Person(
                null,
                askPersonName(),
                askCoordinates(),
                LocalDateTime.now(),
                askPersonHeight(),
                askPersonEyeColor(),
                askPersonHairColor(),
                askPersonNationality(),
                askPersonLocation()
        );
    }
    /**
     * Asks a user the studyGroup's name.
     *
     * @param inputTitle title of input.
     * @param minLength  min length of string
     * @param maxLength  max length of string
     * @param typeOfName shows what kind of name user need to enter
     * @return name
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public String askName(String inputTitle, int minLength, int maxLength, String typeOfName) throws IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                System.out.println(inputTitle);
                System.out.print(INPUT_INFO);
                name = userScanner.nextLine().trim();
                if (fileMode) System.out.println(name);
                if (name.equals("")) throw new NotNullException();
                if (name.length() <= minLength) throw new NotInBoundsException();
                if (name.length() >= maxLength) throw new NotInBoundsException();
                if (!patternSymbols.matcher(name).matches()) throw new WrongNameException();
                break;
            } catch (WrongNameException exception) {
                TextWriter.printErr(String.format("%s must be presented in character format with a capital letter!", typeOfName));
            } catch (NoSuchElementException exception) {
                TextWriter.printErr(String.format("%s not recognized!", typeOfName));
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotNullException exception) {
                TextWriter.printErr(String.format("%s can't be empty!", typeOfName));
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                TextWriter.printErr(String.format("The length of the string is not included in the range (%d; %d)", minLength, maxLength));
            }
        }
        return name;
    }

    /**
     * Asks a user the person's name
     *
     * @return person's name
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public String askPersonName() throws IncorrectInputInScriptException {
        return askName("Enter the person's name:", 0, Integer.MAX_VALUE, "Person's name");
    }

    /**
     * Asks a user the person's X coordinate.
     *
     * @param withLimit set bounds for X
     * @return person's X coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public long askXOfCoordinates(boolean withLimit) throws IncorrectInputInScriptException {
        String strX = "";
        long x;
        while (true) {
            try {
                if (withLimit)
                    System.out.println("Enter the coordinate X < " + MAX_X + ":");
                else
                    System.out.println("Enter the X coordinate:");
                System.out.print(INPUT_INFO);
                strX = userScanner.nextLine().trim();
                if (fileMode) System.out.println(strX);
                x = Long.parseLong(strX);
                if (withLimit && x >= MAX_X) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("The X coordinate is not recognized!");
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInBoundsException exception) {
                TextWriter.printErr("The X coordinate must be in the range (" + 0
                        + ";" + (withLimit ? MAX_X : Long.MAX_VALUE) + ")!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strX).matches())
                    TextWriter.printErr("The X coordinate must be in the range (" + Long.MAX_VALUE
                            + ";" + (withLimit ? MAX_X : Long.MAX_VALUE) + ")!");
                else
                    TextWriter.printErr("The X coordinate must be represented by a number!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Asks a user the person's Y coordinate.
     *
     * @return person's Y coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public float askYOfCoordinates() throws IncorrectInputInScriptException {
        String strY = "";
        float y;
        while (true) {
            try {
                System.out.println("Enter the Y coordinate:");
                System.out.print(INPUT_INFO);
                strY = userScanner.nextLine().trim();
                if (fileMode) System.out.println(strY);
                y = Float.parseFloat(strY);
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("The Y coordinate is not recognized!");
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strY).matches()) //why
                    TextWriter.printErr("The Y coordinate must be in the range (" + Float.MIN_VALUE
                            + ";" + Float.MAX_VALUE + ")!");
                else
                    TextWriter.printErr("The Y coordinate must be represented by a number!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Asks a user the person's coordinates.
     *
     * @return person's coordinates.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        long x = askXOfCoordinates(true);
        float y = askYOfCoordinates();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the person's height
     *
     * @return Person's height
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public int askPersonHeight() throws IncorrectInputInScriptException {
        String strHeight = "";
        int height;
        while (true) {
            try {
                System.out.println("Enter the person's height:");
                System.out.print(INPUT_INFO);
                strHeight = userScanner.nextLine().trim();
                if (fileMode) System.out.println(strHeight);
                height = Integer.parseInt(strHeight);
                if (height <= MIN_HEIGHT || height >= MAX_HEIGHT) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("The number is not recognized!");
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strHeight).matches())
                    TextWriter.printErr("The number must be in the range (" + MIN_HEIGHT + ";" + MAX_HEIGHT + ")!");
                else
                    TextWriter.printErr("Height should be represented by a number!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                TextWriter.printErr("Число должно быть в диапазоне (" + MIN_HEIGHT + ";" + MAX_HEIGHT + ")!");
            }
        }
        return height;
    }

    /**
     * Asks a user the person's eye color
     *
     * @return Person's eye color
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Color askPersonEyeColor() throws IncorrectInputInScriptException {
        String strEyeColor;
        Color color;
        while (true) {
            try {
                System.out.println("List of colors - " + Color.nameList());
                System.out.println("Enter the eye color:");
                System.out.print(INPUT_INFO);
                strEyeColor = userScanner.nextLine().trim();
                if (fileMode) System.out.println(strEyeColor);
                color = Color.valueOf(strEyeColor.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Eye color not recognized!");
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                TextWriter.printErr("This eye color is not in the list!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            }
        }
        return color;
    }

    /**
     * Asks a user the person's hair color
     *
     * @return Person's hair color
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Color askPersonHairColor() throws IncorrectInputInScriptException {
        String strHairColor;
        Color color;
        while (true) {
            try {
                System.out.println("List of colors -" + Color.nameList());
                System.out.println("Enter your hair color:");
                System.out.print(INPUT_INFO);
                strHairColor = userScanner.nextLine().trim();
                if (fileMode) System.out.println(strHairColor);
                color = Color.valueOf(strHairColor.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Hair color is not recognized!");
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                TextWriter.printErr("This hair color is not on the list!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            }
        }
        return color;
    }

    /**
     * Asks a user the person's nationality.
     *
     * @return nationality of the person
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Country askPersonNationality() throws IncorrectInputInScriptException {
        String strNationality;
        Country nationality;
        while (true) {
            try {
                System.out.println("List of nationalities - " + Country.nameList());
                System.out.println("Enter the nationality of the person:");
                System.out.print(INPUT_INFO);
                strNationality = userScanner.nextLine().trim();
                if (fileMode) System.out.println(strNationality);
                nationality = Country.valueOf(strNationality.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Nationality not recognized!");
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                TextWriter.printErr("This nationality is not on the list!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            }
        }
        return nationality;
    }

    /**
     * Asks a user the X/Y/Z coordinate of location.
     *
     * @param coordinateAxis name of coordinateAxis
     * @return value of coordinate
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public double askCoordinateOfLocation(String coordinateAxis) throws IncorrectInputInScriptException {
        String strCoordinate = "";
        double coordinate;
        while (true) {
            try {
                System.out.printf("Enter the location coordinate %s:%n", coordinateAxis);
                System.out.print(INPUT_INFO);
                strCoordinate = userScanner.nextLine().trim();
                if (fileMode) System.out.println(strCoordinate);
                coordinate = Double.parseDouble(strCoordinate);
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr(String.format("The coordinate %s not recognized!", coordinateAxis));
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strCoordinate).matches())
                    TextWriter.printErr("The coordinate must be in the range (" + Double.MIN_VALUE
                            + ";" + Double.MAX_VALUE + ")!");
                else
                    TextWriter.printErr("The coordinate must be represented by a number!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            }
        }
        return coordinate;
    }

    /**
     * Asks a user the person's location
     *
     * @return person's location
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Location askPersonLocation() throws IncorrectInputInScriptException {
        double x = askCoordinateOfLocation("X");
        double y = askCoordinateOfLocation("Y");
        int z = (int) askCoordinateOfLocation("Z");
        String name = askName("Enter the name of the location:", 0, Integer.MAX_VALUE, "Location's name");
        return new Location(x, y, z, name);
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                System.out.println(finalQuestion);
                System.out.print(INPUT_INFO);
                answer = userScanner.nextLine().trim();
                if (fileMode) System.out.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                TextWriter.printErr("Response doesn't recognized");
                System.exit(0);
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInBoundsException exception) {
                TextWriter.printErr("Request must be presented like '+' or '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                TextWriter.printErr("Unexpected error!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }


}


