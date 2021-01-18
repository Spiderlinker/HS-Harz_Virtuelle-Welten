package de.hsharz.images.ui.filter;

import java.awt.image.BufferedImage;
import java.util.Objects;

import de.hsharz.images.filter.utils.ImageUtils;
import de.hsharz.images.pointoperator.HistogramEqualization;
import de.hsharz.images.ui.ImageInfo;
import de.hsharz.images.ui.ImageInfo.ImageColor;
import de.hsharz.images.utils.LayoutUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HistogramEqualizationPane {

	private GridPane root;
	private ImageView previewImg;
	private Button btnChoose;

	private HBox boxButtons;
//	private RadioButton btnGray;
	private RadioButton btnRed;
	private RadioButton btnGreen;
	private RadioButton btnBlue;

	private ImageColor currentVal;

	private ObjectProperty<Image> originalImage = new SimpleObjectProperty<>();
	private BufferedImage originalBufferedImage;

	public HistogramEqualizationPane(ImageInfo image) {
		Objects.requireNonNull(image);
		this.originalBufferedImage = ImageUtils.resize(image.getImage(), 300, 200);
		this.originalImage.set(SwingFXUtils.toFXImage(originalBufferedImage, null));

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		root = new GridPane();
		root.setPadding(new Insets(10));
		root.setHgap(20);
		root.setVgap(20);
		LayoutUtils.setColumnWidths(root, 50, 50);
		LayoutUtils.setRowHeight(root, 80, 10, 10);

		boxButtons = new HBox(20);
		boxButtons.setMaxWidth(Double.MAX_VALUE);
//		boxButtons.setAlignment(Pos.CENTER_LEFT);

//		btnGray = new RadioButton("Grau");
		btnRed = new RadioButton("Rot");
		btnGreen = new RadioButton("Grün");
		btnBlue = new RadioButton("Blau");

		ToggleGroup group = new ToggleGroup();
//		group.getToggles().add(btnGray);
		group.getToggles().add(btnRed);
		group.getToggles().add(btnGreen);
		group.getToggles().add(btnBlue);

		previewImg = new ImageView();
		previewImg.imageProperty().bind(originalImage);

		btnChoose = new Button("Übernehmen");
		btnChoose.setDisable(true);
	}

	private void setupInteractions() {
//		btnGray.setOnAction(e -> calculateNewImage(ImageColor.GRAY));
		btnRed.setOnAction(e -> calculateNewImage(ImageColor.RED));
		btnGreen.setOnAction(e -> calculateNewImage(ImageColor.GREEN));
		btnBlue.setOnAction(e -> calculateNewImage(ImageColor.BLUE));
	}

	private void calculateNewImage(ImageColor val) {
		currentVal = val;
		btnChoose.setDisable(false);
		BufferedImage newImage = new HistogramEqualization(val).perform(new ImageInfo(originalBufferedImage))
				.getImage();
		originalImage.set(SwingFXUtils.toFXImage(newImage, null));
	}

	private void addWidgets() {
		boxButtons.getChildren().addAll(btnRed, btnGreen, btnBlue);
		
		root.add(previewImg, 0, 0, 2, 1);
		root.add(boxButtons, 0, 1, 2, 1);
		root.add(btnChoose, 1, 2);
	}

	public Button getButtonChoose() {
		return btnChoose;
	}

	public ImageColor getSelectedValue() {
		return currentVal;
	}

	public Pane getPane() {
		return root;
	}

}
