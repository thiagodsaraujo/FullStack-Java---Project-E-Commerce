package com.titashop.common.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @Column(name = "all_parent_ids", length = 256, nullable = true)
    private String allParentIDs;


    @SuppressWarnings("JpaAttributeTypeInspection")
    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<Category> children = new HashSet<>();


    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "Default_Image";
    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public static Category copyIdAndName(Category category) {
        Category copyCategory = new Category();

        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());

        return copyCategory;
    }

    public static Category copyIdAndName(Integer id, String name) {
        Category copyCategory = new Category();

        copyCategory.setId(id);
        copyCategory.setName(name);

        return copyCategory;
    }

    public static Category copyFull(Category category) {
        Category copyCategory = new Category();

        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        copyCategory.setImage(category.getImage());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setHasChildren(category.getChildren().size() > 0);

        return copyCategory;
    }


    public static Category copyFull(Category category, String name) {
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);
        return copyCategory;

    }

    public Category(String name, Category parent) {
        this(name); // vai chamar o primeiro construtor
        this.parent = parent;
    }

    public Category(String name, String alias, String image, boolean enabled, Category parent, Set<Category> children) {
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.enabled = enabled;
        this.parent = parent;
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    @Transient
    public String getImagePath() {
        if (this.id == null) return "/images/image-thumbnail.png";
        return "/category-images/" + this.id + "/" + this.image;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }


    public String getAllParentIDs() {
        return allParentIDs;
    }

    public void setAllParentIDs(String allParentIDs) {
        this.allParentIDs = allParentIDs;
    }

    @Transient
    private boolean hasChildren;

    @Override
    public String toString() {
        return this.name;
    }
}


