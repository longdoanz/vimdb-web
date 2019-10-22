package com.viettel.imdb.rest.service;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.IndexModel;
import com.viettel.imdb.rest.model.TableModel;
import io.trane.future.Future;

public interface IndexService {

    Future<Result> createIndex(IMDBClient imdbClient,TableModel tableModel, IndexModel indexModel);

    Future<Result> dropIndex(IMDBClient imdbClient, TableModel tableModel, String fieldName);

}
