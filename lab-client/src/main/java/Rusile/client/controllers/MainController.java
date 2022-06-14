package Rusile.client.controllers;

import Rusile.client.exceptions.ExceptionWithAlert;
import Rusile.client.models.MainModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.LanguagesEnum;
import Rusile.client.util.PathToViews;
import Rusile.client.util.PathToVisuals;
import Rusile.client.util.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController extends  AbstractController implements Initializable {

    private final MainModel mainModel;
    @FXML
    public Button switchButton;
    @FXML
    public ChoiceBox<LanguagesEnum> language_selector;
    @FXML
    public Pane visualizationPane;


    private PathToVisuals currentVisual;

    protected AbstractDataController currentDataController;

    public MainController(ClientSocketChannelIO clientSocketChannelIO, Session session, PathToVisuals currentVisual) {
        mainModel = new MainModel(clientSocketChannelIO, getCurrentStage(), session, this);
        this.currentVisual = currentVisual;
    }


    @FXML
    void addAction() {
        try {
            showPopUpStage(PathToViews.ADD_VIEW,
                    param -> new AddController(mainModel.getClientSocketChannelIO(),
                            mainModel.getSession(),
                            mainModel),
                    getResourceBundle().getString("add_menu.title"),
                    getResourceBundle());
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setResourceBundle(resourceBundle);
        language_selector.setItems(FXCollections.observableArrayList(Stream.of(LanguagesEnum.values()).collect(Collectors.toList())));
        language_selector.setValue(mainModel.getLanguage(getResourceBundle().getLocale().getLanguage()));
        language_selector.getSelectionModel().selectedItemProperty().addListener((m, oldValue, newValue) -> {
            try {
                setResourceBundle(ResourceBundle.getBundle("localization.locale", new Locale(newValue.getLanguageName())));
                switchScene(PathToViews.MAIN_VIEW,
                        param -> new MainController(mainModel.getClientSocketChannelIO(), mainModel.getSession(), currentVisual), getResourceBundle());
                language_selector.setValue(newValue);
            } catch (ExceptionWithAlert e) {
                e.showAlert();
            }
        });
        loadDataVisualization(currentVisual, visualizationPane);
        mainModel.runUpdateLoop();
    }

    private void loadDataVisualization(PathToVisuals pathToVisuals, Pane targetPane) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(pathToVisuals.getPath()));
            loader.setResources(getResourceBundle());
            if (pathToVisuals.equals(PathToVisuals.VISUALIZATION_VIEW)) {
                loader.setControllerFactory(param -> new VisualizationController(this));
                switchButton.setText(getResourceBundle().getString("main_menu.button.switch_to_table"));
            } else if (pathToVisuals.equals(PathToVisuals.TABLE_VIEW)) {
                loader.setControllerFactory(param -> new TableController(mainModel.getClientSocketChannelIO(), mainModel.getSession(), this));
                switchButton.setText(getResourceBundle().getString("main_menu.button.switch_to_visual"));
            }
            currentVisual = pathToVisuals;
            Parent parent = loader.load();
            currentDataController = loader.getController();
            //mainModel.initializeCollection();
            targetPane.getChildren().clear();
            targetPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (ExceptionWithAlert e) {
//            if (e.isFatal()) {
//                e.showAlert();
//                mainModel.prepareForExit();
//                Platform.exit();
//            } else {
//                e.showAlert();
//            }
//        }
    }

    @FXML
    public void switchView() {
        if (currentVisual.equals(PathToVisuals.VISUALIZATION_VIEW)) {
            loadDataVisualization(PathToVisuals.TABLE_VIEW, visualizationPane);
        } else {
            loadDataVisualization(PathToVisuals.VISUALIZATION_VIEW, visualizationPane);
        }
    }

    @FXML
    public void logout() {
        try {
            switchScene(PathToViews.LOGIN_VIEW,
                    param -> new LoginController(mainModel.getClientSocketChannelIO()),
                    getResourceBundle());
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }
    }


    public MainModel getMainModel() {
        return mainModel;
    }

    public void removeById() {
        try {
            showPopUpStage(PathToViews.REMOVE_BY_ID_VIEW,
                    param -> new RemoveByIdController(mainModel.getClientSocketChannelIO(),
                            mainModel.getSession(),
                            mainModel),
                    getResourceBundle().getString("remove_by_id.title"),
                    getResourceBundle());
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }
    }

    public void removeLower() {
        try {
            showPopUpStage(PathToViews.REMOVE_LOWER_VIEW,
                    param -> new RemoveLowerController(mainModel.getClientSocketChannelIO(),
                            mainModel.getSession(),
                            mainModel),
                    getResourceBundle().getString("add_menu.title"),
                    getResourceBundle());
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }
    }

    public void clear() {
        //mainModel.processClearAction();
        //mainModel.getNewCollection();
    }

    public void exit() {
        mainModel.prepareForExit();
        Platform.exit();
    }
}
