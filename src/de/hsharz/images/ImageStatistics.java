package de.hsharz.images;


import de.hsharz.images.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Diese Klasse ist zum Starten des Programmes verantwortlich. Sie erzeugt ein
 * neues UserInterface und stellt dieses auf dem Bildschirm dar.
 *
 * @author Oliver Lindemann u33873@hs-harz.de Matr.Nr.: 26264
 */

public class ImageStatistics extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {

		MainView infoView = new MainView();
		Scene scene = new Scene(infoView.getPane(), 1200, 800);
		stage.setScene(scene);
		stage.setOnCloseRequest(e -> stage.close());
		stage.show();

	}

}
