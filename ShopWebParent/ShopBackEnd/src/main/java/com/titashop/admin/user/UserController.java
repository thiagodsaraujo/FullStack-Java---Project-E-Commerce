package com.titashop.admin.user;


import com.titashop.admin.FileUploadUtil;
import com.titashop.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {


    @Autowired
    private UserService service;



    // no código html, faz referencia a "/users" então no getmapping vamos fazer da mesma forma
    // Aqui ele mudou de listAll para listar a primeira pagina da paginação chamando o método listByPage
    @GetMapping("/users")
    public String listFirstPage(Model model){
        return listByPage(1,model,"firstName", "asc", null);
    }


    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum,
                             Model model,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){

        System.out.println("Sort Field: " + sortField);
        System.out.println("Sort Order: " + sortDir);

        var page = service.listByPage(pageNum, sortField, sortDir, keyword);
        var listUsers = page.getContent();

        System.out.println("TotalElements = " + page.getTotalElements());
        System.out.println("Total Pages = " + page.getTotalPages());
        System.out.println("PageNum = " + pageNum);

        long startCount = (pageNum - 1 ) * UserService.USERS_PER_PAGE + 1;
        System.out.println("startCount = " + startCount);

        long endCount = startCount + UserService.USERS_PER_PAGE -1;
        System.out.println("endCount = " + endCount);


        if (endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";


        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "users";

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
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image")MultipartFile multipartFile) throws IOException {

//        System.out.println(user);
//        System.out.println(multipartFile.getOriginalFilename());
//        user.setCreatedDate (new Date(Calendar.getInstance().getTime().getTime()));

        if (!multipartFile.isEmpty()){

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = service.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
                service.save(user);

            }

        redirectAttributes.addFlashAttribute("message", "The user has been save successfully!");

        return getRedirectURLtoAffectedUserUpdate(user);
    }

    private String getRedirectURLtoAffectedUserUpdate(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];

        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
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

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable (name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "The User ID: " + id + " has been deleted successfully!");
        } catch (UserNotFoundException e) {
            // para aparecer a mensagem na pagina fazemos dessa forma
            redirectAttributes.addFlashAttribute("message",
                    e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes){
        service.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        var listUsers = service.listAll();
        UserCsvExport exporter = new UserCsvExport();
        exporter.export(listUsers,response);
    }



}
