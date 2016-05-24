/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.CompInfoJug;
import Vistas.VistaNuevaPartida;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author mam28
 */
public class ControladorNuevaPartida {
    private final ControladorPrincipal contPrin;
    private VistaNuevaPartida visNuePar;
    private ArrayList<CompInfoJug> vistasInfoJug;
    private int[][] posiciones = {{50, 50}, {400, 50}, {50, 230}, {400, 230}};
    
    ControladorNuevaPartida(ControladorPrincipal contPrin) {
        this.contPrin = contPrin;
        
        this.visNuePar = new VistaNuevaPartida(this.contPrin.getFuentePersonalizada());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visNuePar);
        this.agregarListenersVistaNuevaPartida();
        
        this.vistasInfoJug = new ArrayList();
    }
    
    public void mostrarVistaNuevaPartida(){
        this.visNuePar.setVisible(true);
    }
    
    public void agregarListenersVistaNuevaPartida(){
        this.visNuePar.getAgregar().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.crearControladorBatalla();
                contPrin.getContBat().mostrarVistaBatalla();
                visNuePar.dispose();
//               agregarVistaInfoJugador();
            }
        });
    }
    
    public void agregarVistaInfoJugador(){
        if(vistasInfoJug.size() < 4){
            CompInfoJug visInfoJug = new CompInfoJug(contPrin.getUsuarioActivo(), contPrin.getFuentePersonalizada());
            vistasInfoJug.add(visInfoJug);
            visNuePar.add(visInfoJug, 0);
            visInfoJug.setLocation(posiciones[vistasInfoJug.size() - 1][0], 
                    posiciones[vistasInfoJug.size() - 1][1]);
            visInfoJug.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Máximo 4 jugadores");
        }
    }
    
}
