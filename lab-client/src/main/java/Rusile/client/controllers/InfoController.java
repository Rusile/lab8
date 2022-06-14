package Rusile.client.controllers;

import Rusile.client.exceptions.ExceptionWithAlert;
import Rusile.client.models.MainModel;
import Rusile.client.util.PathToViews;
import Rusile.common.people.Person;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoController extends AbstractController implements Initializable {
    private final Person person;
    private final MainModel mainModel;

    @FXML
    private Label personLabel;

    public InfoController(Person person, MainModel mainModel) {
        this.person = person;
        this.mainModel = mainModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        personLabel.setText(person.toString());
    }

    @FXML
    public void updateAction() {
        try {
            AddController controller = showPopUpStage(PathToViews.ADD_VIEW,
                    param -> new AddController(mainModel.getClientSocketChannelIO(), mainModel.getSession(), mainModel),
                    getResourceBundle().getString("add_menu.title"),
                    getResourceBundle());
            controller.setFields(person.getId(),
                    person.getName(),
                    person.getCoordinates().getX(),
                    person.getCoordinates().getY(),
                    person.getHeight(), person.getLocation().getName(),person.getLocation().getX(),
                    person.getLocation().getY(), person.getLocation().getZ(),
                    person.getEyeColor(), person.getHairColor(), person.getNationality());
            getCurrentStage().close();
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }
    }

    @FXML
    public void removeAction() {
        try {
            RemoveByIdController controller = showPopUpStage(PathToViews.REMOVE_BY_ID_VIEW,
                    param -> new RemoveByIdController(mainModel.getClientSocketChannelIO(), mainModel.getSession(), mainModel),
                    getResourceBundle().getString("remove_by_id.title"),
                    getResourceBundle());
            controller.setField(person.getId());
            getCurrentStage().close();
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }
    }

    @FXML
    public void closeAction() {
        getCurrentStage().close();
    }
}
