package com.mybudget.Repository;

import com.mybudget.Entity.Transaction;
import com.mybudget.Entity.User;
import com.mybudget.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserOrderByDateDesc(User user);

    List<Transaction> findByUserAndTypeOrderByDateDesc(User user, TransactionType type);

    List<Transaction> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDateTime start, LocalDateTime end);

    Optional<Transaction> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user = :user AND t.type = :type AND t.date BETWEEN :start AND :end")
    BigDecimal sumAmountByUserAndTypeAndDateBetween(@Param("user") User user, @Param("type") TransactionType type,
                                                    @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}