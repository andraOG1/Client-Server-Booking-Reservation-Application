package org.example;

import org.example.utils.AbstractServer;
import org.example.utils.ChatRpcConcurrentServer;
import org.example.utils.ProtobuffConcurrentServer;
import org.example.utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffServer {

    private static int defaultPort=55555;
    public static void main(String[] args) {

        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/chatserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        RepoDBUser userRepo=new RepoDBUser(serverProps);
        RepoDBCursa cursaRepo = new RepoDBCursa(serverProps);
        RepoDBRezervare rezervareRepo = new RepoDBRezervare(serverProps);
        IChatServices chatServerImpl=new ServiceAll(userRepo,cursaRepo,rezervareRepo);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new ProtobuffConcurrentServer(chatServerPort, chatServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
