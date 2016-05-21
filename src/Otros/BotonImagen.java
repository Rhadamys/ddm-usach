/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author mam28
 */
public class BotonImagen extends JButton {
    private Image imagen;
    private Image imagenMouseFuera;
    private Image imagenMouseSobre;
    private Image imagenMousePresionado;
    
    public BotonImagen(){
        this.setContentAreaFilled(false);
        this.setBorder(null);
        repaint();
        agregarListeners();
    }
    
    /**
     * Inicializar un botón con imagen de fondo.
     * @param imagen String - Nombre del archivo.
     */
    public BotonImagen(String imagen){
        //Inicializar panel de fondo        
        imagenMouseFuera = new ImageIcon(getClass().getResource(imagen)).getImage();
        imagenMouseSobre = new ImageIcon(getClass().getResource(imagen)).getImage();
        imagenMousePresionado = new ImageIcon(getClass().getResource(imagen)).getImage();
        this.imagen = imagenMouseSobre;
        this.setContentAreaFilled(false);
        this.setBorder(null);
        repaint();
        agregarListeners();
    }
    
    /**
     * Especifíca el comportamiento del botón (cambiar imagen) cuando se
     * producen eventos de mouse.
     */
    public void agregarListeners(){
        this.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                setImagenActual(1);
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                setImagenActual(0);
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                setImagenActual(2);
            }
            
            @Override
            public void mouseReleased(MouseEvent e){
                setImagenActual(1);
            }
        });
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(),
                        this);
        
        super.paint(g);
    }
    
    /**
     * Entrega la imagen que se mostrará cuando el mouse se encuentre sobre
     * el botón.
     * @return Image - Imagen cuando el mouse está sobre el botón
     */
    public Image getImagenSobre(){
        return imagenMouseSobre;
    }
    
    /**
     * Entrega la imagen que se mostrará cuando el mouse salga del botón.
     * @return Image - Imagen cuando el mouse sale del botón
     */
    public Image getImagenFuera(){
        return imagenMouseFuera;
    }
    
    /**
     * Entrega la imagen que se mostrará cuando el botón se encuentra
     * presionado.
     * @return Image - Imagen cuando el mouse está presionado.
     */
    public Image getImagenPresionado(){
        return imagenMousePresionado;
    }
    
    /**
     * Define la imagen que se mostrará cuando el mouse se encuentre sobre
     * el botón
     * @param imagen String - Nombre del archivo
     */
    public void setImagenSobre(String imagen){
        imagenMouseSobre = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando el mouse salga del botón.
     * @param imagen String - Nombre del archivo
     */
    public void setImagenFuera(String imagen){
        imagenMouseFuera = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando se presiona el botón.
     * @param imagen String - Nombre del archivo
     */
    public void setImagenPresionado(String imagen){
        imagenMousePresionado = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    public void setImagenActual(int i){
        switch(i){
            case 0: this.imagen = this.imagenMouseFuera;
                    break;
            case 1: this.imagen = this.imagenMouseSobre;
                    break;
            case 2: this.imagen = this.imagenMousePresionado;
                    break;
        }
        this.repaint();
    }
}
