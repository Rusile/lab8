package Rusile.client.controllers;

import Rusile.client.models.TableModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.TableRow;
import Rusile.common.people.Color;
import Rusile.common.people.Country;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class TableController extends AbstractController implements Initializable {

    @FXML
    public TableColumn xCoordinate;

    @FXML
    public TableColumn locCoordinate;

    @FXML
    private Button applyFilter;

    @FXML
    private TableColumn<TableRow, ?> coordinates;

    @FXML
    private TableColumn<TableRow, String> date;

    @FXML
    private ChoiceBox<String> dateFilter;

    @FXML
    private TableColumn<TableRow, Color> eyeColor;

    @FXML
    private ChoiceBox<String> eyeColorFilter;

    @FXML
    private FlowPane filterPane;

    @FXML
    private TableColumn<TableRow, Color> hairColor;

    @FXML
    private ChoiceBox<String> hairColorFilter;

    @FXML
    private TableColumn<TableRow, Integer> height;

    @FXML
    private TextField heightFilter;

    @FXML
    private TableColumn<TableRow, Integer> id;

    @FXML
    private TextField idFilter;

    @FXML
    private TableColumn<?, ?> location;

    @FXML
    private TableColumn<TableRow, String> locationName;

    @FXML
    private TableColumn<TableRow, Double> locationX;

    @FXML
    private TableColumn<TableRow, Double> locationY;

    @FXML
    private TableColumn<TableRow, Double> locationZ;

    @FXML
    private TableView<TableRow> mainTable;

    @FXML
    private TableColumn<TableRow, String> name;

    @FXML
    private TextField nameFilter;

    @FXML
    private TextField nameLocationFilter;

    @FXML
    private TableColumn<TableRow, Country> nationality;

    @FXML
    private ChoiceBox<String> nationalityFilter;

    @FXML
    private Pane visualizationPane;

    @FXML
    private TextField xFilter;

    @FXML
    private TextField xLocationFilter;

    @FXML
    private TextField yFilter;

    @FXML
    private TextField yLocationFilter;

    @FXML
    private TextField zLocationFilter;

    private final ClientSocketChannelIO socketChannelIO;
    private final TableModel tableModel;

    private ObservableList<TableRow> tableRows = FXCollections.observableArrayList();

    public TableController(ClientSocketChannelIO socketChannelIO) {
        this.socketChannelIO = socketChannelIO;
        this.tableModel = new TableModel(socketChannelIO, getCurrentStage(), this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
