package com.abugrov.realtask.repos;

import com.abugrov.realtask.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    List<Category> findAllByParentIsNull();

    List<Category> findAllByParent(Category parent);
}