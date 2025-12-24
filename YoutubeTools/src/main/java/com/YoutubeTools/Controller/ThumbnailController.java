package com.YoutubeTools.Controller;

import com.YoutubeTools.Service.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThumbnailController {
    @Autowired
    ThumbnailService service;
    @GetMapping("/thumbnail")
    public String getThumbnail(){
        return "thumbnails";
    }
    @PostMapping("/get-thumbnail")
    public String showThumbnail(@RequestParam("videoUrlOrId") String videoUrlOrId , Model model){
        String videoId = service.extractVideoId(videoUrlOrId);
        if(videoId==null){
           model.addAttribute("error", "Invalid Youtube URL");
           return "thumbnails";
        }
        String thumbnaiUrl = "https://img.youtube.com/vi/"+videoId + "/maxresdefault.jpg";
       model.addAttribute("thumbnailUrl", thumbnaiUrl);

        return "thumbnails";
    }
}
