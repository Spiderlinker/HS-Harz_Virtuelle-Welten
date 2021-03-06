package de.hsharz.images.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hsharz.images.filter.utils.ImageUtils;

/**
 * ImageInfo stellt verschiedene Methoden zur Untersuchung und zum Holen von
 * Informationen eines Bildes bereit. Diese Klasse ben�tigt ein Image, welches
 * über den Konstruktor gesetzt wird.
 *
 * Von diesem Bild können nun verschiedene Grauwert-Informationen (min, max,
 * med) geholt werden, die Varianz und Standardabweichung der Grauwerte erfragt
 * werden und die Entropie des Bildes.
 *
 * Die meisten dieser Operationen werden nur einmalig ausgeführt, damit
 * Rechenkapazitäten gespart werden. Einmal berechnete Werte werden dann in
 * Instanzvariablen abgespeichert und bei erneuter Abfrage geliefert.
 *
 * @author Oliver Lindemann, u33873@hs-harz.de, Matr.Nr.: 26264
 */
public class ImageInfo {

	/** Minimaler Grauwert */
	public static final int MIN_COLOR_VALUE = 0;
	/** Maximaler Grauwert */
	public static final int MAX_COLOR_VALUE = 255;

	/** Wert, falls ein Wert noch nicht berechnet worden ist */
	public static final int NOT_CALCULATED = -1;

	// ----- Instanzvariablen -----

	private File imageFile;
	/** Gegebenes Bild, dessen Werte untersucht werden soll */
	private BufferedImage image;
	/** Anzahl der Pixel im gegebenen Bild */
	private int pixelCount = NOT_CALCULATED;

	// ----- Grauwert-Informationen -----

	/*
	 * ImageColor.value.min BLUE.value.min = 12; ...
	 */
	private Map<String, Double> calculatedValues = new HashMap<>();
	private static final String MIN_VALUE = ".value.min";
	private static final String MAX_VALUE = ".value.max";
	private static final String MID_VALUE = ".value.mid";
	private static final String VARIANZ = ".varianz";
	private static final String STANDARD_DEVIATION = ".standard_deviation";
	private static final String ENTROPY = ".entropy";

	private Map<ImageColor, Integer[]> colorValueCounts = new EnumMap<>(ImageColor.class);

	/**
	 * Ergeugt eine neue Instanz von ImageInfo mit dem gegebenen Bild. Das gegebene
	 * Bild kann über verschiedene Methoden untersucht und verschiende Werte
	 * abgerufen werden
	 *
	 * @param image Bild, dessen Werte untersucht und abgerufen werden sollen
	 * @throws IOException
	 */
	public ImageInfo(final BufferedImage image) {
		this(null, image);
	}

	public ImageInfo(final File imageFile, final BufferedImage image) {
		this.imageFile = imageFile;
		this.image = Objects.requireNonNull(image);
	}

	public File getImageFile() {
		return imageFile;
	}

	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Liefert die Anzahl der Pixel dieses Bildes.
	 *
	 * @return Anzahl der Pixel des Bildes
	 */
	public int getPixelCount() {
		// Falls die Anzahl der Pixel noch nicht berechnet worden ist,
		// wird diese Berechnung nun ausgef�hrt. Andernfalls der zuvor
		// berechnete Wert 'pixelCount' zur�ckgegeben
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
	public double getMidValue(ImageColor color) {
		String key = color.toString() + MID_VALUE;
		// Mittleren Grauwert nur 1x berechnen
		if (this.calculatedValues.get(key) == null) {
			double sum = 0; // Summe aller Grauwerte

			// Berechnung der Summe aller Grauwerte
			for (int x = 0; x < this.image.getWidth(); x++) {
				for (int y = 0; y < this.image.getHeight(); y++) {
					// Grauwert von Pixel an der Stelle (x,y) holen
					// und zur Summe addieren
					sum += this.getColorValueOfPixel(color, x, y);
				}
			}
			// Mittleren Grauwert bestimmen mit (Summe / Anzahl Pixel)
			// Mittleren Grauwert abspeichern
			this.calculatedValues.put(key, sum / this.getPixelCount());
		}
		// zuvor berechneten mittleren Grauwert liefern
		return this.calculatedValues.get(key);
	}

	/**
	 * Liefert den Minimalen Grauwert des Bildes (min. {@value #MIN_COLOR_VALUE},
	 * max. {@value #MAX_COLOR_VALUE})
	 *
	 * @return minimaler Grauwert des Bildes
	 */
	public double getMinValue(ImageColor color) {
		String key = color.toString() + MIN_VALUE;
		if (this.calculatedValues.get(key) == null) {
			// Minimalen Grauwert initial auf den maximalen Grauwert setzen,
			// damit ein (kleinerer) minimaler Grauwert gefunden werden kann
			double min = MAX_COLOR_VALUE;
			for (int x = 0; x < this.image.getWidth(); x++) {
				for (int y = 0; y < this.image.getHeight(); y++) {
					// Grauwert des Pixels an der Position x,y holen
					int grayValue = this.getColorValueOfPixel(color, x, y);
					// Pr�fen, ob aktueller Grauwert des Pixels kleiner ist,
					// als der aktuell gefundene minimale Grauwert
					if (grayValue < min) {
						min = grayValue;
					}
				}
			}
			this.calculatedValues.put(key, min);
		}
		return this.calculatedValues.get(key);
	}

	/**
	 * Liefert den Maximalen Grauwert des Bildes (min. {@value #MIN_COLOR_VALUE},
	 * max. {@value #MAX_COLOR_VALUE})
	 *
	 * @return maximaler Grauwert des Bildes
	 */
	public double getMaxValue(ImageColor color) {
		String key = color.toString() + MAX_VALUE;
		// Maximalen Grauwert nur 1x berechnen
		if (this.calculatedValues.get(key) == null) {
			// Maximalen Grauwert initial auf den minimalen Grauwert setzen,
			// damit ein (höherer) maximaler Grauwert gefunden werden kann
			double max = MIN_COLOR_VALUE;
			for (int x = 0; x < this.image.getWidth(); x++) {
				for (int y = 0; y < this.image.getHeight(); y++) {
					// Grauwert des Pixels an der Position x,y holen
					int grayValue = this.getColorValueOfPixel(color, x, y);
					// Prüfen, ob aktueller Grauwert des Pixels größer ist,
					// als der aktuell gefundene maximale Grauwert
					if (grayValue > max) {
						max = grayValue;
					}
				}
			}
			// Gefundenen Grauwert abspeichern
			this.calculatedValues.put(key, max);
		}
		// zuvor berechneten maximalen Grauwert zurückgeben
		return this.calculatedValues.get(key);
	}

	/**
	 * Liefert die Varianz der Grauwerte des Bildes
	 *
	 * @return Varianz der Grauwerte des Bildes
	 */
	public double getVarianz(ImageColor color) {
		String key = color.toString() + VARIANZ;
		// Varianz nur 1x berechnen
		if (this.calculatedValues.get(key) == null) {
			double mid = this.getMidValue(color);
			long sum = 0;
			for (int x = 0; x < this.image.getWidth(); x++) {
				for (int y = 0; y < this.image.getHeight(); y++) {
					// Formel für Berechnung der Varianz anwenden
					// (Einzelner Grauwert - Mittlerer Grauwert)^2
					sum += Math.pow(this.getColorValueOfPixel(color, x, y) - mid, 2);
				}
			}
			// Varianz berechnen und in Instanzvariable abspeichern
			// Summe durch Anzahl der Pixel - 1
			this.calculatedValues.put(key, (double) sum / (this.getPixelCount() - 1));
		}
		return this.calculatedValues.get(key);
	}

	/**
	 * Liefert die Standardabweichung der Grauwerte des Bildes
	 *
	 * @return Standardabweichung der Grauwerte des Bildes
	 */
	public double getStandardabweichung(ImageColor color) {
		String key = color.toString() + STANDARD_DEVIATION;
		if (this.calculatedValues.get(key) == null) {
			// Standardabweichung ist die Wurzel aus der Varianz
			this.calculatedValues.put(key, Math.sqrt(this.getVarianz(color)));
		}
		return this.calculatedValues.get(key);
	}

	/**
	 * Berechnet und liefert die Entropie dieses Bildes
	 *
	 * @return Entropie dieses Bildes
	 */
	public double getEntropy(ImageColor color) {
		String key = color.toString() + ENTROPY;
		// Entropie nur 1x berechnen
		if (this.calculatedValues.get(key) == null) {
			// Entropie ermittelt sich aus:
			// - Summe von(p * log2(p))
			double entropy = 0.0;
			for (int value : this.getColorValueCount(ImageColor.GRAY)) {
				// Pr�fen, ob es diesen Grauwert überhaupt gib, sonst ignoriere diesen Grauwert
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
			this.calculatedValues.put(key, entropy);
		}
		return this.calculatedValues.get(key);
	}

	/**
	 * Berechnet die Häufigkeit / Anzahl jedes einzelnen Grauwertes im Bild. Liefert
	 * ein Array mit einer Größe von {@value #maxGrayValue} + 1. Der Index gibt den
	 * Grauwert an und der darin enthaltene Wert gibt die Häufigkeit dieses
	 * Grauwertes an.
	 *
	 * @return Anzahl der Grauwerte im Bild
	 */
	public Integer[] getColorValueCount(ImageColor c) {
		if (colorValueCounts.get(c) == null) {
			// Array bildet alle Grauwerte und ihre Häufigkeit ab
			// Der Index ist der Grauwert und die darin enthaltende
			// Zahl ist die Anzahl des Grauwertes
			int[] colorCount = new int[MAX_COLOR_VALUE + 1];
			for (int x = 0; x < this.image.getWidth(); x++) {
				for (int y = 0; y < this.image.getHeight(); y++) {
					// Grauwert des Pixels bei x, y ermitteln
					int colorValue = getColorValueOfPixel(c, x, y);

					// Anzahl des Grauwertes inkrementieren
					colorCount[colorValue]++;
				}
			}
			// Berechnete Anzahl von Grauwerten speichern
			this.colorValueCounts.put(c, Arrays.stream(colorCount).boxed().toArray(Integer[]::new));
		}
		return this.colorValueCounts.get(c);
	}

	private int getColorValueOfPixel(ImageColor color, int x, int y) {
		return ImageUtils.getColorValueOfPixel(color, image.getRGB(x, y));
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

	public enum ImageColor {
		GRAY, RED, GREEN, BLUE;
	}

}
