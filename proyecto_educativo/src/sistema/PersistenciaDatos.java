package sistema;

import modelos.*;
import java.io.*;
import java.util.List;

public class PersistenciaDatos {
    private static final String RUTA_BASE = "datos/";

    public PersistenciaDatos() {
        // Crear directorio si no existe
        new File(RUTA_BASE).mkdirs();
    }

    public void guardarEstudiantes(List<Estudiante> estudiantes) {
        guardarObjeto(estudiantes, RUTA_BASE + "estudiantes.dat");
    }

    public void guardarCarreras(List<Carrera> carreras) {
        guardarObjeto(carreras, RUTA_BASE + "carreras.dat");
    }

    public void guardarRamos(List<Ramo> ramos) {
        guardarObjeto(ramos, RUTA_BASE + "ramos.dat");
    }

    @SuppressWarnings("unchecked")
    public List<Estudiante> cargarEstudiantes() {
        return (List<Estudiante>) cargarObjeto(RUTA_BASE + "estudiantes.dat");
    }

    @SuppressWarnings("unchecked")
    public List<Carrera> cargarCarreras() {
        return (List<Carrera>) cargarObjeto(RUTA_BASE + "carreras.dat");
    }

    @SuppressWarnings("unchecked")
    public List<Ramo> cargarRamos() {
        return (List<Ramo>) cargarObjeto(RUTA_BASE + "ramos.dat");
    }

    private void guardarObjeto(Object obj, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(archivo))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Error al guardar en " + archivo + ": " + e.getMessage());
        }
    }

    private Object cargarObjeto(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(archivo))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar " + archivo + ": " + e.getMessage());
            return null;
        }
    }
}