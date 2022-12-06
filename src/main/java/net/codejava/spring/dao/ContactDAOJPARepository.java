package net.codejava.spring.dao;

import net.codejava.spring.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactDAOJPARepository extends JpaRepository<Contact, Long> {

    List<Contact> findByNameLikeOrEmailLike(String name,  String email);

}
