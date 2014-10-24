package org.knitter.giftregistrylite.family;

import org.knitter.giftregistrylite.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.persistence.EntityManager;

@Api(name = "familymemberendpoint", namespace = @ApiNamespace(ownerDomain = "knitter.org", ownerName = "knitter.org", packagePath = "giftregistrylite.family"))
public class FamilyMemberEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
/*	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listFamilyMember")
	public CollectionResponse<FamilyMember> listFamilyMember(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<FamilyMember> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr
					.createQuery("select from FamilyMember as FamilyMember");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<FamilyMember>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (FamilyMember obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<FamilyMember> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}
*/
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
/*	@ApiMethod(name = "getFamilyMember")
	public FamilyMember getFamilyMember(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		FamilyMember familymember = null;
		try {
			familymember = mgr.find(FamilyMember.class, id);
		} finally {
			mgr.close();
		}
		return familymember;
	}
*/
	
	/**
	 * This method gets the FamilyMember for the current logged in user. It uses HTTP GET method.
	 *
	 * @return The familymember for the logged in user.
	 * @throws UnauthorizedException 
	 * @throws NotFoundException 
	 */
	@ApiMethod(name = "getCurrentFamilyMember")
	public FamilyMember getFamilyMember() throws UnauthorizedException, NotFoundException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
	    if (user==null)
	    	throw new UnauthorizedException("Not logged in.  Visit the website to log in.");
		return FamilyMember.getFamilyMember(user);
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param familymember the entity to be inserted.
	 * @return The inserted entity.
	 */
/*	@ApiMethod(name = "insertFamilyMember")
	public FamilyMember insertFamilyMember(FamilyMember familymember) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsFamilyMember(familymember)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(familymember);
		} finally {
			mgr.close();
		}
		return familymember;
	}
*/
	
	/**
	 * This allows a user to join a family. If the family already
	 * exists in the datastore, the token is checked.  If the family
	 * does not exist, it is created. It is assumed that the FamilyMember
	 * does not exist as well.
	 * It uses HTTP POST method.
	 *
	 * @param family the entity to be inserted.
	 * @return The inserted entity.
	 * @throws UnauthorizedException
	 */
	@ApiMethod(name = "insertFamilyMember")
	public FamilyMember insertFamilyMember(Family family) throws UnauthorizedException {
		FamilyMember familymember=null;

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
	    if (user==null)
	    	throw new UnauthorizedException("Not logged in.  Visit the website to log in.");

	    Family tfamily=Family.getFamily(family.getName());
		
		if (tfamily != null) {
			if (!tfamily.getToken().equals(family.getToken()))
				throw new UnauthorizedException("Token mismatch on existing Family");
		} else {
			tfamily=Family.makeFamily(user, family.getName());
		}
		
		// TODO make family member
		
		return familymember;
	}

	
	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param familymember the entity to be updated.
	 * @return The updated entity.
	 */
/*	@ApiMethod(name = "updateFamilyMember")
	public FamilyMember updateFamilyMember(FamilyMember familymember) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsFamilyMember(familymember)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(familymember);
		} finally {
			mgr.close();
		}
		return familymember;
	}
*/
	
	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
/*	@ApiMethod(name = "removeFamilyMember")
	public void removeFamilyMember(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			FamilyMember familymember = mgr.find(FamilyMember.class, id);
			mgr.remove(familymember);
		} finally {
			mgr.close();
		}
	}
*/
	
	@SuppressWarnings("unused")
	private boolean containsFamilyMember(FamilyMember familymember) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			FamilyMember item = mgr.find(FamilyMember.class,
					familymember.getId());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
