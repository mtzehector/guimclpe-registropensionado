package mx.gob.imss.dpes.registropensionadofront.model;

import lombok.Data;

/**
 *
 * @author luisr.rodriguez
 */
@Data
public class PersonaUpdateResponse {
    private Long id;
    private String numEmpleado;
    private String registroPatronal;
    private Long telCelular;
    private String correoElectronico;
    private Long baja;
    private EstadoPersonaEf cveEstadoPersonaEf;
    private Long cveEntidadFinanciera;
    private String rfc;
    
}
