package Model;

import JDBC.Connexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Admin {
    private int id;
    private String email;
    private String mdp;
    private String nom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double get_montant() throws Exception{
        ArrayList<Historique> liste=this.get_historique();
        Connection con=null;
        Statement stmt=null;
        ResultSet rest=null;
        double val=0;
        try{
            for(int i=0;i< liste.size();i++){
                val=val+liste.get(i).getMontant();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            if(rest!=null) rest.close();
            if(stmt!=null) stmt.close();
            if(con!=null) con.close();
        }
        return val;
    }

    public ArrayList<Historique> get_historique() throws Exception{
        ArrayList<Historique> val=new ArrayList();
        Connection con=null;
        Statement stmt=null;
        ResultSet rest=null;
        try{
            String sql="select * from historique";
            con=new Connexion().se_connecter();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rest =stmt.executeQuery(sql);

            int i=0;
            while(rest.next()){
                Historique v=new Historique();
                v.setId(rest.getInt(1));
                v.setLibelle(rest.getString(2));
                v.setMontant(rest.getFloat(3));
                v.setDate(rest.getDate(4));
                val.add(v);
                i+=1;
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }finally{
            if(rest!=null) rest.close();
            if(stmt!=null) stmt.close();
            if(con!=null) con.close();
        }
        return val;
    }
    public ArrayList<Historique> get_historique(Date d) throws Exception{
        ArrayList<Historique> val=new ArrayList();
        Connection con=null;
        Statement stmt=null;
        ResultSet rest=null;
        try{
            String sql="select * from historique where date= '"+d.toString()+"'";
            con=new Connexion().se_connecter();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rest =stmt.executeQuery(sql);

            int i=0;
            while(rest.next()){
                Historique v=new Historique();
                v.setId(rest.getInt(1));
                v.setLibelle(rest.getString(2));
                v.setMontant(rest.getFloat(3));
                v.setDate(rest.getDate(4));
                val.add(v);
                i+=1;
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }finally{
            if(rest!=null) rest.close();
            if(stmt!=null) stmt.close();
            if(con!=null) con.close();
        }
        return val;
    }
    public void insert_historique(String libelle,int montant)throws Exception{
        Connection con=null;
        Statement stmt=null;

        try{
            String sql="insert into historique(id,libelle,montant) values('"+libelle+"'," +montant+ ")";
            con=new Connexion().se_connecter();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stmt.execute(sql);


        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }finally{
            if(stmt!=null) stmt.close();
            if(con!=null) con.close();
        }
    }
    public boolean is_exist() throws Exception{
        Connection con=null;
        String sql="SELECT * FROM admin where email='"+this.getEmail()+"' and mdp='"+this.getMdp()+"'";
        Statement stmt=null;
        ResultSet res=null;
        try{
            con=new Connexion().se_connecter();
            stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            res =stmt.executeQuery(sql);
            if(res.next()){
                this.setNom(res.getString(4));
                return true;
            }
        }
        catch(Exception e){
            throw new Exception("Inscription invalide: "+e.getMessage());
        }
        finally{
            if (res!=null) res.close();
            if (stmt!=null) stmt.close();
            if (con!=null) con.close();
        }
        return false;
    }

}
