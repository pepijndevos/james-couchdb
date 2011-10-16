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

import org.apache.james.mailbox.AbstractMailboxManagerTest;
import org.apache.james.mailbox.BadCredentialsException;
import org.apache.james.mailbox.MailboxException;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.couchdb.mail.CouchDbMailboxManager;
import org.apache.james.mailbox.store.MockAuthenticator;
import org.junit.After;
import org.junit.Before;
import org.slf4j.LoggerFactory;

/**
 * CouchDbMailboxManagerTest that extends the MailboxManagerTest.
 */
public class CouchDbMailboxManagerTest extends AbstractMailboxManagerTest {
    
    /**
     * Setup the mailboxManager.
     * 
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        
        // TODO: database name should be configurable here and in Manager
        new CouchDbUtils().getDbInstance().deleteDatabase("james");
        
        createMailboxManager();
    }
    
    /**
     * Close the system session and entityManagerFactory
     * 
     * @throws MailboxException 
     * @throws BadCredentialsException 
     */
    @After
    public void tearDown() throws BadCredentialsException, MailboxException {
        MailboxSession session = getMailboxManager().createSystemSession("test", LoggerFactory.getLogger("Test"));
        session.close();
    }

    /* (non-Javadoc)
     * @see org.apache.james.mailbox.MailboxManagerTest#createMailboxManager()
     */
    @Override
    protected void createMailboxManager() throws MailboxException {
        
        CouchDbMailboxSessionMapperFactory factory = new CouchDbMailboxSessionMapperFactory();
        CouchDbMailboxManager mailboxManager = new CouchDbMailboxManager(factory, new MockAuthenticator());
        mailboxManager.init();
        
        setMailboxManager(mailboxManager);

    }
    
}
