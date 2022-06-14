package Rusile.client.util;

public enum PathToViews {

    CONNECTION_VIEW("/fxml/connection.fxml"),
    LOGIN_VIEW("/fxml/login_tab.fxml"),
    REGISTRATION_VIEW("/fxml/register_tab.fxml"),
    TABLE_VIEW("/fxml/table.fxml"),
    MAIN_VIEW("/fxml/main.fxml"),
    ADD_VIEW("/fxml/add.fxml"),
    REMOVE_LOWER_VIEW("/fxml/remove_lower.fxml"),
    REMOVE_BY_ID_VIEW("/fxml/remove_by_id.fxml"),
    INFO_VIEW("/fxml/info.fxml");

    private final String path;

    PathToViews(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
