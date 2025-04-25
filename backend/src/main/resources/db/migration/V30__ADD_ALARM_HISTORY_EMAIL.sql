INSERT INTO email_template(email_message_tag, subject, body) VALUES
    (
        'EMAIL_MESSAGE_TAG_7',
        'Волонтер+: Статистика повітряних тривог',
        '
           <!DOCTYPE html>
           <html>
           <head>
               <title><span th:text="\${subject}"></span> платформа "Волонтер+"</title>
           </head>
           <body>
               <h1>Основний аналіз статистики повітряних тривог</h1>
               <p>Шановний <span th:text="\${userName}"></span>,</p>
               <p>Ми хочемо надати Вам AI-аналіз Cтатистика повітряних тривог з платформи Волонтер+</p>
               <p><span th:text="\${data}"></span></p>
               <p>Дякуємо за вашу активність на нашій платформі.</p>
               <p>З повагою,<br>Команда Волонтер+</p>
           </body>
           </html>
        '
    );
