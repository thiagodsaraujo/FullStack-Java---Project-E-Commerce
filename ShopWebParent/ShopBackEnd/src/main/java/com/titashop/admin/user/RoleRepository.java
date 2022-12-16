package com.titashop.admin.user;

import com.titashop.common.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


// Foi colocado no pom do ShopWebParente  o Role que encontra no modulo Common
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
