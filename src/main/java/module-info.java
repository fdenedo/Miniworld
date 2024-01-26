module org.miniworld.miniworld {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.miniworld.miniworld to javafx.fxml;
    exports org.miniworld.miniworld;
    exports org.miniworld.miniworld.model;
    opens org.miniworld.miniworld.model to javafx.fxml;
    exports org.miniworld.miniworld.view;
    opens org.miniworld.miniworld.view to javafx.fxml;
}