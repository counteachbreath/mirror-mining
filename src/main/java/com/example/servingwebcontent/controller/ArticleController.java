package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.Article;
import com.example.servingwebcontent.DatabaseConnection;
import com.example.servingwebcontent.Kategorie;
import com.example.servingwebcontent.KategorieService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jcraft.jsch.JSchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/*Diese Klasse setzt die html Artikel Seite.
*
* Malin Schultz*/

@Controller
public class ArticleController {

    @GetMapping(value = "/chooseKatSel")
    public String article(@RequestParam(name = "chooseKatSel") String katName, Model model) throws JSchException, SQLException, IOException {
        List<Kategorie> katList = KategorieService.getKat();
        model.addAttribute("katList", katList);

        Kategorie kat = KategorieService.getKatByName(katName);
        assert kat != null;
        List<Article> katArt = kat.getKatArt();

        model.addAttribute("katName", katName);
        model.addAttribute("katArt", katArt);

        // Get category from the DB and create lists from JSON columns.
        DatabaseConnection db = new DatabaseConnection();
        List<Map<String, Object>> categories = db.getData("categories");
        for (Map<String, Object> cat : categories) {
            if (cat.get("name").toString().equals(katName)) {
                List<Double> ctoneList = new ArrayList<>();
                JsonObject ctone = new Gson().fromJson(cat.get("comment_tone").toString(), JsonObject.class);
                ctone.keySet().forEach(key -> {
                    Double value = Double.parseDouble(ctone.get(key).toString());
                    ctoneList.add(value);
                });

                List<Double> atoneList = new ArrayList<>();
                JsonObject atone = new Gson().fromJson(cat.get("answer_tone").toString(), JsonObject.class);
                atone.keySet().forEach(key -> {
                    Double value = Double.parseDouble(atone.get(key).toString());
                    atoneList.add(value);
                });
                model.addAttribute("av_ctone", ctoneList);
                model.addAttribute("av_atone", atoneList);
            }
        }
        return "article";
    }

    @GetMapping("/article")
    public String article(Model model) {
        List<Kategorie> katList = KategorieService.getKat();
        model.addAttribute("katList", katList);

        String katName = "";
        List<Article> katArt = new ArrayList<>();
        Article art = new Article();
        katArt.add(art);

        model.addAttribute("katName", katName);
        model.addAttribute("katArt", katArt);

        return "article";
    }
}
