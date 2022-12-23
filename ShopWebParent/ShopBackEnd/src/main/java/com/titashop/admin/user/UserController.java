package com.titashop.admin.user;


import com.titashop.common.entity.Role;
import com.titashop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Controller
public class UserController {


    @Autowired
    private UserService service;



    // no código html, faz referencia a "/users" então no getmapping vamos fazer da mesma forma
    @GetMapping("/users")
    public String listAll(Model model){
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers);
        return "users"; // aqui ele vai procurar o arquivo html chamando users.
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        var listRoles = service.listRoles();

        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle","Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        System.out.println(user);
        user.setCreatedDate(new Date(Calendar.getInstance().getTime().getTime()));
        service.save(user);

        redirectAttributes.addFlashAttribute("message",
                "The user has been save successfully!");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable (name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try {
            User user = service.get(id);
            var listRoles = service.listRoles();
            model.addAttribute("user", user); // pegamos daqui para jogar no html
            model.addAttribute("pageTitle","Editing User (ID: " + id + ")"); // para setar o nome correto da página
            model.addAttribute("listRoles", listRoles);
            return "user_form";
        } catch (UserNotFoundException e) {
            // para aparecer a mensagem na pagina fazemos dessa forma
            redirectAttributes.addFlashAttribute("message",
                    e.getMessage());
            return "redirect:/users";
        }
    }


}
