package Rusile.client.controllers;

import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.models.AddModel;
import Rusile.client.models.MainModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.common.people.Color;
import Rusile.common.people.Country;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddController extends AbstractController implements Initializable {

    private final AddModel addModel;

    private final MainModel mainModel;

    public AddController(ClientSocketChannelIO clientSocketChannelIO, Session session, MainModel mainModel) {
        addModel = new AddModel(clientSocketChannelIO, getCurrentStage(), session, this);
        this.mainModel = mainModel;
    }

    @FXML
    private TextField id_field;

    @FXML
    private TextField coordinatesX_field;

    @FXML
    private TextField coordinatesY_field;

    @FXML
    private ChoiceBox<Country> country_selector;

    @FXML
    private ChoiceBox<Color> eye_selector;

    @FXML
    private ChoiceBox<Color> hair_selector;

    @FXML
    private TextField height_field;

    @FXML
    private TextField locationName_field;

    @FXML
    private TextField locationX_field;

    @FXML
    private TextField locationY_field;

    @FXML
    private TextField locationZ_field;

    @FXML
    private TextField name_field;

    @FXML
    public void addAction() {
        List<TextField> textFields = Arrays.asList(name_field, coordinatesX_field, coordinatesY_field,
                height_field, locationName_field, locationX_field, locationY_field, locationZ_field);
        removeFieldsColoring(textFields);
        try {
            addModel.processAdd(name_field.getText(), coordinatesX_field.getText(),
                    coordinatesY_field.getText(), height_field.getText(),
                    locationName_field.getText(), locationX_field.getText(),
                    locationY_field.getText(), locationZ_field.getText(),
                    eye_selector.getValue(), hair_selector.getValue(),
                    country_selector.getValue());
            getCurrentStage().close();
        } catch (FieldsValidationException e) {
            showFieldsErrors(e.getErrorList(), textFields);
        }
    }

    @FXML
    public void addIfMinAction() {
        List<TextField> textFields = Arrays.asList(name_field, coordinatesX_field, coordinatesY_field,
                height_field, locationName_field, locationX_field, locationY_field, locationZ_field);
        removeFieldsColoring(textFields);
        try {
            addModel.processAddIfMin(name_field.getText(), coordinatesX_field.getText(),
                    coordinatesY_field.getText(), height_field.getText(),
                    locationName_field.getText(), locationX_field.getText(),
                    locationY_field.getText(), locationZ_field.getText(),
                    eye_selector.getValue(), hair_selector.getValue(),
                    country_selector.getValue());
            getCurrentStage().close();
        } catch (FieldsValidationException e) {
            showFieldsErrors(e.getErrorList(), textFields);
        }
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setResourceBundle(resourceBundle);
        hair_selector.setItems(FXCollections.observableArrayList(Stream.of(Color.values()).collect(Collectors.toList())));
        eye_selector.setItems(FXCollections.observableArrayList(Stream.of(Color.values()).collect(Collectors.toList())));
        country_selector.setItems(FXCollections.observableArrayList(Stream.of(Country.values()).collect(Collectors.toList())));
        addRegex(coordinatesX_field, coordinatesY_field, height_field,
                locationX_field, locationY_field, locationZ_field, id_field);
    }

    public void updateByIdAction() {
        List<TextField> textFields = Arrays.asList(name_field, coordinatesX_field, coordinatesY_field,
                height_field, locationName_field, locationX_field, locationY_field, locationZ_field, id_field);
        removeFieldsColoring(textFields);
        try {
            addModel.processUpdate(id_field.getText(), name_field.getText(), coordinatesX_field.getText(),
                    coordinatesY_field.getText(), height_field.getText(),
                    locationName_field.getText(), locationX_field.getText(),
                    locationY_field.getText(), locationZ_field.getText(),
                    eye_selector.getValue(), hair_selector.getValue(),
                    country_selector.getValue());
            getCurrentStage().close();
        } catch (FieldsValidationException e) {
            showFieldsErrors(e.getErrorList(), textFields);
        }
    }

    public void setFields(Long id, String name, Long coordinatesX, Float coordinatesY, int height,
                          String locName, double locX, double locY, int locZ,
                          Color eyeColor, Color hairColor, Country country) {
        id_field.setText(id.toString());
        name_field.setText(name);
        coordinatesX_field.setText(coordinatesX.toString());
        coordinatesY_field.setText(coordinatesY.toString());
        height_field.setText(String.valueOf(height));
        locationName_field.setText(locName);
        locationX_field.setText(String.valueOf(locX));
        locationY_field.setText(String.valueOf(locY));
        locationZ_field.setText(String.valueOf(locZ));
        eye_selector.setValue(eyeColor);
        hair_selector.setValue(hairColor);
        country_selector.setValue(country);
    }
}
