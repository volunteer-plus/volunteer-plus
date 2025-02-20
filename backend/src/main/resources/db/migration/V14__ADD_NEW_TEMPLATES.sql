INSERT INTO email_template(email_message_tag, subject, body) VALUES
    (
        'EMAIL_MESSAGE_TAG_3',
        'Волонтер+: вітаємо в системі [(${name})]!',
        '
           <!DOCTYPE html>
           <html>
           <head>
               <title>Щиро вдячні за реєстрацію на платформу "Волонтер+"!</title>
           </head>
           <body>
               <p>Дякуємо за вашу проактивність на нашій платформі.</p>
               <p>З повагою,<br>Команда Волонтер+</p>
           </body>
           </html>
        '
    );

INSERT INTO email_template(email_message_tag, subject, body) VALUES
    (
        'EMAIL_MESSAGE_TAG_4',
        'Волонтер+: [(${name})], оплата успішно пройшла!',
        '
           <!DOCTYPE html>
           <html>
           <head>
               <title>Щиро вдячні за оплату на платформі "Волонтер+"!</title>
           </head>
           <body>
               <h1>Сума: <span th:text="\${amount}"></span></h1>
               <p>Дякуємо за вашу проактивність на нашій платформі.</p>
               <p>З повагою,<br>Команда Волонтер+</p>
           </body>
           </html>
        '
    );
