package mx.gob.imss.dpes.registropensionadofront.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RegisterRestClient
@Path("/solicitud")
public interface SolicitudBackCliente {
    
    @GET
    @Path("/conteo/{curp}")
    @Produces(MediaType.APPLICATION_JSON)
    public Integer obtenerConteoDeSolicitudesPorCURPPensionado(@PathParam("curp") String curp);

}
