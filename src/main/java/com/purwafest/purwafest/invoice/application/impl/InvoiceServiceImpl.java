package com.purwafest.purwafest.invoice.application.impl;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.discount.domain.entities.Discount;
import com.purwafest.purwafest.discount.infrastructure.repository.DiscountRepository;
import com.purwafest.purwafest.event.application.TicketServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.event.infrastructure.repositories.EventTicketTypeRepository;
import com.purwafest.purwafest.invoice.application.InvoiceService;
import com.purwafest.purwafest.invoice.domain.contants.InvoiceConstants;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import com.purwafest.purwafest.invoice.domain.entities.InvoiceItems;
import com.purwafest.purwafest.invoice.domain.enums.PaymentStatus;
import com.purwafest.purwafest.invoice.infrastucture.repositories.InvoiceItemsRepository;
import com.purwafest.purwafest.invoice.infrastucture.repositories.InvoiceRepository;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceItemRequest;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceResponse;
import com.purwafest.purwafest.point.domain.entities.Point;
import com.purwafest.purwafest.point.domain.entities.PointHistory;
import com.purwafest.purwafest.point.infrastructure.repository.PointHistoryRepository;
import com.purwafest.purwafest.point.infrastructure.repository.PointRepository;
import com.purwafest.purwafest.point.presentation.dtos.PointUsageSummary;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {
  private final InvoiceRepository invoiceRepository;
  private final UserRepository userRepository;
  private final InvoiceItemsRepository invoiceItemsRepository;
  private final EventRepository eventRepository;
  private final EventTicketTypeRepository eventTicketTypeRepository;
  private final TicketServices ticketServices;
  private final PointRepository pointRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final DiscountRepository discountRepository;

  public InvoiceServiceImpl(InvoiceRepository invoiceRepository, UserRepository userRepository, InvoiceItemsRepository invoiceItemsRepository, EventRepository eventRepository, EventTicketTypeRepository eventTicketTypeRepository, TicketServices ticketServices, PointRepository pointRepository, PointHistoryRepository pointHistoryRepository, DiscountRepository discountRepository) {
    this.invoiceRepository = invoiceRepository;
    this.userRepository = userRepository;
    this.invoiceItemsRepository = invoiceItemsRepository;
    this.eventRepository = eventRepository;
    this.eventTicketTypeRepository = eventTicketTypeRepository;
    this.ticketServices = ticketServices;
    this.pointRepository = pointRepository;
    this.pointHistoryRepository = pointHistoryRepository;
    this.discountRepository = discountRepository;
  }

  @Override
  @Transactional
  public InvoiceResponse createInvoice(Integer eventID, List<InvoiceItemRequest> invoiceItemRequests, BigInteger points, Integer userID, Integer discountID) {
    Optional<User> user = userRepository.findById(userID);
    Optional<Event> event = eventRepository.findById(eventID);
    Optional<Discount> discount = discountRepository.findById(discountID);


    if (user.isEmpty()) {
      throw new IllegalArgumentException("User not found!");
    }

    if (event.isEmpty()) {
      throw new IllegalArgumentException("Event not found!");
    }

    if (discount.isEmpty()) {
      throw new IllegalArgumentException("Discount not found!");
    }

    // Step 1: create and save invoice to DB
    Invoice invoice = new Invoice();
    invoice.setUser(user.get());
    invoice.setEvent(event.get());
    invoice.setStatus(PaymentStatus.PAID);
    invoice.setInvoiceItems(new HashSet<>()); // initialize as empty
    invoice.setPaymentDate(Instant.now());
    invoice.setFees(InvoiceConstants.PAYMENT_FEE);
    invoice.setPaymentMethod(InvoiceConstants.PAYMENT_METHOD);
    invoice.setDiscount(discount.get());
    invoice = invoiceRepository.save(invoice); // save invoice first before invoice items

    Set<InvoiceItems> invoiceItemsSet = new HashSet<>();
    BigInteger amount = BigInteger.ZERO;

    // Step 2: create and save invoice items
    for (InvoiceItemRequest invoiceItemRequest : invoiceItemRequests) {
      InvoiceItems invoiceItems = new InvoiceItems();
      Optional<EventTicketType> eventTicketTypeOptional = eventTicketTypeRepository.findById(invoiceItemRequest.getEventTicketTypeID());

      if (eventTicketTypeOptional.isPresent()) {
        EventTicketType eventTicketType = eventTicketTypeOptional.get();

        BigInteger price = eventTicketType.getPrice();
        BigInteger qty = BigInteger.valueOf(invoiceItemRequest.getQty());
        BigInteger subtotal = price.multiply(qty);

        amount = amount.add(subtotal);

        invoiceItems.setEventTicketType(eventTicketType);
        invoiceItems.setQty(invoiceItemRequest.getQty());
        invoiceItems.setSubtotal(subtotal);
        invoiceItems.setInvoice(invoice);

        invoiceItems = invoiceItemsRepository.save(invoiceItems);
        invoiceItemsSet.add(invoiceItems);

        ticketServices.createTicket(invoiceItemRequest.getQty(), invoiceItemRequest.getEventTicketTypeID());
      }
    }

    // Step 3: Update invoice with invoiceItems and re-save
    invoice.setInvoiceItems(invoiceItemsSet);
    invoice.setAmount(amount);
    invoice = invoiceRepository.save(invoice);

    // Step 4: Handle point usage
    PointUsageSummary pointUsageSummary = handlePoints(amount, points, userID);

    invoice.setValuePointUsage(pointUsageSummary.getTotalUsedPoint());
    invoice.setRowAmountPointUsage(pointUsageSummary.getRowUsed());
    invoice.setFinalAmount(amount.subtract(pointUsageSummary.getTotalUsedPoint()));

    // Step 5: Save to point_history table
    PointHistory pointHistory = new PointHistory();
    pointHistory.setUser(user.get());
    pointHistory.setInvoice(invoice);
    pointHistory.setAmountUsed(pointUsageSummary.getTotalUsedPoint());
    pointHistoryRepository.save(pointHistory);

    return InvoiceResponse.toResponse(invoice);
  }

  @Override
  public List<InvoiceResponse> getAllInvoice() {
    Integer userID = Claims.getUserId();

    List<Invoice> invoiceList = invoiceRepository.findAllByUser_Id(userID);

    // return all invoice in invoice response
    List<InvoiceResponse> responses = new ArrayList<>();
    invoiceList.forEach(invoice -> {
      responses.add(InvoiceResponse.toResponse(invoice));
    });

    return responses;
  }

  public BigInteger getSubtotal(Integer qty, BigInteger price) {
    return BigInteger.valueOf(qty).multiply(price);
  }
  private PointUsageSummary handlePoints(BigInteger amount, BigInteger points, Integer userId) {
    List<Point> pointsList = pointRepository
            .findUsablePointsByUserIdOrderByExpiry(userId, Instant.now());

    BigInteger remainingAmountToCover = amount.min(points); // use max point up to amount
    BigInteger usedTotalPoint = BigInteger.ZERO;
    int usedRowCount = 0;

    for (Point point : pointsList) {
      BigInteger available = point.getAmount().subtract(point.getAmountUsed());
      if (available.compareTo(BigInteger.ZERO) <= 0) continue;

      BigInteger toUse = available.min(remainingAmountToCover);

      point.setAmountUsed(point.getAmountUsed().add(toUse));

      if (point.getAmount().equals(point.getAmountUsed())) {
        point.setIsRedeemed(true);
      }

      usedTotalPoint = usedTotalPoint.add(toUse);
      remainingAmountToCover = remainingAmountToCover.subtract(toUse);

      pointRepository.save(point);
      usedRowCount++;

      if (remainingAmountToCover.compareTo(BigInteger.ZERO) <= 0) break;
    }

    return new PointUsageSummary(usedTotalPoint, usedRowCount);
  }
}
