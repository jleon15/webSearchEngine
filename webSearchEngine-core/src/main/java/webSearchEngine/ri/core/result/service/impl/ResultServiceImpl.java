package webSearchEngine.ri.core.result.service.impl;

import webSearchEngine.ri.core.result.service.ResultService;
import org.springframework.stereotype.Service;
import webSearchEngine.ri.model.QueryResult;
import webSearchEngine.ri.queryProcessor.QueryProcessor;

@Service("resultService")
public class ResultServiceImpl implements ResultService{


    @Override
    public QueryResult getResults(String query) {
        QueryProcessor queryProcessor = new QueryProcessor(query);
        return new QueryResult(queryProcessor.manageQuery());
    }

}
