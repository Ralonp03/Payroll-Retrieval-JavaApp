/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import modelo.Practica_Dao;

/**
 *
 * @author Ricardo
 */
public class Sistemas2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Practica_Dao pr = new Practica_Dao();
        if(pr.ejercicio1()){
            System.out.println("--INFO: operacion realizada.");
        }else{
            System.out.println("--ERROR: lo operacion no se realizo.");

        }
    }
    
}
