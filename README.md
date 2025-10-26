# Modern Grade Calculator (Java Swing)

A modern, dark-themed **Java Swing** application to calculate grades.  
Add/remove subject rows dynamically, enter marks, view **per-subject grades** and an **overall grade** with clean UI and instant validation.

https://github.com/<your-username>/modern-swing-grade-calculator

---

## ✨ Features

- 🌓 Dark, modern UI (custom Nimbus tweaks)
- ➕ Add / ➖ Remove subjects on the fly
- ✅ Field validation (invalid ranges & non-numbers)
- 🧮 Per-subject letter grade + overall grade
- ⌨️ Keyboard friendly (press **Enter** to Calculate)
- 🎯 Clean OOP design in a single file for easy learning

---

## 🅰️ Grading Scale

| Percentage | Grade |
|-----------:|:-----:|
| 95%+       |  O    |
| 90%–94.99% |  A+   |
| 85%–89.99% |  A    |
| 80%–84.99% |  B+   |
| 75%–79.99% |  B    |
| 70%–74.99% |  C+   |
| 65%–69.99% |  C    |
| < 65%      |  F    |

> You can customize thresholds in `getGradeFromPercentage(double percentage)`.

---

## 📦 Tech Stack

- Language: **Java 8+**
- GUI: **Swing** + **Nimbus** Look & Feel (with dark theme overrides)
- Storage: **In-memory** (no DB)

---

## 🚀 Run Locally

### 1) Download the source
Save the file as **`GradeCalculator.java`** in an empty folder.

### 2) Compile
```bash
javac GradeCalculator.java
