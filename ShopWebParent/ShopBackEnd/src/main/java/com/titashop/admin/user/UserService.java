package com.titashop.admin.user;

import com.titashop.common.entity.Role;
import com.titashop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;


    public List<User> listAll(){
            return (List<User>) userRepo.findAll();
    }

   public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
   }

    public void save(User user) {
        userRepo.save(user);
    }
}
