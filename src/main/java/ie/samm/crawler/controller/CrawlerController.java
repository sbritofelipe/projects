package ie.samm.crawler.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.commons.validator.routines.UrlValidator;

import ie.samm.crawler.model.enumetaror.WebsiteEnum;
import ie.samm.crawler.model.util.Constants;

//@Controller
@ManagedBean(name="crawlerController")
@RequestScoped
public class CrawlerController extends AbstractController{

//	@ManagedProperty("#{goldenPagesCrawlerController}")
//	private GoldenPagesCrawlerController goldenPagesCrawlerController;
	
	private String url;
	
	public String search(){
		if(UrlValidator.getInstance().isValid(getUrl())){			
			if ((WebsiteEnum.GOLDEN_PAGES.getAddress()).replace(Constants.HTTPS_PROTOCOL, Constants.HTTP_PROTOCOL).equals(getUrl()) ||
				(WebsiteEnum.GOLDEN_PAGES.getAddress()).equals(getUrl())) {
//				getGoldenPagesCrawlerController().init();
				return "goldenPages";
			}
		}else{
			addErrorMessage("Invalid URL.");
		}
		return null;
	}

	/**
	 * @return the goldenPagesCrawlerController
	 */
//	public GoldenPagesCrawlerController getGoldenPagesCrawlerController() {
//		return goldenPagesCrawlerController;
//	}

	/**
	 * @param goldenPagesCrawlerController the goldenPagesCrawlerController to set
	 */
//	public void setGoldenPagesCrawlerController(GoldenPagesCrawlerController goldenPagesCrawlerController) {
//		this.goldenPagesCrawlerController = goldenPagesCrawlerController;
//	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
