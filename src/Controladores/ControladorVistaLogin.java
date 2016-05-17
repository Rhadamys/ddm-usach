/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaLogin;
import Vistas.VistaPrincipal;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class ControladorVistaLogin {
    private JInternalFrame visLog;
    private VistaLogin visLogTipo;
    
    public ControladorVistaLogin(VistaPrincipal visPrin){
        this.visLog = new VistaLogin(visPrin);
        this.visLogTipo = (VistaLogin) this.visLog;
        this.visLog.setVisible(true);
    }
    
    public JInternalFrame getVisLog() {
        return visLog;
    }

    public VistaLogin getVisLogTipo() {
        return visLogTipo;
    }
    
}
