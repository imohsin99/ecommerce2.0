package com.example.userservice.Repository;

import com.example.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    @Query(value = "select u.user_id,u.cnic,u.email,u.name,u.password,u.path,u.user_name FROM Users u WHERE u.email = ?1",nativeQuery = true)
    public User findUserByEmail( final String email);

    public Boolean existsByEmail(String email);

    public User findByCnic(long cnic);

    @Query(
        value = "select u.user_id,u.cnic,u.email,u.name,u.password,u.path,u.user_name FROM Users u where u.user_name=?1",
        nativeQuery = true
    )
    public User findByUser_name(String user_name);
    public User findByEmail(String email);
}
