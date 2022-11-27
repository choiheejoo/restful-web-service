package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    //생성자를 통한 의존성 주입
    public AdminUserController(UserDaoService service){
        this.service=service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveallUsers(){
        List<User1> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password");

        //filter를 사용할 수 있는 형태로 변경
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    //GET /admin/users/1 -> 컨트롤러에는 String으로 전달됨 -> 선언한 데이터 형태로 자동 매핑됨.
    // /admin/v1/users/1
    //@GetMapping("/v1/users/{id}")
    //@GetMapping(value="/users/{id}/", params="version=1")
   // @GetMapping(value="/users/{id}", headers = "X-API-VERSION=1")
    @GetMapping(value="/users/{id}", produces = "application/vnd.company.appv1+json")
    public MappingJacksonValue retrieveUsersV1(@PathVariable int id){
        //ctrl alt v -> 변수생성
        User1 user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        //filter를 사용할 수 있는 형태로 변경
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        return mapping;
    }

    //@GetMapping("/v2/users/{id}")
    //@GetMapping(value="/users/{id}/", params="version=2")
   // @GetMapping(value="/users/{id}", headers = "X-API-VERSION=2")
    @GetMapping(value="/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUsersV2(@PathVariable int id){
        //ctrl alt v -> 변수생성
        User1 user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        //User -> User2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2); // id, name, joinDate, password, ssn
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");

        //filter를 사용할 수 있는 형태로 변경
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);
        return mapping;
    }



}
