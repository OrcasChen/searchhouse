package com.orcas.repository;

import com.orcas.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by xcw on 2018/1/21.
 */
public interface UserRepository extends CrudRepository<User, Long>{

    User findByName(String userName);

    User findUserByPhoneNumber(String telephone);
}
