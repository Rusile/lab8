package Rusile.client.models;

import Rusile.client.controllers.RegistrationController;
import Rusile.client.exceptions.ExceptionWithAlert;
import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.networkManager.AuthorizationModule;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.client.util.validators.UserValidator;
import Rusile.common.util.Request;
import Rusile.common.util.RequestType;
import Rusile.common.util.Response;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RegistrationModel extends AbstractModel {

    private final RegistrationController currentController;

    public RegistrationModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, RegistrationController registrationController) {
        super(clientSocketChannelIO, currentStage);
        currentController = registrationController;
    }

    public Session processRegistration(String username, String fPassword, String sPassword) throws ExceptionWithAlert, FieldsValidationException {
        List<String> errorList = UserValidator.validateRegisterUser(username, fPassword, sPassword);
        if (errorList.stream().anyMatch(Objects::nonNull)) {
            throw new FieldsValidationException(errorList);
        }
        try {
            getClientSocketChannelIO().send(new Request(username, fPassword, RequestType.REGISTER));
            Response response = (Response) getClientSocketChannelIO().receive();
            AuthorizationModule.validateRegistration(response);
            if (AuthorizationModule.isAuthorizationDone()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, response.getMessageToResponse());
                alert.setHeaderText(null);
                alert.showAndWait();
                return new Session(username, fPassword);
            } else {
                throw new ExceptionWithAlert(response.getMessageToResponse());
            }
        } catch (IOException e) {
            throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.connection"));
        } catch (ClassNotFoundException e) {
            throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.response"));
        }
    }
}
