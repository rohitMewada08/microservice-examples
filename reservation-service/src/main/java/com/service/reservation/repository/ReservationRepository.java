package com.service.reservation.repository;

import com.service.reservation.entity.Booking;
import com.service.reservation.enums.RoomStatus;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Booking,String> {

    List<Booking> findAllByHotelId(Long id);
    List<Booking> findAllByRoomId(Long id);
    List<Booking> findAllByGuestId(Long id);
    List<Booking> findByStartAtGreaterThanEqualAndEndAtLessThanEqualAndStatus( Calendar startDate,
                                                                 Calendar endDate,
                                                                 @Param("status") RoomStatus status);

    @Query(value = "SELECT b.* from booking b JOIN booking_room_id br ON b.booking_id = br.booking_booking_id " +
            "WHERE ((b.start_at >= ?1  AND b.start_at <= ?2) " +
            "   OR (b.end_at >= ?1 AND b.end_at <= ?2)) " +
            " AND b.status = 'BOOKED' " +
            " AND br.room_id =?3 limit 1",nativeQuery = true)
    Booking findBooking(String inquiryStartAt, String inquiryEndAt,
                         Long roomId);

}
