package net.codejava.spring.dao;

import java.util.List;

import net.codejava.spring.model.Contact;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Defines DAO operations for the contact model.
 * @author www.codejava.net
 *
 */
public interface ContactDAO {

	@PreAuthorize("hasAuthority('ADMINISTRATOR')")
	public void saveOrUpdate(Contact contact);
	@PreAuthorize("hasAuthority('ADMINISTRATOR')")
	public void delete(int contactId);
	
	public Contact get(int contactId);
	
	public List<Contact> list();
}
