/*
   * JBoss, Home of Professional Open Source
   * Copyright 2005-2008, Red Hat Middleware LLC, and individual contributors
   * by the @authors tag. See the copyright.txt in the distribution for a
   * full listing of individual contributors.
   *
   * This is free software; you can redistribute it and/or modify it
   * under the terms of the GNU Lesser General Public License as
   * published by the Free Software Foundation; either version 2.1 of
   * the License, or (at your option) any later version.
   *
   * This software is distributed in the hope that it will be useful,
   * but WITHOUT ANY WARRANTY; without even the implied warranty of
   * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
   * Lesser General Public License for more details.
   *
   * You should have received a copy of the GNU Lesser General Public
   * License along with this software; if not, write to the Free
   * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
   * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
   */
package org.jboss.messaging.example;

import java.util.Set;

import org.jboss.messaging.core.client.ClientConnection;
import org.jboss.messaging.core.client.ClientConnectionFactory;
import org.jboss.messaging.core.client.ClientConsumer;
import org.jboss.messaging.core.client.ClientMessage;
import org.jboss.messaging.core.client.ClientProducer;
import org.jboss.messaging.core.client.ClientSession;
import org.jboss.messaging.core.client.ConnectionParams;
import org.jboss.messaging.core.client.Location;
import org.jboss.messaging.core.client.impl.ClientConnectionFactoryImpl;
import org.jboss.messaging.core.client.impl.ClientMessageImpl;
import org.jboss.messaging.core.client.impl.ConnectionParamsImpl;
import org.jboss.messaging.core.client.impl.LocationImpl;
import org.jboss.messaging.core.config.impl.ConfigurationImpl;
import org.jboss.messaging.core.exception.MessagingException;
import org.jboss.messaging.core.message.Message;
import org.jboss.messaging.core.remoting.TransportType;
import org.jboss.messaging.core.security.CheckType;
import org.jboss.messaging.core.security.JBMSecurityManager;
import org.jboss.messaging.core.security.Role;
import org.jboss.messaging.core.server.MessagingService;
import org.jboss.messaging.core.server.impl.MessagingServerImpl;
import org.jboss.messaging.core.server.impl.MessagingServiceImpl;
import org.jboss.messaging.jms.client.JBossTextMessage;
import org.jboss.messaging.util.SimpleString;

/**
 * A simple server is started with TCP transport, a message is sent and received.
 * 
 * @author <a href="ataylor@redhat.com">Andy Taylor</a>
 */
public class SimpleExample
{
   public static void main(final String[] args) throws Exception
   {
      MessagingService messagingService = null;
      ClientConnection clientConnection = null;

      try
      {
         //create a new server with an TCP transport
         ConfigurationImpl configuration = new ConfigurationImpl();
         configuration.setTransport(TransportType.TCP);
         configuration.setHost("localhost");
         messagingService = MessagingServiceImpl.newNullStorageMessagingServer(configuration);
         //start the server
         messagingService.start();
         //add a new binding
         
         SimpleString atestq = new SimpleString("atestq");
         
         messagingService.getServer().getPostOffice().addBinding(atestq, atestq, null, false, false);

         //then we create a client as normal
         Location location = new LocationImpl(TransportType.TCP, "localhost", 5400);
         ConnectionParams connectionParams = new ConnectionParamsImpl();
         ClientConnectionFactory connectionFactory = new ClientConnectionFactoryImpl(location, connectionParams);

         clientConnection = connectionFactory.createConnection();
         ClientSession clientSession = clientConnection.createClientSession(false, true, true, 100, true, false);
         ClientProducer clientProducer = clientSession.createProducer(atestq);
         ClientMessage message = new ClientMessageImpl(JBossTextMessage.TYPE, false, 0,
                 System.currentTimeMillis(), (byte) 1);
         message.getBody().putString("Hello!");
         clientProducer.send(message);
         ClientConsumer clientConsumer = clientSession.createConsumer(atestq);
         clientConnection.start();
         Message msg = clientConsumer.receive(5000);
         System.out.println("msg.getPayload() = " + msg.getBody().getString());
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         if (clientConnection != null)
         {
            try
            {
               clientConnection.close();
            }
            catch (MessagingException e1)
            {
               //
            }
         }
         if (messagingService != null && messagingService.isStarted())
         {
            try
            {
               messagingService.stop();
            }
            catch (Exception e1)
            {
               //
            }
         }
      }
   }
}
