package com.purwafest.purwafest.point.presentation;


import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.point.application.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/point")
public class PointRestController {
    private PointService pointService;

    public PointRestController(PointService pointService){
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<?> getTotalPoints(){
        Integer userId = Claims.getUserId();
        return Response.successfulResponse(
                "Get total point Success",
                pointService.getTotalPoints(userId)
        );
    }
}
