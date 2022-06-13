package Rusile.client.util;

public enum PathToViews {

    CONNECTION_VIEW("/fxml/connection.fxml"),
    LOGIN_VIEW("/fxml/login_tab.fxml"),
    REGISTRATION_VIEW("/fxml/register_tab.fxml"),
    MAIN_VIEW("/fxml/main.fxml");
    private final String path;

    PathToViews(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
