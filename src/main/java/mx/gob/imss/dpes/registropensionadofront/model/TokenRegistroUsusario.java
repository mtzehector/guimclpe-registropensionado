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
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author edgar.arenas
 */
@Data
public class TokenRegistroUsusario extends BaseModel {

    private Long id;
    private String nomUsuario;
    private String token;
    private Integer indActivo;
    private Long numTelefono;
    private String correo;
    private String nss;
    private String curp;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date vigenciaToken;
    private Integer indTokenConfirmado;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date altaRegistro;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date bajaRegistro;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date actualizacionRegistro;
    private Long cvePerfil;
    private String matriculaTrabajadorImss;
    private String delegacionTrabajadorImss;
    private Integer cveEntidadFinanciera;
    private String rfc;
    private String registroPatronal;
    private String numEmpleado;
    private String otrosDatosJson;

}
