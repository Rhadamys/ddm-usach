/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 *
 * @author mam28
 */
public class BotonCheckImagen extends JToggleButton implements MouseListener {
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
        this.setOpaque(false);
    }
    
    /**
     * Inicializar un botón con imagen de fondo.
     * @param numBoton
     */
    public BotonCheckImagen(int numBoton){
        String imgNormal = Constantes.VACIO;
        String imgSobre = Constantes.VACIO;
        String imgPresionado = Constantes.VACIO;
        String imgSelNormal = Constantes.VACIO;
        String imgSelSobre = Constantes.VACIO;
        String imgSelPresionado = Constantes.VACIO;
        
        switch(numBoton){
            case Constantes.BTN_MARCO:  imgSobre = Constantes.MARCO_SELECCION;
                                        imgPresionado = Constantes.MARCO_SELECCION;
                                        imgSelNormal = Constantes.MARCO_SELECCION;
                                        imgSelSobre = Constantes.MARCO_SELECCION;
                                        break;
            case Constantes.BTN_REDONDO:    imgNormal = Constantes.BOTON_REDONDO;
                                            imgSobre = Constantes.BOTON_REDONDO_SOBRE;
                                            imgPresionado = Constantes.BOTON_REDONDO_PRESIONADO;
                                            imgSelNormal = Constantes.BOTON_REDONDO_SELECCION;
                                            imgSelSobre = Constantes.BOTON_REDONDO_SELECCION_SOBRE;
                                            imgSelPresionado = Constantes.BOTON_REDONDO_SELECCION_PRESIONADO;
                                            break;
        }
        
        imagenMouseNormal = new ImageIcon(getClass().getResource(imgNormal)).getImage();
        imagenMouseSobre = new ImageIcon(getClass().getResource(imgSobre)).getImage();
        imagenMousePresionado = new ImageIcon(getClass().getResource(imgPresionado)).getImage();
        imagenSelMouseNormal = new ImageIcon(getClass().getResource(imgSelNormal)).getImage();
        imagenSelMouseSobre = new ImageIcon(getClass().getResource(imgSelSobre)).getImage();
        imagenSelMousePresionado = new ImageIcon(getClass().getResource(imgSelPresionado)).getImage();
        
        this.imagen = imagenMouseNormal;
        this.setContentAreaFilled(false);
        this.setBorder(null);
        this.setOpaque(false);
        
        this.addMouseListener(this);
    }
    
    @Override
    public final void paint(Graphics g) {
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        super.paint(g);
    }
        
    /**
     * Define la imagen que se mostrará cuando el mouse se encuentre sobre
     * el botón
     * @param imagen String - Nombre del archivo
     */
    public final void setImagenSobre(String imagen){
        imagenMouseSobre = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando el mouse salga del botón.
     * @param imagen String - Nombre del archivo
     */
    public final void setImagenNormal(String imagen){
        imagenMouseNormal = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando se presiona el botón.
     * @param imagen String - Nombre del archivo
     */
    public final void setImagenPresionado(String imagen){
        imagenMousePresionado = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando el mouse salga del botón.
     * @param imagen String - Nombre del archivo
     */
    public final void setImagenSelNormal(String imagen){
        imagenSelMouseNormal = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando el mouse se encuentre sobre
     * el botón
     * @param imagen String - Nombre del archivo
     */
    public final void setImagenSelSobre(String imagen){
        imagenSelMouseSobre = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    /**
     * Define la imagen que se mostrará cuando se presiona el botón.
     * @param imagen String - Nombre del archivo
     */
    public final void setImagenSelPresionado(String imagen){
        imagenSelMousePresionado = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    public final void setImagenActual(int i){
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
    
    public final void deseleccionado(){
        this.setSelected(false);
        this.setImagenActual(0);
    }
    
    public final void seleccionado(){
        this.setSelected(true);
        this.setImagenActual(3);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        // Nada
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if(isSelected()){
            setImagenActual(5);
        }else{
            setImagenActual(2);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getX() > 0 && e.getX() < getWidth() &&
           e.getY() > 0 && e.getY() < getHeight()){
            if(isSelected()){
                setImagenActual(4);
            }else{
                setImagenActual(1);
            }
        }else{
            mouseExited(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        if(isSelected()){
            setImagenActual(4);
        }else{
            setImagenActual(1);
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if(isSelected()){
            setImagenActual(3);
        }else{
            setImagenActual(0);
        }
    }
}
