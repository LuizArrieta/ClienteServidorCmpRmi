package luisarrieta.rmi.conversion;

import luisarrieta.rmi.conversion.net.Servidor;

/** @author LUIS CAMILO ARRIETA MURIEL */
public class Principal {

    public static void main(String[] args) {
        Servidor servicio = new Servidor();
        try {
            servicio.iniciar();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
