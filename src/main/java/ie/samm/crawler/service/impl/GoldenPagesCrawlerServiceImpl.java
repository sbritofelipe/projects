package ie.samm.crawler.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ie.samm.crawler.model.Business;
import ie.samm.crawler.model.BusinessFilter;
import ie.samm.crawler.model.Category;
import ie.samm.crawler.model.enumetaror.WebsiteEnum;
import ie.samm.crawler.model.util.Constants;
import ie.samm.crawler.service.GoldenPagesCrawlerService;

@Service("crawlerServiceImpl")
public class GoldenPagesCrawlerServiceImpl implements GoldenPagesCrawlerService {

	private static final int NUMBER_OF_RESULTS_PER_PAGE = 20;

	private static final Logger LOGGER = Logger.getLogger(GoldenPagesCrawlerServiceImpl.class.getName());

	/** Html Document parsed from the web page. */
	private Document htmlDocument;

	/** List of links on the page. */
	private List<String> links = new LinkedList<String>();

	private Elements elements = new Elements();

	private Connection connection;

	public GoldenPagesCrawlerServiceImpl() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashSet<Category> findCategories(String url, boolean subcategory) throws Exception {
		this.htmlDocument = getHtmlDocumentFrom(url);// Jsoup.parse(html);
		System.out.println("\n**Visiting** Received web page at " + url);

		Elements linksOnPage = new Elements();
		HashSet objects = new HashSet();

		if (!subcategory) {
			linksOnPage.addAll(this.htmlDocument.body().select("li:contains(Categories)").nextAll().select("a[href]"));
			linksOnPage.addAll(this.htmlDocument.body().select("ul.no-header").select("a[href]"));
			System.out.println(linksOnPage.size() + " categories found.");
		} else {
			linksOnPage = this.htmlDocument.body().select("h2:contains(All categories)").nextAll().select("a[href]");
			System.out.println(linksOnPage.size() + " Subcategories found.");
		}
		for (Element link : linksOnPage) {
			Category cat = new Category(link.text(), link.absUrl("href"));
			objects.add(cat);
		}

		return objects;

	}

	public boolean findBusinessMoreInfoElements(String url) throws IOException {
		this.htmlDocument = getHtmlDocumentFrom(url);
		if (this.htmlDocument != null) {
			elements = new Elements();
			elements.addAll(this.htmlDocument.select("div.col_left"));
			return true;
		} else {
			return false;
		}
	}
	
	private Document getHtmlDocumentFrom(String url) throws IOException, HttpStatusException {
		setConnection(Jsoup.connect(url).userAgent(Constants.USER_AGENT));
		if (getConnection().response().statusCode() == 200) {
			// 200 is the HTTP OK status code
			System.out.println("\n**Visiting** Received web page at " + url);
		}
		if (getConnection().response().statusCode() == 404) {
			return null;
		}
		Document document = null;
		try {
			document = getConnection().get();
		} catch (HttpStatusException e) {
			LOGGER.log(Level.WARNING, "Url " + url + "does not exist.");
		}
		return document;
	}

	/**
	 * 
	 * @throws IOException
	 */
	public HashSet<Business> searchBusinessInfo(BusinessFilter filter, Category subcategory, LinkedHashMap<String, String> linkCompanies) throws Exception {
		HashSet<Business> businesses = new HashSet<Business>();
		if(subcategory != null && !StringUtils.isEmpty(subcategory.getUrl())){
			int toIndex = filter.getFirstResult() + filter.getPageSize() > linkCompanies.size()? linkCompanies.size() : filter.getFirstResult() + filter.getPageSize();
			List<String> sublist = Collections.list(Collections.enumeration(linkCompanies.keySet())).subList(filter.getFirstResult(), toIndex);
			for (String company : sublist) {
				if (findBusinessMoreInfoElements(company)) {
					Business business = populateBusiness(subcategory);
					businesses.add(business);
				}
			}
		}
		return businesses;
	}

	private Business populateBusiness(Category subCategory) {
		Element element = getElements().get(0);
		String companyName = element.select("h1.name").text();
		String phone = element.select("div.phone_contact").first() != null? searchForPatternInElement(Constants.REGEX_TELEPHONE, element.select("div.phone_contact").first()): null;
		String address = element.select("li.contact-address").text();
		String mobile = element.select("#mobileinfo").first() != null? searchForPatternInElement(Constants.REGEX_TELEPHONE, element.select("#mobileinfo").first()) : null;
		String email = element.select("#emailinfo").first() != null? searchForPatternInElement(Constants.REGEX_EMAIL, element.select("#emailinfo").first()) : null;
		String website = element.select("#homepageinfo").first() != null? element.select("#homepageinfo").first().text() : null;
		Business business = new Business(subCategory, companyName, phone, address, mobile, email, website);

		System.out.println(business.toString());
		return business;
	}

	public Set<String> searchForPattern(String pattern) {
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
		}
		System.out.println("Searching for the Pattern...");
		String bodyText = this.htmlDocument.body().text();
		Matcher m = Pattern.compile(pattern).matcher(bodyText.toLowerCase());
		Set<String> words = new HashSet<String>();
		while (m.find()) {
			words.add(m.group());
		}
		return words;
	}

	public String searchForPatternInElement(String pattern, Element element) {
		if (element == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
		}
		String bodyText = element.text();
		Matcher m = Pattern.compile(pattern).matcher(bodyText.toLowerCase());
		if (m.find()) {
			return m.group();
		}
		return null;
	}
	
	@Override
	public LinkedHashMap<String, String> findLinkCompanies(Category category) throws IOException {
		HtmlUnitDriver webDriver = new HtmlUnitDriver();
		webDriver.setJavascriptEnabled(true);
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		webDriver.get(category.getUrl());
		WebDriverWait wait = new WebDriverWait(webDriver, 5);
		this.htmlDocument = Jsoup.parse(webDriver.getPageSource());
		int results = Integer.parseInt(this.htmlDocument.body().select("div#results_header > span").text().replaceAll("[^0-9]", ""));
		int pages = (int) (results > NUMBER_OF_RESULTS_PER_PAGE? Math.ceil(results/NUMBER_OF_RESULTS_PER_PAGE) : 0); 
		for (int i = 0; i < pages; i++) {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='result-next']")));  
			webDriver.findElement(By.id("result-next")).click();
			int resultsOnPage = (i+2) * NUMBER_OF_RESULTS_PER_PAGE > results? results : (i+2) * NUMBER_OF_RESULTS_PER_PAGE;
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='result-item"+ resultsOnPage +"']")));
		}
		String html = webDriver.getPageSource();
		this.htmlDocument = Jsoup.parse(html);
		webDriver.quit();
		if(this.htmlDocument != null){
			Elements linksOnPage = this.htmlDocument.body().select("li[class^=result-item]").select("div.listing_more").select("a[href]").select("a.wt");
			LinkedHashMap<String, String> companies = new LinkedHashMap<>();
			for (int i = 0; i < linksOnPage.size(); i++) {
				Element link = linksOnPage.get(i);
				companies.put(WebsiteEnum.GOLDEN_PAGES.getAddress() + link.attr("href"), link.text());
				if(i + 1 == results){
					break;
				}
			}
			return companies;
		}
		return new LinkedHashMap<>();
	}

	// GETTERS AND SETTERS
	public Document getHtmlDocument() {
		return htmlDocument;
	}

	public void setHtmlDocument(Document htmlDocument) {
		this.htmlDocument = htmlDocument;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public Elements getElements() {
		return elements;
	}

	public void setElements(Elements elements) {
		this.elements = elements;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}