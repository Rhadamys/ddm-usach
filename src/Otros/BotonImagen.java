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
import javax.swing.event.ChangeEvent;

/**
 *
 * @author mam28
 */
public class BotonImagen extends JButton {
    private Image imagenActual;
    private Image imagenMouseNormal;
    private Image imagenMouseSobre;
    private Image imagenMousePresionado;
    private Image imagenDeshabilitado;
    
    public BotonImagen(){
        this.setContentAreaFilled(false);
        this.setBorder(null);
        this.setOpaque(false);
        repaint();
        agregarListeners();
    }
    
    /**
     * Inicializar un botón con imagen de fondo.
     * @param imagen String - Nombre del archivo.
     */
    public BotonImagen(String imagen){
        //Inicializar panel de fondo        
        imagenMouseNormal = new ImageIcon(getClass().getResource(imagen)).getImage();
        imagenMouseSobre = new ImageIcon(getClass().getResource(imagen)).getImage();
        imagenMousePresionado = new ImageIcon(getClass().getResource(imagen)).getImage();
        imagenDeshabilitado = new ImageIcon(getClass().getResource(imagen)).getImage();
        this.imagenActual = imagenMouseSobre;
        this.setContentAreaFilled(false);
        this.setBorder(null);
        this.setOpaque(false);
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
                if(isEnabled()){
                    setImagenActual(1);
                }else{
                    setImagenActual(3);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                if(isEnabled()){
                    setImagenActual(0);
                }else{
                    setImagenActual(3);
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                if(isEnabled()){
                    setImagenActual(2);
                }else{
                    setImagenActual(3);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e){
                if(e.getX() > 0 && e.getX() < e.getComponent().getWidth() &&
                   e.getY() > 0 && e.getY() < e.getComponent().getHeight()){
                    if(isEnabled()){
                        setImagenActual(1);
                    }else{
                        setImagenActual(3);
                    }
                }else{
                    mouseExited(e);
                }
            }
        });
        
        this.addChangeListener((ChangeEvent e) -> {
            if(isEnabled()){
                setImagenActual(0);
            }else{
                setImagenActual(3);
            }
        });
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(imagenActual, 0, 0, getWidth(), getHeight(), this);
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
    public Image getImagenNormal(){
        return imagenMouseNormal;
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
    public void setImagenNormal(String imagen){
        imagenMouseNormal = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando se presiona el botón.
     * @param imagen String - Nombre del archivo
     */
    public void setImagenPresionado(String imagen){
        imagenMousePresionado = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando el botón esté deshabilitado.
     * @param imagen String - Nombre del archivo
     */
    public void setImagenDeshabilitado(String imagen){
        imagenDeshabilitado = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    public void setImagenActual(int i){
        switch(i){
            case 0: this.imagenActual = this.imagenMouseNormal;
                    break;
            case 1: this.imagenActual = this.imagenMouseSobre;
                    break;
            case 2: this.imagenActual = this.imagenMousePresionado;
                    break;
            default: this.imagenActual = this.imagenDeshabilitado;
        }
        this.repaint();
    }
}
