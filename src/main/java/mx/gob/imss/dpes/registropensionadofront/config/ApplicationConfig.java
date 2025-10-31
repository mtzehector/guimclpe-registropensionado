/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.config;

import java.util.Set;
import javax.ws.rs.core.Application;
import mx.gob.imss.dpes.registropensionadofront.service.RecuperarPasswordService;

/**
 *
 * @author eduardo.montesh
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.endpoint.ActivarRegistroEndPoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.endpoint.LogginEndPoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.endpoint.RecuperarPasswordEndPoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.endpoint.RegistroFrontEndpoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.endpoint.ValidarCandidatoOperadorFrontEndpoint.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.rule.CalculoVigenciaToken.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.rule.CompararFechas.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.rule.GenerarToken.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.ActivaRegistroService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.ActualizaPassword.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.ComparaBDTU.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.EnviaCambioPasswordService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.EnviaConfirmacionRegistroUsuarioService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.EnviaRecuperacionPasswordService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.EnviaTokenPorCorreoService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.LogginService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.ObtenDatosBDTUService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.ObtenDatosRenapo.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.ObtenInfoTokenService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.RecuperarPasswordService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.RegistrarTokenUsuarioService.class);
        resources.add(mx.gob.imss.dpes.registropensionadofront.service.ValidarUsuarioService.class);

    }

}
