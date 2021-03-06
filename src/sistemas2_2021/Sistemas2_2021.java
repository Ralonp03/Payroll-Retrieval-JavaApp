/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemas2_2021;

import java.util.List;
import modelo.HibernateUtil;
import modelo.Trabajadorbbdd;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Ricardo
 */
public class Sistemas2_2021 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        SessionFactory sf = null;
        Session session = null;
        Transaction tx = null;
                     
        
        
        
        try{
            
          sf = HibernateUtil.getSessionFactory();
          session = sf.openSession();
          String consultaHQL = "FROM Trabajadorbbdd t";
          Query query = session.createQuery(consultaHQL);
          List<Trabajadorbbdd> listaTrabajadores = query.list();
          for(Trabajadorbbdd bbdd:listaTrabajadores){
              System.out.println("nombre: "+bbdd.getNombre());
              System.out.println("nombre: "+bbdd.getApellido1());
              System.out.println("-----------------");
          }
          HibernateUtil.shutdown();
          
          
          
        }catch(Exception e){
            
        }
        
        
        
    }
    
}
