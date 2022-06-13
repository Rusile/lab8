package Rusile.client.controllers;


import Rusile.client.exceptions.ExceptionWithAlert;
import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.models.ConnectionModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.LanguagesEnum;
import Rusile.client.util.PathToViews;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectionController extends AbstractController implements Initializable {

    private final ConnectionModel connectionModel;

    public ConnectionController(ClientSocketChannelIO clientSocketChannelIO) {
        connectionModel = new ConnectionModel(clientSocketChannelIO, getCurrentStage(), this);
    }

    @FXML
    private Text connection_text;

    @FXML
    private Button connection_button;

    @FXML
    private TextField host_field;

    @FXML
    private ChoiceBox<LanguagesEnum> language_selector;

    @FXML
    private TextField port_field;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        addRegex(port_field);
        language_selector.setItems(FXCollections.observableArrayList(Stream.of(LanguagesEnum.values()).collect(Collectors.toList())));
        language_selector.setValue(connectionModel.getLanguage(getResourceBundle().getLocale().getLanguage()));
        language_selector.getSelectionModel().selectedItemProperty().addListener((m, oldValue, newValue) -> {
            try {
                setResourceBundle(ResourceBundle.getBundle("localization.locale", new Locale(newValue.getLanguageName())));
                switchScene(PathToViews.CONNECTION_VIEW,
                        param -> new ConnectionController(connectionModel.getClientSocketChannelIO()), getResourceBundle());
                language_selector.setValue(newValue);
            } catch (ExceptionWithAlert e) {
                e.showAlert();
            }
        });
    }

    @FXML
    public void connectionAction() {
        List<TextField> textFields = Arrays.asList(host_field, port_field);
        removeFieldsColoring(textFields);
        try {
            connectionModel.connect(host_field.getText(),
                    port_field.getText());
        } catch (ExceptionWithAlert e) {
            if (e.isFatal()) {
                e.showAlert();
                try {
                    connectionModel.getClientSocketChannelIO().closeChannel();
                } catch (IOException exception) { // to locale
                    showErrorAlert(exception.getMessage());
                }
                Platform.exit();
            } else {
                e.showAlert();
                clearFields(textFields);
            }
        } catch (FieldsValidationException e) {
            showFieldsErrors(e.getErrorList(), textFields);
        }
    }

}

