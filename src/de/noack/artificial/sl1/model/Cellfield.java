package de.noack.artificial.sl1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Die nachfolgende Klasse stellt das Zellfeld dar, welches eine zweidimensionale Liste enthält,
 * die im Endeffekt das Zellfeld "aufspannt". Außerdem erhält das Zellfeld eine in der Logik definierte
 * Strategie zur Referenzierung von Nachbarn, folglich die Umgebung. So kann u.a. sowohl eine Moore
 * und eine von Neumann Umgebung implementiert werden. Es können aber auch komplett andere, benutzerdefinierte
 * Umgebungen definiert werden.
 */
public class Cellfield {

	// Zellfeld, welches alle Zellen enthält
	private ArrayList <List <Cell>> cellField = new ArrayList <>();

	// Strategie zur Nachbarschaftsinitialisierung
	private NeighbourStrategy neighbourStrategy;

	/**
	 * Zurzeit wird die Initialisierung so vorgenommen, dass eine Feldgröße, welche die "Kantenlänge" darstellt,
	 * übergeben wird. es werden dann quasi fieldSize * fieldSize Zellen erstellt und zweidimensional aufgespannt.
	 * Des weiteren wird eine Liste von Regeln übergeben, welche über "applyRules()" auf alle Zellen des Feldes angewandt
	 * werden können. Jede Zelle erhält ihre Position und als initialen Status den Use Case spezifischen "NADEL"-Status,
	 * da eine Monokultur initial vorhanden sein soll.
	 *
	 * @param fieldSize
	 * @param rules
	 */
	public Cellfield(int fieldSize, List <Rule> rules) {
		int staticCount = fieldSize;
		while (fieldSize-- > 0) {
			List <Cell> cellList = new ArrayList <>();
			for (int i = staticCount; i > 0; i--) {
				cellList.add(new Cell(State.NADEL, staticCount - fieldSize, staticCount - i, rules));
			}
			cellField.add(cellList);
		}
	}

	public ArrayList <List <Cell>> getCellfield() {
		return cellField;
	}

	// Sucht eine Zelle aus dem Feld anhand ihrer festen Position
	public Cell getCellByPos(int xPos, int yPos) {
		for (List <Cell> cellList : cellField) {
			for (Cell cell : cellList) {
				if (cell.getxPos() == xPos && cell.getyPos() == yPos) {
					return cell;
				}
			}
		}
		return null;
	}

	public NeighbourStrategy getNeighbourStrategy() {
		return neighbourStrategy;
	}

	public void setNeighbourStrategy(NeighbourStrategy neighbourStrategy) {
		this.neighbourStrategy = neighbourStrategy;
	}

	public void applyRules() {
		for (List <Cell> cellList : cellField) {
			for (Cell cell : cellList) {
				cell.applyRules();
			}
		}
	}
}