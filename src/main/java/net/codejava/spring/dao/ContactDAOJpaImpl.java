/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.codejava.spring.dao;

import java.util.List;
import javax.transaction.Transactional;

import net.codejava.spring.exceptions.CrudValidationException;
import net.codejava.spring.model.Contact;
import net.codejava.spring.util.Validator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author Dimitar
 */
@Repository
@Primary
public class ContactDAOJpaImpl extends AbstractDAO<Contact> implements ContactDAO {

    public ContactDAOJpaImpl() {
        super(Contact.class);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void saveOrUpdate(Contact contact) {

        if (!Validator.patternMatchesEmail(contact.getEmail())) {
            throw new CrudValidationException("The email is not valid");
        }
        if (contact.getId() != 0) {
            update(contact);
        } else {
            create(contact);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(int contactId) {
        super.deleteById(contactId);
    }

    @Override
    public Contact get(int contactId) {
        return findOne(contactId);
    }

    @Override
    public List<Contact> list() {
        return findAll();

    }

}
