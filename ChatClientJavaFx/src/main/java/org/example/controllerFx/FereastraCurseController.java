package org.example.controllerFx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.*;
import org.example.controllerFx.alert.LoginActionAlert;
import org.example.dto.RezervareDTO;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class FereastraCurseController implements Initializable, IChatObserver {

    private IChatServices serviceAll;

    public void setService(IChatServices service) {
        this.serviceAll = service;

    }

    public void setUserTaskService(IChatServices serviceAll)
    {
        this.serviceAll = serviceAll;
        initializeTableCurse();
    }
    ///////////////////////////////////////
    //AFIS TOATE CURSELE
    @FXML
    public TableView<Cursa> tableCurse;
    @FXML
    public TableColumn<Cursa,String> tableColumnDest;
    @FXML
    public TableColumn<Cursa, LocalDateTime> tableColumnData;
    @FXML
    public TableColumn<Cursa, LocalDateTime> tableColumnOra;
    @FXML
    public TableColumn<Cursa, Integer> tableColumnNrLocuri;
    ObservableList<Cursa> modelCurse = FXCollections.observableArrayList();
    ////////////////////////////////////////
    //Cautare o cursa + Tabel locuri rezervari
    @FXML
    public TextField dataa;
    @FXML
    public TextField ora;
    @FXML
    public TextField dest;

    @FXML
    public TableView<MIXT> tableRezervari;
    @FXML
    public TableColumn<MIXT,String> tableColumnNume;
    @FXML
    public TableColumn<MIXT,Integer>tableColumnNr;
    ObservableList<MIXT> modelRezervari = FXCollections.observableArrayList();
    @FXML
    public Button cautare;
    ///////////////////////////////////////
    //REZERVARE LOCURI
    @FXML
    public TextField numeCl;
    @FXML
    public TextField nrLocuri;
    @FXML
    public TextField idCursa;
    @FXML
    public Button Rezervare;
    @FXML
    public Button LogOut;


    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableColumnDest.setCellValueFactory(new PropertyValueFactory<Cursa,String>("destinatia"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<Cursa,LocalDateTime>("dataPlecare"));
        tableColumnOra.setCellValueFactory(new PropertyValueFactory<Cursa,LocalDateTime>("oraPlecare"));
        tableColumnNrLocuri.setCellValueFactory(new PropertyValueFactory<Cursa,Integer>("nrLocuri"));
        tableCurse.setItems(modelCurse);

        tableColumnNr.setCellValueFactory(new PropertyValueFactory<MIXT,Integer>("nrLoc"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<MIXT,String>("numeClient"));
        tableRezervari.setItems(modelRezervari);
    }


    public void initializeTableCurse()
    {
        modelCurse.clear();
        Set<Cursa> curse = null;
        try {
            curse = (Set<Cursa>) serviceAll.getAllCurse();
        } catch (ChatException e) {
            throw new RuntimeException(e);
        }

        modelCurse.addAll(curse);
    }



    public void btnCautare()
    {
        try{

            String destinatie = dest.getText();
            String dataPl = dataa.getText();
            Integer oraPl = Integer.valueOf(ora.getText());
            Cursa cursaProba = new Cursa(destinatie,dataPl,oraPl,18);
            Set<Rezervare> rezervari = (Set<org.example.Rezervare>) serviceAll.listaRezByCursa(cursaProba);
            Set<MIXT> mixt_list = new HashSet<>();

            int PrimulLocGol=1;
            for(Rezervare rez : rezervari)
            {
                int cont = rez.getNrLocuri();
                String nume = rez.getNumeClient();
                while (cont>0)
                {
                    MIXT mixt = new MIXT(PrimulLocGol,nume);
                    cont--;
                    mixt_list.add(mixt);
                    PrimulLocGol += 1;
                }
            }
            for(int i=PrimulLocGol; i<=18; i++)
            {
                MIXT mixt = new MIXT(i,"-");
                mixt_list.add(mixt);
            }

            modelRezervari.setAll(mixt_list);

        }
        catch (Exception e)
        {
            LoginActionAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
        }
    }

    public void logout()
    {
        try {
            serviceAll.logout(user, this);
        } catch (ChatException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleLogout() {

        logout();
        Node src = LogOut;
        Stage stage = (Stage) src.getScene().getWindow();
        stage.close();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //partea cu OBSERVER la adaugarea unei rezervari in tabel

    public void addRezervare()
    {
        try {
            String numeClient = numeCl.getText();
            Integer nr_Locuri = Integer.valueOf(nrLocuri.getText());

            Cursa c = tableCurse.getSelectionModel().getSelectedItem();

            RezervareDTO rezervareDTO = new RezervareDTO(numeClient,nr_Locuri,c.getId());
            try{
                saveReservationDTO(rezervareDTO);
            }
            catch (ChatException e) {
              throw new RuntimeException(e);
            }

            //numeCl.clear();
            //nrLocuri.clear();
            //idCursa.clear();
        }
        catch (Exception e)
        {
            LoginActionAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
        }
    }

    //PT A ADAUGA O REZERVARE DE TIPUL DTO
    private void saveReservationDTO(RezervareDTO rezervareDTO) throws ChatException {
        serviceAll.saveRezervare(rezervareDTO);
    }

    @Override
    public void actualizareTable() throws ChatException {

        Platform.runLater(() -> {
            System.out.println("AICI AJUNG");
            Set<Rezervare> rezervari = null;
            try {
                Cursa c = tableCurse.getSelectionModel().getSelectedItem();
                rezervari = (Set<org.example.Rezervare>) serviceAll.listaRezByCursa(c);

                System.out.println("lista rezervari");
            } catch (ChatException e) {
                throw new RuntimeException(e);
            }
            Set<MIXT> mixt_list = new HashSet<>();

            int PrimulLocGol=1;
            for(Rezervare rez : rezervari)
            {
                int cont = rez.getNrLocuri();
                String nume = rez.getNumeClient();
                while (cont>0)
                {
                    System.out.println("fac obiecte mixte");
                    MIXT mixt = new MIXT(PrimulLocGol,nume);
                    cont--;
                    mixt_list.add(mixt);
                    PrimulLocGol += 1;
                }
            }
            for(int i=PrimulLocGol; i<=18; i++)
            {
                MIXT mixt = new MIXT(i,"-");
                mixt_list.add(mixt);
            }

            System.out.println("la final model");
            modelRezervari.setAll(mixt_list);
            System.out.println("gata functia");
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////
}
