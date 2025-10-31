/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.model;

import javax.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author edgar.arenas
 */
@Data
public class UsuarioResponse extends BaseModel{
    
    private Long id;

 
    private String nomUsuario;
      
 
    private String password;
        

    private Integer indActivo;
         
    private RegistroPersona persona;
    private PerfilPersonaModel perfilPersona; 
}
