package io.devopsnextgenx.demo.services;
import io.devopsnextgenx.demo.models.Demo;
import io.devopsnextgenx.demo.repositories.DemoRepository;
import io.devopsnextgenx.demo.exceptions.ResourceNotFoundException;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DemoService {

    private final DemoRepository demoRepository;

    public DemoService(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    public List<Demo> getAllDemos() {
        return (List<Demo>) demoRepository.findAll();
    }

    public Demo getDemoById(Long id) {
        return demoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demo not found with id: " + id));
    }

    public Demo createDemo(Demo demo) {
        return demoRepository.save(demo);
    }

    public Demo updateDemo(Long id, Demo demoDetails) {
        Demo demo = getDemoById(id);
        demo.setName(demoDetails.getName());
        demo.setDescription(demoDetails.getDescription());
        demo.setPrice(demoDetails.getPrice());
        return demoRepository.save(demo);
    }

    public void deleteDemo(Long id) {
        Demo demo = getDemoById(id);
        demoRepository.delete(demo);
    }

}
