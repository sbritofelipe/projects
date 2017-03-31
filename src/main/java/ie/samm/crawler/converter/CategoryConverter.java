package ie.samm.crawler.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import ie.samm.crawler.model.Category;

public class CategoryConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null) {  
            return this.getAttributesFrom(component).get(value);
        }
        return null;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		if (value != null && !"".equals(value)) {  
  
            Category entity = (Category) value;  
  
            // adiciona item como atributo do componente  
            this.addAttribute(component, entity);  
  
            if (entity.getUrl() != null) {  
                return String.valueOf(entity.getUrl());  
            }  
        }  
  
        return (String) value;
	}

	protected void addAttribute(UIComponent component, Category cat) {  
        this.getAttributesFrom(component).put(cat.getUrl().toString(), cat);  
    }  
  
    protected Map<String, Object> getAttributesFrom(UIComponent component) {  
        return component.getAttributes();  
    }  

}
