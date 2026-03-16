package com.example.library.service;

import com.example.library.entity.Borrow;
import com.example.library.repository.BorrowRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OverdueService {

    private final BorrowRepository borrowRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final double LATE_FEE_PER_DAY = 0.5;

    public OverdueService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
    }


    @Scheduled(cron = "0 0 8 ? * MON-FRI")
    public void checkOverdueBooks() {
        List<Borrow> activeBorrows = borrowRepository.findAllByReturnedAtIsNull();

        LocalDate today = LocalDate.now();

        for (Borrow borrow : activeBorrows) {

            if (borrow.getDueDate().isBefore(today)) {

                long daysLate = ChronoUnit.DAYS.between(borrow.getDueDate(), today);
                double fee = daysLate * LATE_FEE_PER_DAY;

                borrow.setLateFee(fee);
                borrowRepository.save(borrow);
            }
        }
    }


    @Scheduled(cron = "0 */10 * * * *")
    public void callUrlEveryTenMinutes() {
        String url = "https://library-nmpw.onrender.com/books";
        restTemplate.getForObject(url, String.class);
        System.out.println(LocalDateTime.now() + "- Called " + url + "-> OK");
    }
}
