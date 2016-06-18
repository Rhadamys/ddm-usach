/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Jugador;
import Otros.BotonImagen;
import Otros.Constantes;
import Otros.VistaPersonalizada;
import java.util.ArrayList;

/**
 *
 * @author mam28
 */
public class VistaBatalla extends VistaPersonalizada {
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
    private SubVistaSeleccionTrampa visSelTram;
    private SubVistaCriaturaRevivir visCriRev;
    private SubVistaSeleccionMagia visSelMag;
    private final SubVistaMagiasActivadas visMagAc;
    private final int[][] posInfoJug = {{5, 5}, {655, 5}, {5, 405}, {655, 405}};
    
    /**
     * Creates new form VistaBatalla
     */
    public VistaBatalla() {
        initComponents();
        
        this.visSelDesp = new SubVistaSeleccionDespliegue();
        this.vistasJugador = new ArrayList();
        
        this.ataque = new BotonImagen(Constantes.BTN_ATAQUE);
        this.invocacion = new BotonImagen(Constantes.BTN_INVOCACION);
        this.magia = new BotonImagen(Constantes.BTN_MAGIA);
        this.movimiento = new BotonImagen(Constantes.BTN_MOVIMIENTO);
        this.trampa = new BotonImagen(Constantes.BTN_TRAMPA);
        this.pausa = new BotonImagen(Constantes.BTN_PAUSA);
        this.terminarTurno = new BotonImagen(Constantes.BTN_NORMAL);
        
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
        
        this.ataque.setEnabled(false);
        this.invocacion.setEnabled(false);
        this.magia.setEnabled(false);
        this.movimiento.setEnabled(false);
        this.trampa.setEnabled(false);
        
        this.terminarTurno.setText("Terminar turno");
        
        mensaje.setText("");
        mensaje.setFont(Constantes.FUENTE_14PX);
        
        this.visMagAc = new SubVistaMagiasActivadas();
        this.add(visMagAc);
                        
        this.setImagenFondo(Constantes.FONDO_BATALLA);
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
        mensaje.setBounds(180, 0, 450, 50);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Agrega una vista de información de jugador a la vista de batalla.
     * @param jug Jugador para el cual se creará la vista.
     */
    public void agregarJugador(Jugador jug){
        this.vistasJugador.add(new SubVistaInfoJugadorBatalla());
        int i = this.vistasJugador.size() - 1;
        
        this.vistasJugador.get(i).setImagen(
                "/Imagenes/Fondos/fondo_j" + (i + 1) + ".png");
        
        this.vistasJugador.get(i).setLocation(posInfoJug[i][0], posInfoJug[i][1]);
        this.vistasJugador.get(i).setNombreJugador(jug.getNombreJugador());
        
        this.vistasJugador.get(i).setIconoJugador("/Imagenes/Jefes/" +
                jug.getJefeDeTerreno().getNomArchivoImagen() + ".png");
        
        this.vistasJugador.get(i).setVidaMaximaJugador(jug.getJefeDeTerreno().getVidaMaxima());        
        this.add(this.vistasJugador.get(i), 0);
    }
    
    public void habilitarBotones(){
        this.getAtaque().setEnabled(true);
        this.getMagia().setEnabled(true);
        this.getMovimiento().setEnabled(true);
        this.getTrampa().setEnabled(true);
        this.getTerminarTurno().setEnabled(true);
        this.getPausa().setEnabled(true);
    }
    
    public void deshabilitarBotones(){
        this.getAtaque().setEnabled(false);
        this.getInvocacion().setEnabled(false);
        this.getMagia().setEnabled(false);
        this.getMovimiento().setEnabled(false);
        this.getTrampa().setEnabled(false);
        this.getTerminarTurno().setEnabled(false);
        this.getPausa().setEnabled(false);
        this.getTablero().getCasilla(0, 0).requestFocus();
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
    
    public SubVistaInfoJugadorBatalla getVistaJugador(int i){
        return vistasJugador.get(i);
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

    public SubVistaSeleccionTrampa getVisSelTram() {
        return visSelTram;
    }

    public SubVistaCriaturaRevivir getVisCriRev() {
        return visCriRev;
    }

    public SubVistaSeleccionMagia getVisSelMag() {
        return visSelMag;
    }

    public SubVistaMagiasActivadas getVisMagAc() {
        return visMagAc;
    }

    public void setTablero(SubVistaTablero tablero) {
        this.tablero = tablero;
    }
    
    public void setMensaje(String mensaje){
        this.mensaje.setText("<html><center>" + mensaje + "</center></html>");
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

    public void setVisSelTram(SubVistaSeleccionTrampa visSelTram) {
        this.visSelTram = visSelTram;
    }

    public void setVisCriRev(SubVistaCriaturaRevivir visCriRev) {
        this.visCriRev = visCriRev;
    }

    public void setVisSelMag(SubVistaSeleccionMagia visSelMag) {
        this.visSelMag = visSelMag;
    }
           
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel mensaje;
    // End of variables declaration//GEN-END:variables
}
