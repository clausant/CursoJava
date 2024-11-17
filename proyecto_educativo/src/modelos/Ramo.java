package modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ramo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private String codigo;
    private String periodo;
    private Map<Estudiante, Double> notas;

    public Ramo(int id, String nombre, String codigo, String periodo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.periodo = periodo;
        this.notas = new HashMap<>();  // Inicialización del Map
    }

    // Getters básicos
    public int getId() { 
        return id; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public String getCodigo() { 
        return codigo; 
    }

    public String getPeriodo() { 
        return periodo; 
    }

    // Métodos para manejar estudiantes y notas
    public void agregarEstudiante(Estudiante estudiante) {
        if (notas == null) {
            notas = new HashMap<>();
        }
        if (!notas.containsKey(estudiante)) {
            notas.put(estudiante, null);
        }
    }

    public void setNota(Estudiante estudiante, Double nota) {
        if (notas == null) {
            notas = new HashMap<>();
        }
        if (nota >= 1.0 && nota <= 7.0) {
            notas.put(estudiante, nota);
        } else {
            throw new IllegalArgumentException("La nota debe estar entre 1.0 y 7.0");
        }
    }

    public Double getNota(Estudiante estudiante) {
        if (notas == null) {
            return null;
        }
        return notas.get(estudiante);
    }

    public List<Estudiante> getEstudiantes() {
        if (notas == null) {
            notas = new HashMap<>();
        }
        return new ArrayList<>(notas.keySet());
    }

    public Map<Estudiante, Double> getNotas() {
        if (notas == null) {
            notas = new HashMap<>();
        }
        return new HashMap<>(notas);
    }

    // Verificar si un estudiante está inscrito
    public boolean tieneEstudiante(Estudiante estudiante) {
        if (notas == null) {
            return false;
        }
        return notas.containsKey(estudiante);
    }

    // Remover estudiante
    public void removerEstudiante(Estudiante estudiante) {
        if (notas != null) {
            notas.remove(estudiante);
        }
    }

    // Obtener promedio del ramo
    public double getPromedio() {
        if (notas == null || notas.isEmpty()) {
            return 0.0;
        }

        double suma = 0.0;
        int cantidad = 0;

        for (Double nota : notas.values()) {
            if (nota != null) {
                suma += nota;
                cantidad++;
            }
        }

        return cantidad > 0 ? suma / cantidad : 0.0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ramo ramo = (Ramo) obj;
        return id == ramo.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", nombre, codigo, periodo);
    }

    // Método para verificar el estado de aprobación de un estudiante
    public boolean estaAprobado(Estudiante estudiante) {
        Double nota = getNota(estudiante);
        return nota != null && nota >= 4.0;
    }

    // Método para obtener el estado del ramo (con o sin nota)
    public String getEstado(Estudiante estudiante) {
        Double nota = getNota(estudiante);
        if (nota == null) {
            return "Sin calificar";
        }
        return nota >= 4.0 ? "Aprobado" : "Reprobado";
    }
}