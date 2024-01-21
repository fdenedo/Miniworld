module org.miniworld.miniworld {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.miniworld.miniworld to javafx.fxml;
    exports org.miniworld.miniworld;
}