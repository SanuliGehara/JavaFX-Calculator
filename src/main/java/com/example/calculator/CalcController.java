package com.example.calculator;
/*
* Division by zero will return th answer as "Infinity"
* Returns the answer as a fractional number*/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.ArrayList;

public class CalcController {
    @FXML
    private TextField textField;

    private final ArrayList<Double> numbers = new ArrayList<>();
    private final ArrayList<String> operators = new ArrayList<>();
    private double currentNumber = 0;
    private boolean newOperation = false;   // flag for new operation with the result of the previous operation
    private boolean isNegative = false;     // flag to indicate a negative number

    @FXML
    public void calculateAnswer(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();  // Get the button that was clicked
        String buttonText = clickedButton.getText();    // Get the text of the clicked button

        if (buttonText.matches("[0-9]")) {  // When a Number button is clicked
            if (newOperation) {
                currentNumber = 0;
                numbers.clear();
                textField.clear();
                newOperation = false;
            }
            currentNumber = currentNumber * 10 + Integer.parseInt(buttonText);   // handles multi-digit numbers

            textField.setText(textField.getText() + buttonText);
        }
        else if (buttonText.equals("=")) {
            addCurrentNumberToNumbers();
            currentNumber = 0;

            // Perform the calculation with the stored numbers and operators
            double result = performCalculation();
            textField.setText(String.valueOf(result));
            numbers.clear();
            operators.clear();
            numbers.add(result);       // store the result for further calculations
            newOperation = true;
        }
        else if (buttonText.equals("C")) {
            // clear the text field and reset values
            numbers.clear();
            operators.clear();
            textField.clear();
            currentNumber= 0;
            newOperation= false;
            isNegative = false;
        }
        else {  // if an operator button is clicked
            if (buttonText.equals("-")) {
                if (textField.getText().isEmpty() || textField.getText().endsWith(" ") || newOperation) {
                    isNegative = true;
                    textField.setText(textField.getText() + "-");
                }
                else {
                    addCurrentNumberToNumbers();
                    operators.add(buttonText);
                    currentNumber = 0;
                    textField.setText(textField.getText() + " " + buttonText + " ");
                    newOperation = false;
                }
            }
            else {
                addCurrentNumberToNumbers();
                operators.add(buttonText);
                currentNumber = 0;
                textField.setText(textField.getText() + " " + buttonText + " ");
                newOperation = false;
            }
        }
    }

    public void addCurrentNumberToNumbers() {
        if (isNegative) {
            currentNumber = -currentNumber;
            isNegative = false;
        }
        numbers.add(currentNumber);
    }

    private double performCalculation() {
        // First pass: handle multiplication and division
        ArrayList<Double> tempNumbers = new ArrayList<>();
        ArrayList<String> tempOperators = new ArrayList<>();
        tempNumbers.add(numbers.get(0));
        //System.out.println("numbers(0): "+numbers.get(0));

        for (int i = 0; i < operators.size(); i++) {
            String operator = operators.get(i);
            if (operator.equals("X") || operator.equals("/")) {
                double operand1 = tempNumbers.remove(tempNumbers.size() - 1);
                double operand2 = numbers.get(i + 1);

                double result = operator.equals("X") ? operand1 * operand2 :
                        operand1 / operand2;

                tempNumbers.add(result); //save result for further calculations
                //System.out.println("*,/: "+operand1 +" "+operator+operand2+" "+result);
            } else {
                tempNumbers.add(numbers.get(i + 1));
                tempOperators.add(operator);
            }
        }

        // Second pass: handle addition and subtraction
        double result = tempNumbers.get(0);
        //System.out.println("+,-: "+result);

        for (int i = 0; i < tempOperators.size(); i++) {
            String operator = tempOperators.get(i);
            //System.out.println(operator);
            double operand = tempNumbers.get(i + 1);
            if (operator.equals("+")) {
                result += operand;
                //System.out.println(result);
            } else if (operator.equals("-")) {
                result -= operand;
                //System.out.println("Final: "+operand+" "+result);
            }
        }
        return result;
    }
}