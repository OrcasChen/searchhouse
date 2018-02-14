package com.orcas.repository;

import com.orcas.entity.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 角色数据DAO
 * Created by xcw on 2018/1/22.
 */
public interface RoleRepository extends CrudRepository<Role, Long>{

    List<Role> findRolesByUserId(Long useId);
}
