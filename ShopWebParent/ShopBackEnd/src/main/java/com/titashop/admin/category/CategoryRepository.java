package com.titashop.admin.category;

import com.titashop.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, CrudRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.name = :name")
    public Category getCategoryByName(@Param("name") String name);

    public Category findByName(String name);

    public Category findByAlias(String alias);


    @Query("SELECT c FROM Category c WHERE CONCAT(c.id, ' ',c.alias, ' ', c.name) LIKE %?1%")
    Page<Category> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);

    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    @Query("SELECT c FROM Category  c WHERE c.parent is NULL")
    public List<Category> findRootCategories();
}
