public class FutbolSala extends Encuentro {
    public FutbolSala(int id, Equipo equipo1, Equipo equipo2) {
        super(id, equipo1, equipo2);
    }

    @Override
    protected void validarReglasDeporte(
            int marcador1, int marcador2) {

        // El fútbol sala permite empates.
        // Encuentro ya validó que los marcadores no sean negativos.
    }

    @Override
    protected int puntosPorMarcador(int propio, int rival) {
        if (propio > rival) {
            return 3;
        }

        if (propio == rival) {
            return 1;
        }

        return 0;
    }

    @Override
    public String getDescripcion() {
        String texto = "Fútbol sala: "
                + getEquipo1().getNombre()
                + " vs "
                + getEquipo2().getNombre();

        if ("FINALIZADO".equals(getEstado())) {
            texto += " | Marcador: "
                    + getMarcador1()
                    + " - "
                    + getMarcador2();
        }

        return texto;
    }
}