package com.purwafest.purwafest.invoice.application.impl;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.event.application.TicketServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.event.infrastructure.repositories.EventTicketTypeRepository;
import com.purwafest.purwafest.invoice.application.InvoiceService;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import com.purwafest.purwafest.invoice.domain.entities.InvoiceItems;
import com.purwafest.purwafest.invoice.domain.enums.PaymentStatus;
import com.purwafest.purwafest.invoice.infrastucture.repositories.InvoiceItemsRepository;
import com.purwafest.purwafest.invoice.infrastucture.repositories.InvoiceRepository;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceItemRequest;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceResponse;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {
  private final InvoiceRepository invoiceRepository;
  private final UserRepository userRepository;
  private final InvoiceItemsRepository invoiceItemsRepository;
  private final EventRepository eventRepository;
  private final EventTicketTypeRepository eventTicketTypeRepository;
  private final TicketServices ticketServices;

  public InvoiceServiceImpl (InvoiceRepository invoiceRepository, UserRepository userRepository, InvoiceItemsRepository invoiceItemsRepository, EventRepository eventRepository, EventTicketTypeRepository eventTicketTypeRepository, TicketServices ticketServices) {
    this.invoiceRepository = invoiceRepository;
    this.userRepository = userRepository;
    this.invoiceItemsRepository = invoiceItemsRepository;
    this.eventRepository = eventRepository;
    this.eventTicketTypeRepository = eventTicketTypeRepository;
    this.ticketServices = ticketServices;
  }

  @Override
  public InvoiceResponse createInvoice(Integer eventID, List<InvoiceItemRequest> invoiceItemRequests, Integer userID) {
    Optional<User> user = userRepository.findById(userID);
    Optional<Event> event = eventRepository.findById(eventID);

    if (user.isEmpty()) {
      throw new IllegalArgumentException("User not found!");
    }

    if (event.isEmpty()) {
      throw new IllegalArgumentException("Event not found!");
    }

// Create invoice
    Invoice invoice = new Invoice();
    invoice.setUser(user.get());
    invoice.setEvent(event.get());
    invoice.setStatus(PaymentStatus.PAID);
    invoiceRepository.save(invoice);

        Set<InvoiceItems> invoiceItemsSet = new HashSet<>();
    System.out.println(invoiceItemRequests + " invoice test");
        invoiceItemRequests.forEach(invoiceItemRequest -> {

          InvoiceItems invoiceItems = new InvoiceItems();
          System.out.println(invoiceItems + " invoice");
          Optional<EventTicketType> eventTicketTypeOptional = eventTicketTypeRepository.findById(invoiceItemRequest.getEventTicketTypeID());

          if (eventTicketTypeOptional.isPresent()) {
            invoiceItems.setEventTicketType(eventTicketTypeOptional.get());
            invoiceItems.setQty(invoiceItemRequest.getQty());
            invoiceItems.setInvoice(invoice);
            invoiceItems.setSubtotal(getSubtotal(invoiceItemRequest.getQty(), eventTicketTypeOptional.get().getPrice()));
            ticketServices.createTicket(invoiceItemRequest.getQty(), invoiceItemRequest.getEventTicketTypeID());
            invoiceItemsSet.add(invoiceItems);
            invoiceItemsRepository.save(invoiceItems);
          }
        });

// save invoice item list to invoice
    invoice.setInvoiceItems(invoiceItemsSet);

    return InvoiceResponse.toResponse(invoice);
  }

  @Override
  public List<InvoiceResponse> getAllInvoice() {
    Integer userID = Claims.getUserId();

    List<Invoice> invoiceList = invoiceRepository.findAllByUser_Id(userID);

    List<InvoiceResponse> responses = new ArrayList<>();
    invoiceList.forEach(invoice -> {
      responses.add(InvoiceResponse.toResponse(invoice));
    });

    return responses;
  }

  public BigInteger getSubtotal(Integer qty, BigInteger price) {
    return  BigInteger.valueOf(qty).multiply(price);
  }

}
