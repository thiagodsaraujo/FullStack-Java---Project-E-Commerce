package com.titashop.admin.user;

import com.titashop.common.entity.Role;
import com.titashop.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> listAll(){
            return (List<User>) userRepo.findAll();
    }

    public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
   }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null); // se não for novo usuário ele já possui id

        if (isUpdatingUser) {
            User existingUser = userRepo.findById(user.getId()).get();
            if (user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword()); // se nao preencher o campo salva a senha que ja está
            } else {
                encodePassword(user); // se nao salvo a senha nova
            }
         } else {
            encodePassword(user);
            user.setCreatedDate(new Date(Calendar.getInstance().getTime().getTime()));
        }
        return userRepo.save(user);
    }

    // método para encodar o password
    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email){
        var userByEmail = userRepo.getUserByEmail(email);
        // se não existir retorna nulo, portanto e-mail válido e pode seguir
        if (userByEmail == null) return  true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew){
            if (userByEmail !=null) return false;
        } else {
            if (userByEmail.getId() != id){
                return false;
            }
        }
        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return  userRepo.findById(id).get();
        } catch (NoSuchElementException e){
            throw new UserNotFoundException("Could not find any user with ID :" + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        var countById = userRepo.countById(id);
        if (countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with ID :" + id);
        }
        userRepo.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepo.updateEnabledStatus(id, enabled);

    }
}
