/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Ricardo
 */
public class Sistemas2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
       
        //Ejercicio 1
        /*
        Practica_Dao pr = new Practica_Dao();
        if(pr.ejercicio1()){
            System.out.println("--INFO: operacion realizada.");
        }else{
            System.out.println("--ERROR: lo operacion no se realizo.");

        }
        */
        //Ejercicio 2
        String localizacionExcel = "resources/SistemasInformacionII.xlsx";
        ExcelIBAN op2 = new ExcelIBAN(localizacionExcel);
        ExecellDNI_Correo op1 = new ExecellDNI_Correo(localizacionExcel);

         try {
             op2.calculoIBAN();
             op1.leerExcel();
             op1.generarEmail();
            
        } catch (Exception ex) {
           System.out.println(ex.getCause());
        
        }

    }
    
}
