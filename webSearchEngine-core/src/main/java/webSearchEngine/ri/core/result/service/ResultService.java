package webSearchEngine.ri.core.result.service;

import webSearchEngine.ri.model.QueryResult;

public interface ResultService {

    QueryResult getResults(String query);
}
