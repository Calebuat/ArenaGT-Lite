import java.time.LocalDate;
import java.util.List;

public abstract class Encuentro implements Programable, Puntuable {
    private final int id;
    private final Equipo equipo1;
    private final Equipo equipo2;
    private int marcador1;
    private int marcador2;
    private LocalDate fecha;
    private int hora;
    private String sede;
    private String estado;

    protected Encuentro(int id, Equipo equipo1, Equipo equipo2) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser positivo.");
        }

        if (equipo1 == null || equipo2 == null) {
            throw new IllegalArgumentException(
                    "Los dos equipos son obligatorios.");
        }

        if (equipo1.getCodigo().equals(equipo2.getCodigo())) {
            throw new IllegalArgumentException(
                    "Un equipo no puede jugar contra sí mismo.");
        }

        this.id = id;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;

        marcador1 = -1;
        marcador2 = -1;
        fecha = null;
        hora = -1;
        sede = "";
        estado = "SIN_PROGRAMAR";
    }

    @Override
    public final int getId() {
        return id;
    }

    public final Equipo getEquipo1() {
        return equipo1;
    }

    public final Equipo getEquipo2() {
        return equipo2;
    }

    public final int getMarcador1() {
        return marcador1;
    }

    public final int getMarcador2() {
        return marcador2;
    }

    @Override
    public final LocalDate getFecha() {
        return fecha;
    }

    @Override
    public final int getHora() {
        return hora;
    }

    @Override
    public final String getSede() {
        return sede;
    }

    @Override
    public final String getEstado() {
        return estado;
    }

    @Override
    public final List<Equipo> getEquipos() {
        return List.of(equipo1, equipo2);
    }

    @Override
    public final void programar(
            LocalDate fecha, int hora, String sede) {

        validarProgramacion(fecha, hora, sede);

        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
        estado = "PROGRAMADO";
    }

    @Override
    public final void registrarResultado(
            int marcador1, int marcador2) {

        if (!"PROGRAMADO".equals(estado)) {
            throw new IllegalStateException(
                    "Solo se acepta resultado de un encuentro PROGRAMADO.");
        }

        if (marcador1 < 0 || marcador2 < 0) {
            throw new IllegalArgumentException(
                    "Los marcadores no pueden ser negativos.");
        }

        validarReglasDeporte(marcador1, marcador2);

        int puntos1 = puntosPorMarcador(marcador1, marcador2);
        int puntos2 = puntosPorMarcador(marcador2, marcador1);

        if (puntos1 < 0 || puntos2 < 0
                || (long) equipo1.getPuntosTabla()
                        + puntos1 > Integer.MAX_VALUE
                || (long) equipo2.getPuntosTabla()
                        + puntos2 > Integer.MAX_VALUE) {

            throw new IllegalArgumentException(
                    "Los puntos calculados están fuera de rango.");
        }

        // Todos los datos se validaron antes de modificar el modelo.
        equipo1.sumarPuntos(puntos1);
        equipo2.sumarPuntos(puntos2);

        this.marcador1 = marcador1;
        this.marcador2 = marcador2;
        estado = "FINALIZADO";
    }

    protected abstract void validarReglasDeporte(
            int marcador1, int marcador2);

    protected abstract int puntosPorMarcador(
            int propio, int rival);

    @Override
    public abstract String getDescripcion();
}
