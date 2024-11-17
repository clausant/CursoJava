// Archivo: src/modelos/Carrera.java
package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrera implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private String codigo;
    private List<Ramo> ramos;

    public Carrera(int id, String nombre, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.ramos = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public List<Ramo> getRamos() { return ramos; }

    public void agregarRamo(Ramo ramo) {
        if (!ramos.contains(ramo)) {
            ramos.add(ramo);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Carrera carrera = (Carrera) obj;
        return id == carrera.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", nombre, codigo);
    }
}
