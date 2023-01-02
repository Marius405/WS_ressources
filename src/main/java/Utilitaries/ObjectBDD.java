/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilitaries;

import JDBC.Connexion;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author USER
 */
public class ObjectBDD {



    public void ObjectBDD(){

    }
    public void ObjectBDD(int i){
        //this.id=i;
    }
    public int rows_number(ResultSet r) throws SQLException
    {
        try
        {
            r.last();
            int val=r.getRow();
            r.beforeFirst();
            return val;
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void select(Connection conn)throws Exception{

        ObjectBDD val=new ObjectBDD();
        Class classe=this.getClass();
        String table=classe.getSimpleName().toLowerCase();
        Field[] attributs=classe.getDeclaredFields();
        String[] attribut_filtre=new String[attributs.length];

        Constructor constructeur= this.getClass().getConstructor();
        Object resultat=new Object();

        resultat= classe.newInstance();
        int id=0;
        Method meth=this.getClass().getMethod("getId");
        System.out.println(meth.getReturnType().getSimpleName());
        id=(int)meth.invoke(this);
        System.out.println("id "+meth.invoke(resultat));
        String requete= "select * from "+table+" where id="+id;
        System.out.println(requete);
        Statement stmt=conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        ResultSet res=stmt.executeQuery(requete);
        int nb=rows_number(res);

        System.out.println(nb);

        if(nb==1){
            if(res.next())
            {

                resultat= constructeur.newInstance();
                int x=0;
                for(int j=0;j<attributs.length;j++)
                {

                    if(attributs[j].isAnnotationPresent(Colonne.class)){
                        continue;
                    }
                    else{
                        attribut_filtre[j]=attributs[j].getName();
                        String setnm="set"+attribut_filtre[j].substring(0, 1).toUpperCase()+ attribut_filtre[j].substring(1);
                        Method m=this.getClass().getMethod(setnm,attributs[j].getType());

                        System.out.println(setnm);
                        System.out.println(attributs[j].getType().getSimpleName());
                        if(attributs[j].getType().getSimpleName().equals("Integer") || attributs[j].getType().getSimpleName().equals("int") )
                        {
                            m.invoke(this,res.getInt(x+1));
                        }
                        else if(attributs[j].getType().getSimpleName().equals("Double") || attributs[j].getType().getSimpleName().equals("double") )
                        {
                            m.invoke(this,res.getDouble(x+1));
                        }
                        else if(attributs[j].getType().getSimpleName().equals("String") )
                        {
                            m.invoke(this,res.getString(x+1));
                        }
                        System.out.println(setnm);
                    }
                    x=x+1;


                }
                System.out.println(nb);
            }
        }

 
    }
    public void create(Connection conn) throws Exception{
        Class classe=this.getClass();
        String table=classe.getSimpleName().toLowerCase();

        Field[] attributs=classe.getDeclaredFields();
        String[] val=new String[attributs.length];
        System.out.println(attributs.length);

        int nbr=val.length;
        int id=0;
        ArrayList<Integer> nbr_colonne=new ArrayList();

        for(int i=0; i<attributs.length; i++)
        {
            if(attributs[i].isAnnotationPresent(Colonne.class)){
                //val=new String[attributs.length-1];
                nbr=nbr-1;
                nbr_colonne.add(new Integer(i));
                id=i;
                System.out.println(attributs[i].getName());
                continue;
            }
            else{
                String nm=attributs[i].getName();
                String getnm="get"+nm.substring(0, 1).toUpperCase() + nm.substring(1);
                Method m=this.getClass().getMethod(getnm);
                StringBuilder sb = new StringBuilder();
                sb.append(m.invoke(this));
                val[i]="\'"+sb+"\'";
            }

        }
        String[] valf=new String[nbr];
        int indice=0;
        for(int t=0;t<valf.length;t++){
            int x=t;
            if(nbr_colonne.size()!=0){
                if(nbr_colonne.get(indice)!=t){
                    valf[t]=val[x];
                }
                else{
                    x=x+1;
                    indice=indice+1;
                    continue;
                }
            }
            else{
                valf[t]=val[t];
            }

        }
        String resultat=String.join(",",valf);
        System.out.println(resultat);
        String requete="Insert into "+table+" values("+resultat+")";
        System.out.println(requete);
        Statement stmt=conn.createStatement();
        stmt.executeUpdate(requete);

        stmt.close();
    }
    public void delete (Connection conn) throws Exception{
        Class classe=this.getClass();
        String table=classe.getSimpleName().toLowerCase();

        Field[] attributs=classe.getDeclaredFields();
        String[] val=new String[attributs.length];
        System.out.println(attributs.length);

        int nbr=val.length;
        int id=0;
        ArrayList<Integer> nbr_colonne=new ArrayList();

        for(int i=0; i<attributs.length; i++)
        {
            if(attributs[i].isAnnotationPresent(Colonne.class)){
                //val=new String[attributs.length-1];
                nbr=nbr-1;
                nbr_colonne.add(new Integer(i));
                id=i;
                System.out.println(attributs[i].getName());
                continue;
            }
            else{
                String nm=attributs[i].getName();
                String getnm="get"+nm.substring(0, 1).toUpperCase() + nm.substring(1);
                Method m=this.getClass().getMethod(getnm);
                StringBuilder sb = new StringBuilder();
                sb.append(m.invoke(this));
                val[i]=nm+"="+"\'"+sb+"\'";
            }

        }
        String[] valf=new String[nbr];
        int indice=0;
        for(int t=0;t<valf.length;t++){
            int x=t;
            if(nbr_colonne.size()!=0){
                if(nbr_colonne.get(indice)!=t){
                    valf[t]=val[x];
                }
                else{
                    x=x+1;
                    indice=indice+1;
                    continue;
                }
            }
            else{
                valf[t]=val[t];
            }

        }
        String resultat=String.join(",",valf);
        System.out.println(resultat);
        String requete="delete * from "+table+" where "+resultat+")";
        System.out.println(requete);
        Statement stmt=conn.createStatement();
        stmt.executeUpdate(requete);

        stmt.close();
    }
    public void update (Connection conn) throws Exception{
        Class classe=this.getClass();
        String table=classe.getSimpleName().toLowerCase();

        Field[] attributs=classe.getDeclaredFields();
        String[] val=new String[attributs.length];
        System.out.println(attributs.length);

        int nbr=val.length;
        int id=0;
        Method meth=this.getClass().getMethod("getId");
        System.out.println(meth.getReturnType().getSimpleName());
        id=(int)meth.invoke(this);
        ArrayList<Integer> nbr_colonne=new ArrayList();

        for(int i=0; i<attributs.length; i++)
        {
            if(attributs[i].isAnnotationPresent(Colonne.class)){
                //val=new String[attributs.length-1];
                nbr=nbr-1;
                nbr_colonne.add(new Integer(i));
                id=i;
                System.out.println(attributs[i].getName());
                continue;
            }
            else{
                String nm=attributs[i].getName();
                String getnm="get"+nm.substring(0, 1).toUpperCase() + nm.substring(1);
                Method m=this.getClass().getMethod(getnm);
                StringBuilder sb = new StringBuilder();
                sb.append(m.invoke(this));
                val[i]="\'"+sb+"\'";
            }

        }
        String[] valf=new String[nbr];
        int indice=0;
        for(int t=0;t<valf.length;t++){
            int x=t;
            if(nbr_colonne.size()!=0){
                if(nbr_colonne.get(indice)!=t){
                    valf[t]=val[x];
                }
                else{
                    x=x+1;
                    indice=indice+1;
                    continue;
                }
            }
            else{
                valf[t]=val[t];
            }

        }
        String resultat=String.join(",",valf);
        System.out.println(resultat);
        String requete="update "+table+" set "+resultat+" where id="+id;
        System.out.println(requete);
        Statement stmt=conn.createStatement();
        stmt.executeUpdate(requete);

        stmt.close();
    }
    public void create()throws Exception{
        Connection con=null;
        try{
            con=new Connexion().se_connecter();
            this.create(con);
        }catch(Exception e){
            throw e;
        }finally{
            if(con!=null) con.close();
        }
    }
    public void select()throws Exception{
        Connection con=null;
        try{
            con=new Connexion().se_connecter();
            this.select(con);
        }catch(Exception e){
            throw e;
        }finally{
            if(con!=null) con.close();
        }
    }
}
