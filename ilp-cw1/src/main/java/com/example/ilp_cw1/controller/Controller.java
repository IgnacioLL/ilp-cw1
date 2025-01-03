package com.example.ilp_cw1.controller;

import com.example.ilp_cw1.Constants;
import com.example.ilp_cw1.utils.RayCasting;
import com.example.ilp_cw1.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class Controller {


    private Double calculateEuclideanDistance(Coordinates position1, Coordinates position2) {
        double lngDiffSquared = Math.pow(position1.getLng() - position2.getLng(), 2);
        double latDiffSquared = Math.pow(position1.getLat() - position2.getLat(), 2);
        return Math.sqrt(lngDiffSquared + latDiffSquared);
    }

    private Coordinates computeMovementDrone(Coordinates start, Double angle) {
        double x = start.lng + Constants.DISTANCE_MOVEMENT_DRONE * Math.cos(angle);
        double y = start.lat + Constants.DISTANCE_MOVEMENT_DRONE * Math.sin(angle);

        Coordinates coordinates = new Coordinates();
        coordinates.setLng(x);
        coordinates.setLat(y);

        return coordinates;
    }

    @GetMapping("/isAlive")
    public Boolean isAlive() {
        return true;
    }

    @GetMapping("/uuid")
    public String getUuid() {
        return "s2761803";
    }

    @PostMapping("/distanceTo")
    public ResponseEntity<Double> getDistanceBetweenCoordinates(@RequestBody CoordinatesPair coordinatesPair) {
        Coordinates position1 = coordinatesPair.getPosition1();
        Coordinates position2 = coordinatesPair.getPosition2();

        if (position1.checkCoordinates() && position2.checkCoordinates()) {
            Double distance = calculateEuclideanDistance(position1, position2);
            return ResponseEntity.ok(distance);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/isCloseTo")
    public ResponseEntity<Boolean> areCoordinatesClose(@RequestBody CoordinatesPair coordinatesPair) {
        Coordinates position1 = coordinatesPair.getPosition1();
        Coordinates position2 = coordinatesPair.getPosition2();

        if (position1.checkCoordinates() && position2.checkCoordinates()) {
            Double distance = calculateEuclideanDistance(position1, position2);
            Boolean isClose = distance < Constants.CLOSE_DISTANCE_THRESHOLD;

            return ResponseEntity.ok(isClose);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/nextPosition")
    public ResponseEntity<Coordinates> nextPosition(@RequestBody NextPositionRequest nextPositionRequest) {
        Coordinates start = nextPositionRequest.getStart();
        Double angle = nextPositionRequest.getAngle();

        if (start.checkCoordinates()) {
            Coordinates end = computeMovementDrone(start, angle);
            return ResponseEntity.ok(end);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/isInRegion")
    public ResponseEntity<Boolean> isInRegion(@RequestBody IsInRegionRequest isInRegionRequest) {
        Region region = isInRegionRequest.getRegion();
        Coordinates position = isInRegionRequest.getPosition();

        if (position.checkCoordinates() && region.checkAllCoordinates() && region.checkPolygon()) {

            double[][] region_matrix = region.convertRegionToMatrix();
            double[] position_vector = position.convertCoordinatesToVector();

            Boolean bool_region = RayCasting.contains(region_matrix, position_vector);

            return ResponseEntity.ok(bool_region);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(
            value = "/validateOrder",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OrderStatus> validateOrder(@RequestBody Order orderRequest) {
        OrderStatus orderStatus = orderRequest.checkOrderStatus();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderStatus);
    }
}
