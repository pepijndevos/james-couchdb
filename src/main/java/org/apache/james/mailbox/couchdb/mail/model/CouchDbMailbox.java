package org.apache.james.mailbox.couchdb.mail.model;

import org.apache.james.mailbox.MailboxPath;
import org.apache.james.mailbox.store.mail.model.Mailbox;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
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

    @JsonCreator
    public CouchDbMailbox(
            @JsonProperty("_id") String id,
            @JsonProperty("_rev") String revision,
            @JsonProperty("user") String user,
            @JsonProperty("namespace") String namespace,
            @JsonProperty("name") String name,
            @JsonProperty("uidValidity") long uidValidity,
            @JsonProperty("highestModSeq") long highestModSeq,
            @JsonProperty("lastUid") long lastUid) {
        this.id = id;
        this.revision = revision;

        this.user = user;
        this.namespace = namespace;
        this.name = name;
        this.uidValidity = uidValidity;
        this.highestModSeq = highestModSeq;
        this.lastUid = lastUid;
    }

    public CouchDbMailbox(MailboxPath path, long uidValidity) {
        this.uidValidity = uidValidity;
        this.user = path.getUser();
        this.namespace = path.getNamespace();
        this.name = path.getName();
        this.highestModSeq = 0;
        this.lastUid = 0;
    }

    @JsonIgnore
    public String getMailboxId() {
        return getId();
    }

    public long getHighestModSeq() {
        return highestModSeq;
    }

    @JsonIgnore
    public void incrementLastUid() {
        this.lastUid++;
    }

    @JsonIgnore
    public void incrementHighestModSeq() {
        this.highestModSeq++;
    }

    public long getLastUid() {
        return lastUid;
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
