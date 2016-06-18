/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Otros.BotonImagen;
import Otros.Constantes;
import Otros.PanelImagen;
import Otros.VistaPersonalizada;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public class SubVistaSeleccionMagia extends VistaPersonalizada implements MouseListener{
    private final ArrayList<HashMap<String, String>> infoMagias;
    private final ArrayList<BotonImagen> panelesMagias;
    private final ArrayList<int[]> magias;
    private final BotonImagen volver;
    private SubVistaInfoElemento visInfoEl;
    private Timer timerVisInfoEl;

    /**
     * Creates new form SubVistaSeleccionTrampa
     * @param magias Magias disponibles.
     * @param puntosMagia Puntos de magia que tiene el jugador.
     */
    public SubVistaSeleccionMagia(ArrayList<int[]> magias, int puntosMagia) {
        initComponents();
        
        infoMagias = new ArrayList();
        HashMap<String, String> lluviaTorrencial = new HashMap();
        lluviaTorrencial.put("Nombre", "Lluvia torrencial");
        lluviaTorrencial.put("NombreArchivoImagen", "magia_1");
        lluviaTorrencial.put("Descripcion", "<html><p align=\"justify\">Hace que las criaturas enemigas gasten dos unidades de movimiento por cada cuadro que deseen desplazarse durante los próximos 3 turnos del juego.</p></html>");
        lluviaTorrencial.put("Costo", "10");
        infoMagias.add(lluviaTorrencial);
        
        HashMap<String, String> hierbasVenenosas = new HashMap();
        hierbasVenenosas.put("Nombre", "Hierbas venenosas");
        hierbasVenenosas.put("NombreArchivoImagen", "magia_2");
        hierbasVenenosas.put("Descripcion", "<html><p align=\"justify\">El jugador puede seleccionar a 3 criaturas oponentes, durante los próximos 3 turnos estas criaturas recibirán un daño igual al 20% de la vida máxima que estas posean.</p></html>");
        hierbasVenenosas.put("Costo", "15");
        infoMagias.add(hierbasVenenosas);
        
        HashMap<String, String> meteoritosDeFuego = new HashMap();
        meteoritosDeFuego.put("Nombre", "Meteoritos de fuego");
        meteoritosDeFuego.put("NombreArchivoImagen", "magia_3");
        meteoritosDeFuego.put("Descripcion", "<html><p align=\"justify\">El jugador selecciona un lugar del terreno, dentro de un radio de 5 cuadros del terreno, cualquier criatura enemiga que esté ubicada en esta sección recibirá un daño de 30% de la vida máxima que posea. Este efecto dura 3 turnos.</p></html>");
        meteoritosDeFuego.put("Costo", "30");
        infoMagias.add(meteoritosDeFuego);
        
        this.panelesMagias = new ArrayList();
        this.magias = magias;
        
        this.volver = new BotonImagen(Constantes.BTN_ATRAS);
        this.add(this.volver);
        this.volver.setLocation(20, 20);
        this.volver.setSize(50, 50);
        
        this.volver.addMouseListener(this);
        
        final int N_COLUMNAS = magias.size();
        final int LADO = 100;
        final int SEP = (640 - N_COLUMNAS * LADO) / (N_COLUMNAS + 1);
        final int MARCO = 20;
        int columna = 0;
        
        for (int[] magia: magias){
            HashMap<String, String> infoMagia = infoMagias.get(magia[0] - 1);
            if(puntosMagia >= Integer.parseInt(infoMagia.get("Costo"))){
                BotonImagen marcoMagia = new BotonImagen(Constantes.BTN_MARCO);
                this.add(marcoMagia);
                marcoMagia.setSize(LADO + MARCO, LADO + MARCO);
                marcoMagia.setLocation((SEP + LADO) * columna + SEP - MARCO / 2 + 80, (this.getHeight() - LADO - MARCO) / 2);
                marcoMagia.setName(String.valueOf(magia[0] - 1));
                marcoMagia.addMouseListener(this);

                PanelImagen iconoMagia = new PanelImagen(Constantes.RUTA_BOTONES
                        + infoMagia.get("NombreArchivoImagen") + Constantes.EXT1);
                this.add(iconoMagia);
                iconoMagia.setSize(LADO, LADO);
                iconoMagia.setLocation((SEP + LADO) * columna + SEP + 80, (this.getHeight() - LADO) / 2);

                panelesMagias.add(marcoMagia);

                columna = columna == (N_COLUMNAS - 1)? 0: ++columna;
            }
        }
        
        this.titulo.setFont(Constantes.FUENTE_24PX);
        
        this.setImagenFondo(Constantes.FONDO_SELECCION_3);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titulo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        titulo.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Selecciona la magia que deseas activar.");
        getContentPane().add(titulo);
        titulo.setBounds(0, 10, 790, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public BotonImagen getPanelMagia(int i){
        return this.panelesMagias.get(i);
    }
    
    public int cantidadMagias(){
        return this.panelesMagias.size();
    }
    
    public int[] getMagia(BotonImagen panelTrampa){
        return magias.get(this.panelesMagias.indexOf(panelTrampa));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables

    public void posicionarVistaInfoElemento(int x){
        if(this.visInfoEl != null){
            this.visInfoEl.setLocation(x > 400 ? 10: 540, 100);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        if(me.getComponent() == volver){
            this.dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        // Nada
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        // Nada
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        if(me.getComponent() != volver){
            HashMap<String, String> infoMagia = this.infoMagias.get(Integer.parseInt(me.getComponent().getName()));
            this.visInfoEl = new SubVistaInfoElemento(
                    infoMagia.get("Nombre"),
                    infoMagia.get("NombreArchivoImagen"),
                    infoMagia.get("Descripcion"));
            this.add(visInfoEl, 0);
            
            this.posicionarVistaInfoElemento(me.getComponent().getX() + 80);
            
            timerVisInfoEl = new Timer();
            timerVisInfoEl.schedule(new TimerTask(){
                @Override
                public void run(){
                    visInfoEl.setVisible(true);
                    this.cancel();
                    timerVisInfoEl.cancel();
                }
            }, 1000, 1);
            
            this.repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        try{
            timerVisInfoEl.cancel();
        }catch(Exception e){
            // Nada
        }
        
        try{
            visInfoEl.setVisible(false);
        }catch(Exception e){
            // Nada
        }
    }
}
