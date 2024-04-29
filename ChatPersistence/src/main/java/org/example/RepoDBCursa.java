package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Cursa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class RepoDBCursa implements IRepositoryCursa {

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public RepoDBCursa(Properties props) {
        logger.info("Initializing RepoDBCursa with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void save(Cursa entity) {
        logger.traceEntry("saving a cursa {}", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Curse(destinatia, data_plecare, ora_plecare, nr_locuri) values (?,?,?,?)")) {
            preStmt.setString(1, entity.getDestinatia());
            preStmt.setString(2, entity.getDataPlecare());
            preStmt.setInt(3, entity.getOraPlecare());
            preStmt.setInt(4, entity.getNrLocuri());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Cursa findOneByDestDataOra(String dest, String Data, Integer Ora) {
        logger.traceEntry("finding cursa with destination {}, leaving date {}, leaving time {}", dest, Data, Ora);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from curse where destinatia = ? AND data_plecare = ? AND ora_plecare = ?" )) {
            preStmt.setString(1, dest);
            preStmt.setString(2, Data);
            preStmt.setInt(3, Ora);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Long ID_preluat = result.getLong("id");
                    Integer nr = result.getInt("nr_locuri");

                    //deja avem data si ora ca parametri, nu mai trebuie sa ii luam din baza de date
                    Cursa cursa = new Cursa(dest, Data, Ora, nr);
                    cursa.setId(ID_preluat); //!!!!!!!!!!!!!!!
                    logger.traceExit(cursa);
                    return cursa;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No cursa found with destination, leaving date, leaving time {}", dest);
        return null;
    }

    @Override
    public void delete(Long id) {
        //to do
    }

    @Override
    public void update(Cursa entity) {

    }

    @Override
    public Cursa findOne(Long id) {

        logger.traceEntry("finding cursa with id {}", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from Curse where id=?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Long ID_preluat = result.getLong("id");
                    String dest = result.getString("destinatia");
                    String data_preluata = result.getString("data_plecare");
                    Integer ora_preluata = result.getInt("ora_plecare");
                    Integer nr = result.getInt("nr_locuri");

                    Cursa cursa = new Cursa(dest, data_preluata, ora_preluata, nr);
                    cursa.setId(ID_preluat); //!!!!!!!!!!!!!!!
                    logger.traceExit(cursa);
                    return cursa;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No cursa found with id {}", id);
        return null;
    }

    @Override
    public Iterable<Cursa> findAll()
    {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Set<Cursa> cursa_list = new HashSet<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Curse") )
        {
            try (ResultSet result = preStmt.executeQuery())
            {
                while (result.next())
                {
                    Long ID_preluat = result.getLong("id");
                    String dest = result.getString("destinatia");
                    String data_preluata = result.getString("data_plecare");
                    Integer ora_preluata = result.getInt("ora_plecare");
                    Integer nr = result.getInt("nr_locuri");
                    Cursa cursa = new Cursa(dest, data_preluata, ora_preluata,nr);
                    cursa.setId(ID_preluat);
                    cursa_list.add(cursa);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit(cursa_list);
        return cursa_list;
    }

}
