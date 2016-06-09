/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Jugador;
import Otros.BotonImagen;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class VistaBatalla extends javax.swing.JInternalFrame {
    private final Font fuente;
    private final BotonImagen ataque;
    private final BotonImagen invocacion;
    private final BotonImagen magia;
    private final BotonImagen movimiento;
    private final BotonImagen trampa;
    private final BotonImagen pausa;
    private final BotonImagen terminarTurno;
    private final ArrayList<SubVistaInfoJugadorBatalla> vistasJugador;
    private final SubVistaSeleccionDespliegue visSelDesp;
    private SubVistaTablero tablero;
    private SubVistaSeleccionDados visSelDados;
    private SubVistaLanzamientoDados visLanDados;
    private SubVistaSeleccionCriatura visSelCri;
    private final int[][] posInfoJug = {{5, 5}, {655, 5}, {5, 405}, {655, 405}};
    
    /**
     * Creates new form VistaBatalla
     * @param fuente Fuente que se utilizará en esta vista.
     */
    public VistaBatalla(Font fuente) {
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.fuente = fuente;
        this.visSelDesp = new SubVistaSeleccionDespliegue();
        this.vistasJugador = new ArrayList();
        
        this.ataque = new BotonImagen("/Imagenes/Botones/ataque.png");
        this.invocacion = new BotonImagen("/Imagenes/Botones/invocacion.png");
        this.magia = new BotonImagen("/Imagenes/Botones/magia.png");
        this.movimiento = new BotonImagen("/Imagenes/Botones/movimiento.png");
        this.trampa = new BotonImagen("/Imagenes/Botones/trampa.png");
        this.pausa = new BotonImagen("/Imagenes/Botones/pausa.png");
        this.terminarTurno = new BotonImagen("/Imagenes/Botones/boton.png");
        
        this.add(ataque);
        this.add(invocacion);
        this.add(magia);
        this.add(movimiento);
        this.add(trampa);
        this.add(pausa);
        this.add(terminarTurno);
        
        this.ataque.setSize(30, 30);
        this.invocacion.setSize(30, 30);
        this.magia.setSize(30, 30);
        this.movimiento.setSize(30, 30);
        this.trampa.setSize(30, 30);
        this.pausa.setSize(30, 30);
        this.terminarTurno.setSize(140, 30);
        
        this.ataque.setLocation(170, 560);
        this.invocacion.setLocation(210, 560);
        this.magia.setLocation(250, 560);
        this.movimiento.setLocation(290, 560);
        this.trampa.setLocation(330, 560);
        this.pausa.setLocation(600, 560);
        this.terminarTurno.setLocation(370, 560);
        
        this.ataque.setImagenSobre("/Imagenes/Botones/ataque_sobre.png");
        this.invocacion.setImagenSobre("/Imagenes/Botones/invocacion_sobre.png");
        this.magia.setImagenSobre("/Imagenes/Botones/magia_sobre.png");
        this.movimiento.setImagenSobre("/Imagenes/Botones/movimiento_sobre.png");
        this.trampa.setImagenSobre("/Imagenes/Botones/trampa_sobre.png");
        this.pausa.setImagenSobre("/Imagenes/Botones/pausa_sobre.png");
        this.terminarTurno.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        
        this.ataque.setImagenPresionado("/Imagenes/Botones/ataque_presionado.png");
        this.invocacion.setImagenPresionado("/Imagenes/Botones/invocacion_presionado.png");
        this.magia.setImagenPresionado("/Imagenes/Botones/magia_presionado.png");
        this.movimiento.setImagenPresionado("/Imagenes/Botones/movimiento_presionado.png");
        this.trampa.setImagenPresionado("/Imagenes/Botones/trampa_presionado.png");
        this.pausa.setImagenPresionado("/Imagenes/Botones/pausa_presionado.png");
        this.terminarTurno.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        
        this.ataque.setImagenDeshabilitado("/Imagenes/Botones/ataque_deshabilitado.png");
        this.invocacion.setImagenDeshabilitado("/Imagenes/Botones/invocacion_deshabilitado.png");
        this.magia.setImagenDeshabilitado("/Imagenes/Botones/magia_deshabilitado.png");
        this.movimiento.setImagenDeshabilitado("/Imagenes/Botones/movimiento_deshabilitado.png");
        this.trampa.setImagenDeshabilitado("/Imagenes/Botones/trampa_deshabilitado.png");
        this.terminarTurno.setImagenDeshabilitado("/Imagenes/Botones/boton_deshabilitado.png");
        
        this.ataque.setEnabled(false);
        this.invocacion.setEnabled(false);
        this.magia.setEnabled(false);
        this.movimiento.setEnabled(false);
        this.trampa.setEnabled(false);
        
        this.terminarTurno.setText("Terminar turno");
        this.terminarTurno.setFont(fuente);
        this.terminarTurno.setForeground(Color.white);
        
        mensaje.setText("");
        mensaje.setFont(new Font(fuente.getName(), Font.TRUETYPE_FONT, 16));
                        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_batalla.png");
        this.add(panelFondo);
        panelFondo.setSize(this.getSize());
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

        setBackground(new java.awt.Color(51, 51, 51));
        setBorder(null);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        mensaje.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        mensaje.setForeground(new java.awt.Color(255, 255, 255));
        mensaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mensaje.setText("Mensaje");
        getContentPane().add(mensaje);
        mensaje.setBounds(180, 10, 450, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Agrega una vista de información de jugador a la vista de batalla.
     * @param jug Jugador para el cual se creará la vista.
     */
    public void agregarJugador(Jugador jug){
        this.vistasJugador.add(new SubVistaInfoJugadorBatalla(this.fuente));
        int i = this.vistasJugador.size() - 1;
        
        this.vistasJugador.get(i).setImagen(
                "/Imagenes/Fondos/fondo_j" + (i + 1) + ".png");
        
        this.vistasJugador.get(i).setLocation(posInfoJug[i][0], posInfoJug[i][1]);
        this.vistasJugador.get(i).setNombreJugador(jug.getNombreJugador());
        
        this.vistasJugador.get(i).setIconoJugador("/Imagenes/Jefes/" +
                jug.getJefeDeTerreno().getNomArchivoImagen() + ".png");
        
        this.tablero.actualizarCasillas();
        
        this.add(this.vistasJugador.get(i), 0);
    }
    
    public SubVistaTablero getTablero() {
        return tablero;
    }

    public BotonImagen getAtaque() {
        return ataque;
    }

    public BotonImagen getInvocacion() {
        return invocacion;
    }

    public BotonImagen getMagia() {
        return magia;
    }

    public BotonImagen getMovimiento() {
        return movimiento;
    }

    public BotonImagen getTrampa() {
        return trampa;
    }

    public BotonImagen getPausa() {
        return pausa;
    }

    public BotonImagen getTerminarTurno() {
        return terminarTurno;
    }
    
    public ArrayList<SubVistaInfoJugadorBatalla> getVistasJugador() {
        return vistasJugador;
    }

    public SubVistaSeleccionDespliegue getVisSelDesp() {
        return visSelDesp;
    }

    public SubVistaSeleccionDados getVisSelDados() {
        return visSelDados;
    }

    public SubVistaLanzamientoDados getVisLanDados() {
        return visLanDados;
    }

    public SubVistaSeleccionCriatura getVisSelCri() {
        return visSelCri;
    }

    public void setTablero(SubVistaTablero tablero) {
        this.tablero = tablero;
    }
    
    public void setMensaje(String mensaje){
        this.mensaje.setText(mensaje);
    }

    public void setVisSelDados(SubVistaSeleccionDados visSelDados) {
        this.visSelDados = visSelDados;
    }
    
    public void setVisLanDados(SubVistaLanzamientoDados visLanDados){
        this.visLanDados = visLanDados;
    }

    public void setVisSelCri(SubVistaSeleccionCriatura visSelCri) {
        this.visSelCri = visSelCri;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel mensaje;
    // End of variables declaration//GEN-END:variables
}
