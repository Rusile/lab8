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

    public MainController(ClientSocketChannelIO clientSocketChannelIO, Session session, PathToVisuals currentVisual) {
        mainModel = new MainModel(clientSocketChannelIO, getCurrentStage(), session, this);
        //this.currentVisual = currentVisual;
    }

    private PathToVisuals currentVisual;

    @FXML
    private Pane visualizationPane;

    @FXML
    private ChoiceBox<LanguagesEnum> language_selector;

    @FXML
    private Button applyFilter;

    @FXML
    private TableColumn<?, ?> coordinates;

    @FXML
    private TableColumn<?, ?> date;

    @FXML
    private ChoiceBox<?> dateFilter;

    @FXML
    private TableColumn<?, ?> eyeColor;

    @FXML
    private ChoiceBox<?> eyeColorFilter;

    @FXML
    private FlowPane filterPane;

    @FXML
    private TableColumn<?, ?> hairColor;

    @FXML
    private ChoiceBox<?> hairColorFilter;

    @FXML
    private TableColumn<?, ?> height;

    @FXML
    private TextField heightFilter;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TextField idFilter;

    @FXML
    private TableColumn<?, ?> location;

    @FXML
    private TableColumn<?, ?> location1;

    @FXML
    private TableColumn<?, ?> location2;

    @FXML
    private TableColumn<?, ?> locationName;

    @FXML
    private TableColumn<?, ?> locationX;

    @FXML
    private TableColumn<?, ?> locationY;

    @FXML
    private TableColumn<?, ?> locationZ;

    @FXML
    private TableView<?> mainTable;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TextField nameFilter;

    @FXML
    private TextField nameLocationFilter;

    @FXML
    private TableColumn<?, ?> nationality;

    @FXML
    private ChoiceBox<?> nationalityFilter;

    @FXML
    private TextField xFilter;

    @FXML
    private TextField xLocationFilter;

    @FXML
    private TextField yLocationFilter;

    @FXML
    private TextField zLocationFilter;

    @FXML
    void addAction() {
        try {
            showPopUpStage(PathToViews.ADD_VIEW,
                    param -> new AddController(mainModel.getClientSocketWorker(),
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
                        param -> new MainController(mainModel.getClientSocketChannelIO(), mainModel.getSession()), getResourceBundle());
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
                loader.setControllerFactory(param -> new TableController(this));
                switchButton.setText(getResourceBundle().getString("main_menu.button.switch_to_visual"));
            }
            currentVisual = pathToVisuals;
            Parent parent = loader.load();
            currentDataController = loader.getController();
            mainModel.initializeCollection();
            targetPane.getChildren().clear();
            targetPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionWithAlert e) {
            if (e.isFatal()) {
                e.showAlert();
                mainModel.prepareForExit();
                Platform.exit();
            } else {
                e.showAlert();
            }
        }
    }
}
