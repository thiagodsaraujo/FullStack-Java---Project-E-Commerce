package com.titashop.common.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "first_name",length = 45, nullable = false)
    private String firstName;

    @Column(name = "last_name",length = 45, nullable = false)
    private String lastName;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(length = 64)
    private String photos;


    private boolean enabled;

    /** aqui vai ser o seguinte, tbl user_roles:
     * user_id => referencia o id de users
     * role_id => referencia o id de roles
     * e faz da seguinte maneira e o Jpa já vai criar a nova tbl intermediaria para fazer as junções
    */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles", //nome da table a ser criada
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdDate = new Date(Calendar.getInstance().getTime().getTime());
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createdDate=" + createdDate +
                ", photos='" + photos + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }

    @Transient
    public String getPhotosImagePath(){
        if (id == null || photos == null) return "/images/default-user.png"; // ele colou image

        return "/user-photos/" + this.id + "/" + this.photos;
    }

    @Transient
    public  String getFullName(){
        return firstName + " " + lastName;
    }
}
