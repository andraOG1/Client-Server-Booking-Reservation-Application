package org.example.utils;

import org.example.IChatServices;
import org.example.protobuff.ProtoWorker;

import java.net.Socket;

public class ProtobuffConcurrentServer extends AbsConcurrentServer{

    private IChatServices server;
    public ProtobuffConcurrentServer(int port, IChatServices Server)
    {
        super(port);
        this.server = Server;
        System.out.println("ProtobufConcurrentServer created");
    }

    @Override
    protected Thread createWorker(Socket client) {

        ProtoWorker worker = new ProtoWorker(server,client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
