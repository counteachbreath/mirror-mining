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
        List<Map<String, Object>> category = db.executeQuery("select comment_tone, answer_tone from a_categories c where c.name = " + "'" + katName + "'");
        List<Double> ctoneList = new ArrayList<>();
        JsonObject ctone = new Gson().fromJson(category.get(0).get("comment_tone").toString(), JsonObject.class);
        List<String> tones = Arrays.asList("Analytical", "Anger", "Confident", "Fear", "Joy", "Sadness", "Tentative");
        for (String tone : tones) {
            if (ctone.has(tone)) {
                Double value = Double.parseDouble(ctone.get(tone).toString());
                ctoneList.add(value);
            } else {
                ctoneList.add(0.0);
            }
        }
        model.addAttribute("av_ctone", ctoneList);

        List<Double> atoneList = new ArrayList<>();
        JsonObject atone = new Gson().fromJson(category.get(0).get("answer_tone").toString(), JsonObject.class);
        for (String tone : tones) {
            if (atone.has(tone)) {
                Double value = Double.parseDouble(atone.get(tone).toString());
                atoneList.add(value);
            } else {
                atoneList.add(0.0);
            }
        }
        model.addAttribute("av_atone", atoneList);
        return "article";
    }

    @GetMapping("/article")
    public String article(Model model) {
        List<Kategorie> katList = KategorieService.getKat();
        model.addAttribute("katList", katList);

        String katName = "wähle hier eine Kategorie aus";
        List<Article> katArt = new ArrayList<>();
        Article art = new Article();
        katArt.add(art);

        model.addAttribute("katName", katName);
        model.addAttribute("katArt", katArt);

        /*
         * Im Folgenden sollen die Daten aus der Datenbank gezogen werden.
         * Diese werden schon erfolgreich in die richtige Chart implementiert.
         * Dies sind die Average Comment Tones. */
        List<Double> av_com_tone = new ArrayList<Double>();
        av_com_tone.add(0.53495);  // Analytical
        av_com_tone.add(0.89323);  // Anger
        av_com_tone.add(0.69058);  // Confident
        av_com_tone.add(0.54758);  // Fear
        av_com_tone.add(0.31234);  // Joy
        av_com_tone.add(0.781923); // Sadness
        av_com_tone.add(0.82012);  // Tentative

        model.addAttribute("av_com_tone", av_com_tone);


        /* Dies sind die Comment Tones für den ausgewählten Artikel.
         *  muss noch an die richtige Stelle integriert werden. */
        List<Double> com_tone = new ArrayList<Double>();
        com_tone.add(0.781239);  // Analytical
        com_tone.add(0.0);       // Anger
        com_tone.add(0.702673);  // Confident
        com_tone.add(0.583166);  // Fear
        com_tone.add(0.587752);  // Joy
        com_tone.add(0.581369);  // Sadness
        com_tone.add(0.760855);  // Tentative

        model.addAttribute("com_tone", com_tone);

        return "article";
    }
}
