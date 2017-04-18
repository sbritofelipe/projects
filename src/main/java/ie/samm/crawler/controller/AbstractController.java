package ie.samm.crawler.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public abstract class AbstractController {
	
	@Autowired
	protected MessageSource messageSource;
	
	protected String getMessage(String messageName, String...params){
		return messageSource.getMessage(messageName, params, null);
	}
	
	protected void addSuccessMessage(String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}
	
	protected void addErrorMessage(String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
	}

}
