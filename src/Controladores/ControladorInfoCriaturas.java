/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Otros.BotonImagen;
import Otros.Constantes;
import Otros.Reproductor;
import Vistas.SubVistaInfoElemento;
import Vistas.VistaInfoCriaturas;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Esterlina
 */
public final class ControladorInfoCriaturas {
    private final ControladorPrincipal contPrin;
    private final VistaInfoCriaturas visCriatura;
    private  SubVistaInfoElemento visInfo;
    
    public ControladorInfoCriaturas(ControladorPrincipal contPrin){
        this.contPrin = contPrin;
        
        this.visCriatura = new VistaInfoCriaturas();
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visCriatura);
        this.agregarListenersVistaInfoCriaturas();
        
        this.agregarListenersIconosCriaturas();
        
        Reproductor.reproducir(Constantes.M_OTROS_FORMS);
    }
    public void mostrarVistaInfoCriaturas(){
        this.visCriatura.setVisible(true);
    }
    
    public void eliminarVistaInfoCriaturas(){
        this.visCriatura.dispose();
        contPrin.crearControladorMenuPrincipal();
        contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
    }

    private void agregarListenersVistaInfoCriaturas() {
       this.visCriatura.getVolver().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                eliminarVistaInfoCriaturas();
            }
        });
    }

    private void agregarListenersIconosCriaturas() {
        for(int i = 0; i < this.visCriatura.cantidadCriaturas(); i++){
            this.visCriatura.getIconoCriatura(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
                    mostrarSubVistaInfoCriatura((BotonImagen) e.getComponent());
                }
            });
        }
        
    }
    
    public void mostrarSubVistaInfoCriatura(BotonImagen iconoCriatura){
        
        visInfo = new SubVistaInfoElemento(this.visCriatura.getCriatura(iconoCriatura), null);
        this.visCriatura.add(visInfo, 0);
        
        visInfo.setVisible(true);
        visInfo.setLocation(50, 100);
    }
}
