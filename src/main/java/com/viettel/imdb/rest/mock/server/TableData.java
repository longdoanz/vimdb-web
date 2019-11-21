package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.protocol.Deserializer;
import com.viettel.imdb.protocol.Serializer;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import io.trane.future.Future;
import io.trane.future.Promise;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.BytesIn;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.pmw.tinylog.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class TableData {
    public DB mapDB;
    private ConcurrentMap dataStore;
    private static IMDBEncodeDecoder encodeDecoder = IMDBEncodeDecoder.getInstance();
    private String tableName;


    public TableData(String tableFileName) {
        tableName = tableFileName;
        try {
            mapDB = DBMaker.fileDB(tableFileName + ".db").checksumHeaderBypass().make();
        } catch (Exception ex) {
            remove();
            mapDB = DBMaker.fileDB(tableFileName + ".db").checksumHeaderBypass().make();
        }
        dataStore = mapDB.hashMap("map").createOrOpen();
        System.out.println(dataStore.keySet());
    }

    public Record get(String key) {
        if(dataStore.get(key) != null)
            return Deserializer.readDocumentWithVersion(Bytes.allocateDirect((byte[]) dataStore.get(key)));
        return null;
    }

    public void insert(Promise<Void> future, String key, List<Field> fieldList) {
        if(dataStore.get(key) != null) {
            future.setException(new ClientException(ErrorCode.KEY_EXIST));
            return;
        }
        dataStore.put(key, fieldsToBytes(fieldList));
        future.setValue(null);
    }

    public void update(Promise<Void> future, String key, List<Field> fieldList) {
        if(dataStore.get(key) == null) {
            future.setException(new ClientException(ErrorCode.KEY_NOT_EXIST));
            return;
        }
        dataStore.replace(key, fieldsToBytes(fieldList));
        future.setValue(null);
    }

    public void delete(Promise<Void> future, String key) {
        if(dataStore.get(key) == null) {
            future.setException(new ClientException(ErrorCode.KEY_NOT_EXIST));
            return;
        }
        dataStore.remove(key);
        future.setValue(null);
    }

    public int size() {
        return dataStore.size();
    }

    public Set<String> getKeySet() {
        return dataStore.keySet();
    }

    private byte[] fieldsToBytes(List<Field> fieldList) {
        Bytes bytesOut = Bytes.allocateElasticDirect();

        Serializer.writeFieldList(bytesOut, fieldList);
        return bytesOut.toByteArray();
    }

    public void remove() {
        try {
//            dataStore.clear();
            if(mapDB != null) {
                dataStore.clear();
                mapDB.close();
            }
            Files.delete(Paths.get(tableName + ".db"));
            System.err.println("drop table: " + tableName);
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }

    public void close() {
        mapDB.close();
    }
}
