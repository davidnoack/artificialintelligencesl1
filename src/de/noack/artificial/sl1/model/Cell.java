package de.noack.artificial.sl1.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse repräsentiert eine Zelle. Dabei ist es zunächst unabhängig, ob es sich hierbei um eine Zelle mit Von-Neumann-Umgebung, oder Moore
 * Umgebung handelt.
 */
public class Cell {

	private State state;
	private int xPos;
	private int yPos;
	private Set <Cell> neighbours;
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

	public void applyRules() {
		for (Rule rule : rules) {
			rule.applyRule(this);
		}
	}
}
