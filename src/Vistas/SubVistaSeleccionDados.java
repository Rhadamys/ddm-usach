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
public class SubVistaSeleccionDados extends javax.swing.JInternalFrame {
    private SubVistaInfoElemento visInfo;
    private final ArrayList<BotonCheckImagen> panelesDados;
    private final ArrayList<Dado> dados;
    private final ContenedorScroll contenedor;
    private final PanelImagen contenedorDados;
    private final BotonImagen lanzarDados;
    private final PanelImagen iconoDado;
    
    /**
     * Creates new form CompSelDados
     * @param fuente Fuente que se utilizará en esta vista.
     * @param dados Dados del jugador.
     */
    public SubVistaSeleccionDados(Font fuente, ArrayList<Dado> dados) {   
        initComponents();
        
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        
        this.setLayout(null);
        this.setBorder(null);
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));   
        
        this.panelesDados = new ArrayList();
        this.dados = dados;
        
        this.iconoDado = new PanelImagen();
        this.add(this.iconoDado);
        this.iconoDado.setSize(150, 150);
        this.iconoDado.setLocation(90, 375);
        
        this.L1.setFont(fuente);
        this.L2.setFont(fuente);
        this.L3.setFont(fuente);
        this.nivel.setFont(fuente);
        this.nombreCriatura.setFont(fuente);
        this.cantidadDados.setFont(fuente);
        this.titulo.setFont(new Font(fuente.getName(), Font.TRUETYPE_FONT, 18));
        
        this.nivel.setText("");
        this.nombreCriatura.setText("");
        
        this.contenedorDados = new PanelImagen();
        this.contenedorDados.setLayout(null);
        
        this.contenedor = new ContenedorScroll();    
        this.add(this.contenedor);
        this.contenedor.setSize(598, 335);
        this.contenedor.setLocation(101, 84);
        this.contenedor.setViewportView(this.contenedorDados);
        this.contenedor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.contenedor.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        final int N_COLUMNAS = 4;
        final int LADO = 100;
        final int SEP = (this.contenedor.getWidth() - N_COLUMNAS * LADO) / (N_COLUMNAS + 1);
        final int MARCO = 20;
        int columna = 0;
        int fila = -1;
        
        for (Dado dado: dados){
            fila = columna == 0 ? ++fila : fila;
            
            BotonCheckImagen marcoDado = new BotonCheckImagen("/Imagenes/vacio.png");
            this.contenedorDados.add(marcoDado);
            marcoDado.setSize(LADO + MARCO, LADO + MARCO);
            marcoDado.setImagenSobre("/Imagenes/Otros/marco_seleccion.png");
            marcoDado.setImagenSelNormal("/Imagenes/Otros/marco_seleccion.png");
            marcoDado.setImagenSelSobre("/Imagenes/Otros/marco_seleccion.png");
            marcoDado.setLocation((SEP + LADO) * columna + SEP - MARCO / 2, (SEP + LADO) * fila + SEP - MARCO / 2);
            
            PanelImagen iconoCriatura = new PanelImagen("/Imagenes/Criaturas/"
                    + dado.getCriatura().getNomArchivoImagen() + ".png");
            this.contenedorDados.add(iconoCriatura);
            iconoCriatura.setSize(LADO, LADO);
            iconoCriatura.setLocation((SEP + LADO) * columna + SEP, (SEP + LADO) * fila + SEP);
            
            panelesDados.add(marcoDado);

            columna = columna == (N_COLUMNAS - 1)? 0: ++columna;
        }
        
        this.contenedorDados.setPreferredSize(new Dimension(640, (LADO + SEP) * (fila + 1) + SEP));
        
        this.lanzarDados = new BotonImagen("/Imagenes/Botones/boton.png");
        this.add(lanzarDados);
        this.lanzarDados.setText("Lanzar dados");
        this.lanzarDados.setFont(fuente);
        this.lanzarDados.setForeground(Color.white);
        this.lanzarDados.setSize(200, 50);
        this.lanzarDados.setLocation(480, 455);
        this.lanzarDados.setImagenSobre("/Imagenes/Botones/boton_sobre.png");
        this.lanzarDados.setImagenPresionado("/Imagenes/Botones/boton_presionado.png");
        
        PanelImagen panelFondo = new PanelImagen("/Imagenes/Fondos/fondo_seleccion_2.png");
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

        L1 = new javax.swing.JLabel();
        L2 = new javax.swing.JLabel();
        nivel = new javax.swing.JLabel();
        nombreCriatura = new javax.swing.JLabel();
        L3 = new javax.swing.JLabel();
        cantidadDados = new javax.swing.JLabel();
        titulo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        L1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L1.setForeground(java.awt.Color.red);
        L1.setText("Nivel:");
        getContentPane().add(L1);
        L1.setBounds(260, 435, 80, 20);

        L2.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L2.setForeground(java.awt.Color.red);
        L2.setText("Criatura:");
        getContentPane().add(L2);
        L2.setBounds(260, 455, 80, 20);

        nivel.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        nivel.setForeground(java.awt.Color.red);
        nivel.setText("Nivel");
        getContentPane().add(nivel);
        nivel.setBounds(350, 435, 150, 20);

        nombreCriatura.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        nombreCriatura.setForeground(java.awt.Color.red);
        nombreCriatura.setText("Nombre criatura");
        getContentPane().add(nombreCriatura);
        nombreCriatura.setBounds(350, 455, 150, 20);

        L3.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L3.setForeground(java.awt.Color.red);
        L3.setText("Dados seleccionados:");
        getContentPane().add(L3);
        L3.setBounds(260, 495, 160, 17);

        cantidadDados.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        cantidadDados.setForeground(java.awt.Color.red);
        cantidadDados.setText("0");
        getContentPane().add(cantidadDados);
        cantidadDados.setBounds(430, 495, 70, 17);

        titulo.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("<html><center>Selecciona los dados que deseas lanzar. Puedes mantener el mouse sobre alguno de los dados para ver información de la criatura del dado. Luego, presiona \"Lanzar dados\".</center></html>");
        getContentPane().add(titulo);
        titulo.setBounds(50, 0, 700, 60);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void setInfoDado(BotonCheckImagen panelDado){
        if(panelDado != null){
            Dado dado = getDado(panelDado);
            this.iconoDado.setImagen("/Imagenes/Dados/dado_" + dado.getNivel() + ".gif");
            this.nivel.setText(String.valueOf(dado.getNivel()));
            this.nombreCriatura.setText(dado.getCriatura().getNombre());
        }else{
            this.iconoDado.setImagen("/Imagenes/vacio.png");
            this.nivel.setText("");
            this.nombreCriatura.setText("");
        }
        
        this.cantidadDados.setText(String.valueOf(this.cantidadSeleccionados()));
    }
    
    public ArrayList<BotonCheckImagen> getPanelesDados() {
        return panelesDados;
    }

    public ArrayList<Dado> getDadosSeleccionados() {
        ArrayList<Dado> dadosSeleccionados = new ArrayList();
        for(int i = 0; i < panelesDados.size(); i++){
            if(panelesDados.get(i).isSelected()){
                dadosSeleccionados.add(dados.get(i));
            }
        }
        
        return dadosSeleccionados;
    }

    public BotonImagen getLanzarDados() {
        return lanzarDados;
    }

    public SubVistaInfoElemento getVisInfo() {
        return visInfo;
    }
    
    public int cantidadSeleccionados(){
        int seleccionados = 0;
        for(int i = 0; i < panelesDados.size(); i++){
            if(panelesDados.get(i).isSelected()){
                seleccionados += 1;
            }
        }
        return seleccionados;
    }
    
    public Dado getDado(BotonCheckImagen panelDado){
        return this.dados.get(panelesDados.indexOf(panelDado));
    }

    public void setVisInfo(SubVistaInfoElemento visInfo) {
        this.visInfo = visInfo;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel L1;
    private javax.swing.JLabel L2;
    private javax.swing.JLabel L3;
    private javax.swing.JLabel cantidadDados;
    private javax.swing.JLabel nivel;
    private javax.swing.JLabel nombreCriatura;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
