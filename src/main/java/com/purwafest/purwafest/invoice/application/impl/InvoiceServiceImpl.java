package com.purwafest.purwafest.invoice.application.impl;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import com.purwafest.purwafest.common.PaginatedResponse;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.discount.domain.entities.Discount;
import com.purwafest.purwafest.discount.infrastructure.repository.DiscountRepository;
import com.purwafest.purwafest.event.application.TicketServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.domain.enums.PromotionType;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.event.infrastructure.repositories.EventTicketTypeRepository;
import com.purwafest.purwafest.event.presentation.dtos.EventListResponse;
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
import com.purwafest.purwafest.promotion.domain.entities.Promotion;
import com.purwafest.purwafest.promotion.infrastructure.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  private final PromotionRepository promotionRepository;

  public InvoiceServiceImpl(InvoiceRepository invoiceRepository, UserRepository userRepository, InvoiceItemsRepository invoiceItemsRepository, EventRepository eventRepository, EventTicketTypeRepository eventTicketTypeRepository, TicketServices ticketServices, PointRepository pointRepository, PointHistoryRepository pointHistoryRepository, DiscountRepository discountRepository, PromotionRepository promotionRepository) {
    this.invoiceRepository = invoiceRepository;
    this.userRepository = userRepository;
    this.invoiceItemsRepository = invoiceItemsRepository;
    this.eventRepository = eventRepository;
    this.eventTicketTypeRepository = eventTicketTypeRepository;
    this.ticketServices = ticketServices;
    this.pointRepository = pointRepository;
    this.pointHistoryRepository = pointHistoryRepository;
    this.discountRepository = discountRepository;
    this.promotionRepository = promotionRepository;
  }

  @Override
  @Transactional
  public InvoiceResponse createInvoice(Integer eventID, List<InvoiceItemRequest> invoiceItemRequests, BigInteger points, Integer userID, Integer promotionID) {
    Optional<User> userOptional = userRepository.findById(userID);
    Optional<Event> eventOptional = eventRepository.findById(eventID);
    Optional<Promotion> promotionOptional = promotionRepository.findById(promotionID);

    // Check if userOptional use promotionOptional or point
    if (!(promotionID == 0)) {
      if (promotionOptional.isEmpty()) {
        throw new IllegalArgumentException("Promotion not found!");
      }
    }

    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("User not found!");
    }

    if (eventOptional.isEmpty()) {
      throw new IllegalArgumentException("Event not found!");
    }

    // Step 1: create and save invoice to DB
    Invoice invoice = new Invoice();
    invoice.setUser(userOptional.get());
    invoice.setEvent(eventOptional.get());
    invoice.setStatus(PaymentStatus.PAID);
    invoice.setInvoiceItems(new HashSet<>()); // initialize as empty
    invoice.setPaymentDate(Instant.now());
    invoice.setFees(InvoiceConstants.PAYMENT_FEE);
    invoice.setPaymentMethod(InvoiceConstants.PAYMENT_METHOD);

    if (promotionOptional.isPresent()) {
      invoice.setPromotion(promotionOptional.get());
    }

    invoice = invoiceRepository.save(invoice); // save invoice first before invoice items

    Set<InvoiceItems> invoiceItemsSet = new HashSet<>();
    BigInteger amount = BigInteger.ZERO;

    // Step 2: created and save invoice items
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

    // Step 4: Handle discount usage
    if (promotionOptional.isPresent()) {
      Promotion promotion = promotionOptional.get();

      if (promotion.isReferralPromotion()) {
        Discount discount = discountRepository.findDiscountByPromotionIDAndUserID(promotionID, userID);
        if(discount.isUsed()){
          throw new IllegalArgumentException("Discount referral on this user is already used!");
        }
        discount.setUsed(true);
      } else {
        UUID uid = UUID.randomUUID();

        Discount discount = Discount.builder()
            .promotion(promotion)
            .isUsed(true)
            .user(userOptional.get())
            .codeVoucher(uid)
            .build();

        discountRepository.save(discount);

        promotion.setUsageCount(promotion.getUsageCount() + 1);
      }

      if (promotion.getType().equals(PromotionType.NOMINAL)) {
        amount = amount.subtract(promotion.getValue());
      } else {
        amount = amount.subtract(amount.multiply(promotion.getValue()).divide(BigInteger.valueOf(100)));
      }
    }


    // Step 5: Handle point usage
    PointUsageSummary pointUsageSummary = handlePoints(amount, points, userID);

    invoice.setValuePointUsage(pointUsageSummary.getTotalUsedPoint());
    invoice.setRowAmountPointUsage(pointUsageSummary.getRowUsed());
    amount = amount.add(invoice.getFees());
    invoice.setFinalAmount(amount.subtract(pointUsageSummary.getTotalUsedPoint()));

    // Step 6: Save to point_history table
    PointHistory pointHistory = new PointHistory();
    pointHistory.setUser(userOptional.get());
    pointHistory.setInvoice(invoice);
    pointHistory.setAmountUsed(pointUsageSummary.getTotalUsedPoint());
    pointHistoryRepository.save(pointHistory);

    return InvoiceResponse.toResponse(invoice);
  }

  @Override
  public PaginatedResponse<InvoiceResponse> getAllInvoice(Pageable pageable) {
    Integer userID = Claims.getUserId();

    Page<Invoice> invoicePage = invoiceRepository.findAllByUser_Id(userID, pageable).map(invoice -> invoice);

    // return all invoice in invoice response
    List<InvoiceResponse> responses = new ArrayList<>();

    invoicePage.getContent().forEach(invoice -> {
      InvoiceResponse response = InvoiceResponse.toResponse(invoice);
      responses.add(response);
    });

    return getInvoiceResponsePaginatedResponse(pageable, invoicePage, responses);
  }

  private static PaginatedResponse<InvoiceResponse> getInvoiceResponsePaginatedResponse(Pageable pageable, Page<Invoice> data, List<InvoiceResponse> invoiceResponses) {
    boolean hasNext = data.getNumber() < data.getTotalPages() - 1;
    boolean hasPrevious = data.getNumber() > 0;

    PaginatedResponse<InvoiceResponse> paginatedResponse = new PaginatedResponse<>();
    paginatedResponse.setPage(pageable.getPageNumber());
    paginatedResponse.setSize(pageable.getPageSize());
    paginatedResponse.setTotalElements(data.getTotalElements());
    paginatedResponse.setTotalPages(data.getTotalPages());
    paginatedResponse.setContent(invoiceResponses);
    paginatedResponse.setHasNext(hasNext);
    paginatedResponse.setHasPrevious(hasPrevious);
    return paginatedResponse;
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
