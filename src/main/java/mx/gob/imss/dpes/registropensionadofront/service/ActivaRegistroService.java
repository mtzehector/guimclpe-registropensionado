/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.registropensionadofront.assembler.RegistroPersonaAssembler;
import mx.gob.imss.dpes.registropensionadofront.exception.InvalidTokenException;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroPersona;
import mx.gob.imss.dpes.registropensionadofront.restclient.RegistroPensionadoClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.montesh
 *
 * Guarda en la base de datos al usuario. Tabla Persona
 */
@Provider
public class ActivaRegistroService extends ServiceDefinition<ActivarRegistroRequest, ActivarRegistroRequest> {

    @Inject
    @RestClient
    RegistroPensionadoClient registroClient;

    @Override
    public Message<ActivarRegistroRequest> execute(Message<ActivarRegistroRequest> request) throws BusinessException {
        try {
            //log.log(Level.INFO, ">>>>>>ActivaRegistroService FRONT activaRegistroPensionado", request.getPayload());
            RegistroPersonaAssembler assembler = new RegistroPersonaAssembler();
            RegistroPersona persona = assembler.assemble(request.getPayload());

            //log.log(Level.INFO, ">>>>>>ActivaRegistroService persona={0}", persona);
            persona.setPassword(
                    DigestUtils.sha256Hex(request.getPayload().getPassword())
            );


            Response response = registroClient.persisteUsuario(persona);

            if (response.getStatus() == 200) {
                Persona p = response.readEntity(Persona.class);
                request.getPayload().setPersona(p);

                //log.log(Level.INFO, ">>>>>>ActivaRegistroService p={0}", p);
                return request;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ActivaRegistroService.execute - request=[" + request + "]", e);
        }
        return response(null, ServiceStatusEnum.EXCEPCION,
            new InvalidTokenException(InvalidTokenException.ERROR_EN_REGISTRO_USUARIO), null);
    }

}
