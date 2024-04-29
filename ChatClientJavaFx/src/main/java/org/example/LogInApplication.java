package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllerFx.FereastraCurseController;
import org.example.controllerFx.LogInController;
import org.example.rpcprotocol.ChatServicesRpcProxy;

import java.io.IOException;
import java.util.Properties;

public class LogInApplication extends Application {

    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(LogInApplication.class.getResourceAsStream("/chatclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IChatServices service = new ChatServicesRpcProxy(serverIP, serverPort);
        FXMLLoader loader = new FXMLLoader(LogInApplication.class.getResource("/LogIn.fxml"));
        Parent root = loader.load();
        LogInController loginController = loader.getController();
        loginController.setService(service);

        FXMLLoader clientLoader = new FXMLLoader(LogInApplication.class.getResource("/fereastraCurse.fxml"));
        Parent clientRoot = clientLoader.load();
        FereastraCurseController userController = clientLoader.getController();
        userController.setService(service);

        loginController.setController(userController);
        loginController.setParent(clientRoot);
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root,440,400));

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

}
