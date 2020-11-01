package de.hsharz.images;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopupWindow {

	private Stage stage;
	private Scene scene;

	public PopupWindow(String title, Parent content) {
		scene = new Scene(content);

		stage = new Stage(StageStyle.UTILITY);
		stage.setScene(scene);
		stage.setTitle(title);
		stage.setAlwaysOnTop(true);
		stage.show();
	}

}
