#!/bin/bash

echo "Bem vindo ao instalador Java!!"
echo "Desenvolvido por ReData"

sudo docker version

if [ $? == 0 ];
	then
		echo "O Docker já está INSTALADO!"
		exit
	else
		echo"Docker Shell não NÃO INSTALADO";
		echo "Gostaria de Instalar o Docker [s/n]";

	read testeVar

	if [ \"$testeVar\" == \"s\" ];

	then
		sudo apt-get update
		sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
		curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
		sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
		sudo apt-get update
		sudo apt-get install -y docker-ce docker-ce-cli containerd.io

	else
		exit

	fi
fi

sudo docker version

sudo docker compose version

if [ $? == 0 ];
        then 
                echo "Docker Compose Instalado"
        else 
                echo "O DOCKER COMPOSE não foi INSTALADO AINDA, deseja instala-lo? [s/n]"

        read docker

        if [ \"$docker\" == \"s\" ];
	then
        	sudo apt update
		sudo curl -L "https://github.com/docker/compose/releases/download/v2.17.3/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose 
		sudo chmod +x /usr/local/bin/docker-compose

	fi

fi

sudo docker compose version

if [ $? == 0 ];
        then 
                echo "Instalado"
fi

git clone https://github.com/Sprint-2-Semestre-SpTech-School/Instalacao-e-uso.git

rm redataInstalador.sh
