/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author mam28
 */
public class ContenedorScroll extends JScrollPane {
    public ContenedorScroll(){
        this.setOpaque(false);
        this.setBorder(null);
        this.getViewport().setOpaque(false);
        this.getVerticalScrollBar().setUI(new ScrollbarUI(0));
        this.getHorizontalScrollBar().setUI(new ScrollbarUI(1));
        this.getVerticalScrollBar().setUnitIncrement(50);
        this.getVerticalScrollBar().setBlockIncrement(50);
        this.getHorizontalScrollBar().setUnitIncrement(50);
        this.getHorizontalScrollBar().setBlockIncrement(50);
    }
    
    class ScrollbarUI extends BasicScrollBarUI {
        private final Image imageThumb, imageTrack;
        private final String[] ab = {"scroll_abajo", "scroll_abajo_sobre", "scroll_abajo_presionado"};
        private final String[] ar = {"scroll_arriba", "scroll_arriba_sobre", "scroll_arriba_presionado"};
        private final String[] izq = {"scroll_izquierda", "scroll_izquierda_sobre", "scroll_izquierda_presionado"};
        private final String[] der = {"scroll_derecha", "scroll_derecha_sobre", "scroll_derecha_presionado"};

        ScrollbarUI(int direccion) {
            imageThumb = new ImageIcon(getClass().getResource("/Imagenes/Otros/barra_scroll_" + (direccion == 0 ? "ver": "hor") + ".png")).getImage();
            imageTrack = new ImageIcon(getClass().getResource("/Imagenes/Otros/fondo_scroll_" + (direccion == 0 ? "ver": "hor") + ".png")).getImage();
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {        
            g.translate(thumbBounds.x, thumbBounds.y);
            g.setColor(new Color(0,0,0,0));
            g.drawRect(0, 0, thumbBounds.width - 2, thumbBounds.height - 1 );
            AffineTransform transform = AffineTransform.getScaleInstance((double)thumbBounds.width/imageThumb.getWidth(null),(double)thumbBounds.height/imageThumb.getHeight(null));
            ((Graphics2D)g).drawImage(imageThumb, transform, null);
            g.translate( -thumbBounds.x, -thumbBounds.y );
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {        
            g.translate(trackBounds.x, trackBounds.y);
            ((Graphics2D)g).drawImage(imageTrack,AffineTransform.getScaleInstance((double)trackBounds.width/imageTrack.getWidth(null),(double)trackBounds.height/imageTrack.getHeight(null)),null);
            g.translate( -trackBounds.x, -trackBounds.y );
        }

        @Override
        protected BotonImagen createDecreaseButton(int orientation) {
            BotonImagen decreaseButton = new BotonImagen();
            decreaseButton.setImagenNormal("/Imagenes/Botones/" + getAppropriateIcon(orientation)[0] + ".png");
            decreaseButton.setImagenSobre("/Imagenes/Botones/" + getAppropriateIcon(orientation)[1] + ".png");
            decreaseButton.setImagenPresionado("/Imagenes/Botones/" + getAppropriateIcon(orientation)[2] + ".png");
            decreaseButton.setImagenActual(0);
            decreaseButton.setPreferredSize(new Dimension(22, 22));
            return decreaseButton;
        }

        @Override
        protected BotonImagen createIncreaseButton(int orientation) {
            BotonImagen increaseButton = new BotonImagen();
            increaseButton.setImagenNormal("/Imagenes/Botones/" + getAppropriateIcon(orientation)[0] + ".png");
            increaseButton.setImagenSobre("/Imagenes/Botones/" + getAppropriateIcon(orientation)[1] + ".png");
            increaseButton.setImagenPresionado("/Imagenes/Botones/" + getAppropriateIcon(orientation)[2] + ".png");
            increaseButton.setImagenActual(0);
            increaseButton.setPreferredSize(new Dimension(22, 22));
            increaseButton.repaint();
            return increaseButton;
        }
        
        private String[] getAppropriateIcon(int orientation){
            switch(orientation){
                case SwingConstants.SOUTH: return ab;
                case SwingConstants.NORTH: return ar;
                case SwingConstants.EAST: return der;
                default: return izq;
            }
        }
  }
}
