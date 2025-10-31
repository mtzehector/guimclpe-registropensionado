/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author eduardo.montesh
 */

@Data
public class BDTURequestOut extends BaseModel {
    
  private Long id;
  private Long cveIdPersona;
  private String curp;
  
  private String nombre;
  private String primerApellido;
  private String segundoApellido;
  private String correoElectronico;
  private String telefono;
  private String nss;
  private String fechaNacimiento;
  private Sexo sexo;
}


