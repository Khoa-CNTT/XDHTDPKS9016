package com.tourism.booking.controller.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AdminController {
    @GetMapping
    public String adminPage() {
        return "Admin Page";
    }
}
