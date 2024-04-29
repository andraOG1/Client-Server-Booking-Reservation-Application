package org.example.controllerFx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.ChatException;
import org.example.IChatServices;
import org.example.ServiceAll;
import org.example.User;
import org.example.controllerFx.FereastraCurseController;
import org.example.controllerFx.alert.LoginActionAlert;

public class LogInController {

    private IChatServices service;
    private FereastraCurseController fereastraCurseController;

    Parent mainParent;
    
    @FXML
    public TextField userNAME;

    @FXML
    public TextField passWORD;
    @FXML
    public Button logIn;

    public void setService(IChatServices s){
        this.service=s;
    }
    
    public void setParent(Parent p){
        this.mainParent=p;
    }

    public void setController(FereastraCurseController u)
    {
        this.fereastraCurseController = u;
    }

    @FXML
    public void BtnLogIn(ActionEvent actionEvent)
    {

        String username = userNAME.getText();
        if(username.isEmpty())
        {
            LoginActionAlert.showMessage(null, Alert.AlertType.ERROR,"Error", "Eroare! Usernameul nu poate sa fie nul!");
            userNAME.clear();
            return;
        }
        String password = passWORD.getText();
        if(password.isEmpty())
        {
            LoginActionAlert.showMessage(null, Alert.AlertType.ERROR,"Error", "Eroare! Parola nu poate sa fie nul!");
            passWORD.clear();
            return;
        }

        User user = new User(username,password);
        try{
            if(user != null)
            {
                try{
                    service.login(user, fereastraCurseController);
                    
                    Stage stage=new Stage();
                    stage.setTitle("User Window for " +user.getUsername());
                    stage.setScene(new Scene(mainParent));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            fereastraCurseController.logout();
                            System.exit(0);
                        }
                    });

                    stage.show();
                    fereastraCurseController.setUser(user);
                    fereastraCurseController.initializeTableCurse();
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

                }   catch (ChatException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("MPP client");
                    alert.setHeaderText("Authentication failure");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        }
        catch (Exception err)
        {
            LoginActionAlert.showMessage(null,Alert.AlertType.ERROR,"Error",err.getMessage());
        }
    }

    public void pressCancel(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setChatController(FereastraCurseController fereastraCurseController) {
        this.fereastraCurseController = fereastraCurseController;
    }

}
