package de.noack.artificial.sl1.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse repräsentiert eine Zelle. Dabei ist es zunächst unabhängig,
 * ob es sich hierbei um eine Zelle mit Von-Neumann-Umgebung, oder Moore
 * Umgebung handelt.
 */
public class Cell {

	// Status, welchen die Zelle annehmen kann.
	private State state;

	// Position auf dem Zellfeld
	private int xPos;
	private int yPos;

	// Referenz auf alle (einzigartigen) benachbarten Zellen
	private Set <Cell> neighbours;

	// Liste an anwendbaren Regeln
	private List <Rule> rules;

	public Cell(State state, int xPos, int yPos, List <Rule> rules) {
		this.rules = rules;
		this.state = state;
		this.xPos = xPos;
		this.yPos = yPos;
		neighbours = new HashSet <>();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Set <Cell> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(Set <Cell> neighbours) {
		this.neighbours = neighbours;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	// Hier werden die in der Logik definierten Regeln auf die Zelle angewandt.
	public void applyRules() {
		for (Rule rule : rules) {
			rule.applyRule(this);
		}
	}
}