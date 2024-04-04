package com.example.cartservice.Repository;

import com.example.Models.Cart;
import com.example.Models.Product;
import com.example.Models.User;
import io.micrometer.core.instrument.config.validate.Validated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    public Cart findCartByUser(User user);

    public List<Cart> findCartsByUser(User user);
    public boolean existsByUser(User user);

    @Query(
            value = "SELECT c.cart_id  FROM Carts c where c.user_user_id= ?1",
            nativeQuery = true
    )
    public String findProductsByUser(Long id);

    @Query(
            value = "select * from carts c where c.user_user_id= ?1",
            nativeQuery = true
    )
    List<Cart> findAllByUserId(long user_id);

    @Query(
        value = "select * from carts c where c.user_id= ?1",
        nativeQuery = true
    )
    Cart findByUserId(long user_id);

    @Modifying
    @Transactional
    @Query(
            value = "update carts  set quantity = ?1 where cart_id = ?2",
            nativeQuery = true
    )
    void incrementQuantity(int value, long id);

//    @Query
//        (
//            value = "select count(c) from carts c where c.status = 0 and c.user_user_id = ?1",
//            nativeQuery = true
//        )
//    int numberOfEntitiesByStatusZero(long id);


//    List<Cart> findCartsByStatus(Integer valueOf);

}
