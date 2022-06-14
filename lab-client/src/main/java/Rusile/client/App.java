package Rusile.client;

import Rusile.client.controllers.ConnectionController;
import Rusile.client.networkManager.ClientSocketChannelIO;
import Rusile.client.util.PathToViews;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientSocketChannelIO clientSocketChannelIO = new ClientSocketChannelIO();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource(PathToViews.CONNECTION_VIEW.getPath()));
        loader.setResources(ResourceBundle.getBundle("localization.locale", new Locale("en")));
        loader.setControllerFactory(cont -> new ConnectionController(clientSocketChannelIO));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        ConnectionController connectionController = loader.getController();
        connectionController.setCurrentStage(primaryStage);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ITMO_LGBT.png"))));
        primaryStage.setTitle(loader.getResources().getString("app.menu"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
