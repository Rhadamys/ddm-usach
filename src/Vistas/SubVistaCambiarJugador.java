/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Jugador;
import Modelos.Usuario;
import Otros.BotonImagen;
import Otros.Constantes;
import Otros.ContenedorScroll;
import Otros.PanelImagen;
import Otros.VistaPersonalizada;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JScrollPane;

/**
 *
 * @author mam28
 */
public class SubVistaCambiarJugador extends VistaPersonalizada {
    private final ArrayList<BotonImagen> panelesJugadores;
    private final ArrayList<Jugador> jugadores;
    private final JScrollPane contenedor;
    private final PanelImagen contenedorJugadores;
    private final BotonImagen volver;
    
    /**
     * Creates new form CompCambiarJugador
     * @param jugDisponibles Jugadores disponibles para el cambio
     */
    public SubVistaCambiarJugador(ArrayList<Jugador> jugDisponibles) {
        initComponents();
        
        this.volver = new BotonImagen(Constantes.BTN_ATRAS);
        this.add(this.volver);
        this.volver.setLocation(105, 88);
        this.volver.setSize(40, 40);
        
        this.panelesJugadores = new ArrayList<BotonImagen>();
        
        this.contenedorJugadores = new PanelImagen();
        this.contenedorJugadores.setLayout(null);
        
        this.contenedor = new ContenedorScroll();    
        this.add(this.contenedor);
        this.contenedor.setSize(598, 335);
        this.contenedor.setLocation(101, 84);
        this.contenedor.setViewportView(this.contenedorJugadores);
        this.contenedor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.contenedor.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        this.jugadores = jugDisponibles;
        
        final int N_COLUMNAS = 4;
        final int LADO = 90;
        final int SEP = (this.contenedor.getWidth() - N_COLUMNAS * LADO) / (N_COLUMNAS + 1);
        final int MARCO = LADO / 6;
        int columna = 0;
        int fila = -1;
        
        for(Jugador jugador: jugadores){
            fila = columna == 0 ? ++fila : fila;
            
            BotonImagen marcoJugador = new BotonImagen();
            this.contenedorJugadores.add(marcoJugador);
            marcoJugador.setSize(LADO + MARCO, LADO + MARCO);
            marcoJugador.setLocation((SEP + LADO) * columna + SEP - MARCO / 2, (SEP + LADO) * fila + SEP - MARCO / 2);
            marcoJugador.setImagenSobre("/Imagenes/Otros/marco_seleccion.png");
            
            PanelImagen iconoJugador = new PanelImagen("/Imagenes/Jefes/" +
                    jugador.getJefeDeTerreno().getNomArchivoImagen() + ".png");
            this.contenedorJugadores.add(iconoJugador);
            iconoJugador.setSize(LADO, LADO);
            iconoJugador.setLocation((SEP + LADO) * columna + SEP, (SEP + LADO) * fila + SEP);
            
            this.panelesJugadores.add(marcoJugador);

            columna = columna == (N_COLUMNAS - 1)? 0: ++columna;
        }
        
        this.contenedorJugadores.setPreferredSize(new Dimension(640, (LADO + SEP) * (fila + 1) + SEP));
        
        this.nombre.setFont(Constantes.FUENTE_24PX);
        this.tipoJugador.setFont(Constantes.FUENTE_24PX);
        
        this.nombre.setText("");
        this.tipoJugador.setText("");
        this.titulo.setFont(Constantes.FUENTE_24PX);
        
        this.setImagenFondo(Constantes.FONDO_SELECCION_2);
        
        this.volver.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                dispose();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tipoJugador = new javax.swing.JLabel();
        nombre = new javax.swing.JLabel();
        titulo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        tipoJugador.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        tipoJugador.setForeground(new java.awt.Color(204, 0, 0));
        tipoJugador.setText("Tipo jugador");
        getContentPane().add(tipoJugador);
        tipoJugador.setBounds(110, 490, 610, 29);

        nombre.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        nombre.setForeground(new java.awt.Color(255, 255, 255));
        nombre.setText("Nombre");
        getContentPane().add(nombre);
        nombre.setBounds(110, 445, 610, 29);

        titulo.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Selecciona un jugador");
        getContentPane().add(titulo);
        titulo.setBounds(0, 10, 790, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents
     
    public void mostrarInformacionJugador(int indiceJugador){
        this.nombre.setText(this.jugadores.get(indiceJugador).getNombreJugador());
        this.tipoJugador.setText(this.jugadores.get(indiceJugador) instanceof Usuario ? "Humano": "Personaje no jugable");
    }
    
    public void borrarCampos(){
        this.nombre.setText("");
        this.tipoJugador.setText("");
    }

    public ArrayList<BotonImagen> getIconosJugadores() {
        return panelesJugadores;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel tipoJugador;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
