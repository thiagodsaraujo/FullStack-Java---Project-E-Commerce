package com.titashop.admin.brand;

import com.titashop.common.entity.Brand;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>, CrudRepository<Brand, Integer> {

    public Brand findByName(String name);

    @Query("SELECT b FROM Brand b where b.name = :name")
    public Brand getBrandByName(String name);

    public Long countById(Integer id);
}
