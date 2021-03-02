/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemas2_2021;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import modelo.HibernateUtil;
import modelo.Nomina;
import modelo.Trabajadorbbdd;
import modelo.Empresas;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Juanc
 */
public class Sistemas2_2021 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SessionFactory sf = null;
        Session sesion = null;
        Transaction tx = null;
        int empresa = 0;

        try{
            Scanner lector = new Scanner(System.in);
            String DNI = lector.nextLine();
            
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            
            String consultaHQL = "FROM Trabajadorbbdd t WHERE t.nifnie = :param1";
            Query query = sesion.createQuery(consultaHQL);
            query.setParameter("param1", DNI);
            List<Trabajadorbbdd> listaResultado = query.list();
            int numeroNomina = 1;
            
            /////////////////////////////////Apartado 1 
            for(Trabajadorbbdd tbd:listaResultado)
            {   
                System.out.println("Nombre: " + tbd.getNombre());
                System.out.println("Primer Apellido: " + tbd.getApellido1());
                System.out.println("Segundo Apellido: " + tbd.getApellido2());
                System.out.println("NIF: " + tbd.getNifnie());
                System.out.println("Categoria: " + tbd.getCategorias().getNombreCategoria());
                System.out.println("Empresa: " + tbd.getEmpresas().getNombre());
                empresa = tbd.getEmpresas().getIdEmpresa();
                System.out.println("Fecha: " + tbd.getFechaAlta());
                
                for (Iterator<Nomina> it = tbd.getNominas().iterator(); it.hasNext(); ) {
                    Nomina n = it.next();
                    if (n.getTrabajadorbbdd().equals(tbd))
                    {
                        System.out.println("Nomina " + numeroNomina + ": " + n.getBrutoNomina());
                        numeroNomina++;
                    }
                }
                
                System.out.println("**************************************");
            }
            
            /////////////////////////////////Apartado 2
            consultaHQL = "FROM Empresas e";
            query = sesion.createQuery(consultaHQL);
            List<Empresas> listaResultado2 = query.list();
            if(empresa != 0){
            for(Empresas et:listaResultado2){
                  
                  if(! (empresa == et.getIdEmpresa()) ){
                      tx=sesion.beginTransaction();
                      String nombreSin =et.getNombre();
                      String aux = "";
                      aux = et.getNombre();
                      aux = aux + "2021";
                      et.setNombre(aux);
                      sesion.saveOrUpdate(et);
                      tx.commit();
                      System.out.println("Empresa "+nombreSin+" actualizada a "+aux);
                  }

              }
            }
            if(listaResultado.size() == 0)
            {
                System.out.println("No existe ningun trabajador con ese DNI");
            }
            
            
            HibernateUtil.shutdown();
         
            
            
        }catch(Exception e){
            
            System.out.println("Ha ocurrido un error: " + e.getMessage());
            
        }
    }
    
}
