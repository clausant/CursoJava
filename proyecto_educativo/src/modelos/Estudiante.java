// Archivo: src/modelos/Estudiante.java
package modelos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rut;
    private LocalDate fechaNacimiento;
    private Direccion direccion;
    private List<Carrera> carreras;

    public Estudiante(int id, String nombres, String apellidoPaterno, String apellidoMaterno, 
                     String rut, LocalDate fechaNacimiento, Direccion direccion) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.rut = rut;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.carreras = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public String getRut() { return rut; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public Direccion getDireccion() { return direccion; }
    public List<Carrera> getCarreras() { return carreras; }

    public void agregarCarrera(Carrera carrera) {
        if (!carreras.contains(carrera) && carreras.size() < 2) {
            carreras.add(carrera);
        } else if (carreras.size() >= 2) {
            throw new IllegalStateException("El estudiante ya está inscrito en el máximo de carreras permitidas (2)");
        }
    }

    public String getNombreCompleto() {
        return String.format("%s %s %s", nombres, apellidoPaterno, apellidoMaterno);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estudiante estudiante = (Estudiante) obj;
        return rut.equals(estudiante.rut);
    }

    @Override
    public int hashCode() {
        return rut.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Estudiante: %s\nRUT: %s\nFecha de Nacimiento: %s\nDirección: %s",
            getNombreCompleto(), rut, fechaNacimiento, direccion);
    }
}
