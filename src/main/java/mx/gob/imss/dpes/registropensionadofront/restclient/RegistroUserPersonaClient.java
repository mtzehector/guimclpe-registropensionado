/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.LogginRequest;
import mx.gob.imss.dpes.registropensionadofront.model.PerfilPersonaModel;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroPersona;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.util.ExceptionMapper;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author edgar.arenas
 */
@Path("/persona")
@RegisterRestClient
//@RegisterProvider(value = ExceptionMapper.class,priority = 50)
public interface RegistroUserPersonaClient {
    
  @POST
  @Path("/validate-persona")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Integer validarUsuarioPersona(RegistroRequest request);
      
  
  
     
}
