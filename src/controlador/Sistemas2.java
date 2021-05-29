/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import modelo.Practica_Dao;

/**
 *
 * @author Ricardo
 */
public class Sistemas2 {

    /**
     * @param args the command line arguments
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
        // TODO code application logic here
        //Practica 1
        /*Practica_Dao pr = new Practica_Dao();
        if(pr.ejercicio1()){
            System.out.println("--INFO: operacion realizada.");
        }else{
            System.out.println("--ERROR: lo operacion no se realizo.");

        }
        */
        
        
        //Practica 2
    	
        String localizacionExcel = "resources/SistemasInformacionII (Ultimo).xlsx";
        
        /*
        ExcelIBAN op2 = new ExcelIBAN(localizacionExcel);
        ExcellDNI_Correo op1 = new ExcellDNI_Correo(localizacionExcel);

         
             op2.calculoIBAN();
             op1.leerExcel();
             op1.operacionDni();
             op1.generarEmail();
            
         
    
    */
    
    //Practica 3
    
     //Eliminamos los pdf que habia en nominas
     File directory = new File("resources/nominas/");

        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (!file.delete())
            {
                System.out.println("Failed to delete "+file);
            }
      }
    
    Vaica p3 = new Vaica(localizacionExcel);	
    	
    System.out.println("-----Consola:");
    System.out.println("Introduce un mes y a�o para la generacion de las nominas(mm/aaaa):");
    Scanner sc = new Scanner(System.in);
    String str = sc.nextLine();
    p3.generador(str);
    
    }
}
