package ie.samm.crawler.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.Category;
import ie.samm.crawler.model.enumetaror.WebsiteEnum;
import ie.samm.crawler.service.impl.CrawlerService;

@ManagedBean(name = "crawlerController")
@ViewScoped
public class CrawlerController extends AbstractController{

	@ManagedProperty("#{crawlerService}")
	private CrawlerService crawlerService;
	
	private String url;
	
	private List<Business> businesses;
	
	private HashSet<Category> categories;
	
	private SelectItem category;
	
	private SelectItem subcategory;
	
	public void initSearch(int page){
		try {
			if (WebsiteEnum.GOLDEN_PAGES.getId().equals(page)) {
				url = WebsiteEnum.GOLDEN_PAGES.getAddress();
				this.setCategories(new HashSet<Category>());
				this.getCategories().addAll(crawlerService.searchCategories(url));
			}
		} catch (IOException e) {
			addErrorMessage("Error.");
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
			addErrorMessage("Error.");
		}
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
	public HashSet<Category> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(HashSet<Category> categories) {
		this.categories = categories;
	}

	/**
	 * @return the crawlerservice
	 */
	public CrawlerService getCrawlerService() {
		return crawlerService;
	}

	/**
	 * @param crawlerService
	 */
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