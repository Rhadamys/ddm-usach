/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Dado;
import Otros.BotonCheckImagen;
import Otros.BotonImagen;
import Otros.ContenedorScroll;
import Otros.PanelImagen;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author mam28
 */
public class VistaModificarPuzzle extends javax.swing.JInternalFrame {
    private SubVistaInfoElemento visInfo;
    private final ArrayList<PanelImagen> iconosDados1;
    private final ArrayList<PanelImagen> iconosDados2;
    private final ArrayList<BotonCheckImagen> panelesDados1;
    private final ArrayList<BotonCheckImagen> panelesDados2;
    private final ArrayList<Dado> dadosPuzzle;
    private final ArrayList<Dado> dadosNoEnPuzzle;
    private final ContenedorScroll contenedor1;
    private final PanelImagen contenedorDados1;
    private final ContenedorScroll contenedor2;
    private final PanelImagen contenedorDados2;
    private final BotonImagen intercambiarDados;
    private final BotonImagen guardarCambios;
    private final BotonImagen volver;

    /**
     * Creates new form VistaModificarPuzle
     * @param fuente Fuente que se utilizará en esta vista.
     * @param dados Dados del jugador.
     */
    public VistaModificarPuzzle(Font fuente, ArrayList<Dado> dados) {
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setLayout(null);
        this.setBorder(null);
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));  
        
        this.volver = new BotonImagen("/Imagenes/Botones/atras.png");
        this.add(this.volver);
        this.volver.setImagenSobre("/Imagenes/Botones/atras_sobre.png");
        this.volver.setImagenPresionado("/Imagenes/Botones/atras_presionado.png");
        this.volver.setLocation(20, 20);
        this.volver.setSize(50, 50); 
        
        this.panelesDados1 = new ArrayList();
        this.panelesDados2 = new ArrayList();
        this.dadosPuzzle = new ArrayList();
        this.dadosNoEnPuzzle = new ArrayList();
        this.iconosDados1 = new ArrayList();
        this.iconosDados2 = new ArrayList();
        
        this.contenedorDados1 = new PanelImagen();
        this.contenedorDados1.setLayout(null);
        
        this.contenedor1 = new ContenedorScroll();    
        this.add(this.contenedor1);
        this.contenedor1.setSize(554, 136);
        this.contenedor1.setLocation(123, 105);
        this.contenedor1.setViewportView(this.contenedorDados1);
        this.contenedor1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.contenedor1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        final int LADO = 100;
        final int SEP = (this.contenedor1.getHeight() - LADO) / 2;
        final int MARCO = 20;
        int columna = 0;
        
        for (Dado dado: dados){
            if(dado.isParaJugar()){
                this.dadosPuzzle.add(dado);

                BotonCheckImagen marcoDado = new BotonCheckImagen("/Imagenes/vacio.png");
                this.contenedorDados1.add(marcoDado);
                marcoDado.setSize(LADO + MARCO, LADO + MARCO);
                marcoDado.setImagenSobre("/Imagenes/Otros/marco_seleccion.png");
                marcoDado.setImagenSelNormal("/Imagenes/Otros/marco_seleccion.png");
                marcoDado.setImagenSelSobre("/Imagenes/Otros/marco_seleccion.png");
                marcoDado.setLocation((SEP + LADO) * columna + SEP - MARCO / 2, SEP - MARCO / 2 - 5);

                PanelImagen iconoCriatura = new PanelImagen("/Imagenes/Criaturas/"
                        + dado.getCriatura().getNomArchivoImagen() + ".png");
                this.contenedorDados1.add(iconoCriatura);
                iconoCriatura.setSize(LADO, LADO);
                iconoCriatura.setLocation((SEP + LADO) * columna + SEP, SEP - 5);

                panelesDados1.add(marcoDado);
                iconosDados1.add(iconoCriatura);
                
                columna++;
            }
        }
        
        this.contenedorDados1.setPreferredSize(new Dimension((LADO + SEP) * columna + SEP, contenedor1.getHeight()));
        
        this.contenedorDados2 = new PanelImagen();
        this.contenedorDados2.setLayout(null);
        
        this.contenedor2 = new ContenedorScroll();    
        this.add(this.contenedor2);
        this.contenedor2.setSize(556, 136);
        this.contenedor2.setLocation(123, 263);
        this.contenedor2.setViewportView(this.contenedorDados2);
        this.contenedor2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.contenedor2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        columna = 0;
        for (Dado dado: dados){
            if(!dado.isParaJugar()){
                this.dadosNoEnPuzzle.add(dado);
                
                BotonCheckImagen marcoDado = new BotonCheckImagen("/Imagenes/vacio.png");
                this.contenedorDados2.add(marcoDado);
                marcoDado.setSize(LADO + MARCO, LADO + MARCO);
                marcoDado.setImagenSobre("/Imagenes/Otros/marco_seleccion.png");
                marcoDado.setImagenSelNormal("/Imagenes/Otros/marco_seleccion.png");
                marcoDado.setImagenSelSobre("/Imagenes/Otros/marco_seleccion.png");
                marcoDado.setLocation((SEP + LADO) * columna + SEP - MARCO / 2, SEP - MARCO / 2 - 5);

                PanelImagen iconoCriatura = new PanelImagen("/Imagenes/Criaturas/"
                        + dado.getCriatura().getNomArchivoImagen() + ".png");
                this.contenedorDados2.add(iconoCriatura);
                iconoCriatura.setSize(LADO, LADO);
                iconoCriatura.setLocation((SEP + LADO) * columna + SEP, SEP - 5);

                panelesDados2.add(marcoDado);
                iconosDados2.add(iconoCriatura);

                columna++;
            }
        }
        
        this.contenedorDados2.setPreferredSize(new Dimension((LADO + SEP) * (columna + 1) + SEP, contenedor2.getHeight()));
        
        this.intercambiarDados = new BotonImagen("/Imagenes/Botones/boton.png");
        this.add(intercambiarDados);
        this.intercambiarDados.setText("Intercambiar dados");
        this.intercambiarDados.setFont(fuente);
        this.intercambiarDados.setForeground(Color.white);
        this.intercambiarDados.setSize(170, 40);
        this.intercambiarDados.setLocation(520, 435);
        this.intercambiarDados.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.intercambiarDados.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        
        this.guardarCambios = new BotonImagen("/Imagenes/Botones/boton.png");
        this.add(guardarCambios);
        this.guardarCambios.setText("Guardar cambios");
        this.guardarCambios.setFont(fuente);
        this.guardarCambios.setForeground(Color.white);
        this.guardarCambios.setSize(170, 40);
        this.guardarCambios.setLocation(520, 485);
        this.guardarCambios.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.guardarCambios.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        this.guardarCambios.setImagenDeshabilitado("/Imagenes/Botones/boton_deshabilitado.png");
        this.guardarCambios.setEnabled(false);
        
        this.titulo.setFont(new Font(fuente.getName(), Font.TRUETYPE_FONT, 18));
        this.L1.setFont(fuente);
        this.L2.setFont(fuente);
        this.L3.setFont(fuente);
        this.L4.setFont(fuente);
        this.L5.setFont(fuente);
        this.L6.setFont(fuente);
        this.nivel1.setFont(fuente);
        this.nombreCriatura1.setFont(fuente);
        this.nivel2.setFont(fuente);
        this.nombreCriatura2.setFont(fuente);
        
        this.nivel1.setText("");
        this.nombreCriatura1.setText("");
        this.nivel2.setText("");
        this.nombreCriatura2.setText("");
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_seleccion_4.png");
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

        titulo = new javax.swing.JLabel();
        L1 = new javax.swing.JLabel();
        L2 = new javax.swing.JLabel();
        nivel1 = new javax.swing.JLabel();
        nombreCriatura1 = new javax.swing.JLabel();
        L3 = new javax.swing.JLabel();
        L4 = new javax.swing.JLabel();
        nombreCriatura2 = new javax.swing.JLabel();
        nivel2 = new javax.swing.JLabel();
        L5 = new javax.swing.JLabel();
        L6 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));
        setBorder(null);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        titulo.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("<html><center>En la región superior, selecciona el dado que quieres sacar. En la región inferior, selecciona el dado que deseas agregar. Luego pulsa \"Intercambiar dados\".</center></html>");
        getContentPane().add(titulo);
        titulo.setBounds(80, 0, 640, 60);

        L1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L1.setForeground(java.awt.Color.red);
        L1.setText("Nivel:");
        getContentPane().add(L1);
        L1.setBounds(230, 435, 80, 20);

        L2.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L2.setForeground(java.awt.Color.red);
        L2.setText("Criatura:");
        getContentPane().add(L2);
        L2.setBounds(230, 455, 80, 20);

        nivel1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        nivel1.setForeground(java.awt.Color.red);
        nivel1.setText("Nivel");
        getContentPane().add(nivel1);
        nivel1.setBounds(320, 435, 150, 20);

        nombreCriatura1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        nombreCriatura1.setForeground(java.awt.Color.red);
        nombreCriatura1.setText("Nombre criatura");
        getContentPane().add(nombreCriatura1);
        nombreCriatura1.setBounds(320, 455, 150, 20);

        L3.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L3.setForeground(java.awt.Color.red);
        L3.setText("Nivel:");
        getContentPane().add(L3);
        L3.setBounds(230, 485, 80, 20);

        L4.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L4.setForeground(java.awt.Color.red);
        L4.setText("Criatura:");
        getContentPane().add(L4);
        L4.setBounds(230, 505, 80, 20);

        nombreCriatura2.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        nombreCriatura2.setForeground(java.awt.Color.red);
        nombreCriatura2.setText("Nombre criatura");
        getContentPane().add(nombreCriatura2);
        nombreCriatura2.setBounds(320, 505, 150, 20);

        nivel2.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        nivel2.setForeground(java.awt.Color.red);
        nivel2.setText("Nivel");
        getContentPane().add(nivel2);
        nivel2.setBounds(320, 485, 150, 20);

        L5.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L5.setForeground(new java.awt.Color(255, 255, 255));
        L5.setText("Dado a sacar:");
        getContentPane().add(L5);
        L5.setBounds(110, 445, 120, 20);

        L6.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L6.setForeground(new java.awt.Color(255, 255, 255));
        L6.setText("Se cambia por:");
        getContentPane().add(L6);
        L6.setBounds(110, 490, 120, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public BotonCheckImagen getPanelDadoPuzzle(int i){
        return this.panelesDados1.get(i);
    }
    
    public BotonCheckImagen getPanelDadoNoEnPuzzle(int i){
        return this.panelesDados2.get(i);
    }

    public BotonImagen getIntercambiarDados() {
        return intercambiarDados;
    }

    public BotonImagen getGuardarCambios() {
        return guardarCambios;
    }

    public BotonImagen getVolver() {
        return volver;
    }
    
    public Dado getDadoPuzzleSeleccionado(){
        try{
            return dadosPuzzle.get(this.indiceDadoPuzzleSeleccionado());
        }catch(Exception e){
            return null;
        }
    }
    
    public Dado getDadoNoEnPuzzleSeleccionado(){
        try{
            return dadosNoEnPuzzle.get(this.indiceDadoNoEnPuzzleSeleccionado());
        }catch(Exception e){
            return null;
        }
    }
    
    public int cantidadDadosPuzzle(){
        return this.dadosPuzzle.size();
    }
    
    public int cantidadDadosNoEnPuzzle(){
        return this.dadosNoEnPuzzle.size();
    }
    
    public boolean sePuedeSeleccionarDadoPuzzle(){
        int seleccionados = 0;
        for(BotonCheckImagen panelDado: panelesDados1){
            if(panelDado.isSelected()){
                seleccionados++;
            }
        }
        
        if(seleccionados == 0 || seleccionados == 1){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean sePuedeSeleccionarDadoNoEnPuzzle(){
        int seleccionados = 0;
        for(BotonCheckImagen panelDado: panelesDados2){
            if(panelDado.isSelected()){
                seleccionados++;
            }
        }
        
        if(seleccionados == 0 || seleccionados == 1){
            return true;
        }else{
            return false;
        }
    }
    
    public void verificarPanelDadoPuzzle(BotonCheckImagen panelDado){
        if(!sePuedeSeleccionarDadoPuzzle()){
            panelDado.setSelected(false);
        }else{
            if(this.indiceDadoPuzzleSeleccionado() == -1){
                this.borrarInfoDado1();
            }else{
                this.nivel1.setText(String.valueOf(this.getDadoPuzzleSeleccionado().getNivel()));
                this.nombreCriatura1.setText(this.getDadoPuzzleSeleccionado().getCriatura().getNombre());
            }
        }
    }
    
    public void verificarPanelDadoNoEnPuzzle(BotonCheckImagen panelDado){
        if(!sePuedeSeleccionarDadoNoEnPuzzle()){
            panelDado.setSelected(false);
        }else{
            if(this.indiceDadoNoEnPuzzleSeleccionado() == -1){
                this.borrarInfoDado2();
            }else{
                this.nivel2.setText(String.valueOf(this.getDadoNoEnPuzzleSeleccionado().getNivel()));
                this.nombreCriatura2.setText(this.getDadoNoEnPuzzleSeleccionado().getCriatura().getNombre());
            }
        }
    }
    
    public int indiceDadoPuzzleSeleccionado(){
        for(int i = 0; i < panelesDados1.size(); i++){
            if(panelesDados1.get(i).isSelected()){
                return i;
            }
        }
        
        return -1;
    }
    
    public int indiceDadoNoEnPuzzleSeleccionado(){
        for(int i = 0; i < panelesDados2.size(); i++){
            if(panelesDados2.get(i).isSelected()){
                return i;
            }
        }
        
        return -1;
    }
    
    public void intercambiarDados(){
        int indice1 = this.indiceDadoPuzzleSeleccionado();
        int indice2 = this.indiceDadoNoEnPuzzleSeleccionado();
        
        Dado dadoSacado = this.getDadoPuzzleSeleccionado();
        Dado dadoColocado = this.getDadoNoEnPuzzleSeleccionado();
        
        dadoSacado.setParaJugar(false);
        dadoColocado.setParaJugar(true);
        
        dadosPuzzle.set(indice1, dadoColocado);
        dadosNoEnPuzzle.set(indice2, dadoSacado);
        
        iconosDados1.get(indice1).setImagen("/Imagenes/Criaturas/"
                        + dadoColocado.getCriatura().getNomArchivoImagen() + ".png");
        iconosDados2.get(indice2).setImagen("/Imagenes/Criaturas/"
                        + dadoSacado.getCriatura().getNomArchivoImagen() + ".png");
        
        panelesDados1.get(indice1).setSelected(false);
        panelesDados2.get(indice2).setSelected(false);
        
        borrarInfoDado1();
        borrarInfoDado2();
        
        this.guardarCambios.setEnabled(true);
    }
    
    public void borrarInfoDado1(){
        nivel1.setText("");
        nombreCriatura1.setText("");
    }
    
    public void borrarInfoDado2(){
        nivel2.setText("");
        nombreCriatura2.setText("");
    }
    
    public ArrayList<Dado> getPuzzleModificado(){
        ArrayList<Dado> nuevoPuzzle = new ArrayList();
        nuevoPuzzle.addAll(dadosPuzzle);
        nuevoPuzzle.addAll(dadosNoEnPuzzle);
        return nuevoPuzzle;
    }
    
    public void puzzleGuardado(){
        this.guardarCambios.setEnabled(false);
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel L1;
    private javax.swing.JLabel L2;
    private javax.swing.JLabel L3;
    private javax.swing.JLabel L4;
    private javax.swing.JLabel L5;
    private javax.swing.JLabel L6;
    private javax.swing.JLabel nivel1;
    private javax.swing.JLabel nivel2;
    private javax.swing.JLabel nombreCriatura1;
    private javax.swing.JLabel nombreCriatura2;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
