pipeline {
    agent any

    environment {
        NAME = 'gestionimmeublerapport'
        VERSION = readMavenPom().getVersion()
        NAME_NETWORK = 'cnss_ibr'
        URL_CONFIG_SERVER = 'https://configuration-manager.afrilins.net/config'
        PORT = 8989
        VOLUME = '/data/gestionimmeublerapport'
        CONTEXT_PATH = '/gestionimmeublerapport/'
        EMAIL = 'noreply@afrilins.net'
    }

    options {
      timeout(time: 120, unit: 'MINUTES')
    }

    stages {

        stage('Clean Install') {
            steps {
                sh  "/opt/apache-maven-3.6.3/bin/mvn clean install -DskipTests"
                stash includes: '**/target/*', name: 'target'
            }
        }



        stage('[DEV] BUILD DOCKER') {
            when { branch 'deploy' }
            steps {
                sh  "docker build -t ${NAME}-dev:${VERSION} ."
            }
        }

        stage('[DEV] RUN DOCKER') {
             when { branch 'deploy' }
            steps {
                sh  "docker rm -f ${NAME}-dev"
                sh  "docker run --network ${NAME_NETWORK} --name ${NAME}-dev -v ${VOLUME}:/data --restart=always -e JAVA_OPTS='-Dspring.profiles.active=prod -Dspring.cloud.config.label=develop -Dserver.port=8989'  -p ${PORT}:8989 -d ${NAME}-dev:${VERSION}"
            }
        }

    }


}
