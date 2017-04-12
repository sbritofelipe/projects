package ie.samm.crawler.model.util;

public class UrlUtils {
	
	private static UrlUtils instance = null;
	
	protected UrlUtils(){
		
	}
	
	public static UrlUtils getInstance(){
		if(instance == null){
			instance = new UrlUtils();
		}
		return instance;
	}
	

}
