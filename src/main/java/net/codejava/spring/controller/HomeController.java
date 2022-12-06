package net.codejava.spring.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.codejava.spring.dao.ContactDAO;
import net.codejava.spring.dao.ContactDAOJPARepository;
import net.codejava.spring.exceptions.CrudValidationException;
import net.codejava.spring.model.Contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller routes accesses to the application to the appropriate
 * hanlder methods.
 *
 * @author www.codejava.net
 */
@Controller
public class HomeController {

    @Autowired
    private ContactDAO contactDAO;

    @Autowired
    private ContactDAOJPARepository contactDAOJPARepository;

    @RequestMapping(value = "/")
    public ModelAndView listContact(ModelAndView model) throws IOException {
        List<Contact> listContact = contactDAOJPARepository.findAll();
        model.addObject("listContact", listContact);
        model.setViewName("home");

        return model;
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public ModelAndView findContacts(@RequestParam("searhTerm") String searhTerm, ModelAndView model, Principal principal) throws IOException {
        System.out.println("SearchTerm=" + searhTerm);
        List<Contact> listContacts = contactDAOJPARepository.findByNameLikeOrEmailLike(searhTerm + "%", searhTerm + "%");
        System.out.println("listContacts=" + listContacts.size());
        System.out.println("Principal=" + principal.getName());
        model.addObject("listContact", listContacts);
        model.addObject("searhTerm", searhTerm);
        model.setViewName("home");
        return model;
    }

    @RequestMapping(value = "/newContact", method = RequestMethod.GET)
    public ModelAndView newContact(ModelAndView model) {
        Contact newContact = new Contact();
        model.addObject("contact", newContact);
        model.setViewName("ContactForm");
        return model;
    }

    @RequestMapping(value = "/saveContact", method = RequestMethod.POST)
    public ModelAndView saveContact(@ModelAttribute Contact contact) {
        ModelAndView model = new ModelAndView("redirect:/");
        try {
            contactDAO.saveOrUpdate(contact);
        } catch (CrudValidationException e) {
            model.setViewName("ContactForm");
            model.addObject("contact", contact);
            model.addObject("message", e.getMessage());
            return model;
        }
        return model;
    }


    @RequestMapping(value = "/deleteContact", method = RequestMethod.GET)
    public ModelAndView deleteContact(HttpServletRequest request) {
        int contactId = Integer.parseInt(request.getParameter("id"));
        contactDAO.delete(contactId);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/editContact", method = RequestMethod.GET)
    public ModelAndView editContact(HttpServletRequest request) {
        int contactId = Integer.parseInt(request.getParameter("id"));
        Contact contact = contactDAO.get(contactId);
        ModelAndView model = new ModelAndView("ContactForm");
        model.addObject("contact", contact);

        return model;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;
    }

    @RequestMapping(value = "/unauthorized", method = {RequestMethod.GET,  RequestMethod.POST})
    @ResponseBody
    public String unauthorized() {
        return "Not authorized, sorry.";
    }

}
