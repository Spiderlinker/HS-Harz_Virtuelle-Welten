package de.hsharz.images;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ImageInfoView {

    // VBox beinhaltet alle Komponenten untereinander
    private VBox                      root;

    // Anzeige des geladenen Bildes
    private ImageView                 imageView;
    // Button, um neues Bild zu laden
    private Button                    btnLoadImage;
    // Anzeige von Informationen des geladenen Bildes
    private TextArea                  fldInformation;

    // Chart beinhaltet Anzahl der einzelnen Grauwerte
    private LineChart<Number, Number> chartGrayValues;
    private NumberAxis                xAxis;
    private NumberAxis                yAxis;

    public ImageInfoView() {
        this.createWidgets();
        this.setupInteractions();
        this.addWidgets();
    }

    private void createWidgets() {
        this.root = new VBox(20);
        this.root.setPadding(new Insets(10));

        this.imageView = new ImageView();
        this.imageView.setFitHeight(300);
        this.imageView.setPreserveRatio(true);

        this.btnLoadImage = new Button("Bild laden...");

        this.fldInformation = new TextArea("Bildinformationen...");
        this.fldInformation.setPrefHeight(300);
        this.fldInformation.setEditable(true);
        this.fldInformation.setWrapText(true);

        this.xAxis = new NumberAxis("Grauwert", 0, 255, 10);
        this.yAxis = new NumberAxis();
        this.yAxis.setLabel("Anzahl der Grauwerte");
        this.chartGrayValues = new LineChart<>(this.xAxis, this.yAxis);
    }

    private void setupInteractions() {
        // Aktion bei Buttonklick -> Neues Bild laden
        this.btnLoadImage.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Bild auswählen");
            fileChooser.getExtensionFilters().addAll( // Eigene ExtensionFilters hinzufügen
                    new ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"), // Bildateien
                    new ExtensionFilter("All Files", "*.*") // Alle Dateien
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            // Nur Bild laden, falls auch eins ausgewählt wurde
            if (selectedFile != null) {
                // Bildinformationen aktualisieren
                this.updateImageInformation(selectedFile);
            }
        });
    }

    /*
     * Bild laden, Informationen auslesen und darstellen
     */
    private void updateImageInformation(final File newImageFile) {
        this.imageView.setImage(new Image(newImageFile.toURI().toString()));

        try {
            // Bild laden und TextArea + Chart aktualisieren
            ImageInfo info = new ImageInfo(ImageIO.read(newImageFile));

            this.updateImageInformation(newImageFile, info);
            this.updateChartInformation(info);

        } catch (IOException | NullPointerException e) {
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
        series1.setName("Bild");

        for (int i = 0; i < info.getGrayValueCount().length; i++) {
            int count = info.getGrayValueCount()[i];
            series1.getData().add(new XYChart.Data<>(i, count));
        }
        this.chartGrayValues.getData().clear();
        this.chartGrayValues.getData().add(series1);
    }

    private void addWidgets() {
        this.root.getChildren().addAll(this.imageView, this.btnLoadImage, this.fldInformation, this.chartGrayValues);
    }

    public Pane getPane() {
        return this.root;
    }

}
