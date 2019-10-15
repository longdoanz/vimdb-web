package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.IndexModel;
import com.viettel.imdb.rest.model.TableModel;
import io.trane.future.Future;

public interface IndexService {

    Future<Result> createIndex(TableModel tableModel, IndexModel indexModel);

    Future<Result> dropIndex(TableModel tableModel, String fieldName);

}
