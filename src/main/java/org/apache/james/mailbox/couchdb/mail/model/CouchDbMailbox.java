package org.apache.james.mailbox.couchdb.mail.model;

import org.apache.james.mailbox.store.mail.model.Mailbox;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

/**
 * Created by IntelliJ IDEA.
 * User: pepijndevos
 * Date: 10/13/11
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class CouchDbMailbox extends CouchDbDocument implements Mailbox<String> {
    private static final long serialVersionUID = -5637388554102567999L;
    
    private String user;
    private String namespace;
    private String name;
    private long uidValidity;

    @JsonIgnore
    public String getMailboxId() {
        return getId();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNamespace() {
        return namespace;
    }

    @TypeDiscriminator
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUidValidity() {
        return uidValidity;
    }
}
