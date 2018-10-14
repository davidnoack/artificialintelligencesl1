package de.noack.artificial.sl1.gui;

import de.noack.artificial.sl1.logic.LogicHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CellularAutomaton extends Application {

	private int xMax = 100;
	private int yMax = 100;
	private int dMax = 10;

	// Zellfeld
	private Canvas cellularField = new Canvas();
	private Button startSimulation = new Button();
	GridPane grid = new GridPane();

	// Slider für das wählen der Feldgröße
	private Slider slider = new Slider();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Zellul\u00e4rer Automat");

		// Label Label für die Eingabe der Anzahl der Durchläufe
		Label countLabel = new Label("Enter count: ");
		countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

		// Eingabefeld für die Anzahl der Durchläufe
		TextField countField = new TextField("0");

		slider.setMin(0);
		slider.setMax(100);
		slider.setOrientation(Orientation.HORIZONTAL);
		slider.setValue(100);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(50);
		slider.setBlockIncrement(5);
		slider.valueProperty().addListener((obs, oldVal, newVal) ->
				slider.setValue(Math.round(newVal.doubleValue())));

		// Wert des Sliders
		Label sliderLabel = new Label(String.valueOf(slider.getValue()).replace(".0", ""));
		sliderLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		slider.valueProperty().addListener(e -> sliderLabel.setText(String.valueOf(slider.getValue()).replace(".0", "")));

		// Startknopf
		startSimulation.setText("Start simulation!");
		startSimulation.setOnAction(e -> startSimulation(Integer.valueOf(countField.getText()).intValue()));

		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));

		grid.add(countLabel, 0, 0);
		grid.add(countField, 1, 0);
		grid.add(slider, 2, 0);
		grid.add(sliderLabel, 3, 0);
		grid.add(startSimulation, 4, 0);
		grid.add(cellularField, 0, 1, 5, 1);

		Scene scene = new Scene(grid, 1024, 1048);

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
		primaryStage.show();
	}

	private void startSimulation(int count) {

		xMax = new Double(slider.getValue()).intValue();
		yMax = xMax;
		dMax = 1000 / xMax;

		// Zellfeld
		cellularField.setHeight(yMax * dMax + 2);
		cellularField.setWidth(xMax * dMax + 2);
		try {
			cellularField.getGraphicsContext2D().clearRect(0, 0, cellularField.getWidth(), cellularField.getHeight());
			LogicHandler.getInstance().getTimer().cancel();
			LogicHandler.getInstance().getTimer().purge();
			LogicHandler.getInstance().simulate(this, count, xMax);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	public int getxMax() {
		return xMax;
	}

	public int getdMax() {
		return dMax;
	}

	public Canvas getCellularField() {
		return cellularField;
	}

	public Button getStartSimulation() {
		return startSimulation;
	}

	public GridPane getGrid() {
		return grid;
	}
}
