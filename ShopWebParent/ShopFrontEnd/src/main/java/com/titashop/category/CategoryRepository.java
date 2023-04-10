package com.titashop.category;

import com.titashop.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.enabled = TRUE ORDER BY c.name ASC")
    public List<Category> findAllEnabled();


    // códig
    @Query("SELECT c FROM Category c WHERE c.enabled = true AND NOT EXISTS "
            + "(SELECT 1 FROM Category child WHERE child.parent = c)")
    List<Category> findAllNoChildren();

}
