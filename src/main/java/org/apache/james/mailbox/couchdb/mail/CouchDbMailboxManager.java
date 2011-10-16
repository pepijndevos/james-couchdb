package org.apache.james.mailbox.couchdb.mail;

import org.apache.james.mailbox.MailboxException;
import org.apache.james.mailbox.MailboxPath;
import org.apache.james.mailbox.MailboxPathLocker;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.couchdb.mail.model.CouchDbMailbox;
import org.apache.james.mailbox.store.Authenticator;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.StoreMailboxManager;
import org.apache.james.mailbox.store.mail.model.Mailbox;


public class CouchDbMailboxManager extends StoreMailboxManager<String> {


    public CouchDbMailboxManager(MailboxSessionMapperFactory<String> mailboxSessionMapperFactory, Authenticator authenticator, MailboxPathLocker locker) {
        super(mailboxSessionMapperFactory, authenticator, locker);
    }

    public CouchDbMailboxManager(MailboxSessionMapperFactory<String> mailboxSessionMapperFactory, Authenticator authenticator) {
        super(mailboxSessionMapperFactory, authenticator);
    }

//    @Override
//    protected StoreMessageManager<String> createMessageManager(Mailbox<String> mailboxRow, MailboxSession session) throws MailboxException {
//        return new CouchDvMessageManager(getMapperFactory(), getMessageSearchIndex(), getEventDispatcher(), getLocker(), mailboxRow);
//    }

    @Override
    protected Mailbox<String> doCreateMailbox(MailboxPath path, MailboxSession session) throws MailboxException {
        Mailbox<String> mailbox =  new CouchDbMailbox(path, randomUidValidity());
        getMapperFactory().getMailboxMapper(session).save(mailbox);
        return mailbox;
    }
}
