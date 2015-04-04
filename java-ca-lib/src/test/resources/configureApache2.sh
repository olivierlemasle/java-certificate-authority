#!/bin/bash

sudo echo "<VirtualHost *:443>
	DocumentRoot /var/www/html/

	SSLEngine on
	SSLCertificateFile /etc/apache2/server.crt
	SSLCertificateKeyFile /etc/apache2/server.key
</VirtualHost>" > /etc/apache2/sites-available/test.conf

sudo cp private.key /etc/apache2/server.key
sudo cp cert.cer /etc/apache2/server.crt
sudo a2ensite test
sudo service apache2 reload
