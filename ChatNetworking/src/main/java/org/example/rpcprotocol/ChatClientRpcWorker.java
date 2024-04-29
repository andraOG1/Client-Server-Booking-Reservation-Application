package org.example.rpcprotocol;

import org.example.*;
import org.example.dto.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.List;


public class ChatClientRpcWorker implements Runnable, IChatObserver {
    private IChatServices service;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ChatClientRpcWorker(IChatServices service, Socket connection) {
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
                Object request=input.readObject();
                Request req = (Request) request;
                System.out.println("reading" + req.type());
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
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

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN)
        {
            System.out.println("Login request ..."+request.type());
            UserDTO udto=(UserDTO)request.data();
            User user= DTOUtils.getFromDTO(udto);
            try {
                service.login(user, this);
                return okResponse;
            } catch (ChatException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request" + request.type());
            // LogoutRequest logReq=(LogoutRequest)request;
            UserDTO udto=(UserDTO)request.data();
            User user= DTOUtils.getFromDTO(udto);
            try {
                service.logout(user, this);
                connected=false;
                return okResponse;

            } catch (ChatException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.GET_ALL_CURSE)
        {
            System.out.println("Get ALL curse request" + request.type());
            try{
                Iterable<Cursa> curseList= service.getAllCurse();
                return new Response.Builder().type(ResponseType.GET_CURSE).data(curseList).build();
            } catch (ChatException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.ALL_REZ_BY_CURSA_ID)
        {
            System.out.println("Get ALL rezervari by cursa id request" + request.type());
            try{

                CursaDTO cursaDTO = (CursaDTO) request.data();
                Cursa cursa = DTOUtils.getFromDTO(cursaDTO);
                Iterable<Rezervare> rezervareList = service.listaRezByCursa(cursa);
                return new Response.Builder().type(ResponseType.ALL_REZ_BY_CURSA_ID_RESPONSE).data(rezervareList).build();
            } catch (ChatException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.SAVE_REZERVARE)
        {
            System.out.println("Save rezervare request" + request.type());
            try{
                RezervareDTO rezervareDTO = (RezervareDTO) request.data();
                service.saveRezervare(rezervareDTO);
                return okResponse;
            } catch (ChatException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response.type());
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }


    @Override
    public void actualizareTable() throws ChatException {
        Response response = new Response.Builder().type(ResponseType.TABELA_UPDATATA).data(null).build();
        try{
            sendResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
