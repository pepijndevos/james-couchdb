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
package org.apache.james.mailbox.couchdb;

import org.apache.james.mailbox.MailboxException;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.SubscriptionException;
import org.apache.james.mailbox.couchdb.mail.CouchDbMailboxMapper;
import org.apache.james.mailbox.couchdb.mail.CouchDbMessageMapper;
import org.apache.james.mailbox.couchdb.mail.CouchDbModSeqProvider;
import org.apache.james.mailbox.couchdb.mail.CouchDbUidProvider;
import org.apache.james.mailbox.couchdb.user.CouchDbSubscriptionMapper;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mailbox.store.user.SubscriptionMapper;

public class CouchDbMailboxSessionMapperFactory extends MailboxSessionMapperFactory<String> {

    private MailboxMapper<String> mailboxMapper;
    private MessageMapper<String> messageMapper;
    private SubscriptionMapper subscriptionMapper;
    
    public CouchDbMailboxSessionMapperFactory() {
        mailboxMapper = new CouchDbMailboxMapper();
        messageMapper = new CouchDbMessageMapper(null, new CouchDbUidProvider(), new CouchDbModSeqProvider());
        subscriptionMapper = new CouchDbSubscriptionMapper();
    }
    
    @Override
    public MailboxMapper<String> createMailboxMapper(MailboxSession session) throws MailboxException {
        return mailboxMapper;
    }

    @Override
    public MessageMapper<String> createMessageMapper(MailboxSession session) throws MailboxException {
        return messageMapper;
    }

    @Override
    public SubscriptionMapper createSubscriptionMapper(MailboxSession session) throws SubscriptionException {
        return subscriptionMapper;
    }

}
