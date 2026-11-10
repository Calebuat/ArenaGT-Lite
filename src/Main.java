import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Copa copa = cargarDatos();

        try (Scanner entrada = new Scanner(System.in)) {
            Vista vista = new Vista(entrada);
            new Controlador(copa, vista).iniciar();
        }
    }

    public static Copa cargarDatos() {
        Copa copa = new Copa();

        copa.registrarEquipo("F", "Faro", "Empresa Norte");
        copa.registrarEquipo("L", "Luna", "Empresa Norte");
        copa.registrarEquipo("S", "Sol", "Empresa Sur");
        copa.registrarEquipo("N", "Nube", "Empresa Sur");

        Equipo faro = copa.buscarEquipo("F");
        Equipo luna = copa.buscarEquipo("L");
        Equipo sol = copa.buscarEquipo("S");
        Equipo nube = copa.buscarEquipo("N");

        copa.agregarEncuentro(new FutbolSala(1, faro, sol));
        copa.agregarEncuentro(new Baloncesto(2, sol, faro));
        copa.agregarEncuentro(new FutbolSala(3, luna, nube));
        copa.agregarEncuentro(new Baloncesto(4, luna, faro));

        copa.agregarEvento(
                new Exhibicion(5, "Clínica de baloncesto"));

        copa.programarEvento(
                1, LocalDate.of(2026, 11, 10), 9, "Central");

        copa.programarEvento(
                2, LocalDate.of(2026, 11, 11), 14, "Central");

        copa.programarEvento(
                3, LocalDate.of(2026, 11, 12), 9, "Norte");

        copa.programarEvento(
                4, LocalDate.of(2026, 11, 13), 10, "Central");

        copa.programarEvento(
                5, LocalDate.of(2026, 11, 12), 9, "Auditorio");

        copa.registrarResultado(1, 3, 1);
        copa.registrarResultado(2, 78, 74);
        copa.registrarResultado(3, 2, 2);

        return copa;
    }
}