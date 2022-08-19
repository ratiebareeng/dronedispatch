package com.oratilebareeng.dronesdispatch;

import com.oratilebareeng.dronesdispatch.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfiguration {
    private final DroneService droneService;

    // log drone battery level every 30 seconds
    @Scheduled(fixedRate = 30000)
    void logDroneBatteryLevels(){
        droneService.logBatteryLevel();
    }
}
