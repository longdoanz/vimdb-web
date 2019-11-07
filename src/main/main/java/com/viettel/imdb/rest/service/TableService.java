package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.TableModel;
import io.trane.future.Future;

public interface TableService {

//    On schedule
//    DeferredResult<RestErrorCode> getList(String db);

    Future<Result> createTable(TableModel tableModel);

    Future<Result> dropTable(TableModel tableModel);

}
