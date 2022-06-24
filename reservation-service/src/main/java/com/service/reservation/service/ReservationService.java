package com.service.reservation.service;

import com.service.reservation.consumer.HotelRestConsumer;
import com.service.reservation.consumer.PaymentRestConsumer;
import com.service.reservation.dto.BookingResponseDto;
import com.service.reservation.dto.Hotel;
import com.service.reservation.dto.Payment;
import com.service.reservation.dto.Room;
import com.service.reservation.entity.Booking;
import com.service.reservation.enums.AvailabilityResponse;
import com.service.reservation.enums.PaymentStatus;
import com.service.reservation.enums.RoomStatus;
import com.service.reservation.exception.AlreadyBookedException;
import com.service.reservation.exception.NotFoundException;
import com.service.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PaymentRestConsumer paymentRestConsumer;

    @Autowired
    HotelRestConsumer hotelRestConsumer;

    public List<Booking> getAll() {
        var list = reservationRepository.findAll();
        if (list.isEmpty())
            throw new NotFoundException("No Booking data found");

      return list;
    }

    public BookingResponseDto save(Booking booking) {
        booking.setStatus(RoomStatus.PENDING);
        long availableCount =
                booking.getRoomId().stream().map(aLong -> checkAvailability(booking.getStartAt(),
                                                                    booking.getEndAt(),aLong)).filter(s -> s.equals(AvailabilityResponse.AVAILABLE)).count();
        if(availableCount != booking.getRoomId().size())
            throw new AlreadyBookedException("Already booked. Try again");

        Booking bookingRecord = reservationRepository.save(booking);

        Payment payment = Payment.builder().amount(booking.getTotalPrice())
                .guestId(booking.getGuestId())
                .bookingId(bookingRecord.getBookingId())
                .build();

        payment = paymentRestConsumer.doPayment(payment);

        if (payment.getPaymentStatus().equals(PaymentStatus.SUCCESS)){
            bookingRecord.setStatus(RoomStatus.BOOKED);
            bookingRecord = reservationRepository.save(bookingRecord);
        }

        Hotel hotel = hotelRestConsumer.getHotel(booking.getHotelId());
        List<Room> rooms = hotel.getRoom().stream()
                .filter(room -> booking.getRoomId().contains(room.getRoomId()))
                .collect(Collectors.toList());
        hotel.setRoom(rooms);

        BookingResponseDto bookingResponseDto =  new BookingResponseDto();
        bookingResponseDto.setBooking(bookingRecord);
        bookingResponseDto.setPayment(payment);
        bookingResponseDto.setHotel(hotel);

      return bookingResponseDto;
    }

    public Booking update(String hotelId, Booking booking) {
        return reservationRepository.findById(hotelId).map( bookingRecord -> {
            bookingRecord.setGuestId(booking.getGuestId());
            bookingRecord.setHotelId(booking.getHotelId());
            bookingRecord.setRoomId(booking.getRoomId());
            bookingRecord.setStatus(booking.getStatus());
            bookingRecord.setStartAt(booking.getStartAt());
            bookingRecord.setEndAt(booking.getEndAt());
            return reservationRepository.save(bookingRecord);
        }).orElseThrow(() ->
                new NotFoundException("Booking with id " + hotelId +" does not exist."));
    }

    public Booking getById(String bookingId) {
       return reservationRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Booking with id " + bookingId +" does not exist."));
    }

    public List<Booking> getByHotelId(Long hotelId) {
        var list = reservationRepository.findAllByHotelId(hotelId);
        if (list.isEmpty())
            throw new NotFoundException("Booking with hotelId " + hotelId +" does not exist.");
        return list;
    }

    public List<Booking> getByRoomId(Long roomId) {
        var list = reservationRepository.findAllByRoomId(roomId);
        if (list.isEmpty())
            throw new NotFoundException("Booking with hotelId " + roomId +" does not exist.");
        return list;
    }

    public List<Booking> getByGuestId(Long guestId) {
        var list = reservationRepository.findAllByGuestId(guestId);
        if (list.isEmpty())
            throw new NotFoundException("Booking with guestId " + guestId +" does not exist.");
        return list;
    }

    public List<Booking> findBookingByEntityDateBetweenAndStatus(Calendar startDate,
                                                                 Calendar endDate,
                                                                 String status) {
        var list = reservationRepository
                        .findByStartAtGreaterThanEqualAndEndAtLessThanEqualAndStatus(
                                startDate,endDate,RoomStatus.valueOf(status));
        if (list.isEmpty())
            throw new NotFoundException("No Booking data found");

        return list;
    }

    public List<Booking> findAllByHotelId(Long hotelId) {
        var list = reservationRepository
                .findAllByHotelId(hotelId);
        if (list.isEmpty())
            throw new NotFoundException("No Booking data found");
        return list;
    }


    public List<Booking> findAllByGuestId(Long hotelId) {
        var list = reservationRepository
                .findAllByGuestId(hotelId);
        if (list.isEmpty())
            throw new NotFoundException("No Booking data found");
        return list;
    }


    public List<Booking> findAllByRoomId(Long hotelId) {
        var list = reservationRepository
                .findAllByRoomId(hotelId);
        if (list.isEmpty())
            throw new NotFoundException("No Booking data found");
        return list;
    }

    public String updateBookingStatus(String bookingId) {
        return reservationRepository.findById(bookingId).map( bookingRecord -> {
            bookingRecord.setStatus(RoomStatus.CANCELLED);
            return reservationRepository.save(bookingRecord).getStatus().name();
        }).orElseThrow(() ->
                new NotFoundException("Booking with id " + bookingId +" does not exist."));
    }

    public AvailabilityResponse checkAvailability(Calendar inquiryStartAt, Calendar inquiryEndAt, Long roomId) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(inquiryStartAt.getTime());
        Booking booking = reservationRepository
                .findBooking(dateFormat.format(inquiryStartAt.getTime()),
                        dateFormat.format(inquiryEndAt.getTime()), roomId);
        return booking != null ? AvailabilityResponse.BOOKED : AvailabilityResponse.AVAILABLE;
    }
}
