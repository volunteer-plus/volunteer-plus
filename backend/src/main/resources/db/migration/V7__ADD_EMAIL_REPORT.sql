INSERT INTO email_template(email_message_tag, subject, body) VALUES
    (
        'EMAIL_MESSAGE_TAG_1',
        'Волонтер+: створений новий звіт',
        '
           <!DOCTYPE html>
           <html>
           <head>
               <title>Новий звіт на платформі "Волонтер+" доступний для перегляду</title>
           </head>
           <body>
               <h1>Новий звіт доступний</h1>
               <p>Шановний <span th:text="\${userName}"></span>,</p>
               <p>Ми хочемо повідомити вас, що на платформі Волонтер+ доступний новий звіт.</p>
               <p><span th:text="\${data}"></span></p>
               <p>Дякуємо за вашу активність на нашій платформі.</p>
               <p>З повагою,<br>Команда Волонтер+</p>
           </body>
           </html>
        '
    );
