/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author mam28
 */
public class PanelImagen extends JPanel {
    private Image imagen;
    
    public PanelImagen(){
        this.imagen = new ImageIcon(getClass().getResource("/Imagenes/vacio.png")).getImage();
        this.setOpaque(false);
    }
    
    public PanelImagen(String imagen){
        this.imagen = new ImageIcon(getClass().getResource(imagen)).getImage();
        if(imagen.endsWith(".gif")){
            this.imagen.flush();
        }
        this.setOpaque(false);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);
    }
    
    public void setImagen(String imagen){
        this.imagen = new ImageIcon(getClass().getResource(imagen)).getImage();
        if(imagen.endsWith(".gif")){
            this.imagen.flush();
        }
        this.repaint();
    }
    
}
