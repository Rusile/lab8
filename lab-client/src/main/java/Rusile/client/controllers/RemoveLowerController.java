package Rusile.client.controllers;

import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.models.AddModel;
import Rusile.client.models.MainModel;
import Rusile.client.models.RemoveLowerModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.common.people.Color;
import Rusile.common.people.Country;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoveLowerController extends AbstractController implements Initializable {
    private final RemoveLowerModel removeLowerModel;

    private final MainModel mainModel;

    public RemoveLowerController(ClientSocketChannelIO clientSocketChannelIO, Session session, MainModel mainModel) {
        removeLowerModel = new RemoveLowerModel(clientSocketChannelIO, getCurrentStage(), session, this);
        this.mainModel = mainModel;
    }


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

    public MainModel getMainModel() {
        return mainModel;
    }


    public void removeLowerAction() {
        List<TextField> textFields = Arrays.asList(name_field, coordinatesX_field, coordinatesY_field,
                height_field, locationName_field, locationX_field, locationY_field, locationZ_field);
        removeFieldsColoring(textFields);
        try {
            removeLowerModel.processRemoveLower(name_field.getText(), coordinatesX_field.getText(),
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setResourceBundle(resourceBundle);
        hair_selector.setItems(FXCollections.observableArrayList(Stream.of(Color.values()).collect(Collectors.toList())));
        eye_selector.setItems(FXCollections.observableArrayList(Stream.of(Color.values()).collect(Collectors.toList())));
        country_selector.setItems(FXCollections.observableArrayList(Stream.of(Country.values()).collect(Collectors.toList())));
        addRegex(coordinatesX_field, coordinatesY_field, height_field,
                locationX_field, locationY_field, locationZ_field);
    }

}
