package de.qaware.demo.cloudobservability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class SpringBootDemoController {

    private static final Duration MAX_SLEEP_TIME = Duration.ofMillis(500);

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootDemoController.class);

    @PostMapping("/trigger-me")
    public String triggerMe() {
        var sleepDuration = sleepForRandomTime();
        return "Slept for " + sleepDuration;
    }

    private Duration sleepForRandomTime() {
        var sleepDuration = Duration.ofMillis(Math.round(MAX_SLEEP_TIME.toMillis() * Math.random()));
        try {
            LOGGER.info("Sleeping for {}", sleepDuration);
            Thread.sleep(sleepDuration.toMillis());
            LOGGER.info("Done sleeping...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted during sleep", e);
        }
        return sleepDuration;
    }
}
