/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.james.mailbox.couchdb.mail;

import org.apache.james.mailbox.MailboxException;
import org.apache.james.mailbox.MailboxNotFoundException;
import org.apache.james.mailbox.MailboxPath;
import org.apache.james.mailbox.couchdb.CouchDbUtils;
import org.apache.james.mailbox.couchdb.mail.model.CouchDbMailbox;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.model.Mailbox;
import org.ektorp.ComplexKey;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

import java.util.List;

@View(name = "all", map = "function(doc) { if (doc.user && doc.name && doc.namespace) emit( null, doc._id )}")
public class CouchDbMailboxMapper extends CouchDbRepositorySupport<CouchDbMailbox> implements MailboxMapper<String> {

    public CouchDbMailboxMapper() {
        super(CouchDbMailbox.class, new CouchDbUtils().getDb());
    }

    /**
     * @see org.apache.james.mailbox.store.mail.MailboxMapper#delete(org.apache.james.mailbox.store.mail.model.Mailbox)
     */
    public void delete(Mailbox<String> mailbox) throws MailboxException {
        remove((CouchDbMailbox) mailbox);
    }
    

    @View(name = "by_mailbox_path", map = "function(doc) { emit([doc.name, doc.user, doc.namespace], doc); }")
    public List<CouchDbMailbox> findByMailboxPath(String namespace, String user, String name) {
        ComplexKey key = ComplexKey.of(name, user, namespace);
        return queryView("by_mailbox_path", key);
    }
    
    /**
     * @see org.apache.james.mailbox.store.mail.MailboxMapper#findMailboxByPath(org.apache.james.mailbox.MailboxPath)
     */
    public Mailbox<String> findMailboxByPath(MailboxPath path) throws MailboxException, MailboxNotFoundException {
        List<CouchDbMailbox> result = findByMailboxPath(path.getNamespace(), path.getUser(), path.getName());
        if(result.isEmpty()) {
            throw new MailboxNotFoundException(path);
        }
        return result.get(0);
    }

    /**
     * @see org.apache.james.mailbox.store.mail.MailboxMapper#findMailboxWithPathLike(org.apache.james.mailbox.MailboxPath)
     */
    public List<Mailbox<String>> findMailboxWithPathLike(MailboxPath path) throws MailboxException {
//        final String regex = path.getName().replace("%", ".*");
//        List<Mailbox<String>> results = new ArrayList<Mailbox<String>>();
//        for (final Mailbox<String> mailbox:mailboxesById.values()) {
//            if (mailbox.getName().matches(regex)) {
//                results.add(mailbox);
//            }
//        }
//        return results;
        throw new MailboxException("TODO");
    }

    /**
     * @see org.apache.james.mailbox.store.mail.MailboxMapper#save(org.apache.james.mailbox.store.mail.model.Mailbox)
     */
    public void save(Mailbox<String> mailbox) throws MailboxException {
        // I thought update did this on its own.
        if(mailbox.getMailboxId() != null) {
            update((CouchDbMailbox) mailbox);
        } else {
            add((CouchDbMailbox) mailbox);
        }
    }

    /**
     * Do nothing
     */
    public void endRequest() {
        // Do nothing
    }

    /**
     * @see org.apache.james.mailbox.store.mail.MailboxMapper#hasChildren(org.apache.james.mailbox.store.mail.model.Mailbox, char)
     */
    public boolean hasChildren(Mailbox<String> mailbox, char delimiter) throws MailboxException,
            MailboxNotFoundException {
        String mailboxName = mailbox.getName() + delimiter;
// TODO:        
//        for (final Mailbox<String> box:mailboxesById.values()) {
//            if (box.getName().startsWith(mailboxName)) {
//                return true;
//            }
//        }
        return false;
    }

    /**
     * @see org.apache.james.mailbox.store.mail.MailboxMapper#list()
     */
    public List<Mailbox<String>> list() throws MailboxException {
        List<?> ret = getAll();
        return (List<Mailbox<String>>) ret;
    }

    public <T> T execute(Transaction<T> transaction) throws MailboxException {
        return transaction.run();
    }
}
