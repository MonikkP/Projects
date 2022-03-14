module ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.jshell;
    requires java.sql;
    requires javafx.swing;
    requires org.apache.pdfbox;

    opens ui to javafx.fxml;
    opens domain to javafx.fxml, javafx.base;

    exports domain;
    exports ui;
}