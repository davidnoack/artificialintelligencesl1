package de.noack.artificial.sl1.model;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Definiert mehrere Status, welche unabhägig von der Definition zufällig oder direkt abgefragt werden können.
 */
public enum State {
	LAUB(Color.LIGHTGREEN),
	NADEL(Color.DARKGREEN),
	TOTHOLZ(Color.BROWN);

	// Liste aller definierten Values
	private static final List <State> VALUES =
			Collections.unmodifiableList(Arrays.asList(values()));

	// Größe der Enumration
	private static final int SIZE = VALUES.size();

	private static final Random RANDOM = new Random();

	// Farbe des jew. Enum-Wertes
	private Color color;

	State(Color color) {
		this.color = color;
	}

	// Gibt einen zufälligen Status zurück.
	public static State randomState() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}

	public Color getColor() {
		return color;
	}
}