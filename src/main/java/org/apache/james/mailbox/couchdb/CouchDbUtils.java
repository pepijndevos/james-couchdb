package org.apache.james.mailbox.couchdb;

import org.apache.james.mailbox.store.mail.model.Mailbox;
import org.codehaus.jackson.annotate.JsonProperty;
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
    HttpClient httpClient = new StdHttpClient.Builder().build();
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
