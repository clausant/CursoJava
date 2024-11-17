// Archivo: src/modelos/Direccion.java
package modelos;

import java.io.Serializable;

public class Direccion implements Serializable {
    private static final long serialVersionUID = 1L;
    private String calle;
    private String numero;
    private String comuna;
    private String ciudad;
    private String pais;

    public Direccion(String calle, String numero, String comuna, String ciudad, String pais) {
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public String getCalle() { return calle; }
    public String getNumero() { return numero; }
    public String getComuna() { return comuna; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }

    @Override
    public String toString() {
        return String.format("%s #%s, %s, %s, %s", calle, numero, comuna, ciudad, pais);
    }
}
