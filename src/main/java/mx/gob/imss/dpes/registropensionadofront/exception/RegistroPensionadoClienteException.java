package mx.gob.imss.dpes.registropensionadofront.exception;

public class RegistroPensionadoClienteException extends LocalBusinessException {
    public final static String REGISTRO_PENSIONADO_BACK_REGISTRO_PENSIONADO_PERSONA_EMAIL = "msg364";
    public final static String REGISTRO_PENSIONADO_BACK_REGISTRO_EMAIL_EMAIL = "msg366";
    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "msg368";

    public RegistroPensionadoClienteException(String causa) {
        super(causa);
    }
}
