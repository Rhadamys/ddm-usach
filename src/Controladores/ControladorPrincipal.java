/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaLogin;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class ControladorPrincipal {
    private ControladorVistaPrincipal contVisPrin;
    private ControladorVistaLogin contVisLog;
    private ControladorVistaRegistro contVisReg;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ControladorPrincipal contPrin = new ControladorPrincipal();
        contPrin.contVisPrin = new ControladorVistaPrincipal();
        contPrin.contVisLog = new ControladorVistaLogin(contPrin.contVisPrin.getVisPrin());
        contPrin.contVisReg = new ControladorVistaRegistro(contPrin.contVisPrin.getVisPrin());
        contPrin.contVisPrin.agregarVista(contPrin.contVisLog.getVisLog());
        contPrin.contVisPrin.agregarVista(contPrin.contVisReg.getVisReg());
        contPrin.agregarEventos();
    }
    
    
    public void agregarEventos(){
        JLabel registrarse = this.contVisLog.getVisLogTipo().getRegistrarse();
        JButton volverReg = this.contVisReg.getVisRegTipo().getVolver();
        
        registrarse.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                mostrarVistaRegistro();
            }
        });
        
        volverReg.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                volverReg();
            }
        });
    }
    
    public void mostrarVistaRegistro(){
        this.contVisReg.getVisReg().setVisible(true);
    }
    
    public void volverReg(){
        this.contVisReg.getVisReg().setVisible(false);
    }
}
