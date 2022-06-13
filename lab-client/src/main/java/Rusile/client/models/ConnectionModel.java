package Rusile.client.models;

import Rusile.client.controllers.ConnectionController;
import Rusile.client.controllers.LoginController;
import Rusile.client.exceptions.ExceptionWithAlert;
import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.PathToViews;
import Rusile.client.util.validators.ConnectionValidator;
import Rusile.common.util.Pair;
import javafx.stage.Stage;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Objects;

public class ConnectionModel extends AbstractModel {

    private final ConnectionController currentController;

    public ConnectionModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, ConnectionController controller) {
        super(clientSocketChannelIO, currentStage);
        currentController = controller;
    }


    public void connect(String address, String port) throws ExceptionWithAlert, FieldsValidationException {
        Pair<List<String>, SocketChannel> pair = ConnectionValidator.validateConnection(address, port);
        if (pair.first.stream().anyMatch(Objects::nonNull)) {
            throw new FieldsValidationException(pair.first);
        }
        String currentAddress = ConnectionValidator.validateAddress(address);
        Integer currentPort = ConnectionValidator.validatePort(port);

        assert currentAddress != null;
        assert currentPort != null;
        getClientSocketChannelIO().setAddress(new InetSocketAddress(currentAddress, currentPort));
        getClientSocketChannelIO().setChannel(pair.second);

        currentController.switchScene(PathToViews.LOGIN_VIEW, comp -> new LoginController(getClientSocketChannelIO()), currentController.getResourceBundle());
    }
}
