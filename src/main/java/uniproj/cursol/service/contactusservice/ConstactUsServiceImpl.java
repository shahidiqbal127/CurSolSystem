package uniproj.cursol.service.contactusservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uniproj.cursol.dao.ContactUsRepo;
import uniproj.cursol.entity.ContactUs;

@Service
public class ConstactUsServiceImpl implements ContactUsService {

    @Autowired
    private ContactUsRepo contactUsRepository;

    @Override
    public ContactUs saveContactForm(ContactUs contactUs) {
        return contactUsRepository.save(contactUs);
    }

}
