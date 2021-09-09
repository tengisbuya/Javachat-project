package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.*;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller{
    private String username;
    private String firstname;
    private String lastname;
    private String user_id;

    // Login variables
    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_password;
    @FXML
    private Label txt_login_error_msg;

    // Register variables
    @FXML
    private TextField reg_txt_username;
    @FXML
    private TextField reg_txt_firstname;
    @FXML
    private TextField reg_txt_lastname;
    @FXML
    private TextField reg_txt_password;
    @FXML
    private Label hidden_reg_txt_error;
    @FXML
    private Label hidden_reg_txt_success;

    // Friendslist variables
    @FXML
    private FlowPane pane_friends_list;
    @FXML
    private Label label_test;
    @FXML
    private Pane chat;

    @FXML
    private void redirectRegister(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("register.fxml"))));
    }

    @FXML
    private void redirectLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("sample.fxml"))));
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        System.out.println("------------ Attempting login...");
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress =
                InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        // Splits text with a "?"
        String str = "login?" + txt_username.getText() + "?" + txt_password.getText();
        sendData = str.getBytes();
        DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length,
                        IPAddress, 9876);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket =
                new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String modifiedSentence =
                new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();

        // Handling response from server
        if (modifiedSentence.equals("false")) {
            // If login credentials are wrong, display error
            System.out.println("Login failed, wrong credentials");
            txt_login_error_msg.setVisible(true);
        } else {
            System.out.println("aww shit bruh u got it working");
            // If login is correct, store information and redirect user to friends page.
            String[] strarr = modifiedSentence.split("\\?");
            user_id = strarr[0];
            username = strarr[1];
            firstname = strarr[2];
            lastname = strarr[3];

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("friends.fxml"));
            ChatController clss = fxmlLoader.getClass();
            clss.user_id = user_id;
            clss.username = username;
            clss.firstname = firstname;
            clss.lastname = lastname;
            Pane root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("JavaChat - Chat with Friends!");
            stage.setScene(new Scene(root1));
            stage.show();
            ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) throws IOException {
        System.out.println("----------- Attempting registration...");
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress =
                InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        // Splits text with a "?"
        String str = "reg?" + reg_txt_username.getText() + "?" + reg_txt_firstname.getText() + "?"
                + reg_txt_lastname.getText() + "?" + reg_txt_password.getText();
        sendData = str.getBytes();
        DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length,
                        IPAddress, 9876);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket =
                new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String modifiedSentence =
                new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();

        System.out.println(modifiedSentence);
        if (modifiedSentence.equals("false")){
            hidden_reg_txt_success.setVisible(false);
            hidden_reg_txt_error.setVisible(true);
        } else {
            hidden_reg_txt_success.setVisible(true);
            hidden_reg_txt_error.setVisible(false);
        }
    }

    @FXML
    private void loadFriends(ActionEvent event) throws IOException {
        System.out.println(chat);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("friends.fxml"))));


    }
}
