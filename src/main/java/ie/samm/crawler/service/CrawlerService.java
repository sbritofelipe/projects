package ie.samm.crawler.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import ie.samm.crawler.model.Business;

public interface CrawlerService {
	
	HashSet<Business> searchBusinessInfo(String url) throws IOException;

	Map<? extends String, ? extends String> searchCategories(String url) throws IOException;

	Map<? extends String, ? extends String> searchSubcategories(LinkedHashMap<String, String> categories) throws IOException;

}
