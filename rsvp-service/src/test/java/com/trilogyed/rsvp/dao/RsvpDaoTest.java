package com.trilogyed.rsvp.dao;

import com.trilogyed.rsvp.model.Rsvp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RsvpDaoTest {

    @Autowired
    RsvpRepository repo;

    @Before
    public void setUp() throws Exception {

        // Clear the database before each test
        List<Rsvp> rsvps = repo.findAll();

        rsvps.stream()
                .forEach(rsvp -> repo.deleteById(rsvp.getId()));
    }

    @Test
    public void shouldAddAndGetRsvpFromDb() {
        Rsvp rsvp = new Rsvp("John Doe", 2);
        rsvp = repo.save(rsvp);
        Rsvp fromDao = repo.findById(rsvp.getId()).get();
        assertEquals(fromDao, rsvp);
    }

    @Test
    public void shouldDeleteRsvpFromDb() {
        Rsvp rsvp = new Rsvp("John Doe", 2);
        rsvp = repo.save(rsvp);
        repo.deleteById(rsvp.getId());
        Optional<Rsvp> fromRepo = repo.findById(rsvp.getId());
        assertFalse(fromRepo.isPresent());
    }

    @Test
    public void shouldGetAllRsvpsFromDb() {
        Rsvp rsvp = new Rsvp("Sally Smith", 4);
        repo.save(rsvp);

        rsvp = new Rsvp("George Smith", 3);
        repo.save(rsvp);

        List<Rsvp> rsvps = repo.findAll();

        assertEquals(2, rsvps.size());
    }

    @Test
    public void shouldUpdateRsvpInDb() {
        Rsvp rsvp = new Rsvp("Joe Jones", 5);
        rsvp = repo.save(rsvp);
        rsvp.setGuestName("NEW NAME");
        repo.save(rsvp);
        Rsvp fromDao = repo.findById(rsvp.getId()).get();
        assertEquals(rsvp, fromDao);
    }
}