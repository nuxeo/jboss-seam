//$Id: BookingListAction.java,v 1.1 2007/06/23 18:33:59 pmuir Exp $
package org.jboss.seam.example.booking;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@Stateful
@Scope(SESSION)
@Name("bookingList")
// @LoggedIn
@TransactionAttribute(REQUIRES_NEW)
public class BookingListAction implements BookingList, Serializable
{
   
   @PersistenceContext
   private EntityManager em;
   
   @In
   private User user;
   
   @DataModel
   private List<Booking> bookings;
   @DataModelSelection 
   private Booking booking;
   
   @Logger 
   private Log log;
   
   @Factory
   @Observer("bookingConfirmed")
   public void getBookings()
   {
      bookings = em.createQuery("select b from Booking b where b.user.username = :username order by b.checkinDate")
            .setParameter("username", user.getUsername())
            .getResultList();
   }
   
   public void cancel()
   {
      log.info("Cancel booking: #{bookingList.booking.id} for #{user.username}");
      Booking cancelled = em.find(Booking.class, booking.getId());
      if (cancelled!=null) em.remove( cancelled );
      getBookings();
      FacesMessages.instance().add("Booking cancelled for confirmation number #0", booking.getId());
   }
   
   public Booking getBooking()
   {
      return booking;
   }
   
   @Destroy @Remove
   public void destroy() {}

}