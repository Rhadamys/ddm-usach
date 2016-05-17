/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.VistaPrincipal;
import Vistas.VistaRegistro;
import javax.swing.JInternalFrame;

/**
 *
 * @author mam28
 */
public class ControladorVistaRegistro {
    private JInternalFrame visReg;
    private VistaRegistro visRegTipo;
    
    public ControladorVistaRegistro(VistaPrincipal visPrin){
        this.visReg = new VistaRegistro(visPrin);
        this.visRegTipo = (VistaRegistro) this.visReg;
    }

    public JInternalFrame getVisReg() {
        return visReg;
    }

    public VistaRegistro getVisRegTipo() {
        return visRegTipo;
    }
    
}
