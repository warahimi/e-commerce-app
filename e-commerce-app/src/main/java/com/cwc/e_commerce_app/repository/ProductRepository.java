package com.cwc.e_commerce_app.repository;

import com.cwc.e_commerce_app.Entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrue();


    /* --------------------------------------------------------------------------------------------------*/

    // JPQL is better for non-trivial search logic
    // Derived queries are OK only for very simple cases
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String keyword); // Derived query

        /*
           Equivalent SQL (Thi is what Hibernate actually runs)
           SELECT * FROM products p
           WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', ?, '%'))
           AND p.is_active = true;
         */
    @Query("""
        SELECT p
        FROM products p
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND p.isActive = true
    """)
    List<Product> searchByName(@Param("keyword") String keyword); // JPQL query

    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndIsActiveTrue(String keyword, String keyword1);

        /*
            Equivalent SQL (What Hibernate actually runs)
            SELECT *
            FROM products p
            WHERE (
                    LOWER(p.name) LIKE LOWER(CONCAT('%', ?, '%'))
                 OR LOWER(p.description) LIKE LOWER(CONCAT('%', ?, '%'))
                  )
              AND p.is_active = true;

        */
    @Query(""" 
    SELECT p FROM products p
    WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
      AND p.isActive = true
    """)
    List<Product> searchByNameOrDescription(@Param("keyword") String keyword);


    /* --------------------------------------------------------------------------------------------------*/

}
