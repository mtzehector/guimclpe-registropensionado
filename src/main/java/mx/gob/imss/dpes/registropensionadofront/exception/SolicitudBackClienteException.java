package mx.gob.imss.dpes.registropensionadofront.exception;

public class SolicitudBackClienteException extends LocalBusinessException {
    public final static String SOLICITUD_BACK_SOLICITUD_CONTEO_CURP = "msg365";

    public SolicitudBackClienteException(String causa) {
        super(causa);
    }
}
