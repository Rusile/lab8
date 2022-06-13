package Rusile.client.util;

public enum PathToVisuals {
    TABLE_VIEW("/fxml/table_i.fxml"),
    VISUALIZATION_VIEW("/fxml/visualization.fxml");

    private final String path;

    PathToVisuals(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
