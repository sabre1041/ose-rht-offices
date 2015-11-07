package com.redhat.openshift.rhtoffices.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class RhtOfficesUtil {

	private List<String> featuredOffices = new ArrayList<String>();
	
	private static final String PROPERTIES_FILE_NAME = "offices.properties";
	private static final String FEATURED_OFFICE_PROPRETY = "featuredOffice";
	
	@PostConstruct
	public void init() {
		try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
			
			Properties props = new Properties();
			props.load(is);
			
			String featuredOfficesRaw = props.getProperty(FEATURED_OFFICE_PROPRETY);
			
			if(featuredOfficesRaw != null) {
				featuredOffices = Arrays.asList(featuredOfficesRaw.split(","));
			}			
			
		} catch (IOException e) {
			// Do nothing if exception thrown
		}
	}
	
	public List<String> getFeaturedOffices() {
		return featuredOffices;
	}
	
}
