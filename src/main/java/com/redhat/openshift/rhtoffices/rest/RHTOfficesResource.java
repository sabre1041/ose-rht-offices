package com.redhat.openshift.rhtoffices.rest;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.redhat.openshift.rhtoffices.domain.RHTOffice;
import com.redhat.openshift.rhtoffices.mongo.DBConnection;
import com.redhat.openshift.rhtoffices.util.RhtOfficesUtil;

@RequestScoped
@Path("/offices")
public class RHTOfficesResource {

	@Inject
	private DBConnection dbConnection;
	
	@Inject
	private RhtOfficesUtil rhtOfficesUtil;

	private DBCollection getRHTOfficesCollection() {
		DB db = dbConnection.getDB();
		DBCollection officeListCollection = db.getCollection("offices");

		return officeListCollection;
	}

	private RHTOffice populateOfficeInformation(DBObject dataValue) {
		RHTOffice theOffice = new RHTOffice();
		theOffice.setName(dataValue.get("name"));
		theOffice.setCode(dataValue.get("code"));
		theOffice.setPosition(dataValue.get("coordinates"));
		theOffice.setId(dataValue.get("_id").toString());
		theOffice.setStreet(dataValue.get("street"));
		theOffice.setCity(dataValue.get("city"));
		theOffice.setState(dataValue.get("state"));
		theOffice.setZip(dataValue.get("zip"));
		theOffice.setPhone(dataValue.get("phone"));
		theOffice.setFax(dataValue.get("fax"));

		
		for(String featuredOffice : rhtOfficesUtil.getFeaturedOffices()) {
			if(theOffice.getCode() != null && theOffice.getCode().equals(featuredOffice.trim())) {
				theOffice.setFeatured(true);
				break;
			}

		}

		return theOffice;
	}

	// get all the mlb parks
	@GET()
	@Produces("application/json")
	public List<RHTOffice> getAllOffices() {
		ArrayList<RHTOffice> allOfficesList = new ArrayList<RHTOffice>();

		DBCollection rhtOffices = this.getRHTOfficesCollection();
		DBCursor cursor = rhtOffices.find();
		try {
			while (cursor.hasNext()) {
				allOfficesList.add(this.populateOfficeInformation(cursor.next()));
			}
		} finally {
			cursor.close();
		}

		return allOfficesList;
	}

	@GET
	@Produces("application/json")
	@Path("within")
	public List<RHTOffice> findOfficesWithin(@QueryParam("lat1") float lat1,
			@QueryParam("lon1") float lon1, @QueryParam("lat2") float lat2,
			@QueryParam("lon2") float lon2) {

		ArrayList<RHTOffice> allOfficesList = new ArrayList<RHTOffice>();
		DBCollection rhtOffices = this.getRHTOfficesCollection();

		// make the query object
		BasicDBObject spatialQuery = new BasicDBObject();

		ArrayList<double[]> boxList = new ArrayList<double[]>();
		boxList.add(new double[] { new Float(lon2), new Float(lat2) });
		boxList.add(new double[] { new Float(lon1), new Float(lat1) });

		BasicDBObject boxQuery = new BasicDBObject();
		boxQuery.put("$box", boxList);

		spatialQuery.put("coordinates", new BasicDBObject("$within", boxQuery));
		System.out.println("Using spatial query: " + spatialQuery.toString());

		DBCursor cursor = rhtOffices.find(spatialQuery);
		try {
			while (cursor.hasNext()) {
				allOfficesList.add(this.populateOfficeInformation(cursor.next()));
			}
		} finally {
			cursor.close();
		}

		return allOfficesList;
	}
}
