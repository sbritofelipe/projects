package ie.samm.crawler.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.WebsiteEnum;
import ie.samm.crawler.service.impl.CrawlerService;

@ManagedBean(name = "crawlerController")
@ViewScoped
public class CrawlerController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty("#{crawlerService}")
	private CrawlerService crawlerService;
	
	private String url;
	
	private List<Business> businesses;
	
	private LinkedHashMap<String, String> categories;
	
	private LinkedHashMap<String, String> subCategories;
	
	private SelectItem category;
	
	private SelectItem subcategory;
	
	public void initSearch(int page){
		try {
			if (WebsiteEnum.GOLDEN_PAGES.getId().equals(page)) {
				url = WebsiteEnum.GOLDEN_PAGES.getAddress();
				this.categories = new LinkedHashMap<String, String>();
				this.subCategories = new LinkedHashMap<String, String>();
				this.categories.putAll(crawlerService.searchCategories(url));
//				this.subCategories.putAll(crawlerService.searchSubcategories(this.categories));
			}
//			this.businesses = new ArrayList<Business>();
//			this.businesses.addAll(crawlerService.searchBusinessInfo(url));
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(businesses.isEmpty()? "No businesses found." : this.businesses.size() + " businesses found."));
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error."));
		}
	}
	
	public void search(int page){
		try {
			if (WebsiteEnum.GOLDEN_PAGES.getId().equals(page)) {
				url = WebsiteEnum.GOLDEN_PAGES.getAddress();
			}
			this.businesses = new ArrayList<Business>();
			this.businesses.addAll(crawlerService.searchBusinessInfo(url));
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(businesses.isEmpty()? "No businesses found." : this.businesses.size() + " businesses found."));
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error."));
		}
	}

	public List<SelectItem> getCategoriesItems(){
		List<SelectItem> items = new ArrayList<SelectItem>();
		if(getCategories() != null){
			for (Entry<String, String> category : getCategories().entrySet()) {
				items.add(new SelectItem(category.getKey(), category.getValue()));
			}
		}
		return items;
	}
	
	// GETTERS AND SETTERS
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the businesses
	 */
	public List<Business> getBusinesses() {
		return businesses;
	}

	/**
	 * @param businesses the businesses to set
	 */
	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}

	/**
	 * @return the categories
	 */
	public LinkedHashMap<String, String> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(LinkedHashMap<String, String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the subCategories
	 */
	public LinkedHashMap<String, String> getSubCategories() {
		return subCategories;
	}

	/**
	 * @param subCategories the subCategories to set
	 */
	public void setSubCategories(LinkedHashMap<String, String> subCategories) {
		this.subCategories = subCategories;
	}

	public CrawlerService getCrawlerService() {
		return crawlerService;
	}

	public void setCrawlerService(CrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}

	/**
	 * @return the category
	 */
	public SelectItem getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(SelectItem category) {
		this.category = category;
	}

	/**
	 * @return the subcategory
	 */
	public SelectItem getSubcategory() {
		return subcategory;
	}

	/**
	 * @param subcategory the subcategory to set
	 */
	public void setSubcategory(SelectItem subcategory) {
		this.subcategory = subcategory;
	}
	
}