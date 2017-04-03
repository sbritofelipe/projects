package ie.samm.crawler.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.Category;
import ie.samm.crawler.model.enumetaror.WebsiteEnum;
import ie.samm.crawler.model.util.Constants;
import ie.samm.crawler.service.CrawlerService;

@Controller
@ViewScoped
public class CrawlerController extends AbstractController{

	@Autowired
	private CrawlerService crawlerService;
	
	private String url;
	
	private List<Business> businesses;
	
	private HashSet<Category> categories;
	
	private HashSet<Category> subcategories;
	
	private Category category;
	
	private Category subcategory;
	
	public void initSearch(){
		try {
			initLists();
			if(!url.contains(Constants.HTTPS)){
				url = Constants.HTTPS + url; 
			}
			if (WebsiteEnum.GOLDEN_PAGES.getAddress().equals(url)) {
				this.setCategories(new HashSet<Category>());
				this.getCategories().addAll(crawlerService.findCategories(url, false));
			}
			addSuccessMessage(getCategories().size() + " categories found.");
		} catch (IOException e) {
			addErrorMessage("Error.");
		}
	}

	private void initLists() {
		this.businesses = new ArrayList<Business>();
		this.categories = new HashSet<Category>();
		this.subcategories = new HashSet<Category>();
	}
	
	public void initSubcategories(){
		try {
			this.subcategories = getCategory() != null? crawlerService.findCategories(getCategory().getUrl(), true) : null;
		} catch (IOException e) {
			addErrorMessage("Error.");
		}
	}
	
	public void search(){
		try {
			this.businesses = new ArrayList<Business>();
			HashSet<Category> categories = new HashSet<Category>();
			if(this.category != null){
				if(this.subcategory != null){
					this.category.setSubcategories(new HashSet<Category>());
					this.category.getSubcategories().add(this.subcategory);
				}
				categories.add(this.category);
			}else{
				categories.addAll(categories);
			}
			this.businesses.addAll(crawlerService.searchBusinessInfo(categories));
			addSuccessMessage(businesses.isEmpty()? "No businesses found." : this.businesses.size() + " businesses found.");
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
	 * @return the subcategories
	 */
	public HashSet<Category> getSubcategories() {
		return subcategories;
	}

	/**
	 * @param subcategories the subcategories to set
	 */
	public void setSubcategories(HashSet<Category> subcategories) {
		this.subcategories = subcategories;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the subcategory
	 */
	public Category getSubcategory() {
		return subcategory;
	}

	/**
	 * @param subcategory the subcategory to set
	 */
	public void setSubcategory(Category subcategory) {
		this.subcategory = subcategory;
	}
	
}