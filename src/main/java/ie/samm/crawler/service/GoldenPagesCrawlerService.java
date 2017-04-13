package ie.samm.crawler.service;

import java.util.HashSet;
import java.util.LinkedHashMap;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.BusinessFilter;
import ie.samm.crawler.model.Category;

public interface GoldenPagesCrawlerService {
	
	HashSet<Business> searchBusinessInfo(BusinessFilter filter, Category category, LinkedHashMap<String, String> companies) throws Exception;

	HashSet<Category> findCategories(String url, boolean subcategory) throws Exception;

	LinkedHashMap<String, String> findLinkCompanies(Category category) throws Exception;
}
