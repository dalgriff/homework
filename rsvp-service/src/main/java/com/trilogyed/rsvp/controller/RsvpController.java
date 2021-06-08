package com.trilogyed.rsvp.controller;

import com.trilogyed.rsvp.dao.RsvpRepository;
import com.trilogyed.rsvp.exceptions.RsvpNotFoundException;
import com.trilogyed.rsvp.model.Rsvp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rsvps")
public class RsvpController {

    @Autowired
    RsvpRepository repo;

    public RsvpController(RsvpRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rsvp createRsvp(@RequestBody Rsvp rsvp) {

        System.out.println("CREATING RSVP");
        return repo.save(rsvp);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Rsvp> getAllRsvps() {

        System.out.println("GETTING ALL RSVPS");
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Rsvp getRsvp(@PathVariable int id) {

        System.out.println("GETTING RSVP ID = " + id);

        Optional<Rsvp> returnVal = repo.findById(id);
        if ( !returnVal.isPresent() ) {
            throw new RsvpNotFoundException("There is no RSVP with id " + id);
        }

        return returnVal.get();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Rsvp updateRsvp(@PathVariable int id, @RequestBody @Valid Rsvp rsvp) {

        System.out.println("UPDATING RSVP ID = " + rsvp.getId());

        // If no ID is provided in the body, set the id to the one in the parameter
        // Without an ID, repo.save() would create a new record in the db
        if( rsvp.getId() == null) {
            rsvp.setId(id);
        }

        repo.save(rsvp);
        return rsvp;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRsvp(@PathVariable int id) {

        System.out.println("DELETING RSVP ID = " + id   );
        repo.deleteById(id);
    }
}