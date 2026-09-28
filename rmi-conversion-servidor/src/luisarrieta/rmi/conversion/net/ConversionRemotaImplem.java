package luisarrieta.rmi.conversion.net;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import luisarrieta.rmi.conversion.lib.DatosCmp;
import luisarrieta.rmi.conversion.lib.IConversionRemota;

/** @author LUIS CAMILO ARRIETA MURIEL */
public class ConversionRemotaImplem extends UnicastRemoteObject implements IConversionRemota {

    public ConversionRemotaImplem() throws RemoteException {
        super();
    }

    @Override
    public DatosCmp convertir(DatosCmp datos) throws RemoteException {
        if (datos.getMetros() < 0) {
            datos.setInterpretacion("ERROR: La longitud en metros debe ser mayor o igual que 0");
            return datos;
        } else {
            float pies = datos.getMetros() * 3.28084f;
            datos.setPies(pies);
            datos.setInterpretacion(datos.getMetros() + " metros equivalen a " + pies + " pies");
            return datos;
        }
    }
}
