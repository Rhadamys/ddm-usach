/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelos.ConjuntoDeDatos;
import vistas.Vistalogin;
import modelos.Modelologin;
        

/**
 *
 * @author Veronica
 */
public class login implements ActionListener{
    //Vistalogin vistaLogin = new Vistalogin();
    Vistalogin vistaLogin;
    ConjuntoDeDatos usuarios;
    Modelologin user;
    
    public login(){
        this.vistaLogin = new Vistalogin();
        this.usuarios = new ConjuntoDeDatos();
        //this.vistaLogin.but.addActionListener(this);
        //crar usuario y pass//
        
    }
    public void IniciarLogin(){}
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()== vistaLogin.getBut()){
            System.out.println("entro!");
            String user = this.vistaLogin.getTextField();
            String pass = this.vistaLogin.getPassField();
            if(this.user.verificarEntrada(user, pass)){
                System.out.println("entro2");
                if(this.user.existeUsuario(user)){
                    if(this.user.iniciarSesion(user, pass)){
                        this.vistaLogin.inicioCorrecto(user);
                    }
                    else{
                        this.vistaLogin.passError(user);
                    }
                }    
                else{
                    this.vistaLogin.userError(user);
                }    
            }
            else{
                this.vistaLogin.camposError();
            }
        }
        //String usuario=vistaLogin.usuario.getText();
        //String pass= String.valueOf(vistaLogin.pass.getPassword());
    }
    
    public static void main(String[] args) {
        login l = new login();
        l.vistaLogin.setVisible(true);
        l.user = new Modelologin("asd", "asdf");
        //l.usuarios.agregar(m);
        l.vistaLogin.agregarListener(l);
    }
            
    
}
