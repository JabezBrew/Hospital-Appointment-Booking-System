package AppointToDoctorRestService.entities;


import lombok.Getter;
import lombok.Setter;

import  jakarta.persistence.Embeddable;
import java.time.LocalDate;


@Embeddable
@Setter
@Getter
public class AvailableDates {
    private LocalDate availabletime;
    private boolean booked;
    public AvailableDates(LocalDate date, boolean isBooked) {
        this.availabletime = date;
        this.booked = isBooked;
    }

    public AvailableDates() {
        availabletime = LocalDate.now();
        booked = true;
    }
}
