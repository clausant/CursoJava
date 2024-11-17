package sistema;

import modelos.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaGestionEducativa {
    private List<Estudiante> estudiantes;
    private List<Carrera> carreras;
    private List<Ramo> ramos;
    private PersistenciaDatos persistencia;
    private static final double NOTA_APROBACION = 4.0;

    public SistemaGestionEducativa() {
        this.persistencia = new PersistenciaDatos();

        // Intentar cargar datos existentes
        this.estudiantes = persistencia.cargarEstudiantes();
        this.carreras = persistencia.cargarCarreras();
        this.ramos = persistencia.cargarRamos();

        // Si no hay datos guardados, inicializar listas vacías
        if (this.estudiantes == null) this.estudiantes = new ArrayList<>();
        if (this.carreras == null) this.carreras = new ArrayList<>();
        if (this.ramos == null) this.ramos = new ArrayList<>();
    }

    // Métodos para agregar elementos
    public void agregarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
        persistencia.guardarEstudiantes(estudiantes);
    }

    public void agregarCarrera(Carrera carrera) {
        carreras.add(carrera);
        persistencia.guardarCarreras(carreras);
    }

    public void agregarRamo(Ramo ramo) {
        ramos.add(ramo);
        persistencia.guardarRamos(ramos);
    }

    // Método para matricular estudiante en una carrera
    public void matricularEstudianteEnCarrera(Estudiante estudiante, Carrera carrera) {
        if (estudiantes.contains(estudiante) && carreras.contains(carrera)) {
            estudiante.agregarCarrera(carrera);
            persistencia.guardarEstudiantes(estudiantes);
        }
    }

    // Método para inscribir ramo
    public void inscribirRamo(Estudiante estudiante, Carrera carrera, Ramo ramo) {
        if (estudiante.getCarreras().contains(carrera)) {
            ramo.agregarEstudiante(estudiante);
            carrera.agregarRamo(ramo);
            persistencia.guardarCarreras(carreras);
            persistencia.guardarRamos(ramos);
        }
    }

    // Métodos para manejar notas
    public void registrarNota(Estudiante estudiante, Ramo ramo, Double nota) {
        if (estudiantes.contains(estudiante) && ramos.contains(ramo)) {
            ramo.setNota(estudiante, nota);
            persistencia.guardarRamos(ramos);
        }
    }

    public Double obtenerNota(Estudiante estudiante, Ramo ramo) {
        if (estudiantes.contains(estudiante) && ramos.contains(ramo)) {
            return ramo.getNota(estudiante);
        }
        return null;
    }

    public double calcularPromedio(Estudiante estudiante) {
        List<Ramo> ramosEstudiante = obtenerRamosEstudiante(estudiante);
        if (ramosEstudiante.isEmpty()) {
            return 0.0;
        }

        double suma = 0.0;
        int count = 0;

        for (Ramo ramo : ramosEstudiante) {
            Double nota = ramo.getNota(estudiante);
            if (nota != null) {
                suma += nota;
                count++;
            }
        }

        return count > 0 ? suma / count : 0.0;
    }

    public double calcularPromedioCarrera(Estudiante estudiante, Carrera carrera) {
        List<Ramo> ramosCarrera = obtenerRamosEstudiantePorCarrera(estudiante, carrera);
        if (ramosCarrera.isEmpty()) {
            return 0.0;
        }

        double suma = 0.0;
        int count = 0;

        for (Ramo ramo : ramosCarrera) {
            Double nota = ramo.getNota(estudiante);
            if (nota != null) {
                suma += nota;
                count++;
            }
        }

        return count > 0 ? suma / count : 0.0;
    }

    public boolean estaAprobado(Estudiante estudiante, Ramo ramo) {
        Double nota = obtenerNota(estudiante, ramo);
        return nota != null && nota >= NOTA_APROBACION;
    }

    // Métodos de búsqueda
    public Estudiante buscarEstudiantePorRut(String rut) {
        return estudiantes.stream()
                .filter(e -> e.getRut().equals(rut))
                .findFirst()
                .orElse(null);
    }

    public List<Ramo> obtenerRamosEstudiante(Estudiante estudiante) {
        List<Ramo> ramosEstudiante = new ArrayList<>();
        estudiante.getCarreras().forEach(carrera -> 
            ramosEstudiante.addAll(carrera.getRamos()));
        return ramosEstudiante;
    }

    public List<Ramo> obtenerRamosEstudiantePorCarrera(Estudiante estudiante, Carrera carrera) {
        if (estudiante.getCarreras().contains(carrera)) {
            return carrera.getRamos().stream()
                    .filter(ramo -> ramo.getEstudiantes().contains(estudiante))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    // Métodos para obtener IDs
    public int getNextEstudianteId() {
        return estudiantes.size() + 1;
    }

    public int getNextCarreraId() {
        return carreras.size() + 1;
    }

    public int getNextRamoId() {
        return ramos.size() + 1;
    }

    // Métodos para obtener listas
    public List<Carrera> getCarrerasDisponibles() {
        return new ArrayList<>(carreras);
    }

    public List<Ramo> getRamosDisponiblesParaCarrera(Carrera carrera) {
        return carrera.getRamos();
    }

    // Método para asociar ramo a carrera
    public void asociarRamoACarrera(Ramo ramo, Carrera carrera) {
        if (carreras.contains(carrera) && ramos.contains(ramo)) {
            carrera.agregarRamo(ramo);
            persistencia.guardarCarreras(carreras);
        }
    }

    // Estadísticas
    public int contarRamosAprobados(Estudiante estudiante) {
        return (int) obtenerRamosEstudiante(estudiante).stream()
                .filter(ramo -> estaAprobado(estudiante, ramo))
                .count();
    }

    public int contarRamosReprobados(Estudiante estudiante) {
        return (int) obtenerRamosEstudiante(estudiante).stream()
                .filter(ramo -> {
                    Double nota = obtenerNota(estudiante, ramo);
                    return nota != null && nota < NOTA_APROBACION;
                })
                .count();
    }

    // Método para guardar todos los datos
    public void guardarDatos() {
        persistencia.guardarEstudiantes(estudiantes);
        persistencia.guardarCarreras(carreras);
        persistencia.guardarRamos(ramos);
    }
}