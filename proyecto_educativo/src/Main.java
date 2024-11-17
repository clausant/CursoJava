// Archivo: src/Main.java
import modelos.*;
import sistema.SistemaGestionEducativa;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SistemaGestionEducativa sistema = new SistemaGestionEducativa();
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final double NOTA_APROBACION = 4.0;

    public static void main(String[] args) {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    registrarEstudiante();
                    break;
                case 2:
                    mostrarEstudiante();
                    break;
                case 3:
                    registrarCarrera();
                    break;
                case 4:
                    matricularEstudianteEnCarrera();
                    break;
                case 5:
                    registrarRamo();
                    break;
                case 6:
                    inscribirRamo();
                    break;
                case 7:
                    ingresarNotas();
                    break;
                case 8:
                    mostrarCalificaciones();
                    break;
                case 9:
                    System.out.println("Guardando datos y saliendo del sistema...");
                    sistema.guardarDatos();
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE GESTIÓN EDUCATIVA ===");
        System.out.println("1. Registrar nuevo estudiante");
        System.out.println("2. Mostrar información de estudiante");
        System.out.println("3. Registrar nueva carrera");
        System.out.println("4. Matricular estudiante en carrera");
        System.out.println("5. Registrar nuevo ramo");
        System.out.println("6. Inscribir ramo a estudiante");
        System.out.println("7. Ingresar notas");
        System.out.println("8. Mostrar calificaciones y estado");
        System.out.println("9. Salir");
        System.out.println("====================================");
    }

    private static void registrarEstudiante() {
        System.out.println("\n=== REGISTRO DE ESTUDIANTE ===");
        System.out.println("Ingrese los datos del estudiante:");

        System.out.print("Nombres: ");
        String nombres = scanner.nextLine();

        System.out.print("Apellido Paterno: ");
        String apellidoPaterno = scanner.nextLine();

        System.out.print("Apellido Materno: ");
        String apellidoMaterno = scanner.nextLine();

        System.out.print("RUT (formato: 12345678-9): ");
        String rut = scanner.nextLine();

        LocalDate fechaNacimiento = null;
        while (fechaNacimiento == null) {
            System.out.print("Fecha de Nacimiento (dd/MM/yyyy): ");
            try {
                fechaNacimiento = LocalDate.parse(scanner.nextLine(), dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Use dd/MM/yyyy");
            }
        }

        System.out.println("\nDirección del estudiante:");
        System.out.print("Calle: ");
        String calle = scanner.nextLine();

        System.out.print("Número: ");
        String numero = scanner.nextLine();

        System.out.print("Comuna: ");
        String comuna = scanner.nextLine();

        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine();

        System.out.print("País: ");
        String pais = scanner.nextLine();

        Direccion direccion = new Direccion(calle, numero, comuna, ciudad, pais);
        Estudiante estudiante = new Estudiante(
            sistema.getNextEstudianteId(),
            nombres, 
            apellidoPaterno,
            apellidoMaterno,
            rut,
            fechaNacimiento,
            direccion
        );

        sistema.agregarEstudiante(estudiante);
        System.out.println("\nEstudiante registrado exitosamente!");
    }

    private static void mostrarEstudiante() {
        System.out.print("\nIngrese el RUT del estudiante: ");
        String rut = scanner.nextLine();

        Estudiante estudiante = sistema.buscarEstudiantePorRut(rut);
        if (estudiante != null) {
            System.out.println("\n=== INFORMACIÓN DEL ESTUDIANTE ===");
            System.out.println(estudiante);

            List<Carrera> carreras = estudiante.getCarreras();
            if (!carreras.isEmpty()) {
                System.out.println("\nCarreras en curso:");
                for (Carrera carrera : carreras) {
                    System.out.println("- " + carrera.getNombre());
                    double promedio = sistema.calcularPromedioCarrera(estudiante, carrera);
                    if (promedio > 0) {
                        System.out.printf("  Promedio: %.1f\n", promedio);
                    }
                }
            } else {
                System.out.println("\nNo está matriculado en ninguna carrera.");
            }
        } else {
            System.out.println("Estudiante no encontrado.");
        }
    }

    private static void registrarCarrera() {
        System.out.println("\n=== REGISTRO DE CARRERA ===");

        int id = sistema.getNextCarreraId();
        System.out.print("Nombre de la carrera: ");
        String nombre = scanner.nextLine();

        System.out.print("Código de la carrera: ");
        String codigo = scanner.nextLine();

        Carrera carrera = new Carrera(id, nombre, codigo);
        sistema.agregarCarrera(carrera);
        System.out.println("Carrera registrada exitosamente!");
    }

    private static void matricularEstudianteEnCarrera() {
        System.out.println("\n=== MATRICULAR ESTUDIANTE EN CARRERA ===");

        System.out.print("Ingrese el RUT del estudiante: ");
        String rut = scanner.nextLine();

        Estudiante estudiante = sistema.buscarEstudiantePorRut(rut);
        if (estudiante == null) {
            System.out.println("Estudiante no encontrado.");
            return;
        }

        List<Carrera> carrerasDisponibles = sistema.getCarrerasDisponibles();
        if (carrerasDisponibles.isEmpty()) {
            System.out.println("No hay carreras disponibles.");
            return;
        }

        System.out.println("\nCarreras disponibles:");
        for (int i = 0; i < carrerasDisponibles.size(); i++) {
            System.out.println((i + 1) + ". " + carrerasDisponibles.get(i).getNombre());
        }

        int indiceCarrera = leerEntero("Seleccione el número de la carrera: ") - 1;
        if (indiceCarrera >= 0 && indiceCarrera < carrerasDisponibles.size()) {
            try {
                sistema.matricularEstudianteEnCarrera(estudiante, carrerasDisponibles.get(indiceCarrera));
                System.out.println("Estudiante matriculado exitosamente!");
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Opción no válida.");
        }
    }

    private static void registrarRamo() {
        System.out.println("\n=== REGISTRO DE RAMO ===");

        int id = sistema.getNextRamoId();
        System.out.print("Nombre del ramo: ");
        String nombre = scanner.nextLine();

        System.out.print("Código del ramo: ");
        String codigo = scanner.nextLine();

        System.out.print("Periodo (ej: 2024-1): ");
        String periodo = scanner.nextLine();

        System.out.println("\nCarreras disponibles:");
        List<Carrera> carreras = sistema.getCarrerasDisponibles();
        for (int i = 0; i < carreras.size(); i++) {
            System.out.println((i + 1) + ". " + carreras.get(i).getNombre());
        }

        int indiceCarrera = leerEntero("Seleccione la carrera a la que pertenece el ramo: ") - 1;
        if (indiceCarrera >= 0 && indiceCarrera < carreras.size()) {
            Ramo ramo = new Ramo(id, nombre, codigo, periodo);
            sistema.agregarRamo(ramo);
            sistema.asociarRamoACarrera(ramo, carreras.get(indiceCarrera));
            System.out.println("Ramo registrado exitosamente!");
        } else {
            System.out.println("Carrera no válida.");
        }
    }

    private static void inscribirRamo() {
        System.out.println("\n=== INSCRIPCIÓN DE RAMO ===");

        System.out.print("Ingrese el RUT del estudiante: ");
        String rut = scanner.nextLine();

        Estudiante estudiante = sistema.buscarEstudiantePorRut(rut);
        if (estudiante == null) {
            System.out.println("Estudiante no encontrado.");
            return;
        }

        List<Carrera> carrerasEstudiante = estudiante.getCarreras();
        if (carrerasEstudiante.isEmpty()) {
            System.out.println("El estudiante no está matriculado en ninguna carrera.");
            return;
        }

        System.out.println("\nCarreras del estudiante:");
        for (int i = 0; i < carrerasEstudiante.size(); i++) {
            System.out.println((i + 1) + ". " + carrerasEstudiante.get(i).getNombre());
        }

        int indiceCarrera = leerEntero("Seleccione la carrera: ") - 1;
        if (indiceCarrera >= 0 && indiceCarrera < carrerasEstudiante.size()) {
            Carrera carrera = carrerasEstudiante.get(indiceCarrera);
            List<Ramo> ramosDisponibles = sistema.getRamosDisponiblesParaCarrera(carrera);

            if (ramosDisponibles.isEmpty()) {
                System.out.println("No hay ramos disponibles para esta carrera.");
                return;
            }

            System.out.println("\nRamos disponibles:");
            for (int i = 0; i < ramosDisponibles.size(); i++) {
                System.out.println((i + 1) + ". " + ramosDisponibles.get(i).getNombre());
            }

            int indiceRamo = leerEntero("Seleccione el ramo a inscribir: ") - 1;
            if (indiceRamo >= 0 && indiceRamo < ramosDisponibles.size()) {
                sistema.inscribirRamo(estudiante, carrera, ramosDisponibles.get(indiceRamo));
                System.out.println("Ramo inscrito exitosamente!");
            } else {
                System.out.println("Ramo no válido.");
            }
        } else {
            System.out.println("Carrera no válida.");
        }
    }

    private static void ingresarNotas() {
        System.out.println("\n=== INGRESO DE NOTAS ===");

        System.out.print("Ingrese el RUT del estudiante: ");
        String rut = scanner.nextLine();

        Estudiante estudiante = sistema.buscarEstudiantePorRut(rut);
        if (estudiante == null) {
            System.out.println("Estudiante no encontrado.");
            return;
        }

        List<Ramo> ramosEstudiante = sistema.obtenerRamosEstudiante(estudiante);
        if (ramosEstudiante.isEmpty()) {
            System.out.println("El estudiante no tiene ramos inscritos.");
            return;
        }

        System.out.println("\nRamos del estudiante:");
        for (int i = 0; i < ramosEstudiante.size(); i++) {
            Ramo ramo = ramosEstudiante.get(i);
            System.out.println((i + 1) + ". " + ramo.getNombre() + 
                (ramo.getNota(estudiante) != null ? " (Nota actual: " + ramo.getNota(estudiante) + ")" : " (Sin nota)"));
        }

        int indiceRamo = leerEntero("Seleccione el ramo para ingresar nota: ") - 1;
        if (indiceRamo >= 0 && indiceRamo < ramosEstudiante.size()) {
            double nota = leerNota("Ingrese la nota (1.0 - 7.0): ");
            sistema.registrarNota(estudiante, ramosEstudiante.get(indiceRamo), nota);
            System.out.println("Nota registrada exitosamente!");
        } else {
            System.out.println("Ramo no válido.");
        }
    }

    private static void mostrarCalificaciones() {
        System.out.println("\n=== CALIFICACIONES Y ESTADO ===");

        System.out.print("Ingrese el RUT del estudiante: ");
        String rut = scanner.nextLine();

        Estudiante estudiante = sistema.buscarEstudiantePorRut(rut);
        if (estudiante == null) {
            System.out.println("Estudiante no encontrado.");
            return;
        }

        System.out.println("\nEstudiante: " + estudiante.getNombreCompleto());
        List<Carrera> carreras = estudiante.getCarreras();

        for (Carrera carrera : carreras) {
            System.out.println("\nCarrera: " + carrera.getNombre());
            List<Ramo> ramosCarrera = sistema.obtenerRamosEstudiantePorCarrera(estudiante, carrera);

            if (!ramosCarrera.isEmpty()) {
                double sumaNotas = 0;
                int cantidadNotas = 0;
                int ramosAprobados = 0;
                int ramosReprobados = 0;

                System.out.println("Ramos cursados:");
                for (Ramo ramo : ramosCarrera) {
                    System.out.printf("- %s: ", ramo.getNombre());
                    Double nota = ramo.getNota(estudiante);
                    if (nota != null) {
                        System.out.printf("%.1f ", nota);
                        if (nota >= NOTA_APROBACION) {
                            System.out.println("(Aprobado)");
                            ramosAprobados++;
                        } else {
                            System.out.println("(Reprobado)");

                            System.out.println("(Reprobado)");
                                                        ramosReprobados++;
                                                    }
                                                    sumaNotas += nota;
                                                    cantidadNotas++;
                                                } else {
                                                    System.out.println("Sin nota");
                                                }
                                            }

                                            if (cantidadNotas > 0) {
                                                double promedio = sumaNotas / cantidadNotas;
                                                System.out.printf("\nPromedio: %.1f\n", promedio);
                                                System.out.println("Ramos aprobados: " + ramosAprobados);
                                                System.out.println("Ramos reprobados: " + ramosReprobados);
                                                System.out.println("Estado general: " + 
                                                    (promedio >= NOTA_APROBACION ? "Aprobado" : "Reprobado"));
                                            } else {
                                                System.out.println("\nNo hay notas registradas para esta carrera.");
                                            }
                                        } else {
                                            System.out.println("No hay ramos inscritos en esta carrera.");
                                        }
                                    }
                                }

                                private static int leerEntero(String mensaje) {
                                    while (true) {
                                        try {
                                            System.out.print(mensaje);
                                            return Integer.parseInt(scanner.nextLine());
                                        } catch (NumberFormatException e) {
                                            System.out.println("Por favor, ingrese un número válido.");
                                        }
                                    }
                                }

                                private static double leerNota(String mensaje) {
                                    while (true) {
                                        try {
                                            System.out.print(mensaje);
                                            double nota = Double.parseDouble(scanner.nextLine());
                                            if (nota >= 1.0 && nota <= 7.0) {
                                                return nota;
                                            } else {
                                                System.out.println("La nota debe estar entre 1.0 y 7.0");
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Por favor, ingrese un número válido.");
                                        }
                                    }
                                }
                            }