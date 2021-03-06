package com.example.try4.controller;



import com.example.try4.dao.AppUserDAO;
import com.example.try4.dao.PlaceDAO;
import com.example.try4.entity.Place;
import com.example.try4.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Transactional

public class PlaceController {
    @Autowired
    StorageService storageService;

    @Autowired
    private AppUserDAO appUserDAO;

    @Autowired
    private PlaceDAO placeDAO;



    @RequestMapping(value = "/newPlace", method = RequestMethod.GET)
    public String newPlace(Model model) {
        Place form = new Place();
        model.addAttribute("place", form);
        return "newPlace";
    }
    @RequestMapping(value = "/newPlace", method = RequestMethod.POST)
    public String saveRegister(@ModelAttribute("app")@Valid Place app,
                               BindingResult result, Model model, //
                               Principal principal, @RequestParam(name = "file1", required = false)MultipartFile file1,
                               @RequestParam(name = "file2", required = false)MultipartFile file2, @RequestParam(name = "file3", required = false)MultipartFile file3) {
        if (result.hasErrors()) {
            model.addAttribute("app", app);
            return "newPost";
        }
        try {
            app=storageService.preStore(file1,file2,file3,app);
            appUserDAO.findAppUserByUserName(principal.getName());
            app.setUsarname(principal.getName());
            placeDAO.addPlace(app);
        } catch (Exception e) {

            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("app", app);
            model.addAttribute("message","there is already exist such image");
            return "newPost";
        }

        return "redirect:/userPage?username="+principal.getName();
    }
}
