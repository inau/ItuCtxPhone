package servlet;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;

import servlet.entities.BeaconEntity;
import servlet.entities.ContextEntity;

public class CtxServlet extends HttpServlet {
	final static long M_range = (long) 0.00005;

	//To avoid hitting the 'request limit' i use a simple cache approach
	protected static volatile boolean dirty = false;
	protected static volatile List<ContextEntity> cached;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		List<ContextEntity> contexts = null;  

		//Check cached values exist and are not dirty
		if(dirty || cached == null) {
			cached = ObjectifyService.ofy().load().type(ContextEntity.class).order("-updated").list();
			dirty = false;
		}
		
		//if no parameters have been passed return all contexts
		if( req.getParameterMap().isEmpty() ) contexts = cached;		
		else {
			try {
				String p1 = req.getParameter("lat"), p2 = req.getParameter("lng"), p3 = req.getParameter("after");
				
				//checks if input params are !null and !""
				if( isValidArg(p1) && isValidArg(p2) ) {
					contexts = new ArrayList<ContextEntity>();
				
					Long  	lat = Long.parseLong( p1 ),
							lng = Long.parseLong( p2 );
					Date d = null;
					if( isValidArg( p3 ) ) d = new Date( Date.parse( p3 ) );

					if(lat != null && lng != null) {
						if(d != null) {
							//filters the cached values if lat/lng and date have values
							for (ContextEntity ce : cached) {
								if( isNearLocationAndAfterDate(lat, lng, d, ce) ) contexts.add(ce);
							}
						}
						else {
							//filters the cached values if lat/lng have values
							for (ContextEntity ce : cached) {
								if( isNearLocation(lat, lng, ce) ) contexts.add(ce);
							}
						}
					}
				}
			}
			catch(Exception e) {
				System.out.println("Hit an error " + e.getMessage() );
			}
		}

		//flush results
		resp.setContentType("application/json");
		Gson gson = new Gson();
		gson.toJson(contexts, resp.getWriter());
	}

	//Objectify only allows to filter on 1 property at a time so we filter in the ws requests
	private boolean isNearLocationAndAfterDate(long lat, long lng, Date d, ContextEntity ce) {
		return isAfterDate(d, ce) && isNearLocation(lat, lng, ce);
	}

	private boolean isAfterDate(Date earliest, ContextEntity ce) {
		return ce.updated.after(earliest);
	}

	private boolean isNearLocation(long lat, long lng, ContextEntity ce) {
		return 	(ce.lat <= (lat + M_range)) 	&&
				(ce.lat >= (lat - M_range)) 	&&
				(ce.lng <= (lng + M_range)) 	&&
				(ce.lng >= (lng - M_range));
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

		//create entity from JSON
		ContextEntity ce = gson.fromJson(rawJson, ContextEntity.class);
		ContextEntity res = null;
		
		//lookup tables by id
		if(ce.uid != null)
		res = ofy().load().type(ContextEntity.class).id(ce.uid).now();
		
		//new key or no key
		if ( res == null ) {
			ce.touch();
			com.googlecode.objectify.Key<ContextEntity> k = ofy().save().entity(ce).now();
			dirty = true;
			gson.toJson( (ContextEntity)ofy().load().key(k).now(), resp.getWriter());
		}
		//existing entity - merge
		else {
			res.values = ce.values;
			res.lat = ce.lat;
			res.lng = ce.lng;
			res.type = ce.type;
			res.touch();
			
			com.googlecode.objectify.Key<ContextEntity> k = ofy().save().entity(res).now();
			dirty = true;
			gson.toJson( (ContextEntity)ofy().load().key(k).now(), resp.getWriter());
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if( !req.getParameterMap().isEmpty() ) {
			String p1 = req.getParameter("uid");
			if( isValidArg( p1 ) ) {
				ContextEntity res = ofy().load().type(ContextEntity.class).id( Long.parseLong(p1) ).now();
				if(res != null) {
					ofy().delete().entity(res).now();
					dirty = true;
					resp.setStatus(202);
				}
			}
		}
	}
	
}
