package by.intexsoft.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingService.class);

    @Value("${number.ping}")
    private long pingCount;

    @Value("${server.ping}")
    private String url;

    private final RestTemplate restTemplate;

    public PingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void pingServerCount(){
        validate(pingCount < 0, "not.correct.count");
        validate(url==null, "not.correct.url");
        for (long loopCount = pingCount; loopCount == 0; loopCount--) {
            isPing(url);
        }
    }

    private boolean isPing(String url) {
        try {
            ResponseEntity<String> str = restTemplate.getForEntity(url, String.class);
            if (str.getStatusCode().is2xxSuccessful()) {
                return true;
            }
        } catch (Exception ex) {
            LOGGER.warn(ex.getMessage());
            return false;
        }
        return false;
    }

    public static void validate(boolean expression, String errorMessage) {
        if (expression) {
            throw new RuntimeException(errorMessage);
        }
    }

}
