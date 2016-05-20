package modelos;



public class Modelologin {
    private String user;
    private String pass;
    
    //Metodos basicos//
    public Modelologin() {
        this.user = null;
        this.user = null;//vacio//
    }
    
    public Modelologin(String user, String pass) { //constructor//
        this.user = user;
        this.pass = pass;
    }
    
    public void setUser(String user) {  //independientes//
        this.user = user;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public void setPass(String pass){
        this.pass=pass;
    }
    
    public String getPass() {
        return this.pass;
    }
    
    //Metodos adicionales//
    
    public boolean verificarEntrada(String user, String pass) {
        System.out.println("Estoy acá");
        if ((user.length() == 0) || (pass.length() == 0)) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean existeUsuario(String user) {
        if (user.equals(this.user)) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean iniciarSesion(String user, String pass) {
        if (user.equals(this.user) && pass.equals(this.pass)){
            return true;
        }
        else {
            return false;
        }
    }
        
}
    
    
    
    


