package Rusile.client.controllers;

import Rusile.client.exceptions.FieldsValidationException;
import Rusile.client.models.MainModel;
import Rusile.client.models.RemoveByIdModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RemoveByIdController extends AbstractController implements Initializable {

    private final RemoveByIdModel removeByIdModel;

    private final MainModel mainModel;

    public RemoveByIdController(ClientSocketChannelIO clientSocketChannelIO, Session session, MainModel mainModel) {
        removeByIdModel = new RemoveByIdModel(clientSocketChannelIO, getCurrentStage(), session, this);
        this.mainModel = mainModel;
    }

    @FXML
    private Button removeById_button;

    @FXML
    private TextField removeById_field;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        addRegex(removeById_field);
    }

    @FXML
    public void removeByIdAction() {
        List<TextField> textFields = new ArrayList<>();
        textFields.add(removeById_field);
        removeFieldsColoring(textFields);
        try {
            removeByIdModel.processRemove(removeById_field.getText());
            getCurrentStage().close();
        } catch (FieldsValidationException e) {
            showFieldsErrors(e.getErrorList(), textFields);
        }
    }


    public MainModel getMainModel() {
        return mainModel;
    }

    public void setField(Long id) {
        removeById_field.setText(id.toString());
    }
}
