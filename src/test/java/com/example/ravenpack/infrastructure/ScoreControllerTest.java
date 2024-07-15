package com.example.ravenpack.infrastructure;

import com.example.ravenpack.application.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testOK() throws Exception {
        this.mockMvc.perform(post("/score")
                .contentType(MediaType.TEXT_PLAIN)
                .content(TestUtils.getCSV()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"user_id\",\"total_messages\",\"avg_score\"")));
    }


    @Test
    public void testFail4xx() throws Exception {
        this.mockMvc.perform(get("/score"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Bad Request: Request method 'GET' is not supported")));
    }


    @Test
    public void testFailFormat() throws Exception {

        String body = new StringBuilder()
                .append("user_id, message")
                .append(System.getProperty("line.separator"))
                .append("1,").toString();

        this.mockMvc.perform(post("/score")
                .contentType(MediaType.TEXT_PLAIN)
                .content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(equalTo("Bad Request: Bad csv format")));
    }
}
