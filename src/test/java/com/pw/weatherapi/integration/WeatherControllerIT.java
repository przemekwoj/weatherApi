package com.pw.weatherapi.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.pw.weatherapi.model.api.CityPosition;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerIT {
    public WeatherControllerIT(@Value("${darkSky.key}") String key, WebApplicationContext context) {
        this.key = key;
        this.context = context;
    }

    private WireMockServer wireMockServer;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final String key;

    private final String jastarniaSegment = createSegment(CityPosition.JASTARNIA);
    private final String wladyslawowoSegment = createSegment(CityPosition.WŁADYSŁAWOWO);

    private String createSegment(CityPosition cityPosition) {
        return cityPosition.getLatitude() + "," + cityPosition.getLongitude();
    }

    private String createStubUrl(String segment) {
        return UriComponentsBuilder.newInstance()
                .pathSegment(key)
                .pathSegment(segment)
                .build()
                .toString();
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
        wireMockServer.start();
    }


    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    private void setUpJastarniaAndWladyslawowoResponses(){
        setupResponseStub("jastarnia", jastarniaSegment);
        setupResponseStub("wladyslawowo", wladyslawowoSegment);
    }
    public void setupResponseStub(String city, String segment) {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(createStubUrl(segment)))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(city + "-response.json")
                        .withStatus(200)));
    }


    private String jsonResponse(String file) throws IOException, ParseException {
        File resourcesDirectory = new File("src/test/resources/expectedResponse");
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(resourcesDirectory.getAbsolutePath() + "/" + file + "-response.json", StandardCharsets.UTF_8));
        return jsonObject.toJSONString();
    }


    @Test
    public void shouldNotChooseAnyLocation_BadWeatherConditions() throws Exception {
        //given
        String url = "/2020-08-09";
        setUpJastarniaAndWladyslawowoResponses();
        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse("2020-08-09-not-found")));
    }

    @Test
    public void shouldChooseBestLocation_OneLocationsWithGoodWeatherConditions() throws Exception {
        //given
        String url = "/2020-08-10";
        setUpJastarniaAndWladyslawowoResponses();
        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse("2020-08-10-jastarnia")));
    }

    @Test
    public void shouldChooseBestLocation_TwoLocationsWithGoodWeatherConditions() throws Exception {
        //given
        String url = "/2020-08-11";
        setUpJastarniaAndWladyslawowoResponses();
        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponse("2020-08-11-wladyslawowo")));
    }

}
