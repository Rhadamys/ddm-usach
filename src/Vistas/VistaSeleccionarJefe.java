/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.JefeDeTerreno;
import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class VistaSeleccionarJefe extends JInternalFrame {
    private VistaPrincipal visPrin;
    private ArrayList<BotonImagen> botones = new ArrayList();
        
    /**
     * Inicializa una nueva instancia de esta vista.
     * @param fuentePersonalizada Fuente que se utilizará en esta vista.
     * @param jefes Lista de jefes de terreno.
     */
    public VistaSeleccionarJefe(Font fuentePersonalizada, ArrayList<JefeDeTerreno> jefes){
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
        
        int filas = 2;
        int columnas = 3;
        int lado = 100;
        int numJefes = jefes.size();
        int separacionHorizontal = (640 - lado * columnas) / (columnas + 1);
        int separacionVertical = (400 - lado * filas) / (filas + 1);
        int aumentoMarco = 20;
        
        for (int i = 0; i < numJefes; i++){
            PanelImagen iconoJefe = new PanelImagen("/Imagenes/Jefes/"
                    + jefes.get(i).getClave() + ".png");
            
            BotonImagen marco = new BotonImagen("/Imagenes/vacio.png");
            
            this.add(marco);
            this.add(iconoJefe);
            iconoJefe.setSize(lado, lado);
            marco.setSize(lado + aumentoMarco, lado + aumentoMarco);
            
            int x = 90 + (separacionHorizontal + lado) * (i % columnas) + separacionHorizontal;
            int y = 60 + (separacionVertical + lado) * (i / columnas) + separacionVertical;
            
            iconoJefe.setLocation(x, y);
            marco.setLocation(x - aumentoMarco / 2, y - aumentoMarco / 2);
            
            marco.setName(String.valueOf(i));
            marco.setImagenSobre("/Imagenes/Otros/marco_seleccion.png");
            
            botones.add(marco);
        }
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_seleccion_2.png");
        this.add(panelFondo);
        panelFondo.setSize(this.getSize());
        
        this.nombre.setFont(new Font(fuentePersonalizada.getName(), Font.TRUETYPE_FONT, 36));
        this.habilidad.setFont(new Font(fuentePersonalizada.getName(), Font.TRUETYPE_FONT, 24));
        
        this.setNombre("");
        this.setHabilidad("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        habilidad = new javax.swing.JLabel();
        nombre = new javax.swing.JLabel();

        setBackground(new java.awt.Color(102, 102, 102));
        setBorder(null);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setVisible(true);
        getContentPane().setLayout(null);

        habilidad.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        habilidad.setForeground(new java.awt.Color(204, 0, 0));
        habilidad.setText("Habilidad");
        getContentPane().add(habilidad);
        habilidad.setBounds(110, 490, 610, 29);

        nombre.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        nombre.setForeground(new java.awt.Color(255, 255, 255));
        nombre.setText("Nombre");
        getContentPane().add(nombre);
        nombre.setBounds(110, 445, 610, 29);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public ArrayList<BotonImagen> getBotones() {
        return botones;
    }
    
    public void setNombre(String nombre){
        this.nombre.setText(nombre);
    }
    
    public void setHabilidad(String habilidad){
        this.habilidad.setText(habilidad);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel habilidad;
    private javax.swing.JLabel nombre;
    // End of variables declaration//GEN-END:variables
}
