package com.YoutubeTools.Service;

import com.YoutubeTools.Model.SearchVideo;
import com.YoutubeTools.Model.Video;
import com.YoutubeTools.Model.VideoDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    private final WebClient.Builder webClientBuilder;

    @Value("${youtube.api.key}")
    private String apikey;

    @Value("${youtube.base.url}")
    private String baseurl;

    @Value("${youtube.api.max.related.videos}")
    private int maxRelatedVideos;

    public SearchVideo searchVideo(String videoTitle) {
        List<String> videoIds = searchforVideoIds(videoTitle);
        if (videoIds.isEmpty()) {
            return SearchVideo.builder()
                    .primaryVideo(null)
                    .relatedVideos(Collections.emptyList())
                    .build();
        }

        String primaryVideoId = videoIds.get(0);
        List<String> relatedVideoIds = videoIds.subList(1, Math.min(videoIds.size(), maxRelatedVideos));

        Video primaryVideo = getVideoById(primaryVideoId);
        List<Video> relatedVideos = new ArrayList<>();
        for (String id : relatedVideoIds) {
            Video video = getVideoById(id);
            if (video != null) {
                relatedVideos.add(video);
            }
        }

        return SearchVideo.builder()
                .primaryVideo(primaryVideo)
                .relatedVideos(relatedVideos)
                .build();
    }

     public VideoDetails getVideoDetails(String videoId){
        VideoApiResponse response = webClientBuilder.baseUrl(baseurl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part" ,"snippet")
                        .queryParam("id" , videoId)
                        .queryParam("key" , apikey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();
        if(response == null || response.items.isEmpty()){
            return null;
        }
        Snippet snippet = response.items.get(0).snippet;
        String thumbnailUrl = snippet.thumbnails.getBestThumbnailUrl();
        return  VideoDetails.builder()
                .id(videoId)
                .title(snippet.title)
                .description(snippet.description)
                .tags(snippet.tags == null ? Collections.emptyList() : snippet.tags)
                .thumbnailUrl(thumbnailUrl)
                .channelTitle(snippet.channelTitle)
                .publishedAt(snippet.publishedAt)
                .build();

     }
    private List<String> searchforVideoIds(String videoTitle) {
        SearchApiResponse response = webClientBuilder.baseUrl(baseurl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", videoTitle)
                        .queryParam("type", "video")
                        .queryParam("maxResults", maxRelatedVideos)
                        .queryParam("key", apikey)
                        .build())
                .retrieve()
                .bodyToMono(SearchApiResponse.class)
                .block();

        if (response == null || response.items == null) {
            return Collections.emptyList();
        }

        List<String> videoIds = new ArrayList<>();
        for (SearchItem item : response.items) {
            if (item.id != null && item.id.videoId != null) {
                videoIds.add(item.id.videoId);
            }
        }
        return videoIds;
    }
    /*public  VideoDetails getVideoDetails(String videoId) {
        VideoApiResponse response = webClientBuilder.baseUrl(baseurl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apikey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.items == null || response.items.isEmpty()) {
            return null;
        }

        Snippet snippet = response.items.get(0).snippet;
        String thumbnailUrl = snippet.thumbnails.getBestThumbnailUrl();
        return VideoDetails.builder()
                .id(videoId)
                .title(snippet.title)
                .description(snippet.description)

                .channelTitle(snippet.channelTitle)

                .tags(snippet.tags == null ? Collections.emptyList() : snippet.tags)
                .thumbnailUrl(thumbnailUrl)

                .build();
    }
*/
    private Video getVideoById(String videoId) {
        VideoApiResponse response = webClientBuilder.baseUrl(baseurl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apikey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.items == null || response.items.isEmpty()) {
            return null;
        }

        Snippet snippet = response.items.get(0).snippet;
        return Video.builder()
                .id(videoId)
                .channelTitle(snippet.channelTitle)
                .title(snippet.title)
                .tags(snippet.tags == null ? Collections.emptyList() : snippet.tags)
                .build();
    }

    @Data
    static class SearchApiResponse {
        List<SearchItem> items;
    }

    @Data
    static class SearchItem {
        Id id;
    }

    @Data
    static class Id {
        String videoId;
    }

    @Data
    static class VideoApiResponse {
        List<VideoItem> items;
    }

    @Data
    static class VideoItem {
        Snippet snippet;
    }

    @Data
    static class Snippet {
        String title;
        String description;
        String channelTitle;
        String publishedAt;
        List<String> tags;
        Thumbnails thumbnails;
    }

    @Data
    static class Thumbnails {
        Thumbnail maxres;
        Thumbnail high;
        Thumbnail medium;
        Thumbnail _default;

        public String getBestThumbnailUrl() {
            if (maxres != null) return maxres.url;
            if (high != null) return high.url;
            if (medium != null) return medium.url;
            return _default != null ? _default.url : "";
        }
    }

    @Data
    static class Thumbnail {
        String url;
    }
}