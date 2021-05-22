package com.example.kursach.repositories;

import com.example.kursach.models.Order;
import com.example.kursach.models.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findAllByUser(User user);
    public Order findByUserAndIsFinished(User user, boolean isFinished);
    public List<Order> findAllByUserAndIsFinished(User user, boolean isFinished);
}
