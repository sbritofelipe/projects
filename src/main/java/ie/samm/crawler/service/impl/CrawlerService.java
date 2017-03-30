package ie.samm.crawler.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.Category;
import ie.samm.crawler.model.util.Constants;
import ie.samm.crawler.service.Crawler;

@Component
public class CrawlerService {


	private Crawler crawler = new Crawler();
	
	/**
	 * Main launching point for the CrawlerController's functionality. Internally it
	 * creates crawler that make an HTTP request and parse the response (the
	 * web page).
	 * 
	 * @param url
	 *            - The starting point of the controller
	 * @throws IOException 
	 */
	public HashSet<Business> searchBusinessInfo(String url) throws IOException {
		LinkedHashMap<String, String> categories = (LinkedHashMap<String, String>) crawler.crawl(url, 0);
		HashSet<Business> businesses = new HashSet<Business>();
		for (Map.Entry<String, String> category : categories.entrySet()) {
			String businessCategory = category.getValue();
			LinkedHashMap<String, String> subCategories = (LinkedHashMap<String, String>) crawler.crawl(category.getKey(), 1);
			for (Map.Entry<String, String> subCategory : subCategories.entrySet()) {
				LinkedHashMap<String, String> companies = (LinkedHashMap<String, String>) crawler.crawl(subCategory.getKey(), 2);
				for (Map.Entry<String, String> company : companies.entrySet()) {
					crawler.crawl(company.getKey(), 3);
					Element element = crawler.getElements().get(0);
					String companyName = element.select("h1.name").text();
					String phone = crawler.searchForPatternInElement(Constants.REGEX_TELEPHONE, element.select("div.phone_contact").first());
					String address = element.select("li.contact-address").text();
					String mobile = element.select("#mobileinfo").first() != null? 
									crawler.searchForPatternInElement(Constants.REGEX_TELEPHONE, element.select("#mobileinfo").first()) : null;
					String email = element.select("#emailinfo").first() != null?
									crawler.searchForPatternInElement(Constants.REGEX_EMAIL, element.select("#emailinfo").first()) : null;
					Business business = new Business(businessCategory, companyName, phone, address, mobile, email);
					
					System.out.println(business.toString());
					businesses.add(business);
//					break;
				}
				break;
			}
			break;
		}
		return businesses;
	}
	
	public HashSet<Category> searchCategories(String url) throws IOException {
		Crawler crawler = new Crawler();
		HashSet<Category> categories = new HashSet<Category>();
		LinkedHashMap<String, String> dataCategories = (LinkedHashMap<String, String>) crawler.crawl(url, 0);
		for (Entry<String, String> dataCat : dataCategories.entrySet()) {
			Category category = new Category(dataCat.getValue(), dataCat.getKey());
			category.setSubcategories(searchSubcategories(category));
			categories.add(category);
		}
		return categories;
	}
	
	public HashSet<Category> searchSubcategories(Category category) throws IOException{
		HashSet<Category> subCategories = new HashSet<Category>();
		LinkedHashMap<String, String> hashMap = (LinkedHashMap<String, String>) crawler.crawl(category.getUrl(), 1);
		for (Entry<String,String> subcat : hashMap.entrySet()) {
			subCategories.add(new Category(subcat.getValue(), subcat.getKey()));
		}
		
		return subCategories;
	}

	/**
	 * @return the crawler
	 */
	public Crawler getCrawler() {
		return crawler;
	}

	/**
	 * @param crawler the crawler to set
	 */
	public void setCrawler(Crawler crawler) {
		this.crawler = crawler;
	}

}