package Rusile.client.models;

import Rusile.client.controllers.InfoController;
import Rusile.client.controllers.MainController;
import Rusile.client.exceptions.ExceptionWithAlert;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.PathToViews;
import Rusile.client.util.Session;
import Rusile.common.people.Person;
import Rusile.common.util.CollectionResponse;
import Rusile.common.util.Request;
import Rusile.common.util.RequestType;
import Rusile.common.util.Response;
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
import java.util.stream.Collectors;

public class MainModel extends AbstractModel {

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
                    //getNewCollection();
                    return null;
                }
            };
        }
    };

    public MainModel(ClientSocketChannelIO clientSocketChannelIO, Stage currentStage, Session session, MainController mainController) {
        super(clientSocketChannelIO, currentStage);
        this.currentController = mainController;
        this.session = session;
    }

    public void runUpdateLoop() {
        scheduledService.setPeriod(Duration.millis(UPDATE_TIME));
        scheduledService.start();
    }

    public Session getSession() {
        return session;
    }

    public ExecutorService getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setToolTip(TableColumn<Person, String> tableColumn) {
        tableColumn.setCellFactory
                (column -> new TableCell<Person, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if (item != null && item.length() >= 10) {
                            setTooltip(new Tooltip(item));
                        }
                    }
                });
    }


    public Set<Person> getPersons() {
        try {
            Request request = new Request("show", RequestType.COMMAND);
            request.setLogin(session.getUsername());
            request.setPassword(session.getPassword());
            getClientSocketChannelIO().send(request);
            Response response = (Response) getClientSocketChannelIO().receive();
            return new HashSet<>(response.getCollectionToResponse());
        }
        catch (Exception e) {
            return null;
        }
    }
//    public void getNewCollection() {
//        Task<Response> task = generateUpdateTask();
//        task.setOnSucceeded(event -> {
//            AbstractResponse response = task.getValue();
//            if (response.getType().equals(CollectionResponse.class) && response.isSuccess()) {
//                Set<MusicBand> newCollection = ((CollectionResponse) response).getCollection();
//                List<Long> newIDs = ((CollectionResponse) response).getUsersIds();
//                currentController.getCurrentDataController().updateElements(getElementsToAdd(newCollection),
//                        getElementsToRemove(newCollection),
//                        getElementsToUpdate(newCollection),
//                        newIDs);
//                personSet.clear();
//                personSet.addAll(newCollection);
//                usersIDs.clear();
//                usersIDs.addAll(newIDs);
//                currentController.getConnectionLabel().setText(currentController.getConnectionText());
//            }
//        });
//        threadPoolExecutor.execute(task);
//    }

//    public void initializeCollection() throws ExceptionWithAlert {
//        Task<Response> task = generateUpdateTask();
//        task.setOnSucceeded(event -> {
//            Response response = task.getValue();
//            if (response.getType().equals(CollectionResponse.class) && response.isSuccess()) {
//                Set<MusicBand> newCollection = ((CollectionResponse) response).getCollection();
//                List<Long> newIDs = ((CollectionResponse) response).getUsersIds();
//                personSet.clear();
//                personSet.addAll(newCollection);
//                usersIDs.clear();
//                usersIDs.addAll(newIDs);
//                currentController.getCurrentDataController().initializeElements(personSet, usersIDs);
//                currentController.getConnectionLabel().setText(currentController.getConnectionText());
//            }
//        });
//        threadPoolExecutor.execute(task);
//    }


    public void processClearAction() {
        Request request = new Request("clear", RequestType.COMMAND);
        request.setLogin(session.getUsername());
        request.setPassword(session.getPassword());
        threadPoolExecutor.execute(generateTask(request, true));
    }

    public void prepareForExit() {
        threadPoolExecutor.shutdown();
        scheduledService.cancel();
        try {
            getClientSocketChannelIO().closeChannel();
        } catch (IOException e) {
            currentController.showErrorAlert(e.getMessage());
        }
    }

    public Canvas generateBandCanvas(Color color, Person person) {
        Canvas canvas = drawPerson(color, 1L);

        canvas.setLayoutX(15.0 + (750.0 / 947.0) * person.getCoordinates().getX());
        canvas.setLayoutY(593.0 - (593.0 / 104.0) * person.getCoordinates().getY());
        canvas.setOnMouseEntered(event -> {
            canvas.setScaleX(1.07);
            canvas.setScaleY(1.07);
        });
        canvas.setOnMouseExited(event -> {
            canvas.setScaleX(1);
            canvas.setScaleY(1);
        });
        canvas.setOnMouseClicked(event -> showInfoElement(person));
        return canvas;
    }

    private Canvas drawPerson(Color color, Long amount) {
        final int offset = 20;
        Canvas canvas = new Canvas(22 + (amount - 1) * offset, 50);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.setStroke(color);
        gc.setLineWidth(3);
        for (int i = 0; i < amount; i++) {
            gc.fillOval(5 + i * offset, 6, 12, 12);
            gc.strokeLine(11 + i * offset, 12, 11 + i * offset, 36);
            gc.strokeLine(11 + i * offset, 17, 20 + i * offset, 29);
            gc.strokeLine(11 + i * offset, 17, 2 + i * offset, 29);
            gc.strokeLine(11 + i * offset, 36, 20 + i * offset, 48);
            gc.strokeLine(11 + i * offset, 36, 2 + i * offset, 48);
        }
        return canvas;
    }
//
//    public List<MusicBand> getElementsToUpdate(Set<MusicBand> newCollection) {
//        List<Long> newIDs = newCollection.stream().map(MusicBand::getId).toList();
//        List<MusicBand> elementsToUpdate = new ArrayList<>();
//        for (Long id : newIDs) {
//            MusicBand m = newCollection.stream().filter(musicBand -> musicBand.getId().equals(id)).findFirst().orElse(null);
//            MusicBand n = personSet.stream().filter(musicBand -> musicBand.getId().equals(id)).findFirst().orElse(null);
//            if (m != null && n != null) {
//                if (!m.equals(n)) {
//                    elementsToUpdate.add(m);
//                }
//            }
//        }
//        return elementsToUpdate;
//    }

//    public List<MusicBand> getElementsToAdd(Set<MusicBand> newCollection) {
//        List<Long> currentIDs = personSet.stream().map(MusicBand::getId).toList();
//        List<Long> newIDs = newCollection.stream().map(MusicBand::getId).toList();
//        List<MusicBand> elementsToAdd = new ArrayList<>();
//        for (Long id : newIDs) {
//            if (!currentIDs.contains(id)) {
//                newCollection.stream().filter(musicBand -> musicBand.getId().equals(id)).findFirst().ifPresent(elementsToAdd::add);
//            }
//        }
//        return elementsToAdd;
//    }
//
//    public List<MusicBand> getElementsToRemove(Set<MusicBand> newCollection) {
//        List<Long> currentIDs = personSet.stream().map(MusicBand::getId).toList();
//        List<Long> newIDs = newCollection.stream().map(MusicBand::getId).toList();
//        List<MusicBand> elementsToRemove = new ArrayList<>();
//        for (Long id : currentIDs) {
//            if (!newIDs.contains(id)) {
//                personSet.stream().filter(musicBand -> musicBand.getId().equals(id)).findFirst().ifPresent(elementsToRemove::add);
//            }
//        }
//        return elementsToRemove;
//    }

    public void showInfoElement(Person person) {
        try {
            currentController.showPopUpStage(PathToViews.INFO_VIEW,
                    param -> new InfoController(person, this),
                    currentController.getResourceBundle().getString("visual_info.title"),
                    currentController.getResourceBundle());
        } catch (ExceptionWithAlert e) {
            e.showAlert();
        }

    }

    public Task<Response> generateTask(Request commandRequest, boolean isUpdateNeeded) {
        Task<Response> task = new Task<Response>() {
            @Override
            protected Response call() throws Exception {
                try {
                    commandRequest.setLogin(session.getUsername());
                    commandRequest.setPassword(session.getPassword());
                    getClientSocketChannelIO().send(commandRequest);
                    return (Response) getClientSocketChannelIO().receive();
                } catch (IOException e) {
                    throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.connection"));
                } catch (ClassNotFoundException e) {
                    throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.response"));
                }
            }
        };
        task.setOnFailed(event -> {
            ExceptionWithAlert exceptionWithAlert = (ExceptionWithAlert) task.getException();
            if (exceptionWithAlert.isFatal()) {
                exceptionWithAlert.showAlert();
                prepareForExit();
                Platform.exit();
            } else {
                exceptionWithAlert.showAlert();
            }
        });
        task.setOnSucceeded(event -> {
            Response response = task.getValue();

            currentController.showInfoAlert(response.getMessageToResponse());
            if (isUpdateNeeded) {
                //getNewCollection();
            }

        });
        return task;
    }

    public Task<Response> generateUpdateTask() {
        Task<Response> task = new Task<Response>() {
            @Override
            protected Response call() throws Exception {
                try {
                    Request request = new Request("show", RequestType.COMMAND);
                    request.setLogin(session.getUsername());
                    request.setPassword(session.getPassword());
                    getClientSocketChannelIO().send(request);
                    return (Response) getClientSocketChannelIO().receive();
                } catch (IOException e) {
                    throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.connection"));
                } catch (ClassNotFoundException e) {
                    throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.response"));
                }
            }
        };
        task.setOnFailed(event -> {
            ExceptionWithAlert exceptionWithAlert = (ExceptionWithAlert) task.getException();
            if (exceptionWithAlert.isFatal()) {
                exceptionWithAlert.showAlert();
                prepareForExit();
                Platform.exit();
            }
        });
        return task;
    }
}
