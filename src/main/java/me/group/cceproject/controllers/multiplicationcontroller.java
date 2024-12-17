package me.group.cceproject.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class multiplicationcontroller {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    private char[] tape;
    private int headPosition;
    
    @FXML
    private Label b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15;

    @FXML
    private Button backbtn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label mult2, mult3, mult5, mult6;

    @FXML
    void backbtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/me/group/cceproject/main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void settape(String data) {
        // Initialize the tape with a size greater than the input data to allow head movement
        int tapeSize = 5000; // Choose a size that accommodates input and head movements
        tape = new char[tapeSize];
        Arrays.fill(tape, ' '); // Fill the tape with blank spaces

        // Copy the input data into the tape starting at the center
        int start = (tapeSize / 2) - (data.length() / 2);
        for (int i = 0; i < data.length(); i++) {
            tape[start + i] = data.charAt(i);
        }

        // Set the head position at the last character of the input data
        headPosition = start + data.length() - 1;

        // Define the tape display center index (b8 is the center)
        final int displayCenterIndex = 7; // Center at b8

        // Create an array for labels
        Label[] labels = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15};

        // Reset all labels to empty
        for (Label label : labels) {
            label.setText(" "); // Clear all labels
        }

        // Fill the labels with tape contents around the head position
        // The head is always centered at b8
        for (int i = 0; i < labels.length; i++) {
            int tapeIndex = headPosition - displayCenterIndex + i;
            if (tapeIndex >= 0 && tapeIndex < tape.length) {
                labels[i].setText(String.valueOf(tape[tapeIndex]));
            } else {
                labels[i].setText(" "); // Empty space for out-of-bounds
            }
        }

        // Call the Turing Machine addition logic
        performTuringMachineMultiplication();
    }

    private void performTuringMachineMultiplication() {
        final int[] currentState = {0}; // Start state q0
        final int[] steps = {0};
        final boolean[] halt = {false};

        Thread turingThread = new Thread(() -> {
            while (!halt[0]) {
                char currentSymbol = tape[headPosition];

                switch (currentState[0]) {
                    case 0:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 1;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = ' ';
                            currentState[0] = 10;
                            moveHead(-1);
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 20;
                        } else {
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;

                    case 1:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 2;
                        } else if (currentSymbol == '0' || currentSymbol == '1') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 1;
                        } else{
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;

                    case 2:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1);
                            currentState[0] = 3;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = '0';
                            moveHead(1);
                            currentState[0] = 3;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '1';
                            moveHead(1);
                            currentState[0] = 3;
                        } else if (currentSymbol == '$') {
                            tape[headPosition] = '1';
                            moveHead(-1);
                            currentState[0] = 2;
                        } else if (currentSymbol == '@') {
                            tape[headPosition] = '0';
                            moveHead(-1);
                            currentState[0] = 2;
                        } else{
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;

                    case 3:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(4);
                            currentState[0] = 5;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = '0';
                            moveHead(-1);
                            currentState[0] = 3;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '1';
                            moveHead(-1);
                            currentState[0] = 3;
                        }
                        
                        steps[0]++;
                        break;

                    /*case 4: //reject
                        halt[0] = true;
                        break;*/

                    case 5: //accept
                        halt[0] = true;
                        break;

                    case 10:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 11;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 10;
                        } else{ 
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                    
                    case 11:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 12;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 11;
                        } else{
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                    
                    case 12:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = '@';
                            moveHead(1);
                            currentState[0] = 15;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = '@';
                            moveHead(1);
                            currentState[0] = 15;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '$';
                            moveHead(1);
                            currentState[0] = 15;
                        } else if (currentSymbol == '$' || currentSymbol == '@') {
                            moveHead(-1);
                            currentState[0] = 12;
                        } else{
                            currentState[0] = 12;
                        }
                        steps[0]++;
                        break;
                    
                    case 15:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1);
                            currentState[0] = 16;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(1);
                            currentState[0] = 15;
                        }
                        steps[0]++;
                        break;
                    
                    case 16:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1);
                            currentState[0] = 17;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(1);
                            currentState[0] = 16;
                        }
                        steps[0]++;
                        break;
                    
                    case 17:
                        if (currentSymbol == ' ') {
                            moveHead(-1);
                            currentState[0] = 0;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(1);
                            currentState[0] = 17;
                        }
                        steps[0]++;
                        break;
                    
                    case 20:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 21;
                        } else if (currentSymbol == '0' || currentSymbol == '1') {
                            moveHead(-1);
                            currentState[0] = 20;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 20;
                        } else{
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;

                    case 21:
                        if (currentSymbol == '0') {
                            tape[headPosition] = 'x';
                            moveHead(-1);
                            currentState[0] = 22;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = 'y';
                            moveHead(-1);
                            currentState[0] = 26;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 21;
                        } else {
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                    
                    case 22:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 23;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 22;
                        } else{
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                    
                    // State 23: Transition rules for ' ', '0', '1', and '*'
                    case 23:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = '@';
                            moveHead(1);
                            currentState[0] = 24;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = '@';
                            moveHead(1);
                            currentState[0] = 24;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '$';
                            moveHead(1);
                            currentState[0] = 24;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 23;
                        } else{
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                    
                    // State 24: Transition rules for ' ' and '*'
                    case 24:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1);
                            currentState[0] = 25;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(1);
                            currentState[0] = 24;
                        }
                        steps[0]++;
                        break;
                    
                    // State 25: Transition rules for 'x', 'y', and '*'
                    case 25:
                        if (currentSymbol == 'x') {
                            tape[headPosition] = 'x';
                            moveHead(-1);
                            currentState[0] = 30;
                        } else if (currentSymbol == 'y') {
                            tape[headPosition] = 'y';
                            moveHead(-1);
                            currentState[0] = 30;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(1);
                            currentState[0] = 25;
                        }
                        steps[0]++;
                        break;
                    
                    // State 26: Transition rules for ' ' and '*'
                    case 26:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 27;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 26;
                        } else{
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                    
                    // State 27: Transition rules for ' ', '0', '1', and '*'
                    case 27:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = '$';
                            moveHead(1);
                            currentState[0] = 24;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = '$';
                            moveHead(1);
                            currentState[0] = 24;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '@';
                            moveHead(-1);
                            currentState[0] = 28;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 27;
                        }
                        steps[0]++;
                        break;
                    
                    // State 28: Transition rules for ' ', '0', '1'
                    case 28:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = '1';
                            moveHead(1);
                            currentState[0] = 24;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = '1';
                            moveHead(1);
                            currentState[0] = 24;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '0';
                            moveHead(-1);
                            currentState[0] = 28;
                        }
                        steps[0]++;
                        break;
                
                    // State 30: Transition rules for ' ', '0', '1'
                    case 30:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 40;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = 'x';
                            moveHead(-1);
                            currentState[0] = 31;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = 'y';
                            moveHead(-1);
                            currentState[0] = 35;
                        } else {
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                
                    // State 31: Transition rules for ' ' and '*'
                    case 31:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 32;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 31;
                        }
                        steps[0]++;
                        break;

                    case 32:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = 'x';
                            moveHead(1);
                            currentState[0] = 33;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = 'x';
                            moveHead(1);
                            currentState[0] = 33;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = 'y';
                            moveHead(1);
                            currentState[0] = 33;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 32;
                        }
                        steps[0]++;
                        break;

                    case 33:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1);
                            currentState[0] = 34;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(1);
                            currentState[0] = 33;
                        }
                        steps[0]++;
                        break;

                    case 34:
                        if (currentSymbol == 'x') {
                            tape[headPosition] = 'x';
                            moveHead(-1);
                            currentState[0] = 30;
                        } else if (currentSymbol == 'y') {
                            tape[headPosition] = 'y';
                            moveHead(-1);
                            currentState[0] = 30;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(1);
                            currentState[0] = 34;
                        }
                        steps[0]++;
                        break;

                    case 35:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 36;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 35;
                        }
                        steps[0]++;
                        break;

                    case 36:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = 'y';
                            moveHead(1);
                            currentState[0] = 33;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = 'y';
                            moveHead(1);
                            currentState[0] = 33;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = 'x';
                            moveHead(-1);
                            currentState[0] = 37;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 36;
                        }
                        steps[0]++;
                        break;

                    case 37:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = '1';
                            moveHead(1);
                            currentState[0] = 33;
                        } else if (currentSymbol == '0') {
                            tape[headPosition] = '1';
                            moveHead(1);
                            currentState[0] = 33;
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '0';
                            moveHead(-1);
                            currentState[0] = 37;
                        }
                        steps[0]++;
                        break;

                    case 40:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1);
                            currentState[0] = 41;
                        } else if (isStarSymbol(currentSymbol)) {
                            tape[headPosition] = currentSymbol; // Replace '*' with the current symbol
                            moveHead(-1);
                            currentState[0] = 40;
                        }
                        steps[0]++;
                        break;
                
                    // State 41: Transition rules for ' ', 'x', 'y', and '*'
                    case 41:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1);
                            currentState[0] = 42;
                        } else if (currentSymbol == 'x') {
                            tape[headPosition] = '0';
                            moveHead(1);
                            currentState[0] = 41;
                        } else if (currentSymbol == 'y') {
                            tape[headPosition] = '1';
                            moveHead(1);
                            currentState[0] = 41;
                        } else if (isStarSymbol(currentSymbol)) {  // Updated check for '*'
                            tape[headPosition] = currentSymbol;  // Replace '*' with current symbol
                            moveHead(1);
                            currentState[0] = 41;
                        }
                        steps[0]++;
                        break;

                    case 42:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1);
                            currentState[0] = 43;
                        } else if (currentSymbol == 'x') {
                            tape[headPosition] = '0';
                            moveHead(1);
                            currentState[0] = 42;
                        } else if (currentSymbol == 'y') {
                            tape[headPosition] = '1';
                            moveHead(1);
                            currentState[0] = 42;
                        } else if (isStarSymbol(currentSymbol)) {  // Updated check for '*'
                            tape[headPosition] = currentSymbol;  // Replace '*' with current symbol
                            moveHead(1);
                            currentState[0] = 42;
                        }
                        steps[0]++;
                        break;

                    case 43:
                        if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 0;  // Halt state
                        } else if (isStarSymbol(currentSymbol)) {  // Updated check for '*'
                            tape[headPosition] = currentSymbol;  // Replace '*' with current symbol
                            moveHead(1);
                            currentState[0] = 43;
                        }
                        steps[0]++;
                        break;
                }
                // Update UI
                Platform.runLater(() -> {
                    updateDisplayedTape();
                    if (currentState[0] == 5){
                        Platform.runLater(() -> mult5.setText("State: Halt"));
                        centerResult();
                    } else if(currentState[0] == 4){
                        Platform.runLater(() -> mult5.setText("State: Halt | Rejected"));
                    } else{
                        mult5.setText("State: q" + currentState[0]);
                    }
                    if (!halt[0]) {
                        mult6.setText("Steps: " + steps[0]);
                    }
                });

                // Pause for visualization
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });

        turingThread.start();
    }

    private void moveHead(int direction) {
        headPosition += direction;
    
        // Ensure head does not go out of tape bounds
        if (headPosition < 0) {
            headPosition = 0;
        } else if (headPosition >= tape.length) {
            headPosition = tape.length - 1;
        }
    }

    private void updateDisplayedTape() {
        Label[] labels = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15};
        int tapeStart = headPosition - 7; // Start of the visible tape section

        boolean leftHasData = false; // To check if there is data to the left
        boolean rightHasData = false; // To check if there is data to the right

        // Check for non-blank symbols to the left of the visible tape
        for (int i = 0; i < tapeStart; i++) {
            if (i >= 0 && (tape[i] == '0' || tape[i] == '1')) {
                leftHasData = true;
                break;
            }
        }

        // Check for non-blank symbols to the right of the visible tape
        for (int i = tapeStart + labels.length; i < tape.length; i++) {
            if (i < tape.length && (tape[i] == '0' || tape[i] == '1')) {
                rightHasData = true;
                break;
            }
        }

        // Update labels
        for (int i = 0; i < labels.length; i++) {
            int tapeIndex = tapeStart + i;

            if (tapeIndex < 0) {
                labels[i].setText(""); // Out of bounds on the left
            } else if (tapeIndex >= tape.length) {
                labels[i].setText(""); // Out of bounds on the right
            } else {
                labels[i].setText(String.valueOf(tape[tapeIndex])); // Display tape content
            }
        }

        // Add ellipsis if there is data beyond the visible tape
        if (leftHasData) {
            labels[0].setText("..."); // Ellipsis on the leftmost label
        }
        if (rightHasData) {
            labels[labels.length - 1].setText("..."); // Ellipsis on the rightmost label
        }
    }

    private void centerResult() {
        // Total number of labels
        final int totalLabels = 15;
        
        // The result will be taken from the tape
        String result = new String(tape).trim();  // Trim the result to remove leading/trailing spaces
    
        // Calculate the length of the result
        int resultLength = result.length();
    
        // Determine the index of the center label (b8 is the center)
        final int centerIndex = 7; // b8 is index 7 (0-based index)
    
        // Calculate the starting position of the result to center it around b8
        int startIndex = centerIndex - (resultLength / 2);
    
        // Create an array of labels
        Label[] labels = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15};
    
        // Clear all labels before setting the new result
        for (Label label : labels) {
            label.setText(" "); // Clear all labels
        }
    
        // Fill the labels with the result, ensuring it stays within bounds
        for (int i = 0; i < resultLength; i++) {
            int labelIndex = startIndex + i;
    
            // Make sure the label index stays within bounds (0 to 14)
            if (labelIndex >= 0 && labelIndex < totalLabels) {
                labels[labelIndex].setText(String.valueOf(result.charAt(i)));
            }
        }
    }

    private boolean isStarSymbol(char symbol) {
        return symbol == ' ' || symbol == 'x' || symbol == 'y' || symbol == 'z' ||
               symbol == 'a' || symbol == 'b' || symbol == 'c' || symbol == '0' ||
               symbol == '1' || symbol == '$' || symbol == '@';
    }

    @FXML
    void initialize() {
        assert b1 != null : "fx:id=\"b1\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b10 != null : "fx:id=\"b10\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b11 != null : "fx:id=\"b11\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b12 != null : "fx:id=\"b12\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b13 != null : "fx:id=\"b13\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b14 != null : "fx:id=\"b14\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b15 != null : "fx:id=\"b15\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b2 != null : "fx:id=\"b2\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b3 != null : "fx:id=\"b3\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b4 != null : "fx:id=\"b4\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b5 != null : "fx:id=\"b5\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b6 != null : "fx:id=\"b6\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b7 != null : "fx:id=\"b7\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b8 != null : "fx:id=\"b8\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert b9 != null : "fx:id=\"b9\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert backbtn != null : "fx:id=\"backbtn\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert mult2 != null : "fx:id=\"mult2\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert mult3 != null : "fx:id=\"mult3\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert mult5 != null : "fx:id=\"mult5\" was not injected: check your FXML file 'multiplication.fxml'.";
        assert mult6 != null : "fx:id=\"mult6\" was not injected: check your FXML file 'multiplication.fxml'.";
    }
  
}




