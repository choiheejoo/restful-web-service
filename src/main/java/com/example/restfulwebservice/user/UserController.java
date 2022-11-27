package com.example.restfulwebservice.user;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    private UserDaoService service;

    //생성자를 통한 의존성 주입
    public UserController(UserDaoService service){
        this.service=service;
    }

    @GetMapping("/users")
    public List<User1> retrieveallUsers(){
        return service.findAll();
    }

    //GET /users/1 -> 컨트롤러에는 String으로 전달됨 -> 선언한 데이터 형태로 자동 매핑됨.
    @GetMapping("/users/{id}")
    public EntityModel<User1> retrieveUsers(@PathVariable int id){
        //ctrl alt v -> 변수생성
        User1 user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        //hateoas
        //현재 리소스와 연관된(호출 가능한) 자원 상태 정보를 제공
        EntityModel<User1> model = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveallUsers());
        model.add(linkTo.withRel("all-users"));

        return model;
    }

    //ServletUriComponentsBuilder클래스를 사용하여 현재 요청의 URI를 가져올 수 있다.
    // 다시 말해, 서버에서 반환시켜주려는 값을 Response 엔터티에 담아 리턴할 수 있다. 즉 상세정보 보기가 가능하다.
    //fromCurrentRequest()를 사용하여 현재 요청된 Request 값을 사용하고,
    //path로 반환시킬 위치,
    //buildAndExpand에 새롭게 설정한 id값을 path에 지정시켜준다.
    //마지막으로 toUri()로 URI로 반환시켜준다.
    //리턴하기 위해 void를 ResponseEntity<User>로 우리가 도메인으로 지정했던 User를 반환하게 만든다.
    @PostMapping("/users")
    public ResponseEntity<User1> CreateUser(@Valid @RequestBody User1 user){
        User1 savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()//.build().toUri(); -> // 요청 uri만
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/users")
    public User1 updateUser(@RequestBody User1 user){
        User1 updatedUser = service.updateById(user);

        if(updatedUser == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", user.getId()));
        }
        return updatedUser;
    }

    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable int id){
        User1 user = service.DeleteById(id);

        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

}
