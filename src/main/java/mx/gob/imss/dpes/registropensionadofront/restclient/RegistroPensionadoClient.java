/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.restclient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.LogginRequest;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroPersona;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.TokenRegistroUsusario;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author edgar.arenas
 */
@Path("/registroPensionado")
@RegisterRestClient
//@RegisterProvider(value = ExceptionMapper.class,priority = 50)
public interface RegistroPensionadoClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(RegistroRequest request);

    @POST
    @Path("/usuario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Integer validarUsuario(RegistroRequest request);

    @POST
    @Path("/tokenInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenInfoToken(ActivarRegistroRequest token);

    @POST
    @Path("/registro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response persisteUsuario(RegistroPersona persona);

    @POST
    @Path("/actualizar/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarPassword(ActivarRegistroRequest token);

    @POST
    @Path("/acceder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loggeo(LogginRequest request);

    @GET
    @Path("/tokenCreacion/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTokenCreacionPorCorreo(@PathParam("email") String email);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/registro/{email}")
    public Response persistePensionadoPorEmail(@PathParam("email") String correo);

    @GET
    @Path("/persona/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtienePersonaPorCorreo(@PathParam("email") String correo);
}
