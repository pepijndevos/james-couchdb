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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Flags;
import javax.mail.Flags.Flag;

import org.apache.james.mailbox.MailboxException;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.MessageMetaData;
import org.apache.james.mailbox.MessageRange;
import org.apache.james.mailbox.store.SimpleMessageMetaData;
import org.apache.james.mailbox.store.mail.AbstractMessageMapper;
import org.apache.james.mailbox.store.mail.ModSeqProvider;
import org.apache.james.mailbox.store.mail.UidProvider;
import org.apache.james.mailbox.store.mail.model.Mailbox;
import org.apache.james.mailbox.store.mail.model.Message;
import org.apache.james.mailbox.store.mail.model.impl.SimpleMessage;

public class CouchDbMessageMapper extends AbstractMessageMapper<String> {

    private Map<String, Map<String, Message<String>>> mailboxByUid;
    private static final int INITIAL_SIZE = 256;
    
    public CouchDbMessageMapper(MailboxSession session, UidProvider<String> uidProvider, ModSeqProvider<String> modSeqProvider) {
        super(session, uidProvider, modSeqProvider);
        this.mailboxByUid = new ConcurrentHashMap<String, Map<String, Message<String>>>(INITIAL_SIZE);
    }
    
    private Map<String, Message<String>> getMembershipByUidForMailbox(Mailbox<String> mailbox) {
        Map<String, Message<String>> membershipByUid = mailboxByUid.get(mailbox.getMailboxId());
        if (membershipByUid == null) {
            membershipByUid = new ConcurrentHashMap<String, Message<String>>(INITIAL_SIZE);
            mailboxByUid.put(mailbox.getMailboxId(), membershipByUid);
        }
        return membershipByUid;
    }
    
    /**
     * @see org.apache.james.mailbox.store.mail.MessageMapper#countMessagesInMailbox(org.apache.james.mailbox.store.mail.model.Mailbox)
     */
    public long countMessagesInMailbox(Mailbox<String> mailbox) throws MailboxException {
        return getMembershipByUidForMailbox(mailbox).size();
    }

    /**
     * @see org.apache.james.mailbox.store.mail.MessageMapper#countUnseenMessagesInMailbox(org.apache.james.mailbox.store.mail.model.Mailbox)
     */
    public long countUnseenMessagesInMailbox(Mailbox<String> mailbox) throws MailboxException {
        long count = 0;
        for(Message<String> member:getMembershipByUidForMailbox(mailbox).values()) {
            if (!member.isSeen()) {
                count++;
            }
        }
        return count;
    }

    /**
     * @see org.apache.james.mailbox.store.mail.MessageMapper#delete(org.apache.james.mailbox.store.mail.model.Mailbox,
     * org.apache.james.mailbox.store.mail.model.Message)
     */
    public void delete(Mailbox<String> mailbox, Message<String> message) throws MailboxException {
        getMembershipByUidForMailbox(mailbox).remove(message.getUid());
    }


    /**
     * @see org.apache.james.mailbox.store.mail.MessageMapper#findInMailbox(org.apache.james.mailbox.store.mail.model.Mailbox, org.apache.james.mailbox.MessageRange, org.apache.james.mailbox.store.mail.MessageMapper.FetchType, int)
     */
    public Iterator<Message<String>> findInMailbox(Mailbox<String> mailbox, MessageRange set, FetchType ftype, int max) throws MailboxException {
        List<Message<String>> results;
        final MessageRange.Type type = set.getType();
        switch (type) {
            case ALL:
                results = new ArrayList<Message<String>>(getMembershipByUidForMailbox(mailbox).values());
                break;
            case FROM:
                results = new ArrayList<Message<String>>(getMembershipByUidForMailbox(mailbox).values());
                for (final Iterator<Message<String>> it=results.iterator();it.hasNext();) {
                   if (it.next().getUid()< set.getUidFrom()) {
                       it.remove(); 
                   }
                }
                break;
            case RANGE:
                results = new ArrayList<Message<String>>(getMembershipByUidForMailbox(mailbox).values());
                for (final Iterator<Message<String>> it=results.iterator();it.hasNext();) {
                   final long uid = it.next().getUid();
                if (uid<set.getUidFrom() || uid>set.getUidTo()) {
                       it.remove(); 
                   }
                }
                break;
            case ONE:
                results  = new ArrayList<Message<String>>(1);
                final Message<String> member = getMembershipByUidForMailbox(mailbox).get(set.getUidFrom());
                if (member != null) {
                    results.add(member);
                }
                break;
            default:
                results = new ArrayList<Message<String>>();
                break;
        }
        Collections.sort(results);
        
        if (max > 0 && results.size() > max) {
            results = results.subList(0, max -1);
        }
        return results.iterator();
    }
    

    /**
     * @see org.apache.james.mailbox.store.mail.MessageMapper#findRecentMessageUidsInMailbox(org.apache.james.mailbox.store.mail.model.Mailbox)
     */
    public List<Long> findRecentMessageUidsInMailbox(Mailbox<String> mailbox) throws MailboxException {
        final List<Long> results = new ArrayList<Long>();
        for(Message<String> member : getMembershipByUidForMailbox(mailbox).values()) {
            if (member.isRecent()) {
                results.add(member.getUid());
            }
        }
        Collections.sort(results);
        return results;
    }

    /**
     * @see org.apache.james.mailbox.store.mail.MessageMapper#findFirstUnseenMessageUid(org.apache.james.mailbox.store.mail.model.Mailbox)
     */
    public Long findFirstUnseenMessageUid(Mailbox<String> mailbox) throws MailboxException {
        List<Message<String>> memberships = new ArrayList<Message<String>>(getMembershipByUidForMailbox(mailbox).values());
        Collections.sort(memberships);
        for (int i = 0;  i < memberships.size(); i++) {
            Message<String> m = memberships.get(i);
            if (m.isSeen() == false) {
                return m.getUid();
            }
        }
        return null;
    }

    public void deleteAll() {
        mailboxByUid.clear();
    }

    /**
     * Do nothing
     */
    public void endRequest() {
        // Do nothing
    }

    /**
     * @see org.apache.james.mailbox.store.mail.AbstractMessageMapper#copy(org.apache.james.mailbox.store.mail.model.Mailbox, long, long, org.apache.james.mailbox.store.mail.model.Message)
     */
    protected MessageMetaData copy(Mailbox<String> mailbox, long uid, long modSeq, Message<String> original) throws MailboxException {
        SimpleMessage<String> message = new SimpleMessage<String>(mailbox, original);
        message.setUid(uid);
        message.setModSeq(modSeq);
        Flags flags = original.createFlags();
        
        // Mark message as recent as it is a copy
        flags.add(Flag.RECENT);
        message.setFlags(flags);
        return save(mailbox, message);
    }

    /**
     * @see org.apache.james.mailbox.store.mail.AbstractMessageMapper#save(org.apache.james.mailbox.store.mail.model.Mailbox, org.apache.james.mailbox.store.mail.model.Message)
     */
    protected MessageMetaData save(Mailbox<String> mailbox, Message<String> message) throws MailboxException {
        SimpleMessage<String> copy = new SimpleMessage<String>(mailbox, message);
        copy.setUid(message.getUid());
        copy.setModSeq(message.getModSeq());
        getMembershipByUidForMailbox(mailbox).put(message.getMailboxId(), copy);
        
        return new SimpleMessageMetaData(message);
    }

    /**
     * Do nothing
     */
    protected void begin() throws MailboxException {
        
    }

    /**
     * Do nothing
     */
    protected void commit() throws MailboxException {
        
    }


    /**
     * Do nothing
     */
    protected void rollback() throws MailboxException {        
    }

    @Override
    public Map<Long, MessageMetaData> expungeMarkedForDeletionInMailbox(final Mailbox<String> mailbox, MessageRange set) throws MailboxException {
        final Map<Long, MessageMetaData> filteredResult = new HashMap<Long, MessageMetaData>();

        Iterator<Message<String>> it = findInMailbox(mailbox, set, FetchType.Metadata, -1);
        while(it.hasNext()) {
            Message<String> member = it.next();
            if (member.isDeleted()) {
                filteredResult.put(member.getUid(), new SimpleMessageMetaData(member));

                delete(mailbox, member);
            }
        }
        return filteredResult;
    }
}
