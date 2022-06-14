package Rusile.client.models;

import Rusile.client.controllers.AddController;
import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.client.util.validators.NumberValidator;
import Rusile.client.util.validators.PersonValidator;
import Rusile.common.people.*;
import Rusile.common.util.Request;
import Rusile.common.util.RequestType;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AddModel extends AbstractModel{
    private final Session session;

    private final AddController currentController;

    public AddModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, Session session, AddController currentController) {
        super(clientSocketChannelIO, currentStage);
        this.session = session;
        this.currentController = currentController;
    }

    public void processAdd(String name, String coordinatesX, String coordinatesY, String height,
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
        Request request = new Request("add", person, RequestType.COMMAND);
        request.setLogin(session.getUsername()); request.setPassword(session.getPassword());
        currentController.getMainModel().getThreadPoolExecutor().execute(currentController.getMainModel().generateTask(request, true));
    }

    public void processAddIfMin(String name, String coordinatesX, String coordinatesY, String height,
                                String locName, String locX, String locY, String locZ,
                                Color eyeColor, Color hairColor, Country country) throws FieldsValidationException {
        List<String> errorList = PersonValidator.validatePerson(name, coordinatesX, coordinatesY, height, locName, locX, locY, locZ);
        if (errorList.stream().anyMatch(Objects::nonNull)) {
            throw new FieldsValidationException(errorList);
        }
        Person person = new Person(null, name, new Coordinates(Long.parseLong(coordinatesX), Float.parseFloat(coordinatesY)),
                LocalDateTime.now(), Integer.parseInt(height), eyeColor, hairColor, country,
                new Location(Double.parseDouble(locX), Double.parseDouble(locY), Integer.parseInt(locZ), locName));
        Request request = new Request("add_if_min", person, RequestType.COMMAND);
        request.setLogin(session.getUsername()); request.setPassword(session.getPassword());
        currentController.getMainModel().getThreadPoolExecutor().execute(currentController.getMainModel().generateTask(request, true));
    }

    public void processUpdate(String id, String name, String coordinatesX, String coordinatesY, String height,
                              String locName, String locX, String locY, String locZ,
                              Color eyeColor, Color hairColor, Country country) throws FieldsValidationException {
        List<String> errorList = NumberValidator.validateId(id);
        errorList.addAll(PersonValidator.validatePerson(name, coordinatesX, coordinatesY, height, locName, locX, locY, locZ));
        if (errorList.stream().anyMatch(Objects::nonNull)) {
            throw new FieldsValidationException(errorList);
        }
        Person person = new Person(NumberValidator.getValidatedId(id), name, new Coordinates(Long.parseLong(coordinatesX), Float.parseFloat(coordinatesY)),
                LocalDateTime.now(), Integer.parseInt(height), eyeColor, hairColor, country,
                new Location(Double.parseDouble(locX), Double.parseDouble(locY), Integer.parseInt(locZ), locName));
        Request request = new Request("update", NumberValidator.getValidatedId(id), person, RequestType.COMMAND);
        request.setLogin(session.getUsername()); request.setPassword(session.getPassword());
        currentController.getMainModel().getThreadPoolExecutor().execute(currentController.getMainModel().generateTask(request, true));

    }


}
