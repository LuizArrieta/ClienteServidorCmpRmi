package luisarrieta.rmi.conversion.net;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import luisarrieta.rmi.conversion.lib.IConversionRemota;

/** @author LUIS CAMILO ARRIETA MURIEL */
public class Servidor {

    private final int puerto = 9008;
    private Registry registro;
    private static final String NOMBRE_SERVICIO = "ConversionCmp";

    public void iniciar() throws Exception {
        try {
            IConversionRemota implementacion = new ConversionRemotaImplem();
            registro = LocateRegistry.createRegistry(puerto);
            registro.rebind(NOMBRE_SERVICIO, implementacion);
            System.out.println("Servidor RMI disponible en el puerto " + puerto);
        } catch (Exception ex) {
            throw new Exception("Error al iniciar el registro RMI: " + ex.getMessage());
        }
    }

    public void detener() throws Exception {
        if (registro != null) {
            registro.unbind(NOMBRE_SERVICIO);
        }
    }
}
