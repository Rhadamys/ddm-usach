/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author mam28
 */
public class CajaTextoImagen extends JTextField {
    private Image imagenNormal;
    private Image imagenIncorrecto;
    private Image imagenCorrecto;
    private Image imagen;
    
    public CajaTextoImagen(){
        this.imagenNormal = new ImageIcon(getClass().getResource(
                "/Imagenes/Otros/fondo_caja_texto.png")).getImage();
        this.imagenIncorrecto = new ImageIcon(getClass().getResource(
                "/Imagenes/Otros/fondo_caja_texto_incorrecto.png")).getImage();
        this.imagenCorrecto = new ImageIcon(getClass().getResource(
                "/Imagenes/Otros/fondo_caja_texto_correcto.png")).getImage();
        this.setFont(Constantes.FUENTE_14PX);
        this.setForeground(Color.white);
        
        this.imagen = this.imagenNormal;
        this.setCursor(Constantes.CURSOR);
        this.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.setOpaque(false);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        super.paint(g);
    }

    public void setImagenNormal(Image imagenNormal) {
        this.imagenNormal = imagenNormal;
    }

    public void setImagenIncorrecto(Image imagenIncorrecto) {
        this.imagenIncorrecto = imagenIncorrecto;
    }

    public void setImagenCorrecto(Image imagenCorrecto) {
        this.imagenCorrecto = imagenCorrecto;
    }
    
    public void setImagenActual(int i){
        switch(i){
            case 0: this.imagen = this.imagenNormal;
                    break;
            case 1: this.imagen = this.imagenCorrecto;
                    break;
            case 2: this.imagen = this.imagenIncorrecto;
                    break;
        }
        this.repaint();
    }
    
}
