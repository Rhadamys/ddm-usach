/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author mam28
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AccionTest.class, ElementoEnCampoTest.class, PersonajeNoJugableTest.class, CriaturaTest.class, TrampaTest.class, TurnoTest.class, JefeDeTerrenoTest.class, DadoTest.class, TableroTest.class, PosicionTest.class, UsuarioTest.class, JugadorTest.class, PuzleDeDadosTest.class, TerrenoTest.class})
public class ModelosSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
