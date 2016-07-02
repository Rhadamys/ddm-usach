/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author mam28
 */
public final class BotonImagen extends JButton implements MouseListener, ChangeListener{
    private Image imagenActual;
    private Image imagenMouseNormal;
    private Image imagenMouseSobre;
    private Image imagenMousePresionado;
    private Image imagenDeshabilitado;
    
    public BotonImagen(){
        this.setContentAreaFilled(false);
        this.setBorder(null);
        this.setOpaque(false);
        this.setCursor(Constantes.CURSOR);
        repaint();
        
        this.addMouseListener(this);
        this.addChangeListener(this);
    }
    
    /**
     * Inicializar un botón con imagen de fondo.
     * @param numBoton Numero del botón que se creará (Ver en Constantes)
     */
    public BotonImagen(int numBoton){ 
        String imgNormal = Constantes.VACIO;
        String imgSobre = Constantes.VACIO;
        String imgPresionado = Constantes.VACIO;
        String imgDeshabilitado = Constantes.VACIO;
        
        switch(numBoton){
            case Constantes.BTN_NORMAL: imgNormal = Constantes.BOTON;
                                        imgSobre = Constantes.BOTON_SOBRE;
                                        imgPresionado = Constantes.BOTON_PRESIONADO;
                                        imgDeshabilitado = Constantes.BOTON_DESHABILITADO;
                                        break;
            case Constantes.BTN_REDONDO:    imgNormal = Constantes.BOTON_REDONDO;
                                            imgSobre = Constantes.BOTON_REDONDO_SOBRE;
                                            imgPresionado = Constantes.BOTON_REDONDO_PRESIONADO;
                                            break;
            case Constantes.BTN_ATAQUE: imgNormal = Constantes.ATAQUE;
                                        imgSobre = Constantes.ATAQUE_SOBRE;
                                        imgPresionado = Constantes.ATAQUE_PRESIONADO;
                                        imgDeshabilitado = Constantes.ATAQUE_DESHABILITADO;
                                        break;
            case Constantes.BTN_INVOCACION: imgNormal = Constantes.INVOCACION;
                                            imgSobre = Constantes.INVOCACION_SOBRE;
                                            imgPresionado = Constantes.INVOCACION_PRESIONADO;
                                            imgDeshabilitado = Constantes.INVOCACION_DESHABILITADO;
                                            break;
            case Constantes.BTN_MAGIA:  imgNormal = Constantes.MAGIA;
                                        imgSobre = Constantes.MAGIA_SOBRE;
                                        imgPresionado = Constantes.MAGIA_PRESIONADO;
                                        imgDeshabilitado = Constantes.MAGIA_DESHABILITADO;
                                        break;  
            case Constantes.BTN_MOVIMIENTO: imgNormal = Constantes.MOVIMIENTO;
                                            imgSobre = Constantes.MOVIMIENTO_SOBRE;
                                            imgPresionado = Constantes.MOVIMIENTO_PRESIONADO;
                                            imgDeshabilitado = Constantes.MOVIMIENTO_DESHABILITADO;
                                            break;
            case Constantes.BTN_TRAMPA: imgNormal = Constantes.TRAMPA;
                                        imgSobre = Constantes.TRAMPA_SOBRE;
                                        imgPresionado = Constantes.TRAMPA_PRESIONADO;
                                        imgDeshabilitado = Constantes.TRAMPA_DESHABILITADO;
                                        break;
            case Constantes.BTN_PAUSA:  imgNormal = Constantes.PAUSA;
                                        imgSobre = Constantes.PAUSA_SOBRE;
                                        imgPresionado = Constantes.PAUSA_PRESIONADO;
                                        imgDeshabilitado = Constantes.PAUSA_DESHABILITADO;
                                        break;
            case Constantes.BTN_ATRAS:  imgNormal = Constantes.ATRAS;
                                        imgSobre = Constantes.ATRAS_SOBRE;
                                        imgPresionado = Constantes.ATRAS_PRESIONADO;
                                        break;
            case Constantes.BTN_MARCO:  imgSobre = Constantes.MARCO_SELECCION;
                                        break;
            case Constantes.BTN_NUEVA_PARTIDA:  imgNormal = Constantes.NUEVA_PARTIDA;
                                                imgSobre = Constantes.NUEVA_PARTIDA_SOBRE;
                                                imgPresionado = Constantes.NUEVA_PARTIDA_PRESIONADO;
                                                break;
            case Constantes.BTN_NUEVO_TORNEO:   imgNormal = Constantes.TORNEO;
                                                imgSobre = Constantes.TORNEO_SOBRE;
                                                imgPresionado = Constantes.TORNEO_PRESIONADO;
                                                break;
            case Constantes.BTN_MODIFICAR_PUZZLE:   imgNormal = Constantes.MODIFICAR_PUZZLE;
                                                    imgSobre = Constantes.MODIFICAR_PUZZLE_SOBRE;
                                                    imgPresionado = Constantes.MODIFICAR_PUZZLE_PRESIONADO;
                                                    break;
            case Constantes.BTN_INFO_CRIATURAS: imgNormal = Constantes.INFO_CRIATURAS;
                                                imgSobre = Constantes.INFO_CRIATURAS_SOBRE;
                                                imgPresionado = Constantes.INFO_CRIATURAS_PRESIONADO;
                                                break;
            case Constantes.BTN_SALIR:  imgNormal = Constantes.SALIR;
                                        imgSobre = Constantes.SALIR_SOBRE;
                                        imgPresionado = Constantes.SALIR_PRESIONADO;
                                        break;
        }
        
        imagenMouseNormal = new ImageIcon(getClass().getResource(imgNormal)).getImage();
        imagenMouseSobre = new ImageIcon(getClass().getResource(imgSobre)).getImage();
        imagenMousePresionado = new ImageIcon(getClass().getResource(imgPresionado)).getImage();
        imagenDeshabilitado = new ImageIcon(getClass().getResource(imgDeshabilitado)).getImage();
        
        this.imagenActual = imagenMouseNormal;
        
        this.setContentAreaFilled(false);
        this.setBorder(null);
        this.setOpaque(false);
        this.setFont(Constantes.FUENTE_14PX);
        this.setCursor(Constantes.CURSOR);
        this.setForeground(Color.white);
        repaint();
        
        this.addMouseListener(this);
        this.addChangeListener(this);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(imagenActual, 0, 0, getWidth(), getHeight(), this);
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
     * Define la imagen que se mostrará cuando el botón esté deshabilitado.
     * @param imagen String - Nombre del archivo
     */
    public final void setImagenDeshabilitado(String imagen){
        imagenDeshabilitado = new ImageIcon(getClass().getResource(imagen)).getImage();
    }
    
    public final void setImagenActual(int i){
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

    @Override
    public void mouseClicked(MouseEvent me) {
        // Nada
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if(isEnabled()){
            setImagenActual(2);
        }else{
            setImagenActual(3);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getX() > 0 && e.getX() < e.getComponent().getWidth() &&
           e.getY() > 0 && e.getY() < e.getComponent().getHeight()){
            if(isEnabled()){
                Reproductor.reproducirEfecto(Constantes.CLICK);
                setImagenActual(1);
            }else{
                setImagenActual(3);
            }
        }else{
            mouseExited(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        if(isEnabled()){
            setImagenActual(1);
        }else{
            setImagenActual(3);
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if(isEnabled()){
            setImagenActual(0);
        }else{
            setImagenActual(3);
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        if(isEnabled()){
            setImagenActual(0);
        }else{
            setImagenActual(3);
        }
    }
}
