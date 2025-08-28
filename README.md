
# 📢 Notification Engine

An intelligent email notification system that automatically sends alerts for upcoming training sessions based on their scheduled time.

---

##  Purpose

The Notification Engine is designed to streamline communication by automatically sending email reminders for scheduled trainings to ensure participants are notified well in advance.

---

##  Tech Stack

| Layer       | Technology       |
|-------------|------------------|
| Backend     | Spring Boot       |
| Frontend    | React.js (VS Code)|
| Database    | MySQL             |
| Email       | Gmail SMTP        |
| IDE         | IntelliJ IDEA (Backend) |

---

🌟 **Key Features**
📅 Dashboard View: Visual overview of all scheduled trainings

✉️ **Automated Email Notifications:**

Sent 2 days, 1 day, 1 hour, and 30 minutes before training

➕ Add Trainings: Easily add new training sessions from UI

✅ Mark/Select Trainings: Checkbox selection for bulk actions

🔄 Reschedule Feature: Reschedule multiple trainings at once

🗑️ Delete Option: Remove outdated or incorrect entries

🔍 Search & Filter: Find trainings by title, trainer, or email

---

## 🖥️ Project Preview

![UI Screenshot](https://github.com/user-attachments/assets/5eccacf9-218c-4f7b-af5c-60982fcf7ac4)


> Modern, responsive, and user-friendly interface built using React.

---

## 🔧 Configuration

### 📬 Email Setup (Gmail SMTP)
Configure the following in your `application.properties` (Spring Boot):

src/main/resources/application.properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/notificationdb
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Gmail SMTP for Email Notifications
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true


⚠️ Important Setup Notes:

✅ Use port 587 for TLS encryption.
✅ Make sure to enable "Less secure app access" or use an App Password from your Google account.

🗂️ Project Structure
**Backend – Spring Boot**

<img width="567" height="581" alt="image" src="https://github.com/user-attachments/assets/8babc57c-3040-4ee0-994f-c58224511d46" />


**Frontend – React (Vite)**
<img width="273" height="380" alt="image" src="https://github.com/user-attachments/assets/edcf4e96-7329-4362-aaff-f91a1dbbc677" />


🚀 How to Run the Project
🧩 Backend (Spring Boot)
1.Open the project in IntelliJ IDEA or any Java IDE.

2.Make sure MySQL is running and the notificationdb database is created.

3.Update your application.properties with correct DB and email credentials.

4.Run the project using the main class: NotificationEngineApplication.java
Spring Boot will start at:
👉 http://localhost:8080

💻 Frontend (React with Vite)
1.Open the notification-engine folder in VS Code or any code editor.

2.Install the dependencies: npm install
3.Run the React development server: npm run dev
Frontend will run at:
👉 http://localhost:5173

