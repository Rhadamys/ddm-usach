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
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author mam28
 */
public class BotonCheckImagen extends JToggleButton {
    private Image imagen;
    private Image imagenMouseNormal;
    private Image imagenMouseSobre;
    private Image imagenMousePresionado;
    private Image imagenSelMouseNormal;
    private Image imagenSelMouseSobre;
    private Image imagenSelMousePresionado;
    
    public BotonCheckImagen(){
        this.setContentAreaFilled(false);
        this.setBorder(null);
        agregarListeners();
    }
    
    /**
     * Inicializar un botón con imagen de fondo.
     * @param imagen String - Nombre del archivo.
     */
    public BotonCheckImagen(String imagen){
        //Inicializar panel de fondo
        imagenMouseNormal = new ImageIcon(getClass().getResource(imagen)).getImage();
        imagenMouseSobre = new ImageIcon(getClass().getResource(imagen)).getImage();
        imagenMousePresionado = new ImageIcon(getClass().getResource(imagen)).getImage();
        this.imagen = imagenMouseSobre;
        this.setContentAreaFilled(false);
        this.setBorder(null);
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
                if(((BotonCheckImagen) e.getComponent()).isSelected()){
                    ((BotonCheckImagen) e.getComponent()).setImagenActual(4);
                }else{
                    ((BotonCheckImagen) e.getComponent()).setImagenActual(1);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                if(((BotonCheckImagen) e.getComponent()).isSelected()){
                    ((BotonCheckImagen) e.getComponent()).setImagenActual(3);
                }else{
                    ((BotonCheckImagen) e.getComponent()).setImagenActual(0);
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                if(((BotonCheckImagen) e.getComponent()).isSelected()){
                    ((BotonCheckImagen) e.getComponent()).setImagenActual(5);
                }else{
                    ((BotonCheckImagen) e.getComponent()).setImagenActual(2);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e){
                if(((BotonCheckImagen) e.getComponent()).isSelected()){
                    ((BotonCheckImagen) e.getComponent()).setImagenActual(4);
                }else{
                    ((BotonCheckImagen) e.getComponent()).setImagenActual(1);
                }
            }
        });
        
        this.addChangeListener((ChangeEvent e) -> {
            if(((BotonCheckImagen) e.getSource()).isSelected()){
                ((BotonCheckImagen) e.getSource()).setImagenActual(3);
            }else{
                ((BotonCheckImagen) e.getSource()).setImagenActual(1);
            }
        });
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        
        this.setOpaque(false);
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
     * Define la imagen que se mostrará cuando el mouse salga del botón.
     * @param imagen String - Nombre del archivo
     */
    public void setImagenSelNormal(String imagen){
        imagenSelMouseNormal = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando el mouse se encuentre sobre
     * el botón
     * @param imagen String - Nombre del archivo
     */
    public void setImagenSelSobre(String imagen){
        imagenSelMouseSobre = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando se presiona el botón.
     * @param imagen String - Nombre del archivo
     */
    public void setImagenSelPresionado(String imagen){
        imagenSelMousePresionado = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    public void setImagenActual(int i){
        switch(i){
            case 0: this.imagen = this.imagenMouseNormal;
                    break;
            case 1: this.imagen = this.imagenMouseSobre;
                    break;
            case 2: this.imagen = this.imagenMousePresionado;
                    break;
            case 3: this.imagen = this.imagenSelMouseNormal;
                    break;
            case 4: this.imagen = this.imagenSelMouseSobre;
                    break;
            case 5: this.imagen = this.imagenSelMousePresionado;
                    break;
        }
        this.repaint();
    }
}
