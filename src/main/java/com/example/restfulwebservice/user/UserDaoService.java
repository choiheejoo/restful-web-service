package com.example.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static List<User1> users = new ArrayList<>();
    private static int userCount=3;

    //static block => 클래스 변수 초기화에 사용(클래스가 처음 로딩될 때 한번만 수행) => users 초기화
    static{
        users.add(new User1(1, "woo", new Date(), "pass1", "701010-000000"));
        users.add(new User1(2, "joo", new Date(),"pass2", "701010-111111"));
        users.add(new User1(3, "hana", new Date(),"pass3", "701010-222222"));
    }

    public List<User1> findAll(){
        return users;
    }

    public User1 save(User1 user){
        if(user.getId()==null){
            user.setId(++userCount);
        }
        users.add(user);
        return user;
    }

    public User1 findOne(int id){
       for(User1 user : users){
           if(user.getId() == id){
               return user;
           }
       }
       return null;
   }

   public User1 updateById(User1 user){
        for(User1 savedUser : users){
            if(savedUser.getId() == user.getId()){
                savedUser.setName(user.getName());
                savedUser.setJoinDate(user.getJoinDate());
                return savedUser;
            }
        }
        return null;
   }

   public User1 DeleteById(int id){
        //열거형 데이터 -> 순차적으로 접근하기 위함.
       Iterator<User1> iterator = users.iterator();

       while (iterator.hasNext()){
           User1 user = iterator.next();

           if(user.getId() == id){
               iterator.remove();
               return user;
           }
       }
       return null;
   }

}
