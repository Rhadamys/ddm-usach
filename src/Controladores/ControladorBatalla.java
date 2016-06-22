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
import Modelos.PersonajeNoJugable;
import Modelos.Posicion;
import Modelos.Tablero;
import Modelos.Trampa;
import Modelos.Turno;
import Modelos.Usuario;
import ModelosDAO.PuzzleDeDadosDAO;
import Otros.BotonCheckImagen;
import Otros.BotonImagen;
import Otros.Constantes;
import Otros.InteligenciaArtificial;
import Otros.Reproductor;
import Vistas.SubVistaCambioTurno;
import Vistas.SubVistaCriaturaRevivir;
import Vistas.SubVistaCuadroDialogo;
import Vistas.SubVistaFinDelJuego;
import Vistas.SubVistaInfoElemento;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mam28
 */
public final class ControladorBatalla {
    private final ControladorPrincipal contPrin;
    private final VistaBatalla visBat;
    private final SubVistaMenuPausa visPausBat;
    private SubVistaCuadroDialogo visMen;
    private final Tablero tablero;
    private final Accion accion;
    private SubVistaInfoElemento visInfoEl;
    private Timer timerVisInfoEl;
    
    public ControladorBatalla(
            ControladorPrincipal contPrin,
            ArrayList<Jugador> jugadores){
        
        this.contPrin = contPrin;
        
        // Instancia y agrega la vista de batalla
        this.visBat = new VistaBatalla();
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visBat);
        // Instancia y agrega el tablero a la vista batalla
        this.tablero = new Tablero(jugadores);
        this.accion = new Accion();
        this.crearVistaTablero();
        // Agrega los listeners a los componentes de la vista batalla
        this.agregarListenersVistaBatalla();
        
        // Agregar la vista para seleccionar el despliegue de dado
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDesp());
        this.agregarListenersVistaSeleccionarDespliegue();
        
        this.agregarJugadoresPartida(jugadores);
        
        this.visPausBat = new SubVistaMenuPausa();
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visPausBat);
        this.agregarListenersVistaPausaBatalla();
        
        Reproductor.reproducir(Constantes.M_BATALLA);
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
            // Reinicia los valores del jugador (Turno y trampas)
            this.tablero.getJugador(i).reiniciar();
            // Crea las vista de resumen de jugador en la batalla
            this.visBat.agregarJugador(jugPartida.get(i));
            // Ubica al jefe de terreno en el tablero
            this.tablero.getPosicion(Constantes.POS_JUG_TAB[i][0], Constantes.POS_JUG_TAB[i][1])
                    .setElemento(jugPartida.get(i).getJefeDeTerreno());
            // Le asigna la posición en que se encuentra el jefe de terreno al jugador
            this.asignarCasilla(Constantes.POS_JUG_TAB[i], i);
            
            // Le asigna dos trampa de cada tipo a un jugador
            for(int j = 1; j <= 3; j++){
                // j: Número de trampa; (i + 1): Número del jugador
                this.tablero.getJugador(i).agregarTrampa(new Trampa(j, i + 1));
                this.tablero.getJugador(i).agregarTrampa(new Trampa(j, i + 1));
            }
            
            // Reinicia los valores de los dados y sus criaturas
            for(Dado dado: this.tablero.getJugador(i).getDados()){
                dado.setParaLanzar(true);
                dado.getCriatura().reiniciar(i + 1);
            }
            
            // Reinicia los valores del jefe de terreno
            this.tablero.getJugador(i).getJefeDeTerreno().reiniciar(i + 1);
        }
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
            public void mouseReleased(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    // Si se cumplen las condiciones para realizar un ataque
                    if(sePuedeAtacar()){
                        // Solicitar la criatura que atacará
                        solicitarCriaturaAtacante();
                    }else{
                        // Sino, muestra un mensaje indicando que no se puede atacar
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
            public void mouseReleased(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    // Si se está realizando una invocación
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
            public void mouseReleased(MouseEvent e){
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
            public void mouseReleased(MouseEvent e){
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
            public void mouseReleased(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(sePuedeColocarTrampa()){
                        mostrarVistaSeleccionTrampa();
                    }else{
                        mostrarMensaje("No tienes puntos de trampa suficientes, no te quedan trampas o no se cumple con las condiciones necesarias en el tablero para colocar una trampa.");
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
            public void mouseReleased(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    Reproductor.pausar();
                    visPausBat.setVisible(true);
                }
            }
        });
        
        this.visBat.getTerminarTurno().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                cambiarTurno();
            }
        });
    }
    
    public void mostrarVistaBatalla(){
        this.visBat.setVisible(true);
    }

    public VistaBatalla getVisBat() {
        return visBat;
    }
    
    // Actualiza las vistas de resumen de jugador en la vista batalla
    public void actualizarVistasJugador(){
        for(int i = 0; i < this.tablero.cantidadJugadores(); i++){
            Jugador jug = this.tablero.getJugador(i);
            Turno turJug = jug.getTurno();
            this.visBat.getVistaJugador(i).actualizarVista(
                    turJug.getPuntosAtaque(),
                    turJug.getPuntosMagia(),
                    turJug.getPuntosMovimiento(),
                    turJug.getPuntosTrampa(),
                    jug.getJefeDeTerreno().getVida(),
                    jug.cantidadTrampaDeOso(),
                    jug.cantidadTrampaParaLadrones(),
                    jug.cantidadRenacerDeLosMuertos());
        }
    }
    
    public void iniciarJuego(){
        this.tablero.getJugador(0).getJefeDeTerreno().restarVida(950);
        this.tablero.getJugador(1).getJefeDeTerreno().restarVida(1050);
        
        this.tablero.setTurnoActual(-1);
        this.cambiarTurno();
    }

    public Tablero getTablero() {
        return tablero;
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
     */
    public void agregarListenersCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                this.visBat.getTablero().getCasilla(i, j).addMouseListener(new MouseAdapter(){                    
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
                            case 2: comprobarCasillaTieneCriaturaNivel((SubVistaPosicion) e.getComponent(), 2);
                                    break;
                            case 3: comprobarCasillaTieneCriaturaNivel((SubVistaPosicion) e.getComponent(), 3);
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
                            case 40:    casillaTieneCriaturaEnemiga((SubVistaPosicion) e.getComponent());
                                        break;
                            case 41:    mostrarAreaAfectada((SubVistaPosicion) e.getComponent());
                                        break;
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e){
                        switch(tablero.getNumAccion()){
                            case 0: break;
                            case 1: visBat.getTablero().reiniciarCasillas();
                                    invocarCriatura((SubVistaPosicion) e.getComponent());
                                    break;
                            case 2: reemplazarCriaturaNivel((SubVistaPosicion) e.getComponent(), 2);
                                    break;
                            case 3: reemplazarCriaturaNivel((SubVistaPosicion) e.getComponent(), 3);
                                    break;
                            case 10:    setCriaturaAMover((SubVistaPosicion) e.getComponent());
                                        break;
                            case 11:    cambiarEstadoCasillaCamino((SubVistaPosicion) e.getComponent());
                                        break;
                            case 20:    setCriaturaAtacante((SubVistaPosicion) e.getComponent());
                                        break;
                            case 21:    comprobarEnemigoSeleccionado((SubVistaPosicion) e.getComponent());
                                        break;
                            case 30:    colocarTrampa((SubVistaPosicion) e.getComponent());
                                        break;
                            case 40:    cambiarEstadoCriaturaAfectada((SubVistaPosicion) e.getComponent());
                                        break;
                            case 41:    setAreaAfectada((SubVistaPosicion) e.getComponent());
                                        break;
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e){
                        ocultarVistaInfoElemento();
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

                                    mostrarDespliegue(
                                            tablero.getNumDespliegue(),
                                            visBat.getTablero().getBotonActual(),
                                            tablero.getDireccion(),
                                            tablero.getTurnoActual());
                                    break;
                            default:    if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                                            finalizarAccion();
                                        }
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
        this.visBat.getTablero().reiniciarCasillas();
        for(int[] coord: this.tablero.getDespliegue(botonActual.getFila(), botonActual.getColumna())){
            Posicion posAct = this.tablero.getPosicion(coord[0], coord[1]);
            if(posAct != null){
                if(posAct.getDueno() == 0){
                    this.visBat.getTablero().getCasilla(coord[0], coord[1]).setImagenSobre(
                            "/Imagenes/Botones/casilla_j" + (turno + 1) + ".png");
                }else{
                    this.visBat.getTablero().getCasilla(coord[0], coord[1]).casillaIncorrecta();
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
            this.asignarCasilla(coord, turno);
        }
    }
    
    /**
     * Asigna una casilla a un jugador y guarda el elemento en campo señalado en dicha
     * posición.
     * @param coord Índice (coordenada) de la casilla.
     * @param turno Turno actual.
     */
    public void asignarCasilla(int[] coord, int turno){
        this.tablero.asignarPosicion(coord, (turno + 1));
        this.visBat.getTablero().marcarCasilla(coord, (turno + 1));
    }
    
    public boolean sePuedeMostrarVistaInfoElemento(SubVistaPosicion posicion){
        Posicion posicionActual = this.tablero.getPosicion(posicion.getFila(), posicion.getColumna());
        return posicionActual.getElemento() != null && !(posicionActual.getElemento() instanceof Trampa);
    }
    
    public void mostrarVistaInfoElemento(SubVistaPosicion posicion){
        Posicion posicionActual = this.tablero.getPosicion(posicion.getFila(), posicion.getColumna());
        
        this.visInfoEl = new SubVistaInfoElemento(
                posicionActual.getElemento());
        this.visBat.add(this.visInfoEl, 0);

        this.timerVisInfoEl = new Timer();
        this.timerVisInfoEl.schedule(new TimerTask(){
            @Override
            public void run(){
                visInfoEl.setVisible(true);
                this.cancel();
                timerVisInfoEl.cancel();
            }
        }, 1000, 1);
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
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el lanzamiento de dados">  
    
    public void crearVistaSeleccionDados(Jugador jugador){
        ArrayList<Dado> dadosParaLanzar = new ArrayList();
        
        for(Dado dado: jugador.getDados()){
            if(dado.isParaJugar() && dado.isParaLanzar()){
                dadosParaLanzar.add(dado);
            }
        }
        
        this.visBat.setVisSelDados(new SubVistaSeleccionDados(dadosParaLanzar));
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
            
            @Override
            public void mouseReleased(MouseEvent e){
                comprobarCantidadDadosSeleccionados((BotonCheckImagen) e.getComponent());
            }
        });
    }
    
    public void agregarListenersVistaSeleccionDados(){
        this.visBat.getVisSelDados().getLanzarDados().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                if(visBat.getVisSelDados().cantidadSeleccionados() >= 1){
                    lanzarDados(visBat.getVisSelDados().getDadosSeleccionados());
                }else{
                    mostrarMensaje("Selecciona al menos un dado para continuar.");
                }
            }
        });
    }
    
    public void comprobarCantidadDadosSeleccionados(BotonCheckImagen panelDado){
        if(this.visBat.getVisSelDados().cantidadSeleccionados() > 4){
                this.mostrarMensaje("Máximo 4 dados.");
                panelDado.deseleccionado();
        }
    }
    
    public void lanzarDados(ArrayList<Dado> dadosALanzar){        
        this.tablero.getJugadorActual().getTurno().lanzarDados(dadosALanzar);
        this.visBat.getVisSelDados().dispose();
        
        this.visBat.setVisLanDados(new SubVistaLanzamientoDados(
                this.tablero.getJugadorActual().getTurno().getResultadoLanzamiento(),
                this.tablero.getJugadorActual().getTurno().getDadosLanzados()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisLanDados());
        this.visBat.getVisLanDados().setVisible(true);
        this.agregarListenersVistaLanzamientoDados();
        
    }
    
    public void mostrarVistaInfoCriatura(BotonCheckImagen panelDado){
        this.visBat.getVisSelDados().setVisInfo(new SubVistaInfoElemento(
                this.visBat.getVisSelDados().getDado(panelDado).getCriatura()));
        this.visBat.getVisSelDados().add(this.visBat.getVisSelDados().getVisInfo(), 0);
        
        this.timerVisInfoEl = new Timer();
        this.timerVisInfoEl.schedule(new TimerTask(){
            @Override
            public void run(){
                visBat.getVisSelDados().getVisInfo().setVisible(true);
                this.cancel();
                timerVisInfoEl.cancel();
            }
        }, 1000, 10);
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
    
    public void agregarListenersVistaLanzamientoDados(){
        this.visBat.getVisLanDados().getAculumarPuntos().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
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
            public void mouseReleased(MouseEvent e){
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
        return this.tablero.getJugadorActual().getTurno().cantidadCarasInvocacion();
    }
    
    public void acumularPuntos(){
        this.tablero.getJugadorActual().getTurno().acumularPuntos();
        this.visBat.getVisLanDados().dispose();
        this.actualizarVistasJugador();
    }
    
    public void realizarAcciones(){
        this.visBat.getVisLanDados().dispose();
        this.acumularPuntos();
        this.visBat.habilitarBotones();
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista pausa batalla">
    
    public void agregarListenersVistaPausaBatalla(){
        this.visPausBat.getContinuarPartida().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                Reproductor.continuar();
                visPausBat.setVisible(false);
            }
        });
        
        this.visPausBat.getVolverMenuPrincipal().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                mostrarMensajeVolverMenuPrincipal();
            }
        });
        
        this.visPausBat.getSalirAplicacion().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                contPrin.getContVisPrin().mostrarMensajeSalir();
            }
        });
    }

    public SubVistaMenuPausa getVisPausBat() {
        return visPausBat;
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
        
    public void mostrarMensaje(String mensaje){
        this.visMen = new SubVistaCuadroDialogo("<html><center>" + mensaje + "</center></html>",
                "Aceptar");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
    }
    
    public void mostrarMensajeCarasInvocacion(){
        this.visMen = new SubVistaCuadroDialogo("<html><center>Atención: Las caras de invocación no se acumulan. ¿Continuar de todos modos?</center></html>",
                "Si", "No");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("puntos_invocacion");
        this.agregarListenersVistaMensaje();
    }
    
    public void mostrarMensajeVolverMenuPrincipal(){
        this.visMen = new SubVistaCuadroDialogo(
                "<html><center>¿Deseas volver al menú principal? Se perderá el progreso de la partida.</center></html>",
                "Si", "No");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("volver_menu");
        this.agregarListenersVistaMensaje();
    }
    
    public void agregarListenersVistaMensaje() {
        this.visMen.getBoton1().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                switch(visMen.getName()){
                    case "volver_menu": volverMenuPrincipal();
                                        break;
                    case "puntos_invocacion":   acumularPuntos();
                                                cambiarTurno();
                                                break;
                }
                visMen.dispose();
            }
        });
        
        this.visMen.getBoton2().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                visMen.dispose();
            }
        });
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el ataque de criatura">  
    
    /*
     * Verifica si se cumplen las condiciones para realizar un ataque.
    */
    public boolean sePuedeAtacar(){
        if(this.tablero.getJugadorActual().getTurno().getPuntosAtaque() != 0){
            int cantidadJugadoresTienenCriaturas = this.tablero.cantidadJugadoresTienenCriaturas();
            
            boolean criaturaQuePuedaAtacar = false;
            for(int i = 0; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    if(!criaturaQuePuedaAtacar){
                        Posicion posAct = this.tablero.getPosicion(i, j);
                        if(posAct.getElemento() instanceof Criatura &&
                           posAct.getElemento().getDueno() == (this.tablero.getTurnoActual() + 1)){
                            for(int[] coord: this.tablero.getIdxVecinos(posAct)){
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

            return cantidadJugadoresTienenCriaturas > 0 && criaturaQuePuedaAtacar;
        }else{
            return false;
        }
    }
    
    public void solicitarCriaturaAtacante(){
        this.tablero.setNumAccion(20);
        this.visBat.deshabilitarBotones();
        this.visBat.setMensaje("Selecciona la criatura que atacará.");
    }
    
    public void setCriaturaAtacante(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura){
            if(posAct.getElemento().getDueno() == (this.tablero.getTurnoActual() + 1)){
                this.accion.setCriaturaAtacante((Criatura) posAct.getElemento());
                this.tablero.setNumAccion(21);
                this.visBat.setMensaje("Selecciona el enemigo a atacar.");
                Reproductor.reproducirEfecto(Constantes.SELECCION);
            }else{
                Reproductor.reproducirEfecto(Constantes.ERROR);
                this.visBat.setMensaje("Esta criatura no te pertenece.");
            }
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Aquí no hay una criatura.");
        }
    }
    
    public void comprobarEnemigoSeleccionado(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() != null && posAct.getElemento().getDueno() != (this.tablero.getTurnoActual() + 1)){
            if(posAct.getElemento() != null && !(posAct.getElemento() instanceof Trampa)){
                this.tablero.setNumAccion(0);
                this.visBat.getTablero().actualizarCasillas();
                Reproductor.reproducirEfecto(Constantes.DANIO);
                
                Timer timerParpadeo = new Timer();
                timerParpadeo.schedule(new TimerTask(){
                    int tic = 0;
                    @Override
                    public void run(){
                        casilla.parpadearIcono();
                        if(tic == 5){
                            this.cancel();
                            timerParpadeo.cancel();
                            atacarEnemigo(posAct);
                        }
                        tic++;
                    }
                }, 0, 100);
            }else{
                Reproductor.reproducirEfecto(Constantes.ERROR);
                this.visBat.setMensaje("Aquí no hay un enemigo que se pueda atacar.");
            }
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Selecciona un enemigo.");
        }
    }
    
    public void atacarEnemigo(Posicion posAct){
        int vida = this.accion.atacarEnemigo(posAct.getElemento());
        if(vida <= 0){
            Reproductor.reproducirEfecto(Constantes.E_MUERE);
            if(posAct.getElemento() instanceof JefeDeTerreno){
                this.tablero.agregarPerdedor(posAct.getDueno() - 1);
                this.eliminarJugadorPartida(posAct.getDueno());
                if(this.tablero.cantidadJugadores() == 1){
                    finalizarPartida();
                }
            }
            posAct.setElemento(null);
        }else if(accion.getCriaturaAtacante().getVida() <= 0){
            Reproductor.reproducirEfecto(Constantes.E_MUERE);
            int[][] vecinos = this.tablero.getIdxVecinos(posAct);
            for(int[] coord: vecinos){
                Posicion posVecinoAct = this.tablero.getPosicion(coord[0], coord[1]);
                if(posVecinoAct.getElemento() instanceof Criatura &&
                   posVecinoAct.getElemento().equals(accion.getCriaturaAtacante())){
                    posVecinoAct.setElemento(null);
                    break;
                }
            }
        }

        this.tablero.getJugadorActual().getTurno().descontarPuntoAtaque();
        this.finalizarAccion();
    }
    
    public void comprobarCasillaCriaturaAtacante(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura &&
           posAct.getElemento().getDueno() == (this.tablero.getTurnoActual() + 1)){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
        }
    }
    
    public void comprobarCasillaElementoAtacado(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if((posAct.getElemento() instanceof Criatura || posAct.getElemento() instanceof JefeDeTerreno) &&
            posAct.getElemento().getDueno() != (this.tablero.getTurnoActual() + 1)){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
        }
        casilla.setImagenActual(1);
    }
        
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la invocación de criaturas">  
    
    /**
     * Agrega los listeners a la "vista" de selección de despliegue de dados.
     */
    public void agregarListenersVistaSeleccionarDespliegue(){        
        for(BotonImagen botonDespliegue: this.visBat.getVisSelDesp().getBotonesDespliegue()){
            botonDespliegue.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
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
        this.visBat.deshabilitarBotones();
        this.visBat.getInvocacion().setEnabled(true);
    }
    
    public void agregarListenersVistaSeleccionCriatura(int i){
        this.visBat.getVisSelCri().getPanelCriatura(i).addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                seleccionarCriaturaAInvocar((BotonImagen) e.getComponent());
            }
        });
    }
        
    public void mostrarVistaSeleccionCriatura(){
        this.visBat.getVisLanDados().dispose();
        
        this.visBat.setVisSelCri(new SubVistaSeleccionCriatura(this.criaturasQueSePuedenInvocar(
                this.tablero.getJugadorActual().getTurno().getDadosLanzados())));
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
        
        Criatura criaturaSeleccionada = this.visBat.getVisSelCri().getCriatura(panelCriatura);
        accion.setCriaturaAInvocar(criaturaSeleccionada);
        
        if(criaturaSeleccionada.getNivel() <= 2){
            this.mostrarVistaSeleccionarDespliegue();
        }else if(criaturaSeleccionada.getNivel() == 3){
            this.tablero.setNumAccion(2);
            this.visBat.setMensaje("Selecciona una criatura de nivel 2.");
        }else{
            this.tablero.setNumAccion(3);
            this.visBat.setMensaje("Selecciona una criatura de nivel 3.");
        }
    }
    
    public void reemplazarCriaturaNivel(SubVistaPosicion casilla, int nivel){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura){
            if(posAct.getElemento().getDueno() == this.tablero.getTurnoActual() + 1){
                if(((Criatura) posAct.getElemento()).getNivel() == nivel){
                    accion.invocarCriatura(posAct, this.tablero.getJugadorActual().getDados());
                    this.finalizarAccion();
                }else{
                    this.visBat.setMensaje("Selecciona una criatura de nivel " + nivel + ".");
                }
            }else{
                this.visBat.setMensaje("Esta criatura no te pertenece.");
            }
        }else{
            this.visBat.setMensaje("Aquí no hay una criatura.");
        }
    }
    
    public boolean sePuedeInvocar(int[][] idxCasillas, int turno){
        return this.tablero.estaDisponible(idxCasillas) &&
               this.tablero.estaConectadoAlTerreno(idxCasillas, (turno + 1));
    }
    
    public void invocarCriatura(SubVistaPosicion posicion){
        int[][] despliegue = tablero.getDespliegue(posicion.getFila(), posicion.getColumna());
        
        if(this.sePuedeInvocar(despliegue, tablero.getTurnoActual())){
            Reproductor.reproducirEfecto(Constantes.E_INVOCACION);
            this.accion.invocarCriatura(this.tablero.getPosicion(posicion.getFila(), posicion.getColumna()),
                    this.tablero.getJugadorActual().getDados());
            asignarCasillas(despliegue, tablero.getTurnoActual());
            this.finalizarAccion();
            this.visBat.getInvocacion().setEnabled(false);
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("No se puede invocar en la posición actual.");
        }
    }
    
    public void comprobarCasillaTieneCriaturaNivel(SubVistaPosicion casilla, int nivel){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura &&
           posAct.getElemento().getDueno() == this.tablero.getTurnoActual() + 1 &&
           ((Criatura) posAct.getElemento()).getNivel() == nivel){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
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
                int cantidadCriaturasInvocadas = this.tablero.numInvOtrosJugadores();
                
                if(cantidadCriaturasInvocadas < 3){
                    magiasDisponibles.remove(i);
                }
                break;
            }
        }
        
        this.visBat.setVisSelMag(new SubVistaSeleccionMagia(
                magiasDisponibles,
                this.tablero.getJugadorActual().getTurno().getPuntosMagia()));
        
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelMag());
        this.visBat.getVisSelMag().setVisible(true);
        
        this.agregarListenersVistaSeleccionMagia();
    }
    
    public void agregarListenersVistaSeleccionMagia(){
        for(int i = 0; i < this.visBat.getVisSelMag().cantidadMagias(); i++){
            this.visBat.getVisSelMag().getPanelMagia(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
                    agregarMagiaActivada(visBat.getVisSelMag().getMagia((BotonImagen) e.getComponent()));
                }
            });
        }
    }
    
    public void agregarMagiaActivada(int[] magia){
        this.visBat.getVisSelMag().dispose();
        
        switch (magia[0]) {
            case 2:
                this.visBat.setMensaje("Selecciona 3 criaturas enemigas.");
                this.accion.reiniciarMagia2();
                this.tablero.setNumAccion(40);
                this.visBat.deshabilitarBotones();
                break;
            case 3:
                this.visBat.setMensaje("Marca el área afectada.");
                this.tablero.setNumAccion(41);
                this.visBat.deshabilitarBotones();
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
                    accion.agregarCriaturaAfectada((Criatura) posAct.getElemento());
                    if(accion.cantidadCriaturasAfectadas() == 3){
                        this.finalizarAccion();
                        this.accion.activarMagia(2, this.tablero.getTurnoActual() + 1);
                        this.tablero.getJugadorActual().getTurno().descontarPuntosMagia(15);
                        this.mostrarMensaje("Se ha activado la magia.");
                    }
                }else{
                    accion.quitarCriaturaAfectada((Criatura) posAct.getElemento());
                }
            }else{
                this.visBat.setMensaje("Elige una criatura enemiga.");
                casilla.deseleccionado();
            }
        }else{
            this.visBat.setMensaje("Aquí no hay una criatura.");
            casilla.deseleccionado();
        }
    }
    
    public void casillaTieneCriaturaEnemiga(SubVistaPosicion casilla){
        this.visBat.getTablero().actualizarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura &&
           posAct.getElemento().getDueno() != (this.tablero.getTurnoActual() + 1)){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
        }
    }
    
    public void mostrarAreaAfectada(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
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
        return this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() >= 1 &&
                this.tablero.cantidadCriaturasInvocadas(this.tablero.getTurnoActual() + 1) != 0;
    }
    
    public void solicitarSeleccionarCriatura(){
        this.visBat.setMensaje("Selecciona una criatura.");
        this.tablero.setNumAccion(10);
        this.visBat.deshabilitarBotones();
    }
    
    public boolean criaturaNoEstaInmovilizada(Posicion posicion){
        return ((Criatura) posicion.getElemento()).getTurnosInmovilizada() == 0;
    }
    
    public void setCriaturaAMover(SubVistaPosicion posicion){
        Posicion posAct = this.tablero.getPosicion(posicion.getFila(), posicion.getColumna());
        if(posAct.getElemento() instanceof Criatura){
            if(((Criatura) posAct.getElemento()).getDueno() == (this.tablero.getTurnoActual() + 1)){
                if(this.criaturaNoEstaInmovilizada(posAct)){
                    Reproductor.reproducirEfecto(Constantes.SELECCION);
                    this.accion.setCriaturaAMover((Criatura) posAct.getElemento());
                    this.accion.agregarPosicionAlCamino(posAct);
                    this.visBat.setMensaje("Marca las casillas del camino.");
                    this.tablero.setNumAccion(11);
                }else{
                    Reproductor.reproducirEfecto(Constantes.ERROR);
                    this.visBat.setMensaje("La criatura está inmovilizada. Quedan " +
                            ((Criatura) posAct.getElemento()).getTurnosInmovilizada() + " turnos.");
                    this.visBat.getTablero().reiniciarCasillas();
                }
            }else{
                Reproductor.reproducirEfecto(Constantes.ERROR);
                this.visBat.setMensaje("Esta criatura no te pertenece.");
                this.visBat.getTablero().reiniciarCasillas();
            }
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Aquí no hay una criatura.");
            this.visBat.getTablero().reiniciarCasillas();
        }
    }
    
    public void cambiarEstadoCasillaCamino(SubVistaPosicion casilla){
        Posicion posicionActual = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(this.accion.caminoContienePosicion(posicionActual)){
            if(this.sePuedeEliminarCasilla(posicionActual)){
                Reproductor.reproducirEfecto(Constantes.MARCA_CAMINO);
                this.accion.eliminarPosicionDelCamino(posicionActual);
            }else{           
                Reproductor.reproducirEfecto(Constantes.ERROR);      
                casilla.seleccionado();
            }
        }else{
            if(this.accion.largoDelCamino() == 0 ||
               this.sePuedeAgregarCasilla(posicionActual)){
                Reproductor.reproducirEfecto(Constantes.MARCA_CAMINO);
                this.accion.agregarPosicionAlCamino(posicionActual);
            }else{  
                Reproductor.reproducirEfecto(Constantes.ERROR);              
                casilla.deseleccionado();
            } 
        }
        
        if(this.accion.largoDelCamino() > 1){
            this.visBat.getMovimiento().setEnabled(true);
        }else{
            this.visBat.getMovimiento().setEnabled(false);
        }
        
        this.pintarCamino();
    }
    
    public boolean sePuedeAgregarCasilla(Posicion posicion){
        if((this.accion.largoDelCamino() - 1) * accion.getCriaturaAMover().getCostoMovimiento() <
                this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() &&
                
          (posicion.getElemento() == null || posicion.getElemento() instanceof Trampa) &&
           posicion.getDueno() != 0){
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
        return this.accion.getUltimaPosicionAgregada().equals(posicion) && accion.largoDelCamino() > 1;
    }
    
    public void pintarCamino(){
        this.visBat.getTablero().reiniciarCasillas();
        for(Posicion posicion: this.accion.getPosicionesMovimiento()){
            this.visBat.getTablero().getCasilla(posicion.getFila(), posicion.getColumna()).seleccionado();
        }
    }
    
    public void moverCriatura(){
        this.tablero.setNumAccion(0);
        this.visBat.deshabilitarBotones();
        
        Timer timerMovimiento = new Timer();
        timerMovimiento.schedule(new TimerTask(){
            int pasos = accion.largoDelCamino();
            int tic = 1;
            
            @Override
            public void run(){
                if(tic != pasos){
                    ElementoEnCampo elemento = accion.moverCriatura(tic);
                    tablero.getJugadorActual().getTurno().descontarPuntosMovimiento(accion.getCriaturaAMover().getCostoMovimiento());

                    Posicion posAnt = accion.getPosicionAnterior();
                    Posicion posAct = accion.getPosicionActual();
                    
                    SubVistaPosicion casAnt = visBat.getTablero().getCasilla(posAnt.getFila(), posAnt.getColumna());
                    SubVistaPosicion casAct = visBat.getTablero().getCasilla(posAct.getFila(), posAct.getColumna());
                    
                    casAnt.setImagenElemento("/Imagenes/vacio.png");
                    casAnt.deseleccionado();
                    
                    casAct.setImagenElemento("/Imagenes/Criaturas/" + accion.getCriaturaAMover().getNomArchivoImagen() + ".png");

                    if(elemento instanceof Trampa &&
                       ((elemento.getDueno() != (tablero.getTurnoActual() + 1) &&
                       ((Trampa) elemento).getNumTrampa() != 3) || 
                       (elemento.getDueno() == (tablero.getTurnoActual() + 1) &&
                       ((Trampa) elemento).getNumTrampa() == 3))) {
                        
                        activarTrampa((Trampa) elemento);
                        visBat.setMensaje("Se ha activado una trampa.");
                        this.cancel();
                        timerMovimiento.cancel();
                    }

                    Reproductor.reproducirEfecto(Constantes.PASO);
                    
                    tic++;
                }else{
                    finalizarAccion();      
                    this.cancel();
                    timerMovimiento.cancel();
                }
            }
        }, 500, 300);        
        
    }
    
    public void comprobarCasillaSeleccionCriatura(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        this.visBat.getTablero().reiniciarCasillas();
        if(posAct.getElemento() instanceof Criatura &&
           posAct.getElemento().getDueno() == (this.tablero.getTurnoActual() + 1)){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
        }
    }
    
    public void comprobarCasillaMovimiento(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        this.pintarCamino();
        if(posAct.equals(this.accion.getUltimaPosicionAgregada()) ||
          (!this.accion.caminoContienePosicion(posAct) && posAct.getDueno() != 0 && 
          (posAct.getElemento() == null || posAct.getElemento() instanceof Trampa))){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
        }
    }
    
    public void mostrarMensajeNoSePuedeMover(){
        this.mostrarMensaje("No tienes criaturas para mover o no tienes puntos de movimiento.");
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con colocar trampas">  
    
    public boolean sePuedeColocarTrampa(){
        return this.tablero.getJugadorActual().getTurno().getPuntosTrampa() >= 10 &&
               this.tablero.getJugadorActual().cantidadTrampas() > 0;
    }
    
    public  void mostrarVistaSeleccionTrampa(){
        this.visBat.setVisSelTram(new SubVistaSeleccionTrampa(
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
                public void mouseReleased(MouseEvent e){
                    setTrampaAColocar(visBat.getVisSelTram().getTrampa((BotonImagen) e.getComponent()));
                }
            });
        }
    }
    
    public void setTrampaAColocar(Trampa trampa){
        this.visBat.getVisSelTram().dispose();
        this.accion.setTrampaAColocar(trampa);
        this.tablero.setNumAccion(30);
        this.visBat.deshabilitarBotones();
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
                    this.finalizarAccion();
                    break;
            case 2: trampa.trampaParaLadrones(accion);
                    this.visBat.getTablero().reiniciarCasillas();
                    moverCriatura();
                    this.finalizarAccion();
                    break;
            case 3: if(!this.tablero.getJugador(trampa.getDueno() - 1).getCriaturasMuertas().isEmpty()){
                        trampa.setPosicionReemplazo(accion.getPosicionActual());
                        trampa.setCriaturaAReemplazar(accion.getCriaturaAMover());
                        this.tablero.setTrampaActivada(trampa);
                        mostrarVistaCriaturaRevivir(trampa.getDueno() - 1);
                        agregarListenersVistaCriaturaRevivir();
                    }else{
                        this.mostrarMensaje("La trampa \"Renacer de los muertos\" no tuvo efecto porque no tienes criaturas muertas.");
                    }
                    this.finalizarAccion();
                    break;
        }
    }
    
    public void mostrarVistaCriaturaRevivir(int numJug){
        this.visBat.setVisCriRev(new SubVistaCriaturaRevivir(this.tablero.getJugador(numJug).getCriaturasMuertas()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisCriRev());
        this.visBat.getVisCriRev().setVisible(true);
    }
    
    public void agregarListenersVistaCriaturaRevivir(){
        for(int i = 0; i < this.visBat.getVisCriRev().getCantidadCriaturas(); i++){
            this.visBat.getVisCriRev().getPanelCriatura(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
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
        this.visBat.getTablero().getCasilla(posicion.getFila(), posicion.getColumna()).setImagenElemento(
                "/Imagenes/Criaturas/" + posicion.getElemento().getNomArchivoImagen() + ".png");
    }
    
    public void comprobarCasillaTrampa(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getDueno() == 0 || posAct.getElemento() != null){
            casilla.casillaIncorrecta();
        }else{
            casilla.casillaCorrecta();
        }
        casilla.setImagenActual(1);
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el turno">  
        
    public void revisarCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.tablero.getPosicion(i, j);
                SubVistaPosicion casAct = this.visBat.getTablero().getCasilla(i, j);
                if(posAct.getElemento() == null){
                    casAct.setImagenElemento("/Imagenes/vacio.png");
                }else if(!(posAct.getElemento() instanceof Trampa)){
                    if(posAct.getElemento() instanceof Criatura){
                        casAct.setImagenElemento("/Imagenes/Criaturas/" + posAct.getElemento().getNomArchivoImagen() + ".png");
                    }else{
                        casAct.setImagenElemento("/Imagenes/Jefes/" + posAct.getElemento().getNomArchivoImagen() + ".png");
                    }
                }
            }
        }
    }
    
    public void cambiarTurno(){
        this.tablero.cambiarTurno();
        for(Dado dado: this.tablero.getJugadorActual().getDados()){
            dado.getCriatura().disminuirTurnosInmovilizada();
            dado.getCriatura().disminuirTurnosCostoMovInc();
        }

        this.tablero.aplicarMagias(accion);
            
        this.finalizarAccion();        
        this.visBat.deshabilitarBotones();
        
        ControladorBatalla contBat = this;
        
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
                    
                    if(tablero.getJugadorActual() instanceof PersonajeNoJugable){
                        new InteligenciaArtificial(contBat, (PersonajeNoJugable) tablero.getJugadorActual());
                    }
                    
                    this.cancel();
                    timerAnimacion.cancel();
                }
                tic++;
            }
        }, 0, 3500);
    }
    
    public void finalizarAccion(){        
        this.tablero.setNumAccion(0);
        actualizarVistasJugador();
        visBat.getTablero().reiniciarCasillas();
        this.revisarCasillas(); 
        this.visBat.habilitarBotones();
        
        ArrayList<int[]> magias = this.accion.getMagias();
        for(int[] magia: magias){
            this.visBat.getVisMagAc().actualizarMagia(magia[0] - 1, magia[1]);
        }
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
        this.visBat.getVistaJugador(numJug - 1).setVisible(false);
        this.revisarCasillas();
    }
    
    public void finalizarPartida(){        
        if(this.tablero.getJugadorActual() instanceof Usuario){
            ArrayList<Jugador> perdedores = this.tablero.getPerdedores();
            Random rnd = new Random();
            ArrayList<Dado> dadosAgregados = new ArrayList();
            for(Jugador perdedor: perdedores){
                dadosAgregados.add(perdedor.getDado(rnd.nextInt(perdedor.getDados().size())));
            }

            try {
                PuzzleDeDadosDAO.agregarDadosAlPuzzle(((Usuario) this.tablero.getJugadorActual()).getId(), dadosAgregados);
                for(Dado dado: dadosAgregados){
                    dado.setParaJugar(false);
                    this.tablero.getJugadorActual().agregarDado(dado);
                }
            } catch (SQLException ex) {
                // Nada
            }
            
        }
        
        Reproductor.reproducir(Constantes.M_GANADOR);
        Reproductor.loop = false;
        
        SubVistaFinDelJuego visFin = new SubVistaFinDelJuego(this.tablero.getTurnoActual() + 1);
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visFin);
        visFin.setVisible(true);
        
        visFin.getFinalizarPartida().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                contPrin.crearControladorMenuPrincipal();
                contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
                visFin.dispose();
                visBat.dispose();
            }
        });
    }
    
// </editor-fold>
}