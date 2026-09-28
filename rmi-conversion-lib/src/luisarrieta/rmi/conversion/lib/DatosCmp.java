package luisarrieta.rmi.conversion.lib;

import java.io.Serializable;

/** @author LUIS CAMILO ARRIETA MURIEL */
public class DatosCmp implements Serializable {

    private float metros;
    private float pies;
    private String interpretacion;

    public DatosCmp() {
    }

    public DatosCmp(float metros) {
        this.metros = metros;
    }

    public float getMetros() { return metros; }
    public void setMetros(float metros) { this.metros = metros; }
    public float getPies() { return pies; }
    public void setPies(float pies) { this.pies = pies; }
    public String getInterpretacion() { return interpretacion; }
    public void setInterpretacion(String interpretacion) { this.interpretacion = interpretacion; }
}
