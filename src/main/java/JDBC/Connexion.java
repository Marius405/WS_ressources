package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connexion {
    private String host="snuffleupagus.db.elephantsql.com";

    private String dbname="tkuzylyb";
    private String user="tkuzylyb";
    private String mdp="JVYt4omM8FN8qF59-sZNtcDdjnk8NTUR";
    public Connection se_connecter()throws Exception{
        try{
            Class.forName("org.postgresql.Driver");
            Connection connect = DriverManager.getConnection("jdbc:postgresql://"+host+"/"+dbname,user,mdp);
            return connect;
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception("il y a un probleme avec la connection, verifier les donnees de connection");
        }
    }
}
