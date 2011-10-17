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

import org.apache.james.mailbox.AbstractStressTest;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.couchdb.mail.CouchDbMailboxManager;
import org.apache.james.mailbox.store.MockAuthenticator;
import org.junit.After;
import org.junit.Before;
import org.slf4j.LoggerFactory;

/**
 * CouchDbStressTest that extends the AbstractStressTest.
 */
public class CouchDbStressTest extends AbstractStressTest {
    
    private CouchDbMailboxManager mailboxManager; 
    
    @Before
    public void setup() throws Exception {
        new CouchDbUtils().getDbInstance().deleteDatabase("james");
        CouchDbMailboxSessionMapperFactory factory = new CouchDbMailboxSessionMapperFactory();
        mailboxManager = new CouchDbMailboxManager(factory, new MockAuthenticator());
        mailboxManager.init();
    }
    
    @After
    public void tearDown() throws Exception {
        MailboxSession session = getMailboxManager().createSystemSession("test", LoggerFactory.getLogger("Test"));
        session.close();
    }
    
    @Override
    protected MailboxManager getMailboxManager()  {
        return mailboxManager;
    }
    
}
