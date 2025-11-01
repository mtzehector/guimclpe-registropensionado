package mx.gob.imss.dpes.registropensionadofront.model;

import lombok.Data;

/**
 *
 * @author luisr.rodriguez
 */
@Data
public class PersonalUpdateResponse {
    private String cveCurp;
    private String numEmpleado;
    private Long cveEntidadFinanciera;
}
