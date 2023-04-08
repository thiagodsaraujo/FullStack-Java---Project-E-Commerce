package com.titashop.admin.security;

import com.titashop.common.entity.Role;
import com.titashop.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TitaShopUserDetails implements UserDetails {


    private static final long serialVersion = 1L;

    private User user;

    public TitaShopUserDetails(User user) {
        this.user = user;
    }


    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        for(Role role: roles){
            authorityList.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorityList;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }
    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public String getFullName(){
        return this.user.getFirstName() + " " + this.user.getLastName();
    }

    public String getFirstName(){
        return this.user.getFirstName();
    }

    public String getLastName(){
        return this.user.getLastName();
    }

    public String getNomeCompleto(){
        return this.user.getFirstName() + " " + this.user.getLastName();
    }


    public String getPhotosImagePath() {
        if(this.user.getId()==null||this.user.getPhotos() == null) return "/images/pic.png";

        return "/user-photos/"+this.user.getId() +"/" +this.user.getPhotos();
    }

    public void setFirstName(String firstName){
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName){
        this.user.setFirstName(lastName);
    }

    public boolean hasRole(String roleName){
        return user.hasRole(roleName);
    }
}
