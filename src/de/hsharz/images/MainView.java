package de.hsharz.images;

import java.io.File;
import java.io.IOException;

import de.hsharz.images.filter.BinaryFilter;
import de.hsharz.images.filter.BinaryFilterPane;
import de.hsharz.images.utils.PopupWindow;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainView {

	private BorderPane root;
	private TabPane tabPane;

	private MenuBar menuBar;
	private Menu menuFile;
	private Menu menuStatistics;
	private Menu menuFilter;
	private Menu menuLowPass;
	private Menu menuHighPass;

	private MenuItem itemLoadFile;
	private MenuItem itemShowStatistics;
	private MenuItem itemGauss;
	private MenuItem itemMedian;
	private MenuItem itemRectangle;
	private MenuItem itemGrayImage;
	private MenuItem itemBinaryImage;

	public MainView() {
		createWidgets();
		setupInteractions();
		addWidgets();
	}

	private void createWidgets() {
		root = new BorderPane();
		tabPane = new TabPane();

		menuBar = new MenuBar();
		menuFile = new Menu("Datei");
		itemLoadFile = new MenuItem("Neues Bild laden...");

		menuFile.getItems().add(itemLoadFile);

		menuStatistics = new Menu("Statistik");
		itemShowStatistics = new MenuItem("Statistik einblenden");
		menuStatistics.getItems().add(itemShowStatistics);

		menuFilter = new Menu("Filter");
		menuLowPass = new Menu("Tiefpassfilter");
		menuHighPass = new Menu("Hochpassfilter");
		itemGrayImage = new MenuItem("Schwarz/Weiß");
		itemBinaryImage = new MenuItem("Binärbild");
		menuFilter.getItems().addAll(menuLowPass, menuHighPass, itemGrayImage, itemBinaryImage);

		itemGauss = new MenuItem("Gauss");
		itemMedian = new MenuItem("Median");
		itemRectangle = new MenuItem("Rechteck");
		menuLowPass.getItems().addAll(itemGauss, itemMedian, itemRectangle);

		menuBar.getMenus().addAll(menuFile, menuStatistics, menuFilter);
	}

	private void setupInteractions() {
		// Aktion bei Buttonklick -> Neues Bild laden
		this.itemLoadFile.setOnAction(e -> {

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Bild auswählen");
			fileChooser.getExtensionFilters().addAll( // Eigene ExtensionFilters hinzuf�gen
					new ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"), // Bildateien
					new ExtensionFilter("All Files", "*.*") // Alle Dateien
			);
			File selectedFile = fileChooser.showOpenDialog(null);
			// Nur Bild laden, falls auch eins ausgew�hlt wurde
			if (selectedFile != null) {
				try {
					createNewImageInfoView(selectedFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		itemShowStatistics.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			try {
				ImageInfoView imageInfo = new ImageInfoView(imageTab.getSelectedFile(),
						imageTab.getCurrentImage());
				new PopupWindow("Bildstatistik / Histogramm", imageInfo.getPane()).show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		itemGrayImage.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			imageTab.performFileOperation(f -> {
				ImageInfo info = new ImageInfo(imageTab.getCurrentImage());
				return info.getAsGrayImage();
			});

		});

		itemBinaryImage.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			BinaryFilterPane binaryFilterPane = new BinaryFilterPane(imageTab.getCurrentImage());
			PopupWindow popup = new PopupWindow("Binärbild erstellen", binaryFilterPane.getPane());
			binaryFilterPane.getButtonChoose().setOnAction(e2 -> {
				popup.close();
				int selectedValue = binaryFilterPane.getSelectedValue();
				imageTab.performImageOperation(
						image -> new BinaryFilter(selectedValue).perform(image));
			});
			popup.showAndWait();

		});
	}

	private ImageTab getSelectedTab() {
		Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
		if (selectedTab instanceof ImageTab) {
			return (ImageTab) selectedTab;
		}
		return null;
	}

	private void createNewImageInfoView(File imageFile) throws IOException {
		ImageTab tab = new ImageTab(imageFile);
//		ImageInfoView view = new ImageInfoView(imageFile);
//		tab.setContent(view.getPane());
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().selectLast();
	}

	private void addWidgets() {
		root.setTop(menuBar);
		root.setCenter(tabPane);
	}

	public Pane getPane() {
		return root;
	}

}
