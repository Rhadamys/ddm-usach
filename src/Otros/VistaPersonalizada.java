/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Color;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class VistaPersonalizada extends JInternalFrame {
    private final PanelImagen panelFondo;
    
    public VistaPersonalizada(){        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setSize(Constantes.TAMANO_VENTANA);
        this.setLayout(null);
        this.setBorder(null);
        this.setOpaque(false);
        this.setBackground(new Color(0, 0, 0, 0));
        
        panelFondo = new PanelImagen();
        panelFondo.setSize(this.getSize());
        this.add(panelFondo);
    }
    
    public final void setImagenFondo(String imagen){
        this.panelFondo.setImagen(imagen);
        this.add(panelFondo);
    }
    
    @Override
    public final void setBackground(Color color){
        super.setBackground(new Color(0, 0, 0, 0));
    }
}
