package Rusile.client.controllers;

import Rusile.common.people.Person;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public abstract class AbstractDataController {
    private final MainController mainController;
    private ResourceBundle resourceBundle;

    public AbstractDataController(MainController mainController) {
        this.mainController = mainController;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public MainController getMainController() {
        return mainController;
    }

    public abstract void updateElements(List<Person> elementsToAdd, List<Person> elementsToRemove, List<Person> elementsToUpdate, List<Long> usersIDs);

    public abstract void initializeElements(Set<Person> musicBandSet, List<Long> usersIDs);
}
