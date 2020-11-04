package de.hsharz.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import de.hsharz.images.ImageInfo.ImageColor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Diese Klasse ist die UI-Klasse. Diese stellt ein User-Interface dem Benutzer
 * dar, über das er ein neues Bild laden kann. Dieses Bild wird analysiert und
 * verschiedenste Informationen über Grauwerte dem Benutzer angezeigt.
 *
 * @author Oliver Lindemann u33873@hs-harz.de Matr.Nr.: 26264
 */
public class ImageInfoView {

	// VBox beinhaltet alle Komponenten untereinander
	private VBox root;

	// Anzeige von Informationen des geladenen Bildes
	private TextArea fldInformation;

	private TabPane tabCharts;
	private Tab tabGray;
	private Tab tabRed;
	private Tab tabGreen;
	private Tab tabBlue;

	// Chart beinhaltet Anzahl der einzelnen Grauwerte
	private LineChart<Number, Number> chartGrayValues;
	private LineChart<Number, Number> chartRedValues;
	private LineChart<Number, Number> chartGreenValues;
	private LineChart<Number, Number> chartBlueValues;

	private File imageFile;
	private BufferedImage loadedImage;

	public ImageInfoView(File imageFile) throws IOException {
		this(imageFile, null);
	}

	public ImageInfoView(File imageFile, BufferedImage image) throws IOException {
		this.imageFile = Objects.requireNonNull(imageFile);
		if (image == null) {
			loadedImage = ImageIO.read(imageFile);
		} else {
			loadedImage = image;
		}

		this.createWidgets();
		this.addWidgets();

		updateImageInformation();
	}

	private void createWidgets() {
		this.root = new VBox(10);

		tabGray = new Tab("Grauwerte");
		tabRed = new Tab("Rotwerte");
		tabGreen = new Tab("Grünwerte");
		tabBlue = new Tab("Blauwerte");
		tabCharts = new TabPane(tabGray, tabRed, tabGreen, tabBlue);

		this.fldInformation = new TextArea("Bildinformationen...");
		this.fldInformation.setPrefHeight(300);
		this.fldInformation.setEditable(false);
		this.fldInformation.setWrapText(true);

		chartGrayValues = createChart("Grauwerte");
		chartRedValues = createChart("Rotwerte");
		chartGreenValues = createChart("Grünwerte");
		chartBlueValues = createChart("Blauwerte");

		tabGray.setContent(chartGrayValues);
		tabRed.setContent(chartRedValues);
		tabGreen.setContent(chartGreenValues);
		tabBlue.setContent(chartBlueValues);
	}

	private LineChart<Number, Number> createChart(String title) {
		NumberAxis xAxis = new NumberAxis("Grauwerte", 0, 255, 10);
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Anzahl der Grauwerte");
		LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
		chart.setCreateSymbols(false);
		return chart;
	}

	/*
	 * Bild laden, Informationen auslesen und darstellen
	 */
	private void updateImageInformation() {
		try {
			// Bild laden und TextArea + Chart aktualisieren
			ImageInfo info = new ImageInfo(loadedImage);

			this.updateImageInformation(imageFile, info);
			this.updateChartInformation(info);

		} catch (NullPointerException e) {
			this.fldInformation.setText("Fehler beim Lesen der Datei: " + e.getMessage());
			e.printStackTrace();
		}

	}

	// Informationen in TextArea darstellen
	private void updateImageInformation(final File file, final ImageInfo info) {
		StringBuilder builder = new StringBuilder();

		builder.append("Pfad: " + file.getAbsolutePath()).append("\n");
		builder.append("Anzahl Pixel: " + info.getPixelCount()).append("\n");
		builder.append("Minimaler Grauwert: " + info.getMinGrayValue()).append("\n");
		builder.append("Maximaler Grauwert: " + info.getMaxGrayValue()).append("\n");
		builder.append("Mittlerer Grauwert: " + info.getMidGrayValue()).append("\n");
		builder.append("Varianz: " + info.getVarianz()).append("\n");
		builder.append("Standardabweichung: " + info.getStandardabweichung()).append("\n");
		builder.append("Entropie: " + info.getEntropy()).append("\n");

		this.fldInformation.setText(builder.toString());
	}

	// Chart updaten mit neuen Bildinformationen
	private void updateChartInformation(final ImageInfo info) {

		this.chartGrayValues.getData().clear();
		this.chartGrayValues.getData().add(createSeries(ImageColor.GRAY, info));

		this.chartRedValues.getData().clear();
		this.chartRedValues.getData().add(createSeries(ImageColor.RED, info));

		this.chartGreenValues.getData().clear();
		this.chartGreenValues.getData().add(createSeries(ImageColor.GREEN, info));

		this.chartBlueValues.getData().clear();
		this.chartBlueValues.getData().add(createSeries(ImageColor.BLUE, info));
	}

	private Series<Number, Number> createSeries(ImageColor color, final ImageInfo info) {
		Series<Number, Number> series = new XYChart.Series<>();
		series.setName(color.toString());
		for (int i = 0; i < info.getColorValueCount(color).length; i++) {
			int count = info.getColorValueCount(color)[i];
			series.getData().add(new XYChart.Data<>(i, 0));
			series.getData().add(new XYChart.Data<>(i, count));
			series.getData().add(new XYChart.Data<>(i, 0));
		}
		return series;
	}

	private void addWidgets() {
		this.root.getChildren().addAll(this.fldInformation, this.tabCharts);
	}

	public Pane getPane() {
		return this.root;
	}

}
