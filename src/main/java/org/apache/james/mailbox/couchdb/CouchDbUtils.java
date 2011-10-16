package org.apache.james.mailbox.couchdb;

import org.ektorp.*;
import org.ektorp.impl.*;
import org.ektorp.http.*;

/**
 * Created by IntelliJ IDEA.
 * User: pepijndevos
 * Date: 10/13/11
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CouchDbUtils {
    
    // TODO: a way to get user and password from configuration 
    HttpClient httpClient = new StdHttpClient.Builder()
    .username("admin")
    .password("sa")
    .build();
    
    CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
    // if the second parameter is true, the database will be created if it doesn't exists
    CouchDbConnector db = dbInstance.createConnector("james", true);

    public CouchDbConnector getDb() {
        return db;
    }

    public CouchDbInstance getDbInstance() {
        return dbInstance;
    }
}
