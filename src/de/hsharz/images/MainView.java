package de.hsharz.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
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
		menuFilter.getItems().addAll(menuLowPass, menuHighPass, itemGrayImage);

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
				} catch (FileNotFoundException e1) {
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
				new PopupWindow("Bildstatistik / Histogramm", new ScrollPane(imageInfo.getPane()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		itemGrayImage.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			imageTab.performOperation(f -> {
				try {
					ImageInfo info = new ImageInfo(ImageIO.read(imageTab.getSelectedFile()));
					BufferedImage bufferedGrayImage = info.getAsGrayImage();
					return SwingFXUtils.toFXImage(bufferedGrayImage, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			});

		});
	}

	private ImageTab getSelectedTab() {
		Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
		if (selectedTab instanceof ImageTab) {
			return (ImageTab) selectedTab;
		}
		return null;
	}

	private void createNewImageInfoView(File imageFile) throws FileNotFoundException {
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
