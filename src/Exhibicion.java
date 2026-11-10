import java.time.LocalDate;
import java.util.List;

public class Exhibicion implements Programable {
    private final int id;
    private final String titulo;
    private LocalDate fecha;
    private int hora;
    private String sede;

    public Exhibicion(int id, String titulo) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser positivo.");
        }

        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException(
                    "El título no puede estar vacío.");
        }

        this.id = id;
        this.titulo = titulo.trim();
        fecha = null;
        hora = -1;
        sede = "";
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public int getHora() {
        return hora;
    }

    @Override
    public String getSede() {
        return sede;
    }

    @Override
    public String getDescripcion() {
        return "Exhibición: " + titulo;
    }

    @Override
    public String getEstado() {
        return fecha == null ? "SIN_PROGRAMAR" : "PROGRAMADA";
    }

    @Override
    public List<Equipo> getEquipos() {
        return List.of();
    }

    @Override
    public void programar(
            LocalDate fecha, int hora, String sede) {

        validarProgramacion(fecha, hora, sede);

        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
    }
}