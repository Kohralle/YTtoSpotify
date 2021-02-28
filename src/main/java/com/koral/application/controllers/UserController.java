package com.koral.application.controllers;

import com.koral.application.converter.Converter;
import com.koral.application.converter.ReturnInfo;
import com.koral.application.model.CurrentUser;
import com.koral.application.model.PremiumStatus;
import com.koral.application.model.Song;
import com.koral.application.model.User;
import com.koral.application.repository.PremiumRepository;
import com.koral.application.repository.SongRepository;
import com.koral.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
public class UserController {

    CurrentUser currentUser = new CurrentUser();

    @Autowired
    UserRepository userRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    PremiumRepository premiumStatusRepository;


    @PostMapping("/converter")
    public ModelAndView postconverter(HttpServletRequest request) throws GeneralSecurityException, IOException {
        String spotify_token = request.getParameter("spotify_token");
        String yt_playlist_id = request.getParameter("playlist_id");
        String spotify_username = request.getParameter("spotify_username");
        String spotify_playlist_name = request.getParameter("playlist_name");

        Converter converter = new Converter();
        ReturnInfo returnInfo = converter.RunConverter(spotify_token, yt_playlist_id, spotify_username, spotify_playlist_name);

        String playlist_id = returnInfo.getPlaylist_url();

        List<Song> songs = returnInfo.getSongs();

        Long user_id = this.currentUser.getId();
        System.out.println("USERID" + user_id);

        for (int i = 0; i < songs.size(); i++) {
            User user = userRepository.getOne(user_id);
            user.addItem(songs.get(i));
            songRepository.save(songs.get(i));
        }

        return new ModelAndView("redirect:https://open.spotify.com/playlist/" + playlist_id);
    }

    @GetMapping("/register")

    public String register() {

        return "register";
    }

    @GetMapping("/premium")

    public String premium() {
        Long id = this.currentUser.getId();

        String premium_status = userRepository.findById(id).get().getPremiumStatus().getStatus();

        if(premium_status.equals("true")){
            return "premium_unsubscribe";
        }

        else {
            return "premium";
        }
    }

    @GetMapping("/subscribe")
    public String subscribe_to_premium(){
        Long id = this.currentUser.getId();
        User user = userRepository.findById(id).get();
        user.getPremiumStatus().setStatus("true");
        userRepository.save(user);

        return "premium_unsubscribe";
    }

    @GetMapping("/unsubscribe")
    public String unsubscribe_premium(){
        Long id = this.currentUser.getId();
        User user = userRepository.findById(id).get();
        user.getPremiumStatus().setStatus("false");
        userRepository.save(user);

        return "premium";
    }

    @PostMapping("/")
    public String hello(HttpServletRequest request){
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        user.setPremiumStatus(new PremiumStatus("false", 15));

        userRepository.save(user);
        return "login";

    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/home")
    public String gethome() {

        return "home";
    }

    @GetMapping("/signout")
    public String signout() {

        return "login";
    }

    @PostMapping("/home")
    public String posthome(HttpServletRequest request){
        int index = 0;
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        for (int i = 0; i < userRepository.count(); i++) {
            User db_user = userRepository.findAll().get(i);
            if (db_user.getEmail().equals(email)){
                System.out.println("Email found");
                index = i;
                Long id = (long) i;
                this.currentUser.setId(db_user.getId());
                break;
            }
        }

        System.out.println("LOGGED IN USER ID" + this.currentUser.getId());

        if (userRepository.findAll().get(index).getPassword().equals(password)){
            return "home";
        }

        else {
            return "incorrect_password";
        }

    }

    @GetMapping("list")
    public String showList(Model model){
        model.addAttribute("songs", userRepository.findById(currentUser.getId()).get().getSongs());
        return "listsongs";
    }
}
