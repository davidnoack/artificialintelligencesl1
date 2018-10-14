package de.noack.artificial.sl1.model;

import java.util.ArrayList;
import java.util.List;

public class Cellfield {

	private ArrayList <List <Cell>> cellField = new ArrayList <>();
	private NeighbourStrategy neighbourStrategy;

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

	public void setCellfield(ArrayList <List <Cell>> cellfield) {
		this.cellField = cellfield;
	}

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
