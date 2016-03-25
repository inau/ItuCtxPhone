package servlet.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class BeaconEntity {
	
	@Id
	public String key;
	
	public String uid, major, minor;
	
	@Index public long lat, lng;
	@Index public Date updated;

	public BeaconEntity() {
		this.updated = new Date();
	}
	
	public BeaconEntity(String uid, String major, String minor, long lat, long lng) {
		this();
		this.uid = uid;
		this.major = major;
		this.minor = minor;
		this.lat = lat;
		this.lng = lng;
		this.key = GenKey(this.uid, this.major, this.minor);
	}
	
	public void touch() {
		this.updated = new Date();
	}
	
	public static String GenKey(String uid, String major, String minor) {
		return uid +":"+major+":"+minor;
	}
	
}
