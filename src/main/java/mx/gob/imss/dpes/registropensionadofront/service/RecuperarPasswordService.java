/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.exception.RegistroPensionadoClienteException;
import mx.gob.imss.dpes.registropensionadofront.exception.SolicitudBackClienteException;
import mx.gob.imss.dpes.registropensionadofront.exception.UsrNotFoundException;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroPersona;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.TokenRegistroUsusario;
import mx.gob.imss.dpes.registropensionadofront.restclient.RegistroPensionadoClient;
import mx.gob.imss.dpes.registropensionadofront.restclient.SolicitudBackCliente;
import mx.gob.imss.dpes.registropensionadofront.rule.CalculoVigenciaToken;
import mx.gob.imss.dpes.registropensionadofront.rule.GenerarToken;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class RecuperarPasswordService extends ServiceDefinition<RegistroRequest, RegistroRequest> {

    @Inject
    @RestClient
    private RegistroPensionadoClient client;

    @Inject
    @RestClient
    private SolicitudBackCliente solicitudBackCliente;

    @Inject
    private CalculoVigenciaToken rule1;

    @Inject
    private GenerarToken rule2;

    private RegistroPersona obtienePersonaPorCorreo(String correo) throws BusinessException {
        try {
            Response respuestaPersona = client.obtienePersonaPorCorreo(correo);

            if (respuestaPersona != null && respuestaPersona.getStatus() == 200)
                return respuestaPersona.readEntity(RegistroPersona.class);

            return null;
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "RecuperarPasswordService.obtienePersonaPorCorreo - correo = [" + correo + "]", e);
            throw new RegistroPensionadoClienteException(
                    RegistroPensionadoClienteException.REGISTRO_PENSIONADO_BACK_REGISTRO_PENSIONADO_PERSONA_EMAIL);
        }
    }

    private Integer obtenerConteoDeSolicitudesPorCURPPensionado(String curp) throws BusinessException {
        try {
            return solicitudBackCliente.obtenerConteoDeSolicitudesPorCURPPensionado(curp);
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "RecuperarPasswordService.obtenerConteoDeSolicitudesPorCURPPensionado - " +
                            "curp = [" + curp + "]", e);
            throw new SolicitudBackClienteException(
                    SolicitudBackClienteException.SOLICITUD_BACK_SOLICITUD_CONTEO_CURP);
        }
    }

    private Boolean persistePensionadoPorEmail(String correo) throws BusinessException {
        try {
            Response respuestaPersona = client.persistePensionadoPorEmail(correo);

            if (respuestaPersona != null && respuestaPersona.getStatus() == 200)
                return Boolean.TRUE;

            return Boolean.FALSE;
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "RecuperarPasswordService.persistePensionadoPorEmail - correo = [" + correo + "]", e);
            throw new RegistroPensionadoClienteException(
                    RegistroPensionadoClienteException.REGISTRO_PENSIONADO_BACK_REGISTRO_EMAIL_EMAIL);
        }
    }

    private Integer registrarPensionadoPorEmail(String correo) throws BusinessException {
        try {
            RegistroPersona persona = obtienePersonaPorCorreo(correo);
            if(!(persona != null && persona.getCveCurp() != null))
                return 0;

            if (obtenerConteoDeSolicitudesPorCURPPensionado(persona.getCveCurp()) == 0)
                return 0;

            if (persistePensionadoPorEmail(correo)) {
                return 1;
            }

            return 0;
        } catch(BusinessException e) {
            throw e;
        } catch(Exception e) {
            log.log(Level.SEVERE,
                    "RecuperarPasswordService.registrarPensionadoPorEmail - correo = [" + correo + "]", e);
            throw new UsrNotFoundException(
                    UsrNotFoundException.ERROR_AL_REGISTRAR_PENSIONADO_POR_EMAIL);
        }
    }

    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {

        log.log(Level.INFO, ">>>>>>>FRONT Valida Usuario Recupera Pass : {0}", request.getPayload());
        Integer validaUsuario = 0;
        try {
            validaUsuario = client.validarUsuario(request.getPayload());
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
            if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                ExceptionUtils.throwServiceException("RegistroPensionadoBack");
            }
            if (e.getMessage().contains("Unknown error, status code 500")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR UsrNotFoundException");
                throw new UsrNotFoundException();
            }
        }

        if(validaUsuario == 0)
            validaUsuario = registrarPensionadoPorEmail(request.getPayload().getCorreo());

        log.log(Level.INFO, ">>>>>>>FRONT Valida Usuario validaUsuario={0}", validaUsuario);

        if (validaUsuario == 1) {

            rule1.apply(request.getPayload());
            rule2.apply(request.getPayload());
            log.log(Level.INFO, ">>>>>>>REQUEST DESPUES DE APLICAR RULES : {0}", request);
            return new Message<>(request.getPayload());

        } else {
            Boolean tieneToken = false;

            try {
                log.log(Level.INFO, "CorreoTokenVencido request={0}", request);

                //TokenRegistroUsusario rq = new TokenRegistroUsusario();
                //rq.setCorreo(request.getPayload().getCorreo());
                //Response response = client.obtenerTokenCreacionPorCorreo(rq);
                Response response = client.obtenerTokenCreacionPorCorreo(request.getPayload().getCorreo());

                log.log(Level.INFO, "Response correoTokenVencido Status: {0}", response.getStatus());

                if (response.getStatus() == 200) {
                    TokenRegistroUsusario tk = response.readEntity(TokenRegistroUsusario.class);
                    log.log(Level.INFO, "CorreoTokenVencido tk: {0}", tk);
                    request.getPayload().setTokenRegistroUsuario(tk);
                    tieneToken = true;
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, ">>>>>>> correoTokenVencido  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
                if (e.getMessage() != null) {
                    if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                        log.log(Level.SEVERE, ">>>>>>> correoTokenVencido !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                        ExceptionUtils.throwServiceException("RegistroPensionadoBack");
                    }
                    if (e.getMessage().contains("Unknown error, status code 406")) {
                        log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR correoTokenVencidoException");
                    }
                }
            }
            log.log(Level.INFO, "CorreoTokenVencido tieneToken: {0}", tieneToken);

            if (tieneToken) {
                return new Message<>(request.getPayload());
            } else {
                throw new UsrNotFoundException();
            }
        }
    }

}
