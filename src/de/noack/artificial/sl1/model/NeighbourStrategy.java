package de.noack.artificial.sl1.model;

/**
 * Erlaubt die Definition von Nachbarstrategien, welche in der Logik implementiert werden können.
 */
public interface NeighbourStrategy {

	// Initialisiert eine Von Neumann Umgebung
	void initVonNeumannNeighbours(Cellfield cellfield);

	// Initialisiert eine Moore Umgebung
	void initMooreNeighbours(Cellfield cellfield);
}