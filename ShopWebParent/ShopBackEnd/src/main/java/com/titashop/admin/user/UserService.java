package com.titashop.admin.user;

import com.titashop.common.entity.Role;
import com.titashop.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE = 5;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByEmail(String email){
        return userRepo.getUserByEmail(email);
    }


    public List<User> listAll(){

        return (List<User>) userRepo.findAll(Sort.by("id").ascending());
    }


    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);

        // código para asc ou desc
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

        if (keyword != null){
            return userRepo.findAll(keyword,pageable);
        }

        return userRepo.findAll(pageable);
    }

    public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
   }

    public User save(User user) {

        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepo.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());

            } else {
                encodePassword(user);

            }

        } else {
            encodePassword(user);
        }

        return userRepo.save(user);
    }

    public User updateAccount(User userInform){
        var userInDB = userRepo.findById(userInform.getId()).get();

        if (!userInform.getPassword().isEmpty()){
            userInDB.setPassword(userInform.getPassword());
            encodePassword(userInDB);
        }

        if (userInform.getPhotos() != null){
            userInDB.setPhotos(userInform.getPhotos());
        }

        userInDB.setFirstName(userInform.getFirstName());
        userInDB.setLastName(userInform.getLastName());

        return userRepo.save(userInDB);
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
