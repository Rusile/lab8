package Rusile.client.models;

import Rusile.client.controllers.AddController;
import Rusile.client.controllers.RemoveLowerController;
import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.client.util.validators.PersonValidator;
import Rusile.common.people.*;
import Rusile.common.util.Request;
import Rusile.common.util.RequestType;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class RemoveLowerModel extends AbstractModel {
    private final Session session;

    private final RemoveLowerController currentController;

    public RemoveLowerModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, Session session, RemoveLowerController currentController) {
        super(clientSocketChannelIO, currentStage);
        this.session = session;
        this.currentController = currentController;
    }

    public void processRemoveLower(String name, String coordinatesX, String coordinatesY, String height,
                           String locName, String locX, String locY, String locZ,
                           Color eyeColor, Color hairColor, Country country) throws FieldsValidationException {
        List<String> errorList = PersonValidator.validatePerson(name, coordinatesX, coordinatesY, height,
                locName, locX, locY, locZ);
        if (errorList.stream().anyMatch(Objects::nonNull)) {
            throw new FieldsValidationException(errorList);
        }
        Person person = new Person(null, name, new Coordinates(Long.parseLong(coordinatesX), Float.parseFloat(coordinatesY)),
                LocalDateTime.now(), Integer.parseInt(height), eyeColor, hairColor, country,
                new Location(Double.parseDouble(locX), Double.parseDouble(locY), Integer.parseInt(locZ), locName));
        Request request = new Request("remove_lower", person, RequestType.COMMAND);
        request.setLogin(session.getUsername()); request.setPassword(session.getPassword());
        currentController.getMainModel().getThreadPoolExecutor().execute(currentController.getMainModel().generateTask(request, true));
    }
}
