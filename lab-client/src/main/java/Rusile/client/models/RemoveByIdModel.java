package Rusile.client.models;

import Rusile.client.controllers.RemoveByIdController;
import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.client.util.validators.NumberValidator;
import Rusile.common.util.Request;
import Rusile.common.util.RequestType;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class RemoveByIdModel extends AbstractModel {

    private final Session session;

    private final RemoveByIdController currentController;

    public RemoveByIdModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, Session session, RemoveByIdController currentController) {
        super(clientSocketChannelIO, currentStage);
        this.session = session;
        this.currentController = currentController;
    }

    public void processRemove(String id) throws FieldsValidationException {
        List<String> errorList = NumberValidator.validateId(id);
        if (errorList.stream().anyMatch(Objects::nonNull)) {
            throw new FieldsValidationException(errorList);
        }
        Request request = new Request("remove_by_id", NumberValidator.getValidatedId(id), RequestType.COMMAND);
        request.setLogin(session.getUsername()); request.setPassword(session.getPassword());
        currentController.getMainModel().getThreadPoolExecutor().execute(currentController.getMainModel()
                .generateTask(request, true));
    }
}
