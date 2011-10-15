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
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.couchdb.mail.model.CouchDbMailbox;
import org.apache.james.mailbox.store.mail.UidProvider;
import org.apache.james.mailbox.store.mail.model.Mailbox;

public class CouchDbUidProvider implements UidProvider<String>{

    private CouchDbMailboxMapper mailboxMapper = new CouchDbMailboxMapper();
    
    @Override
    public long nextUid(MailboxSession session, Mailbox<String> mailbox) throws MailboxException {
        CouchDbMailbox couchMailbox = (CouchDbMailbox) mailbox;

        while(true) {
            ((CouchDbMailbox) mailbox).incrementLastUid();
            try {
                mailboxMapper.save(couchMailbox);
                return couchMailbox.getLastUid();
            } catch (MailboxException e) {
                couchMailbox = mailboxMapper.get(couchMailbox.getId());
            }
        }
    }

    @Override
    public long lastUid(MailboxSession session, Mailbox<String> mailbox) throws MailboxException {
        return ((CouchDbMailbox) mailbox).getLastUid();
    }


}
