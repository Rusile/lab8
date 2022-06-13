package Rusile.client.util;

import Rusile.common.people.Color;
import Rusile.common.people.Country;
import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class TableRow {
    private IntegerProperty id;
    private StringProperty name;
    private LongProperty x;
    private FloatProperty y;
    private ObjectProperty<LocalDateTime> creationDate;
    private IntegerProperty height;
    private ObjectProperty<Color> eyeColor;
    private ObjectProperty<Color> hairColor;
    private ObjectProperty<Country> nationality;
    private DoubleProperty locationX;
    private DoubleProperty locationY;
    private IntegerProperty locationZ;
    private StringProperty locationName;


    public TableRow(int id, String name, long xCoor, float yCoor,
                    LocalDateTime time, int height, Color eyeColor, Color hairColor,
                    Country nationality, double locX, double locY, int locZ,
                    String locName) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        x = new SimpleLongProperty(xCoor);
        y = new SimpleFloatProperty(yCoor);
        creationDate = new SimpleObjectProperty<>(time);
        this.height = new SimpleIntegerProperty(height);
        this.eyeColor = new SimpleObjectProperty<>(eyeColor);
        this.hairColor = new SimpleObjectProperty<>(hairColor);
        this.nationality = new SimpleObjectProperty<>(nationality);
        locationX = new SimpleDoubleProperty(locX);
        locationY = new SimpleDoubleProperty(locY);
        locationZ = new SimpleIntegerProperty(locZ);
        locationName = new SimpleStringProperty(locName);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public float getX() {
        return x.get();
    }

    public FloatProperty xProperty() {
        return x;
    }

    public float getY() {
        return y.get();
    }

    public FloatProperty yProperty() {
        return y;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate.get();
    }

    public ObjectProperty<ZonedDateTime> creationDateProperty() {
        return creationDate;
    }

    public int getHeight() {
        return height.get();
    }

    public IntegerProperty heightProperty() {
        return height;
    }

    public Color getEyeColor() {
        return eyeColor.get();
    }

    public ObjectProperty<Color> eyeColorProperty() {
        return eyeColor;
    }

    public Color getHairColor() {
        return hairColor.get();
    }

    public ObjectProperty<Color> hairColorProperty() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality.get();
    }

    public ObjectProperty<Country> nationalityProperty() {
        return nationality;
    }

    public float getLocationX() {
        return locationX.get();
    }

    public FloatProperty locationXProperty() {
        return locationX;
    }

    public float getLocationY() {
        return locationY.get();
    }

    public FloatProperty locationYProperty() {
        return locationY;
    }

    public float getLocationZ() {
        return locationZ.get();
    }

    public FloatProperty locationZProperty() {
        return locationZ;
    }

    public String getLocationName() {
        return locationName.get();
    }

    public StringProperty locationNameProperty() {
        return locationName;
    }
}
