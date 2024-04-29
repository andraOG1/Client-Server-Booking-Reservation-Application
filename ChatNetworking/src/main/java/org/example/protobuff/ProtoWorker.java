package org.example.protobuff;

import org.example.*;
import org.example.dto.*;
import org.example.protobuff.TransporturiCurseProto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.List;


public class ProtoWorker implements Runnable, IChatObserver {
    private IChatServices service;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ProtoWorker(IChatServices service, Socket connection) {
        this.service = service;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {

                System.out.println("Waiting requests...");
                TransporturiCurseProto.Request request = TransporturiCurseProto.Request.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                TransporturiCurseProto.Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
            throw new RuntimeException(e);
        }
    }

    //private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private TransporturiCurseProto.Response handleRequest(TransporturiCurseProto.Request request){
        TransporturiCurseProto.Response response=null;
        switch (request.getType())
        {
            case LOGIN:
            {
                System.out.println("Login request");
                User user = ProtoUtils.getUser(request);
                try {
                    service.login(user, this);
                    return ProtoUtils.createOkResponse();
                } catch (ChatException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case LOGOUT:
            {
                System.out.println("Logout request");
                User user = ProtoUtils.getUser(request);
                try
                {
                    service.logout(user, this);
                    connected = false;
                    return ProtoUtils.createOkResponse();
                }
                catch (ChatException e)
                {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GET_ALL_CURSE:
            {
                System.out.println("Get ALL curse request");
                try
                {
                    Iterable<Cursa> curseList= service.getAllCurse();
                    return ProtoUtils.createAllCurseResponse(curseList);
                }
                catch (ChatException e)
                {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case ALL_REZ_BY_CURSA_ID:
            {
                System.out.println("Get ALL rezervari by cursa id request");
                try
                {
                    Cursa cursa = ProtoUtils.getCursa(request);
                    Iterable<Rezervare> rezervareList = service.listaRezByCursa(cursa);
                    return ProtoUtils.createAllRezervariByCursaIdResponse(rezervareList);
                }
                catch (ChatException e)
                {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case SAVE_REZERVARE:
            {
                System.out.println("Save rezervare request");
                Rezervare rezervare = ProtoUtils.getRezervare(request);
                try
                {
                    service.saveRezervare(rezervare);
                    return ProtoUtils.createOkResponse();
                }
                catch (ChatException e)
                {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
        }
        return response;
    }

    private void sendResponse(TransporturiCurseProto.Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }


    @Override
    public void actualizareTable() throws ChatException
    {
        System.out.println("Actualizare tabela rezervari");
        try
        {
            //???
            sendResponse(ProtoUtils.createTabelaRezervariUpdateResponse(null));
        }
        catch (IOException e)
        {
            throw new ChatException("Error sending response: "+e);
        }
        /*
        //Response response = new Response.Builder().type(ResponseType.TABELA_UPDATATA).data(null).build();
        try{
            sendResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
    }
}
