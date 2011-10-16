package org.apache.james.mailbox.couchdb.mail.model;

import org.apache.james.mailbox.MailboxPath;
import org.apache.james.mailbox.store.mail.model.Mailbox;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.CouchDbDocument;


public class CouchDbMailbox extends CouchDbDocument implements Mailbox<String> {
    private static final long serialVersionUID = -5637388554102567999L;

    private String id;
    private String revision;
    private String user;
    private String namespace;
    private String name;
    private long uidValidity;
    private long highestModSeq;
    private long lastUid;

    public CouchDbMailbox(){}

    public CouchDbMailbox(MailboxPath path, long uidValidity) {
        this.uidValidity = uidValidity;
        this.user = path.getUser();
        this.namespace = path.getNamespace();
        this.name = path.getName();
        this.highestModSeq = 0;
        this.lastUid = 0;
    }

    public long getHighestModSeq() {
        return highestModSeq;
    }

    public String getId() {
        return id;
    }

    public long getLastUid() {
        return lastUid;
    }

    @JsonIgnore
    public String getMailboxId() {
        return getId();
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getRevision() {
        return revision;
    }

    public long getUidValidity() {
        return uidValidity;
    }

    public String getUser() {
        return user;
    }

    @JsonIgnore
    public void incrementHighestModSeq() {
        this.highestModSeq++;
    }

    @JsonIgnore
    public void incrementLastUid() {
        this.lastUid++;
    }

    public void setHighestModSeq(long highestModSeq) {
        this.highestModSeq = highestModSeq;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void setLastUid(long lastUid) {
        this.lastUid = lastUid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public void setUidValidity(long uidValidity) {
        this.uidValidity = uidValidity;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
