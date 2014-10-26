package org.blandsite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.blandsite.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import javax.jdo.Query;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;

@Api(name = "giftendpoint", namespace = @ApiNamespace(ownerDomain = "blandsite.org", ownerName = "blandsite.org", packagePath = ""))
public class GiftEndpoint {


	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getGift")
	public Gift getGift(@Named("id") String id, @Named("token") String token) {
		Gift gift = null;
		Token t=Token.validateToken(token);
		if (t != null) {
			PersistenceManager mgr = getPersistenceManager();
			try {
				Gift gt = mgr.getObjectById(Gift.class, id);
				if (gt.getUsername().equals(t.getUsername())) {
					gift=gt;
				}
			} finally {
				mgr.close();
			}
		}
		return gift;
	}
	
	@ApiMethod(name = "listGifts")
	public CollectionResponse<Gift> listGifts(
			@Named("token") String token,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Gift> execute = null;

		try {
			mgr = getPersistenceManager();
			
			Token t=Token.validateToken(token);
			if (t!=null) {
				
				Query query = mgr.newQuery(Gift.class);
				query.setFilter("username == usernameParam");
				query.setOrdering("ranking desc");
				query.declareParameters("String usernameParam");

				
				if (cursorString != null && cursorString != "") {
					cursor = Cursor.fromWebSafeString(cursorString);
					HashMap<String, Object> extensionMap = new HashMap<String, Object>();
					extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
					query.setExtensions(extensionMap);
				}
	
				if (limit != null) {
					query.setRange(0, limit);
				}
	
				execute = (List<Gift>) query.execute(t.getUsername());
				cursor = JDOCursorHelper.getCursor(execute);
				if (cursor != null)
					cursorString = cursor.toWebSafeString();
	
				// Tight loop for fetching all entities from datastore and accomodate
				// for lazy fetch.
				for (Gift obj : execute)
					;
			}
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Gift> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}	
	

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param gift the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertGift")
	public Gift insertGift(Gift gift, @Named("token") String token) {
		Token t=Token.validateToken(token);
		if (t != null) {
			
			PersistenceManager mgr = getPersistenceManager();
			try {
				if (containsGift(gift)) {
					throw new EntityExistsException("Object already exists");
				}
				User u=mgr.getObjectById(User.class, t.getUsername());
				u.addGift(gift);
				ArrayList a=new ArrayList();
				a.add(u);
				a.add(gift);
				mgr.makePersistentAll(a);
			} finally {
				mgr.close();
			}
		}
		return gift;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param gift the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateGift")
	public Gift updateGift(Gift gift, @Named("token") String token) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsGift(gift)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(gift);
		} finally {
			mgr.close();
		}
		return gift;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeGift")
	public void removeGift(@Named("id") String id, @Named("token") String token) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Gift gift = mgr.getObjectById(Gift.class, id);
			mgr.deletePersistent(gift);
		} finally {
			mgr.close();
		}
	}

	private boolean containsGift(Gift gift) {
		if (gift == null || gift.getKey() == null)  return false;
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Gift.class, gift.getKey());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
