package Rusile.client.models;

import Rusile.client.controllers.LoginController;
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

public class LoginModel extends AbstractModel{

    private final LoginController currentController;

    public LoginModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, LoginController currentController) {
        super(clientSocketChannelIO, currentStage);
        this.currentController = currentController;
    }

    public Session processLogin(String username, String password) throws ExceptionWithAlert, FieldsValidationException {
        List<String> errorList = UserValidator.validateLoginUser(username, password);
        if (errorList.stream().anyMatch(Objects::nonNull)) {
            throw new FieldsValidationException(errorList);
        }
        try {
            getClientSocketChannelIO().send(new Request(username, password, RequestType.LOGIN));
            Response response = (Response) getClientSocketChannelIO().receive();
            if (response.getInfo() != null)
                AuthorizationModule.validateRegistration(response);
            if (AuthorizationModule.isAuthorizationDone()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, response.getMessageToResponse());
                alert.setHeaderText(null);
                alert.showAndWait();
                return new Session(username, password);
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
