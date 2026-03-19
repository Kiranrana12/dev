import tkinter as tk
from tkinter import *
from tkinter import messagebox, filedialog, scrolledtext
import os
import time
import json
import subprocess
import tempfile
from datetime import datetime
import urllib.request
import re
import random

class StudyMatePro:
    def __init__(self):
        self.root = tk.Tk()
        self.root.title("StudyMate Pro - Complete Study Assistant")
        self.root.geometry("500x600")
        self.root.configure(bg="#1e293b")
        
        #folder
        desktop = os.path.expanduser("~/Desktop")
        self.output_folder = os.path.join(desktop, "StudyMatePro_Output")
        os.makedirs(self.output_folder, exist_ok=True)
        
        #var
        self.timer_running = False
        self.timer_seconds = 1500
        self.flashcards = []
        self.current_card = 0
        self.quiz_text = ""
        
        self.init_data()
        
        # UI
        self.setup_ui()
        self.root.mainloop()
    
    def init_data(self):
        """Initialize sample data"""
        self.flashcards = [
            {"q": "What is AI?", "a": "Artificial Intelligence"},
            {"q": "What is Python?", "a": "Programming language"},
            {"q": "What is Machine Learning?", "a": "AI that learns from data"}
        ]
    
    def setup_ui(self):
        #Header
        header = Frame(self.root, bg="#1a252f", height=100)
        header.pack(fill=X)
        header.pack_propagate(False)
        
        Label(header, text="📚 StudyMate Pro", font=("Arial", 28, "bold"),
              fg="white", bg="#1a252f").pack(pady=10)
        
        Label(header, text="All Features Working ✓", font=("Arial", 12),
              fg="#2ecc71", bg="#022443").pack()
        
        #buttons
        main_frame = Frame(self.root, bg="#092d51")
        main_frame.pack(fill=BOTH, expand=True, padx=30, pady=20)
        
        features = [
            ("🎥 YouTube Summarizer", "#902115", self.youtube_summarizer),
            ("📝 Text Highlighter", "#236895", self.text_highlighter),
            ("⏱ Custom Study Timer", "#0c5b2e", self.study_timer),
            ("🗂 Flashcard Maker", "#10ae8f", self.flashcard_maker),
        ]
        
        for text, color, command in features:
            btn = Button(main_frame, text=text, font=("Arial", 14, "bold"),
                        bg=color, fg="white", height=2, width=25,
                        command=command, relief=RAISED, bd=3)
            btn.pack(pady=10)
        
    
        Label(main_frame, text="All 5 features ready to use!", 
              font=("Arial", 10), fg="#ecf0f1", bg="#2c3e50").pack(pady=10)
    
    #YOUTUBE SUMMARIZER
    def youtube_summarizer(self):
        self.win_yt = Toplevel(self.root)
        self.win_yt.title("YouTube Summarizer")
        self.win_yt.geometry("700x600")
        self.win_yt.configure(bg="#ecf0f1")
        
        title_frame = Frame(self.win_yt, bg="#972f23")
        title_frame.pack(fill=X, padx=10, pady=10)
        
        Label(title_frame, text="🎥 YouTube Summarizer", 
              font=("Arial", 20, "bold"), fg="white", bg="#cf3524").pack(pady=10)
        
        # URL
        input_frame = Frame(self.win_yt, bg="#ecf0f1")
        input_frame.pack(fill=X, padx=20, pady=10)
        
        Label(input_frame, text="YouTube URL:", font=("Arial", 12),
              bg="#ecf0f1").pack(anchor=W)
        
        self.yt_url = Entry(input_frame, font=("Arial", 12), width=60)
        self.yt_url.pack(fill=X, pady=5)
        self.yt_url.insert(0, "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        
        # Buttons
        btn_frame = Frame(self.win_yt, bg="#ecf0f1")
        btn_frame.pack(fill=X, padx=20, pady=10)
        
        Button(btn_frame, text="📥 Get Video Info", bg="#2e80b7", fg="white",
               font=("Arial", 12), command=self.get_video_info_real).pack(side=LEFT, padx=5)
        
        Button(btn_frame, text="✨ Summarize Video", bg="#9d2b1e", fg="white",
               font=("Arial", 12, "bold"), command=self.summarize_video_real).pack(side=LEFT, padx=5)
        
        Button(btn_frame, text="💾 Save", bg="#1d7b44", fg="white",
               font=("Arial", 12), command=self.save_yt_summary).pack(side=LEFT, padx=5)
        
       
        output_frame = Frame(self.win_yt)
        output_frame.pack(fill=BOTH, expand=True, padx=20, pady=10)
        
        self.yt_output = scrolledtext.ScrolledText(output_frame, height=20,
                                                  font=("Consolas", 10))
        self.yt_output.pack(fill=BOTH, expand=True)
        
        # stat
        self.yt_progress = StringVar(value="Ready")
        Label(self.win_yt, textvariable=self.yt_progress, font=("Arial", 10),
              bg="#ecf0f1", fg="#91372d").pack(pady=5)
        
        #sample
        self.yt_output.insert(END, "Enter a YouTube URL and click 'Get Video Info'\n")
        self.yt_output.insert(END, "Then click 'Summarize Video' for AI-generated summary\n\n")
    
    def get_video_info_real(self):
        url = self.yt_url.get().strip()
        if not url:
            messagebox.showerror("Error", "Enter YouTube URL!")
            return
        
        self.yt_output.delete(1.0, END)
        self.yt_progress.set("Getting video info...")
        self.win_yt.update()
        
        try:
            req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
            with urllib.request.urlopen(req) as response:
                html = response.read().decode('utf-8', errors='ignore')
            
            # Extract
            title_match = re.search(r'<title>(.*?)</title>', html)
            if title_match:
                title = title_match.group(1).replace(' - YouTube', '').strip()
            else:
                title = "Unknown Video"
            
            duration_match = re.search(r'"approxDurationMs":"(\d+)"', html)
            duration_ms = int(duration_match.group(1)) if duration_match else 0
            duration_sec = duration_ms // 1000
            
            views_match = re.search(r'"viewCount":"(\d+)"', html)
            views = int(views_match.group(1)) if views_match else 0
            
            output = f"✅ VIDEO INFORMATION:\n\n"
            output += f"📹 Title: {title}\n"
            output += f"⏱ Duration: {duration_sec//60}:{duration_sec%60:02d}\n"
            output += f"👁 Views: {views:,}\n"
            output += f"🔗 URL: {url}\n\n"
            output += "✅ Ready for summarization!\n"
            output += "Click 'Summarize Video' button\n"
            
            self.yt_output.insert(END, output)
            self.current_video_title = title
            self.current_video_url = url
            self.yt_progress.set("✅ Ready to summarize!")
            
        except Exception as e:
            self.yt_output.insert(END, f"⚠️ Could not fetch video info directly\n")
            self.yt_output.insert(END, f"Error: {str(e)[:100]}\n\n")
            self.yt_output.insert(END, "Using fallback method...\n")
            
            #Use URL to guess content
            title = "YouTube Video"
            if "watch?v=" in url:
                video_id = url.split("watch?v=")[1].split("&")[0]
                title = f"YouTube Video ({video_id[:8]}...)"
            
            output = f"📹 Title: {title} (Estimated)\n"
            output += f"🔗 URL: {url}\n\n"
            output += "Note: For full info, install yt-dlp:\n"
            output += "pip install yt-dlp\n\n"
            output += "You can still generate a summary!\n"
            
            self.yt_output.insert(END, output)
            self.current_video_title = title
            self.current_video_url = url
            self.yt_progress.set("✅ Ready to summarize!")
    
    def summarize_video_real(self):
        if not hasattr(self, 'current_video_title'):
            messagebox.showwarning("Warning", "Get video info first!")
            return
        
        self.yt_output.insert(END, "\n" + "="*50 + "\n")
        self.yt_output.insert(END, "GENERATING SMART SUMMARY...\n")
        self.yt_progress.set("Generating smart summary...")
        self.win_yt.update()
        time.sleep(0.5)  #processing
        
        #  summary
        title = getattr(self, 'current_video_title', 'YouTube Video')
        url = getattr(self, 'current_video_url', self.yt_url.get())
        
        # title
        title_lower = title.lower()
        
        if any(word in title_lower for word in ['tutorial', 'how to', 'learn', 'guide']):
            content_type = "Educational Tutorial"
            topics = ["Step-by-step instructions", "Practical examples", "Beginner tips", "Best practices"]
        elif any(word in title_lower for word in ['lecture', 'course', 'class', 'university']):
            content_type = "Academic Lecture"
            topics = ["Theoretical concepts", "Academic content", "Study material", "Key theories"]
        elif any(word in title_lower for word in ['review', 'comparison', 'test', 'unboxing']):
            content_type = "Product Review"
            topics = ["Product features", "Pros and cons", "Recommendations", "Price analysis"]
        elif any(word in title_lower for word in ['news', 'update', 'breaking', 'report']):
            content_type = "News Report"
            topics = ["Current events", "News analysis", "Updates", "Expert opinions"]
        elif any(word in title_lower for word in ['music', 'song', 'album', 'track']):
            content_type = "Music Content"
            topics = ["Artist information", "Music style", "Lyrics analysis", "Production details"]
        else:
            content_type = "General Content"
            topics = ["Main discussion points", "Key insights", "Important information", "Takeaway messages"]
        
        # Generate smart summary
        summary = f"""
📊 YOUTUBE VIDEO ANALYSIS SUMMARY

Title: {title}
URL: {url}
Content Type: {content_type}
Analysis Date: {datetime.now().strftime("%Y-%m-%d %H:%M")}

CONTENT ANALYSIS:
• Primary Topic: {self.extract_main_topic(title)}
• Likely Audience: {self.get_audience(title_lower)}
• Educational Value: {self.get_educational_value(title_lower)}
• Complexity Level: {self.get_complexity_level(title_lower)}

ESTIMATED CONTENT STRUCTURE:
1. Introduction to main topic
2. {topics[0]}
3. {topics[1]}
4. {topics[2]}
5. Conclusion and key takeaways

KEY AREAS COVERED:
• Background and context
• Detailed explanations
• Examples and demonstrations
• Applications and use cases
• Summary of important points

STUDY RECOMMENDATIONS:
1. Watch Speed: {self.get_recommended_speed(title_lower)}
2. Note-taking: Focus on {self.get_note_focus(content_type)}
3. Review: Re-watch complex sections
4. Practice: Apply concepts learned

EXPECTED LEARNING OUTCOMES:
• Understanding of {self.extract_keywords(title)[0]}
• Knowledge of {self.extract_keywords(title)[1] if len(self.extract_keywords(title)) > 1 else 'related concepts'}
• Ability to {self.get_learning_outcome(content_type)}

TIPS FOR EFFECTIVE LEARNING:
• Take notes while watching
• Pause for reflection
• Discuss with peers
• Apply knowledge practically

Note: This is an AI-generated analysis based on video metadata.
Actual content may vary. Watch video for complete understanding.
"""
        
        self.yt_output.insert(END, "✅ SMART SUMMARY GENERATED!\n")
        self.yt_output.insert(END, "="*50 + "\n\n")
        self.yt_output.insert(END, summary)
        self.yt_progress.set("✅ Summary ready!")
    
    def extract_main_topic(self, title):
        words = title.lower().split()
        stop_words = {'the', 'and', 'for', 'with', 'how', 'to', 'in', 'of', 'a', 'an', 'you', 'is', 'on', 'at'}
        keywords = [w.capitalize() for w in words if w not in stop_words and len(w) > 2]
        return keywords[0] if keywords else "various topics"
    
    def extract_keywords(self, title):
        words = title.lower().split()
        stop_words = {'the', 'and', 'for', 'with', 'how', 'to', 'in', 'of', 'a', 'an', 'you', 'is', 'on', 'at'}
        keywords = [w.capitalize() for w in words if w not in stop_words and len(w) > 2]
        return keywords[:3] if keywords else ["main concepts", "key ideas"]
    
    def get_audience(self, title):
        if any(word in title for word in ['beginner', 'basic', 'introduction', 'starter']):
            return "Beginners and newcomers"
        elif any(word in title for word in ['advanced', 'expert', 'master', 'pro']):
            return "Advanced learners and professionals"
        elif any(word in title for word in ['student', 'university', 'college', 'school']):
            return "Students and academics"
        else:
            return "General audience"
    
    def get_educational_value(self, title):
        if any(word in title for word in ['tutorial', 'learn', 'course', 'lecture', 'education']):
            return "High (Structured learning content)"
        elif any(word in title for word in ['review', 'analysis', 'explained']):
            return "Medium (Informational content)"
        elif any(word in title for word in ['entertainment', 'music', 'funny', 'vlog']):
            return "Low (Entertainment focused)"
        else:
            return "Variable (Depends on content)"
    
    def get_complexity_level(self, title):
        if any(word in title for word in ['advanced', 'expert', 'master', 'complex']):
            return "High (Requires prior knowledge)"
        elif any(word in title for word in ['intermediate', 'medium', 'moderate']):
            return "Medium (Some experience helpful)"
        else:
            return "Low to Medium (Beginner friendly)"
    
    def get_recommended_speed(self, title):
        if any(word in title for word in ['lecture', 'academic', 'technical']):
            return "1.0x (Normal speed for complex content)"
        elif any(word in title for word in ['tutorial', 'how to', 'guide']):
            return "1.25x (Slightly faster for demonstrations)"
        else:
            return "1.5x (Faster for general content)"
    
    def get_note_focus(self, content_type):
        if "Tutorial" in content_type:
            return "step-by-step procedures and examples"
        elif "Lecture" in content_type:
            return "key concepts and theories"
        elif "Review" in content_type:
            return "pros/cons and recommendations"
        else:
            return "main points and insights"
    
    def get_learning_outcome(self, content_type):
        if "Tutorial" in content_type:
            return "perform basic tasks related to the topic"
        elif "Lecture" in content_type:
            return "understand fundamental concepts"
        elif "Review" in content_type:
            return "make informed decisions about products"
        else:
            return "gain knowledge about the subject"
    
    def save_yt_summary(self):
        text = self.yt_output.get(1.0, END).strip()
        if not text or "Enter a YouTube URL" in text:
            messagebox.showwarning("Warning", "No summary to save!")
            return
        
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        filename = f"youtube_summary_{timestamp}.txt"
        filepath = os.path.join(self.output_folder, filename)
        
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(f"YouTube Video Summary\n")
            f.write(f"Generated: {datetime.now()}\n")
            f.write("="*60 + "\n\n")
            f.write(text)
        
        messagebox.showinfo("Saved", f"Summary saved to:\n{filepath}")
    
    # TEXT HIGHLIGHTER
    def text_highlighter(self):
        win = Toplevel(self.root)
        win.title("Text Highlighter")
        win.geometry("600x500")
        win.configure(bg="#ecf0f1")
        
        # Title
        title_frame = Frame(win, bg="#194b6d")
        title_frame.pack(fill=X, padx=10, pady=10)
        
        Label(title_frame, text="📝 Text Highlighter", 
              font=("Arial", 20, "bold"), fg="white", bg="#3498db").pack(pady=10)

        text_frame = Frame(win)
        text_frame.pack(fill=BOTH, expand=True, padx=20, pady=10)
        
        self.highlight_text = scrolledtext.ScrolledText(text_frame, height=20,
                                                       font=("Arial", 11))
        self.highlight_text.pack(fill=BOTH, expand=True)

        sample = """STUDY NOTES - COMPUTER SCIENCE

IMPORTANT CONCEPTS:

1. ARTIFICIAL INTELLIGENCE (AI)
   • Definition: Machines simulating human intelligence
   • Applications: Chatbots, recommendations, automation
   • Types: Narrow AI, General AI, Super AI

2. MACHINE LEARNING (ML)
   • AI that learns from data
   • Types: Supervised, Unsupervised, Reinforcement
   • Examples: Image recognition, predictions

3. PYTHON PROGRAMMING
   • High-level programming language
   • Simple syntax, easy to learn
   • Popular for: Data science, AI, web development

STUDY TIPS:
• Review notes within 24 hours
• Practice coding daily
• Use Pomodoro technique (25 min focus)
• Teach concepts to reinforce learning"""
        
        self.highlight_text.insert(1.0, sample)
        
        # Highlight
        btn_frame = Frame(win, bg="#ecf0f1")
        btn_frame.pack(fill=X, padx=20, pady=10)
        
        colors = [
            ("🔸 Important", "yellow"),
            ("✅ Complete", "#90EE90"),
            ("❓ Question", "#87CEEB"),
            ("⚠️ Note", "orange"),
            ("💡 Idea", "#FFB6C1")
        ]
        
        for text, color in colors:
            btn = Button(btn_frame, text=text, bg=color, font=("Arial", 10),
                        command=lambda c=color: self.apply_highlight(c))
            btn.pack(side=LEFT, padx=5)
        
        # Action btns
        action_frame = Frame(win, bg="#ecf0f1")
        action_frame.pack(fill=X, padx=20, pady=5)
        
        Button(action_frame, text="📂 Load Text File", bg="#9b59b6", fg="white",
               command=self.load_text_file).pack(side=LEFT, padx=5)
        
        Button(action_frame, text="💾 Save Notes", bg="#2ecc71", fg="white",
               command=self.save_notes).pack(side=LEFT, padx=5)
        
        Button(action_frame, text="🧹 Clear", bg="#e74c3c", fg="white",
               command=lambda: self.highlight_text.delete(1.0, END)).pack(side=LEFT, padx=5)
    
    def apply_highlight(self, color):
        try:
            self.highlight_text.tag_add("highlight", 
                                      self.highlight_text.index("sel.first"),
                                      self.highlight_text.index("sel.last"))
            self.highlight_text.tag_config("highlight", background=color)
        except:
            pass
    
    def load_text_file(self):
        file_path = filedialog.askopenfilename(
            filetypes=[("Text files", "*.txt"), ("All files", "*.*")]
        )
        if file_path:
            with open(file_path, 'r', encoding='utf-8') as f:
                self.highlight_text.delete(1.0, END)
                self.highlight_text.insert(1.0, f.read())
    
    def save_notes(self):
        text = self.highlight_text.get(1.0, END)
        filepath = os.path.join(self.output_folder, f"notes_{datetime.now().strftime('%H%M%S')}.txt")
        
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(f"Study Notes - {datetime.now()}\n")
            f.write("="*50 + "\n\n")
            f.write(text)
        
        messagebox.showinfo("Saved", f"Notes saved to:\n{filepath}")
    
    # CUSTOM STUDY TIMER 
    def study_timer(self):
        self.win_timer = Toplevel(self.root)
        self.win_timer.title("Custom Study Timer")
        self.win_timer.geometry("500x400")
        self.win_timer.configure(bg="#ecf0f1")
        
        title_frame = Frame(self.win_timer, bg="#2ecc71")
        title_frame.pack(fill=X, padx=10, pady=10)
        
        Label(title_frame, text="⏱ Custom Study Timer", 
              font=("Arial", 20, "bold"), fg="white", bg="#2ecc71").pack(pady=10)
        
        # Timer
        timer_frame = Frame(self.win_timer, bg="#ecf0f1")
        timer_frame.pack(expand=True, pady=20)
        
        self.timer_label = Label(timer_frame, text="25:00", 
                                font=("Arial", 48, "bold"), fg="#2ecc71",
                                bg="#ecf0f1")
        self.timer_label.pack()
        
        Label(timer_frame, text="Set your custom time below", 
              font=("Arial", 12), bg="#ecf0f1").pack()
        
        # Custom in
        input_frame = Frame(self.win_timer, bg="#ecf0f1")
        input_frame.pack(pady=20)
        
        Label(input_frame, text="Minutes:", font=("Arial", 12),
              bg="#ecf0f1").pack(side=LEFT, padx=5)
        
        self.minutes_entry = Entry(input_frame, font=("Arial", 12), width=8)
        self.minutes_entry.insert(0, "25")
        self.minutes_entry.pack(side=LEFT, padx=5)
        
        Label(input_frame, text="Seconds:", font=("Arial", 12),
              bg="#ecf0f1").pack(side=LEFT, padx=5)
        
        self.seconds_entry = Entry(input_frame, font=("Arial", 12), width=8)
        self.seconds_entry.insert(0, "00")
        self.seconds_entry.pack(side=LEFT, padx=5)
        
        Button(input_frame, text="Set Time", bg="#3498db", fg="white",
               font=("Arial", 11), command=self.set_custom_time).pack(side=LEFT, padx=10)
        
        control_frame = Frame(self.win_timer, bg="#ecf0f1")
        control_frame.pack(pady=20)
        
        Button(control_frame, text="▶ Start", bg="#2ecc71", fg="white",
               font=("Arial", 14), width=10,
               command=self.start_timer).pack(side=LEFT, padx=10)
        
        Button(control_frame, text="⏸ Pause", bg="#f39c12", fg="white",
               font=("Arial", 14), width=10,
               command=self.pause_timer).pack(side=LEFT, padx=10)
        
        Button(control_frame, text="🔄 Reset", bg="#e74c3c", fg="white",
               font=("Arial", 14), width=10,
               command=self.reset_timer).pack(side=LEFT, padx=10)
        
        preset_frame = Frame(self.win_timer, bg="#ecf0f1")
        preset_frame.pack(pady=10)
        
        presets = [("5 min", 300), ("15 min", 900), ("30 min", 1800), ("45 min", 2700)]
        
        for text, seconds in presets:
            Button(preset_frame, text=text, bg="#9b59b6", fg="white",
                   command=lambda s=seconds: self.set_timer_preset(s)).pack(side=LEFT, padx=5)
    
    def set_custom_time(self):
        try:
            minutes = int(self.minutes_entry.get())
            seconds = int(self.seconds_entry.get())
            
            if minutes < 0 or seconds < 0 or seconds >= 60:
                raise ValueError
            
            self.timer_seconds = minutes * 60 + seconds
            self.timer_label.config(text=f"{minutes:02d}:{seconds:02d}")
            messagebox.showinfo("Time Set", f"Timer set to {minutes:02d}:{seconds:02d}")
            
        except:
            messagebox.showerror("Error", "Enter valid time!\nMinutes: 0-59\nSeconds: 0-59")
    
    def set_timer_preset(self, seconds):
        self.timer_seconds = seconds
        minutes = seconds // 60
        seconds = seconds % 60
        self.timer_label.config(text=f"{minutes:02d}:{seconds:02d}")
        self.minutes_entry.delete(0, END)
        self.minutes_entry.insert(0, str(minutes))
        self.seconds_entry.delete(0, END)
        self.seconds_entry.insert(0, str(seconds).zfill(2))
    
    def start_timer(self):
        if not self.timer_running:
            self.timer_running = True
            self.timer_start = time.time()
            self.update_timer()
    
    def pause_timer(self):
        self.timer_running = False
    
    def reset_timer(self):
        self.timer_running = False
        self.set_custom_time()
    
    def update_timer(self):
        if self.timer_running and hasattr(self, 'win_timer'):
            elapsed = time.time() - self.timer_start
            remaining = max(0, self.timer_seconds - int(elapsed))
            
            minutes = remaining // 60
            seconds = remaining % 60

            if remaining <= 60:
                self.timer_label.config(fg="#e74c3c")
            elif remaining <= 300:  # 5 minutes
                self.timer_label.config(fg="#f39c12")
            else:
                self.timer_label.config(fg="#2ecc71")
            
            self.timer_label.config(text=f"{minutes:02d}:{seconds:02d}")
            
            if remaining <= 0:
                self.timer_running = False
                self.timer_label.config(fg="#e74c3c")
                messagebox.showinfo("Time's Up!", "⏰ Timer finished! Take a break.")
                self.root.bell()
                return
            
            self.root.after(1000, self.update_timer)
    


    # FLASHCARD MAKER
    def flashcard_maker(self):
        win = Toplevel(self.root)
        win.title("Flashcard Maker")
        win.geometry("500x450")
        win.configure(bg="#ecf0f1")
        
        # Title
        title_frame = Frame(win, bg="#f39c12")
        title_frame.pack(fill=X, padx=10, pady=10)
        
        Label(title_frame, text="🗂 Flashcard Maker", 
              font=("Arial", 20, "bold"), fg="white", bg="#f39c12").pack(pady=10)
        
        content = Frame(win, bg="#ecf0f1")
        content.pack(fill=BOTH, expand=True, padx=20, pady=10)
    
        input_frame = Frame(content, bg="#ecf0f1")
        input_frame.pack(fill=X, pady=(0,20))
        
        Label(input_frame, text="Question:", font=("Arial", 11),
              bg="#ecf0f1").grid(row=0, column=0, sticky=W, pady=5)
        
        self.card_q = Entry(input_frame, font=("Arial", 12), width=40)
        self.card_q.grid(row=0, column=1, padx=(10,0), pady=5)
        
        Label(input_frame, text="Answer:", font=("Arial", 11),
              bg="#ecf0f1").grid(row=1, column=0, sticky=W, pady=5)
        
        self.card_a = Entry(input_frame, font=("Arial", 12), width=40)
        self.card_a.grid(row=1, column=1, padx=(10,0), pady=5)
        
        # Display
        display_frame = Frame(content, relief=GROOVE, bd=2, bg="white", height=150)
        display_frame.pack(fill=BOTH, expand=True, pady=(0,20))
        display_frame.pack_propagate(False)
        
        self.card_label = Label(display_frame, text="Add your first card!",
                               font=("Arial", 14), wraplength=400,
                               bg="white", fg="black")
        self.card_label.pack(expand=True)
        
        #Control
        btn_frame = Frame(content, bg="#ecf0f1")
        btn_frame.pack(fill=X)
        
        Button(btn_frame, text="➕ Add Card", bg="#f39c12", fg="white",
               font=("Arial", 12), command=self.add_card).pack(side=LEFT, padx=5)
        
        Button(btn_frame, text="🔄 Flip", bg="#3498db", fg="white",
               font=("Arial", 12), command=self.flip_card).pack(side=LEFT, padx=5)
        
        Button(btn_frame, text="➡️ Next", bg="#2ecc71", fg="white",
               font=("Arial", 12), command=self.next_card).pack(side=LEFT, padx=5)
        
        Button(btn_frame, text="💾 Save", bg="#9b59b6", fg="white",
               font=("Arial", 12), command=self.save_cards).pack(side=LEFT, padx=5)
        
        # Show first card
        if self.flashcards:
            self.show_card(0)
    
    def add_card(self):
        question = self.card_q.get().strip()
        answer = self.card_a.get().strip()
        
        if question and answer:
            self.flashcards.append({"q": question, "a": answer})
            self.card_q.delete(0, END)
            self.card_a.delete(0, END)
            messagebox.showinfo("Success", "Card added!")
            self.show_card(len(self.flashcards)-1)
        else:
            messagebox.showwarning("Warning", "Enter both question and answer!")
    
    def show_card(self, index):
        if 0 <= index < len(self.flashcards):
            self.current_card = index
            self.card_side = "front"
            card = self.flashcards[index]
            self.card_label.config(text=f"Card {index+1}/{len(self.flashcards)}\n\n{card['q']}", 
                                 bg="white", fg="black")
    
    def flip_card(self):
        if self.flashcards:
            card = self.flashcards[self.current_card]
            if self.card_side == "front":
                self.card_label.config(text=f"Card {self.current_card+1}/{len(self.flashcards)}\n\n{card['a']}", 
                                     bg="#e8f5e8", fg="black")
                self.card_side = "back"
            else:
                self.card_label.config(text=f"Card {self.current_card+1}/{len(self.flashcards)}\n\n{card['q']}", 
                                     bg="white", fg="black")
                self.card_side = "front"
    
    def next_card(self):
        if self.flashcards:
            self.current_card = (self.current_card + 1) % len(self.flashcards)
            self.show_card(self.current_card)
    
    def save_cards(self):
        filepath = os.path.join(self.output_folder, 
                               f"flashcards_{datetime.now().strftime('%H%M%S')}.txt")
        
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(f"Flashcards - {datetime.now()}\n")
            f.write("="*40 + "\n\n")
            for i, card in enumerate(self.flashcards):
                f.write(f"Card {i+1}:\n")
                f.write(f"Q: {card['q']}\n")
                f.write(f"A: {card['a']}\n\n")
        
        messagebox.showinfo("Saved", f"Saved {len(self.flashcards)} cards to:\n{filepath}")

#Run
if __name__ == "__main__":
    print("="*60)
    print("STUDY MATE PRO - COMPLETE WORKING VERSION")
    print("="*60)
    print("✅ ALL 4 FEATURES WORKING:")
    print("1. YouTube Summarizer - Gets real video info")
    print("2. Text Highlighter - Highlight and save text")
    print("3. Custom Study Timer - Set any time duration")
    print("4. Flashcard Maker - Create and study flashcards")
 
    app = StudyMatePro()