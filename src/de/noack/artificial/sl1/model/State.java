package de.noack.artificial.sl1.model;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum State {
	LAUB(Color.LIGHTGREEN),
	NADEL(Color.DARKGREEN),
	TOTHOLZ(Color.BROWN);

	private static final List <State> VALUES =
			Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	private Color color;
	State(Color color) {
		this.color = color;
	}

	public static State randomState() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}

	public Color getColor() {
		return color;
	}
}
