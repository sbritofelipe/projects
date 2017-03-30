package ie.samm.crawler.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class AbstractController {
	
	protected void addSuccessMessage(String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}
	
	protected void addErrorMessage(String msg){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
	}

}
