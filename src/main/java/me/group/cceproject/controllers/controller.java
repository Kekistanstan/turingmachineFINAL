package me.group.cceproject.controllers;

import me.group.cceproject.controllers.*;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button addbtn;

    @FXML
    private Button multiplybtn;

    @FXML
    private TextField tf1;

    @FXML
    void addhandle(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/group/cceproject/add.fxml"));
        root  = loader.load();

        //Set the tape of turing machine
        addcontrol controladd = loader.getController();
        controladd.settape(tf1.getText());

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void multiplicationhandle(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/group/cceproject/multiplication.fxml"));
        root  = loader.load();

        //Set the tape of turing machine
        multiplicationcontroller controlmultiply = loader.getController();
        controlmultiply.settape(tf1.getText());

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
