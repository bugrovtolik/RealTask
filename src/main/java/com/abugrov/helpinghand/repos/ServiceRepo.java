package com.abugrov.helpinghand.repos;

import com.abugrov.helpinghand.domain.Service;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceRepo extends CrudRepository<Service, Long> {

    List<Service> findByTitle(String title);
}
