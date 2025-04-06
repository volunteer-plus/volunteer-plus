INSERT INTO email_template(email_message_tag, subject, body) VALUES
    (
        'EMAIL_MESSAGE_TAG_5',
        'Волонтер+: аналіз звітів',
        '
           <!DOCTYPE html>
           <html>
           <head>
               <title>Аналіз звітів на платформі "Волонтер+"</title>
           </head>
           <body>
               <h1>Основний аналіз звітів</h1>
               <p>Шановний <span th:text="\${userName}"></span>,</p>
               <p>Ми хочемо надати Вам AI-аналіз звітів з платформи Волонтер+</p>
               <p><span th:text="\${data}"></span></p>
               <p>Дякуємо за вашу активність на нашій платформі.</p>
               <p>З повагою,<br>Команда Волонтер+</p>
           </body>
           </html>
        '
    );
