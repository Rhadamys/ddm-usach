/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class VistaLogin extends javax.swing.JInternalFrame {
    private VistaPrincipal vistaPrincipal;
    
    /**
     * Creates new form VistaLogin
     */
    public VistaLogin() {
        initComponents();
    }

    /**
     * Inicializar VistaLogin
     * @param vistaPrincipal Vista principal que contiene a este JInternalFrame
     */
    public VistaLogin(VistaPrincipal vistaPrincipal){
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.vistaPrincipal = vistaPrincipal;
        initComponents();
        
        BotonImagen botonLogin = new BotonImagen("/Imagenes/boton.png");
        botonLogin.setLocation(270, 515);
        botonLogin.setSize(270, 40);
        botonLogin.setText("Ingresar");
        botonLogin.setForeground(Color.white);
        botonLogin.setImagenSobre("/Imagenes/boton_mouse_sobre.png");
        botonLogin.setImagenPresionado("/Imagenes/boton_presionado.png");
        botonLogin.setFont(vistaPrincipal.getFont());
        this.add(botonLogin);
        
        PanelImagen fondo = new PanelImagen("/Imagenes/fondo_login.png");
        fondo.setSize(this.getSize());
        this.add(fondo);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        L1 = new javax.swing.JLabel();
        usuario = new javax.swing.JTextField();
        L2 = new javax.swing.JLabel();
        pass = new javax.swing.JPasswordField();
        L3 = new javax.swing.JLabel();
        registrarse = new javax.swing.JLabel();
        mensaje = new javax.swing.JLabel();

        setBackground(new java.awt.Color(102, 102, 102));
        setBorder(null);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        L1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L1.setForeground(new java.awt.Color(255, 255, 255));
        L1.setText("Usuario:");
        getContentPane().add(L1);
        L1.setBounds(270, 440, 80, 20);
        L1.setFont(vistaPrincipal.getFont());

        usuario.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        usuario.setToolTipText("Ingresa tu usuario");
        getContentPane().add(usuario);
        usuario.setBounds(350, 440, 190, 23);
        usuario.setFont(vistaPrincipal.getFont());

        L2.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L2.setForeground(new java.awt.Color(255, 255, 255));
        L2.setText("Pass:");
        getContentPane().add(L2);
        L2.setBounds(270, 480, 80, 20);
        L2.setFont(vistaPrincipal.getFont());

        pass.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        pass.setToolTipText("Ingresa tu contraseña");
        pass.setEchoChar('*');
        getContentPane().add(pass);
        pass.setBounds(350, 480, 190, 23);
        pass.setFont(vistaPrincipal.getFont());

        L3.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L3.setForeground(new java.awt.Color(255, 255, 255));
        L3.setText("¿No tienes una cuenta?");
        getContentPane().add(L3);
        L3.setBounds(270, 560, 180, 17);
        L3.setFont(vistaPrincipal.getFont());

        registrarse.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        registrarse.setForeground(new java.awt.Color(204, 204, 204));
        registrarse.setText("Registrate");
        registrarse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(registrarse);
        registrarse.setBounds(460, 560, 80, 17);
        registrarse.setFont(vistaPrincipal.getFont());

        mensaje.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        mensaje.setForeground(new java.awt.Color(255, 255, 255));
        mensaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mensaje.setText("Mensaje");
        getContentPane().add(mensaje);
        mensaje.setBounds(270, 410, 270, 17);
        mensaje.setFont(vistaPrincipal.getFont());

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    public JLabel getRegistrarse() {
        return registrarse;
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel L1;
    private javax.swing.JLabel L2;
    private javax.swing.JLabel L3;
    private javax.swing.JLabel mensaje;
    private javax.swing.JPasswordField pass;
    private javax.swing.JLabel registrarse;
    private javax.swing.JTextField usuario;
    // End of variables declaration//GEN-END:variables
}
