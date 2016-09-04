## Symphony FAQbot

Symphony FAQbot is designed to capture questions and answers from the community.  The primary goal is to improve productivity by reducing time spent asking and answering the same questions over and over again.  FAQbot attempts to accomplish this by curating questions and answers.
FAQbot works by having conversations that start with a question search.
Sometimes it may be desirable to, in the middle of a conversation, start over with a new conversation.  To do this, simply send FAQbot "reset".
Please note that FAQbot is a work in progress.  Some features that are planned, but not yet implemented, are:
* Handling of at-mentions: FAQbot does not yet understand at-mentions, and therefore ignores them.  This will be fixed soon.
* Scoreboard: Display a ranking of contributors to FAQbot, ordered by most highly voted answers contributed.
* Subject Matter Experts: When a new question is posted, FAQbot will attempt to determine what it's about and if there exist any known experts on that topic.  If so, FAQbot will forward the question to the subject matter experts.
* Automatic Answer Generation: When a new question is posted, FAQbot will attempt to answer the question automatically by searching known repositories of documentation.  If the user deems one of these answers to be adequate, then the answer shall be recorded for posterity (and voting!).

FAQbot is currently designed to be used only in 1-on-1 conversations.  It will work in a chatroom as-is, however it will answer every message and therefore be quite annoying.  This will be improved by having it respond only to at-mentions when in a chatroom.

#### to run faqbot:

1. docker-compose up

2. run SymphonyFaqbotApp.java

#### to configure faqbot:

1. edit src/main/resources/faqbot.properties