/**
 * Copyright (C) 2009 Lambico Team <lucio.benfante@gmail.com>
 *
 * This file is part of Lambico Test.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lambico.test.spring;

import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Mock implementation of JavaMailSender for test porpouse.
 *
 * @author Enrico Giurin
 * @author Lucio Benfante
 * @version $Revision$
 */
public class MailSenderMock extends JavaMailSenderImpl {

    /** The logger for this class. */
    private static Logger logger = Logger.getLogger(
            MailSenderMock.class);

    /**
     * {@inheritDoc}
     *
     * @param mimeMessages {@inheritDoc}
     * @param originalMessages {@inheritDoc}
     */
    @Override
    protected void doSend(final MimeMessage[] mimeMessages, final Object[] originalMessages) {
        if (mimeMessages != null) {
            for (MimeMessage mimeMessage : mimeMessages) {
                try {
                    logger.info(mimeMessage.getContent());
                } catch (Exception ex) {
                    logger.error("Can't get message content", ex);
                }
            }
        }
        if (originalMessages != null) {
            for (Object o : originalMessages) {
                logger.info(o);
            }
        }
    }
}
