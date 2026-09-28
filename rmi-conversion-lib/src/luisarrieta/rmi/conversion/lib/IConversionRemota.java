package luisarrieta.rmi.conversion.lib;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** @author LUIS CAMILO ARRIETA MURIEL */
public interface IConversionRemota extends Remote {

    public DatosCmp convertir(DatosCmp datos) throws RemoteException;
}
