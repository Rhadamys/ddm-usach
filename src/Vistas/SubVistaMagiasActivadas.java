/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.Constantes;
import Otros.PanelImagen;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class SubVistaMagiasActivadas extends PanelImagen {
    private final ArrayList<PanelImagen> panelesMagias;
    private final ArrayList<JLabel> etiquetasTurRes;
    private final String[] nombresMagias;
    
    /**
     * Creates new form SubVistaMagiasActivadas
     */
    public SubVistaMagiasActivadas() {
        initComponents();
        
        this.setLayout(null);
        this.setSize(120, 40);
        this.setLocation(160, 5);
        this.setBorder(null);
        
        this.panelesMagias = new ArrayList();
        this.etiquetasTurRes = new ArrayList();
        
        this.nombresMagias = new String[3];
        this.nombresMagias[0] = "Lluvia torrencial";
        this.nombresMagias[1] = "Hierbas venenosas";
        this.nombresMagias[2] = "Meteoritos de fuego";
        
        int[][] posPanelesMagias = {{10, 5}, {45, 5}, {80, 5}};
        for(int i = 0; i < 3; i++){            
            JLabel turnosRestantes = new JLabel("0");
            this.add(turnosRestantes);
            turnosRestantes.setForeground(Color.white);
            turnosRestantes.setFont(Constantes.FUENTE_14PX);
            turnosRestantes.setLocation(posPanelesMagias[i][0] + 25, 5);
            turnosRestantes.setSize(10, 10);
            turnosRestantes.setBackground(Color.darkGray);
            
            PanelImagen panelMagia = new PanelImagen(Constantes.RUTA_OTROS + "/magia_" + (i+1) + Constantes.EXT1);
            this.add(panelMagia);
            panelMagia.setSize(30, 30);
            panelMagia.setLocation(posPanelesMagias[i][0], posPanelesMagias[i][1]);
            panelMagia.setToolTipText("<html><b>" + nombresMagias[i] + "</b><br>No está activada.");
            
            panelesMagias.add(panelMagia);
            etiquetasTurRes.add(turnosRestantes);
        }
        
        this.setImagen(Constantes.VACIO_CAFE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public void actualizarMagia(int i, int turnos){
        this.etiquetasTurRes.get(i).setText(String.valueOf(turnos));
        if(turnos != 0){
            this.panelesMagias.get(i).setToolTipText("<html><b>" + nombresMagias[i] + "</b><br>Quedan " + turnos + " turnos.");
        }else{
            this.panelesMagias.get(i).setToolTipText("<html><b>" + nombresMagias[i] + "</b><br>No está activada.");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}