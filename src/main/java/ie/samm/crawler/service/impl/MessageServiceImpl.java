package ie.samm.crawler.service.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ie.samm.crawler.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageSource messageSource;
	
	@Override
	public String getMessage(String message, Object... params) {
		return messageSource.getMessage(message, params, Locale.ENGLISH);
	}

}
