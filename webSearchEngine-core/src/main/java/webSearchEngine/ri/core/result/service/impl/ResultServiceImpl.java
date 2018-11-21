package webSearchEngine.ri.core.result.service.impl;

import webSearchEngine.ri.core.result.service.ResultService;
import org.springframework.stereotype.Service;

@Service("resultService")
public class ResultServiceImpl implements ResultService{


    @Override
    public String sayHello() {
        return "Hello world";
    }

}
