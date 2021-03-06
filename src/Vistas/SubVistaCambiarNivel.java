/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonImagen;
import Otros.Constantes;
import Otros.VistaPersonalizada;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class SubVistaCambiarNivel extends VistaPersonalizada {
    private final BotonImagen nivel1;
    private final BotonImagen nivel2;
    private final BotonImagen nivel3;
    private final BotonImagen volver;

    /**
     * Creates new form SubVistaCambiarNivel
     * @param nivelActual Nivel actual del PNJ
     */
    public SubVistaCambiarNivel(int nivelActual) {
        initComponents();
        
        this.volver = new BotonImagen(Constantes.BTN_ATRAS);
        this.add(this.volver);
        this.volver.setLocation(105, 88);
        this.volver.setSize(40, 40);
        
        this.nivel1 = new BotonImagen(Constantes.BTN_NORMAL);
        this.nivel2 = new BotonImagen(Constantes.BTN_NORMAL);
        this.nivel3 = new BotonImagen(Constantes.BTN_NORMAL);
        
        this.add(nivel1);
        this.nivel1.setText("Nivel 1");
        this.nivel1.setSize(160, 40);
        this.nivel1.setLocation(120, 150);
        this.nivel1.setEnabled(nivelActual != 1);
        
        JLabel infoNiv1 = new JLabel();
        this.add(infoNiv1);
        infoNiv1.setFont(Constantes.FUENTE_18PX);
        infoNiv1.setForeground(Color.white);
        infoNiv1.setSize(400, 100);
        infoNiv1.setLocation(300, 110);
        infoNiv1.setText("<html><b><i>Nivel 1:</i></b><br>" +
                "- No pone trampas ni activa magias.<br>"+
                "- Siempre invoca la criatura de menor nivel disponible.<br>" +
                "- Ataca máximo 3 veces por turno.</html>");
        
        this.add(nivel2);
        this.nivel2.setText("Nivel 2");
        this.nivel2.setSize(160, 40);
        this.nivel2.setLocation(120, 270);
        this.nivel2.setEnabled(nivelActual != 2);
        
        JLabel infoNiv2 = new JLabel();
        this.add(infoNiv2);
        infoNiv2.setFont(Constantes.FUENTE_18PX);
        infoNiv2.setForeground(Color.white);
        infoNiv2.setSize(400, 140);
        infoNiv2.setLocation(300, 220);
        infoNiv2.setText("<html><b><i>Nivel 2:</i></b><br>" +
                "- No pone trampas pero si puede activar magias.<br>"+
                "- Siempre invoca la criatura de mayor nivel disponible.<br>" +
                "- Ataca máximo 5 veces por turno.</html>");
        
        this.add(nivel3);
        this.nivel3.setText("Nivel 3");
        this.nivel3.setSize(160, 40);
        this.nivel3.setLocation(120, 390);
        this.nivel3.setEnabled(nivelActual != 3);
        
        JLabel infoNiv3 = new JLabel();
        this.add(infoNiv3);
        infoNiv3.setFont(Constantes.FUENTE_18PX);
        infoNiv3.setForeground(Color.white);
        infoNiv3.setSize(400, 100);
        infoNiv3.setLocation(300, 370);
        infoNiv3.setText("<html><b><i>Nivel 3:</i></b><br>" +
                "- Puede poner trampas y activar magias.<br>"+
                "- Siempre invoca la criatura de mayor nivel disponible.<br>" +
                "- No tiene límite de ataques por turno.</html>");
        
        this.setImagenFondo(Constantes.FONDO_SELECCION_3);
        
        this.volver.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                dispose();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public BotonImagen getNivel1() {
        return nivel1;
    }

    public BotonImagen getNivel2() {
        return nivel2;
    }

    public BotonImagen getNivel3() {
        return nivel3;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
