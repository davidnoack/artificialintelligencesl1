package de.noack.artificial.sl1.logic;

import de.noack.artificial.sl1.gui.CellularAutomaton;
import de.noack.artificial.sl1.model.*;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Die Klasse LogicHandler kapselt sämtliche Zugriffe, welche von außen auf das Modell durchgeführt werden. Es handelt sich hierbei um ein Singleton,
 * d.h. die Klasse ist lediglich einmalig instanziierbar. Dieser Ansatz ist an dieser Stelle gewählt worden, da Java Executor Service Elemente
 * verwendet werden und die Ergebnisse, welche aus den Berechnungen erfolgen, stets konsistent sein müssen. Außerdem wird das Modell hier einmalig zum
 * "Leben erweckt", d.h. zwei Instanzen würden auch zu zwei parallel existierenden Kontexten führen.
 */
public class LogicHandler {

	ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	// Hier wird die einzig erzeugbare Instanz deklariert und instanziiert.
	private static LogicHandler ourInstance = new LogicHandler();
	// Für jeden Durchlauf der Regeln wird der Timer Count um 1 erhöht. Dies ist notwendig, um sicherzustellen, dass nach Erreichen der gewünschten
	// Durchlaufzahl, kein weiterer Task mehr durchgeführt wird. Er wird mit 0 initialisiert.
	int timerCount = 0;

	List <ScheduledFuture> taskList = new ArrayList <>();

	// Allein über diese Methode kann die einzig vorhandene Instanz bezogen werden.
	public static LogicHandler getInstance() {
		return ourInstance;
	}

	// Die Methode "simulate" erhält die graphische Oberfläche als Input und manipuliert diese anhand der Eingabedaten. Hierzu wird zunächst das
	// fachliche Modell initialisiert und im Anschluss anhand der gewählten Parameter mit den definierten Regeln manipuliert.
	public void simulate(CellularAutomaton gui, int count, int fieldSize) {
		for (ScheduledFuture scheduledFuture : taskList) {
			scheduledFuture.cancel(true);
			taskList.clear();
		}
		executorService = Executors.newSingleThreadScheduledExecutor();
		timerCount = 0;

		Cellfield cellfield = new Cellfield(fieldSize, defineRules());
		cellfield.setNeighbourStrategy(new NeighbourStrategy() {
			@Override
			public void initVonNeumannNeighbours(Cellfield cellfield) {
				for (List <Cell> cellList : cellfield.getCellfield()) {
					for (Cell cell : cellList) {
						Set <Cell> neighbours = new HashSet <>();
						neighbours.add(cellfield.getCellByPos(cell.getxPos() - 1, cell.getyPos()));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() + 1, cell.getyPos()));
						neighbours.add(cellfield.getCellByPos(cell.getxPos(), cell.getyPos() - 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos(), cell.getyPos() + 1));
						neighbours.remove(null);
						cell.setNeighbours(neighbours);
					}
				}
			}

			@Override
			public void initMooreNeighbours(Cellfield cellfield) {
				for (List <Cell> cellList : cellfield.getCellfield()) {
					for (Cell cell : cellList) {
						Set <Cell> neighbours = new HashSet <>();
						neighbours.add(cellfield.getCellByPos(cell.getxPos() - 1, cell.getyPos()));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() - 1, cell.getyPos() + 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() - 1, cell.getyPos() - 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() + 1, cell.getyPos()));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() + 1, cell.getyPos() + 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() + 1, cell.getyPos() - 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos(), cell.getyPos() - 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() + 1, cell.getyPos() - 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() - 1, cell.getyPos() - 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos(), cell.getyPos() + 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() + 1, cell.getyPos() + 1));
						neighbours.add(cellfield.getCellByPos(cell.getxPos() - 1, cell.getyPos() + 1));
						neighbours.remove(null);
						cell.setNeighbours(neighbours);
					}
				}
			}
		});
		cellfield.getNeighbourStrategy().initMooreNeighbours(cellfield);

		GraphicsContext gc = gui.getCellularField().getGraphicsContext2D();
		for (List <Cell> cellList : cellfield.getCellfield()) {
			for (Cell cell : cellList) {
				gc.setFill(cell.getState().getColor());
				gc.fillOval(cell.getxPos() * gui.getdMax() + 2, cell.getyPos() * gui.getdMax() + 2, gui.getdMax(), gui.getdMax());
			}
		}

		taskList.add(executorService.scheduleAtFixedRate(() -> {
			if (timerCount++ >= count) {
				executorService.shutdown();
				return;
			}
			cellfield.applyRules();
			for (List <Cell> cellList : cellfield.getCellfield()) {
				for (Cell cell : cellList)
					Platform.runLater(() -> {
						gc.setFill(cell.getState().getColor());
						gc.fillOval(cell.getxPos() * gui.getdMax() + 2, cell.getyPos() * gui.getdMax() + 2, gui.getdMax(), gui.getdMax());
					});
			}
			Platform.runLater(() -> gui.getIterationLabel().setText("Iteration No.: " + timerCount));
		}, 1, 1000, TimeUnit.MILLISECONDS));
	}

	/**
	 * Unter der Methode defineRules sind je nach Use Case Regeln definiert, welche dem Modell zur Selbstanwendung übergeben werden. countAlives
	 *
	 * @return Liste aus den definierten Regeln.
	 */
	private List <Rule> defineRules() {

		// Da die Regeln Teil der Logik sind, werden sie hier im Logic Handler einzeln definiert, in eine ArrayList gepackt und dem Modell zur
		// Selbstanwendung übergeben.
		return Arrays.asList((Rule) cell -> {
			int[] counts = initCell(cell);
			if ((cell.getState() == State.NADEL) && (counts[State.NADEL.ordinal()] + counts[State.TOTHOLZ.ordinal()] > 3)) {
				if (new Random().nextDouble() <= 0.9) {
					cell.setState(State.TOTHOLZ);
					return;
				}
			}

			if (cell.getState() == State.TOTHOLZ) {
				if (new Random().nextDouble() <= 0.3) {
					cell.setState(State.LAUB);
				} else {
					cell.setState(State.NADEL);
				}
			} else {
				if (new Random().nextDouble() <= 0.1) {
					cell.setState(State.TOTHOLZ);
				}
			}
		});
	}

	private int[] initCell(Cell cell) {
		Set <Cell> neighbours = cell.getNeighbours();
		int countLaubholz = 0;
		int countNadelholz = 0;
		int countTotholz = 0;

		for (Cell neighbourCell : neighbours) {
			switch (neighbourCell.getState()) {
				case LAUB:
					countLaubholz++;
					break;
				case NADEL:
					countNadelholz++;
					break;
				case TOTHOLZ:
					countTotholz++;
					break;
			}
		}
		int[] counts = new int[3];
		counts[State.LAUB.ordinal()] = countLaubholz;
		counts[State.NADEL.ordinal()] = countNadelholz;
		counts[State.TOTHOLZ.ordinal()] = countTotholz;

		return counts;
	}

	public int getTimerCount() {
		return timerCount;
	}
}