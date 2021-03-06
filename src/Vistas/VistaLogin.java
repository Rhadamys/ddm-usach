/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonImagen;
import Otros.CajaPassImagen;
import Otros.CajaTextoImagen;
import Otros.Constantes;
import Otros.PanelImagen;
import Otros.VistaPersonalizada;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class VistaLogin extends VistaPersonalizada {
    private final BotonImagen ingresar;
    private final CajaTextoImagen usuario;
    private final CajaPassImagen pass;
    private final BotonImagen salir;
    
    /**
     * Creates new form VistaLogin
     */
    public VistaLogin() {
        initComponents();
        
        this.add(L1);
        this.add(L2);
        this.add(L3);
        this.add(mensaje);
        this.add(registrarse);
        
        usuario = new CajaTextoImagen();
        this.add(usuario);
        usuario.setSize(190, 30);
        usuario.setLocation(350, 435);
        
        pass = new CajaPassImagen();
        this.add(pass);
        pass.setSize(190, 30);
        pass.setLocation(350, 475);
                
        this.ingresar = new BotonImagen(Constantes.BTN_NORMAL);
        this.ingresar.setLocation(310, 515);
        this.ingresar.setSize(190, 40);
        this.ingresar.setText("Ingresar");
        this.add(this.ingresar);
                
        this.salir = new BotonImagen(Constantes.BTN_REDONDO);
        this.salir.setLocation(740, 10);
        this.salir.setSize(50, 50);
        this.salir.setLayout(null);
        this.add(this.salir);
        
        PanelImagen iconoSalir = new PanelImagen("/Imagenes/Otros/equis.png");
        salir.add(iconoSalir);
        iconoSalir.setSize(salir.getWidth() / 2, salir.getHeight() / 2);
        iconoSalir.setLocation((salir.getWidth() - iconoSalir.getWidth()) / 2, 
                (salir.getHeight() - iconoSalir.getHeight()) / 2);
        
        this.L1.setFont(Constantes.FUENTE_14PX);
        this.L2.setFont(Constantes.FUENTE_14PX);
        this.L3.setFont(Constantes.FUENTE_14PX);
        this.mensaje.setFont(Constantes.FUENTE_14PX);
        this.usuario.setFont(Constantes.FUENTE_14PX);
        this.pass.setFont(Constantes.FUENTE_14PX);
        this.registrarse.setFont(Constantes.FUENTE_14PX);
        
        this.mensaje.setText("");
        
        this.setImagenFondo("/Imagenes/Fondos/fondo_login.png");
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
        L2 = new javax.swing.JLabel();
        L3 = new javax.swing.JLabel();
        registrarse = new javax.swing.JLabel();
        mensaje = new javax.swing.JLabel();

        setBackground(new java.awt.Color(102, 102, 102));
        setMaximumSize(new java.awt.Dimension(800, 600));

        L1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L1.setForeground(new java.awt.Color(255, 255, 255));
        L1.setText("Usuario:");
        getContentPane().add(L1);
        L1.setBounds(270, 440, 80, 20);

        L2.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L2.setForeground(new java.awt.Color(255, 255, 255));
        L2.setText("Pass:");
        getContentPane().add(L2);
        L2.setBounds(270, 480, 80, 20);

        L3.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L3.setForeground(new java.awt.Color(255, 255, 255));
        L3.setText("¿No tienes una cuenta?");
        getContentPane().add(L3);
        L3.setBounds(270, 560, 180, 17);

        registrarse.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        registrarse.setForeground(new java.awt.Color(204, 204, 204));
        registrarse.setText("Registrate");
        registrarse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(registrarse);
        registrarse.setBounds(460, 560, 80, 17);

        mensaje.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        mensaje.setForeground(new java.awt.Color(255, 255, 255));
        mensaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mensaje.setText("Mensaje");
        getContentPane().add(mensaje);
        mensaje.setBounds(270, 410, 270, 17);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    /**
     * Comprueba que se hayan llenado todos los campos requeridos.
     * @return True si los campos se han llenado, false en caso contrario.
     */
    public boolean comprobarCampos(){
        if("".equals(this.getUsuario())){
            this.usuarioErroneo();
        }else{
            this.usuarioCorrecto();
        }

        if("".equals(this.getPass())){
            this.passErronea();
        }
        
        // Retorna verdadero si ambos campos están completos (escritos)
        return !"".equals(this.getUsuario()) && !"".equals(this.getPass());
    }
    
    /**
     * Pinta de rojo el fondo de la caja de texto de usuario, indicando que se debe
     * ingresar un usuario en el campo.
     */
    public void usuarioErroneo(){
        this.usuario.setImagenActual(2);
    }
    
    /**
     * Pinta de verde el fondo de la caja de texto de usuario, indicando que el
     * usuario ingresado existe en los registros.
     */
    public void usuarioCorrecto(){
        this.usuario.setImagenActual(1);
    }
    
    /**
     * Pinta de rojo el fondo de la caja de contraseña, indicando que la contraseña
     * ingresada es incorrecta para el usuario o no se ha ingresado una contraseña
     * válida.
     */
    public void passErronea(){
        this.pass.setImagenActual(2);
    }
    
    public JLabel getRegistrarse() {
        return registrarse;
    }
    
    public BotonImagen getIngresar() {
        return ingresar;
    }
    
    public CajaTextoImagen getCajaUsuario(){
        return this.usuario;
    }
    
    public CajaPassImagen getCajaPass(){
        return this.pass;
    }

    public BotonImagen getSalir() {
        return salir;
    }
    
    public String getUsuario(){
        return this.usuario.getText();
    }
    
    public String getPass(){
        return String.valueOf(this.pass.getPassword());
    }
    
    public void setMensaje(String mensaje){
        this.mensaje.setText(mensaje);
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel L1;
    private javax.swing.JLabel L2;
    private javax.swing.JLabel L3;
    private javax.swing.JLabel mensaje;
    private javax.swing.JLabel registrarse;
    // End of variables declaration//GEN-END:variables
}
