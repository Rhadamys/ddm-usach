/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Criatura;
import Modelos.Dado;
import Modelos.ElementoEnCampo;
import Modelos.JefeDeTerreno;
import Modelos.Jugador;
import Modelos.Trampa;
import Otros.BarraVida;
import Otros.Constantes;
import Otros.PanelImagen;
import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author mam28
 */
public class SubVistaInfoElemento extends PanelImagen {
    private final BarraVida barraVida;

    /**
     * Creates new form SubVistaInfoCriatura
     * @param elemento Elemento para el cual se creará la vista.
     * @param dueno Dueño del elemento mostrado
     */
    public SubVistaInfoElemento(ElementoEnCampo elemento, Jugador dueno) {
        initComponents();
        
        this.setVisible(false);
        this.setSize(250, 400);
        this.setImagen(Constantes.FONDO_INFO_ELEMENTO);
        
        PanelImagen icono = new PanelImagen("/Imagenes/" +
                (elemento instanceof Criatura ? "Criaturas/" : 
                (elemento instanceof JefeDeTerreno ? "Jefes/" : "Otros/")) +
                elemento.getNomArchivoImagen() + ".png");
        this.add(icono);
        icono.setSize(80, 80);
        icono.setLocation(85, 10);
        
        if(dueno != null){
            PanelImagen iconoDueno = new PanelImagen(Constantes.RUTA_JEFES +
                    dueno.getJefeDeTerreno().getNomArchivoImagen() + Constantes.EXT1);
            this.add(iconoDueno);
            iconoDueno.setSize(40, 40);
            iconoDueno.setLocation(22, 30);
            
            JLabel nomDueno = new JLabel(dueno.getNombreJugador());
            this.add(nomDueno);
            nomDueno.setSize(80,20);
            nomDueno.setLocation(2, 70);
            nomDueno.setForeground(Color.white);
            nomDueno.setFont(Constantes.FUENTE_14PX);
            nomDueno.setHorizontalAlignment(JLabel.CENTER);
        }
        
        this.nombre.setText(elemento.getNombre());
        this.nombre.setFont(Constantes.FUENTE_18PX);
        
        this.L1.setFont(Constantes.FUENTE_14PX);
        this.L2.setFont(Constantes.FUENTE_14PX);
        this.L3.setFont(Constantes.FUENTE_14PX);
        
        this.descripcion.setFont(Constantes.FUENTE_14PX);
        this.descripcion.setText(elemento.getDescripcion());
        
        this.barraVida = new BarraVida();
        this.add(this.barraVida);
        this.barraVida.setSize(120, 15);
        this.barraVida.setLocation(100, 140);
        
        String aumento = "";
        if(!(elemento instanceof Trampa)){
            
            if(elemento instanceof Criatura){
                this.barraVida.setMaximum(((Criatura) elemento).getVidaMaxima());
                this.barraVida.setValue(((Criatura) elemento).getVida());
                
                if(((Criatura) elemento).getVidaPorDefecto() < barraVida.getMaximum()){
                    aumento = " (+" + (barraVida.getMaximum() - ((Criatura) elemento).getVidaPorDefecto()) + ")";
                }
                
                this.defensa.setText(String.valueOf(((Criatura) elemento).getDefensa()));
                this.ataque.setText(String.valueOf(((Criatura) elemento).getAtaque()));
                
                if(((Criatura) elemento).getDefensaPorDefecto() < ((Criatura) elemento).getDefensa()){
                    this.defensa.setText(this.defensa.getText() + " (+" + (((Criatura) elemento).getDefensa() - ((Criatura) elemento).getDefensaPorDefecto()) + ")");
                }
                
                if(((Criatura) elemento).getAtaquePorDefecto() < ((Criatura) elemento).getAtaque()){
                    this.ataque.setText(this.ataque.getText() + " (+" + (((Criatura) elemento).getAtaque() - ((Criatura) elemento).getAtaquePorDefecto()) + ")");
                }
                
            }else{
                this.barraVida.setMaximum(((JefeDeTerreno) elemento).getVidaMaxima());
                this.barraVida.setValue(((JefeDeTerreno) elemento).getVida());
                
                this.defensa.setText("No aplica");
                this.ataque.setText("No aplica");
            }
        }else{
            this.barraVida.setMaximum(1);
            this.barraVida.setValue(1);
                
            this.defensa.setText("No aplica");
            this.ataque.setText("No aplica");
        }
            
        this.defensa.setFont(Constantes.FUENTE_14PX);
        this.ataque.setFont(Constantes.FUENTE_14PX);

        this.vida.setFont(Constantes.FUENTE_14PX);
        this.vida.setText(barraVida.getValue() + "/" + barraVida.getMaximum() + aumento);
    }

    /**
     * Creates new form SubVistaInfoCriatura
     * @param nombre
     * @param nomArIm
     * @param descripcion
     */
    public SubVistaInfoElemento(String nombre, String nomArIm, String descripcion) {
        initComponents();
        
        this.setVisible(false);
        this.setSize(250, 400);
        this.setImagen("/Imagenes/Fondos/fondo_info_elemento.png");
        
        PanelImagen icono = new PanelImagen("/Imagenes/Otros/" + nomArIm + ".png");
        this.add(icono);
        icono.setSize(80, 80);
        icono.setLocation(85, 10);
        
        this.nombre.setText(nombre);
        this.nombre.setFont(Constantes.FUENTE_18PX);
        
        this.L1.setFont(Constantes.FUENTE_14PX);
        this.L2.setFont(Constantes.FUENTE_14PX);
        this.L3.setFont(Constantes.FUENTE_14PX);
        
        this.descripcion.setFont(Constantes.FUENTE_14PX);
        this.descripcion.setText(descripcion);
        
        this.barraVida = new BarraVida();
        this.add(this.barraVida);
        this.barraVida.setSize(120, 15);
        this.barraVida.setLocation(100, 140);
        
        this.barraVida.setMaximum(0);
        this.barraVida.setValue(0);
                
        this.defensa.setText("No aplica");
        this.ataque.setText("No aplica");
        this.vida.setText("No aplica");
            
        this.defensa.setFont(Constantes.FUENTE_14PX);
        this.ataque.setFont(Constantes.FUENTE_14PX);
        this.vida.setFont(Constantes.FUENTE_14PX);
    }

    /**
     * Creates new form SubVistaInfoCriatura
     * @param dado Dado para el que se creará la vista
     */
    public SubVistaInfoElemento(Dado dado) {
        initComponents();
        
        this.setVisible(false);
        this.setSize(250, 400);
        this.setImagen(Constantes.FONDO_INFO_ELEMENTO);
        
        PanelImagen icono = new PanelImagen(Constantes.RUTA_DADOS + "dado_" + 
                dado.getNivel() + Constantes.EXT2);
        this.add(icono);
        icono.setSize(80, 80);
        icono.setLocation(85, 10);
        
        this.nombre.setText(dado.getCriatura().getNombre());
        this.nombre.setFont(Constantes.FUENTE_18PX);
        
        this.L1.setFont(Constantes.FUENTE_14PX);
        this.L2.setFont(Constantes.FUENTE_14PX);
        this.L3.setFont(Constantes.FUENTE_14PX);
        
        int carasAtk = 0, carasInv = 0, carasMag = 0, carasMov = 0;
        for(int cara: dado.getCaras()){
            switch(cara){
                case 1:
                    carasAtk++;
                    break;
                case 2:
                    carasInv++;
                    break;
                case 3:
                    carasMag++;
                    break;
                case 4:
                    carasMov++;
                    break;
            }
        }
        
        int probAtk = carasAtk * 100 / 6;
        int probInv = carasInv * 100 / 6;
        int probMag = carasMag * 100 / 6;
        int probMov = carasMov * 100 / 6;
        int probTra = 100 - probAtk - probInv - probMag - probMov;
        
        this.descripcion.setFont(Constantes.FUENTE_14PX);
        this.descripcion.setText(
                "<html>Nivel: " + dado.getNivel() +
                "<br>Prob. Atk.: " + probAtk + "%" +
                "<br>Prob. Inv.: " + probInv + "%" +
                "<br>Prob. Mag.: " + probMag + "%" +
                "<br>Prob. Mov.: " + probMov + "%" +
                "<br>Prob. Tra.: " + probTra + "%</html>");
        
        this.barraVida = new BarraVida();
        this.add(this.barraVida);
        this.barraVida.setSize(120, 15);
        this.barraVida.setLocation(100, 140);
        
        this.barraVida.setMaximum(dado.getCriatura().getVidaMaxima());
        this.barraVida.setValue(this.barraVida.getMaximum());
            
        this.defensa.setFont(Constantes.FUENTE_14PX);
        this.ataque.setFont(Constantes.FUENTE_14PX);

        this.vida.setFont(Constantes.FUENTE_14PX);
        this.vida.setText(String.valueOf(dado.getCriatura().getVidaMaxima()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nombre = new javax.swing.JLabel();
        L1 = new javax.swing.JLabel();
        L2 = new javax.swing.JLabel();
        L3 = new javax.swing.JLabel();
        descripcion = new javax.swing.JLabel();
        vida = new javax.swing.JLabel();
        defensa = new javax.swing.JLabel();
        ataque = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));
        setMaximumSize(new java.awt.Dimension(360, 400));
        setMinimumSize(new java.awt.Dimension(360, 400));

        nombre.setFont(new java.awt.Font("Consolas", 0, 24)); // NOI18N
        nombre.setForeground(new java.awt.Color(255, 255, 255));
        nombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombre.setText("Nombre");
        add(nombre);
        nombre.setBounds(0, 100, 250, 29);

        L1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L1.setForeground(new java.awt.Color(255, 255, 255));
        L1.setText("Vida:");
        add(L1);
        L1.setBounds(20, 140, 70, 17);

        L2.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L2.setForeground(new java.awt.Color(255, 255, 255));
        L2.setText("Defensa:");
        add(L2);
        L2.setBounds(20, 180, 70, 17);

        L3.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        L3.setForeground(new java.awt.Color(255, 255, 255));
        L3.setText("Ataque:");
        add(L3);
        L3.setBounds(20, 200, 70, 17);

        descripcion.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        descripcion.setForeground(new java.awt.Color(255, 255, 255));
        descripcion.setText("Descripción");
        descripcion.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        add(descripcion);
        descripcion.setBounds(20, 240, 210, 150);

        vida.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        vida.setForeground(java.awt.Color.yellow);
        vida.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        vida.setText("100/100");
        add(vida);
        vida.setBounds(20, 160, 200, 17);

        defensa.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        defensa.setForeground(java.awt.Color.yellow);
        defensa.setText("100");
        add(defensa);
        defensa.setBounds(100, 180, 120, 17);

        ataque.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        ataque.setForeground(java.awt.Color.yellow);
        ataque.setText("100");
        add(ataque);
        ataque.setBounds(100, 200, 120, 17);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel L1;
    private javax.swing.JLabel L2;
    private javax.swing.JLabel L3;
    private javax.swing.JLabel ataque;
    private javax.swing.JLabel defensa;
    private javax.swing.JLabel descripcion;
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel vida;
    // End of variables declaration//GEN-END:variables
}
