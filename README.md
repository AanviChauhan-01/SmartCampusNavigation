# Smart Campus Navigation System

An intelligent desktop-based campus navigation system built using Java, designed to simulate real-world smart campus behavior with dynamic routing, crowd awareness, and personalized scheduling.


## Overview

This project integrates **graph algorithms, real-time simulation, and database-driven user personalization** to create a smart navigation experience within a campus environment.

Unlike traditional static route planners, this system adapts dynamically based on:
- User timetable
- Crowd density
- Real-time class schedules

---

## Key Features

### User Authentication
- Secure Login & Registration system
- User-specific data stored using MySQL
- Persistent timetable management

### Smart Route Navigation
- Implements **Dijkstra’s Algorithm** for shortest path calculation
- Automatically computes optimal routes between locations
- Displays path visually on an interactive map

### Real-Time Crowd Simulation
- Simulates crowd levels dynamically across campus paths
- Crowd levels categorized as:
  - 🟢 LOW
  - 🟡 MEDIUM
  - 🔴 HIGH
- Visual consistency between map and crowd status panel

### Intelligent Timetable System
- Add and manage class schedules
- Automatically loads saved timetable on login
- Triggers:
  - Class reminders
  - Auto-navigation to class location

### Alternate Route Suggestions
- Suggests alternative paths by adjusting path weights
- Helps avoid crowded routes

### Interactive Map UI
- Built using Java Swing
- Live path highlighting
- Animated user movement across nodes

---

## System Architecture

- **Frontend:** Java Swing (UI + Map Visualization)
- **Backend Logic:** Java (Graph + Algorithms)
- **Database:** MySQL
- **Connectivity:** JDBC (MySQL Connector/J)

---

## Technologies Used

- Java
- Java Swing
- MySQL
- JDBC
- Graph Data Structures
- Dijkstra Algorithm

---

##  Project Structure

SmartCampus/
│
├── SmartCampus.java # Entry point
├── UI.java # Main UI logic
├── LoginUI.java # Login system
├── MapPanel.java # Map visualization
├── Graph.java # Graph structure
├── Node.java # Node representation
├── Edge.java # Edge with weights + crowd
├── Dijkstra.java # Pathfinding algorithm
├── CrowdSimulator.java # Crowd logic
├── Timetable.java # Timetable management
├── TimeSlot.java # Class structure
├── DBConnection.java # Database connection

## Setup Instructions

### 1. Database Setup

### 2. Add JDBC Driver
Download MySQL Connector/J and place the .jar file in your project directory.

### 3. Compile & Run
javac -cp ".;mysql-connector-j-8.x.x.jar" *.java
java -cp ".;mysql-connector-j-8.x.x.jar" SmartCampus

### How It Works
User logs in or registers
Timetable loads from database
User selects or adds a class
System:
Automatically sets destination
Computes shortest path
Updates crowd dynamically
Map visualizes:
Optimal route
Crowd density
User movement

#### Future Enhancements

### 1. AI-Based Crowd Prediction & Management
This system can be extended into an intelligent crowd management platform by integrating machine learning and real-time data analytics.
In large-scale environments such as:
- Railway stations
- Concert venues
- Mass gatherings (e.g., Kumbh Mela)
- Public rallies and events

the system can utilize AI models to:

- Predict crowd density using historical and real-time data
- Analyze live CCTV feeds using computer vision
- Detect potential congestion zones before they become critical
- Suggest safer alternative routes dynamically
- Generate early warnings for authorities

### 2. Smart Decision Support System
By combining:
- Graph-based routing (current system)
- AI prediction models
- Real-time sensor/CCTV data

the system can evolve into a **decision-support tool for authorities**, helping in:

- Efficient crowd control by police
- Preventing stampedes and unsafe crowd build-ups
- Optimizing pedestrian movement in real-time
- Dynamic redirection during emergencies

### 3. Scalable Smart Infrastructure Integration

Future versions can be integrated with:
- IoT sensors
- Mobile applications
- GPS-based tracking
- Public information systems

to create a fully connected **Smart City Crowd Management System**.

## Author: Aanvi Chauhan

This project demonstrates the integration of algorithms, UI design, and database systems to solve a real-world problem in a smart and scalable way.

