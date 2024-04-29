package org.example.rpcprotocol;

import org.example.*;
import org.example.dto.CursaDTO;
import org.example.dto.DTOUtils;
import org.example.dto.RezervareDTO;
import org.example.dto.UserDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ChatServicesRpcProxy implements IChatServices {
    private String host;
    private int port;

    private IChatObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ChatServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    @Override
    public void login(User user, IChatObserver client) throws ChatException {
        initializeConnection();
        UserDTO udto= DTOUtils.getDTO(user);
        System.out.println("request sent");
        Request req=new Request.Builder().type(RequestType.LOGIN).data(udto).build();

        sendRequest(req);
        Response response=readResponse();
        System.out.println("request received");
        if (response.type()== ResponseType.OK){

            System.out.println("req type");
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new ChatException(err);
        }
    }

    @Override
    public Iterable<Cursa> getAllCurse() throws ChatException{
        Request request = new Request.Builder().type(RequestType.GET_ALL_CURSE).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new ChatException(err);
        }
        Iterable<Cursa> curse = (Iterable<Cursa>) response.data();
        return curse;
    }

    @Override
    public User findOneUserByUsername(String username) {
        return null;
    }

    @Override
    public Iterable<Rezervare> getAllRezervari() {
        return null;
    }

    @Override
    public void saveRezervare(Object rezervareDTO) throws ChatException {
        RezervareDTO rezDTO = (RezervareDTO) rezervareDTO;
        Request request = new Request.Builder().type(RequestType.SAVE_REZERVARE).data(rezDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new ChatException(err);
        }
    }

    @Override
    public Cursa findByDestDataOra(String dest, String Data, Integer Ora) throws ChatException {
        return null;
    }

    @Override
    public Cursa findOneCursa(Long id) throws ChatException {
        Request request = new Request.Builder().type(RequestType.GET_CURSA).data(id).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new ChatException(err);
        }
        Cursa cursa = (Cursa) response.data();
        return cursa;
    }

    @Override
    public Iterable<Rezervare> listaRezByCursa(Cursa c) throws ChatException{

        CursaDTO cursaDTO = DTOUtils.getDTO(c);
        Request request = new Request.Builder().type(RequestType.ALL_REZ_BY_CURSA_ID).data(cursaDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new ChatException(err);
        }
        Iterable<Rezervare> rezervari = (Iterable<Rezervare>) response.data();
        return rezervari;
    }

    @Override
    public void logout(User user, IChatObserver client) throws ChatException {
        UserDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ChatException(err);
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendRequest(Request request)throws ChatException {
        try{
            synchronized (output) {
                output.writeObject(request);
                output.flush();
            }
        } catch (IOException e) {
            throw new ChatException("Error sendind request " + e);
        }

    }

    private Response readResponse(){
        Response response=null;
        try{
            System.out.println("extracting response");
            response=qresponses.take();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    private void initializeConnection(){
        try{
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private class ReaderThread implements Runnable
    {
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    Response resp = (Response) response;
                    System.out.println("response received "+resp.type());
                    if(isUpdate((Response) response))
                    {
                        handleUpdate((Response) response);
                    }
                    else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } catch (IOException e)
                {
                    throw new RuntimeException(e+ " dsa "+e.getMessage());
                }
                catch (ClassNotFoundException e)
                {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    ///////////////////
    ///PENTRU OBSERVER
    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.TABELA_UPDATATA) {
            try {
                client.actualizareTable();
            } catch (ChatException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.TABELA_UPDATATA;
    }

}
