package Rusile.client.controllers;

import Rusile.common.people.Person;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class VisualizationController extends AbstractDataController implements Initializable {


    private final HashMap<Person, Canvas> visualPeople = new HashMap<>();

    @FXML
    public Pane personPane;


    public VisualizationController(MainController mainController) {
        super(mainController);
    }

    @Override
    public void updateElements(List<Person> elementsToAdd, List<Person> elementsToRemove, List<Person> elementsToUpdate, List<Long> usersIDs) {
        for (Person m : elementsToAdd) {
            addToVisual(m, !usersIDs.contains(m.getId()));
        }
        for (Person m : elementsToRemove) {
            removeFromVisual(m);
        }
        List<Long> idsToUpdate = elementsToUpdate.stream().map(Person::getId).collect(Collectors.toList());
        for (Long id : idsToUpdate) {
            Person m = elementsToUpdate.stream().filter(person -> person.getId().equals(id)).collect(Collectors.toList()).get(0);
            Person n = visualPeople.keySet().stream().filter(person -> person.getId().equals(id)).collect(Collectors.toList()).get(0);
            removeFromVisual(n);
            addToVisual(m, !usersIDs.contains(m.getId()));
        }
    }

    @Override
    public void initializeElements(Set<Person> personSet, List<Long> usersIDs) {
        for (Person m : personSet) {
            addToVisual(m, true);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        initializeElements(getMainController().getMainModel().getPersons(), new ArrayList<>());
    }

    public void removeFromVisual(Person person) {
        Canvas canvas = visualPeople.get(person);
        personPane.getChildren().remove(canvas);
    }
    public void addToVisual(Person person, boolean alien) {
        Color color;
        if (alien) {
            color = Color.CYAN;
        } else {
            color = Color.DEEPPINK;
        }
        Canvas canvas = getMainController().getMainModel().generateBandCanvas(color, person);
        visualPeople.put(person, canvas);
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(1500));
        fade.setFromValue(0);
        fade.setToValue(10);
        fade.setNode(canvas);
        fade.play();
        personPane.getChildren().add(canvas);
    }
}
