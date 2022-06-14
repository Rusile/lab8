
package Rusile.client.models;

import Rusile.client.controllers.TableController;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.client.util.TableRow;
import Rusile.common.interfaces.Data;
import Rusile.common.people.Person;
import Rusile.common.util.Request;
import Rusile.common.util.RequestType;
import Rusile.common.util.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Deque;

public class TableModel extends AbstractModel {

    private final TableController tableController;
    private final Session session;

    public TableModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, TableController tableController, Session session) {
        super(clientSocketChannelIO, currentStage);
        this.tableController = tableController;
        this.session = session;
    }

    public ObservableList<TableRow> fillTableWithRemoteData() {
        try {
            ObservableList<TableRow> tableRows = FXCollections.observableArrayList();
            Request request = new Request("show", RequestType.COMMAND);
            request.setLogin(session.getUsername()); request.setPassword(session.getPassword());
            getClientSocketChannelIO().send(request);
            Response response = (Response) getClientSocketChannelIO().receive();
            //response.getCollectionToResponse().forEach(System.out::println);
            response.getCollectionToResponse().
                    forEach(val -> tableRows.add(
                            new TableRow(
                                    Math.toIntExact(val.getId()),
                                    val.getName(),
                                    val.getCoordinates().getX(),
                                    val.getCoordinates().getY(),
                                    val.getCreationDate(),
                                    val.getHeight(),
                                    val.getEyeColor(),
                                    val.getHairColor(),
                                    val.getNationality(),
                                    val.getLocation().getX(),
                                    val.getLocation().getY(),
                                    val.getLocation().getZ(),
                                    val.getLocation().getName()
                            )
                    ));
            return tableRows;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

