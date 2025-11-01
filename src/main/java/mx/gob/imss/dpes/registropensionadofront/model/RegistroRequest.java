/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.relacionlaboral.model.RelacionLaboralOut;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpRequest;

/**
 *
 * @author eduardo.montesh
 */
@Data
public class RegistroRequest extends BaseModel {
    
    private Long cvePersona;
    @Getter
    @Setter
    private String curp;
    @Getter
    @Setter
    private String nss;
    @Getter
    @Setter
    private Long numTelefono;
    @Getter
    @Setter
    private String correo;
    @Getter
    @Setter
    private String correoConfirmar;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter
    @Setter
    private Date vigenciaToken;
    @Getter
    @Setter
    private String token;
    @Getter
    @Setter
    private RenapoCurpRequest renapoRequest;
    @Getter
    @Setter
    private BDTURequest bdtuRequest;
    @Getter
    @Setter
    private Long cvePerfil;
    @Getter
    @Setter
    private String matriculaTrabajadorImss;
    @Getter
    @Setter
    private String delegacionTrabajadorImss;
    @Getter
    @Setter
    private String rfc;
    @Getter
    @Setter
    private String registroPatronal;
    @Getter
    @Setter
    private String numEmpleado;
    @Getter
    @Setter
    private RelacionLaboralOut relacionLaboral;
    @Getter
    @Setter
    private Integer cveEntidadFinanciera;
    @Getter
    @Setter
    private String otrosDatosJson;
    @Getter
    @Setter    
    private TokenRegistroUsusario tokenRegistroUsuario;
    @Getter
    @Setter
    private Long firmaCartaRecibo = 0L;
    
}
