package mx.gob.imss.dpes.registropensionadofront.exception;

import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.MessageResolver;
import org.eclipse.microprofile.config.inject.ConfigProperty;


public class LocalBusinessException extends BusinessException {
    //@Inject @ConfigProperty(name = "properties.file", defaultValue = "/errors.properties")
    //String PROPERTIES_FILE;
    String PROPERTIES_FILE = "/errors.properties";
    @Getter @Setter static URL propertiesURL  = null;
    
 
    public LocalBusinessException(String messageKey) {
        setPropertiesURL();
        this.setMessageKey(messageKey);
        
    }

    

    public LocalBusinessException() {
        setPropertiesURL();
        this.setMessageKey("error001");
    }
    
    

    @Override
    public String getMessage() {
        if(propertiesURL == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "ERROR >>> PROPERTIES_FILE ' {0} ' not found", PROPERTIES_FILE);
            return MessageResolver.getMessage(this.getMessageKey(), getParameters());
        }
        else{
            return MessageResolver.getMessage(propertiesURL,this.getMessageKey(),getParameters());
        }
    }
    
        public void setPropertiesURL()  {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String jarName = new java.io.File(this.getClass().getProtectionDomain()
                .getCodeSource().getLocation().getPath())
                .getName();

 Logger.getLogger(this.getClass().getName()).log(Level.INFO, ">>> ---LocalBusinessException setProperties, jarName={0}", jarName);
        try {
            Enumeration<URL> urlsEnum = classLoader.getResources(PROPERTIES_FILE);
            while (urlsEnum.hasMoreElements() && jarName != null && jarName.length() > 0) {
                URL localURL = urlsEnum.nextElement();
                if (localURL.toString().contains(jarName)) {
                    propertiesURL = localURL;
                }
Logger.getLogger(this.getClass().getName()).log(Level.INFO, "      >>>####  LocalBusinessException urls={0}", localURL);

            }
Logger.getLogger(this.getClass().getName()).log(Level.INFO, ">>>||| LocalBusinessException propertiesURL={0}", propertiesURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
