/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelos.*;
import Otros.*;
import Vistas.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JInternalFrame;

/**
 * *****************************************************************************
 * ***                      ALGUNAS CONSIDERACIONES                          ***
 * *****************************************************************************
 * 
 * Diferencia entre casilla y posición: La posición es el elemento lógico (Modelo)
 * de una posición en el tablero, y la casilla es su vista (SubVistaPosicion).
 * 
 * Las interacciones de las casillas con el mouse están dadas por un número de
 * acción, el cual se detalla a continuación:
 * 
 * CUANDO SE HACE CLIC:
 * *****************************************************************************
 * * Núm. * Detalle                                                            *
 * *****************************************************************************
 * * 0    * No se realiza acción alguna                                        *
 * *****************************************************************************
 * * 1    * Intenta invocar una criatura en la posición de la casilla seleccio-*
 * *      * nada con el despliegue de dados elegido.                           *
 * *****************************************************************************
 * * 2    * Intenta reemplazar una criatura en la posición seleccionada. La    *
 * *      * criatura debe ser de nivel 2.                                      *
 * *****************************************************************************
 * * 3    * Intenta reemplazar una criatura en la posición seleccionada. La    *
 * *      * criatura debe ser de nivel 3.                                      *
 * *****************************************************************************
 * * 10   * Selecciona la criatura en la posición marcada para moverla.        *
 * *****************************************************************************
 * * 11   * Marcar el camino del movimiento de la criatura.                    *
 * *****************************************************************************
 * * 20   * Selecciona a la criatura en la posición marcada para realizar un   *
 * *      * ataque.                                                            *
 * *****************************************************************************
 * * 21   * Selecciona a la criatura o jefe enemigo en la posición marcada para*
 * *      * atacarlo / a.                                                      *
 * *****************************************************************************
 * * 30   * Coloca una trampa en la posición marcada.                          *
 * *****************************************************************************
 * * 40   * Agrega o quita la criatura de la posición marcada de la lista de   *
 * *      * criaturas que serán afectadas por la magia "Hierbas venenosas".    *
 * *****************************************************************************
 * * 41   * Marca el área que será afectada por la magia "Meteoritos de fuego" *
 * *****************************************************************************
 * 
 * Estos números de acción se utilizan también en los listeners de "mouseEntered"
 * para marcar la posición como "Correcta" (En verde) o "Incorrecta" (en rojo)
 * dependiendo de si se cumplen con las condiciones de cada uno de los casos
 * anteriores.
 * 
 * El número de acción se almacena en el tablero. Se obtiene con el método
 * "getNumAccion()".
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
                            case 10:    compCriaturaSelecMov((SubVistaPosicion) e.getComponent());
                                        break;
                            case 11:    cambiarEstadoCasillaCamino((SubVistaPosicion) e.getComponent());
                                        break;
                            case 20:    comprobarCriaturaSelec((SubVistaPosicion) e.getComponent());
                                        break;
                            case 21:    comprobarEnemigoSelec((SubVistaPosicion) e.getComponent());
                                        break;
                            case 30:    comprobarPosicionTrampa((SubVistaPosicion) e.getComponent());
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
                casAct.setImagenSobre(Constantes.CASILLA_JUGADOR + (this.getTablero().getJugadorActual().getNumJug()) + 
                        Constantes.EXT1);
                casAct.setImagenActual(1);
            }else if(casAct != null){ // Si la casilla ya pertenece a un jugador
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
                contPrin.getContVisPrin().getVisPrin().repaint();
                
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
     * Muestra un mensaje indicando que el jugador no puede mover criaturas.
     */
    public void mostrarMensajeNoSePuedeMover(){
        this.mostrarMensaje("No tienes criaturas para mover o no tienes puntos de movimiento.");
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
            if(posVecino != null && (posVecino.getElemento() instanceof Criatura ||
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
            if(posVecino != null && this.accion.getCriaturaAtacante().equals(posVecino.getElemento())){
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
            
            // Elimina al elemento del tablero
            this.tablero.getPosElem(elemAtacado).setElemento(null);
                
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
    
    /**
     * Magia 2 (Hierbas venenosas): Agrega o quita una criatura de la lista de
     * criaturas afectadas por esta magia. Realiza las comprobaciones para
     * determinar que la criatura es enemiga.
     * @param casilla Casilla seleccionada por el usuario en el tablero.
     */
    public void cambiarEstadoCriaturaAfectada(SubVistaPosicion casilla){
        // Se obtiene la posición correspondiente a la casilla.
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        
        // Si en la posición hay una criatura.
        if(posAct.getElemento() instanceof Criatura){
            // Si es en equipos y los equipos son diferentes o si es individual y la criatura
            // no pertenece al jugador del turno acutal.
            if((this.tablero.isEnEquipos() && this.tablero.getJugadorActual().getEquipo() != this.tablero.getJugador(posAct.getElemento().getDueno() - 1).getEquipo()) ||
                    (!this.tablero.isEnEquipos() && posAct.getElemento().getDueno() != this.tablero.getJugadorActual().getNumJug())){
                // Si la casilla está seleccionada
                if(casilla.isSelected()){
                    // Se agrega la criatura a la lista de criaturas afectadas
                    accion.agregarCriaturaAfectada((Criatura) posAct.getElemento());
                    
                    // Si en la lista hay 3 criaturas
                    if(accion.cantidadCriaturasAfectadas() == 3){
                        // Finaliza la acción
                        this.finalizarAccion();
                        
                        // Activa la magia
                        this.accion.activarMagia(2, this.tablero.getJugadorActual());
                        
                        // Descuenta los puntos de magia
                        this.tablero.getJugadorActual().getTurno().descontarPuntosMagia(15);
                        
                        // Muestra el mensaje de que se ha activado la magia
                        this.mostrarMensaje("Se ha activado la magia.");
                    }
                }else{
                    // Si la casilla está desmarcada, se quita la criatura de la lista
                    // de criaturas afectadas.
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
    
    /**
     * Define el área que será afectada por la magia 3 (Meteoritos de fuego).
     * @param casilla Casilla actual sobre la que se encuentra el mouse.
     */
    public void setAreaAfectada(SubVistaPosicion casilla){
        // Se obtiene el cuadrante que afecta la magia
        
        // 5 casillas arriba de la posición actual
        int filaEsqSupIzq = casilla.getFila() - 5;
        // 5 casillas a la izquierda de la posición actual
        int columnaEsqSupIzQ = casilla.getColumna() - 5;
        
        // Se inicializa el área afectada
        ArrayList<Posicion> areaAfectada = new ArrayList<Posicion>();
        
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                // Se obtiene la posición actual
                Posicion posAct = this.tablero.getPosicion(filaEsqSupIzq + i, columnaEsqSupIzQ + j);
                
                // Si la posición no es null (Está dentro de los límites del tablero.
                if(posAct != null){
                    // Se agrega la posición al área afectada.
                    areaAfectada.add(posAct);
                }
            }
        }
        
        // Se le indica al modelo acción el área afectada.
        accion.setAreaDeEfecto(areaAfectada);
        
        // Se activa la magia
        accion.activarMagia(3, this.tablero.getJugadorActual());
        
        // Se descuentan los puntos de coste de la magia.
        this.tablero.getJugadorActual().getTurno().descontarPuntosMagia(30);
        
        // Finaliza la acción
        this.finalizarAccion();
        
        // Se muestra un mensaje indicando que la magia está activada.
        this.mostrarMensaje("Se ha activado la magia en el área marcada.");
    }
    
    //VISUALES
    /**
     * Marca la casilla como correcta o incorrecta dependiendo de si se cumplen las
     * condiciones de la magia número 2 (Seleccionar enemigo)
     * @param casilla Casilla actual sobre la que se encuentra el mouse.
     */
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
    
    /**
     * Muestra el área que será afectada por la magia "Meteoritos de fuego".
     * @param casilla Casilla actual sobre la que se encuentra el mouse.
     */
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
    
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el movimiento de criaturas"> 
    
    /**
     * Indica si el usuario puede realizar movimiento de criaturas. Devuelve
     * verdadero si tienepor lo menos un punto de movimiento y por lo menos una
     * criatura sobre el terreno.
     * @return Verdadero si puede mover alguna criatura.
     */
    public boolean sePuedeMover(){
        return this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() >= 1 &&
               this.tablero.getJugadorActual().cantidadCriaturasInvocadas() != 0;
    }
    
    /**
     * Cambia la acción del tablero para solicitar al usuario que seleccione la
     * criatura que desea mover.
     */
    public void solicitarSeleccionarCriatura(){
        // Se muestra un mensaje indicando que se seleccione una criatura
        this.visBat.setMensaje("Selecciona una criatura.");
        
        // Se cambia el número de acción
        this.tablero.setNumAccion(10);
        
        // Deshabilita los botones para evitar que el usuario elija otra acción
        // mientras se está seleccionando la criatura.
        this.visBat.deshabilitarBotones();
    }
    
    /**
     * Comprueba si la casilla seleccionada tiene una criatura del jugador y si
     * la criatura seleccionada se puede mover.
     * @param casilla Casilla que seleccionó el usuario.
     */
    public void compCriaturaSelecMov(SubVistaPosicion casilla){
        // Se obtiene la posición correspondiente a la casilla
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        
        // Si la posición tiene una criatura
        if(posAct.getElemento() instanceof Criatura){
            // Si el dueño de la criatura es el jugador del turno actual
            if(posAct.getElemento().getDueno() == this.tablero.getJugadorActual().getNumJug()){
                // Si la craitura no está inmovilizada
                if(((Criatura) posAct.getElemento()).getTurnosInmovilizada() == 0){
                    // Define la criatura que se moverá
                    this.setCriaturaAMover(posAct);
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
    
    /**
     * Define la criatura que se moverá.
     * @param posCri Posición de la criatura.
     */
    public void setCriaturaAMover(Posicion posCri){
        Reproductor.reproducirEfecto(Constantes.SELECCION);
        
        // Indica al modelo acción la criatura que se moverá
        this.accion.setCriaturaAMover((Criatura) posCri.getElemento());
        
        // Agrega la posición de la criatura al camino
        this.accion.agregarPosicionAlCamino(posCri);
        
        // Muestra un mensaje indicando que se seleccione el camino
        this.visBat.setMensaje("Marca las casillas del camino.");
        
        // Se cambia el número de acción
        this.tablero.setNumAccion(11);
    }
    
    /**
     * Cambia el estado de una casilla del camino, es decir, la agrega o la quita
     * del camino.
     * @param casilla Casilla que presionó el usuario. 
     */
    public void cambiarEstadoCasillaCamino(SubVistaPosicion casilla){
        // Posición correspondiente a la casilla
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        
        // Si el camino contiene la posición
        if(this.accion.caminoContienePosicion(posAct)){
            // Si se puede eliminar la posición del camino
            if(this.sePuedeEliminarCasilla(posAct)){
                Reproductor.reproducirEfecto(Constantes.MARCA_CAMINO);
                
                // Elimina la posición del camino
                this.accion.eliminarPosicionDelCamino(posAct);
            }else{           
                Reproductor.reproducirEfecto(Constantes.ERROR);   
            }
        }else{ // Si el camino no contiene la posición
            // Si se puede agregar la posición al camino
            if(this.sePuedeAgregarCasilla(posAct)){
                Reproductor.reproducirEfecto(Constantes.MARCA_CAMINO);
                
                // Agrega la posición al camino
                this.accion.agregarPosicionAlCamino(posAct);
            }else{  
                Reproductor.reproducirEfecto(Constantes.ERROR);
            } 
        }
        
        // Si el camino contiene más de una posición, se habilita el botón "Mover"
        // para que el usuario pueda mover la criatura.
        if(this.accion.largoDelCamino() > 1){
            this.visBat.getMovimiento().setEnabled(true);
        }else{
            this.visBat.getMovimiento().setEnabled(false);
        }
        
        // Re-pinta el camino seleccionado por el usuario
        this.pintarCamino();
    }
    
    /**
     * Indica si se puede agregar al camino la posición señalada
     * @param posAct Posición actual que se intenta añadir.
     * @return Verdadero si se puede agregar la posición.
     */
    public boolean sePuedeAgregarCasilla(Posicion posAct){
        // Si el largo del camino multiplicado por el coste del movimiento de la criatura
        // seleccionada es menor o igual a los puntos de movimiento de los que dispone el usuario
        // y además la posición está vacía o tiene una trampa (oculta) y la posición pertenece
        // a algún jugador.
        if((this.accion.largoDelCamino() - 1) * accion.getCriaturaAMover().getCostoMovimiento() <=
                this.tablero.getJugadorActual().getTurno().getPuntosMovimiento() &&
                (posAct.getElemento() == null || posAct.getElemento() instanceof Trampa) &&
                 posAct.getDueno() != 0){
            
            // Comprueba si la posición está al lado de la última posición agregada. Así se 
            // evita que el camino quede cortado.
            for(int[] coord: this.tablero.getIdxVecinos(posAct)){                    
                if(this.accion.getUltimaPosicionAgregada().equals(this.tablero.getPosicion(coord[0], coord[1]))){
                    return true;
                }
            }
        }else{
            return false;
        }
        
        return false;
    }
    
    /**
     * Indica si se puede eliminar la posición del camino. La única condición que
     * aquí existe es que la posición seleccionada sea la última posición agregada
     * al camino (así se evita cortarlo) y que además el largo sea de por lo menos
     * una casilla (donde esa "una" casilla es la posición de la criatura).
     * @param posAct Posición actual que se está intentando eliminar
     * @return Verdadero si se puede eliminar la posición del camino.
     */
    public boolean sePuedeEliminarCasilla(Posicion posAct){
        return this.accion.getUltimaPosicionAgregada().equals(posAct) && accion.largoDelCamino() > 1;
    }
    
    /**
     * Pinta el camino seleccionado por el jugador (de color naranja).
     */
    public void pintarCamino(){
        // Reiniciar las casillas (las desmarca todas)
        this.visBat.getTablero().reiniciarCasillas();
        
        // Por cada posición del camino, la selecciona y queda pintada de color naranja.
        for(Posicion posicion: this.accion.getPosicionesMovimiento()){
            this.visBat.getTablero().getCasilla(posicion.getFila(), posicion.getColumna()).seleccionado();
        }
    }
    
    /**
     * Mover a la criatura (Posición por posición). En caso de que en el camino
     * la criatura active alguna trampa, se corta el proceso y se activan los
     * efectos de la trampa.
     */
    public void moverCriatura(){
        // Esto es para evitar que el usuario pueda interrumpir el movimiento
        this.tablero.setNumAccion(0);
        this.visBat.deshabilitarBotones();
        
        // Crea un timer que animará el movimiento
        Timer timerMovimiento = new Timer();
        timerMovimiento.schedule(new TimerTask(){
            int pasos = accion.largoDelCamino();
            int tic = 1;
            
            @Override
            public void run(){
                // Si aún no se llega a la posición final
                if(tic != pasos){
                    // Mueve la criatura a la siguiente posición y obtiene el elemento
                    // de la posición actual en que se encuentra la criatura.
                    ElementoEnCampo elemento = accion.moverCriatura(tic);
                    
                    // Descuenta una cantidad de puntos de movimiento equivalente al
                    // costo de movimiento de la criatura.
                    tablero.getJugadorActual().getTurno().descontarPuntosMovimiento(accion.getCriaturaAMover().getCostoMovimiento());

                    // Obtiene la posición actual y anterior de la criatura.
                    Posicion posAnt = accion.getPosicionAnterior();
                    Posicion posAct = accion.getPosicionActual();
                    
                    // Obtiene las casillas correspondientes a las posiciones anteriormente obtenidas.
                    SubVistaPosicion casAnt = visBat.getTablero().getCasilla(posAnt.getFila(), posAnt.getColumna());
                    SubVistaPosicion casAct = visBat.getTablero().getCasilla(posAct.getFila(), posAct.getColumna());
                    
                    // Elimina el icono de la criatura de la casilla anterior y la deselecciona.
                    casAnt.setImagenElemento(Constantes.VACIO);
                    casAnt.deseleccionado();
                    
                    // Pone el icono de la criatura sobre la casilla actual
                    casAct.setImagenElemento(Constantes.RUTA_CRIATURAS +
                            accion.getCriaturaAMover().getNomArchivoImagen() + Constantes.EXT1);

                    // Si el elemento en la posición actual de la criatua es una trampa.
                    if(elemento instanceof Trampa){
                        // La trampa se activa si y sólo si:
                        
                        // Si el número de trampa es 3 (Renacer de los muertos) y quien mueve
                        // a la criatura es el jugador actual
                        if((elemento.getDueno() == (tablero.getJugadorActual().getNumJug()) &&
                               ((Trampa) elemento).getNumTrampa() == 3) ||
                                
                                // O el número de trampa es distinto de 3 y:
                                // La batalla es en equipos y el número de equipo de quien colocó la trampa
                                // es distinto al número de equipo del jugador que mueve la criatura.
                                (tablero.isEnEquipos() &&
                                tablero.getJugador(elemento.getDueno() - 1).getEquipo() != tablero.getJugadorActual().getEquipo() &&
                               ((Trampa) elemento).getNumTrampa() != 3) ||
                                
                                // O la batalla no es en equipos y el número del jugador que mueve la criatura
                                // es distinto al número de jugador que colocó la trampa.
                                (!tablero.isEnEquipos() &&
                                elemento.getDueno() != tablero.getJugadorActual().getNumJug() &&
                               ((Trampa) elemento).getNumTrampa() != 3)){

                            // Se activa la trampa
                            activarTrampa((Trampa) elemento);
                            
                            // Se muestra un mensaje indicando que se activó una trampa
                            visBat.setMensaje("Se ha activado una trampa.");
                            
                            // Se cancela el movimiento
                            this.cancel();
                            timerMovimiento.cancel();
                        }
                    }
                    
                    Reproductor.reproducirEfecto(Constantes.PASO);
                    
                    tic++;
                }else{
                    // Se finalizar el movimiento
                    finalizarAccion();      
                    this.cancel();
                    timerMovimiento.cancel();
                }
            }
        }, 500, 300);        
        
    }
    
    // VISUALES
    /**
     * Marca la casilla como correcta o incorrecta dependiendo de si se cumplen
     * las condiciones de selección de la criatura a mover.
     * @param casilla Casilla actual sobre la que se encuentra el mouse.
     */
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
    
    /**
     * Marca la casilla como correcta o incorrecta dependiendo de si se cumplen
     * las condiciones de selección de posición para el movimiento.
     * @param casilla Casilla actual sobre la que se encuentra el mouse.
     */
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
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con trampas">  

// <editor-fold defaultstate="collapsed" desc="Colocar trampas">  
    
    /**
     * Indica si el jugador puede colocar trampas. Las condiciones con que el jugador
     * tenga una cantidad de puntos de trampa mayor o igual al coste de la trampa de
     * menor coste dentro de sus trampas disponibles y que tenga por lo menos una trampa
     * disponible.
     * @return Verdadero si puede colocar alguna trampa.
     */
    public boolean sePuedeColocarTrampa(){
        ArrayList<Trampa> trampasDisp = this.tablero.getJugadorActual().getTrampas();
        
        // Si hay alguna trampa disponible
        if(!trampasDisp.isEmpty()){
            int puntosMinimosNecesarios = 30;
            
            // Obtener la cantidad mínima de puntos necesarios
            for(Trampa trampa: trampasDisp){
                if(trampa.getCosto() < puntosMinimosNecesarios){
                    puntosMinimosNecesarios = trampa.getCosto();
                }
            }
            
            // Verdadero si el jugador tiene la cantidad mínima de puntos necesarios
            return this.tablero.getJugadorActual().getTurno().getPuntosTrampa() >= puntosMinimosNecesarios;
        }else{
            return false;
        }
    }
    
    /**
     * Muestra la vista de selección de trampa del jugador, en la cual aparecen
     * las trampas que tiene disponibles.
     */
    public  void mostrarVistaSeleccionTrampa(){
        // Instancia la vista de selección de trampa
        this.visBat.setVisSelTram(new SubVistaSeleccionTrampa(
                this.tablero.getJugadorActual().getTrampas(),
                this.tablero.getJugadorActual().getTurno().getPuntosTrampa()));
        
        // Agrega la vista al formulario principal y la hace visible
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisSelTram());
        this.visBat.getVisSelTram().setVisible(true);
        
        // Agrega los listeners a la vista
        this.agregarListenersVistaSeleccionTrampa();
    }
    
    /**
     * Agrega los listeners a la vista de selección de trampa
     */
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
    
    /**
     * Define la trampa que se colocará.
     * @param trampa Trampa seleccionada en la vista de selección de trampa.
     */
    public void setTrampaAColocar(Trampa trampa){
        this.visBat.getVisSelTram().dispose();
        
        // Le indica al modelo acción la trampa que se colocará.
        this.accion.setTrampaAColocar(trampa);
        
        // Cambia el número de acción
        this.tablero.setNumAccion(30);
        
        // Deshabilita los botones para evitar que el usuario pueda realizar
        // otra acción mientras selecciona la posición en la que desea colocar
        // la trampa.
        this.visBat.deshabilitarBotones();
        
        // Muestra un mensaje indicando que elija la posición en la que quiere
        // colocar la trampa.
        this.visBat.setMensaje("Selecciona una posición para colocar la trampa.");
    }
    
    /**
     * Comprueba si se puede colocar la trampa en la posición indicada.
     * @param casilla Casilla seleccionada por el usuario.
     */
    public void comprobarPosicionTrampa(SubVistaPosicion casilla){
        // Posición correspondiente a la casilla
        Posicion posAct = this.tablero.getPosicion(casilla.getFila(), casilla.getColumna());
        
        // Si la posición pertenece a algún jugador
        if(posAct.getDueno() != 0){
            // Si la posición está vacía (No tiene criatura, otra trampa, etc).
            if(posAct.getElemento() == null){
                // Coloca la trampa en la posición actual.
                this.colocarTrampa(posAct);
            }else{
                Reproductor.reproducirEfecto(Constantes.ERROR);
                this.visBat.setMensaje("La casilla está ocupada. Selecciona una casilla disponible.");
            }
        }else{
            Reproductor.reproducirEfecto(Constantes.ERROR);
            this.visBat.setMensaje("Selecciona una casilla que pertenezca a algún jugador.");
        }
    }
    
    /**
     * Coloca la trampa en la posición indicada.
     * @param posTram Posición en la que se colocará la trampa.
     */
    public void colocarTrampa(Posicion posTram){
        // Indica al modelo acción que coloque la trampa en la posición entregada.
        accion.colocarTrampa(posTram, this.tablero.getJugadorActual());
        
        // Finaliza la acción
        this.finalizarAccion();
        
        // Muestra un mensaje indicando que se ha colocado la trampa
        this.mostrarMensaje("Se ha colocado la trampa en la posición indicada.");
    }
    
    // VISUALES
    /**
     * Marca la casilla como correcta o incorrecta dependiendo de si se cumplen con
     * las condiciones de la posición en la que se colocará la trampa.
     * @param casilla Casilla actual sobre la que se encuentra el mouse.
     */
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
    
// <editor-fold defaultstate="collapsed" desc="Activar trampas">  
    
    /**
     * Activa una trampa si se pasa sobre ella en el movimiento de criatura.
     * @param trampa Trampa que se ha activado.
     */
    public void activarTrampa(Trampa trampa){
        // Dependiendo del número de trampa
        switch(trampa.getNumTrampa()){
            case 1:  // Si la trampa es "Trampa de osos"
                Registro.registrarAccion(Registro.ACTIVACION_TRAMPA,
                                this.tablero.getJugadorActual().getNombreJugador() + ";" +
                                trampa.getNombre() + ";" +
                                this.tablero.getJugador(trampa.getDueno() - 1).getNombreJugador());
                    
                // Se activa el efecto trampa de osos en la trampa
                trampa.trampaDeOsos(accion);
                
                // Finaliza la acción
                this.finalizarAccion();
                break;
                
            case 2: // Si la trampa es "Trampa para ladrones"
                Registro.registrarAccion(Registro.ACTIVACION_TRAMPA,
                                this.tablero.getJugadorActual().getNombreJugador() + ";" +
                                trampa.getNombre() + ";" +
                                this.tablero.getJugador(trampa.getDueno() - 1).getNombreJugador());
                
                // Se activa el efecto trampa para ladrones
                trampa.trampaParaLadrones(accion);
                this.visBat.getTablero().reiniciarCasillas();
                
                // Se hace retroceder una casilla a la criatura
                moverCriatura();
                
                // Finaliza la acción
                this.finalizarAccion();
                break;
                
            case 3: // Si la trampa es renacer de los muertos
                // Si el jugador tiene por lo menos una criatura muerta para revivir
                if(!this.tablero.getJugador(trampa.getDueno() - 1).getCriaturasMuertas().isEmpty()){
                    Registro.registrarAccion(Registro.ACTIVACION_TRAMPA,
                            this.tablero.getJugadorActual().getNombreJugador() + ";" +
                            String.valueOf(true) + ";");

                    // Indica a la trampa la posición en la que se reemplazará la criatura
                    trampa.setPosicionReemplazo(accion.getPosicionActual());
                    
                    // Indica a la trampa la criatura que se reemplazará
                    trampa.setCriaturaAReemplazar(accion.getCriaturaAMover());
                    
                    // Guarda en el tablero la trampa que se ha activado
                    this.tablero.setTrampaActivada(trampa);
                    
                    // Muestra la vista de selección de criatura a revivir
                    mostrarVistaCriaturaRevivir(trampa.getDueno() - 1);
                }else{
                    Registro.registrarAccion(Registro.ACTIVACION_TRAMPA,
                            this.tablero.getJugadorActual().getNombreJugador() + ";" +
                            String.valueOf(false) + ";");

                    // Muestra un mensaje indicando que la trampa no tuvo efecto porque
                    // el jugador no tiene criaturas muertas.
                    this.mostrarMensaje("La trampa \"Renacer de los muertos\" no tuvo efecto porque no tienes criaturas muertas.");
                }
                this.finalizarAccion();
                break;
        }
    }
    
    /**
     * Instancia y muestra la vista para elegir la criatura que se revivirá.
     * @param numJug Número del jugador que activó la trampa.
     */
    public void mostrarVistaCriaturaRevivir(int numJug){
        // Intancia la vista y la muestra.
        this.visBat.setVisCriRev(new SubVistaCriaturaRevivir(this.tablero.getJugador(numJug).getCriaturasMuertas()));
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(this.visBat.getVisCriRev());
        this.visBat.getVisCriRev().setVisible(true);
                    
        // Agrega los listeners a la vista de selección de criatura
        agregarListenersVistaCriaturaRevivir();
    }
    
    /**
     * Agrega los listeners a la vista de selección de criatura a revivir.
     */
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
    
    /**
     * Revive la criatura seleccionada en la posición actual de la trampa.
     * @param criReemp Criatura que se pondrá en la posición. 
     */
    public void revivirCriatura(Criatura criReemp){
        this.visBat.getVisCriRev().dispose();
        
        // Se recupera la trampa desde el tablero.
        Trampa trampa = this.tablero.getTrampaActivada();
        
        // Se activan los efectos de renacer de los muertos, indicando la criatura
        // que se deberá poner en la posición.
        trampa.renacerDeLosMuertos(criReemp,
                this.tablero.getJugador(trampa.getCriaturaAReemplazar().getDueno() - 1));
        
        // Obtiene la posición en la que se realizó el reemplazo
        Posicion posicion = trampa.getPosicionReemplazo();
        
        // Obtiene la casilla correspondiente a la posición y pone la imagen de la criatura
        // que se ha puesto en esa posición.
        this.visBat.getTablero().getCasilla(posicion.getFila(), posicion.getColumna()).setImagenElemento(
                Constantes.RUTA_CRIATURAS + posicion.getElemento().getNomArchivoImagen() + Constantes.EXT1);
    }
    
// </editor-fold>
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Todo lo relacionado con el turno">  
       
    /**
     * Revisa las posiciones buscando cambios y actualiza las casillas (vista).
     * Por ejemplo, si un jugador perdió, todas sus criaturas serán eliminadas
     * del tablero. Este método borrará las imágenes de las criaturas del tablero
     * también.
     */
    public void revisarCasillas(){
        // Se recorre todo el tablero
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                // Se obtiene la posición actual
                Posicion posAct = this.tablero.getPosicion(i, j);
                
                // Se obtiene la casilla correspondiente a la posición
                SubVistaPosicion casAct = this.visBat.getTablero().getCasilla(i, j);
                
                // Si la posición no tiene ningún elemento.
                if(posAct.getElemento() == null){
                    // Se pone la imagen vacío (Transparente)
                    casAct.setImagenElemento(Constantes.VACIO);
                }else if(posAct.getElemento() instanceof Criatura){
                    // Si la posición tiene una criatura, se pone la imagen de la criatura
                    // en la casilla.
                    casAct.setImagenElemento(Constantes.RUTA_CRIATURAS + posAct.getElemento().getNomArchivoImagen() + Constantes.EXT1);
                }else if (posAct.getElemento() instanceof JefeDeTerreno){
                    // Si la posición tiene un jefe, se pone la imagen del jefe
                    // en la casilla.
                    casAct.setImagenElemento(Constantes.RUTA_JEFES + posAct.getElemento().getNomArchivoImagen() + Constantes.EXT1);
                }
            }
        }
    }
    
    /**
     * Finaliza una acción y habilita todo lo necesario para que el usuario pueda
     * realizar alguna otra acción o pasar el turno.
     */
    public void finalizarAccion(){        
        this.tablero.setNumAccion(0);
        
        // Actualiza las vistas de jugador
        actualizarVistasJugador();
        visBat.getTablero().reiniciarCasillas();
        this.revisarCasillas(); 
        this.visBat.habilitarBotones();
        
        // Actualiza la vista de magias que están activadas.
        ArrayList<int[]> magias = this.accion.getMagias();
        for(int[] magia: magias){
            this.visBat.getVisMagAc().actualizarMagia(magia[0] - 1, magia[1]);
        }
    }
    
    /**
     * Cambia el turno.
     */
    public void cambiarTurno(){
        // Si en la partida quedan sólo PNJs
        if(this.tablero.soloQuedanPNJs()){
            // Finaliza la partida, pero primero, determina aleatoriamente el ganador
            // entre los PNJs de la partida.
            do{
                int numJug = 0;
                do{
                    // Obtiene un número de jugador al azar e intenta agregarlo a los
                    // perdedores. Se hace hasta que el jugador encontrado se pueda
                    // agregar a los perdedores.
                    numJug = new Random().nextInt(this.tablero.cantidadJugadores(true)) + 1;
                    if(this.tablero.estaEnPerdedores(this.tablero.getJugador(numJug - 1))){
                        numJug = 0;
                    }
                }while(numJug == 0);
                this.tablero.agregarPerdedor(numJug);
            }while(this.tablero.cantidadJugadores(false) != 1);
            
            // Cambia el turno lógicamente para indicar quién es el jugador que ganó.
            this.tablero.cambiarTurno();
            
            // Finaliza la partida
            this.finalizarPartida();
        }else{
            // Muestra la barra de botones de acción (Movimiento, ataque, etc)
            this.visBat.mostrarBarraBotones();

            // Cambia el turno lógicamente
            this.tablero.cambiarTurno();
            
            // Disminuye a cada criatura la cantidad de turnos que está inmovilizada
            // o que su movimiento está aumentado.
            for(Dado dado: this.tablero.getJugadorActual().getDados()){
                dado.getCriatura().disminuirTurnosInmovilizada();
                dado.getCriatura().disminuirTurnosCostoMovInc();
            }

            // Aplica las magias que estén activadas sobre el tablero.
            this.tablero.aplicarMagias(accion);

            // Finaliza cualquier acción que esté activa y deshabilita los
            // botones de la barra de botones
            this.finalizarAccion();        
            this.visBat.deshabilitarBotones();

            ControladorBatalla contBat = this;
            
            // Instancia un timer con dos acciones (Distinguidas por el "tic")
            Timer timerAnimacion = new Timer();
            timerAnimacion.schedule(new TimerTask(){
                SubVistaCambioTurno visCamTur = new SubVistaCambioTurno(tablero.getTurnoActual());
                int tic = 0;

                @Override
                public void run(){
                    if(tic == 0){
                        // En el primer tic, muestra la vista de cambio de turno (Animación)
                        contPrin.getContVisPrin().getVisPrin().agregarVista(visCamTur);
                        visCamTur.setVisible(true);
                    }else{
                        // En el segundo tic, oculta la vista
                        visCamTur.dispose();
                        crearVistaSeleccionDados(tablero.getJugadorActual());

                        // Si el jugador del turno actual es un PNJ, instancia una
                        // nueva InteligenciaArtificial para el PNJ
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
            
            // Cambia el cursor de la aplicación al color del jugador.
            Constantes.cambiarCursor(this.tablero.getJugadorActual().getNumJug());

            // Registra el cambio de turno.
            Registro.registrarAccion(Registro.TURNO, this.tablero.getJugadorActual().getNombreJugador());
        }
    }
    
    /**
     * Elimina a un jugador de la partida. Éste método se llama cuando el jugador
     * muere luego de un ataque.
     * @param numJug Número del jugador que perdió.
     */
    public void eliminarJugadorPartida(int numJug){
        // Agrega al jugador a perdedores.
        this.tablero.agregarPerdedor(numJug);
        
        // Recorre todo el tablero para eliminar todos los elementos del jugador
        // (Jefe de terreno, criaturas y trampas). La única trampa que se elimina
        // es la trampa 3 (Renacer de los muertos), puesto que necesita del jugador
        // para poder activarse.
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                // Se obtiene la posición actual
                Posicion posAct = this.tablero.getPosicion(i, j);
                
                // Si la posición actual tiene algún elemento y su dueño es el
                // jugador que perdió y el elemento es una criatura o es una trampa
                // y su número de trampa es 3 (Renacer de los muertos)
                if(posAct.getElemento() != null && posAct.getElemento().getDueno() == numJug &&
                        (posAct.getElemento() instanceof Criatura ||
                        (posAct.getElemento() instanceof Trampa &&
                        ((Trampa) posAct.getElemento()).getNumTrampa() == 3))){
                    posAct.setElemento(null);
                }
            }
        }
        
        // Oculta la vista de información del jugador
        this.visBat.getVistaJugador(numJug - 1).setVisible(false);
        
        // Desactiva todas las magias que haya activado el jugador
        ArrayList<int[]> magiasActivadas = this.accion.getMagiasActivadas();
        for(int[] magia: magiasActivadas){
            if(magia[2] == numJug){
                this.accion.desactivarMagia(magia[0], this.tablero);
            }
        }
    }
    
    /**
     * Finaliza la partida en curso.
     */
    public void finalizarPartida(){       
        // Actualiza la cantida de partidas jugadas y ganadas para cada jugador
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
            
        // Si la batalla es en equipos
        if(this.tablero.isEnEquipos()){
            // Se obtiene el número del equipo ganador
            int numEquipoGanador = this.tablero.getJugadorActual().getEquipo();
            
            // Se crea una lista donde se guardarán los ganadores
            ArrayList<Jugador> ganadores = new ArrayList<Jugador>();
            
            // Para cada jugador del tablero
            for(int i = 0; i < this.tablero.cantidadJugadores(true); i++){
                // Se obtiene el jugador actual
                Jugador jugAct = this.tablero.getJugador(i);
                
                // Si el número de equipo del jugador es distinto del equipo ganador
                // y además no está en la lista de perdedores.
                if(jugAct.getEquipo() != numEquipoGanador && !this.tablero.estaEnPerdedores(jugAct)){
                    // Se elimina al jugador de la partida (Agrega a perdedores)
                    this.eliminarJugadorPartida(jugAct.getNumJug());
                }else if(jugAct.getEquipo() == numEquipoGanador){
                    // Sino si el número de equipo del jugador es igual al número
                    // del equipo ganador, se agrega a la lista de ganadores.
                    ganadores.add(jugAct);
                }
            }
            
            // Obtiene una copia de un dado de cada perdedor
            ArrayList<Dado> dadosAgregados = this.obtenerCopiaDadoDePerdedores();
            
            // Ahora, por cada ganador
            for(Jugador ganador: ganadores){
                // Si el ganador es un usuario
                if(ganador instanceof Usuario){
                    // Se actualiza su puzzle agregando los dados ganados
                    PuzzleDeDados.agregarDadosAlPuzzle(ganador.getId(), dadosAgregados);
                    
                    // Comprueba si el jugador actual es el usuario activo y agrega los dados a su puzzle
                    this.agregarDadosAlPuzzle(ganador, dadosAgregados);
                }
            }
        }else if(this.tablero.getJugadorActual() instanceof Usuario && !this.tablero.isTorneo()){
            // Si la partida es individual y el jugador actual es un usuario y no se está jugando un torneo
            
            // Obtiene una copia de un dado de cada perdedor
            ArrayList<Dado> dadosAgregados = this.obtenerCopiaDadoDePerdedores();

            // Se agregan los dados al puzzle del jugador (Guardar en BDD)
            PuzzleDeDados.agregarDadosAlPuzzle(this.tablero.getJugadorActual().getId(), dadosAgregados);

            // Comprueba si el ganador es el usuario activo y agrega los dados a su puzzle
            this.agregarDadosAlPuzzle(this.contPrin.getUsuarioActivo(), dadosAgregados);
        }
        
        // Instancia una vista que señala el ganador, ya sea un equipo o un jugador
        SubVistaFinDelJuego visFin = new SubVistaFinDelJuego(this.tablero.isEnEquipos() ? 
                this.tablero.getJugadorActual().getEquipo() : this.tablero.getJugadorActual().getNumJug(),
                this.tablero.isEnEquipos());
        
        this.contPrin.getContVisPrin().getVisPrin().agregarVista(visFin);
        visFin.setVisible(true);
        
        // Agrega el listener al botón "Finalizar partida" de la vista de fin de partida.
        visFin.getFinalizarPartida().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                // Si se está jugando un torneo
                if(tablero.isTorneo()){
                    // Finaliza la partida del torneo, y le indica el jugador que gano
                    // y los perdedores.
                    contPrin.getContTor().finalizarPartidaTorneo(tablero.getJugadorActual(), tablero.getPerdedores());
                }else{ // Si no es un torneo
                    // Vuelve al menú principal
                    contPrin.crearControladorMenuPrincipal();
                    contPrin.getContMenuPrin().mostrarVistaMenuPrincipal();
                }
                visFin.dispose();
                visBat.dispose();
            }
        });
        
        Constantes.cambiarCursor(0);
        
        // Registra el usuario que ganó la partida.
        Registro.registrarAccion(Registro.JUGADOR_GANA, this.tablero.getJugadorActual().getNombreJugador());
    }
    
    /**
     * Obtiene una copia de un dado de cada uno de los perdedores de la partida.
     * @return Lista de copias de dado obtenida desde los perdedores.
     */
    public ArrayList<Dado> obtenerCopiaDadoDePerdedores(){
        // Obtiene la lista de perdedores
        ArrayList<Jugador> perdedores = this.tablero.getPerdedores();
        Random rnd = new Random();
        
        // Crea una nueva lista donde se pondrán las copias de dado
        ArrayList<Dado> dadosAgregados = new ArrayList<Dado>();
        
        // Por cada perdedor
        for(Jugador perdedor: perdedores){
            // Se obtiene una copia de uno de sus dados al azar
            dadosAgregados.add(perdedor.getDado(rnd.nextInt(perdedor.getDados().size())));
        }
        return dadosAgregados;
    }
    
    /**
     * Agrega dados al puzzle al jugador señalado (Sólo si el jugador es el usuario
     * activo). Esto con fines de que el jugador pueda ver su puzzle actualizado
     * al volver al menú. Los otros jugadores no es necesario hacer este proceso puesto
     * que su puzzle se verá actualizado cuando inicien sesión.
     * @param jug Jugador actual que se evaluará.
     * @param dadosAgregados Dados que serán agregados al puzzle del jugador.
     */
    public void agregarDadosAlPuzzle(Jugador jug, ArrayList<Dado> dadosAgregados){
        // Si el nombre del jugador es igual al nombre del usuario activo.
        if(jug.getNombreJugador().equals(
                this.contPrin.getUsuarioActivo().getNombreJugador())){
            // Por cada dado
            for(Dado dado: dadosAgregados){
                // Indica que no es para jugar (Fuera del puzzle de 15 dados)
                dado.setParaJugar(false);
                
                // Agrega el dado al puzzle del jugador
                this.contPrin.getUsuarioActivo().agregarDado(dado);
            }
        }
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