package com.utmn.shanaurin.supercomputers.controllers;

import com.utmn.shanaurin.supercomputers.Supercomputer;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class RestApiController {
    private final List<Supercomputer> supercomputers = new ArrayList<>();

    public RestApiController() {
        supercomputers.addAll(
                List.of(
                    new Supercomputer("Computer 1"),
                    new Supercomputer("Computer 2"),
                    new Supercomputer("Computer 3")
                )
        );
    }

    @GetMapping("/supercomputers")
    Iterable<Supercomputer> getSupercomputers() {
        return supercomputers;
    }
}
