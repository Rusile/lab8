package Rusile.client.models;

import Rusile.client.controllers.MainController;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.Session;
import Rusile.common.people.Person;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainModel extends AbstractModel{

    private final static int UPDATE_TIME = 5000;
    private final Session session;
    private final MainController currentController;
    private final Set<Person> personSet = new HashSet<>();
    private final List<Long> usersIDs = new ArrayList<>();
    private final ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(4);
    private final ScheduledService<Void> scheduledService = new ScheduledService<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() {
                    getNewCollection();
                    return null;
                }
            };
        }
    };

}
