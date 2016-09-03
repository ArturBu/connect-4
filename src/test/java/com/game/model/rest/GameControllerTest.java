package com.game.model.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.game.rest.GameController;


public class GameControllerTest {
	
    private MockMvc mockMvc;

    @Before
    public void setup() {
    	GameController controller = EasyMock.createMock(GameController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void doSomeTests() throws Exception {
        mockMvc.perform(get("/game/start?playerOneName=Thomm&playerTwoName=Bill"))               
                .andExpect(status().isOk());
    }

}
