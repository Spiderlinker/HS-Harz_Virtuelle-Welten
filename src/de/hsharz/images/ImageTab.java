package de.hsharz.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ImageTab extends Tab {

	private File selectedFile;

	// Deque ('Deck') ist unsynchronized und optimiert für performance
	// und ist das nicht-thread-safe Äquivalent zu Stack
	private Deque<Image> previousImages = new LinkedList<>();
	private Deque<Image> nextImages = new LinkedList<>();

	private Image originalImage;
	private ObjectProperty<Image> currentImageProperty = new SimpleObjectProperty<>();

	private BorderPane root;
	private HBox boxMenu;
	private StackPane paneImage;

	private Button btnUndo;
	private Button btnRedo;
	private ToggleButton btnShowOriginalImage;

	private ImageView viewOriginalImage;
	private ImageView viewCurrentImage;

	public ImageTab(File selectedFile) throws FileNotFoundException {
		this.selectedFile = Objects.requireNonNull(selectedFile);

		loadImages();

		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void loadImages() throws FileNotFoundException {
		originalImage = new Image(new FileInputStream(selectedFile));
		currentImageProperty.set(new Image(new FileInputStream(selectedFile)));
	}

	private void createWidgets() {
		setText(selectedFile.getName());

		root = new BorderPane();
		boxMenu = new HBox(5);
		paneImage = new StackPane();

		btnUndo = new Button("Undo");
		btnRedo = new Button("Redo");
		btnShowOriginalImage = new ToggleButton("Show Original Image");

		viewOriginalImage = new ImageView(originalImage);
		viewOriginalImage.visibleProperty().bind(btnShowOriginalImage.selectedProperty());

		viewCurrentImage = new ImageView();
		viewCurrentImage.imageProperty().bind(currentImageProperty);
	}

	private void setupInteractions() {
		btnUndo.setOnAction(e -> undo());
		btnRedo.setOnAction(e -> redo());
	}

	private void addWidgets() {
		root.setTop(boxMenu);
		root.setCenter(new ScrollPane(paneImage));

		boxMenu.getChildren().addAll(btnUndo, btnRedo, btnShowOriginalImage);

		paneImage.getChildren().addAll(viewCurrentImage, viewOriginalImage);

		setContent(root);
	}

	public void undo() {
		if (previousImages.isEmpty()) {
			return;
		}

		nextImages.add(currentImageProperty.get());
		currentImageProperty.set(previousImages.pop());
	}

	public void redo() {
		if (nextImages.isEmpty()) {
			return;
		}

		previousImages.add(currentImageProperty.get());
		currentImageProperty.set(nextImages.pop());
	}

	public void performOperation(Function<File, Image> imageFileOperation) {
		Image newImage = imageFileOperation.apply(selectedFile);
		if (newImage != null) {
			previousImages.add(currentImageProperty.get());
			currentImageProperty.set(newImage);
		}
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public BufferedImage getCurrentImage() {
		return SwingFXUtils.fromFXImage(currentImageProperty.get(), null);
	}

}
