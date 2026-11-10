import java.time.LocalDate;
import java.util.List;

public interface Programable extends Comparable<Programable> {
    int getId();
    LocalDate getFecha();
    int getHora();
    String getSede();
    String getDescripcion();
    String getEstado();
    List<Equipo> getEquipos();

    void programar(LocalDate fecha, int hora, String sede);

    default void validarProgramacion(LocalDate fecha, int hora, String sede) {
        if ("FINALIZADO".equals(getEstado())) {
            throw new IllegalStateException(
                    "No se puede reprogramar un encuentro finalizado.");
        }

        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }

        if (hora < 0 || hora > 23) {
            throw new IllegalArgumentException(
                    "La hora debe estar entre 0 y 23.");
        }

        if (sede == null || sede.isBlank()) {
            throw new IllegalArgumentException(
                    "La sede no puede estar vacía.");
        }
    }

    @Override
    default int compareTo(Programable otro) {
        if (getFecha() == null || otro.getFecha() == null) {
            if (getFecha() == null && otro.getFecha() == null) {
                return 0;
            }

            return getFecha() == null ? 1 : -1;
        }

        int comparacion = getFecha().compareTo(otro.getFecha());

        if (comparacion != 0) {
            return comparacion;
        }

        comparacion = Integer.compare(getHora(), otro.getHora());

        if (comparacion != 0) {
            return comparacion;
        }

        return getSede().compareToIgnoreCase(otro.getSede());
    }
}