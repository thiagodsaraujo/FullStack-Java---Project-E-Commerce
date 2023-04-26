package com.titashop.product;

import com.titashop.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {


    @Query("SELECT p FROM Product p where p.enabled=true " +
            "and (p.category.id= ?1 OR p.category.allParentIDs LIKE %?2%)" +
            " ORDER BY p.name ASC")
    public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);


    public Product findByAlias(String alias);


    // aqui usamos a linguagem sql nativa e nao como acima, que colocamos o nome da entidade em especifico.
    @Query(value = "SELECT * FROM products p WHERE p.enabled = TRUE AND " +
            "MATCH(name, short_description, full_description) AGAINST(?1)", nativeQuery = true)
    public Page<Product> search(String keyword, Pageable pageable);



}
