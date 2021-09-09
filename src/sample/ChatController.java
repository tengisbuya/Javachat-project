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

public class ChatController implements Initializable{
    public String username;
    public String firstname;
    public String lastname;
    public String user_id;

    // Friendslist variables
    @FXML
    private FlowPane pane_friends_list;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        try {
            System.out.println("----------- Loading users...");
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress =
                    InetAddress.getByName("localhost");
            byte[] sendData;
            byte[] receiveData = new byte[1024];

            // send "friends" packet to server
            String str = "friends?" + user_id;
            sendData = str.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length,
                            IPAddress, 9876);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket =
                    new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String packageString =
                    new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("FROM SERVER: " + packageString);
            clientSocket.close();
            String[] friends = packageString.split("\\?\\?");
            for (int i = 0; i < friends.length; i++) {
                String[] args = friends[i].split("\\?");
                Pane row_pane = new Pane();
                row_pane.setPrefWidth(240);
                row_pane.setStyle("-fx-border-color: black");
                Label lbl = new Label(args[0] + " - " + args[1]);
                row_pane.getChildren().add(lbl);
                pane_friends_list.getChildren().add(row_pane);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
