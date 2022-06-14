package Rusile.client.controllers;

import Rusile.common.people.Person;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class TableController extends AbstractDataController implements Initializable {
    public TableController(MainController mainController) {
        super(mainController);
    }

    @Override
    public void updateElements(List<Person> elementsToAdd, List<Person> elementsToRemove, List<Person> elementsToUpdate, List<Long> usersIDs) {

    }

    @Override
    public void initializeElements(Set<Person> musicBandSet, List<Long> usersIDs) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
