/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final XmlMapper xmlMapper = new XmlMapper();

    @ResponseBody
    @GetMapping("/toXml")
    public Mono<String> findById() throws JsonProcessingException {
        User user = new User();
        user.setId(1L);
        user.setFirstName("jittagorn");
        user.setLastName("pitakmetagoon");
        xmlMapper.writeValueAsString(user);
        return Mono.just(xmlMapper.writeValueAsString(user));
    }

    @ResponseBody
    @GetMapping("/toUser")
    public Mono<User> update() throws JsonProcessingException {
        final String xml = "<xml>"
                + "<id>1</id>"
                + "<first_name>jittagorn</first_name>"
                + "<last_name>pitakmetagoon</last_name>"
                + "</xml>";
        final User user = xmlMapper.readValue(xml, User.class);
        log.debug("user => {}", user);
        return Mono.just(user);
    }

    @Data
    @JacksonXmlRootElement(localName = "xml")
    public static class User {

        private Long id;

        @JacksonXmlProperty(localName = "first_name")
        private String firstName;

        @JacksonXmlProperty(localName = "last_name")
        private String lastName;

    }

}
