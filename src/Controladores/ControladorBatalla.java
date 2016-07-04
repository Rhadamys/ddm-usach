/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.*;
import Otros.*;
import Vistas.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;

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
            ArrayList<Jugador> jugadores,
            boolean esEnEquipos,
            boolean esTorneo){
        
        this.contPrin = contPrin;
        
        // Instancia y agrega la vista de batalla
        this.visBat = new VistaBatalla();
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visBat);
        // Instancia y agrega el tablero a la vista batalla
        this.tablero = new Tablero(jugadores, esEnEquipos, esTorneo);
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
                if(e.getComponent().isEnabled()){
                    cambiarTurno();
                }
            }
        });
    }
    
    /**
     * Agrega las vistas de resumen de información de jugador para los jugadores
     * que conforman esta partida.
     * @param jugPartida Jugadores de la partida para los cuales se crearán las
     * vistas.
     */
    public void agregarJugadoresPartida(ArrayList<Jugador> jugPartida){
        for(int i = 0; i < this.tablero.cantidadJugadores(true); i++){
            Jugador jugAct = this.tablero.getJugador(i);
            
            // Reinicia los valores del jugador (Turno y trampas)
            jugAct.reiniciar(i + 1);
            // Crea las vista de resumen de jugador en la batalla
            this.visBat.agregarJugador(jugAct);
            // Inicializar el terreno del jugador
            jugAct.setTerreno(new Terreno());
            // Ubica al jefe de terreno en el tablero
            this.tablero.getPosicion(Constantes.POS_JUG_TAB[i][0], Constantes.POS_JUG_TAB[i][1])
                    .setElemento(jugAct.getJefeDeTerreno());
            // Le asigna la posición en que se encuentra el jefe de terreno al jugador
            this.asignarCasilla(Constantes.POS_JUG_TAB[i], i + 1);
        }
    }
    
    /**
     * Actualiza las vistas de información de jugador en la vista batalla con los
     * datos actuales del jugador.
     */
    public void actualizarVistasJugador(){
        for(int i = 0; i < this.tablero.cantidadJugadores(true); i++){
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
                    jug.cantidadRenacerDeLosMuertos(),
                    jug.equals(this.tablero.getJugadorActual()));
        }
    }
    
    /**
     * Inicia el juego comenzando el primer turno.
     */
    public void iniciarJuego(){
        this.visBat.setVisible(true);
        
        // Se cambia el turno actual a -1, debido a que luego se sumará 1 al llamar
        // a "cambiarTurno", y entonces el turno será "0".
        this.tablero.setTurnoActual(-1);
        
        // Cambia el turno (Comienza el primer turno)
        this.cambiarTurno();
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista tablero">    
    
    /**
     * Instancia una nueva "vista" de tablero y la agrega a la vista de batalla
     * instanciada en este controlador. Además, crea una nueva instancia del modelo
     * Tablero y la almacena en este controlador.
     */
    public void crearVistaTablero(){
        // Crear la vista tablero
        this.visBat.setTablero(new SubVistaTablero());
        
        // Agregar los listeners a las casillas de la vista tablero
        this.agregarListenersCasillas();
    }
    
    /**
     * Agrega los listeners a una de las posiciones dentro de la "vista" tablero.
     * Las acciones a realizar cuando se interactúe con el mouse o teclado están
     * dadas por un número de acción (Que se encuentra en el modelo Tablero).
     * 
     * Mouse entra a la casilla (mouseEntered): Estos listeners se activarán cuando
     * el mouse se pase por sobre la casilla, y generalmente lo que se hará será
     * "pintar" el tablero.
     * 
     * Se hace clic sobre la casilla (mouseReleased): Estos listeners se activarán
     * cuando el usuario suelte el botón. Se utiliza por sobre "mouseClicked" porque
     * éste último causaba algunos problemas en ciertas situaciones.
     * 
     * Para ver a que corresponde cada número de acción, ver el modelo Tablero.
     */
    public void agregarListenersCasillas(){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                this.visBat.getTablero().getCasilla(i, j).addMouseListener(new MouseAdapter(){                    
                    @Override
                    public void mouseEntered(MouseEvent e){
                        visBat.setMensaje("");
                        switch(tablero.getNumAccion()){
                            case 0:
                                if(sePuedeMostrarVistaInfoElemento((SubVistaPosicion) e.getComponent())){
                                    SubVistaPosicion casAct = (SubVistaPosicion) e.getComponent();
                                    Posicion posAct = tablero.getPosicion(casAct.getFila(), casAct.getColumna());
                                    mostrarVistaInfoElemento(posAct.getElemento(),
                                            e.getComponent().getX() + 150, visBat);
                                }
                                break;
                            case 1: 
                                visBat.getTablero().setBotonActual((SubVistaPosicion) e.getComponent());
                                mostrarDespliegue(visBat.getTablero().getBotonActual());
                                break;
                            case 2: 
                                comprobarCasillaTieneCriaturaNivel((SubVistaPosicion) e.getComponent(), 2);
                                break;
                            case 3: 
                                comprobarCasillaTieneCriaturaNivel((SubVistaPosicion) e.getComponent(), 3);
                                break;
                            case 10:    
                                comprobarCasillaSeleccionCriatura((SubVistaPosicion) e.getComponent());
                                break;
                            case 11:   
                                comprobarCasillaMovimiento((SubVistaPosicion) e.getComponent());
                                break;
                            case 20:    
                                comprobarCasillaCriaturaAtacante((SubVistaPosicion) e.getComponent());
                                break;
                            case 21:
                                comprobarCasillaElementoAtacado((SubVistaPosicion) e.getComponent());
                                break;
                            case 30:    
                                comprobarCasillaTrampa((SubVistaPosicion) e.getComponent());
                                break;
                            case 40:
                                casillaTieneCriaturaEnemiga((SubVistaPosicion) e.getComponent());
                                break;
                            case 41:
                                mostrarAreaAfectada((SubVistaPosicion) e.getComponent());
                                break;
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e){
                        switch(tablero.getNumAccion()){
                            case 0: break;
                            case 1: visBat.getTablero().reiniciarCasillas();
                                    sePuedeInvocar((SubVistaPosicion) e.getComponent());
                                    break;
                            case 2: comprobarSiContieneCriaturaDelNivel((SubVistaPosicion) e.getComponent(), 2);
                                    break;
                            case 3: comprobarSiContieneCriaturaDelNivel((SubVistaPosicion) e.getComponent(), 3);
                                    break;
                            case 10:    setCriaturaAMover((SubVistaPosicion) e.getComponent());
                                        break;
                            case 11:    cambiarEstadoCasillaCamino((SubVistaPosicion) e.getComponent());
                                        break;
                            case 20:    comprobarCriaturaSelec((SubVistaPosicion) e.getComponent());
                                        break;
                            case 21:    comprobarEnemigoSelec((SubVistaPosicion) e.getComponent());
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
                            case 1:
                                // Cambia la dirección del despliegue (visualmente).
                                switch(e.getKeyCode()){
                                    case KeyEvent.VK_W: tablero.setDireccion(0);
                                                        break;
                                    case KeyEvent.VK_A: tablero.setDireccion(1);
                                                        break;
                                    case KeyEvent.VK_S: tablero.setDireccion(2);
                                                        break;
                                    case KeyEvent.VK_D: tablero.setDireccion(3);
                                                        break;
                                }

                                mostrarDespliegue(visBat.getTablero().getBotonActual());
                                break;
                            default:    
                                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
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
     * @param casilla Casilla actual sobre el que se encuentra el mouse.
     */
    public void mostrarDespliegue(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        
        for(int[] coord: this.tablero.getDespliegue(casilla.getFila(), casilla.getColumna())){
            // Obtiene la posición de las coordenadas actuales
            Posicion posAct = this.tablero.getPosicion(coord[0], coord[1]);
            SubVistaPosicion casAct = this.visBat.getTablero().getCasilla(coord[0], coord[1]);
            if(posAct != null && posAct.getDueno() == 0){ // Si la casilla no pertenece a ningún jugador
                casAct.setImagenSobre(Constantes.CASILLA_JUGADOR + (this.getTablero().getTurnoActual() + 1) + 
                        Constantes.EXT1);
                casAct.setImagenActual(1);
            }else{ // Si la casilla ya pertenece a un jugador
                casAct.casillaIncorrecta();
            }
        }
    }
    
    /**
     * Marca las casillas del despliegue como propiedad del jugador del turno actual.
     * @param idxCasillas Índices (Coordenadas) de las casillas que forman el despliegue.
     * @param numJug Número del jugador actual.
     */
    public void asignarCasillas(int[][] idxCasillas, int numJug){
        for(int[] coord: idxCasillas){
            this.asignarCasilla(coord, numJug);
        }
    }
    
    /**
     * Asigna una casilla a un jugador y guarda el elemento en campo señalado en dicha
     * posición.
     * @param coord Índice (coordenada) de la casilla.
     * @param numJug Turno actual.
     */
    public void asignarCasilla(int[] coord, int numJug){
        this.tablero.asignarPosicion(coord, numJug);
        this.visBat.getTablero().marcarCasilla(coord, numJug);
    }
    
    /**
     * Indica si se puede mostrar la vista de información de elemento de la posición
     * actual. Esto se hace para los casos en que la casilla no contenga ningún
     * elemento o el elemento sea una trampa, y así evitar que el jugador pueda
     * ver donde hay una trampa.
     * @param casilla Casilla actual sobre la que se encuentra el mouse.
     * @return Verdadero si se puede mostrar la vista de información.
     */
    public boolean sePuedeMostrarVistaInfoElemento(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        return posAct.getElemento() instanceof Criatura ||
                posAct.getElemento() instanceof JefeDeTerreno;
    }
    
    /**
     * Muestra la vista de información del elemento señalado.
     * @param elemento Elemento para el que se creará la vista.
     * @param x Posición x del botón en la vista (en pixeles).
     * @param vista Vista en la que se mostrará la información
     */
    public void mostrarVistaInfoElemento(ElementoEnCampo elemento, int x, JInternalFrame vista){
        // Instancia una nueva vista de información de elemento.
        this.visInfoEl = new SubVistaInfoElemento(elemento,
                this.tablero.getJugador(elemento.getDueno() - 1));
        
        // Agrega la vista a la vista batalla.
        vista.add(this.visInfoEl, 0);

        // Crea un timer para hacer visible la vista luego de 1 segundo.
        this.timerVisInfoEl = new Timer();
        this.timerVisInfoEl.schedule(new TimerTask(){
            @Override
            public void run(){
                visInfoEl.setVisible(true);
                visInfoEl.setLocation(x > 400 ? 10: 540, 100);
                this.cancel();
                timerVisInfoEl.cancel();
            }
        }, 1000, 1);
    }
    
    /**
     * Muestra la vista de información del elemento señalado.
     * @param dado Dado para el que se creará la vista
     * @param x Posición x del botón en la vista (en pixeles).
     * @param vista Vista en la que se mostrará la información
     */
    public void mostrarVistaInfoElemento(Dado dado, int x, JInternalFrame vista){
        // Instancia una nueva vista de información de elemento.
        this.visInfoEl = new SubVistaInfoElemento(dado);
        
        // Agrega la vista a la vista batalla.
        vista.add(this.visInfoEl, 0);

        // Crea un timer para hacer visible la vista luego de 1 segundo.
        this.timerVisInfoEl = new Timer();
        this.timerVisInfoEl.schedule(new TimerTask(){
            @Override
            public void run(){
                visInfoEl.setVisible(true);
                visInfoEl.setLocation(x > 400 ? 10: 540, 100);
                this.cancel();
                timerVisInfoEl.cancel();
            }
        }, 1000, 1);
    }
    
    /**
     * Oculta la vista de información de elemento. Esto se produce cuando el mouse
     * sale del botón.
     */
    public void ocultarVistaInfoElemento(){
        if(this.visInfoEl != null){
            // Se cancela el timer para mostrar la vista
            this.timerVisInfoEl.cancel();
            
            // Oculta la vista
            this.visInfoEl.setVisible(false);
            
            this.contPrin.getContVisPrin().getVisPrin().repaint();
        }
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el lanzamiento de dados">  
    
    /**
     * Crea la vista de selección de dados con los dados que tiene disponibles el
     * jugador para lanzar.
     * @param jugador Jugador para el que se creará la vista.
     */
    public void crearVistaSeleccionDados(Jugador jugador){
        ArrayList<Dado> dadosParaLanzar = new ArrayList<Dado>();
        
        // Se obtiene una lista de los dados disponibles
        for(Dado dado: jugador.getDados()){
            if(dado.isParaJugar() && dado.isParaLanzar()){
                // isParaJugar indica que el dado pertenece a los 15 dados para jugar.
                // isParaLanzar indica que el dado está disponible para ser lanzado.
                
                dadosParaLanzar.add(dado);
            }
        }
        
        // Se instancia la vista de selección de dados y se agrega al form principal.
        this.visBat.setVisSelDados(new SubVistaSeleccionDados(dadosParaLanzar));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelDados());
        this.visBat.getVisSelDados().setVisible(true);
        
        this.agregarListenersVistaSeleccionDados();
    }
    
    /**
     * Agrega listeners a los botones de los dados.
     */
    public void agregarListenersVistaSeleccionDados(){
        for(int i = 0; i < this.visBat.getVisSelDados().getPanelesDados().size(); i++){
            this.visBat.getVisSelDados().getPanelesDados().get(i).addMouseListener(new MouseAdapter(){            
                @Override
                public void mouseEntered(MouseEvent e){
                    mostrarVistaInfoElemento(
                            visBat.getVisSelDados().getDado((BotonCheckImagen) e.getComponent()),
                            e.getComponent().getX() + 101, visBat.getVisSelDados());
                    visBat.getVisSelDados().setInfoDado((BotonCheckImagen) e.getComponent());
                }

                @Override
                public void mouseExited(MouseEvent e){
                    ocultarVistaInfoElemento();
                    visBat.getVisSelDados().setInfoDado(null);
                }

                @Override
                public void mouseReleased(MouseEvent e){
                    comprobarCantidadDadosSeleccionados((BotonCheckImagen) e.getComponent());
                }
            });
        }
        
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
    
    /**
     * Comprueba que no se hayan seleccionado más de 4 dados.
     * @param panelDado 
     */
    public void comprobarCantidadDadosSeleccionados(BotonCheckImagen panelDado){
        if(this.visBat.getVisSelDados().cantidadSeleccionados() > 4){
                this.mostrarMensaje("Máximo 4 dados.");
                panelDado.deseleccionado();
        }
    }
    
    /**
     * Lanza los dados.
     * @param dadosALanzar Dados que se lanzarán (obtenidos desde la vista de
     * selección de dados.
     */
    public void lanzarDados(ArrayList<Dado> dadosALanzar){
        this.visBat.getVisSelDados().dispose();
        
        // Lanzar los dados lógicamente
        this.tablero.getJugadorActual().getTurno().lanzarDados(dadosALanzar);
        
        // Instancia la vista de lanzamiento de dados
        this.visBat.setVisLanDados(new SubVistaLanzamientoDados(
                    this.tablero.getJugadorActual().getTurno().getResultadoLanzamiento(),
                    this.tablero.getJugadorActual().getTurno().getDadosLanzados(),
                    !(this.tablero.getJugadorActual().cantidadCriaturasInvocadas() == 0 &&
                    this.cantidadCarasInvocacion() > 0)));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisLanDados());
        this.visBat.getVisLanDados().setVisible(true);
        
        // Agrega los listeners a la vista de lanzamiento de dados.
        this.agregarListenersVistaLanzamientoDados();
    }
    
    /**
     * Agrega los listeners a la vista de lanzamiento de dados.
     */
    public void agregarListenersVistaLanzamientoDados(){
        this.visBat.getVisLanDados().getAculumarPuntos().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                if(e.getComponent().isEnabled()){
                    if(cantidadCarasInvocacion() != 0){
                        // Si salió al menos una cara de invocación, se muestra un mensaje
                        // indicando que las caras de invocación no se acumulan.
                        mostrarMensajeCarasInvocacion();
                    }else{
                        // Se acumulan los puntos y se pasa el turno.
                        acumularPuntos();
                        cambiarTurno();
                    }
                }
            }
        });
        
        this.visBat.getVisLanDados().getRealizarAcciones().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                // Se acumulan los puntos del lanzamiento.
                acumularPuntos();
                
                if(cantidadCarasInvocacion() != 0 &&
                    !criaturasQueSePuedenInvocar(tablero.getJugadorActual().getTurno().getDadosLanzados()).isEmpty()){
                    // Si salió al menos una cara de invocación y hay criaturas disponibles para invocar.
                    if(tablero.estaConectadoAlTerrenoDeOtros() &&
                            tablero.getJugadorActual().cantidadCriaturasInvocadas() > 0){
                        // Si está conectado al terreno de otro jugador y tiene al menos una criatura sobre
                        // el terreno, se muestra un mensaje preguntando si desea invocar.
                        mostrarMensajeRealizarInvocacion();
                    }else{
                        // De lo contrario, se obliga al jugador a invocar.
                        mostrarVistaSeleccionCriatura();
                    }
                }else{
                    // De lo contrario, se obliga al jugador a invocar.
                    realizarAcciones();
                }
            }
        });
    }
    
    /**
     * Devuelve la cantidad de caras de invocación resultantes del lanzamiento
     * de dados.
     * @return Cantidad de caras de invocación.
     */
    public int cantidadCarasInvocacion(){
        return this.tablero.getJugadorActual().getTurno().cantidadCarasInvocacion();
    }
    
    /**
     * Acumula los puntos obtenidos en el lanzamiento de dados (Excepto invocación)
     */
    public void acumularPuntos(){
        this.tablero.getJugadorActual().getTurno().acumularPuntos();
        this.visBat.getVisLanDados().dispose();
        this.actualizarVistasJugador();
    }
    
    /**
     * Acumula los puntos y habilita los botones para realizar acciones.
     */
    public void realizarAcciones(){
        this.visBat.getVisLanDados().dispose();
        this.visBat.habilitarBotones();
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista pausa batalla">
    
    /**
     * Agrega los listeners a la vista de pausa en la batalla.
     */
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
    
    /**
     * Las acciones que se ejecutan cuando en el menú de pausa de la batalla se
     * elije "Volver al menú principal".
     */
    public void volverMenuPrincipal(){
        this.visBat.dispose();
        this.visPausBat.dispose();
        this.contPrin.crearControladorMenuPrincipal();
        this.contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
        this.visMen.dispose();
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la vista de cuadro de diálogo"> 
        
    /**
     * Muestra un cuadro de diálogo con un sólo botón "Aceptar".
     * @param mensaje Mensaje que se mostrará en el cuadro de diálogo.
     */
    public void mostrarMensaje(String mensaje){
        this.visMen = new SubVistaCuadroDialogo(mensaje, "Aceptar");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
    }
    
    /**
     * Muestra un mensaje indicando al jugador que las caras de invocación no se
     * acumulan.
     */
    public void mostrarMensajeCarasInvocacion(){
        this.visMen = new SubVistaCuadroDialogo("Atención: Las caras de invocación no se acumulan. ¿Continuar de todos modos?",
                "Si", "No");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("puntos_invocacion");
        this.agregarListenersVistaMensaje();
    }
    
    /**
     * Muestra un mensaje al jugador preguntando si desea realizar una invocación.
     * Este mensaje se muestra cuando el terreno del jugador ya está conectado
     * al terreno de otro jugador, de modo que la invocación es opcional.
     */
    public void mostrarMensajeRealizarInvocacion(){
        this.visMen = new SubVistaCuadroDialogo("¿Deseas invocar una criatura?<br>Recuerda: Las caras de invocación no se acumulan.",
                "Si", "No");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("realizar_invocacion");
        this.agregarListenersVistaMensaje();
    }
    
    /**
     * Muestra un mensaje preguntando si el usuario desea abandonar la partida
     * y volver al menú principal.
     */
    public void mostrarMensajeVolverMenuPrincipal(){
        this.visMen = new SubVistaCuadroDialogo(
                "¿Deseas volver al menú principal? Se perderá el progreso de la partida.",
                "Si", "No");
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visMen);
        this.visMen.setVisible(true);
        this.visMen.setName("volver_menu");
        this.agregarListenersVistaMensaje();
    }
    
    /**
     * Agrega los listeners a la vista mensaje. A esta vista se le asigna un nombre,
     * y dependiendo de ese nombre son las acciones que realizarán los botones.
     */
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
                    case "realizar_invocacion": mostrarVistaSeleccionCriatura();
                                                break;
                }
                visMen.dispose();
            }
        });
        
        this.visMen.getBoton2().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                switch(visMen.getName()){
                    case "realizar_invocacion": realizarAcciones();
                                                break;
                }
                visMen.dispose();
            }
        });
    }
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el ataque de criatura">  
    
    /**
     * Verifica si se cumplen las condiciones para realizar un ataque. Estas
     * verificaciones son:
     * 1. - Que el jugador tenga puntos de ataque disponibles.
     * 2. - Que el jugador tenga sobre el terreno alguna criatura.
     * 3. - Que alguna de esas criaturas esté en condiciones de atacar.
     * @return Verdadero si el jugador puede atacar.
    */
    public boolean sePuedeAtacar(){
        // Comprobar que el jugador cuenta con puntos de ataque.
        if(this.tablero.getJugadorActual().getTurno().getPuntosAtaque() != 0){   
            
            // Buscar alguna criatura que esté en condiciones de atacar.
            for(int i = 0; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    // Si aún no se ha encontrado alguna criatura en condiciones de atacar.
                    // Se obtiene la posición actual del ciclo for.
                    Posicion posAct = this.tablero.getPosicion(i, j);

                    // Si la posición tiene una criatura y el dueño es el jugador del turno actual.
                    if(posAct.getElemento() instanceof Criatura &&
                            posAct.getElemento().getDueno() == this.tablero.getJugadorActual().getNumJug()){

                        // Comprueba que la criatura esté en condiciones de atacar (Tenga un enemigo
                        // al lado suyo.
                        if(this.estaEnCondicionesDeAtacar(posAct)){
                            return true;
                        }
                    }
                }
            }

            return false;
        }else{
            return false;
        }
    }
    
    /**
     * Comprueba si la criatura en la posición indicada está en condiciones de
     * atacar (Que tenga un enemigo al lado)
     * @param posCri Posición de la criatura
     * @return Verdadero si la criatura puede atacar.
     */
    public boolean estaEnCondicionesDeAtacar(Posicion posCri){
        // Revisa las casillas alrededor de la criatura
        for(int[] coord: this.tablero.getIdxVecinos(posCri)){
            // Se obtiene la posición vecina actual del ciclo for.
            Posicion posVecino = this.tablero.getPosicion(coord[0], coord[1]);

            // Si la posición vecina contiene una criatura o un jefe de terreno y el dueño
            // no es el jugador del turno actual o no es del mismo equipo (Es decir, es un enemigo)
            if(posVecino != null & (posVecino.getElemento() instanceof Criatura ||
                    posVecino.getElemento() instanceof JefeDeTerreno) &&
                    ((this.tablero.isEnEquipos() && this.tablero.getJugador(posVecino.getElemento().getDueno() - 1).getEquipo() != this.tablero.getJugadorActual().getEquipo()) ||
                    (!this.tablero.isEnEquipos() && posVecino.getElemento().getDueno() != this.tablero.getJugadorActual().getNumJug()))){

                // Se indica que se ha encontrado una criatura en condiciones de atacar.
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Solicita al usuario que selecciona la criatura con la que desea atacar.
     */
    public void solicitarCriaturaAtacante(){
        this.tablero.setNumAccion(20);
        
        // Evitar que el usuario presione el botón de alguna otra acción
        this.visBat.deshabilitarBotones(); 
        
        this.visBat.setMensaje("Selecciona la criatura que atacará.");
    }
    
    /**
     * Comprueba que en la casilla que presionó el usuario haya una criatura aliada.
     * @param casilla 
     */
    public void comprobarCriaturaSelec(SubVistaPosicion casilla){
        // Obtiene la posición correspondiente a la casilla marcada
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        
        // Si la casilla contiene una criatura
        if(posAct.getElemento() instanceof Criatura){
            // Si el dueño de la criatura es el jugador del turno actual
            if(posAct.getElemento().getDueno() == this.tablero.getJugadorActual().getNumJug()){
                // Si la criatura está en condiciones de atacar
                if(this.estaEnCondicionesDeAtacar(posAct)){
                    this.setCriaturaAtacante((Criatura) posAct.getElemento());
                }else{
                    Reproductor.reproducirEfecto(Constantes.ERROR);
                    this.visBat.setMensaje("Esta criatura no tiene a quién atacar.");
                }
            }else{
                Reproductor.reproducirEfecto(Constantes.ERROR);
                this.visBat.setMensaje("Esta criatura no te pertenece.");
            }
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Aquí no hay una criatura.");
        }
    }
    
    /**
     * Infica al modelo acción la criatura que se eligió para realizar el ataque.
     * @param criatura Criatura que eligió el jugador.
     */
    public void setCriaturaAtacante(Criatura criatura){
        Reproductor.reproducirEfecto(Constantes.SELECCION);
        this.accion.setCriaturaAtacante(criatura);
        this.tablero.setNumAccion(21);
        this.visBat.setMensaje("Selecciona el enemigo a atacar.");
    }
    
    /**
     * Comprueba que en la posición elegida haya un enemigo y que a su lado tenga
     * a la criatura que el jugador previamente eligió para atacar.
     * @param casilla Casilla elegida por el usuario.
     */
    public void comprobarEnemigoSelec(SubVistaPosicion casilla){
        // Obtiene la posición correspondiente a la casilla marcada
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        
        // Si la posición actual contiene una criatura o un jefe de terreno
        if((posAct.getElemento() instanceof Criatura || posAct.getElemento() instanceof JefeDeTerreno) &&
                posAct.getElemento().getDueno() != this.tablero.getJugadorActual().getNumJug()){
            if(this.tablero.isEnEquipos()){
                if(this.tablero.getJugadorActual().getEquipo() != 
                        this.tablero.getJugador(posAct.getElemento().getDueno() - 1).getEquipo()){
                    this.criaturaAtacanteAlrededor(casilla, posAct);
                }else{
                    Reproductor.reproducirEfecto(Constantes.ERROR);
                    this.visBat.setMensaje("¡La criatura pertenece a un aliado!");
                }
            }else{
                this.criaturaAtacanteAlrededor(casilla, posAct);
            }
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Aquí no hay un enemigo que se pueda atacar.");
        }
    }
    
    /**
     * Revisa si el enemigo seleccionado tiene alrededor la criatura que el usuario
     * eligió para atacar, y así evitar ataques a distancia.
     * @param casilla Casilla del enemigo seleccionado.
     * @param posAct Posición correspondiente a la casilla.
     */
    public void criaturaAtacanteAlrededor(SubVistaPosicion casilla, Posicion posAct){
        boolean alrededorCriaturaAtacante = false;
        for(int[] vecino: this.tablero.getIdxVecinos(posAct)){
            Posicion posVecino = this.tablero.getPosicion(vecino[0], vecino[1]);
            if(posVecino != null && posVecino.getElemento().equals(this.accion.getCriaturaAtacante())){
                alrededorCriaturaAtacante = true;
                break;
            }
        }
            
        if(alrededorCriaturaAtacante){
            this.tablero.setNumAccion(0);
            this.visBat.getTablero().actualizarCasillas();
            Reproductor.reproducirEfecto(Constantes.DANIO);

            // Crea un timer para animar el ataque de la criatura, luego ataca logicamente.
            Timer timerParpadeo = new Timer();
            timerParpadeo.schedule(new TimerTask(){
                    int tic = 0;
                    @Override
                    public void run(){
                        casilla.parpadearIcono();
                        if(tic == 5){
                            atacarEnemigo(posAct.getElemento());
                            this.cancel();
                            timerParpadeo.cancel();
                        }
                        tic++;
                    }
                }, 0, 100);
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Este enemigo no está al alcance de la criatura seleccionada.");
        }
    }
    
    /**
     * Ataca al enemigo seleccionado con la criatura seleccionada.
     * @param elemAtacado Elemento que se está atacando.
     */
    public void atacarEnemigo(ElementoEnCampo elemAtacado){
        // Registra el atacaque
        Registro.registrarAccion(Registro.ATAQUE,
                this.tablero.getJugadorActual().getNombreJugador() + ";" +
                (elemAtacado instanceof Criatura ?
                        elemAtacado.getNombre() :
                        this.tablero.getJugador(elemAtacado.getDueno() - 1).getNombreJugador()) + ";" +
                accion.getCriaturaAtacante().getNombre());
        
        // Realiza el ataque y obtiene la vida del elemento atacado luego del ataque.
        int vida = this.accion.atacarEnemigo(elemAtacado);
        
        // Si la vida del elemento es menor o igual a cero (murió)
        if(vida <= 0){
            Reproductor.reproducirEfecto(Constantes.E_MUERE);
            
            // Si el elemento era un jefe de terreno
            if(elemAtacado instanceof JefeDeTerreno){
                // Se elimina al jugador de la partida
                this.eliminarJugadorPartida(elemAtacado.getDueno());
                
                // Si sólo queda un jugador
                if(this.tablero.cantidadJugadores(false) == 1 ||
                        this.tablero.isEnEquipos()){
                    // Finaliza la partida
                    finalizarPartida();
                    return;
                }
            }
            
            // Elimina al elemento del tablero
            this.tablero.getPosElem(elemAtacado).setElemento(null);
            
        // Si la criatura atacante murió (El enemigo tenía mayor defensa que el ataque de la criatura)
        }else if(accion.getCriaturaAtacante().getVida() <= 0){
            Reproductor.reproducirEfecto(Constantes.E_MUERE);
            
            // Elimina la criatura atacante del tablero
            this.tablero.getPosElem(accion.getCriaturaAtacante()).setElemento(null);
        }

        // Decuenta el punto de ataque utilizado
        this.tablero.getJugadorActual().getTurno().descontarPuntoAtaque();
        
        // Finaliza el ataque
        this.finalizarAccion();
    }
    
    // VISUALES
    /**
     * Marca la casilla como correcta o incorrecta dependiendo de si se cumplen
     * las condiciones al seleccionar la criatura que atacará.
     * @param casilla Casilla sobre la que se encuentra el mouse.
     */
    public void comprobarCasillaCriaturaAtacante(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura &&
                posAct.getElemento().getDueno() == this.tablero.getJugadorActual().getNumJug() &&
                this.estaEnCondicionesDeAtacar(posAct)){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
        }
    }
    
    /**
     * Marca la casilla como correcta o incorrecta dependiendo de si se cumplem o
     * no las condiciones al seleccionar el enemigo que se atacará.
     * @param casilla Casilla actual sobre la que se encuentra el mouse.
     */
    public void comprobarCasillaElementoAtacado(SubVistaPosicion casilla){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if((posAct.getElemento() instanceof Criatura || posAct.getElemento() instanceof JefeDeTerreno) &&
                posAct.getElemento().getDueno() != this.tablero.getJugadorActual().getNumJug()){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
        }
        casilla.setImagenActual(1);
    }
        
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la invocación de criaturas">  
    
// <editor-fold defaultstate="collapsed" desc="Selección de criatura a invocar">  
    
    /**
     * Obtiene una lista con las criaturas que se pueden invocar dependiendo de
     * las condiciones del jugador en el juego.
     * @param dados Dados lanzados
     * @return Lista de criaturas
     */
    public ArrayList<Criatura> criaturasQueSePuedenInvocar(ArrayList<Dado> dados){
        ArrayList<Criatura> criaturas = new ArrayList<Criatura>();
        
        // Por cada dado lanzado
        for(Dado dado: dados){
            // Obtiene el nivel de la criatura del dado
            int nivel = dado.getCriatura().getNivel();
            if(nivel == 1){
                // Si es de nivel 1, siempre se puede invocar
                criaturas.add(dado.getCriatura());
            }else if(this.cantidadCarasInvocacion() >= 2){
                // Sólo se pueden invocar criaturas de nivel 2 o más si en el resultado
                // del lanzamiento de dados se obtuvo 2 o más caras de invocación.
                
                if(nivel == 2){
                    // Si la criatura es de nivel 2, se puede invocar.
                    criaturas.add(dado.getCriatura());
                }else if(this.tablero.getJugadorActual().haInvocadoCriaturasDelNivel(nivel - 1)){
                    // Si la criatura es de nivel 3 o 4, se revisa si el jugador tiene sobre
                    // el terreno criaturas de nivel 2 o 3 respectivamente para reemplazar.
                    criaturas.add(dado.getCriatura());
                }
            }
        }
        
        // Devuelve la lista de criaturas que se pueden invocar
        return criaturas;
    }
   
    /**
     * Agrega los listeners a la vista de selección de criatura.
     */
    public void agregarListenersVistaSeleccionCriatura(){
        for(int i = 0; i < this.visBat.getVisSelCri().getCantidadCriaturas(); i++){
            this.visBat.getVisSelCri().getPanelCriatura(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
                    setCriaturaAInvocar(visBat.getVisSelCri().getCriatura((BotonImagen) e.getComponent()));
                }
                
                @Override
                public void mouseEntered(MouseEvent e){
                    mostrarVistaInfoElemento(visBat.getVisSelCri().getCriatura((BotonImagen) e.getComponent()),
                            e.getComponent().getX() + 101, visBat.getVisSelCri());
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    ocultarVistaInfoElemento();
                }
            });
        }
    }
        
    /**
     * Muestra la vista de selección de criatura (Para invocar)
     */
    public void mostrarVistaSeleccionCriatura(){
        // Cierra la vista de lanzamiento de dados
        this.visBat.getVisLanDados().dispose();
        
        // Instancia la vista de selección de criatura
        this.visBat.setVisSelCri(new SubVistaSeleccionCriatura(this.criaturasQueSePuedenInvocar(
                this.tablero.getJugadorActual().getTurno().getDadosLanzados())));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelCri());
        this.visBat.getVisSelCri().setVisible(true);
        
        // Agrega los listeners
        this.agregarListenersVistaSeleccionCriatura();
    }
    
    /**
     * Infica al modelo acción la criatura que se invocará y dependiendo del nivel
     * de la criatura seleccionada se indica las acciones siguientes a realizar.
     * @param criSelec Criatura que eligió el usuario en la vista de selección
     * de criaturas.
     */
    public void setCriaturaAInvocar(Criatura criSelec){
        // Oculta la vista de selección de criatura.
        this.visBat.getVisSelCri().dispose();
        
        // Le indica al modelo acción la criatura que se invocará
        accion.setCriaturaAInvocar(criSelec);
        
        if(criSelec.getNivel() <= 2){
            this.mostrarVistaSeleccionarDespliegue();
        }else{
            this.tablero.setNumAccion(criSelec.getNivel() - 1);
            this.visBat.setMensaje("Selecciona una criatura de nivel " + criSelec.getNivel() + ".");
        }
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Selección de despliegues">  
    
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
    
    /**
     * Muestra la vista de selección de despliegues.
     */
    public void mostrarVistaSeleccionarDespliegue(){
        this.visBat.getVisSelDesp().setVisible(true);
    }
    
    /**
     * Cambia el valor del despliegue a mostrar en el tablero.
     * @param numDespliegue Número de despliegue a mostrar.
     */
    public void cambiarDespliegue(int numDespliegue){
        this.visBat.getVisSelDesp().setVisible(false);
        this.tablero.setNumAccion(1);
        this.tablero.setNumDespliegue(numDespliegue);
        this.visBat.deshabilitarBotones();
        this.visBat.getInvocacion().setEnabled(true);
    }
    
// </editor-fold>
    
    /**
     * Comprueba si se puede invocar en la casilla seleccionada.
     * @param casilla Casilla que eligió el usuario.
     */
    public void sePuedeInvocar(SubVistaPosicion casilla){
        // Obtiene el despliegue
        int[][] despliegue = tablero.getDespliegue(casilla.getFila(), casilla.getColumna());
        
        // Si todas las posiciones del despliegue están disponibles
        if(this.tablero.estaDisponible(despliegue)){
            // Si el despliegue está conectado al terreno del jugador
            if(this.tablero.estaConectadoAlTerreno(despliegue, this.tablero.getJugadorActual().getNumJug())){
                // Invoca la criatura
                this.invocarCriatura(despliegue, this.tablero.getPosicion(casilla.getFila(), casilla.getColumna()));
            }else{
                Reproductor.reproducirEfecto(Constantes.ERROR);
                this.visBat.setMensaje("El despliegue no está conectado a tu terreno.");
            }
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Esta casilla está ocupada por ti u otro jugador.");
        }
    }
    
    /**
     * Invoca la criatura en la ubicación indicada.
     * @param despliegue Despliegue de dados.
     * @param posInv Posición en la que se ubicará la criatura.
     */
    public void invocarCriatura(int[][] despliegue, Posicion posInv){
        // Invoca a la criatura (la sitúa en la posición indicada)
        this.accion.invocarCriatura(posInv, this.tablero.getJugadorActual().getDados());
        
        // Marca las casillas como propiedad del jugador que está invocando
        asignarCasillas(despliegue, this.tablero.getJugadorActual().getNumJug());
        
        // Finaliza la acción.
        this.finalizarAccion();
        
        // Deshabilita el botón para cambiar el despliegue.
        this.visBat.getInvocacion().setEnabled(false);
        
        // Registra la invocación realizada.
        Registro.registrarAccion(Registro.INVOCACION, this.tablero.getJugadorActual().getNombreJugador() +
                ";" + accion.getCriaturaAInvocar().getNombre());
    }
    
    /**
     * Comprueba si la casilla seleccionada tiene una criatura del nivel necesario
     * para realizar el reemplazo.
     * @param casilla Casilla seleccionada por el usuario.
     * @param nivel Nivel de la criatura que se necesita para el reemplazo.
     */
    public void comprobarSiContieneCriaturaDelNivel(SubVistaPosicion casilla, int nivel){
        // Se obtiene la posición correspondiente a la casilla seleccionada
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        
        // Si la posición contiene una criatura
        if(posAct.getElemento() instanceof Criatura){
            // Si el dueño es el jugador del turno actual
            if(posAct.getElemento().getDueno() == this.tablero.getJugadorActual().getNumJug()){
                // Si la criatura es del nivel necesario para el reemplazo
                if(((Criatura) posAct.getElemento()).getNivel() == nivel){
                    // Se reemplaza la criatura.
                    this.reemplazarCriatura(posAct);
                }else{
                    Reproductor.reproducirEfecto(Constantes.ERROR);
                    this.visBat.setMensaje("Selecciona una criatura de nivel " + nivel + ".");
                }
            }else{
                Reproductor.reproducirEfecto(Constantes.ERROR);
                this.visBat.setMensaje("Esta criatura no te pertenece.");
            }
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Aquí no hay una criatura.");
        }
    }
    
    /**
     * Reemplaza la criatura en la posición señalada por la nueva criatura seleccionada
     * por el jugador.
     * @param posReemp Posición en la que se realizará el reemplazo.
     */
    public void reemplazarCriatura(Posicion posReemp){
        // Sacrifica a la criatura
        ((Criatura) posReemp.getElemento()).restarVida(-1);
        
        // Invoca a la nueva criatura en la posición indicada.
        accion.invocarCriatura(posReemp, this.tablero.getJugadorActual().getDados());
        
        // Finaliza la acción.
        this.finalizarAccion();
    }
    
    // VISUAL
    /**
     * Marca la casilla como correcta o incorrecta dependiendo de si en la posición
     * hay o no una criatura del nivel necesario para realizar el reemplazo.
     * @param casilla Casilla sobre la que se encuentra el mouse.
     * @param nivel Nivel necesario para realizar el reemplazo.
     */
    public void comprobarCasillaTieneCriaturaNivel(SubVistaPosicion casilla, int nivel){
        this.visBat.getTablero().reiniciarCasillas();
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura &&
                posAct.getElemento().getDueno() == this.tablero.getJugadorActual().getNumJug() &&
                ((Criatura) posAct.getElemento()).getNivel() == nivel){
            casilla.casillaCorrecta();
        }else{
            casilla.casillaIncorrecta();
        }
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con la activación de magias">  
    
    /**
     * Comprueba si se pueden activar magias, viendo si hay magias disponibles y
     * si el jugador cuenta con los puntos necesarios para activar la magia de menor
     * costo disponible.
     * @return Verdadero si el jugador puede activar alguna magia.
     */
    public boolean sePuedeActivarMagia(){
        ArrayList<int[]> magiasDisponibles = this.accion.getMagiasDisponibles();
        
        // Si hay alguna magia disponible
        if(!magiasDisponibles.isEmpty()){
            int puntosMinimosNecesarios = 30;
            
            // Obtener la cantidad mínima de puntos necesarios
            for(int[] magia: magiasDisponibles){
                if(magia[3] < puntosMinimosNecesarios){
                    puntosMinimosNecesarios = magia[3];
                }
            }
            
            // Verdadero si el jugador tiene la cantidad mínima de puntos necesarios
            return this.tablero.getJugadorActual().getTurno().getPuntosMagia() >= puntosMinimosNecesarios;
        }else{
            return false;
        }
    }
    
    /**
     * Muestra la vista de selección de magias.
     */
    public void mostrarVistaSeleccionMagia(){
        // Obtener las magias disponibles
        ArrayList<int[]> magiasDisponibles = accion.getMagiasDisponibles();
        
        // Ver si dentro de las magias disponibles está la magia 2 (Hierbas venenosas)
        for(int i = 0; i < magiasDisponibles.size(); i++){
            // Si la magia actual es la número 2
            if(magiasDisponibles.get(i)[0] == 2){
                // Si hay menos de 3 criaturas invocadas por otros jugadores
                if(this.tablero.cantidadInvOtrosJugadores() < 3){
                    // Quitar hierbas venenosas de las magias disponibles
                    magiasDisponibles.remove(i);
                    break;
                }
            }
        }
        
        // Intanciar la vista de selección de magia
        this.visBat.setVisSelMag(new SubVistaSeleccionMagia(
                magiasDisponibles,
                this.tablero.getJugadorActual().getTurno().getPuntosMagia(),
                this.accion.getInfoMagias()));
        
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelMag());
        this.visBat.getVisSelMag().setVisible(true);
        
        // Agrega los listeners a la vista de selección de magia
        this.agregarListenersVistaSeleccionMagia();
    }
    
    /**
     * Agrega los listeners a la vista de selección de magia.
     */
    public void agregarListenersVistaSeleccionMagia(){
        for(int i = 0; i < this.visBat.getVisSelMag().cantidadMagias(); i++){
            this.visBat.getVisSelMag().getPanelMagia(i).addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
                    activarMagia(visBat.getVisSelMag().getMagia((BotonImagen) e.getComponent()));
                }
            });
        }
    }
    
    /**
     * Activa la magia seleccionada por el jugador.
     * @param magia Magia seleccionada por el jugador.
     */
    public void activarMagia(int[] magia){
        // Oculta la vissta de selección de magia
        this.visBat.getVisSelMag().dispose();
        
        // Dependiendo del número de la magia seleccionada.
        switch (magia[0]) {
            case 2:
                // Si es la magia número 2, solicita seleccionar las 3 criaturas enemigas.
                this.visBat.setMensaje("Selecciona 3 criaturas enemigas.");
                this.accion.reiniciarMagia2();
                this.tablero.setNumAccion(40);
                this.visBat.deshabilitarBotones();
                break;
            case 3:
                // Si es la magia número 3, solicita marcar el area afectada.
                this.visBat.setMensaje("Marca el área afectada.");
                this.tablero.setNumAccion(41);
                this.visBat.deshabilitarBotones();
                break;
            default:
                // Si es la magia número 1, la activa e informa al usuario.
                this.finalizarAccion();
                this.accion.activarMagia(magia[0], this.tablero.getJugadorActual());
                this.tablero.getJugadorActual().getTurno().descontarPuntosMagia(magia[3]);
                this.mostrarMensaje("Se ha activado la magia.");
                break;
        }
    }
    
    public void cambiarEstadoCriaturaAfectada(SubVistaPosicion casilla){
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        if(posAct.getElemento() instanceof Criatura){
            if((this.tablero.isEnEquipos() && this.tablero.getJugadorActual().getEquipo() != this.tablero.getJugador(posAct.getElemento().getDueno() - 1).getEquipo()) ||
                    (!this.tablero.isEnEquipos() && posAct.getElemento().getDueno() != this.tablero.getJugadorActual().getNumJug())){
                if(casilla.isSelected()){
                    accion.agregarCriaturaAfectada((Criatura) posAct.getElemento());
                    if(accion.cantidadCriaturasAfectadas() == 3){
                        this.finalizarAccion();
                        this.accion.activarMagia(2, this.tablero.getJugadorActual());
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
           posAct.getElemento().getDueno() != this.tablero.getJugadorActual().getNumJug()){
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
                SubVistaPosicion casAct = this.visBat.getTablero().getCasilla(filaEsqSupIzq + i, columnaEsqSupIzQ + j);
                if(casAct != null){
                    casAct.seleccionado();
                }
            }
        }
    }
    
    public void setAreaAfectada(SubVistaPosicion casilla){
        int filaEsqSupIzq = casilla.getFila() - 5;
        int columnaEsqSupIzQ = casilla.getColumna() - 5;
        ArrayList<Posicion> areaAfectada = new ArrayList<Posicion>();
        
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                Posicion posAct = this.tablero.getPosicion(filaEsqSupIzq + i, columnaEsqSupIzQ + j);
                if(posAct != null){
                    areaAfectada.add(posAct);
                }
            }
        }
        
        accion.setAreaDeEfecto(areaAfectada);
        accion.activarMagia(3, this.tablero.getJugadorActual());
        this.tablero.getJugadorActual().getTurno().descontarPuntosMagia(30);
        this.finalizarAccion();
        this.mostrarMensaje("Se ha activado la magia en el área marcada.");
    }
    
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el movimiento de criaturas"> 
    
    public boolean sePuedeMover(){
        return this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() >= 1 &&
               this.tablero.getJugadorActual().cantidadCriaturasInvocadas() != 0;
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
            if(((Criatura) posAct.getElemento()).getDueno() == this.tablero.getJugadorActual().getNumJug()){
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
                if(this.accion.getUltimaPosicionAgregada().equals(this.tablero.getPosicion(coord[0], coord[1]))){
                    return true;
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

                    if(elemento instanceof Trampa){
                        if(elemento.getDueno() == (tablero.getJugadorActual().getNumJug()) &&
                               ((Trampa) elemento).getNumTrampa() == 3){
                            
                        }else if(tablero.isEnEquipos() &&
                                tablero.getJugador(elemento.getDueno() - 1).getEquipo() != tablero.getJugadorActual().getEquipo() &&
                               ((Trampa) elemento).getNumTrampa() != 3){
                            
                        }else if(!tablero.isEnEquipos() &&
                                elemento.getDueno() != tablero.getJugadorActual().getNumJug() &&
                               ((Trampa) elemento).getNumTrampa() != 3){

                                activarTrampa((Trampa) elemento);
                                visBat.setMensaje("Se ha activado una trampa.");
                                this.cancel();
                                timerMovimiento.cancel();
                        }
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
           posAct.getElemento().getDueno() == this.tablero.getJugadorActual().getNumJug()){
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
                
                @Override
                public void mouseEntered(MouseEvent e){
                    mostrarVistaInfoElemento(visBat.getVisSelTram().getTrampa((BotonImagen) e.getComponent()),
                            e.getComponent().getX() + 80, visBat.getVisSelTram());
                }
                
                @Override
                public void mouseExited(MouseEvent e){
                    ocultarVistaInfoElemento();
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
                        Registro.registrarAccion(Registro.ACTIVACION_TRAMPA,
                                this.tablero.getJugadorActual().getNombreJugador() + ";" +
                                String.valueOf(true));
                        
                        trampa.setPosicionReemplazo(accion.getPosicionActual());
                        trampa.setCriaturaAReemplazar(accion.getCriaturaAMover());
                        this.tablero.setTrampaActivada(trampa);
                        mostrarVistaCriaturaRevivir(trampa.getDueno() - 1);
                        agregarListenersVistaCriaturaRevivir();
                    }else{
                        Registro.registrarAccion(Registro.ACTIVACION_TRAMPA,
                                this.tablero.getJugadorActual().getNombreJugador() + ";" +
                                String.valueOf(false));
                        
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
                    casAct.setImagenElemento(Constantes.VACIO);
                }else if(!(posAct.getElemento() instanceof Trampa)){
                    if(posAct.getElemento() instanceof Criatura){
                        casAct.setImagenElemento(Constantes.RUTA_CRIATURAS + posAct.getElemento().getNomArchivoImagen() + Constantes.EXT1);
                    }else{
                        casAct.setImagenElemento(Constantes.RUTA_JEFES + posAct.getElemento().getNomArchivoImagen() + Constantes.EXT1);
                    }
                }
            }
        }
    }
    
    public void cambiarTurno(){
        if(this.tablero.soloQuedanPNJs()){
            do{
                int numJug = 0;
                do{
                    numJug = new Random().nextInt(this.tablero.cantidadJugadores(true)) + 1;
                    if(this.tablero.estaEnPerdedores(this.tablero.getJugador(numJug - 1))){
                        numJug = 0;
                    }
                }while(numJug == 0);
                this.tablero.agregarPerdedor(numJug);
            }while(this.tablero.cantidadJugadores(false) != 1);
            
            this.tablero.cambiarTurno();
            this.finalizarPartida();
        }else{
            this.visBat.mostrarBarraBotones();

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
                            visBat.ocultarBarraBotones();
                            new InteligenciaArtificial(contBat, (PersonajeNoJugable) tablero.getJugadorActual());
                        }

                        this.cancel();
                        timerAnimacion.cancel();
                    }
                    tic++;
                }
            }, 0, 3500);
            
            Constantes.cambiarCursor(this.tablero.getJugadorActual().getNumJug());

            Registro.registrarAccion(Registro.TURNO, this.tablero.getJugadorActual().getNombreJugador());
        }
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
        this.tablero.agregarPerdedor(numJug);
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                Posicion posAct = this.tablero.getPosicion(i, j);
                if(posAct.getElemento() instanceof Criatura &&
                        (posAct.getElemento().getDueno() == numJug ||
                        (posAct.getElemento() instanceof Trampa &&
                        ((Trampa) posAct.getElemento()).getDueno() == numJug &&
                        ((Trampa) posAct.getElemento()).getNumTrampa() == 3))){
                    posAct.setElemento(null);
                }
            }
        }
        
        this.visBat.getVistaJugador(numJug - 1).setVisible(false);
        
        ArrayList<int[]> magiasActivadas = this.accion.getMagiasActivadas();
        for(int[] magia: magiasActivadas){
            if(magia[2] == numJug){
                this.accion.desactivarMagia(magia[0], this.tablero);
            }
        }
    }
    
    public void finalizarPartida(){        
        for(int i = 0; i < this.tablero.cantidadJugadores(true); i++){
            // Obtener el jugador
            Jugador jug = this.tablero.getJugador(i);
            
            // Incrementar partidas jugadas
            jug.aumPartJug();
            
            // Actualizar partidas jugadas en la BDD
            Jugador.actualizarPartidaJugada(jug.getId(), jug.getPartJug());
            
            // Si el jugador es el ganador
            if(!this.tablero.estaEnPerdedores(jug)){
                // Incrementar partidas ganadas
                jug.aumPartGan();
                
                // Actualizar partidas ganadas en la BDD
                Jugador.actualizarPartidaGanada(jug.getId(), jug.getPartGan());
            }
        }
            
        if(this.tablero.isEnEquipos()){
            int numEquipoGanador = this.tablero.getJugadorActual().getEquipo();
            ArrayList<Jugador> ganadores = new ArrayList<Jugador>();
            for(int i = 0; i < this.tablero.cantidadJugadores(true); i++){
                Jugador jugAct = this.tablero.getJugador(i);
                if(jugAct.getEquipo() != numEquipoGanador && !this.tablero.estaEnPerdedores(jugAct)){
                    this.eliminarJugadorPartida(jugAct.getNumJug());
                }else if(jugAct.getEquipo() == numEquipoGanador){
                    ganadores.add(jugAct);
                }
            }
                
            ArrayList<Jugador> perdedores = this.tablero.getPerdedores();
            Random rnd = new Random();
            ArrayList<Dado> dadosAgregados = new ArrayList<Dado>();
            for(Jugador perdedor: perdedores){
                dadosAgregados.add(perdedor.getDado(rnd.nextInt(perdedor.getDados().size())));
            }
            
            for(Jugador ganador: ganadores){
                if(ganador instanceof Usuario){
                    PuzzleDeDados.agregarDadosAlPuzzle(ganador.getId(), dadosAgregados);
                    for(Dado dadoGanado: dadosAgregados){
                        dadoGanado.setParaJugar(false);
                        ganador.agregarDado(dadoGanado);
                    }
                }
            }
        }else{
            if(this.tablero.getJugadorActual() instanceof Usuario && !this.tablero.isTorneo()){
                ArrayList<Jugador> perdedores = this.tablero.getPerdedores();
                Random rnd = new Random();
                ArrayList<Dado> dadosAgregados = new ArrayList<Dado>();
                for(Jugador perdedor: perdedores){
                    dadosAgregados.add(perdedor.getDado(rnd.nextInt(perdedor.getDados().size())));
                }

                PuzzleDeDados.agregarDadosAlPuzzle(this.tablero.getJugadorActual().getId(), dadosAgregados);
                for(Dado dado: dadosAgregados){
                    dado.setParaJugar(false);
                    this.tablero.getJugadorActual().agregarDado(dado);
                }
            }
        }
        
        Reproductor.reproducir(Constantes.M_GANADOR);
        
        SubVistaFinDelJuego visFin = new SubVistaFinDelJuego(this.tablero.isEnEquipos() ? 
                this.tablero.getJugadorActual().getEquipo() : this.tablero.getJugadorActual().getNumJug(),
                this.tablero.isEnEquipos());
        
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visFin);
        visFin.setVisible(true);
        
        visFin.getFinalizarPartida().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                if(tablero.isTorneo()){
                    contPrin.getContTor().finalizarPartidaTorneo(tablero.getJugadorActual(), tablero.getPerdedores());
                }else{
                    contPrin.crearControladorMenuPrincipal();
                    contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
                }
                visFin.dispose();
                visBat.dispose();
            }
        });
        
        Constantes.cambiarCursor(0);
        
        Registro.registrarAccion(Registro.JUGADOR_GANA, this.tablero.getJugadorActual().getNombreJugador());
    }
    
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Getters && Setters"> 
    
    public VistaBatalla getVisBat() {
        return visBat;
    }
    
    public Tablero getTablero() {
        return tablero;
    }

    public Accion getAccion() {
        return accion;
    }

    public ControladorPrincipal getContPrin() {
        return contPrin;
    }
    
// </editor-fold>
}