package de.hsharz.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.gillius.jfxutils.chart.StableTicksAxis;

import de.hsharz.images.ImageInfo.ImageColor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
	private BorderPane root;
	private Region chartPane;

	private VBox boxButtons;
	private RadioButton btnGray;
	private RadioButton btnRed;
	private RadioButton btnGreen;
	private RadioButton btnBlue;

	private GridPane gridInfos;
	private Label lblPath;
	private Label lblPixelAmount;
	private Label lblMidValue;
	private Label lblMaxValue;
	private Label lblMinValue;
	private Label lblVarianz;
	private Label lblStandardDeviation;
	private Label lblEntropy;

	// Chart beinhaltet Anzahl der einzelnen Grauwerte
	private LineChart<Number, Number> chart;

	private File imageFile;
	private BufferedImage loadedImage;

	private ImageInfo info;

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

		info = new ImageInfo(loadedImage);

		this.createWidgets();
		this.setupInteractions();
		this.addWidgets();

		this.updateChartInformation(info);
		this.btnGray.fire();
	}

	private void createWidgets() {
		this.root = new BorderPane();
		this.root.setPadding(new Insets(20));

		StableTicksAxis xAxis = new StableTicksAxis(-5, 255);
		StableTicksAxis yAxis = new StableTicksAxis();
		yAxis.setLabel("Anzahl der " + "Anzahl der Farbwerte");
		chart = new LineChart<>(xAxis, yAxis);
		chart.setCreateSymbols(false);
		chart.setLegendVisible(false);

		setupChartInteractions();

		boxButtons = new VBox(20);
		boxButtons.setMaxWidth(Double.MAX_VALUE);
		boxButtons.setAlignment(Pos.CENTER_LEFT);

		btnGray = new RadioButton("Grau");
		btnRed = new RadioButton("Rot");
		btnGreen = new RadioButton("Grün");
		btnBlue = new RadioButton("Blau");

		ToggleGroup group = new ToggleGroup();
		group.getToggles().add(btnGray);
		group.getToggles().add(btnRed);
		group.getToggles().add(btnGreen);
		group.getToggles().add(btnBlue);

		gridInfos = new GridPane();
		gridInfos.setHgap(10);
		gridInfos.setVgap(10);

		lblPath = new Label(imageFile.getAbsolutePath());
		lblPixelAmount = new Label(String.valueOf(info.getPixelCount()));
		lblMidValue = new Label("N/A");
		lblMaxValue = new Label("N/A");
		lblMinValue = new Label("N/A");
		lblVarianz = new Label("N/A");
		lblStandardDeviation = new Label("N/A");
		lblEntropy = new Label("N/A");
	}

	private void setupChartInteractions() {
		// Zooming works only via primary mouse button without ctrl held down
		chartPane = JFXChartUtil.setupZooming(chart, mouseEvent -> {
			if (mouseEvent.getButton() != MouseButton.PRIMARY || mouseEvent.isShortcutDown())
				mouseEvent.consume();
		});

		ChartPanManager panner = new ChartPanManager(chart);
		panner.setMouseFilter(mouseEvent -> {
			if (mouseEvent.getButton() == MouseButton.SECONDARY
					|| (mouseEvent.getButton() == MouseButton.PRIMARY
							&& mouseEvent.isShortcutDown())) {
				// let it through
			} else {
				mouseEvent.consume();
			}
		});
		panner.start();
		JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(chart);
	}

	// Informationen in TextArea darstellen
	private void updateImageInformation(ImageColor color) {
		lblMinValue.setText(String.valueOf(info.getMinValue(color)));
		lblMaxValue.setText(String.valueOf(info.getMaxValue(color)));
		lblMidValue.setText(String.valueOf(info.getMidValue(color)));
		lblVarianz.setText(String.valueOf(info.getVarianz(color)));
		lblStandardDeviation.setText(String.valueOf(info.getStandardabweichung(color)));
		lblEntropy.setText(String.valueOf(info.getEntropy(color)));
	}

	// Chart updaten mit neuen Bildinformationen
	private void updateChartInformation(final ImageInfo info) {

		this.chart.getData().clear();
		createSeries(ImageColor.GRAY, info);
		createSeries(ImageColor.GREEN, info);
		createSeries(ImageColor.RED, info);
		createSeries(ImageColor.BLUE, info);

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

		chart.getData().add(series);
		series.getNode().setStyle("-fx-stroke: " + color.toString() + ";");

		switch (color) {
		case GRAY:
			series.getNode().visibleProperty().bind(btnGray.selectedProperty());
			break;
		case RED:
			series.getNode().visibleProperty().bind(btnRed.selectedProperty());
			break;
		case GREEN:
			series.getNode().visibleProperty().bind(btnGreen.selectedProperty());
			break;
		case BLUE:
			series.getNode().visibleProperty().bind(btnBlue.selectedProperty());
			break;
		}

		return series;
	}

	private void setupInteractions() {
		btnGray.setOnAction(e -> updateImageInformation(ImageColor.GRAY));
		btnRed.setOnAction(e -> updateImageInformation(ImageColor.RED));
		btnGreen.setOnAction(e -> updateImageInformation(ImageColor.GREEN));
		btnBlue.setOnAction(e -> updateImageInformation(ImageColor.BLUE));
	}

	private void addWidgets() {
		this.boxButtons.getChildren().addAll(btnGray, btnRed, btnGreen, btnBlue);

		gridInfos.add(createBoldLabel("Pixelanzahl:"), 0, 0);
		gridInfos.add(lblPixelAmount, 1, 0);
		gridInfos.add(createBoldLabel("Pfad:"), 2, 0);
		gridInfos.add(lblPath, 3, 0, 3, 1);

		gridInfos.add(createBoldLabel("Min Value:"), 0, 1);
		gridInfos.add(lblMinValue, 1, 1);
		gridInfos.add(createBoldLabel("Max Value:"), 2, 1);
		gridInfos.add(lblMaxValue, 3, 1);
		gridInfos.add(createBoldLabel("Mid Value:"), 4, 1);
		gridInfos.add(lblMidValue, 5, 1);

		gridInfos.add(createBoldLabel("Varianz:"), 0, 2);
		gridInfos.add(lblVarianz, 1, 2);
		gridInfos.add(createBoldLabel("Standardabweichung:"), 2, 2);
		gridInfos.add(lblStandardDeviation, 3, 2);
		gridInfos.add(createBoldLabel("Entropy:"), 4, 2);
		gridInfos.add(lblEntropy, 5, 2);

		this.root.setRight(boxButtons);
		this.root.setCenter(chartPane);
		this.root.setBottom(gridInfos);

	}

	private Label createBoldLabel(String text) {
		Label lbl = new Label(text);
		lbl.setStyle("-fx-font-weight: bold");
		return lbl;
	}

	public Pane getPane() {
		return this.root;
	}

}
