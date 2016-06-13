/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.Accion;
import Modelos.Criatura;
import Modelos.Dado;
import Modelos.ElementoEnCampo;
import Modelos.JefeDeTerreno;
import Modelos.Jugador;
import Modelos.Posicion;
import Modelos.Tablero;
import Modelos.Terreno;
import Modelos.Trampa;
import Otros.BotonCheckImagen;
import Otros.BotonImagen;
import Vistas.SubVistaCambioTurno;
import Vistas.SubVistaCriaturaRevivir;
import Vistas.SubVistaCuadroDialogo;
import Vistas.SubVistaInfoElemento;
import Vistas.SubVistaInfoJugadorBatalla;
import Vistas.SubVistaLanzamientoDados;
import Vistas.SubVistaPosicion;
import Vistas.SubVistaTablero;
import Vistas.VistaBatalla;
import Vistas.SubVistaMenuPausa;
import Vistas.SubVistaSeleccionCriatura;
import Vistas.SubVistaSeleccionDados;
import Vistas.SubVistaSeleccionMagia;
import Vistas.SubVistaSeleccionTrampa;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public class ControladorBatalla {
    private final ControladorPrincipal contPrin;
    private final VistaBatalla visBat;
    private final SubVistaMenuPausa visPausBat;
    private SubVistaCuadroDialogo visMen;
    private final Tablero tablero;
    private final Accion accion;
    private final ArrayList<Terreno> terrenos;
    private final int[][] posJugTab = {{7, 0}, {7, 14}, {0, 7}, {14, 7}};
    private SubVistaInfoElemento visInfoEl;
    private Timer timerVisInfoEl;
    
    public ControladorBatalla(
            ControladorPrincipal contPrin,
            ArrayList<Jugador> jugadores){
        
        this.contPrin = contPrin;
        
        // Instancia y agrega la vista de batalla
        this.visBat = new VistaBatalla(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visBat);
        // Instancia y agrega el tablero a la vista batalla
        this.tablero = new Tablero();
        this.terrenos = new ArrayList();
        this.accion = new Accion();
        this.crearVistaTablero();
        // Agrega los listeners a los componentes de la vista batalla
        this.agregarListenersVistaBatalla();
        
        // Agregar la vista para seleccionar el despliegue de dado
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDesp());
        this.agregarListenersVistaSeleccionarDespliegue();
        
        this.tablero.setJugadores(jugadores);
        this.agregarJugadoresPartida(jugadores);
        
        this.visPausBat = new SubVistaMenuPausa(this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visPausBat);
        this.agregarListenersVistaPausaBatalla();
    }
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con vista batalla">   
    
    /**
     * Agrega las vistas de resumen de información de jugador para los jugadores
     * que conforman esta partida.
     * @param jugPartida Jugadores de la partida para los cuales se crearán las
     * vistas.
     */
    public void agregarJugadoresPartida(ArrayList<Jugador> jugPartida){
        for(int i = 0; i < jugPartida.size(); i++){
            this.terrenos.add(new Terreno());
            this.tablero.getJugador(i).reiniciar();
            this.tablero.getJugador(i).setTerreno(this.terrenos.get(i));
            this.visBat.agregarJugador(jugPartida.get(i));
            this.tablero.asignarCasilla(posJugTab[i], i + 1);
            this.tablero.getPosicion(posJugTab[i][0], posJugTab[i][1])
                    .setElemento(jugPartida.get(i).getJefeDeTerreno());
            this.asignarCasilla(posJugTab[i], i, jugPartida.get(i).getJefeDeTerreno());
            
            for(int j = 1; j <= 3; j++){
                this.tablero.getJugador(i).agregarTrampa(new Trampa(j, i + 1));
                this.tablero.getJugador(i).agregarTrampa(new Trampa(j, i + 1));
            }
            
            for(Dado dado: this.tablero.getJugador(i).getDados()){
                dado.setParaLanzar(true);
                dado.getCriatura().reiniciar(i + 1);
            }
            
            this.tablero.getJugador(i).getJefeDeTerreno().reiniciar(i + 1);
        }
        
        this.visBat.getTablero().actualizarCasillas();
    }
       
    /**
     * Agrega los listeners a los componentes de la vista de batalla.
     */
    public void agregarListenersVistaBatalla(){
        this.visBat.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent e){
                visBat.setMensaje("");
            }
        });
        
        this.visBat.getAtaque().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(sePuedeAtacar()){
                        solicitarCriaturaAtacante();
                    }else{
                        mostrarMensaje("No se puede atacar porque no se cumple con el mínimo de criaturas requeridas en el tablero o no existen criaturas en condiciones de atacar.");
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Realizar un ataque");
                }
            }
        });
        
        this.visBat.getInvocacion().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(tablero.getNumAccion() == 1){
                        mostrarVistaSeleccionarDespliegue();
                    }else{
                        mostrarMensaje("No estás invocando una criatura actualmente.");
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Cambiar despliegue invocación");
                }
            }
        });
        
        this.visBat.getMagia().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(sePuedeActivarMagia()){
                        mostrarVistaSeleccionMagia();
                    }else{
                        mostrarMensaje("No hay magias disponibles o no tienes puntos de magia suficientes.");
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Activar una magia");
                }
            }
        });
        
        this.visBat.getMovimiento().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(tablero.getNumAccion() == 0){
                        if(sePuedeMover()){
                            solicitarSeleccionarCriatura();
                        }else{
                            mostrarMensajeNoSePuedeMover();
                        }
                    }else{
                        moverCriatura();
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Mover una criatura");
                }
            }
        });
        
        this.visBat.getTrampa().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(sePuedeColocarTrampa()){
                        mostrarVistaSeleccionTrampa();
                    }else{
                        mostrarMensaje("No tienes puntos de trampa suficientes o no te quedan trampas.");
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visBat.setMensaje("Colocar una trampa");
                }
            }
        });
        
        this.visBat.getPausa().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    visPausBat.setVisible(true);
                }
            }
        });
        
        this.visBat.getTerminarTurno().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    cambiarTurno();
                }
            }
        });
    }
    
    public void mostrarVistaBatalla(){
        this.visBat.setVisible(true);
    }

    public VistaBatalla getVisBat() {
        return visBat;
    }
    
    public void actualizarVistaJugador(int i){
        SubVistaInfoJugadorBatalla visInfoJug = this.visBat.getVistaJugador(i);
        visInfoJug.setPuntosAtaque(String.valueOf(this.tablero.getJugador(i).getTurno().getPuntosAtaque()));
        visInfoJug.setPuntosMagia(String.valueOf(this.tablero.getJugador(i).getTurno().getPuntosMagia()));
        visInfoJug.setPuntosMovimiento(String.valueOf(this.tablero.getJugador(i).getTurno().getPuntosMovimiento()));
        visInfoJug.setPuntosTrampa(String.valueOf(this.tablero.getJugador(i).getTurno().getPuntosTrampa()));
        visInfoJug.setVidaJugador(this.tablero.getJugador(i).getJefeDeTerreno().getVida());
    }
    
    public void iniciarJuego(){
        this.tablero.getJugador(0).getJefeDeTerreno().restarVida(950);
        this.tablero.getJugador(1).getJefeDeTerreno().restarVida(1050);
        
        this.tablero.setTurnoActual(-1);
        this.cambiarTurno();
    }
    
    public Terreno getTerrenoJugActual(){
        return this.terrenos.get(tablero.getTurnoActual());
    }
    
    public void mostrarMensaje(String mensaje){
        this.visMen = new SubVistaCuadroDialogo("<html><center>" + mensaje + "</center></html>",
                "Aceptar", this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista tablero">    
    
    /**
     * Instancia una nueva "vista" de tablero y la agrega a la vista de batalla
     * instanciada en este controlador. Además, crea una nueva instancia del modelo
     * Tablero y la almacena en este controlador.
     */
    public void crearVistaTablero(){
        this.visBat.setTablero(new SubVistaTablero());
        
        this.agregarListenersCasillas();
        
        this.visBat.add(this.visBat.getTablero(), 0);
        this.visBat.getTablero().setLocation(150, 50);
    }
    
    /**
     * Agrega los listeners a una de las posiciones dentro de la "vista" tablero.
     * @param fila Fila de la posición dentro del tablero.
     * @param columna Columna de la posición dentro del tablero.
     */
    public void agregarListenersCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                this.visBat.getTablero().getCasilla(i, j).addMouseListener(new MouseAdapter(){
                    boolean fuiClicado = false;
                    
                    @Override
                    public void mouseEntered(MouseEvent e){
                        visBat.setMensaje("");
                        switch(tablero.getNumAccion()){
                            case 0: if(sePuedeMostrarVistaInfoElemento((SubVistaPosicion) e.getComponent())){
                                        mostrarVistaInfoElemento((SubVistaPosicion) e.getComponent());
                                        posicionarVistaInfoElemento(e.getComponent().getX() + 150);
                                    }
                                    break;
                            case 1: visBat.getTablero().setBotonActual((SubVistaPosicion) e.getComponent());
                                    mostrarDespliegue(
                                            tablero.getNumDespliegue(),
                                            visBat.getTablero().getBotonActual(),
                                            tablero.getDireccion(),
                                            tablero.getTurnoActual());
                                    break;
                            case 10:    comprobarCasillaSeleccionCriatura((SubVistaPosicion) e.getComponent());
                                        break;
                            case 11:    comprobarCasillaMovimiento((SubVistaPosicion) e.getComponent());
                                        break;
                            case 20:    comprobarCasillaCriaturaAtacante((SubVistaPosicion) e.getComponent());
                                        break;
                            case 21:    comprobarCasillaElementoAtacado((SubVistaPosicion) e.getComponent());
                                        break;
                            case 30:    comprobarCasillaTrampa((SubVistaPosicion) e.getComponent());
                                        break;
                            case 40:    mostrarAreaAfectada((SubVistaPosicion) e.getComponent());
                                        break;
                        }
                    }

                    @Override
                    public void mouseClicked(MouseEvent e){
                        fuiClicado = true;
                        
                        switch(tablero.getNumAccion()){
                            case 0: break;
                            case 1: visBat.getTablero().reiniciarCasillas();
                                    invocarCriatura((SubVistaPosicion) e.getComponent());
                                    break;
                            case 10:    setCriaturaAMover((SubVistaPosicion) e.getComponent());
                                        break;
                            case 11:    cambiarEstadoCasillaCamino((SubVistaPosicion) e.getComponent());
                                        break;
                            case 20:    setCriaturaAtacante((SubVistaPosicion) e.getComponent());
                                        break;
                            case 21:    atacarEnemigo((SubVistaPosicion) e.getComponent());
                                        break;
                            case 30:    colocarTrampa((SubVistaPosicion) e.getComponent());
                                        break;
                            case 40:    setAreaAfectada((SubVistaPosicion) e.getComponent());
                                        break;
                            case 41:    cambiarEstadoCriaturaAfectada((SubVistaPosicion) e.getComponent());
                                        break;
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e){
                        fuiClicado = false;
                        
                        ocultarVistaInfoElemento();
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e){                        
                        if(((BotonCheckImagen) e.getComponent()).isSelected()){
                            ((BotonCheckImagen) e.getComponent()).setImagenActual(3);
                        }else{
                            ((BotonCheckImagen) e.getComponent()).setImagenActual(0);
                        }
                    }
                });

                this.visBat.getTablero().getCasilla(i, j).addKeyListener(new KeyAdapter(){
                    @Override
                    public void keyPressed(KeyEvent e){
                        switch(tablero.getNumAccion()){
                            case 0: break;
                            case 1: switch(e.getKeyCode()){
                                        case KeyEvent.VK_W: tablero.setDireccion(0);
                                                            break;
                                        case KeyEvent.VK_A: tablero.setDireccion(1);
                                                            break;
                                        case KeyEvent.VK_S: tablero.setDireccion(2);
                                                            break;
                                        case KeyEvent.VK_D: tablero.setDireccion(3);
                                                            break;
                                    }

                                    visBat.getTablero().actualizarCasillas();
                                    mostrarDespliegue(
                                            tablero.getNumDespliegue(),
                                            visBat.getTablero().getBotonActual(),
                                            tablero.getDireccion(),
                                            tablero.getTurnoActual());
                                    break;
                        }
                    }
                });
            }
        }
    }
    
    /**
     * Pinta los botones que conforman el despliegue de dados en la posición indicada
     * por el botón actual sobre el que se encuentra el mouse.
     * @param numDespliegue Número del despliegue a mostrar.
     * @param botonActual Botón actual sobre el que se encuentra el mouse.
     * @param direccion Dirección del despliegue.
     * @param turno Turno actual.
     */
    public void mostrarDespliegue(int numDespliegue, SubVistaPosicion botonActual, int direccion, int turno){
        this.visBat.getTablero().actualizarCasillas();
        for(int[] coord: this.tablero.getDespliegue(botonActual.getFila(), botonActual.getColumna())){
            Posicion posAct = this.tablero.getPosicion(coord[0], coord[1]);
            if(posAct != null){
                if(posAct.getDueno() == 0){
                    this.visBat.getTablero().getCasilla(coord[0], coord[1]).setImagenSobre(
                            "/Imagenes/Botones/casilla_j" + (turno + 1) + ".png");
                }else{
                    this.visBat.getTablero().getCasilla(coord[0], coord[1]).setImagenSobre(
                            "/Imagenes/Botones/casilla_error.png");
                }
                this.visBat.getTablero().getCasilla(coord[0], coord[1]).setImagenActual(1);
            }else{
                this.visBat.setMensaje("Fuera de los límites del tablero.");
            }
        }
    }
    
    /**
     * Marca las casillas del despliegue como propiedad del jugador del turno actual.
     * @param idxCasillas Índices (Coordenadas) de las casillas que forman el despliegue.
     * @param turno Turno actual.
     */
    public void asignarCasillas(int[][] idxCasillas, int turno){
        for(int[] coord: idxCasillas){
            this.asignarCasilla(coord, turno, this.tablero.getPosicion(coord[0], coord[1]).getElemento());
        }
    }
    
    /**
     * Asigna una casilla a un jugador y guarda el elemento en campo señalado en dicha
     * posición.
     * @param coord Índice (coordenada) de la casilla.
     * @param turno Turno actual.
     * @param elemento Elemento que se guardará en la casilla.
     */
    public void asignarCasilla(int[] coord, int turno, ElementoEnCampo elemento){
        this.tablero.asignarCasilla(coord, (turno + 1));
        this.visBat.getTablero().marcarCasilla(coord, (turno + 1),                            
            "/Imagenes/" + ((elemento == null || elemento instanceof Trampa)? "vacio.png" :
                    ((elemento instanceof Criatura ? "Criaturas/" : "Jefes/") + 
                    elemento.getNomArchivoImagen() + ".png")));
    }
    
    public boolean sePuedeMostrarVistaInfoElemento(SubVistaPosicion posicion){
        Posicion posicionActual = this.tablero.getPosicion(posicion.getFila(), posicion.getColumna());
//        return posicionActual.getElemento() != null && !(posicionActual.getElemento() instanceof Trampa);
        return posicionActual.getElemento() != null;
    }
    
    public void mostrarVistaInfoElemento(SubVistaPosicion posicion){
        Posicion posicionActual = this.tablero.getPosicion(posicion.getFila(), posicion.getColumna());
        
        this.visInfoEl = new SubVistaInfoElemento(
                posicionActual.getElemento(),
                this.contPrin.getFuente());
        this.visBat.add(this.visInfoEl, 0);

        this.timerVisInfoEl = new Timer();
        this.timerVisInfoEl.schedule(new TimerTask(){
            @Override
            public void run(){
                visInfoEl.setVisible(true);
                timerVisInfoEl.cancel();
            }
        }, 1500, 10);
    }
    
    public void posicionarVistaInfoElemento(int x){
        if(this.visInfoEl != null){
            this.visInfoEl.setLocation(x > 400 ? 10: 540, 100);
        }
    }
    
    public void ocultarVistaInfoElemento(){
        if(this.visInfoEl != null){
            try{
                this.timerVisInfoEl.cancel();
            }catch(Exception e){
                // Nada
            }
            this.visInfoEl.setVisible(false);
            this.visBat.repaint();
        }
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de selección de dados">  
    
    public void crearVistaSeleccionDados(Jugador jugador){
        ArrayList<Dado> dadosParaLanzar = new ArrayList();
        
        for(Dado dado: jugador.getDados()){
            if(dado.isParaJugar() && dado.isParaLanzar()){
                dadosParaLanzar.add(dado);
            }
        }
        
        this.visBat.setVisSelDados(new SubVistaSeleccionDados(this.contPrin.getFuente(), dadosParaLanzar));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDados());
        this.visBat.getVisSelDados().setVisible(true);
        
        for(int i = 0; i < this.visBat.getVisSelDados().getPanelesDados().size(); i++){
            this.agregarListenersPanelDado(i);
        }
        
        this.agregarListenersVistaSeleccionDados();
    }
    
    public void agregarListenersPanelDado(int i){
        this.visBat.getVisSelDados().getPanelesDados().get(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                comprobarCantidadDadosSeleccionados((BotonCheckImagen) e.getComponent());
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                mostrarVistaInfoCriatura((BotonCheckImagen) e.getComponent());
                posicionarVistaInfoCriatura(e.getComponent().getX() + 101);
                visBat.getVisSelDados().setInfoDado((BotonCheckImagen) e.getComponent());
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                ocultarVistaInfoCriatura();
                visBat.getVisSelDados().setInfoDado(null);
            }
        });
    }
    
    public void agregarListenersVistaSeleccionDados(){
        this.visBat.getVisSelDados().getLanzarDados().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(visBat.getVisSelDados().cantidadSeleccionados() >= 1){
                    lanzarDados();
                }else{
                    mostrarMensaje("Selecciona al menos un dado para continuar.");
                }
            }
        });
    }
    
    public void comprobarCantidadDadosSeleccionados(BotonCheckImagen panelDado){
        if(panelDado.isSelected() && this.visBat.getVisSelDados().cantidadSeleccionados() > 4){
                SubVistaCuadroDialogo visMen = new SubVistaCuadroDialogo("Máximo 4 dados.", "Aceptar", this.contPrin.getFuente());
                this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
                visMen.setVisible(true);
                panelDado.setSelected(false);
        }
    }
    
    public void lanzarDados(){        
        this.tablero.getJugadorActual().getTurno().lanzarDados(this.visBat.getVisSelDados().getDadosSeleccionados());
        this.visBat.getVisSelDados().dispose();
        
        this.visBat.setVisLanDados(new SubVistaLanzamientoDados(
                this.tablero.getJugadorActual().getTurno().getResultadoLanzamiento(),
                this.tablero.getJugadorActual().getTurno().getDadosLanzados(), this.contPrin.getFuente()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisLanDados());
        this.visBat.getVisLanDados().setVisible(true);
        this.agregarListenersVistaLanzamientoDados();
        
    }
    
    public void mostrarVistaInfoCriatura(BotonCheckImagen panelDado){
        this.visBat.getVisSelDados().setVisInfo(new SubVistaInfoElemento(
                this.visBat.getVisSelDados().getDado(panelDado).getCriatura(),
                this.contPrin.getFuente()));
        this.visBat.getVisSelDados().add(this.visBat.getVisSelDados().getVisInfo(), 0);
        
        this.timerVisInfoEl = new Timer();
        this.timerVisInfoEl.schedule(new TimerTask(){
            @Override
            public void run(){
                visBat.getVisSelDados().getVisInfo().setVisible(true);
                timerVisInfoEl.cancel();
            }
        }, 1500, 10);
    }
    
    public void posicionarVistaInfoCriatura(int x){
        this.visBat.getVisSelDados().getVisInfo().setLocation(x > 400 ? 10: 540, 100);
        this.visBat.getVisSelDados().repaint();
    }
    
    public void ocultarVistaInfoCriatura(){
        try{
            this.timerVisInfoEl.cancel();
        }catch(Exception e){
            // Nada
        }
        
        try{
            this.visBat.getVisSelDados().getVisInfo().setVisible(false);
            this.visBat.getVisSelDados().repaint();
        }catch(Exception e){
            // Nada
        }
    }
// </editor-fold>
  
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de lanzamiento de dados">  
    
    public void agregarListenersVistaLanzamientoDados(){
        this.visBat.getVisLanDados().getAculumarPuntos().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(cantidadCarasInvocacion() != 0){
                    mostrarMensajeCarasInvocacion();
                }else{
                    acumularPuntos();
                    cambiarTurno();
                }
            }
        });
        
        this.visBat.getVisLanDados().getRealizarAcciones().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                acumularPuntos();
                if(cantidadCarasInvocacion() != 0 &&
                    !criaturasQueSePuedenInvocar(tablero.getJugadorActual().getTurno().getDadosLanzados()).isEmpty()){
                    mostrarVistaSeleccionCriatura();
                }else{
                    realizarAcciones();
                }
            }
        });
    }
    
    public int cantidadCarasInvocacion(){
        int cantidadInvocacion = 0;
        for(int cara: this.tablero.getJugadorActual().getTurno().getResultadoLanzamiento()){
            if(cara == 2){
                cantidadInvocacion++;
            }
        }
        
        return cantidadInvocacion;
    }
    
    public void acumularPuntos(){
        this.tablero.getJugadorActual().getTurno().acumularPuntos();
        this.visBat.getVisLanDados().dispose();
        this.actualizarVistaJugador(this.tablero.getTurnoActual());
    }
    
    public void mostrarMensajeCarasInvocacion(){
        this.visMen = new SubVistaCuadroDialogo("<html><center>Atención: Las caras de invocación no se acumulan. ¿Continuar de todos modos?</center></html>",
                "Si", "No", this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("puntos_invocacion");
        this.agregarListenersVistaMensaje();
    }
    
    public void realizarAcciones(){
        this.visBat.getVisLanDados().dispose();
        this.habilitarBotones();
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de selección de despliegue"> 
    
    /**
     * Agrega los listeners a la "vista" de selección de despliegue de dados.
     */
    public void agregarListenersVistaSeleccionarDespliegue(){        
        for(BotonImagen botonDespliegue: this.visBat.getVisSelDesp().getBotonesDespliegue()){
            botonDespliegue.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    cambiarDespliegue(Integer.parseInt(e.getComponent().getName()));
                }
            });
        }
    }
    
    public void mostrarVistaSeleccionarDespliegue(){
        this.visBat.getVisSelDesp().setVisible(true);
    }
    
    public void ocultarVistaSeleccionarDespliegue(){
        this.visBat.getVisSelDesp().setVisible(false);
    }
    
    /**
     * Cambia el valor del despliegue a mostrar en el tablero.
     * @param numDespliegue Número de despliegue a mostrar.
     */
    public void cambiarDespliegue(int numDespliegue){
        ocultarVistaSeleccionarDespliegue();
        this.tablero.setNumAccion(1);
        this.tablero.setNumDespliegue(numDespliegue);
        this.visBat.getTablero().getCasilla(0, 0).requestFocus();
        this.visBat.getInvocacion().setEnabled(true);
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista pausa batalla">
    
    public void agregarListenersVistaPausaBatalla(){
        this.visPausBat.getContinuarPartida().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                visPausBat.setVisible(false);
            }
        });
        
        this.visPausBat.getVolverMenuPrincipal().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                mostrarMensajeVolverMenuPrincipal();
            }
        });
        
        this.visPausBat.getSalirAplicacion().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                contPrin.getContVisPrin().mostrarMensajeSalir();
            }
        });
    }

    public SubVistaMenuPausa getVisPausBat() {
        return visPausBat;
    }
    
    public void mostrarMensajeVolverMenuPrincipal(){
        this.visMen = new SubVistaCuadroDialogo(
                "<html><center>¿Deseas volver al menú principal? Se perderá el progreso de la partida.</center></html>",
                "Si", "No", this.contPrin.getFuente());
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("volver_menu");
        this.agregarListenersVistaMensaje();
    }
    
    public void volverMenuPrincipal(){
        this.contPrin.getContBat().getVisBat().dispose();
        this.contPrin.getContBat().getVisPausBat().dispose();
        this.contPrin.crearControladorMenuPrincipal();
        this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
        this.visMen.dispose();
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de cuadro de diálogo">  
    public void agregarListenersVistaMensaje() {
        this.visMen.getBoton1().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                switch(visMen.getName()){
                    case "volver_menu": volverMenuPrincipal();
                                        break;
                    case "puntos_invocacion":   acumularPuntos();
                                                break;
                }
                visMen.dispose();
            }
        });
        
        this.visMen.getBoton2().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                visMen.dispose();
            }
        });
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el ataque de criatura">  
    
    public boolean sePuedeAtacar(){
        if(this.tablero.getJugadorActual().getTurno().getPuntosAtaque() != 0){
            int cantidadJugadoresTienenCriaturas = 0;
            for(Terreno terreno: terrenos){
                if(terreno.cantidadCriaturasInvocadas() != 0){
                    cantidadJugadoresTienenCriaturas++;
                }
            }
            
            boolean criaturaQuePuedaAtacar = false;
            for(int i = 0; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    if(!criaturaQuePuedaAtacar){
                        Posicion posAct = this.tablero.getPosicion(i, j);
                        if(posAct.getElemento() instanceof Criatura &&
                           posAct.getElemento().getDueno() == (this.tablero.getTurnoActual() + 1)){
                            for(int[] coord: this.tablero.getIdxVecinos(this.tablero.getPosicion(i, j))){
                                Posicion posVecino = this.tablero.getPosicion(coord[0], coord[1]);
                                if(posVecino.getElemento() != null &&
                                   !(posVecino.getElemento() instanceof Trampa) &&
                                   posVecino.getElemento().getDueno() != (this.tablero.getTurnoActual() + 1)){
                                    criaturaQuePuedaAtacar = true;
                                    break;
                                }
                            }
                        }
                    }else{
                        break;
                    }
                }
            }

            return cantidadJugadoresTienenCriaturas > 1 && criaturaQuePuedaAtacar;
        }else{
            return false;
        }
    }
    
    public void solicitarCriaturaAtacante(){
        this.tablero.setNumAccion(20);
        this.deshabilitarBotones();
        this.visBat.setMensaje("Selecciona la criatura que atacará.");
    }
    
    public void setCriaturaAtacante(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura){
            if(posAct.getElemento().getDueno() == (this.tablero.getTurnoActual() + 1)){
                this.accion.setCriaturaAtacante((Criatura) posAct.getElemento());
                this.tablero.setNumAccion(21);
                this.visBat.setMensaje("Selecciona el enemigo a atacar.");
            }else{
                this.visBat.setMensaje("Esta criatura no te pertenece.");
            }
        }else{
            this.visBat.setMensaje("Aquí no hay una criatura.");
        }
    }
    
    public void atacarEnemigo(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getDueno() != (this.tablero.getTurnoActual() + 1)){
            if(posAct.getElemento() != null && !(posAct.getElemento() instanceof Trampa)){
                int vida = this.accion.atacarEnemigo(posAct.getElemento());
                if(vida <= 0){
                    if(posAct.getElemento() instanceof JefeDeTerreno){
                        this.tablero.agregarPerdedor(posAct.getDueno() - 1);
                        this.eliminarJugadorPartida(posAct.getDueno());
                        if(this.tablero.cantidadJugadores() == 1){
                            finalizarPartida();
                        }
                    }
                    posAct.setElemento(null);
                }
                
                this.tablero.getJugadorActual().getTurno().descontarPuntoAtaque();
                this.revisarCasillas();
                this.finalizarAccion();
            }else{
                this.visBat.setMensaje("Aquí no hay un enemigo que se pueda atacar.");
            }
        }else{
            this.visBat.setMensaje("Selecciona un enemigo.");
        }
    }
    
    public void comprobarCasillaCriaturaAtacante(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getDueno() == 0 || !(posAct.getElemento() instanceof Criatura)){
            casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
        }else{
            casilla.setImagenSobre("/Imagenes/Botones/casilla_correcta.png");
        }
        casilla.setImagenActual(1);
    }
    
    public void comprobarCasillaElementoAtacado(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getDueno() == (this.tablero.getTurnoActual() + 1) || 
           posAct.getDueno() == 0 || posAct.getElemento() instanceof Trampa ||
           posAct.getElemento() == null){
            casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
        }else{
            casilla.setImagenSobre("/Imagenes/Botones/casilla_correcta.png");
        }
        casilla.setImagenActual(1);
    }
        
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la invocación de criaturas">  
    
    public void agregarListenersVistaSeleccionCriatura(int i){
        this.visBat.getVisSelCri().getPanelCriatura(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                seleccionarCriaturaAInvocar((BotonImagen) e.getComponent());
            }
        });
    }
        
    public void mostrarVistaSeleccionCriatura(){
        this.visBat.getVisLanDados().dispose();
        
        this.visBat.setVisSelCri(new SubVistaSeleccionCriatura(this.criaturasQueSePuedenInvocar(
                this.tablero.getJugadorActual().getTurno().getDadosLanzados()), this.contPrin.getFuente()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelCri());
        this.visBat.getVisSelCri().setVisible(true);
        
        for(int i = 0; i < this.visBat.getVisSelCri().getCantidadCriaturas(); i++){
            this.agregarListenersVistaSeleccionCriatura(i);
        }
    }
    
    public ArrayList<Criatura> criaturasQueSePuedenInvocar(ArrayList<Dado> dados){
        ArrayList<Criatura> criaturas = new ArrayList();
        for(Dado dado: dados){
            int nivel = dado.getCriatura().getNivel();
            if(nivel == 1){
                criaturas.add(dado.getCriatura());
            }else if(this.cantidadCarasInvocacion() >= 2){
                if(nivel == 2){
                    criaturas.add(dado.getCriatura());
                }else{
                    boolean terminar = false;
                    for(int i = 0; i < 15; i++){
                        if(!terminar){
                            for(int j = 0; j < 15; j++){
                                if(this.tablero.getPosicion(i, j).getElemento() instanceof Criatura &&
                                   ((Criatura) this.tablero.getPosicion(i, j).getElemento()).getNivel() == (nivel - 1)){
                                    criaturas.add(dado.getCriatura());
                                    terminar = true;
                                    break;
                                }
                            }
                        }else{
                            break;
                        }
                    }
                }
            }
        }
        return criaturas;
    }
    
    public void seleccionarCriaturaAInvocar(BotonImagen panelCriatura){
        this.visBat.getVisSelCri().dispose();
        accion.setCriaturaAInvocar(this.visBat.getVisSelCri().getCriatura(panelCriatura));
        this.mostrarVistaSeleccionarDespliegue();
    }
    
    public boolean sePuedeInvocar(int[][] idxCasillas, int turno){
        return this.tablero.estaDisponible(idxCasillas) &&
               this.tablero.estaConectadoAlTerreno(idxCasillas, (turno + 1));
    }
    
    public void invocarCriatura(SubVistaPosicion posicion){
        int[][] despliegue = tablero.getDespliegue(posicion.getFila(), posicion.getColumna());
        
        if(this.sePuedeInvocar(despliegue, tablero.getTurnoActual())){
            this.accion.invocarCriatura(this.tablero.getPosicion(posicion.getFila(), posicion.getColumna()),
                    this.tablero.getJugadorActual().getDados());
            asignarCasillas(despliegue, tablero.getTurnoActual());
            this.finalizarAccion();
            this.visBat.getInvocacion().setEnabled(false);
        }else{
            this.visBat.setMensaje("No se puede invocar en la posición actual.");
        }
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la activación de magias">  
    
    public boolean sePuedeActivarMagia(){
        return this.accion.cantidadMagiasActivadas() < 3 &&
               this.tablero.getJugadorActual().getTurno().getPuntosMagia() >= 10;
    }
    
    public void mostrarVistaSeleccionMagia(){
        ArrayList<int[]> magiasDisponibles = accion.getMagiasDisponibles();
        
        for(int i = 0; i < magiasDisponibles.size(); i++){
            if(magiasDisponibles.get(i)[0] == 2){
                int cantidadCriaturasInvocadas = 0;
                for(int j = 0; j < this.tablero.cantidadJugadores(); j++){
                    if(j != this.tablero.getTurnoActual()){
                        cantidadCriaturasInvocadas += this.terrenos.get(j).cantidadCriaturasInvocadas();
                    }
                }
                
                if(cantidadCriaturasInvocadas == 0){
                    magiasDisponibles.remove(i);
                }
                break;
            }
        }
        
        this.visBat.setVisSelMag(new SubVistaSeleccionMagia(
                this.contPrin.getFuente(), magiasDisponibles,
                this.tablero.getJugadorActual().getTurno().getPuntosMagia()));
        
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelMag());
        this.visBat.getVisSelMag().setVisible(true);
        
        this.agregarListenersVistaSeleccionMagia();
    }
    
    public void agregarListenersVistaSeleccionMagia(){
        for(int i = 0; i < this.visBat.getVisSelMag().cantidadMagias(); i++){
            this.visBat.getVisSelMag().getPanelMagia(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    agregarMagiaActivada(visBat.getVisSelMag().getMagia((BotonImagen) e.getComponent()));
                }
            });
        }
    }
    
    public void agregarMagiaActivada(int[] magia){
        this.visBat.getVisSelMag().dispose();
        
        switch (magia[0]) {
            case 3:
                this.visBat.setMensaje("Marca el área afectada.");
                this.tablero.setNumAccion(40);
                this.deshabilitarBotones();
                break;
            case 2:
                this.visBat.setMensaje("Selecciona 3 criaturas enemigas.");
                this.tablero.setNumAccion(41);
                this.deshabilitarBotones();
                break;
            default:
                this.finalizarAccion();
                this.accion.activarMagia(magia[0], this.tablero.getTurnoActual() + 1);
                this.tablero.getJugadorActual().getTurno().descontarPuntosMagia(magia[3]);
                this.mostrarMensaje("Se ha activado la magia.");
                break;
        }
    }
    
    public void cambiarEstadoCriaturaAfectada(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura){
            if(posAct.getElemento().getDueno() != (this.tablero.getTurnoActual() + 1)){
                if(casilla.isSelected()){
                    accion.quitarCriaturaAfectada((Criatura) posAct.getElemento());
                }else{
                    accion.agregarCriaturaAfectada((Criatura) posAct.getElemento());
                    if(accion.cantidadCriaturasAfectadas() == 3){
                        this.finalizarAccion();
                        this.accion.activarMagia(2, this.tablero.getTurnoActual() + 1);
                        this.tablero.getJugadorActual().getTurno().descontarPuntosMagia(15);
                        this.mostrarMensaje("Se ha activado la magia.");
                    }
                }
            }else{
                this.visBat.setMensaje("Elige una criatura enemiga.");
            }
        }else{
            this.visBat.setMensaje("Aquí no hay una criatura.");
        }
    }
    
    public void casillaTieneCriaturaEnemiga(SubVistaPosicion casilla){
        this.visBat.getTablero().actualizarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura &&
           posAct.getElemento().getDueno() != (this.tablero.getTurnoActual() + 1)){
            casilla.setImagenSobre("/Imagenes/Botones/casilla_correcta.png");
        }else{
            casilla.setImagenSobre("/Imagenes/Botones/casilla_erronea.png");
        }
        casilla.setImagenActual(1);
    }
    
    public void mostrarAreaAfectada(SubVistaPosicion casilla){
        this.visBat.getTablero().actualizarCasillas();
        
        int filaEsqSupIzq = casilla.getFila() - 5;
        int columnaEsqSupIzQ = casilla.getColumna() - 5;
        
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                try{
                    this.visBat.getTablero().getCasilla(filaEsqSupIzq + i, columnaEsqSupIzQ + j).setImagenSobre("/Imagenes/Botones/casilla_seleccionada.png");
                    this.visBat.getTablero().getCasilla(filaEsqSupIzq + i, columnaEsqSupIzQ + j).setImagenActual(1);
                }catch(Exception e){
                    // Nada
                }
            }
        }
    }
    
    public void setAreaAfectada(SubVistaPosicion casilla){
        int filaEsqSupIzq = casilla.getFila() - 5;
        int columnaEsqSupIzQ = casilla.getColumna() - 5;
        ArrayList<Posicion> areaAfectada = new ArrayList();
        
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                Posicion posAct = this.tablero.getPosicion(filaEsqSupIzq + i, columnaEsqSupIzQ + j);
                if(posAct != null){
                    areaAfectada.add(posAct);
                }
            }
        }
        
        accion.setAreaDeEfecto(areaAfectada);
        accion.activarMagia(3, this.tablero.getTurnoActual() + 1);
        this.tablero.getJugadorActual().getTurno().descontarPuntosMagia(30);
        this.finalizarAccion();
        this.mostrarMensaje("Se ha activado la magia en el área marcada.");
    }
    
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el movimiento de criaturas"> 
    
    public boolean sePuedeMover(){
        return this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() >= this.tablero.getJugadorActual().getDado(0).getCriatura().getCostoMovimiento() &&
                getTerrenoJugActual().cantidadCriaturasInvocadas() != 0 &&
                getTerrenoJugActual().criaturaQuePuedaMoverse();
    }
    
    public void solicitarSeleccionarCriatura(){
        this.visBat.setMensaje("Selecciona una criatura.");
        this.tablero.setNumAccion(10);
        this.deshabilitarBotones();
    }
    
    public boolean criaturaNoEstaInmovilizada(Posicion posicion){
        return ((Criatura) posicion.getElemento()).getTurnosInmovilizada() == 0;
    }
    
    public void setCriaturaAMover(SubVistaPosicion posicion){
        Posicion posAct = this.tablero.getPosicion(posicion.getFila(), posicion.getColumna());
        if(posAct.getElemento() instanceof Criatura){
            if(((Criatura) posAct.getElemento()).getDueno() == (this.tablero.getTurnoActual() + 1)){
                if(this.criaturaNoEstaInmovilizada(posAct)){
                    this.accion.setCriaturaAMover((Criatura) posAct.getElemento());
                    this.cambiarEstadoCasillaCamino(posicion);
                    this.visBat.setMensaje("Marca las casillas del camino.");
                    this.tablero.setNumAccion(11);
                }else{
                    this.visBat.setMensaje("La criatura está inmovilizada. Quedan " +
                            ((Criatura) posAct.getElemento()).getTurnosInmovilizada() + " turnos.");
                    this.visBat.getTablero().reiniciarCasillas();
                }
            }else{
                this.visBat.setMensaje("Esta criatura no te pertenece.");
                this.visBat.getTablero().reiniciarCasillas();
            }
        }else{
            this.visBat.setMensaje("Aquí no hay una criatura.");
            this.visBat.getTablero().reiniciarCasillas();
        }
    }
    
    public void cambiarEstadoCasillaCamino(SubVistaPosicion casilla){
        Posicion posicionActual = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(this.accion.caminoContienePosicion(posicionActual)){
            if(this.sePuedeEliminarCasilla(posicionActual)){
                this.accion.eliminarPosicionDelCamino(posicionActual);
            }else{                
                casilla.setSelected(true);
            }
        }else{
            if(this.accion.largoDelCamino() == 0 ||
               this.sePuedeAgregarCasilla(posicionActual)){
                this.accion.agregarPosicionAlCamino(posicionActual);
            }else{                
                casilla.setSelected(false);
            } 
        }
        
        if(this.accion.largoDelCamino() > 1){
            this.visBat.getMovimiento().setEnabled(true);
        }else{
            this.visBat.getMovimiento().setEnabled(false);
        }
        
        this.visBat.getTablero().actualizarCasillas();
    }
    
    public boolean sePuedeAgregarCasilla(Posicion posicion){
        if((this.accion.largoDelCamino() - 1) < this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() &&
          (posicion.getElemento() == null || posicion.getElemento() instanceof Trampa)){
            for(int[] coord: this.tablero.getIdxVecinos(posicion)){
                try{                    
                    if(this.accion.getUltimaPosicionAgregada().equals(this.tablero.getPosicion(coord[0], coord[1]))){
                        return true;
                    }
                }catch(Exception e){
                    // Nada
                }
            }
        }else{
            return false;
        }
        
        return false;
    }
    
    public boolean sePuedeEliminarCasilla(Posicion posicion){
        return this.accion.getUltimaPosicionAgregada().equals(posicion);
    }
    
    public void moverCriatura(){
        this.tablero.setNumAccion(0);
        this.deshabilitarBotones();
        this.visBat.getTablero().actualizarCasillas();
        
        Timer timerMovimiento = new Timer();
        timerMovimiento.schedule(new TimerTask(){
            int pasos = accion.largoDelCamino();
            int tic = 1;
            
            @Override
            public void run(){
                if(tic != pasos){
                    Posicion posAnt = accion.getPosicionCamino(tic - 1);
                    Posicion posAct = accion.getPosicionCamino(tic);

                    ElementoEnCampo elemento = accion.siguientePosicion(tic);
                    tablero.getJugadorActual().getTurno().descontarPuntosMovimiento(accion.getCriaturaAMover().getCostoMovimiento());

                    visBat.getTablero().getCasilla(posAnt.getFila(), posAnt.getColumna()).setImagenIconoElemento(
                            "/Imagenes/vacio.png");
                    visBat.getTablero().getCasilla(posAct.getFila(), posAct.getColumna()).setImagenIconoElemento(
                            "/Imagenes/Criaturas/" + accion.getCriaturaAMover().getNomArchivoImagen() + ".png");
                    
                    visBat.getTablero().getCasilla(posAnt.getFila(), posAnt.getColumna()).deseleccionado();

                    if(elemento instanceof Trampa &&
                       elemento.getDueno() != (tablero.getTurnoActual() + 1)){
                        
                        activarTrampa((Trampa) elemento);
                        visBat.setMensaje("Se ha activado una trampa.");
                        this.cancel();
                        timerMovimiento.cancel();
                    }

                    tic++;
                }else{
                    finalizarMovimiento();        
                    this.cancel();
                    timerMovimiento.cancel();
                }
            }
        }, 1000, 500);        
        
    }
    
    public void comprobarCasillaSeleccionCriatura(SubVistaPosicion casilla){
        this.visBat.getTablero().actualizarCasillas();
        if(this.tablero.getPosicion(casilla.getFila(), casilla.getColumna()).getDueno() == (this.tablero.getTurnoActual() + 1) &&
           this.tablero.getPosicion(casilla.getFila(), casilla.getColumna()).getElemento() instanceof Criatura){
            casilla.setImagenSobre("/Imagenes/Botones/casilla_correcta.png");
        }else{
            casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
        }
        casilla.setImagenActual(1);
    }
    
    public void comprobarCasillaMovimiento(SubVistaPosicion casilla){
        Posicion posicionActual = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        this.visBat.getTablero().actualizarCasillas();
        if(!this.accion.caminoContienePosicion(posicionActual) && posicionActual.getDueno() != 0 && 
          (posicionActual.getElemento() == null || posicionActual.getElemento() instanceof Trampa)){
            casilla.setImagenSobre("/Imagenes/Botones/casilla_correcta.png");
        }else{
            casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
        }
        casilla.setImagenActual(1);
    }
    
    public void mostrarMensajeNoSePuedeMover(){
        this.mostrarMensaje("No tienes criaturas para mover o no tienes puntos de movimiento.");
    }
            
    public void finalizarMovimiento(){
        accion.finalizarMovimiento();
        this.finalizarAccion();
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con colocar trampas">  
    
    public boolean sePuedeColocarTrampa(){
        return this.tablero.getJugadorActual().getTurno().getPuntosTrampa() >= 10 &&
               this.tablero.getJugadorActual().cantidadTrampas() > 0;
    }
    
    public  void mostrarVistaSeleccionTrampa(){
        this.visBat.setVisSelTram(new SubVistaSeleccionTrampa(
                this.contPrin.getFuente(),
                this.tablero.getJugadorActual().getTrampas(),
                this.tablero.getJugadorActual().getTurno().getPuntosTrampa()));
        
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelTram());
        this.visBat.getVisSelTram().setVisible(true);
        
        this.agregarListenersVistaSeleccionTrampa();
    }
    
    public void agregarListenersVistaSeleccionTrampa(){
        for(int i = 0; i < this.visBat.getVisSelTram().cantidadTrampas(); i++){
            this.visBat.getVisSelTram().getPanelTrampa(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    setTrampaAColocar(visBat.getVisSelTram().getTrampa((BotonImagen) e.getComponent()));
                }
            });
        }
    }
    
    public void setTrampaAColocar(Trampa trampa){
        this.visBat.getVisSelTram().dispose();
        this.accion.setTrampaAColocar(trampa);
        this.tablero.setNumAccion(30);
        this.deshabilitarBotones();
        this.visBat.setMensaje("Selecciona una posición para colocar la trampa.");
    }
    
    public void colocarTrampa(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getDueno() != 0){
            if(posAct.getElemento() == null){
                accion.colocarTrampa(posAct, this.tablero.getJugadorActual());
                this.finalizarAccion();
                this.mostrarMensaje("Se ha colocado la trampa en la posición indicada.");
            }else{
                this.visBat.setMensaje("La casilla está ocupada. Selecciona una casilla disponible.");
            }
        }else{
            this.visBat.setMensaje("Selecciona una casilla que pertenezca a algún jugador.");
        }
    }
    
    public void activarTrampa(Trampa trampa){
        switch(trampa.getNumTrampa()){
            case 1: trampa.trampaDeOsos(accion);
                    finalizarMovimiento();
                    break;
            case 2: trampa.trampaParaLadrones(accion);
                    this.visBat.getTablero().reiniciarCasillas();
                    moverCriatura();
                    break;
            case 3: if(!this.tablero.getJugador(trampa.getDueno() - 1).getCriaturasMuertas().isEmpty()){
                        trampa.setPosicionReemplazo(accion.getPosicionCamino(accion.getPasoActualMovimiento()));
                        finalizarMovimiento();
                        trampa.setCriaturaAReemplazar(accion.getCriaturaAMover());
                        this.tablero.setTrampaActivada(trampa);
                        mostrarVistaCriaturaRevivir(trampa.getDueno() - 1);
                        agregarListenersVistaCriaturaRevivir();
                    }else{
                        finalizarMovimiento();
                        this.mostrarMensaje("La trampa \"Renacer de los muertos\" no tuvo efecto porque " + 
                                this.tablero.getJugador(trampa.getDueno() - 1).getNombreJugador() + 
                                " no tiene criaturas muertas.");
                    }
                    break;
        }
    }
    
    public void mostrarVistaCriaturaRevivir(int numJug){
        this.visBat.setVisCriRev(new SubVistaCriaturaRevivir(this.tablero.getJugador(numJug).getCriaturasMuertas(),
                this.contPrin.getFuente()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisCriRev());
        this.visBat.getVisCriRev().setVisible(true);
    }
    
    public void agregarListenersVistaCriaturaRevivir(){
        for(int i = 0; i < this.visBat.getVisCriRev().getCantidadCriaturas(); i++){
            this.visBat.getVisCriRev().getPanelCriatura(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    revivirCriatura(visBat.getVisCriRev().getCriatura((BotonImagen) e.getComponent()));
                }
            });
        }
    }
    
    public void revivirCriatura(Criatura criaturaReemplazo){
        this.visBat.getVisCriRev().dispose();
        
        Trampa trampa = this.tablero.getTrampaActivada();
        trampa.renacerDeLosMuertos(criaturaReemplazo,
                this.tablero.getJugador(trampa.getCriaturaAReemplazar().getDueno() - 1));
        
        Posicion posicion = trampa.getPosicionReemplazo();
        this.visBat.getTablero().getCasilla(posicion.getFila(), posicion.getColumna()).setImagenIconoElemento(
                "/Imagenes/Criaturas/" + posicion.getElemento().getNomArchivoImagen() + ".png");
    }
    
    public void comprobarCasillaTrampa(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getDueno() == 0 || posAct.getElemento() != null){
            casilla.setImagenSobre("/Imagenes/Botones/casilla_error.png");
        }else{
            casilla.setImagenSobre("/Imagenes/Botones/casilla_correcta.png");
        }
        casilla.setImagenActual(1);
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el turno">  
    
    public void habilitarBotones(){
        this.visBat.getAtaque().setEnabled(true);
        this.visBat.getMagia().setEnabled(true);
        this.visBat.getMovimiento().setEnabled(true);
        this.visBat.getTrampa().setEnabled(true);
        this.visBat.getTerminarTurno().setEnabled(true);
    }
    
    public void deshabilitarBotones(){
        this.visBat.getAtaque().setEnabled(false);
        this.visBat.getInvocacion().setEnabled(false);
        this.visBat.getMagia().setEnabled(false);
        this.visBat.getMovimiento().setEnabled(false);
        this.visBat.getTrampa().setEnabled(false);
        this.visBat.getTerminarTurno().setEnabled(false);
    }
    
    public void revisarCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(this.tablero.getPosicion(i, j).getElemento() == null){
                    this.visBat.getTablero().getCasilla(i, j).setImagenIconoElemento("/Imagenes/vacio.png");
                }
            }
        }
    }
    
    public void cambiarTurno(){
        if(this.tablero.getTurnoActual() != -1){
            for(Dado dado: this.tablero.getJugadorActual().getDados()){
                dado.getCriatura().disminuirTurnosInmovilizada();
                dado.getCriatura().disminuirTurnosCostoMovInc();
            }

            this.tablero.aplicarMagias(accion);
            this.revisarCasillas(); 
        }
        
        this.deshabilitarBotones();
        this.visBat.getTablero().reiniciarCasillas();
        this.tablero.cambiarTurno();
        
        Timer timerAnimacion = new Timer();
        timerAnimacion.schedule(new TimerTask(){
            SubVistaCambioTurno visCamTur = new SubVistaCambioTurno(tablero.getTurnoActual());
            int tic = 0;
            
            @Override
            public void run(){
                if(tic == 0){
                    contPrin.getContVisPrin().getVisPrin().agregarVista(visCamTur);
                    visCamTur.setVisible(true);
                }else{
                    visCamTur.dispose();
                    crearVistaSeleccionDados(tablero.getJugadorActual());
                    this.cancel();
                    timerAnimacion.cancel();
                }
                tic++;
            }
        }, 0, 3500);
    }
    
    public void finalizarAccion(){
        actualizarVistaJugador(tablero.getTurnoActual());
        visBat.getTablero().reiniciarCasillas();
        this.tablero.setNumAccion(0);
        this.habilitarBotones();
    }
    
    public void eliminarJugadorPartida(int numJug){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.tablero.getPosicion(i, j);
                if(posAct.getElemento() instanceof Criatura && posAct.getElemento().getDueno() == numJug){
                    posAct.setElemento(null);
                }else if(posAct.getElemento() instanceof Trampa &&
                   ((Trampa) posAct.getElemento()).getDueno() == numJug &&
                   ((Trampa) posAct.getElemento()).getNumTrampa() == 3){
                    posAct.setElemento(null);
                }
            }
        }
        
        this.revisarCasillas();
    }
    
    public void finalizarPartida(){
        this.mostrarMensaje("¡El ganador es " + this.tablero.getJugadorActual().getNombreJugador()+ "!");
        
        this.contPrin.crearControladorMenuPrincipal();
        this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
        
        this.visBat.dispose();
    }
    
// </editor-fold>
}