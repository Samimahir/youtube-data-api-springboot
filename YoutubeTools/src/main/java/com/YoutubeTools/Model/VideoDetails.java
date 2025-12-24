package com.YoutubeTools.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDetails {
    private String id;
    private String title;
    private String description;
    private String channelTitle;
    private List<String> tags;       // List of tags for the video
    private String thumbnailUrl;     // URL of the thumbnail
    private String publishedAt;      // Video publish date
}
