package de.hsharz.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import javax.imageio.ImageIO;

import de.hsharz.images.filter.Filter;
import de.hsharz.images.utils.ZoomableScrollPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ImageTab extends Tab {

	private File selectedFile;

	// Deque ('Deck') ist unsynchronized und optimiert für performance
	// und ist das nicht-thread-safe Äquivalent zu Stack
	private Deque<BufferedImage> previousImages = new ArrayDeque<>();
	private Deque<BufferedImage> nextImages = new ArrayDeque<>();

	private BufferedImage originalImage;
	private ObjectProperty<BufferedImage> currentImageProperty = new SimpleObjectProperty<>();

	private BorderPane root;
	private HBox boxMenu;
	private StackPane stackPaneImages;

	private Button btnUndo;
	private Button btnRedo;
	private ToggleButton btnShowOriginalImage;

	private ScrollPane scrollCurrentImage;
	private ImageView viewOriginalImage;
	private ImageView viewCurrentImage;

	final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

	public ImageTab(File selectedFile) throws IOException {
		this.selectedFile = Objects.requireNonNull(selectedFile);

		createWidgets();
		setupInteractions();
		addWidgets();

		loadAndUpdateImages();
	}

	private void loadAndUpdateImages() throws IOException {
		originalImage = ImageIO.read(selectedFile);
		currentImageProperty.set(originalImage);

		viewOriginalImage.setImage(SwingFXUtils.toFXImage(originalImage, null));
	}

	private void createWidgets() {
		setText(selectedFile.getName());

		root = new BorderPane();
		boxMenu = new HBox(5);
		stackPaneImages = new StackPane();

		btnUndo = new Button("Undo");
		btnRedo = new Button("Redo");
		btnShowOriginalImage = new ToggleButton("Show Original Image");

		scrollCurrentImage = new ZoomableScrollPane(stackPaneImages);

		viewOriginalImage = new ImageView();
		viewOriginalImage.preserveRatioProperty().set(true);
		viewOriginalImage.visibleProperty().bind(btnShowOriginalImage.selectedProperty());

		viewCurrentImage = new ImageView();
		viewCurrentImage.preserveRatioProperty().set(true);
		currentImageProperty.addListener((observable, oldValue, newValue) -> viewCurrentImage
				.setImage(SwingFXUtils.toFXImage(newValue, null)));

	}

	private void setupInteractions() {
		btnUndo.setOnAction(e -> undo());
		btnRedo.setOnAction(e -> redo());
	}

	private void addWidgets() {
		root.setTop(boxMenu);
		root.setCenter(scrollCurrentImage);

		boxMenu.getChildren().addAll(btnUndo, btnRedo, btnShowOriginalImage);
		stackPaneImages.getChildren().addAll(viewCurrentImage, viewOriginalImage);

		setContent(root);
	}

	public void undo() {
		if (previousImages.isEmpty()) {
			return;
		}

		nextImages.push(currentImageProperty.get());
		currentImageProperty.set(previousImages.pop());
	}

	public void redo() {
		if (nextImages.isEmpty()) {
			return;
		}

		previousImages.push(currentImageProperty.get());
		currentImageProperty.set(nextImages.pop());

	}

	public void performImageOperation(Filter filter) {
		BufferedImage newImage = filter.perform(currentImageProperty.get());
		if (newImage != null) {
			previousImages.push(currentImageProperty.get());
			currentImageProperty.set(newImage);
		}
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public BufferedImage getCurrentImage() {
		return currentImageProperty.get();
	}

}
