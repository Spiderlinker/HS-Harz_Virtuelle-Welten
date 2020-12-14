![](.//media/image1.png){width="4.086614173228346in"
height="0.5590551181102362in"}

![](.//media/image2.png){width="3.622047244094488in"
height="2.0118110236220472in"}![](.//media/image2.png){width="2.9448818897637796in"
height="2.0354330708661417in"}

Inhaltsverzeichnis {#inhaltsverzeichnis .TOC-Heading}
==================

[A Funktionsumfang 2](#funktionsumfang)

[A.1 Datei 3](#datei)

[A.1.1 Neues Bild laden 3](#neues-bild-laden)

[A.1.2 Bild speichern unter... 4](#bild-speichern-unter)

[A.2 Statistik 4](#statistik)

[A.2.1 Statistik einblenden 4](#statistik-einblenden)

[A.3 Farben 5](#farben)

[A.3.1 Histogrammausgleich 5](#histogrammausgleich)

[A.3.2 Grauwerttransformation 6](#grauwerttransformation)

[A.3.3 Graustufen 7](#graustufen)

[A.3.4 Binärbild 7](#binärbild)

[A.4 Filter 8](#filter)

[A.4.1 Tiefpassfilter 8](#tiefpassfilter)

[A.4.2 Hochpassfilter 9](#hochpassfilter)

[A.4.3 Morphologische Filter 10](#morphologische-filter)

[A.4.4 Erosion 11](#erosion)

[B Implementierungsdetails 12](#implementierungsdetails)

Funktionsumfang
===============

Im ersten Teil dieser Dokumentation werden die im Programm enthaltenden
Funktionen vorgestellt. Es wird gezeigt, unter welchen Menüpunkten
welche Funktion eingeordnet ist und was für eine Auswirkung jede
Funktion auf ein gewähltes Bild hat.

![](.//media/image3.png){width="3.7493055555555554in"
height="2.691666666666667in"}Die Ausgangssituation nach Start des
Programmes ist im Abbildung 1 Programmfenster nach Start dargestellt.
Der Benutzer findet hier ein leeres Fenster mit lediglich der Menüleiste
am oberen Fensterrand vor.

Nachfolgend werden nun alle Menüpunkte nacheinander vorgestellt und
deren enthaltenen Funktionen kurz erläutert.

Eine kurze Übersicht über die komplette Menüstruktur ist hier
abgebildet:

+---------+---+---------+---+---------+---+---------+---------+
| **      |   | **Stat  |   | **F     |   | **F     |         |
| Datei** |   | istik** |   | arben** |   | ilter** |         |
+=========+===+=========+===+=========+===+=========+=========+
| Neues   |   | St      |   | Histog  |   | Tiefpas | Gauss   |
| Bild    |   | atistik |   | ramm-au |   | sfilter |         |
| laden   |   | ein     |   | sgleich |   | \>      | R       |
|         |   | blenden |   |         |   |         | echteck |
+---------+---+---------+---+---------+---+---------+---------+
| Bild    |   |         |   | Gr      |   | Hochpas | Laplace |
| sp      |   |         |   | auwert- |   | sfilter | 4er\    |
| eichern |   |         |   | transfo |   | \>      | Laplace |
| u       |   |         |   | rmation |   |         | 8er\    |
| nter... |   |         |   |         |   |         | Sobe    |
|         |   |         |   |         |   |         | lfilter |
+---------+---+---------+---+---------+---+---------+---------+
|         |   |         |   | Gra     |   | Morphol | Dila    |
|         |   |         |   | ustufen |   | ogische | tation\ |
|         |   |         |   |         |   | Filter  | Erosion |
|         |   |         |   |         |   | \>      |         |
+---------+---+---------+---+---------+---+---------+---------+
|         |   |         |   | Bi      |   |         |         |
|         |   |         |   | närbild |   |         |         |
+---------+---+---------+---+---------+---+---------+---------+

Datei
-----

Im Menü ‚Datei' sind zwei Untermenüpunkte zu finden: ‚Neues Bild
laden...' und ‚Bild speichern unter...'.

![](.//media/image5.png){width="2.2916666666666665in" height="1.0in"}

### Neues Bild laden

Über den Punkt „Neues Bild laden..." erhält der Anwender die Möglichkeit
ein Bild über einen FileChooser auszuwählen und zu öffnen. Nach dem
Bestätigen der gewählten Datei wird das Bild in der Anwendung
dargestellt.

![](.//media/image6.png){width="3.7131944444444445in"
height="3.84375in"}

Abbildung Geöffnetes Bild

Bei Öffnen mehrerer Bilder werden die Bilder in jeweils eigenen Tabs
geöffnet und der Anwender kann zwischen den geöffneten Bildern wechseln.
Alle weiteren (Bild-) Operationen werden auf dem Bild des aktuell
geöffneten Tabs ausgeführt.

![](.//media/image7.png){width="3.0868055555555554in"
height="1.1041666666666667in"}

Abbildung Mehrere Bilder geöffnet

Die Funktionen „Undo" und „Redo" machen die zuletzt durchgeführte
Operation rückgängig bzw. wiederholen diese. Mit „Show Original Image"
wird über dem möglicherweise veränderten angezeigten Bild das
Originalbild (welches der Benutzer anfangs geöffnet hat) eingeblendet.

### Bild speichern unter...

Über diesen Menüpunkt wird das aktuell ausgewählte und geöffnete Bild im
angezeigten Zustand gespeichert. Der Benutzer kann über einen
FileChooser den Ablageort und Namen der Datei wählen, unter der das Bild
gespeichert werden soll.

Statistik
---------

Das Menü Statistik beinhaltet lediglich einen Menüpunkt: „Statistik
einblenden".

![](.//media/image8.png){width="2.75in" height="1.28125in"}

### Statistik einblenden

![](.//media/image9.png){width="4.2868055555555555in"
height="2.3618055555555557in"}Die Statistik zeigt das Histogramm des
aktuellen Bildes an. Hier kann über die Knöpfe am rechten Rand der
gewünschte Farbkanal ausgewählt werden, der im Histogramm angezeigt
werden soll.

![](.//media/image10.png){width="4.799212598425197in"
height="1.220472440944882in"}Unterhalb des Histogramms sind weitere
statistische Informationen des Bildes enthalten, wie z.B. die Anzahl der
Pixel, der minimale, maximale und mittlere Farbwert, die Varianz,
Standardabweichung und die Entropie.

Farben
------

Das Menü „Farben" beinhaltet vier Menüpunkte, die sich alle um das Thema
„Farbe des Bildes" drehen.

![](.//media/image16.png){width="2.2959995625546807in"
height="1.1592180664916885in"}

### Histogrammausgleich

![](.//media/image17.png){width="6.062992125984252in"
height="2.9881889763779528in"}Bei dem Punkt „Histogrammausgleich" öffnet
sich ein neues Popup-Fenster, in dem das geöffnete Bild als Vorschaubild
befindet. Unter dem Vorschaubild befinden sich drei Knöpfe. Über diese
kann der Farbkanal gewählt werden, auf dem der Histogrammausgleich
durchgeführt werden soll.

„Das Verfahren des Histogrammausgleichs beruht auf dem theoretischen
Ansatz, das Bild so zu transformieren, dass die
Auftrittswahrscheinlichkeit aller verfügbaren Graustufen in etwa gleich
wird (möglichst flaches Histogramm)."
(<http://www.gm.fh-koeln.de/~konen/WPF-BV/BV07.PDF>, S. 5)

Das Ergebnis des Histogrammausgleichs auf dem roten Farbkanal ist in
Abbildung 7 Histogrammausgleich - Ergebnis Farbkanal rot dargestellt.
Signifikant zu sehen sind hier die deutlich großen Lücken am rechten
Rand und die Umverteilung der roten Farbwerte in Richtung der Mitte.

![](.//media/image23.png){width="5.8in" height="3.1802121609798775in"}

Abbildung Histogrammausgleich - Ergebnis Farbkanal rot

### Grauwerttransformation

![](.//media/image24.png){width="6.271653543307087in"
height="4.488188976377953in"}Mit der Graustufentransformation kann die
Anzahl der verwendeten Graustufen (Kanäle) eingestellt werden. Über den
im Popup angezeigten Schieberegler kann die Anzahl der Graustufen
eingestellt werden. Je weniger Graustufen, desto mehr Details gehen
verloren. Der Unterschied zwischen der unterschiedlichen Anzahl von
Graustufen ist in den folgenden Abbildungen sichtbar.

### Graustufen

![](.//media/image28.png){width="3.456692913385827in"
height="2.52755905511811in"}Wenn das Bild lediglich in ein sog.
„Schwarz/Weiß-Bild" konvertiert werden soll bzw. nur noch Graustufen
vorhanden sein sollen, dann kann dies über den Menüpunkt „Graustufen"
erfolgen. Dem Bild wird dadurch die Farbe „entzogen" und statt in Farbe
in Grautönen dargestellt.

### Binärbild

![](.//media/image32.png){width="6.239583333333333in"
height="4.4875in"}Beim Punkt Binärbild wird das Bild zu einem binären
Bild umgewandelt. D.h. es gibt nur noch die Farben schwarz und weiß.
Über den Regler im Popup-Fenster kann der Schwellwert eingestellt
werden. Dieser gibt an, ab welchem Wert ein Farbwert als Schwarz oder
als Weiß interpretiert werden soll. In den nachfolgenden Abbildungen
sind zwei unterschiedliche Schwellwerte abgebildet.

Filter
------

Der Menüpunkt Filter beinhaltet verschiedene Unterpunkte.

![](.//media/image36.png){width="3.5625in" height="1.375in"}

### Tiefpassfilter

Das Untermenü Tiefpassfilter beinhaltet zwei weitere Operationen, die im
Folgenden kurz erläutert werden.

![](.//media/image37.png){width="4.208333333333333in"
height="1.4895833333333333in"}

#### Gauss

![](.//media/image28.png){width="3.456692913385827in"
height="2.52755905511811in"}Der Gauss-Filter ist ein Glättungsfilter und
lässt das Bild etwas unschärfer darstellen. Er glättet also die Kanten.
Eine beispielhafte Anwendung ist nachfolgend dargestellt.

#### Rechteck

![](.//media/image28.png){width="3.9448818897637796in"
height="2.5393700787401574in"}Der Rechteck-Filter ist wie der
Gauss-Filter ein Glättungsfilter. Diese beiden Filter unterscheiden sich
in der verwendeten Filtermaske. Die Unterschiede werden in den folgenden
Bildern deutlich.

### Hochpassfilter

Die Hochpassfilter beinhalten drei Filter. Alle dienen der
Kantenerkennung.

![](.//media/image42.png){width="4.34375in"
height="1.6041666666666667in"}

#### Laplace 4er

Die Wirkung des Laplace-4er Filters ist, wie nachfolgend noch zu sehen
sein wird, nicht allzu gut im Vergleich zu dem Laplace-8er Filter oder
dem Sobel-Filter.

![](.//media/image28.png){width="3.9448818897637796in"
height="2.52755905511811in"}Allerdings lassen sich auch hier schon die
Kanten der Luftballons erkennen.

#### Laplace 8er

![](.//media/image28.png){width="3.9448818897637796in"
height="2.52755905511811in"}Der Laplace-8er Filter verwendet eine etwas
andere Filtermaske als der Laplace-4er Filter. Hierdurch wird das
Ergebnis schon deutlich besser. Die Kanten der Luftballons sind deutlich
erkennbar.

#### Sobel

![](.//media/image28.png){width="3.9448818897637796in"
height="2.52755905511811in"}Beim Sobel-Filter gibt es eine sog.
Horizontal und vertikale Faltung. Diese Faltungen basieren auch wieder
auf Filtermasken. Das gute Ergebnis wird durch die
Nacheinanderausführung der horizontalen und vertikalen Filtermasken
erzielt.

### Morphologische Filter

Die morphologischen Filter werden auf Binärbilden angewandt.

![](.//media/image49.png){width="4.958333333333333in"
height="1.8020833333333333in"}

Bei Ausführung der Filter wird das Bild, falls es zuvor nicht schon in
ein Binärbild umgewandelt worden ist, in ein Binärbild mit einem
Schwellwert von 127 umgewandelt.

#### Dilatation

![](.//media/image50.png){width="5.425196850393701in"
height="2.52755905511811in"}Die Dilatation vergrößert bzw. dehnt die
Abbildung des Bildes aus. In diesem Fall wird der schwarz eingefärbte
Bereich des Bildes erweitert. Die Ballons werden größer.

### Erosion

![](.//media/image50.png){width="5.425196850393701in"
height="2.52755905511811in"}Die Erosion ist das Gegenstück zur
Dilatation. Die Erosion trägt Teile des Bildes ab. Die Abbildung wird
also verkleinert. In diesem Fall wird der schwarze Bereich des Bildes
vom Rand her abgetragen. Die Ballons werden kleiner. Die weißen Bereiche
(Reflektionen) in den Ballons dadurch größer.

Implementierungsdetails
=======================

![](.//media/image60.png){width="6.2in" height="3.0694444444444446in"}

![](.//media/image61.png){width="6.295833333333333in"
height="1.7215277777777778in"}

![](.//media/image62.png){width="6.295833333333333in"
height="4.095833333333333in"}

![](.//media/image63.png){width="6.2868055555555555in"
height="2.3305555555555557in"}
