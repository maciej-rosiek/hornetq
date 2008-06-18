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

package org.jboss.messaging.core.remoting.impl.wireformat;

import org.jboss.messaging.util.MessagingBuffer;

/**
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 * 
 * @version <tt>$Revision$</tt>
 */
public class SessionCreateProducerResponseMessage extends PacketImpl
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   private long producerTargetID;
   
   private int initialCredits;
   
   private int maxRate;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public SessionCreateProducerResponseMessage(final long producerTargetID, final int initialCredits, final int maxRate)
   {
      super(SESS_CREATEPRODUCER_RESP);

      this.producerTargetID = producerTargetID;
      
      this.initialCredits = initialCredits;
      
      this.maxRate = maxRate;
   }
   
   public SessionCreateProducerResponseMessage()
   {
      super(SESS_CREATEPRODUCER_RESP);
   }

   // Public --------------------------------------------------------

   public long getProducerTargetID()
   {
      return producerTargetID;
   }
   
   public int getInitialCredits()
   {
   	return initialCredits;
   }
   
   public int getMaxRate()
   {
   	return maxRate;
   }
   
   public void encodeBody(final MessagingBuffer buffer)
   {
      buffer.putLong(producerTargetID);
      buffer.putInt(initialCredits);
      buffer.putInt(maxRate);
   }
   
   public void decodeBody(final MessagingBuffer buffer)
   {
      producerTargetID = buffer.getLong();      
      initialCredits = buffer.getInt();
      maxRate = buffer.getInt();
   }
   

   @Override
   public String toString()
   {
      StringBuffer buf = new StringBuffer(getParentString());
      buf.append(", producerTargetID=" + producerTargetID);
      buf.append(", initialCredits=" + initialCredits);
      buf.append(", maxRate=" + maxRate);
      buf.append("]");
      return buf.toString();
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}
