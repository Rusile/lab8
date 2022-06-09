package Rusile.common.people;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Person's data class
 */
public class Person implements Comparable<Person>, Serializable {
    @NotNull
    @Positive(message = "ID должен быть больше нуля!")
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @NotBlank(message = "Имя должно содержать хотя бы 1 символ")
    @Pattern(regexp = "^[A-Z][a-z]*(\\\\s(([a-z]{1,3})|(([a-z]+\\\\')?[A-Z][a-z]*)))*$", message = "Имя должно начинаться с заглавной буквы и представлено в символьном формате!")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NotNull(message = "Координаты не могут быть null")
    private Coordinates coordinates; //Поле не может быть null
    @NotNull(message = "Дата не может быть null")
    @PastOrPresent
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @NotNull(message = "Рост не может быть null")
    @Positive(message = "Рост должен быть больше нуля")
    private int height; //Значение поля должно быть больше 0
    private Color eyeColor; //Поле может быть null
    private Color hairColor; //Поле может быть null
    @NotNull(message = "Национальность не может быть null")
    private Country nationality; //Поле не может быть null
    private Location location; //Поле может быть null

    /**
     *
     * @param id - person's id
     * @param name - person's name
     * @param coordinates - person's coordinates
     * @param creationDate - date of adding the person to the collection
     * @param height - person's height
     * @param eyeColor - person's eye color
     * @param hairColor - person's hair color
     * @param nationality - person's nationality
     * @param location - person's location
     */
    public Person(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, int height, Color eyeColor, Color hairColor, Country nationality, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }


    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return height == person.height && name.equals(person.name) &&
                coordinates.equals(person.coordinates) && creationDate.equals(person.creationDate) &&
                eyeColor == person.eyeColor && hairColor == person.hairColor &&
                nationality == person.nationality && location.equals(person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, height, eyeColor, hairColor, nationality, location);
    }

    @Override
    public String toString() {
        return "Person \"" + name + "\":" +
                "\n id=" + id +
                "\n coordinates=" + coordinates +
                "\n creationDate=" + creationDate.toLocalDate() +
                "\n height=" + height +
                "\n eyeColor=" + eyeColor +
                "\n hairColor=" + hairColor +
                "\n nationality=" + nationality +
                "\n location=" + location + "\n";
    }

    /**
     * Compares name, height and nationality
     * @param another - another person to compare
     * @return 0 if equals, >0 is greater, <0 if less
     */
    @Override
    public int compareTo(Person another) {
        int resultByName = this.name.compareTo(another.name);
        int resultByHeight = this.height - another.height;
        int resultByNationality = this.nationality.toString().compareTo(another.nationality.toString());

        if (resultByName == 0) {
            if (resultByHeight == 0) {
                return resultByNationality;
            } else {
                return resultByHeight;
            }
        } else {
            return resultByName;
        }
    }
}
