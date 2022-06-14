package Rusile.client.controllers;

import Rusile.client.models.TableModel;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.client.util.TableRow;
import Rusile.common.people.Color;
import Rusile.common.people.Country;
import Rusile.common.people.Person;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TableController extends AbstractDataController implements Initializable {

    @FXML
    public TableColumn<TableRow, Long> xCoordinate;

    @FXML
    public TableColumn<TableRow, Float> yCoordinate;

    @FXML
    private Button applyFilter;

    private URL url;
    private ResourceBundle resourceBundle;

    @FXML
    private TableColumn<TableRow, ?> coordinates;

    @FXML
    private TableColumn<TableRow, LocalDateTime> date;

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
    private TableColumn<TableRow, Integer> locationZ;

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
    private ObservableList<TableRow> masterData = FXCollections.observableArrayList();
    private final Session session;


    public TableController(ClientSocketChannelIO socketChannelIO, Session session, MainController mainController) {
        super(mainController);
        this.socketChannelIO = socketChannelIO;
        this.tableModel = new TableModel(socketChannelIO, mainController.getCurrentStage(), this, session);
        this.session = session;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.url = url;
        this.resourceBundle = resourceBundle;
        tableRows = tableModel.fillTableWithRemoteData();
        if (tableRows != null) {
            masterData.addAll(tableRows);
        }

        mainTable.setItems(masterData);

        if (tableRows != null) {
            tableRows.addListener((ListChangeListener<TableRow>) change -> updateFilteredData());
        }

        applyFilter.setOnAction(event -> updateFilteredData());

        id.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        name.setCellValueFactory(cell -> cell.getValue().nameProperty());
        xCoordinate.setCellValueFactory(cell -> cell.getValue().xProperty().asObject());
        yCoordinate.setCellValueFactory(cell -> cell.getValue().yProperty().asObject());
        date.setCellValueFactory(cell -> cell.getValue().creationDateProperty());
        height.setCellValueFactory(cell -> cell.getValue().heightProperty().asObject());
        eyeColor.setCellValueFactory(cell -> cell.getValue().eyeColorProperty());
        hairColor.setCellValueFactory(cell -> cell.getValue().hairColorProperty());
        nationality.setCellValueFactory(cell -> cell.getValue().nationalityProperty());
        locationX.setCellValueFactory(cell -> cell.getValue().locationXProperty().asObject());
        locationY.setCellValueFactory(cell -> cell.getValue().locationYProperty().asObject());
        locationZ.setCellValueFactory(cell -> cell.getValue().locationZProperty().asObject());
        locationName.setCellValueFactory(cell -> cell.getValue().locationNameProperty());


        nationalityFilter.setValue(resourceBundle.getString("main_menu.table.genre"));

        ObservableList<String> stringObservableList = FXCollections.observableArrayList();
        stringObservableList.addAll(Color.arrayOfElements());
        eyeColorFilter.setItems(stringObservableList);
        hairColorFilter.setItems(stringObservableList);

        ObservableList<String> stringObservableList1 = FXCollections.observableArrayList();
        stringObservableList1.addAll(Arrays.stream(Country.values()).map(Enum::name).collect(Collectors.toList()));
        nationalityFilter.setItems(stringObservableList1);

//        eyeColorFilter.setValue("BLACK");
//        hairColorFilter.setValue("RED");
//        nationalityFilter.setValue("GERMANY");

        setIntegerFilter(zLocationFilter);
        setIntegerFilter(xFilter);
        setIntegerFilter(idFilter);
        setIntegerFilter(heightFilter);

        setFloatFilter(yFilter);

        setDoubleFilter(xLocationFilter);
        setDoubleFilter(yLocationFilter);

    }

    private void updateFilteredData() {
        masterData.clear();
//        System.out.println(eyeColorFilter.getValue() + hairColorFilter.getValue() + nationalityFilter.getValue());
        tableRows.stream().
                filter(e -> idFilter.getText().isEmpty() || Integer.toString(e.getId()).contains(idFilter.getText())).
                filter(e -> nameFilter.getText().isEmpty() || e.getName().contains(nameFilter.getText())).
                filter(e -> (eyeColorFilter.getValue() == null) || e.getEyeColor().name().contains(eyeColorFilter.getValue())).
                filter(e -> (hairColorFilter.getValue() == null) || e.getHairColor().name().contains(hairColorFilter.getValue())).
                filter(e -> (nationalityFilter.getValue().equals("Country")) || e.getNationality().name().contains(nationalityFilter.getValue())).
                filter(e -> xFilter.getText().isEmpty() || Float.toString(e.getX()).contains(xFilter.getText())).
                filter(e -> yFilter.getText().isEmpty() || Float.toString(e.getY()).contains(yFilter.getText())).
                filter(e -> xLocationFilter.getText().isEmpty() || Double.toString(e.getLocationX()).contains(locationX.getText())).
                filter(e -> yLocationFilter.getText().isEmpty() || Double.toString(e.getLocationY()).contains(locationY.getText())).
                filter(e -> zLocationFilter.getText().isEmpty() || Integer.toString(e.getLocationZ()).contains(locationZ.getText())).
//                filter(e -> locationName.getText().isEmpty() || e.getLocationName().contains(locationName.getText())).
        forEach(e -> masterData.add(e));

        reapplyTableSortOrder();
    }

    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<TableRow, ?>> sortOrder = new ArrayList<>(mainTable.getSortOrder());
        mainTable.getSortOrder().clear();
        mainTable.getSortOrder().addAll(sortOrder);
    }

    public static void setFloatFilter(TextField filter) {
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && (!isFloat(newValue) || Float.parseFloat(newValue) < 0 ||
                    (newValue.length() > 1 && newValue.indexOf('.') != 1 && newValue.indexOf("00") == 0) ||
                    newValue.contains("d") || newValue.contains("f") || newValue.contains(" "))) {
                if (oldValue.isEmpty())
                    filter.clear();
                else
                    filter.setText(oldValue);
            }
        });
    }

    public static void setIntegerFilter(TextField filter) {
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && (!isInteger(newValue) || Integer.parseInt(newValue) < 0 ||
                    newValue.indexOf("00") == 0) ||
                    newValue.contains("d") || newValue.contains("f") || newValue.contains(" ")) {
                if (oldValue.isEmpty())
                    filter.clear();
                else
                    filter.setText(oldValue);
            }
        });
    }

    public static void setDoubleFilter(TextField filter) {
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && (!isDouble(newValue) || Double.parseDouble(newValue) < 0 ||
                    newValue.indexOf("00") == 0) ||
                    newValue.contains("d") || newValue.contains("f") || newValue.contains(" ")) {
                if (oldValue.isEmpty())
                    filter.clear();
                else
                    filter.setText(oldValue);
            }
        });
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public void updateElements(List<Person> elementsToAdd, List<Person> elementsToRemove, List<Person> elementsToUpdate, List<Long> usersIDs) {
        initialize(url, resourceBundle);
    }

    @Override
    public void initializeElements(Set<Person> musicBandSet, List<Long> usersIDs) {
        initialize(url, resourceBundle);
    }
}
