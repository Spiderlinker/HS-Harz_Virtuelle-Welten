package de.hsharz.images.ui.filter;

import java.awt.image.BufferedImage;
import java.util.Objects;

import de.hsharz.images.filter.grayvaluetransformation.BinaryFilter;
import de.hsharz.images.filter.utils.ImageUtils;
import de.hsharz.images.ui.ImageInfo;
import de.hsharz.images.utils.LayoutUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BinaryFilterPane {

	private GridPane root;
	private Slider slider;
	private TextField fldValue;
	private ImageView previewImg;
	private Button btnChoose;

	private ObjectProperty<Image> originalImage = new SimpleObjectProperty<>();
	private BufferedImage originalBufferedImage;

	public BinaryFilterPane(ImageInfo image) {
		Objects.requireNonNull(image);
		this.originalBufferedImage = ImageUtils.resize(image.getImage(), 300, 200);
		this.originalImage.set(SwingFXUtils.toFXImage(originalBufferedImage, null));

		createWidgets();
		setupInteractions();
		addWidgets();

		calculateNewImage((int) slider.getValue());
	}

	private void createWidgets() {
		root = new GridPane();
		root.setPadding(new Insets(10));
		root.setHgap(20);
		root.setVgap(20);
		LayoutUtils.setColumnWidths(root, 50, 50);
		LayoutUtils.setRowHeight(root, 80, 10, 10);

		slider = new Slider(0, 255, 127);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setSnapToTicks(true);
		slider.setMinorTickCount(4);
		slider.setMajorTickUnit(5);
		slider.setBlockIncrement(1);

		fldValue = new TextField();
		fldValue.setEditable(false);

		fldValue.textProperty().bind(slider.valueProperty().asString());

		previewImg = new ImageView();
		previewImg.imageProperty().bind(originalImage);

		btnChoose = new Button("Übernehmen");
	}

	private void setupInteractions() {
		slider.valueProperty().addListener(
				(observable, oldValue, newValue) -> calculateNewImage(newValue.intValue()));
	}

	private void calculateNewImage(int val) {
		BufferedImage newImage = new BinaryFilter(val).perform(new ImageInfo(originalBufferedImage))
				.getImage();
		originalImage.set(SwingFXUtils.toFXImage(newImage, null));
	}

	private void addWidgets() {
		root.add(previewImg, 0, 0, 2, 1);
		root.add(slider, 0, 1, 2, 1);
		root.add(fldValue, 0, 2);
		root.add(btnChoose, 1, 2);
	}

	public Button getButtonChoose() {
		return btnChoose;
	}

	public int getSelectedValue() {
		return (int) slider.getValue();
	}

	public Pane getPane() {
		return root;
	}

}
