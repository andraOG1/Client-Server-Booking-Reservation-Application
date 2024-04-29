package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoDBUser implements IRepositoryUser{

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public RepoDBUser(Properties props)
    {
        logger.info("Initializing RepoDBUser with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }



    @Override
    public User findOneByUsername(String username)
    {
        logger.traceEntry("finding user with username {}", username);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Users where username=?"))
        {
            preStmt.setString(1,username);
            try(ResultSet result = preStmt.executeQuery())
            {
                if(result.next())
                {
                    Long ID_preluat = result.getLong("id");
                    String username_preluat = result.getString("username");
                    String password = result.getString("password");


                    User user = new User(username_preluat,password);
                    user.setId(ID_preluat); //!!!!!!!!!!!!!!!
                    logger.traceExit(user);
                    return user;
                }
            }
        }
        catch (SQLException ex)
        {
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No user found with username {}", username);
        return null;
    }

    @Override
    public void save(User entity) {
        logger.traceEntry("saving a user {}", entity);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("insert into Users(username, password) values (?,?)"))
        {
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());

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
        //to do
    }

    @Override
    public void update(User entity) {
        //to do
    }

    @Override
    public User findOne(Long id) {

        logger.traceEntry("finding user with id {}", id);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("select * from Users where id=?"))
        {
            preStmt.setLong(1,id);
            try(ResultSet result = preStmt.executeQuery())
            {
                if(result.next())
                {
                    Long ID_preluat = result.getLong("id");
                    String username = result.getString("username");
                    String password = result.getString("password");


                    User user = new User(username,password);
                    user.setId(ID_preluat); //!!!!!!!!!!!!!!!
                    logger.traceExit(user);
                    return user;
                }
            }
        }
        catch (SQLException ex)
        {
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No user found with id {}", id);
        return null;
    }

    @Override
    public Iterable<User> findAll()
    {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users_list = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Users"))
        {
            try (ResultSet result = preStmt.executeQuery())
            {
                while (result.next())
                {
                    Long ID_preluat = result.getLong("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username,password);
                    user.setId(ID_preluat);
                    users_list.add(user);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit(users_list);
        return users_list;
    }
}
