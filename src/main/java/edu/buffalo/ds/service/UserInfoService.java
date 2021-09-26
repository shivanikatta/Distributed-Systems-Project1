package edu.buffalo.ds.service;

import edu.buffalo.ds.api.ApiResponse;
import edu.buffalo.ds.database.daos.UserInfoDAO;
import edu.buffalo.ds.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserInfoService {
    private UserInfoDAO userInfoDAO;


    public List<User> getAllUsers()
    {
        return userInfoDAO.getAllUsers();
    }

    public ApiResponse  saveUser(User user)
    {
        if(userInfoDAO.save(user))
        {
            return new ApiResponse("SUCCESS");
        }
        else
        {
            return new ApiResponse("ERROR");
        }
    }

}
