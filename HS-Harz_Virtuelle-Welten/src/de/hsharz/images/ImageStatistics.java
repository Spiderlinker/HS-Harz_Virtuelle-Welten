package de.hsharz.images;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ImageStatistics extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {

        ImageInfoView infoView = new ImageInfoView();
        Scene scene = new Scene(infoView.getPane(), 700, 800);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> stage.close());
        stage.show();

    }

}
