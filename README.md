# 🚀 Field Sales Automation & Order Tracking System

[![Platform](https://img.shields.io/badge/Platform-Android_Java-brightgreen.svg)](https://developer.android.com/)
[![Backend](https://img.shields.io/badge/Backend-Laravel_11_Sanctum-red.svg)](https://laravel.com/)
[![Database](https://img.shields.io/badge/Database-MySQL-blue.svg)](https://www.mysql.com/)

**Field Sales Automation** একটি কমপ্লিট সলিউশন যা মাঠ পর্যায়ের বিক্রয় কর্মীদের সেলস অর্ডার এবং বকেয়া (Due) কালেকশনকে ডিজিটালাইজ করে। এটি একটি ইন্ডাস্ট্রি-গ্রেড অ্যাপ যা জটিল হিসাব-নিকাশ সহজ করতে এবং রিয়েল-টাইম ডাটা সিঙ্ক্রোনাইজেশন নিশ্চিত করতে তৈরি করা হয়েছে।
---
## 🌍 Live Demo & Official App Download

আমাদের সিস্টেমের লাইভ ডেমো এবং লেটেস্ট অ্যাপ্লিকেশনটি নিচের বাটনগুলো ব্যবহার করে সরাসরি অ্যাক্সেস করতে পারেন:

| **Laravel Admin Control Panel** | **Android App Download (v1.0.0)** |
| :---: | :---: |
| [![Admin Panel](https://github.com/user-attachments/assets/9b0df085-c90a-4f06-96bc-9ed6380dff3d)](https://futuretechbd.net/fieldsales) | [![Download APK](https://img.shields.io/badge/DOWNLOAD-APK_v1.0.0-success?style=for-the-badge&logo=android&logoColor=white)](https://github.com/developershahiduzzaman/Field-Sales-Automation-Android-App-With-Admin-Panel/releases/download/v1.0.0/app-release.apk) |
| [🌐 Visit Admin Portal](https://futuretechbd.net/fieldsales) | [📥 Get the App](https://github.com/developershahiduzzaman/Field-Sales-Automation-Android-App-With-Admin-Panel/releases/download/v1.0.0/app-release.apk) |

---

### 🔑 Demo Credentials for Testing
আপনি নিচের তথ্যগুলো ব্যবহার করে সরাসরি অ্যাডমিন প্যানেলে লগইন করতে পারেন:

* **URL:** [futuretechbd.net/fieldsales](https://futuretechbd.net/fieldsales)
* **Email:** `admin@gmail.com`
* **Password:** `12345678`

> **Note:** এটি একটি লাইভ প্রোডাকশন বিল্ড। ডেমো ব্যবহারের সময় কোনো গুরুত্বপূর্ণ ডাটা পরিবর্তন বা ডিলিট না করার অনুরোধ রইল।

---

## 📅 The 20-Day Development Journey

### 🏗️ Phase 1: Foundation & Backend (Day 1 - 8)
- **Analysis:** বিজনেস প্রবলেম অ্যানালাইসিস এবং MVP ফিচার লিস্ট ফাইনাল করা।
- **DB Design:** UUID ব্যবহার করে স্কেলেবল ERD তৈরি (Users, Outlets, Products, Orders)।
- **Laravel Setup:** Laravel 11 এবং Sanctum ব্যবহার করে সিকিউর API আর্কিটেকচার।
- **Order Logic:** মাল্টিপল প্রোডাক্ট হ্যান্ডলিং এবং ডাটা ইন্টিগ্রিটি নিশ্চিত করা।
- **Stability:** রিগোরাস API টেস্টিং এবং এজ-কেস এরর হ্যান্ডলিং।

### 📱 Phase 2: Android Development & UI/UX (Day 9 - 14)
- **Architecture:** Scalable MVVM আর্কিটেকচার এবং Retrofit নেটওয়ার্কিং সেটআপ।
- **Premium UI:** Lottie Animation এবং Material Design ব্যবহার করে লগইন ও প্রোডাক্ট ক্যাটালগ।
- **Admin Panel:** লারাভেল দিয়ে সেন্ট্রালাইজড ড্যাশবোর্ড (Live Search, CRUD, Image Handling)।
- **Shop Management:** অ্যাপ থেকে সরাসরি আউটলেট রেজিস্ট্রেশন এবং ডাটা সিঙ্ক্রোনাইজেশন।

### 💰 Phase 3: Sales, Due & Profile Management (Day 15 - 20)
- **Order System:** ইন-অ্যাপ মেমো ভিউ এবং এক ক্লিকে **WhatsApp**-এ ইনভয়েস শেয়ারিং।
- **Reporting:** লারাভেল সাব-কোয়েরি ব্যবহার করে ডুপ্লিকেট ডাটা মুক্ত নির্ভুল সেলস রিপোর্ট।
- **Due Management:** অটোমেটেড ডিউ ট্র্যাকিং এবং ইনস্ট্যান্ট কালেকশন সিঙ্ক সিস্টেম (Real-time update on Paid vs Due)।
- **Profile Module:** Multipart API ব্যবহার করে প্রোফাইল পিকচার আপলোড এবং Android 13+ পারমিশন হ্যান্ডেল।

---

## ✨ Key Features 

* **🔐 Secure Auth:** Laravel Sanctum ভিত্তিক টোকেন ম্যানেজমেন্ট ও সেশন সিকিউরিটি।
* **💸 Smart Due Tracking:** অর্ডার প্লেসমেন্টের সাথে সাথে স্বয়ংক্রিয়ভাবে বকেয়া হিসাব আপডেট।
* **📊 Performance Dashboard:** অফিসারদের ডেইলি টার্গেট বনাম অ্যাচিভমেন্টের রিয়েল-টাইম গ্রাফ।
* **📄 Digital Memo:** ডিজিটাল ইনভয়েস জেনারেশন এবং কাস্টমারদের সাথে সরাসরি শেয়ারিং।
* **🖼️ Profile Sync:** ইমেজ আপলোড সহ ডাইনামিক ইউজার প্রোফাইল ম্যানেজমেন্ট।

---

## 🛠️ Technology Stack

- **Android:** Java, MVVM, Retrofit, Glide, Lottie, Material Components.
- **Backend:** Laravel 11, Sanctum API, AJAX Live Search.
- **Database:** MySQL (Relational Schema with Indexing).
- **Communication:** RESTful APIs with JSON.

---
## 📸 App Showcase

### 📱 Android Mobile Application
<p align="center">
  <img src="https://github.com/user-attachments/assets/dbf83860-daf6-44a2-9b2d-671b3261e6b7" width="200" title="Splash Screen">
  <img src="https://github.com/user-attachments/assets/9f15943d-6c15-4c3b-a1e0-c97c82ec1f03" width="200" title="Login UI">
  <img src="https://github.com/user-attachments/assets/5383055f-0f7e-4229-9305-2412c6c91f56" width="200" title="Dashboard">
  <img src="https://github.com/user-attachments/assets/5c3856d0-e172-43d8-b104-6fbee1836206" width="200" title="Product List">
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/a8cab707-a12b-4ed0-80fd-0904f2b8a84c" width="200" title="Order Management">
  <img src="https://github.com/user-attachments/assets/cf245dae-f313-44a7-b279-3630ae464d31" width="200" title="In-App Memo">
  <img src="https://github.com/user-attachments/assets/cdc833a2-c0e6-4f83-8b44-9d84f9cf9f1c" width="200" title="Due Collection">
  <img src="https://github.com/user-attachments/assets/cd6cb26e-0934-46e5-9fae-34227b05ccbf" width="200" title="Profile View">
</p>

### 💻 Laravel Admin Control Panel
<p align="center">
  <img src="https://github.com/user-attachments/assets/9b0df085-c90a-4f06-96bc-9ed6380dff3d" width="100%" title="Web Dashboard">
</p>
