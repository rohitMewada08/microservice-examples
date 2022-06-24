package com.service.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.reservation.entity.Booking;
import com.service.reservation.enums.RoomStatus;
import com.service.reservation.repository.ReservationRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    ReservationRepository mockReservationRepository;

    @Autowired
    ReservationRepository reservationRepository;
    ObjectMapper om = new ObjectMapper();

    private Booking booking;
    List<Booking> booikngs;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        Calendar cl= Calendar.getInstance();
        cl.set(2021,06,07);
        booking = Booking.builder().startAt(cl).endAt(cl)
                .hotelId(1L).guestId(02L).roomId(05L)
                .status(RoomStatus.BOOKED).build();

        booikngs = new ArrayList<>(Arrays.asList(booking));

    }

    @Test
    public void getHotelTest() throws Exception {

        when(mockReservationRepository.findAll()).thenReturn(booikngs);
        mockMvc.perform(get("/reservation/get-all").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(content().json(om.writeValueAsString(booikngs)));

    }

    @Test
    public void getBookingTest() throws Exception {

        when(mockReservationRepository.findAll()).thenReturn(booikngs);
        MvcResult result = mockMvc.perform(get("/reservation/get-booking/CANCELLED")
                        .param("startAt","2021-06-07").param("endAt","2021-06-08")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andReturn();

        System.out.println(om.writeValueAsString(result));
    }

    @Test
    public void saveBookingTest() throws Exception {

       Booking boo = reservationRepository.save(booking);
        System.out.println(boo);
    }
}
