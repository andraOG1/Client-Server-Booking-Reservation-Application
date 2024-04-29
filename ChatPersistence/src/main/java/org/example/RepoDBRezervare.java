package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Rezervare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class RepoDBRezervare implements IRepositoryRezervare{

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public RepoDBRezervare(Properties props)
    {
        logger.info("Initializing RepoDBRezervare with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public void save(Rezervare entity)
    {
        logger.traceEntry("saving a reservation {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("insert into Rezervari(nume_client, nr_locuri,id_cursa) values (?,?,?)"))
        {
            preStmt.setString(1, entity.getNumeClient());
            preStmt.setInt(2, entity.getNrLocuri());
            preStmt.setLong(3, entity.getIdCursa());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);


        }
        catch (SQLException ex)
        {
            logger.error(ex);
            System.out.println("Error DB "+ ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Rezervare entity) {

    }

    @Override
    public Rezervare findOne(Long id) {
        return null;
    }

    @Override
    public Rezervare findOneByNumeNrLocuri(String nume, Integer nrLocuri) {
        logger.traceEntry("finding reservation with name {} and seats number {}", nume, nrLocuri);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Rezervari where nume_client=? AND nr_locuri=? " ))
        {
            preStmt.setString(1,nume);
            preStmt.setInt(2,nrLocuri);
            try(ResultSet result = preStmt.executeQuery())
            {
                if(result.next())
                {
                    Long ID_preluat = result.getLong("id");
                    String nume_preluat = result.getString("nume_client");
                    Integer nr_locuri_preluate = result.getInt("nr_locuri");
                    Long id_cursa = result.getLong("id_cursa");

                    Rezervare rezervare = new Rezervare(nume_preluat,nr_locuri_preluate,id_cursa);
                    rezervare.setId(ID_preluat); //!!!!!!!!!!!!!!!
                    logger.traceExit(rezervare);
                    return rezervare;
                }
            }
        }
        catch (SQLException ex)
        {
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No reservation found with name {}", nume);
        return null;

    }

    @Override
    public Iterable<Rezervare> findAll()
    {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Set<Rezervare> rezervari_lista =new HashSet<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Rezervari"))
        {
            try(ResultSet result=preStmt.executeQuery())
            {
                while (result.next())
                {
                    Long ID_preluat = result.getLong("id");
                    String nume_cl = result.getString("nume_client");
                    Integer nr_locuri_rezervate = result.getInt("nr_locuri");
                    Long id_cursa = result.getLong("id_cursa");
                    Rezervare rezervare = new Rezervare(nume_cl,nr_locuri_rezervate,id_cursa);
                    rezervare.setId(ID_preluat);
                    rezervari_lista.add(rezervare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(rezervari_lista);
        return rezervari_lista;
    }

    @Override
    public Iterable<Rezervare> findAllByCursa(Long id)
    {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Set<Rezervare> rezervari_lista =new HashSet<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Rezervari R inner join Curse c ON R.id_cursa = C.id" +
                " where C. id=?"))
        {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long ID_preluat = result.getLong("id");
                    String numeCl = result.getString("nume_client");
                    Integer nr_locuri = result.getInt("nr_locuri");
                    Long id_cursa = Long.valueOf(result.getInt("id_cursa"));

                    Rezervare rezervare = new Rezervare(numeCl,nr_locuri,id_cursa);
                    rezervare.setId(ID_preluat); //!!!!!!!!!!!!!!!
                    rezervari_lista.add(rezervare);
                    logger.traceExit(rezervare);

                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No cursa found with id {}", id);
        return rezervari_lista;
    }
}
