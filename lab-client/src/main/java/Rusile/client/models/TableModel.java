package Rusile.client.models;

import Rusile.client.controllers.TableController;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.TableRow;
import Rusile.common.interfaces.Data;
import Rusile.common.util.Response;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class TableModel extends AbstractModel {

    private final TableController tableController;

    public TableModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, TableController tableController) {
        super(clientSocketChannelIO, currentStage);
        this.tableController = tableController;
    }

    public void initializeCheckBoxes() {

    }

    public void fillTableWithRemoteData(ObservableList<TableRow> tableRows) {
        try {
            Response response = (Response) getClientSocketChannelIO().receive();

        }
        catch (Exception e) {

        }
    }
}
