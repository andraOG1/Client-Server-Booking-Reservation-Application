package org.example.protobuff;

//import chat.model.Message;
import org.example.ChatException;
import org.example.Cursa;
import org.example.Rezervare;
import org.example.User;
import org.example.rpcprotocol.Response;
import org.example.rpcprotocol.ResponseType;

import java.util.List;


public class ProtoUtils {
    public static TransporturiCurseProto.Request createLoginRequest(User user){
        TransporturiCurseProto.User userDTO=TransporturiCurseProto.User.newBuilder().setUsername(user.getUsername()).setPassword(user.getPassword()).build();
        TransporturiCurseProto.Request request= TransporturiCurseProto.Request.newBuilder().setType(TransporturiCurseProto.Request.Type.LOGIN)
                .setUser(userDTO).build();
        return request;
    }
    public static TransporturiCurseProto.Request createLogoutRequest(User user){
        TransporturiCurseProto.User userDTO=TransporturiCurseProto.User.newBuilder().setUsername(user.getUsername()).build();
        TransporturiCurseProto.Request request= TransporturiCurseProto.Request.newBuilder().setType(TransporturiCurseProto.Request.Type.LOGOUT)
                .setUser(userDTO).build();
        return request;
    }

    public static TransporturiCurseProto.Request createRezervareRequest(Rezervare rezervare)
    {
        TransporturiCurseProto.Rezervare rezervareDTO=TransporturiCurseProto.Rezervare.newBuilder().setNumeClient(rezervare.getNumeClient()).setNrLocuri(rezervare.getNrLocuri()).setIdCursa(rezervare.getIdCursa()).build();
        TransporturiCurseProto.Request request= TransporturiCurseProto.Request.newBuilder().setType(TransporturiCurseProto.Request.Type.SAVE_REZERVARE)
                .setRezervare(rezervareDTO).build();
        return request;
    }

    public static TransporturiCurseProto.Response createOkResponse(){
        TransporturiCurseProto.Response response=TransporturiCurseProto.Response.newBuilder()
                .setType(TransporturiCurseProto.Response.Type.OK).build();
        return response;
    }

    public static TransporturiCurseProto.Response createErrorResponse(String text){
        TransporturiCurseProto.Response response=TransporturiCurseProto.Response.newBuilder()
                .setType(TransporturiCurseProto.Response.Type.ERROR).build();
        return response;
    }

    //response pentru o lista de curse
    public static TransporturiCurseProto.Response createAllCurseResponse(Iterable<Cursa> curse)
    {
        TransporturiCurseProto.Response.Builder response=TransporturiCurseProto.Response.newBuilder()
                .setType(TransporturiCurseProto.Response.Type.GET_CURSE);

        for (Cursa cursa: curse){
            TransporturiCurseProto.Cursa cursaDTO=TransporturiCurseProto.Cursa.newBuilder().setDestinatia(cursa.getDestinatia()).setDataPlecare(cursa.getDataPlecare()).setOraPlecare(cursa.getOraPlecare()).setNrLocuri(cursa.getNrLocuri()).build();
            response.addCurseList(cursaDTO);
        }
        return response.build();
    }

    //response pentru o lista de rezervari in functie de id ul unei curse
    public static TransporturiCurseProto.Response createAllRezervariByCursaIdResponse(Iterable<Rezervare> rezervari)
    {
        TransporturiCurseProto.Response.Builder response=TransporturiCurseProto.Response.newBuilder()
                .setType(TransporturiCurseProto.Response.Type.ALL_REZ_BY_CURSA_ID_RESPONSE);

        for (Rezervare rez : rezervari)
        {
            TransporturiCurseProto.Rezervare rezervareDTO = TransporturiCurseProto.Rezervare.newBuilder().setNumeClient(rez.getNumeClient()).setNrLocuri(rez.getNrLocuri()).setIdCursa(rez.getIdCursa()).build();
            response.addRezervareList(rezervareDTO);
        }
        return response.build();
    }

    public static Cursa getCursa(TransporturiCurseProto.Request request)
    {
        TransporturiCurseProto.Cursa cursaDTO=request.getCursa();
        Cursa cursa=new Cursa(cursaDTO.getDestinatia(),cursaDTO.getDataPlecare(),cursaDTO.getOraPlecare(),cursaDTO.getNrLocuri());
        cursa.setId(cursaDTO.getId());
        return cursa;
    }

    public static User getUser(TransporturiCurseProto.Request request)
    {
        TransporturiCurseProto.User userDTO=request.getUser();
        User user=new User(userDTO.getUsername(),userDTO.getPassword());
        user.setId(userDTO.getId());
        return user;
    }

    //response ca sa salvez o rezervare
    public static Rezervare getRezervare(TransporturiCurseProto.Request request)
    {
        TransporturiCurseProto.Rezervare rezervareDTO=request.getRezervare();
        Rezervare rezervare=new Rezervare(rezervareDTO.getNumeClient(),rezervareDTO.getNrLocuri(),rezervareDTO.getIdCursa());
        return rezervare;
    }

    //////////////PENTRU OBSERVER
    public static TransporturiCurseProto.Response createTabelaRezervariUpdateResponse(Iterable<Rezervare> rezervari)
    {
        TransporturiCurseProto.Response.Builder response = TransporturiCurseProto.Response.newBuilder().
                setType(TransporturiCurseProto.Response.Type.TABELA_UPDATATA);

        for( Rezervare rezervare : rezervari)
        {
            TransporturiCurseProto.Rezervare rezDTO = TransporturiCurseProto.Rezervare.newBuilder().setNumeClient(rezervare.getNumeClient()).setNrLocuri(rezervare.getNrLocuri()).setIdCursa(rezervare.getIdCursa()).build();
            response.addRezervareList(rezDTO);
        }
        return response.build();
    }
}
