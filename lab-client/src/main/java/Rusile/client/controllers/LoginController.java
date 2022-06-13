package Rusile.client.controllers;

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
import Rusile.client.models.LoginModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.LanguagesEnum;
import Rusile.client.util.PathToViews;
import Rusile.client.util.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends AbstractController implements Initializable {

    private final LoginModel loginModel;

    public LoginController(ClientSocketChannelIO clientSocketChannelIO) {
        this.loginModel = new LoginModel(clientSocketChannelIO, getCurrentStage(), this);
    }

    @FXML
    private ChoiceBox<LanguagesEnum> language_selector;

    @FXML
    private URL location;

    @FXML
    private Button login_button;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button register_button;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setResourceBundle(resourceBundle);
        language_selector.setItems(FXCollections.observableArrayList(Stream.of(LanguagesEnum.values()).collect(Collectors.toList())));
        language_selector.setValue(loginModel.getLanguage(resourceBundle.getLocale().getLanguage()));
        language_selector.getSelectionModel().selectedItemProperty().addListener((m, oldValue, newValue) -> {
            try {
                setResourceBundle(ResourceBundle.getBundle("localization.locale", new Locale(newValue.getLanguageName())));
                switchScene(PathToViews.LOGIN_VIEW,
                        param -> new LoginController(loginModel.getClientSocketChannelIO()), getResourceBundle());
                language_selector.setValue(newValue);
            } catch (ExceptionWithAlert e) {
                e.showAlert();
            }
        });
    }


    @FXML
    void loginAction() {
        List<TextField> textFields = Arrays.asList(login_field, password_field);
        removeFieldsColoring(textFields);
        try {
            Session session = loginModel.processLogin(login_field.getText(),
                    password_field.getText());
            //switchScene(PathToViews.MAIN_VIEW, param -> new MainController(loginModel.getClientSocketWorker(), session, PathToVisuals.TABLE_VIEW), getResourceBundle());
        } catch (ExceptionWithAlert e) {
            if (e.isFatal()) {
                e.showAlert();
                try {
                    loginModel.getClientSocketChannelIO().closeChannel();
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
    void registerAction() {
        try {
            switchScene(PathToViews.REGISTRATION_VIEW, param -> new RegistrationController(loginModel.getClientSocketChannelIO()), getResourceBundle());
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }
    }
}
