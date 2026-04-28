
package com.Ar_Tech.repositories;

import com.Ar_Tech.models.ServiceOrderProductsParts;
import com.Ar_Tech.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
