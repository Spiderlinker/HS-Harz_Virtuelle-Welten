package de.hsharz.images;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * ImageInfo stellt verschiedene Methoden zur Untersuchung und zum Holen von Informationen eines Bildes bereit.
 * Diese Klasse benötigt ein Image, welches über den Konstruktor gesetzt wird.
 *
 * Von diesem Bild können nun verschiedene Grauwert-Informationen (min, max, med) geholt werden,
 * die Varianz und Standardabweichung der Grauwerte erfragt werden und die Entropie des Bildes.
 *
 * Die meisten dieser Operationen werden nur einmalig ausgeführt, damit Rechenkapazitäten gespart werden.
 * Einmal berechnete Werte werden dann in Instanzvariablen abgespeichert und bei erneuter Abfrage geliefert.
 *
 * @author Oliver Lindemann,
 * u33873@hs-harz.de,
 * Matr.Nr.: 26264
 */
public class ImageInfo {

    /** Minimaler Grauwert */
    public static final int MIN_GRAY_VALUE     = 0;
    /** Maximaler Grauwert */
    public static final int MAX_GRAY_VALUE     = 255;

    /** Wert, falls ein Wert noch nicht berechnet worden ist */
    public static final int NOT_CALCULATED     = -1;

    // ----- Instanzvariablen -----

    /** Gegebenes Bild, dessen Werte untersucht werden soll */
    private BufferedImage   image;
    /** Anzahl der Pixel im gegebenen Bild */
    private int             pixelCount         = NOT_CALCULATED;

    // ----- Grauwert-Informationen -----

    /** Minimaler Grauwert des gegebenen Bildes */
    private int             minGrayValue       = NOT_CALCULATED;
    /** Maximaler Grauwert des gegebenen Bildes */
    private int             maxGrayValue       = NOT_CALCULATED;
    /** Mittlerer Grauwert des gegebenen Bildes */
    private int             midGrayValue       = NOT_CALCULATED;

    // Verschiedene Informationen über die Verteilung der Grauwerte
    /** Varianz der Grauwerte */
    private double          varianz            = NOT_CALCULATED;
    /** Standardabweichung der Grauwerte */
    private double          standardabweichung = NOT_CALCULATED;
    /** Entropie der Grauwerte */
    private double          entropy            = NOT_CALCULATED;
    // Beinhaltet die Anzahl der einzelnen Grauwerte
    // index = Grauwert, Value = Count
    private int[]           grayValueCount     = null;

    /**
     * Ergeugt eine neue Instanz von ImageInfo mit dem gegebenen Bild.
     * Das gegebene Bild kann über verschiedene Methoden untersucht und
     * verschiende Werte abgerufen werden
     *
     * @param image Bild, dessen Werte untersucht und abgerufen werden sollen
     */
    public ImageInfo(final BufferedImage image) {
        this.image = Objects.requireNonNull(image);
    }

    /**
     * Liefert die Anzahl der Pixel dieses Bildes.
     *
     * @return Anzahl der Pixel des Bildes
     */
    public int getPixelCount() {
        // Falls die Anzahl der Pixel noch nicht berechnet worden ist,
        // wird diese Berechnung nun ausgeführt. Andernfalls der zuvor
        // berechnete Wert 'pixelCount' zurückgegeben
        if (this.pixelCount == NOT_CALCULATED) {
            this.pixelCount = this.image.getWidth() * this.image.getHeight();
        }
        return this.pixelCount;
    }

    /**
     * Mittleren Grauwert des Bildes berechnen.
     *
     * @return Liefert den mittleren Grauwert des Bildes
     */
    public int getMidGrayValue() {
        // Mittleren Grauwert nur 1x berechnen
        if (this.midGrayValue == NOT_CALCULATED) {
            int sum = 0; // Summe aller Grauwerte

            // Berechnung der Summe aller Grauwerte
            for (int x = 0; x < this.image.getWidth(); x++) {
                for (int y = 0; y < this.image.getHeight(); y++) {
                    // Grauwert von Pixel an der Stelle (x,y) holen
                    // und zur Summe addieren
                    sum += this.getGrayOfPixel(x, y);
                }
            }
            // Mittleren Grauwert bestimmen mit (Summe / Anzahl Pixel)
            // Mittleren Grauwert abspeichern
            this.midGrayValue = sum / this.getPixelCount();
        }
        // zuvor berechneten mittleren Grauwert liefern
        return this.midGrayValue;
    }

    /**
     * Liefert den Minimalen Grauwert des Bildes
     * (min. {@value #MIN_GRAY_VALUE}, max. {@value #MAX_GRAY_VALUE})
     *
     * @return minimaler Grauwert des Bildes
     */
    public int getMinGrayValue() {
        if (this.minGrayValue == NOT_CALCULATED) {
            // Minimalen Grauwert initial auf den maximalen Grauwert setzen,
            // damit ein (kleinerer) minimaler Grauwert gefunden werden kann
            int min = MAX_GRAY_VALUE;
            for (int x = 0; x < this.image.getWidth(); x++) {
                for (int y = 0; y < this.image.getHeight(); y++) {
                    // Grauwert des Pixels an der Position x,y holen
                    int grayValue = this.getGrayOfPixel(x, y);
                    // Prüfen, ob aktueller Grauwert des Pixels kleiner ist,
                    // als der aktuell gefundene minimale Grauwert
                    if (grayValue < min) {
                        min = grayValue;
                    }
                }
            }
            this.minGrayValue = min;
        }
        return this.minGrayValue;
    }

    /**
     * Liefert den Maximalen Grauwert des Bildes
     * (min. {@value #MIN_GRAY_VALUE}, max. {@value #MAX_GRAY_VALUE})
     *
     * @return maximaler Grauwert des Bildes
     */
    public int getMaxGrayValue() {
        // Maximalen Grauwert nur 1x berechnen
        if (this.maxGrayValue == NOT_CALCULATED) {
            // Maximalen Grauwert initial auf den minimalen Grauwert setzen,
            // damit ein (höherer) maximaler Grauwert gefunden werden kann
            int max = MIN_GRAY_VALUE;
            for (int x = 0; x < this.image.getWidth(); x++) {
                for (int y = 0; y < this.image.getHeight(); y++) {
                    // Grauwert des Pixels an der Position x,y holen
                    int grayValue = this.getGrayOfPixel(x, y);
                    // Prüfen, ob aktueller Grauwert des Pixels größer ist,
                    // als der aktuell gefundene maximale Grauwert
                    if (grayValue > max) {
                        max = grayValue;
                    }
                }
            }
            // Gefundenen Grauwert abspeichern
            this.maxGrayValue = max;
        }
        // zuvor berechneten maximalen Grauwert zurückgeben
        return this.maxGrayValue;
    }

    /**
     * Liefert die Varianz der Grauwerte des Bildes
     *
     * @return Varianz der Grauwerte des Bildes
     */
    public double getVarianz() {
        // Varianz nur 1x berechnen
        if (this.varianz == NOT_CALCULATED) {
            int mid = this.getMidGrayValue();
            long sum = 0;
            for (int x = 0; x < this.image.getWidth(); x++) {
                for (int y = 0; y < this.image.getHeight(); y++) {
                    // Formel für Berechnung der Varianz anwenden
                    // (Einzelner Grauwert - Mittlerer Grauwert)^2
                    sum += Math.pow(this.getGrayOfPixel(x, y) - mid, 2);
                }
            }
            // Varianz berechnen und in Instanzvariable abspeichern
            // Summe durch Anzahl der Pixel - 1
            this.varianz = (double) sum / (this.getPixelCount() - 1);
        }
        return this.varianz;
    }

    /**
     * Liefert die Standardabweichung der Grauwerte des Bildes
     *
     * @return Standardabweichung der Grauwerte des Bildes
     */
    public double getStandardabweichung() {
        if (this.standardabweichung == NOT_CALCULATED) {
            // Standardabweichung ist die Wurzel aus der Varianz
            this.standardabweichung = Math.sqrt(this.getVarianz());
        }
        return this.standardabweichung;
    }

    /**
     * Berechnet und liefert die Entropie dieses Bildes
     *
     * @return Entropie dieses Bildes
     */
    public double getEntropy() {
        // Entropie nur 1x berechnen
        if (this.entropy == NOT_CALCULATED) {
            // Entropie ermittelt sich aus:
            // - Summe von(p * log2(p))
            double entropy = 0.0;
            for (int value : this.getGrayValueCount()) {
                // Prüfen, ob es diesen Grauwert überhaupt gib, sonst ignoriere diesen Grauwert
                // Wenn Anzahl des Grauwertes > 0, dann berechne damit die Entropie
                if (value > 0) {
                    // Wahrscheinlichkeit dieses Grauwertes bestimmen
                    // Anzahl der Pixel mit diesem Grauwert / Anzahl der Pixel
                    double p = (double) value / this.getPixelCount();
                    // p * log2(p)
                    entropy -= p * ImageInfo.log2(p);
                }
            }
            // Berechnete Entropie abspeichern
            this.entropy = entropy;
        }
        return this.entropy;
    }

    /**
     * Berechnet die Häufigkeit / Anzahl jedes einzelnen Grauwertes im Bild.
     * Liefert ein Array mit einer Größe von {@value #maxGrayValue} + 1.
     * Der Index gibt den Grauwert an und der darin enthaltene Wert gibt
     * die Häufigkeit dieses Grauwertes an.
     *
     * @return Anzahl der Grauwerte im Bild
     */
    public int[] getGrayValueCount() {
        if (this.grayValueCount == null) {
            // Array bildet alle Grauwerte und ihre Häufigkeit ab
            // Der Index ist der Grauwert und die darin enthaltende
            // Zahl ist die Anzahl des Grauwertes
            int[] grayCount = new int[MAX_GRAY_VALUE + 1];
            for (int x = 0; x < this.image.getWidth(); x++) {
                for (int y = 0; y < this.image.getHeight(); y++) {
                    // Grauwert des Pixels bei x, y ermitteln
                    int grayValue = this.getGrayOfPixel(x, y);
                    // Anzahl des Grauwertes inkrementieren
                    grayCount[grayValue]++;
                }
            }
            // Berechnete Anzahl von Grauwerten speichern
            this.grayValueCount = grayCount;
        }
        return this.grayValueCount;
    }

    /**
     * Liefert den Grauwert des Pixels an der angegebenen Stelle (x, y).
     *
     * @param x x-Koordinate des Pixels
     * @param y y-Koordinate des Pixels
     * @return Grauwert des angegebenen Pixels
     */
    private int getGrayOfPixel(final int x, final int y) {
        return this.getGrayOfPixel(this.image.getRGB(x, y));
    }

    /**
     * Liefert den Grauwert des (in RGB-Form) übergebenen Pixels
     *
     * @param rgb Pixel in RGB-Form, dessen Grauwert berechnet werden soll
     * @return Grauwert des angegebenen Pixels
     */
    private int getGrayOfPixel(final int rgb) {

        // Pixel ist aufgebaut: (int = 4Byte = 32Bit)

        // Meta --- Red ---- Green -- Blue
        // 00000000 00000000 00000000 00000000
        // --
        // rgb >> 16 & 0xFF
        // Rot wird nach 'ganz rechts geschoben'
        // und dann mit Maske 0xFF (nur die letzten 8 Bit übernehmen)
        // UND-Verknüpft, damit bleiben nur die Rotwerte übrig

        int r = (rgb >> 16) & 0xFF; // Rot extrahieren
        int g = (rgb >> 8) & 0xFF; // Grün extrahieren
        int b = rgb & 0xFF; // Blau extrahieren

        // Grauwert berechnen
        // Rot + Grün + Blau / Anzahl an Werten (3)
        return (r + g + b) / 3;
    }

    // ----- Hilfsmethoden -----

    /**
     * Berechnet den Logarithmus zur Basis 2 von dem gegebenen Wert {@code value}.
     *
     * @param value Wert von dem der Logarithmus zur Basis 2 berechnet werden soll
     * @return Logarithmus zur Basis 2 von dem gegebenen Wert {@code value}
     */
    public static double log2(final double value) {
        // loga b = ln b / ln a
        return Math.log(value) / Math.log(2);
    }

}
