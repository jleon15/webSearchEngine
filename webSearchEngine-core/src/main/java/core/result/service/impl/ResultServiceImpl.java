package core.result.service.impl;

import core.result.service.ResultService;
import org.springframework.stereotype.Service;

@Service("resultService")
public class ResultServiceImpl implements ResultService{


    @Override
    public String sayHello() {
        return "Hello world";
    }

}
