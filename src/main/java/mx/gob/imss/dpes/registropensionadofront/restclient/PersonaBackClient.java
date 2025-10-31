package mx.gob.imss.dpes.registropensionadofront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.personaef.model.PersonaEF;
import mx.gob.imss.dpes.registropensionadofront.model.PersonaUpdateResponse;
import mx.gob.imss.dpes.registropensionadofront.model.PersonalUpdateResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * @author juan.garfias
 */
@RegisterRestClient
@Path("/persona")
public interface PersonaBackClient {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/personaEF")
    public Response load(PersonaEF request);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/email/{email}")
    public Integer validateByEmail(@PathParam("email") String email);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/curp/{curp}")
    public Integer validateByCurp(@PathParam("curp") String curp);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/rp/{rp}/{ef}")
    public Integer validateByRP(@PathParam("rp") String rp,@PathParam("ef") String ef);
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(PersonaUpdateResponse personas);
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updatePersonalEF")
    public Response updatePersonaEF(PersonalUpdateResponse personas);
}
