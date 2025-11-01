/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.model;

import lombok.Data;

/**
 *
 * @author eduardo.montesh
 */
@Data
public class RenapoResponse {
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String sexo;
    private String numEntidadReg;
    private String anioReg;
}
