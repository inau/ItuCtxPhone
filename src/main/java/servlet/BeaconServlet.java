package servlet;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.googlecode.objectify.Result;

import servlet.entities.BeaconEntity;
import servlet.entities.ContextEntity;

public class BeaconServlet extends HttpServlet {
	final static long M_range = (long) 0.00005;

	//To avoid hitting the 'request limit' i use a simple cache approach
	protected static volatile boolean dirty = false;
	protected static volatile List<BeaconEntity> cached;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if(dirty || cached == null) {
			cached = ofy().load().type(BeaconEntity.class).order("-updated").list();
			dirty = false;
		}

		List<BeaconEntity> beacons = null;
		try {
			String p1 = req.getParameter("lat"), p2 = req.getParameter("lng");
			if( isValidArg(p1) && isValidArg(p2) ) {
				//checks if input params are actual values
				Long  	lat = Long.parseLong( p1 ),
						lng = Long.parseLong( p2 );    		
				if(lat != null && lng != null) {
					//filters the cached values if lat/lng has values
					beacons = new ArrayList<BeaconEntity>();
					for (BeaconEntity be : cached) {
						if( filterRangeLocation(lat, lng, be) ) beacons.add(be);
					}
				}
			}
		} catch (Exception e) {}
		Gson gson = new Gson();
		resp.setContentType("application/json");

		if(beacons == null) beacons = cached;

		gson.toJson(beacons, resp.getWriter());
	}

	//Objectify only allows to filter on 1 property at a time so we filter in the ws requests
	private boolean filterRangeLocation(long lat, long lng, BeaconEntity be) {
		return 	(be.lat <= (lat + M_range)) 	&&
				(be.lat >= (lat - M_range)) 	&&
				(be.lng <= (lng + M_range)) 	&&
				(be.lng >= (lng - M_range));
	}

	private boolean isValidArg(String arg) {
		return arg != null || arg != "";
	}

	
	// Process the http POST of the form
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Gson gson = new Gson();

		String rawJson = new BufferedReader(
				new InputStreamReader( req.getInputStream() )
				).readLine();

		BeaconEntity be = gson.fromJson(rawJson, BeaconEntity.class);
		BeaconEntity res = null;

		//lookup tables by id
		if( be.key != null )
		res = ofy().load().type(BeaconEntity.class).id(be.key).now();

		//new key or no key
		if ( res == null ) {
			be.touch();
			be.key = BeaconEntity.GenKey(be.uid, be.major, be.minor);
			com.googlecode.objectify.Key<BeaconEntity> k = ofy().save().entity(be).now();
			dirty = true;
			gson.toJson( (BeaconEntity)ofy().load().key(k).now() , resp.getWriter());
		}
		//existing entity - merge
		else {
			res.lat = be.lat;
			res.lng = be.lng;
			res.touch();
			com.googlecode.objectify.Key<BeaconEntity> k = ofy().save().entity(res).now();
			dirty = true;
			gson.toJson( (BeaconEntity)ofy().load().key(k).now(), resp.getWriter());
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if( !req.getParameterMap().isEmpty() ) {
			String p1 = req.getParameter("key");
			System.out.println(":> "+p1);
			if( isValidArg( p1 ) && p1.split(":").length == 3) {
				BeaconEntity res = ofy().load().type(BeaconEntity.class).id( p1 ).now();
				if(res != null) {
					ofy().delete().entity(res).now();
					dirty = true;
					resp.setStatus(202);
					return;
				}
			}
			resp.getWriter().append("No match");
		}
	}
}
