📺 YouTube Data API Integration Project

A Java Spring Boot application that integrates with YouTube Data API v3 to fetch YouTube channel and video information such as titles, descriptions, view count, likes, and other statistics using REST APIs.

This project demonstrates external API integration, JSON parsing, and RESTful service development using Spring Boot.

🚀 Features

Fetch YouTube channel details using Channel ID

Retrieve video metadata from YouTube Data API

Display video title, description, views, likes, and statistics

RESTful API implementation using Spring Boot

API key–based authentication

JSON response parsing and handling

🛠️ Tech Stack

Java

Spring Boot

Spring Web (REST APIs)

YouTube Data API v3

JSON

Postman

Git & GitHub

🧩 Project Workflow

User provides a YouTube Channel ID

Spring Boot sends a request to YouTube Data API v3

API returns a JSON response

Application parses the response

Required channel and video details are returned via REST endpoint

🔑 API Configuration

Create a project in Google Cloud Console

Enable YouTube Data API v3

Generate an API Key

Add the API key in application.properties

youtube.api.key=YOUR_API_KEY

▶️ How to Run the Project

Clone the repository

git clone https://github.com/Samimahir/youtube-data-api.git


Open the project in IntelliJ / Eclipse

Add your API key in application.properties

Run the Spring Boot application

Test APIs using Postman or browser

📌 Sample API Endpoint
GET /youtube/channel/{channelId}


Returns:

Channel name

Subscriber count

Total views

Video details

🎯 Learning Outcomes

Hands-on experience with YouTube Data API

External API integration using Spring Boot

REST API development

JSON data parsing in Java

Backend application architecture

📂 Project Structure
src/main/java
 ├── controller
 ├── service
 ├── model
 └── config
