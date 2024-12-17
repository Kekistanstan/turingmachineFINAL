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

public class addcontrol {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private char[] tape;
    private int headPosition;

    @FXML
    private ResourceBundle resources;
    
    @FXML
    private Label b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15;

    @FXML
    private URL location;

    @FXML
    private Label addl5, addl6;

    @FXML
    private Button btnback;

    @FXML
    void btnback(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/me/group/cceproject/main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void settape(String data) {
        int tapeSize = 500; // Choose a size that accommodates input and head movements
        tape = new char[tapeSize];
        Arrays.fill(tape, ' '); // Fill the tape with blank spaces

        // Center-align the input data on the tape
        int start = (tapeSize / 2) - (data.length() / 2);
        for (int i = 0; i < data.length(); i++) {
            tape[start + i] = data.charAt(i);
        }

        // Split the input into the two binary numbers
        String[] parts = data.split(" ");
        String firstBinary = parts[0]; // First binary input
        String secondBinary = parts.length > 1 ? parts[1] : ""; // Second binary input (if it exists)

        // Check if the first binary input has less than 2 characters
        if (firstBinary.length() < 2) {
            // Place the head at the start of the first binary input
            headPosition = start; // Position at the start of the first binary number
        } else {
            // Otherwise, place the head 2 positions to the left of the space between the two binary numbers
            int spaceIndex = data.indexOf(" ");
            headPosition = start + spaceIndex - 2;
        }

        // Define the tape display center index
        final int displayCenterIndex = 7; // Center at b8

        // Create an array for labels
        Label[] labels = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15};

        // Reset all labels to empty
        for (Label label : labels) {
            label.setText(" "); // Clear all labels
        }

        // Fill the labels with tape contents around the head position
        for (int i = 0; i < labels.length; i++) {
            int tapeIndex = headPosition - displayCenterIndex + i;
            if (tapeIndex >= 0 && tapeIndex < tape.length) {
                labels[i].setText(String.valueOf(tape[tapeIndex]));
            }
        }

        // Call the Turing Machine addition logic
        performTuringMachineAddition();
    }
    
    private void performTuringMachineAddition() {
        final int[] currentState = {0}; // Start state q0
        final int[] steps = {0}; 
        final boolean[] halt = {false};
        final int[] dir = {1};

        // Using a Thread object to manage the thread state
        Thread turingThread = new Thread(() -> {
            while (!halt[0]) {
                char currentSymbol = tape[headPosition];

                switch (currentState[0]) {
                    case 0: // State q0: Move right to end of the first block
                        if (currentSymbol == '0') {
                            moveHead(1); // Continue moving right
                            currentState[0] = 0; // Stay in q0
                        } else if (currentSymbol == '1') {
                            moveHead(1); // Continue moving right
                            currentState[0] = 0; // Stay in q0
                        } else if (currentSymbol == ' ') {
                            tape[headPosition] = '+'; // Mark end of the first block
                            moveHead(1); // Move to the second block
                            currentState[0] = 1; // Transition to q1
                        } else if (currentSymbol == '+') {
                            tape[headPosition] = '+'; // Mark end of the first block
                            moveHead(1); // Move to the second block
                            currentState[0] = 1; // Transition to q1
                        } else {
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                
                    case 1: // State q1: Move right to end of the second block
                        if (currentSymbol == '0') {
                            moveHead(1); // Continue moving right
                            currentState[0] = 1; // Stay in q1
                        } else if (currentSymbol == '1') {
                            moveHead(1); // Continue moving right
                            currentState[0] = 1; // Stay in q1
                        } else if (currentSymbol == ' ') {
                            tape[headPosition] = ' '; // Mark end of the second block
                            moveHead(-1); // Move left for subtraction
                            currentState[0] = 2; // Transition to q2
                        } else {
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                
                    case 2: // State q2: Subtract one in binary
                        if (currentSymbol == '0') {
                            tape[headPosition] = '1'; // Subtract 1 from 0 (borrow)
                            moveHead(-1); // Move left
                            currentState[0] = 2; // Stay in q2
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '0'; // Subtract 1 from 1 (no borrow)
                            moveHead(-1); // Move left
                            currentState[0] = 3; // Transition to q3
                        } else if (currentSymbol == ' ') {
                            tape[headPosition] = ' ';
                            moveHead(1); // Move right
                            currentState[0] = 5; // Transition to q5 for cleanup
                        } else if (currentSymbol == '+') {
                            tape[headPosition] = ' ';
                            moveHead(1); // Move right
                            currentState[0] = 5; // Transition to q5 for cleanup
                        }
                        steps[0]++;
                        break;
                
                    case 3: // State q3: Move left to the end of the first block
                        if (currentSymbol == '0') {
                            moveHead(-1); // Move left
                            currentState[0] = 3; // Stay in q3
                        } else if (currentSymbol == '1') {
                            moveHead(-1); // Move left
                            currentState[0] = 3; // Stay in q3
                        } else if (currentSymbol == '+') {
                            tape[headPosition] = '+'; // Clear marker
                            moveHead(-1); // Move left to prepare for addition
                            currentState[0] = 4; // Transition to q4
                        }
                        steps[0]++;
                        break;
                
                    case 4: // State q4: Add one in binary
                        if (currentSymbol == '0') {
                            tape[headPosition] = '1'; // Add 1 to 0
                            moveHead(1); // Move right
                            currentState[0] = 0; // Transition back to q0
                        } else if (currentSymbol == '1') {
                            tape[headPosition] = '0'; // Add 1 to 1 (carry)
                            moveHead(-1); // Move left
                            currentState[0] = 4; // Stay in q4 to handle carry
                        } else if (currentSymbol == ' ') {
                            tape[headPosition] = '1'; // Add carry to blank space
                            moveHead(1); // Move right
                            currentState[0] = 0; // Transition back to q0
                        } else {
                            halt[0] = true;
                        }
                        steps[0]++;
                        break;
                
                    case 5: // State q5: Cleanup
                        if (currentSymbol == '1') {
                            tape[headPosition] = ' '; // Clear marker
                            moveHead(1); // Move head to the right
                            currentState[0] = 5; // Stay in q5
                            steps[0]++;
                        } else if (currentSymbol == ' ') {
                            currentState[0] = 6; // Stay in q5
                            steps[0]++;
                        } 
                        break;

                    case 6:
                        if(currentSymbol == ' '){
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState [0] = 6;
                            steps[0]++;
                        } else if(currentSymbol == '+'){
                            tape[headPosition] = ' ';
                            moveHead(-1);
                            currentState[0] = 7;
                        } else if(currentSymbol == '0' || currentSymbol == '1'){
                            moveHead(-3);
                            currentState[0] = 7;
                        } 

                        break;

                    case 7: //handles halting the turing machine
                        halt[0] = true; // Halt the Turing machine
                        break;
                    
                        /* 
                    case 8: //handles wrong input
                        halt[0] = true;
                        break;
                        */
                }

                Platform.runLater(() -> {
                    updateDisplayedTape();
                    if (currentState[0] == 7){
                        Platform.runLater(() -> addl5.setText("State: Halt"));
                        centerResult();
                    } else if(currentState[0] == 8){
                        Platform.runLater(() -> addl5.setText("State: Halt | Rejected"));
                    } else{
                        addl5.setText("State: q" + currentState[0]);
                    }
                    if (!halt[0]) {
                        addl6.setText("Steps: " + steps[0]);
                    }
                });

                // Pause for visualization
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
    
    //handles the overflow of character-array
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

    //Centers the output in the display
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


    @FXML
    void initialize() {
        assert b1 != null : "fx:id=\"b1\" was not injected: check your FXML file 'add.fxml'.";
        assert b10 != null : "fx:id=\"b10\" was not injected: check your FXML file 'add.fxml'.";
        assert b11 != null : "fx:id=\"b11\" was not injected: check your FXML file 'add.fxml'.";
        assert b12 != null : "fx:id=\"b12\" was not injected: check your FXML file 'add.fxml'.";
        assert b13 != null : "fx:id=\"b13\" was not injected: check your FXML file 'add.fxml'.";
        assert b14 != null : "fx:id=\"b14\" was not injected: check your FXML file 'add.fxml'.";
        assert b15 != null : "fx:id=\"b15\" was not injected: check your FXML file 'add.fxml'.";
        assert b2 != null : "fx:id=\"b2\" was not injected: check your FXML file 'add.fxml'.";
        assert b3 != null : "fx:id=\"b3\" was not injected: check your FXML file 'add.fxml'.";
        assert b4 != null : "fx:id=\"b4\" was not injected: check your FXML file 'add.fxml'.";
        assert b5 != null : "fx:id=\"b5\" was not injected: check your FXML file 'add.fxml'.";
        assert b6 != null : "fx:id=\"b6\" was not injected: check your FXML file 'add.fxml'.";
        assert b7 != null : "fx:id=\"b7\" was not injected: check your FXML file 'add.fxml'.";
        assert b8 != null : "fx:id=\"b8\" was not injected: check your FXML file 'add.fxml'.";
        assert b9 != null : "fx:id=\"b9\" was not injected: check your FXML file 'add.fxml'.";
        assert addl5 != null : "fx:id=\"addl5\" was not injected: check your FXML file 'add.fxml'.";
        assert addl6 != null : "fx:id=\"addl6\" was not injected: check your FXML file 'add.fxml'.";
        assert btnback != null : "fx:id=\"btnback\" was not injected: check your FXML file 'add.fxml'.";
    }

}
