//Node is the parent class of each of the components of the Scene (Label, Button, TextField, Slider)

package edu.miracosta.cs112.nclark.ic25_tipcalculatorfx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;

//Refactored this file name to Main.
public class Main extends Application {
    //Any Node that changes or is interacted with by user becomes a "field"
    private TextField billAmountTF = new TextField();
    //Start tipPercent with a default value.
    private Label tipPercentLabel = new Label("15%");
    //v = min, v1 = max, v2 = default
    private Slider tipPercentSlider = new Slider(0, 30, 15);
    private TextField tipAmountTF = new TextField();
    private TextField totalAmountTF = new TextField();
    private Button clearButton = new Button("Clear");
    private Button calculateButton = new Button("Calculate");

    @Override
    public void start(Stage stage) throws IOException {
        //Deleted controller and former line 13 replaced it with GridPane line.
        //Panes are a type of layout in JavaFX which uses row, column positions to place nodes
        //Panes go (column, row) for coordinates
        GridPane pane = new GridPane();
        //Want to add gaps between each of the panes. Parameter takes pixels for the gap
        pane.setVgap(5);
        pane.setHgap(5);
        //Want to center our panes in the middle of the scene
        pane.setAlignment(Pos.CENTER);

        //Name, column, row for Label
        pane.add(new Label("Bill Amount:"), 0, 0);
        pane.add(billAmountTF, 1, 0);
        billAmountTF.setAlignment(Pos.CENTER_RIGHT);
        tipAmountTF.setAlignment(Pos.CENTER_RIGHT);
        totalAmountTF.setAlignment(Pos.CENTER_RIGHT);

        pane.add(tipPercentLabel, 0, 1);
        pane.add(tipPercentSlider, 1, 1);
        tipPercentSlider.setShowTickMarks(true);
        tipPercentSlider.setMajorTickUnit(5);
        tipPercentSlider.setMinorTickCount(4);
        tipPercentSlider.setShowTickLabels(true);
        tipPercentSlider.setSnapToTicks(true);

        //Wire the slider to the calculate method
        tipPercentSlider.valueProperty().addListener((obsVal, oldVal, newVal) -> {
            tipPercentLabel.setText(newVal.intValue() + "%");
            calculate();
        });


        pane.add(new Label("Tip Amount:"), 0, 2);
        //Don't want user to be able to manipulate this pane
        //Pressing tab no longer goes from the slider to tip amount, it passes over to total amount
        tipAmountTF.setFocusTraversable(false);
        tipAmountTF.setMouseTransparent(true);
        pane.add(tipAmountTF, 1, 2);

        //Wire up bill amount text field to calculate method to change as the text changes
        billAmountTF.textProperty().addListener((obsVal, oldVal, newVal) -> calculate());

        pane.add(new Label("Total Amount:"), 0, 3);
        //Now it goes to clear
        totalAmountTF.setFocusTraversable(false);
        totalAmountTF.setMouseTransparent(true);
        pane.add(totalAmountTF, 1, 3);

        //clear and calculate live in the same cell
        //Make horizontal box for stacking horizontally
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(clearButton);
        hBox.getChildren().add(calculateButton);

        //Attach methods to each button
        // clearButton is clicked -> clear()
        // -> is a lambda expression. Basically, whenever e happens, do the action to the right.
        clearButton.setOnAction(e -> clear());

        // calculateButton is clicked -> calculate()
        calculateButton.setOnAction(e -> calculate());

        pane.add(hBox, 1, 4);

        Scene scene = new Scene(pane, 320, 240);
        stage.setTitle("Tip Calculator");
        stage.setScene(scene);
        stage.show();
    }

    private void clear() {
        //Clear out text fields, set slider back to 15%, focus returns to billAmountTF
        //clear() is built in already and has no relation to our clear method
        billAmountTF.clear();
        tipAmountTF.clear();
        totalAmountTF.clear();

        tipPercentSlider.setValue(15);

        billAmountTF.requestFocus();

    }

    private void calculate() {
        if (billAmountTF.getText().isEmpty()){
            clear();
            return;
        }
        try {
            double billAmount = Double.parseDouble(billAmountTF.getText());
            double tipPercent = (int) tipPercentSlider.getValue();
            double tipAmount = billAmount * tipPercent / 100.0;
            double totalAmount = billAmount + tipAmount;
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            //Update text fields

            tipAmountTF.setText(currency.format(tipAmount));
            totalAmountTF.setText(currency.format(totalAmount));

        }
        catch(NumberFormatException e) {
            billAmountTF.clear(); //clear anything non-numeric out
            billAmountTF.requestFocus(); //Let user type again
        }
    }

    public static void main(String[] args) {
        launch();
    }
}