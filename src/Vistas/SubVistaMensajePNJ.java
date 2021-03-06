/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.PersonajeNoJugable;
import Otros.Constantes;
import Otros.PanelImagen;
import Otros.VistaPersonalizada;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public class SubVistaMensajePNJ extends VistaPersonalizada {

    /**
     * Creates new form SubVistaMensajePNJ
     * @param pnj PNJ para el cual se crea la vista mensaje
     * @param mensaje Mensaje del PNJ
     */
    public SubVistaMensajePNJ(PersonajeNoJugable pnj, String mensaje) {
        initComponents();
        
        this.setSize(800, 150);
        this.setLocation(0, 600);
        this.mensaje.setFont(Constantes.FUENTE_18PX);
        
        PanelImagen iconoPNJ = new PanelImagen(Constantes.RUTA_JEFES +
                pnj.getJefeDeTerreno().getNomArchivoImagen() + Constantes.EXT1);
        this.add(iconoPNJ);
        iconoPNJ.setSize(100, 100);
        iconoPNJ.setLocation(650, 0);
        
        this.mensaje.setText("<html><b>" + pnj.getNombreJugador() + ":</b><br>" + mensaje + "</html>");
        
        Timer timerAnimacion = new Timer();
        timerAnimacion.schedule(new TimerTask(){
            int tic = 0;
            
            @Override
            public void run(){
                if(tic < 150){
                    setLocation(0, getY() - 1);
                }else if(tic > 2000){
                    setLocation(0, getY() + 1);
                    if(getY() > 600){
                        dispose();
                        this.cancel();
                        timerAnimacion.cancel();
                    }
                }
                
                tic++;
            }
        }, 0, 2);
        
        this.setImagenFondo(Constantes.FONDO_MENSAJE_PNJ);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mensaje = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));

        mensaje.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        mensaje.setForeground(new java.awt.Color(255, 255, 255));
        mensaje.setText("Mensaje");
        mensaje.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(mensaje);
        mensaje.setBounds(40, 60, 560, 100);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel mensaje;
    // End of variables declaration//GEN-END:variables
}
