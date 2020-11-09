package spodlivoi.service;

import spodlivoi.entity.Users;

import java.util.List;

public interface Roller {

    String roll(Users user);

    String getTop(List<Users> users) ;


}
