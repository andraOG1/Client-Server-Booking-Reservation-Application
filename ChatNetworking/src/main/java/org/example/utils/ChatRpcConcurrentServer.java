package org.example.utils;

import org.example.rpcprotocol.ChatClientRpcWorker;
import org.example.IChatServices;

import java.net.Socket;


public class ChatRpcConcurrentServer extends AbsConcurrentServer {
    private IChatServices chatServer;
    public ChatRpcConcurrentServer(int port, IChatServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("rpc server");
    }

    @Override
    protected Thread createWorker(Socket client) {

        ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
