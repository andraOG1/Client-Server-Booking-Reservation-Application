package org.example;

import org.example.dto.RezervareDTO;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceAll implements IChatServices {

    private IRepositoryUser repoUser;
    private IRepositoryCursa repoCursa;
    private IRepositoryRezervare repoRezervare;

    private Map<String, IChatObserver> loggedClients;

    public ServiceAll(IRepositoryUser repoUser, IRepositoryCursa repoCursa, IRepositoryRezervare repoRezervare) {
        this.repoUser = repoUser;
        this.repoCursa = repoCursa;
        this.repoRezervare = repoRezervare;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Iterable<Rezervare> listaRezByCursa(Cursa c)
    {
        Cursa cursa = repoCursa.findOneByDestDataOra(c.getDestinatia(), c.getDataPlecare(), c.getOraPlecare());
        Set<Rezervare> rezervari = null;
        rezervari = (Set<Rezervare>) repoRezervare.findAllByCursa(cursa.getId());
        return rezervari;
    }
    @Override
    public synchronized User findOneUserByUsername(String username)
    {
        return repoUser.findOneByUsername(username);
    }

    @Override
    public synchronized Iterable<Cursa> getAllCurse()
    {
        return repoCursa.findAll();
    }

    @Override
    public synchronized Cursa findByDestDataOra(String dest, String Data, Integer Ora)
    {
        return repoCursa.findOneByDestDataOra(dest, Data, Ora);
    }

    @Override
    public synchronized Iterable<Rezervare> getAllRezervari()
    {
        return repoRezervare.findAll();
    }

//    @Override
//    public Iterable<Rezervare> getAllRezervariByCursa(Long id) throws ChatException
//    {
//        return repoRezervare.findAllByCursa(id);
//    }


    ///////////////////////////////////////////////////////////////////////////////////
    // PARTEA CU OBSERVER PT ADAUGARE REZERVARI IN TABEL
    @Override
    public synchronized void saveRezervare(Object rezervare) throws ChatException
    {
        //transformam din DTO in Rezervare simpla
        RezervareDTO rezervareDTO = (RezervareDTO) rezervare;
        //Rezervare rez = repoRezervare.findOneByNumeNrLocuri(rezervareDTO.getNumeClient(), rezervareDTO.getNrLocuri());
        Rezervare rez  = new Rezervare(rezervareDTO.getNumeClient(), rezervareDTO.getNrLocuri(),rezervareDTO.getIdCursa());
        repoRezervare.save(rez);
        notifyTablesChange();
    }

    private final int defaultThreadsNo = 1;
    private void notifyTablesChange() {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(var v:loggedClients.values())
        {
            executor.execute(()->{
                try{
                    System.out.println("Notifying");
                    v.actualizareTable();
                }
                catch (ChatException e)
                {
                    System.out.println("Error notifying");
                }
            });
        }
        executor.shutdown();
    }

    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public synchronized Cursa findOneCursa(Long id)  throws ChatException{
        return repoCursa.findOne(id);
    }

    @Override
    public synchronized void login(User user, IChatObserver client) throws ChatException {
        User userR= repoUser.findOneByUsername(user.getUsername());
        if (userR!=null){
            if(loggedClients.get(user.getUsername())!=null)
                throw new ChatException("User already logged in.");
            loggedClients.put(user.getUsername(), client);
        }else
            throw new ChatException("Authentication failed.");

    }

    @Override
    public synchronized void logout(User user, IChatObserver client) throws ChatException {
        IChatObserver localClient=loggedClients.remove(user.getUsername());
        if (localClient==null)
            throw new ChatException("User "+user.getUsername()+" is not logged in.");
    }

}
