package de.noack.artificial.sl1.gui;

import de.noack.artificial.sl1.logic.LogicHandler;
import javafx.application.Application;
import javafx.application.Platform;
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


/**
 * CellularAutomaton stellt eine JavaFX Anwendung bestehend aus zwei Eingabeparametern (Anzahl der
 * Durchläufe und Feldgröße), einem Button zum Starten, sowie einem Zellfeld, welches anhand der
 * Feldgröße entsprechend "gemalt" wird.
 */
public class CellularAutomaton extends Application {

	// Durchmesser einer Zelle
	private int dMax = 10;

	// Zellfeld
	private Canvas cellularField = new Canvas();

	// Label für den aktuellen Durchlauf -> Muss als globale Variable deklariert sein,
	// um getter Zugriff von außerhalb zu ermöglichen.
	private Label iterationLabel = new Label("Iteration No.: 0");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Zellul\u00e4rer Automat");

		primaryStage.setOnCloseRequest(t -> {
			Platform.exit();
			System.exit(0);
		});

		// Label für die Eingabe der Anzahl der Durchläufe
		Label countLabel = new Label("Enter iteration count: ");
		countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

		// Label für die Feldgröße
		Label sizeLabel = new Label("Choose fieldsize: ");
		sizeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

		iterationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

		// Eingabefeld für die Anzahl der Durchläufe
		TextField countField = new TextField("0");

		// Sicherstellen, dass nur Nummern eingetippt werden können.
		countField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				countField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		// Slider für das wählen der Feldgröße
		Slider slider = new Slider();
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

		// Aktueller Wert des Sliders zur Ausgabe
		Label sliderLabel = new Label(String.valueOf(slider.getValue()).replace(".0", ""));
		sliderLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		slider.valueProperty().addListener(e -> sliderLabel.setText(String.valueOf(slider.getValue()).replace(".0", "")));

		// Button zum Starten der Anwendung
		Button startSimulation = new Button();
		startSimulation.setText("Start simulation!");
		startSimulation.setOnAction(e -> startSimulation(Integer.valueOf(countField.getText()).intValue(), new Double(slider.getValue()).intValue()));

		// Raster für die Oberfläche wird befüllt.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));

		int columnIndex = 0;
		grid.add(countLabel, columnIndex++, 0);
		grid.add(countField, columnIndex++, 0);
		grid.add(sizeLabel, columnIndex++, 0);
		grid.add(slider, columnIndex++, 0);
		grid.add(sliderLabel, columnIndex++, 0);
		grid.add(startSimulation, columnIndex++, 0);
		grid.add(iterationLabel, columnIndex++, 0);
		grid.add(cellularField, 0, 1, columnIndex, 1);

		Scene scene = new Scene(grid, 1024, 1048);

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(false);
		primaryStage.show();
	}

	private void startSimulation(int count, int fieldSize) {

		dMax = 1000 / fieldSize;

		cellularField.setHeight(fieldSize * dMax + 2);
		cellularField.setWidth(fieldSize * dMax + 2);
		cellularField.getGraphicsContext2D().clearRect(0, 0, cellularField.getWidth(), cellularField.getHeight());
		LogicHandler.getInstance().simulate(this, count, fieldSize);
	}

	public int getdMax() {
		return dMax;
	}

	public Canvas getCellularField() {
		return cellularField;
	}

	public Label getIterationLabel() {
		return iterationLabel;
	}
}