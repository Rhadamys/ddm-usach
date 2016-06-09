/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Jugador;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;

/**
 *
 * @author mam28
 */
public class VistaMenuPrincipal extends javax.swing.JInternalFrame {
    private final BotonImagen nuevaPartida;
    private final BotonImagen nuevoTorneo;
    private final BotonImagen salir;
    private final BotonImagen cerrarSesion;
    
    /**
     * Creates new form VistaMenuPrincipal
     * @param fuente Fuente que se utilizará en esta vista.
     * @param jugador Jugador que ha iniciado sesión
     */
    public VistaMenuPrincipal(Font fuente, Jugador jugador) {
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.nuevaPartida = new BotonImagen("/Imagenes/Botones/partida.png");
        this.nuevoTorneo = new BotonImagen("/Imagenes/Botones/torneo.png");
        this.salir = new BotonImagen("/Imagenes/Botones/salir.png");
        
        BotonImagen[] botones = {nuevaPartida, nuevoTorneo, salir};
        int cantidadCompoenentes = 3;
        int separacion = (this.getSize().width - 200 * cantidadCompoenentes) 
                / (cantidadCompoenentes + 1);
        
        String[] imagenBotonSobre = {
            "/Imagenes/Botones/partida_sobre.png", 
            "/Imagenes/Botones/torneo_sobre.png", 
            "/Imagenes/Botones/salir_sobre.png"};
        
        String[] imagenBotonPresionado = {
            "/Imagenes/Botones/partida_presionado.png", 
            "/Imagenes/Botones/torneo_presionado.png", 
            "/Imagenes/Botones/salir_presionado.png"};
        
        String[] mensajeBoton = {"Nueva partida", "Nuevo torneo", "Salir"};
        
        for (int i = 0; i < cantidadCompoenentes; i ++){
            final int index = i;
            int posicionX = 200 * i + separacion * (i + 1);
            
            botones[i].setSize(200, 320);
            botones[i].setLocation(posicionX, 140);
            botones[i].setImagenSobre(imagenBotonSobre[i]);
            botones[i].setImagenPresionado(imagenBotonPresionado[i]);
            botones[i].addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e){
                    mensaje.setText(mensajeBoton[index]);
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    mensaje.setText("");
                }
            });
            this.add(botones[i]);
            
            PanelImagen panel = new PanelImagen("/Imagenes/Fondos/cajon_menu.png");
            panel.setSize(botones[i].getSize());
            panel.setLocation(posicionX, 140);
            this.add(panel);
        }
        
        this.cerrarSesion = new BotonImagen();
        this.add(cerrarSesion);
        this.cerrarSesion.setSize(60, 60);
        this.cerrarSesion.setLocation(730, 10);
        this.cerrarSesion.setImagenSobre("/Imagenes/Otros/cerrar_sesion.png");
        this.cerrarSesion.setImagenPresionado("/Imagenes/Otros/cerrar_sesion_presionado.png");
        this.cerrarSesion.setLayout(null);
        this.cerrarSesion.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e){
                    mensaje.setText("Cerrar sesión");
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    mensaje.setText("");
                }
            });
        
        PanelImagen iconoJugador = new PanelImagen();
        this.add(iconoJugador);
        iconoJugador.setSize(60, 60);
        iconoJugador.setLocation(730, 10);
        iconoJugador.setImagen("/Imagenes/Jefes/" + jugador.getJefeDeTerreno().getNomArchivoImagen() + ".png");
        iconoJugador.setBorder(new LineBorder(Color.darkGray, 2));
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_menu_principal.png");
        panelFondo.setSize(this.getSize());
        this.add(panelFondo);
        
        this.mensaje.setText("");
        this.mensaje.setFont(new Font(fuente.getName(), 
                Font.TRUETYPE_FONT, 36));
        this.mensajeBienvenida.setFont(new Font(fuente.getName(), 
                Font.TRUETYPE_FONT, 24));
        
        this.mensajeBienvenida.setText("<html>¡Bienvenid@ <i><b style=\"color:orange;\">" + jugador.getNombreJugador() + "</b></i>!</html>");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mensaje = new javax.swing.JLabel();
        mensajeBienvenida = new javax.swing.JLabel();

        setBackground(new java.awt.Color(102, 102, 102));
        setBorder(null);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        mensaje.setFont(new java.awt.Font("Consolas", 0, 36)); // NOI18N
        mensaje.setForeground(new java.awt.Color(255, 255, 255));
        mensaje.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mensaje.setText("Mensaje");
        getContentPane().add(mensaje);
        mensaje.setBounds(410, 540, 360, 40);

        mensajeBienvenida.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        mensajeBienvenida.setForeground(new java.awt.Color(255, 255, 255));
        mensajeBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mensajeBienvenida.setText("Bienvenida");
        getContentPane().add(mensajeBienvenida);
        mensajeBienvenida.setBounds(350, 20, 360, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public BotonImagen getNuevaPartida() {
        return nuevaPartida;
    }

    public BotonImagen getNuevoTorneo() {
        return nuevoTorneo;
    }
    
    public BotonImagen getSalir(){
        return salir;
    }

    public BotonImagen getCerrarSesion() {
        return cerrarSesion;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel mensaje;
    private javax.swing.JLabel mensajeBienvenida;
    // End of variables declaration//GEN-END:variables
}
