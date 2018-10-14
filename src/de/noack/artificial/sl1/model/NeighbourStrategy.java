package de.noack.artificial.sl1.model;

public interface NeighbourStrategy {

	void initVonNeumannNeighbours(Cellfield cellfield);

	void initMooreNeighbours(Cellfield cellfield);
}
