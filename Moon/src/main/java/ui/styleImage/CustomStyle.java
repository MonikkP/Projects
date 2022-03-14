package ui.styleImage;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CustomStyle {
    public static void setStyleImage(ImageView imageView) {
        Glow glow = new Glow();
        glow.setLevel(0.5);
        imageView.setOnMouseEntered(event -> {
            imageView.setEffect(glow);
        });
        imageView.setOnMouseExited(event -> {
            imageView.setEffect(null);
        });
    }

    public static void setStyleExitButton(Button button) {
        button.setStyle("-fx-background-color: #232222; ");
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: #6e1010; ");
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: #232222; ");
        });

    }

    public static void setStyleImageButtons(ImageView imageView) {
        Rectangle clip = new Rectangle();
        clip.setWidth(252);
        clip.setHeight(106);

        clip.setArcHeight(20);
        clip.setArcWidth(20);
        clip.setStroke(Color.BLACK);
        imageView.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        imageView.setClip(null);

        imageView.setEffect(new DropShadow(20, Color.BLACK));

        imageView.setImage(image);

    }
}
