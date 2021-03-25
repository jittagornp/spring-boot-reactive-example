package me.jittagornp.example.reactive.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.model.*;
import me.jittagornp.example.reactive.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(tags = {"User"}, description = "ผู้ใช้งาน")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "ดึงข้อมูลผู้ใช้งานทั้งหมด", response = Pagination.class)
    public Mono findAll(final PaginationRequest paginationRequest) {
        if (paginationRequest.isPageRequest()) {
            return userService.findAllAsPage(paginationRequest.toPage());
        } else {
            return userService.findAllAsSlice(paginationRequest.toSlice());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "ดึงข้อมูลผู้ใช้งานด้วย id")
    public Mono<User> findById(@PathVariable("id") final UUID id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "สร้างข้อมูลผู้ใช้งาน")
    public Mono<User> create(@RequestBody @Validated  final CreateUser user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "อัพเดทข้อมูลผู้ใช้งานด้วย id")
    public Mono<User> update(@PathVariable("id") final UUID id, @RequestBody @Validated final UpdateUser user) {
        user.setId(id);
        return userService.update(user);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "อัพเดทข้อมูลผู้ใช้งานบางส่วนด้วย id")
    public Mono<User> partialUpdateById(@PathVariable("id") final UUID id, @RequestBody @Validated final PartialUpdateUser user) {
        user.setId(id);
        return userService.partialUpdate(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "ลบข้อมูลผู้ใช้งานด้วย id")
    public Mono<Void> deleteById(@PathVariable("id") final UUID id) {
        return userService.deleteById(id);
    }
}
