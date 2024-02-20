package com.bloomberg.datawarehouse.Repositories;

import com.bloomberg.datawarehouse.Models.Deal;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for Deal entities, extending the Spring Data CrudRepository.
 */
public interface IDealRepository extends CrudRepository<Deal, String> {
}
