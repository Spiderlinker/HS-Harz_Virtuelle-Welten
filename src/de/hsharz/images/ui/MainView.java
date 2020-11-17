package de.hsharz.images.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.hsharz.images.filter.grayvaluetransformation.BinaryFilter;
import de.hsharz.images.filter.grayvaluetransformation.DynamicFilter;
import de.hsharz.images.filter.grayvaluetransformation.GrayScaleFilter;
import de.hsharz.images.filter.grayvaluetransformation.HistogramEqualization;
import de.hsharz.images.filter.highpass.Laplace4Filter;
import de.hsharz.images.filter.highpass.Laplace8Filter;
import de.hsharz.images.filter.highpass.SobelFilter;
import de.hsharz.images.filter.lowpass.GaussFilter;
import de.hsharz.images.filter.lowpass.RectangularFilter;
import de.hsharz.images.ui.ImageInfo.ImageColor;
import de.hsharz.images.ui.filter.BinaryFilterPane;
import de.hsharz.images.ui.filter.HistogramDynamicPane;
import de.hsharz.images.ui.filter.HistogramEqualizationPane;
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
	private Menu menuColors;
	private Menu menuFilter;
	private Menu menuLowPass;
	private Menu menuHighPass;

	private MenuItem itemLoadFile;
	private MenuItem itemSaveFileAs;
	private MenuItem itemShowStatistics;

	private MenuItem itemGauss;
//	private MenuItem itemMedian;
	private MenuItem itemRectangle;

	private MenuItem itemLaplace4;
	private MenuItem itemLaplace8;
	private MenuItem itemSobel;

	private MenuItem itemGrayImage;
	private MenuItem itemBinaryImage;
	private MenuItem itemGrayScaleTransformation;
//	private MenuItem itemDynamicColorHistogram;
	private MenuItem itemHistogramEqualization;

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
		itemSaveFileAs = new MenuItem("Bild speichern unter...");

		menuFile.getItems().addAll(itemLoadFile, itemSaveFileAs);

		menuStatistics = new Menu("Statistik");
		itemShowStatistics = new MenuItem("Statistik einblenden");
		menuStatistics.getItems().add(itemShowStatistics);

		menuColors = new Menu("Farben");
		itemGrayImage = new MenuItem("Graustufen");
		itemBinaryImage = new MenuItem("Binärbild");
		itemGrayScaleTransformation = new MenuItem("Grauwerttransformation");
		itemHistogramEqualization = new MenuItem("Histogrammausgleich");
		menuColors.getItems().addAll(itemHistogramEqualization, itemGrayScaleTransformation, itemGrayImage,
				itemBinaryImage);

		menuFilter = new Menu("Filter");
		menuLowPass = new Menu("Tiefpassfilter");
		menuHighPass = new Menu("Hochpassfilter");
		menuFilter.getItems().addAll(menuLowPass, menuHighPass);

		itemGauss = new MenuItem("Gauss");
//		itemMedian = new MenuItem("Median");
		itemRectangle = new MenuItem("Rechteck");
		menuLowPass.getItems().addAll(itemGauss, itemRectangle);

		itemLaplace4 = new MenuItem("Laplace 4er");
		itemLaplace8 = new MenuItem("Laplace 8er");

		itemSobel = new MenuItem("Sobelfilter");
		menuHighPass.getItems().addAll(itemLaplace4, itemLaplace8, itemSobel);

		menuBar.getMenus().addAll(menuFile, menuStatistics, menuColors, menuFilter);
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

		itemSaveFileAs.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Datei auswählen");
			fileChooser.getExtensionFilters().addAll( // Eigene ExtensionFilters hinzufügen
					new ExtensionFilter("JPG", "*.jpg", "*.jpeg"), // Bildateien
					new ExtensionFilter("PNG", "*.png"), // Bildateien
					new ExtensionFilter("BMP", "*.bmp"), // Bildateien
					new ExtensionFilter("All Files", "*.*") // Alle Dateien
			);
			File selectedFile = fileChooser.showSaveDialog(null);
			// Nur Bild speichern, falls auch eine Datei ausgewählt wurde
			System.out.println(selectedFile);
			if (selectedFile != null) {
				try {
					String selectedExtension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
					saveImageToFile(imageTab.getCurrentImage().getImage(), selectedExtension, selectedFile);
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
				ImageInfoView imageInfo = new ImageInfoView(imageTab.getCurrentImage());
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

			imageTab.applyFilter(new GrayScaleFilter());
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
				imageTab.applyFilter(new BinaryFilter(selectedValue));
			});
			popup.showAndWait();
		});

		itemGrayScaleTransformation.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			HistogramDynamicPane dynamicFilterPane = new HistogramDynamicPane(imageTab.getCurrentImage());
			PopupWindow popup = new PopupWindow("Binärbild erstellen", dynamicFilterPane.getPane());
			dynamicFilterPane.getButtonChoose().setOnAction(e2 -> {
				popup.close();
				int selectedValue = dynamicFilterPane.getSelectedValue();
				imageTab.applyFilter(new DynamicFilter(selectedValue));
			});
			popup.showAndWait();
		});

		itemRectangle.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			imageTab.applyFilter(new RectangularFilter());
		});

		itemGauss.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			imageTab.applyFilter(new GaussFilter());
		});

		itemLaplace4.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			imageTab.applyFilter(new Laplace4Filter());
		});

		itemLaplace8.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			imageTab.applyFilter(new Laplace8Filter());
		});

		itemSobel.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			imageTab.applyFilter(new SobelFilter());
		});

		itemHistogramEqualization.setOnAction(e -> {
			ImageTab imageTab = getSelectedTab();
			if (imageTab == null) {
				return;
			}

			HistogramEqualizationPane histogramEqualizationPane = new HistogramEqualizationPane(
					imageTab.getCurrentImage());
			PopupWindow popup = new PopupWindow("Histogram Equalization", histogramEqualizationPane.getPane());
			histogramEqualizationPane.getButtonChoose().setOnAction(e2 -> {
				popup.close();
				ImageColor selectedValue = histogramEqualizationPane.getSelectedValue();
				imageTab.applyFilter(new HistogramEqualization(selectedValue));
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
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().selectLast();
	}

	private void saveImageToFile(BufferedImage image, String extension, File saveTo) throws IOException {
		System.out.println("Saving to file: " + saveTo + " with extension: " + extension);
		ImageIO.write(image, extension.replace("*.", ""), saveTo);
	}

	private void addWidgets() {
		root.setTop(menuBar);
		root.setCenter(tabPane);
	}

	public Pane getPane() {
		return root;
	}

}
