package de.hsharz.images.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopupWindow extends Stage {

	private Scene scene;

	public PopupWindow(String title, Parent content) {
		this(title, content, false);
	}

	public PopupWindow(String title, Parent content, boolean showAndWait) {
		super(StageStyle.UTILITY);
		scene = new Scene(content);

		setScene(scene);
		setTitle(title);
		setAlwaysOnTop(true);
	}
}
