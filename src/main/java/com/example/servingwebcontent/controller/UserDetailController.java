package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.DatabaseConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jcraft.jsch.JSchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class UserDetailController {
    @GetMapping(value = "/userDetail")
    public String userDetail(@RequestParam(name = "userDetail", required = false, defaultValue = "userid") String id, Model model) throws JSchException, SQLException, IOException {
        model.addAttribute("userid", id);

        // Get user from the DB and create lists from JSON columns.
        DatabaseConnection db = new DatabaseConnection();
        List<Map<String, Object>> user = db.executeQuery("select * from a_users where id = " + id);
        List<Double> ctoneList = new ArrayList<>();
        JsonObject ctone = new Gson().fromJson(user.get(0).get("comment_tone").toString(), JsonObject.class);
        List<String> tones = Arrays.asList("Analytical", "Anger", "Confident", "Fear", "Joy", "Sadness", "Tentative");
        for (String tone : tones) {
            if (ctone.has(tone)) {
                Double value = Double.parseDouble(ctone.get(tone).toString());
                ctoneList.add(value);
            }
            else {
                ctoneList.add(0.0);
            }
        }
        model.addAttribute("user_c", ctoneList);

        List<Double> atoneList = new ArrayList<>();
        JsonObject atone = new Gson().fromJson(user.get(0).get("answer_tone").toString(), JsonObject.class);
        for (String tone : tones) {
            if (atone.has(tone)) {
                Double value = Double.parseDouble(atone.get(tone).toString());
                atoneList.add(value);
            }
            else {
                atoneList.add(0.0);
            }
        }
        model.addAttribute("user_a", atoneList);

        List<Double> persList = new ArrayList<>();
        JsonObject pers = new Gson().fromJson(user.get(0).get("personality").toString(), JsonObject.class);
        pers.keySet().forEach(key -> {
            Double value = Double.parseDouble(pers.get(key).toString());
            persList.add(value);
        });
        model.addAttribute("user_pi", persList);
        return "userDetail";
    }
}
