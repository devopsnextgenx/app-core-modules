package io.devopsnextgenx.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import io.devopsnextgenx.demo.models.Demo;
import jakarta.transaction.Transactional;

@Transactional
public interface DemoRepository extends CrudRepository<Demo, Long> {
    
}
