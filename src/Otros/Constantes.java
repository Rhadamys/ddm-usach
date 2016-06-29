/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Otros;

import java.awt.Dimension;
import java.awt.Font;

/**
 *
 * @author mam28
 */
public final class Constantes {
    // Coordenadas
    public static final int[][] POS_JUG_TAB = {{7, 0}, {7, 14}, {0, 7}, {14, 7}};
    
    // Fuentes
    public static Font FUENTE_14PX;
    public static Font FUENTE_18PX;
    public static Font FUENTE_24PX;
    public static Font FUENTE_36PX;
    
    // Dimensiones
    public static final Dimension TAMANO_VENTANA = new Dimension(800, 600);
    public static final Dimension TAMANO_CONTENEDOR = new Dimension(598, 335);
    public static final Dimension TAMANO_CONTENEDOR_SIN_BARRA = new Dimension(598, 432);
    public static final int LADO = 100;
    public static final int MARCO = 20;
    
    // Botones
    public static final int BTN_NORMAL = 1;
    public static final int BTN_REDONDO = 2;
    public static final int BTN_ATAQUE = 3;
    public static final int BTN_INVOCACION = 4;
    public static final int BTN_MAGIA = 5;
    public static final int BTN_MOVIMIENTO = 6;
    public static final int BTN_TRAMPA = 7;
    public static final int BTN_PAUSA = 8;
    public static final int BTN_ATRAS = 9;
    public static final int BTN_MARCO = 10;
    public static final int BTN_NUEVA_PARTIDA = 11;
    public static final int BTN_NUEVO_TORNEO = 12;
    public static final int BTN_MODIFICAR_PUZZLE = 13;
    public static final int BTN_INFO_CRIATURAS = 14;
    public static final int BTN_SALIR = 15;
    
    //*****************************************************
    //* Imagenes                                          *
    //*****************************************************
    // Extensiones
    public static final String EXT1 = ".png";
    public static final String EXT2 = ".gif";
    
    // Carpetas
    public static final String RUTA = "/Imagenes/";
    public static final String RUTA_BOTONES = RUTA + "Botones/";
    public static final String RUTA_CRIATURAS = RUTA + "Criaturas/";
    public static final String RUTA_DADOS = RUTA + "Dados/";
    public static final String RUTA_FONDOS = RUTA + "Fondos/";
    public static final String RUTA_JEFES = RUTA + "Jefes/";
    public static final String RUTA_OTROS = RUTA + "Otros/";
    
    // Botones
    public static final String ATAQUE = RUTA_BOTONES + "ataque" + EXT1;
    public static final String ATAQUE_SOBRE = RUTA_BOTONES + "ataque_sobre" + EXT1;
    public static final String ATAQUE_PRESIONADO = RUTA_BOTONES + "ataque_presionado" + EXT1;
    public static final String ATAQUE_DESHABILITADO = RUTA_BOTONES + "ataque_deshabilitado" + EXT1;
    
    public static final String ATRAS = RUTA_BOTONES + "atras" + EXT1;
    public static final String ATRAS_SOBRE = RUTA_BOTONES + "atras_sobre" + EXT1;
    public static final String ATRAS_PRESIONADO = RUTA_BOTONES + "atras_presionado" + EXT1;
    
    public static final String BOTON = RUTA_BOTONES + "boton" + EXT1;
    public static final String BOTON_SOBRE = RUTA_BOTONES + "boton_sobre" + EXT1;
    public static final String BOTON_PRESIONADO = RUTA_BOTONES + "boton_presionado" + EXT1;
    public static final String BOTON_DESHABILITADO = RUTA_BOTONES + "boton_deshabilitado" + EXT1;
    public static final String BOTON_SELECCION = RUTA_BOTONES + "boton_sel" + EXT1;
    public static final String BOTON_SELECCION_SOBRE = RUTA_BOTONES + "boton_sel_sobre" + EXT1;
    public static final String BOTON_SELECCION_PRESIONADO = RUTA_BOTONES + "boton_sel_presionado" + EXT1;
    
    public static final String BOTON_REDONDO = RUTA_BOTONES + "boton_redondo" + EXT1;
    public static final String BOTON_REDONDO_SOBRE = RUTA_BOTONES + "boton_redondo_sobre" + EXT1;
    public static final String BOTON_REDONDO_PRESIONADO = RUTA_BOTONES + "boton_redondo_presionado" + EXT1;
    public static final String BOTON_REDONDO_SELECCION = RUTA_BOTONES + "boton_redondo_sel" + EXT1;
    public static final String BOTON_REDONDO_SELECCION_SOBRE = RUTA_BOTONES + "boton_redondo_sel_sobre" + EXT1;
    public static final String BOTON_REDONDO_SELECCION_PRESIONADO = RUTA_BOTONES + "boton_redondo_sel_presionado" + EXT1;
    
    public static final String CASILLA = RUTA_BOTONES + "casilla" + EXT1;
    public static final String CASILLA_CORRECTA = RUTA_BOTONES + "casilla_correcta" + EXT1;
    public static final String CASILLA_INCORRECTA = RUTA_BOTONES + "casilla_error" + EXT1;
    public static final String CASILLA_SELECCIONADA = RUTA_BOTONES + "casilla_seleccionada" + EXT1;
    public static final String CASILLA_JUGADOR = RUTA_BOTONES + "casilla_j";
    
    public static final String INFO_CRIATURAS = RUTA_BOTONES + "info_criaturas" + EXT1;
    public static final String INFO_CRIATURAS_SOBRE = RUTA_BOTONES + "info_criaturas_sobre" + EXT1;
    public static final String INFO_CRIATURAS_PRESIONADO = RUTA_BOTONES + "info_criaturas_presionado" + EXT1;
    
    public static final String INVOCACION = RUTA_BOTONES + "invocacion" + EXT1;
    public static final String INVOCACION_SOBRE = RUTA_BOTONES + "invocacion_sobre" + EXT1;
    public static final String INVOCACION_PRESIONADO = RUTA_BOTONES + "invocacion_presionado" + EXT1;
    public static final String INVOCACION_DESHABILITADO = RUTA_BOTONES + "invocacion_deshabilitado" + EXT1;
    
    public static final String MAGIA = RUTA_BOTONES + "magia" + EXT1;
    public static final String MAGIA_SOBRE = RUTA_BOTONES + "magia_sobre" + EXT1;
    public static final String MAGIA_PRESIONADO = RUTA_BOTONES + "magia_presionado" + EXT1;
    public static final String MAGIA_DESHABILITADO = RUTA_BOTONES + "magia_deshabilitado" + EXT1;
    public static final String BOTON_MAGIA = RUTA_BOTONES + "magia_";
    
    public static final String MODIFICAR_PUZZLE = RUTA_BOTONES + "modificar_puzzle" + EXT1;
    public static final String MODIFICAR_PUZZLE_SOBRE = RUTA_BOTONES + "modificar_puzzle_sobre" + EXT1;
    public static final String MODIFICAR_PUZZLE_PRESIONADO = RUTA_BOTONES + "modificar_puzzle_presionado" + EXT1;
    
    public static final String MOVIMIENTO = RUTA_BOTONES + "movimiento" + EXT1;
    public static final String MOVIMIENTO_SOBRE = RUTA_BOTONES + "movimiento_sobre" + EXT1;
    public static final String MOVIMIENTO_PRESIONADO = RUTA_BOTONES + "movimiento_presionado" + EXT1;
    public static final String MOVIMIENTO_DESHABILITADO = RUTA_BOTONES + "movimiento_deshabilitado" + EXT1;
    
    public static final String NUEVA_PARTIDA = RUTA_BOTONES + "partida" + EXT1;
    public static final String NUEVA_PARTIDA_SOBRE = RUTA_BOTONES + "partida_sobre" + EXT1;
    public static final String NUEVA_PARTIDA_PRESIONADO = RUTA_BOTONES + "partida_presionado" + EXT1;
    
    public static final String PAUSA = RUTA_BOTONES + "pausa" + EXT1;
    public static final String PAUSA_SOBRE = RUTA_BOTONES + "pausa_sobre" + EXT1;
    public static final String PAUSA_PRESIONADO = RUTA_BOTONES + "pausa_presionado" + EXT1;
    public static final String PAUSA_DESHABILITADO = RUTA_BOTONES + "pausa_deshabilitado" + EXT1;
    
    public static final String SALIR = RUTA_BOTONES + "salir" + EXT1;
    public static final String SALIR_SOBRE = RUTA_BOTONES + "salir_sobre" + EXT1;
    public static final String SALIR_PRESIONADO = RUTA_BOTONES + "salir_presionado" + EXT1;
    
    public static final String TORNEO = RUTA_BOTONES + "torneo" + EXT1;
    public static final String TORNEO_SOBRE = RUTA_BOTONES + "torneo_sobre" + EXT1;
    public static final String TORNEO_PRESIONADO = RUTA_BOTONES + "torneo_presionado" + EXT1;
    
    public static final String TRAMPA = RUTA_BOTONES + "trampa" + EXT1;
    public static final String TRAMPA_SOBRE = RUTA_BOTONES + "trampa_sobre" + EXT1;
    public static final String TRAMPA_PRESIONADO = RUTA_BOTONES + "trampa_presionado" + EXT1;
    public static final String TRAMPA_DESHABILITADO = RUTA_BOTONES + "trampa_deshabilitado" + EXT1;
    public static final String BOTON_TRAMPA = RUTA_BOTONES + "trampa_";
    
    // Fondos
    public static final String CAJON_MENU = RUTA_FONDOS + "cajon_menu" + EXT1;
    public static final String FONDO_AZUL_TRANS = RUTA_FONDOS + "fondo_azul_transparente" + EXT1;
    public static final String FONDO_BATALLA = RUTA_FONDOS + "fondo_batalla" + EXT1;
    public static final String FONDO_CUADRO_DIALOGO = RUTA_FONDOS + "fondo_mensaje" + EXT1;
    public static final String FONDO_INFO_CRIATURAS = RUTA_FONDOS + "fondo_info_criaturas" + EXT1;
    public static final String FONDO_INFO_ELEMENTO = RUTA_FONDOS + "fondo_info_elemento" + EXT1;
    public static final String FONDO_LANZAMIENTO_DADOS = RUTA_FONDOS + "fondo_lanzamiento_dados" + EXT1;
    public static final String FONDO_LOGIN = RUTA_FONDOS + "fondo_login" + EXT1;
    public static final String FONDO_MENU_PAUSA = RUTA_FONDOS + "fondo_menu_pausa" + EXT1;
    public static final String FONDO_MENU_PRINCIPAL = RUTA_FONDOS + "fondo_menu_principal" + EXT1;
    public static final String FONDO_REGISTRO = RUTA_FONDOS + "fondo_registro" + EXT1;
    public static final String FONDO_SELECCION_1 = RUTA_FONDOS + "fondo_seleccion" + EXT1;
    public static final String FONDO_SELECCION_2 = RUTA_FONDOS + "fondo_seleccion_2" + EXT1;
    public static final String FONDO_SELECCION_3 = RUTA_FONDOS + "fondo_seleccion_3" + EXT1;
    public static final String FONDO_SELECCION_4 = RUTA_FONDOS + "fondo_seleccion_4" + EXT1;
    
    // Otros
    public static final String IMAGEN_DADO = RUTA_DADOS + "dado_";
    public static final String IMAGEN_DESPLIEGUE = RUTA_OTROS + "despliegue_";
    public static final String MARCO_SELECCION = RUTA_OTROS + "marco_seleccion" + EXT1;
    public static final String TURNO_ACTUAL = RUTA_OTROS + "turno_actual" + EXT1;
    public static final String VACIO = RUTA + "vacio" + EXT1;
    public static final String VACIO_GRIS = RUTA + "vacio_oscuro" + EXT1;
    public static final String VACIO_CAFE = RUTA + "vacio_cafe" + EXT1;
    
    //*****************************************************
    //* Sonidos                                           *
    //*****************************************************
    // Rutas
    public static final String RUTA_EFECTOS = "src/Efectos/";
    public static final String RUTA_MUSICA = "src/Musica/";
    
    // Extensiones
    public static final String EXT_E = ".wav";
    public static final String EXT_M = ".mp3";
    
    // Música
    public static final String[] M_LOGIN = {"global_menu"};
    public static final String[] M_MENU_PRINCIPAL = {"menu"};
    public static final String[] M_NUEVA_PARTIDA = {"free_duel"};
    public static final String[] M_BATALLA = {"normal", "forest", "sea", "abstract", "destiny", "technology"};
    public static final String[] M_GANADOR = {"you_win"};
    public static final String[] M_OTROS_FORMS = {"other_forms"};
    
    // Efectos
    public static final String ERROR = RUTA_EFECTOS + "error" + EXT_E;
    public static final String PASO = RUTA_EFECTOS + "paso" + EXT_E;
    public static final String CLICK = RUTA_EFECTOS + "click" + EXT_E;
    public static final String DANIO = RUTA_EFECTOS + "ataque" + EXT_E;
    public static final String DADOS_SUBIENDO = RUTA_EFECTOS + "dados_subiendo" + EXT_E;
    public static final String CAE_DADO = RUTA_EFECTOS + "cae_dado" + EXT_E;
    public static final String CAMBIO_TURNO = RUTA_EFECTOS + "cambio_turno" + EXT_E;
    public static final String E_MUERE = RUTA_EFECTOS + "muere" + EXT_E;
    public static final String SELECCION = RUTA_EFECTOS + "seleccion" + EXT_E;
    public static final String MARCA_CAMINO = RUTA_EFECTOS + "camino" + EXT_E;
    public static final String E_INVOCACION = RUTA_EFECTOS + "invocacion" + EXT_E;
}
