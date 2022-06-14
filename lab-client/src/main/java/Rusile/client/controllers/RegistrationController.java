package Rusile.client.controllers;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Rusile.client.exceptions.ExceptionWithAlert;
import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.models.RegistrationModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.LanguagesEnum;
import Rusile.client.util.PathToViews;
import Rusile.client.util.PathToVisuals;
import Rusile.client.util.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class RegistrationController extends AbstractController implements Initializable {

    private final RegistrationModel registrationModel;

    public RegistrationController(ClientSocketChannelIO clientSocketChannelIO) {
        this.registrationModel = new RegistrationModel(clientSocketChannelIO, getCurrentStage(), this);
    }

    @FXML
    private Button login_button;

    @FXML
    private URL location;

    @FXML
    private PasswordField first_password_field;

    @FXML
    private ChoiceBox<LanguagesEnum> language_selector;

    @FXML
    private TextField login_field;

    @FXML
    private Button register_button;

    @FXML
    private PasswordField second_password_field;

    @FXML
    public void registerAction() {
        List<TextField> textFields = Arrays.asList(login_field, first_password_field, second_password_field);
        removeFieldsColoring(textFields);
        try {
            Session session = registrationModel.processRegistration(login_field.getText(),
                    first_password_field.getText(),
                    second_password_field.getText());
            if (session.getPassword() != null)
            switchScene(PathToViews.MAIN_VIEW, param -> new MainController(registrationModel.getClientSocketChannelIO(), session, PathToVisuals.TABLE_VIEW), getResourceBundle());
        } catch (ExceptionWithAlert e) {
            if (e.isFatal()) {
                e.showAlert();
                try {
                    registrationModel.getClientSocketChannelIO().closeChannel();
                } catch (IOException ex) {
                    showErrorAlert(ex.getMessage());
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

    @FXML
    public void loginAction() {
        try {
            switchScene(PathToViews.LOGIN_VIEW, param -> new LoginController(registrationModel.getClientSocketChannelIO()), getResourceBundle());
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setResourceBundle(resourceBundle);
        language_selector.setItems(FXCollections.observableArrayList(Stream.of(LanguagesEnum.values()).collect(Collectors.toList())));
        language_selector.setValue(LanguagesEnum.lg);
        language_selector.getSelectionModel().selectedItemProperty().addListener((m, oldValue, newValue) -> {
            try {
                setResourceBundle(ResourceBundle.getBundle("localization.locale", new Locale(newValue.getLanguageName())));
                LanguagesEnum.lg = newValue;
                switchScene(PathToViews.REGISTRATION_VIEW,
                        param -> new RegistrationController(registrationModel.getClientSocketChannelIO()), getResourceBundle());
            } catch (ExceptionWithAlert e) {
                e.showAlert();
            }
        });
    }
}
