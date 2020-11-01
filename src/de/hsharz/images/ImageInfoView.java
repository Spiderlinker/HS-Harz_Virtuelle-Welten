package de.hsharz.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
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

	// Chart beinhaltet Anzahl der einzelnen Grauwerte
	private LineChart<Number, Number> chartGrayValues;

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

		this.fldInformation = new TextArea("Bildinformationen...");
		this.fldInformation.setPrefHeight(300);
		this.fldInformation.setEditable(false);
		this.fldInformation.setWrapText(true);

		NumberAxis xAxis = new NumberAxis("Grauwert", 0, 255, 10);
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Anzahl der Grauwerte");
		this.chartGrayValues = new LineChart<>(xAxis, yAxis);
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
		Series<Number, Number> series1 = new XYChart.Series<>();
		series1.setName(imageFile.getName());

		for (int i = 0; i < info.getGrayValueCount().length; i++) {
			int count = info.getGrayValueCount()[i];
			series1.getData().add(new XYChart.Data<>(i, count));
		}
		this.chartGrayValues.getData().clear();
		this.chartGrayValues.getData().add(series1);
	}

	private void addWidgets() {
		this.root.getChildren().addAll(this.fldInformation, this.chartGrayValues);
	}

	public Pane getPane() {
		return this.root;
	}

}
