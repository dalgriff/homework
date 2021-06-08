package com.trilogyed.rsvp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.rsvp.dao.RsvpRepository;
import com.trilogyed.rsvp.model.Rsvp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(RsvpController.class)
public class RsvpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RsvpRepository repo;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {

    }

    @Test
    public void getRsvpByIdShouldReturnRsvpWithIdJson() throws Exception {
        Rsvp rsvp = new Rsvp();
        rsvp.setGuestName("Mock Gruber");
        rsvp.setTotalAttending(100);
        rsvp.setId(8);

        // Since findById returns an Optional, we create one with our rsvp object as the value
        Optional<Rsvp> returnVal = Optional.of(rsvp);

        //Object to JSON in String
        String outputJson = mapper.writeValueAsString(rsvp);

        // Mocking DAO response
        // This is another way to mock using mockito
        // same as doReturn(returnVal).when(repo).findById(8);
        // We could also set up our mocks in a separate method, if we so chose
        when(repo.findById(8)).thenReturn(returnVal);


        this.mockMvc.perform(get("/rsvps/8"))
                .andDo(print())
                .andExpect(status().isOk())
                //use the objectmapper output with the json method
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getRsvpThatDoesNotExistReturns404() throws Exception {

        // Since findById returns an Optional, we create one. But this time without a value
        // as that would be the expected behavior if we searched for a non-existant id
        Optional<Rsvp> returnVal = Optional.empty();

        int idForRsvpThatDoesNotExist = 100;

        when(repo.findById(idForRsvpThatDoesNotExist)).thenReturn(returnVal);

        this.mockMvc.perform(get("/rsvps/" + idForRsvpThatDoesNotExist))
                    .andDo(print())
                    .andExpect(status().isNotFound());
    }

    @Test
    public void createRsvpShouldReturnCreatedRsvp() throws Exception {

        Rsvp inputRsvp = new Rsvp();
        inputRsvp.setGuestName("Mock Gruber");
        inputRsvp.setTotalAttending(100);

        //Object to JSON in String
        String inputJson = mapper.writeValueAsString(inputRsvp);

        Rsvp outputRsvp = new Rsvp();
        outputRsvp.setGuestName("Mock Gruber");
        outputRsvp.setTotalAttending(100);
        outputRsvp.setId(8);

        //Object to JSON in String
        String outputJson = mapper.writeValueAsString(outputRsvp);

        when(repo.save(inputRsvp)).thenReturn(outputRsvp);

        this.mockMvc.perform(post("/rsvps")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                    ).andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json(outputJson));
    }

    @Test
    public void getAllRsvpsShouldReturnAListOfRsvps() throws Exception {

        Rsvp rsvp = new Rsvp();
        rsvp.setGuestName("Mock Gruber");
        rsvp.setTotalAttending(100);
        rsvp.setId(8);

        Rsvp rsvp2 = new Rsvp();
        rsvp2.setGuestName("Old MockDonald");
        rsvp2.setTotalAttending(2);
        rsvp2.setId(9);

        List<Rsvp> rsvpList = new ArrayList<>();
        rsvpList.add(rsvp);
        rsvpList.add(rsvp2);

        //Object to JSON in String
        when(repo.findAll()).thenReturn(rsvpList);

        List<Rsvp> listChecker = new ArrayList<>();
        listChecker.addAll(rsvpList);
        //To confirm the test is testing what we think it is... add another Rsvp.
        // Uncommenting the below line causes the test to fail. As expected!
        // listChecker.add(new Rsvp(10, "Donald Duck", 2));
        String outputJson = mapper.writeValueAsString(listChecker);

        this.mockMvc.perform(get("/rsvps"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(outputJson));
    }


    @Test
    public void updateRsvpsShouldReturnAnUpdatedRsvps() throws Exception {

        Rsvp rsvp = new Rsvp();
        rsvp.setGuestName("Mock Gruber");
        rsvp.setTotalAttending(100);
        rsvp.setId(8);

        //these will be the same
        String inputJson = mapper.writeValueAsString(rsvp);
        String outputJson = mapper.writeValueAsString(rsvp);

        this.mockMvc.perform(put("/rsvps/" + rsvp.getId())
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }


    @Test
    public void deleteRsvpIsOkNoContentReturned() throws Exception {

        //can't mock the call to delete. it returns void
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/rsvps/8"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}

