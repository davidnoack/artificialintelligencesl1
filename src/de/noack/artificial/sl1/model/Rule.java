package de.noack.artificial.sl1.model;

/**
 * Stellt eine Regel dar, welche auf eine Zelle angewandt werden kann.
 */
public interface Rule {

	void applyRule(Cell cell);
}