pipeline {
  agent {
    docker {
      image 'maven-ssh:v1.0'
      args '-u node -m 1G -v /home/jenkins/.ssh:/home/root/.ssh -v /etc/localtime:/etc/localtime'
    }
  }
  stages {
    stage('ShowDate') {
      steps {
        sh 'date'
      }
    }

    stage('Compile') {
      steps {
        sh 'mvn package'
      }
    }

    stage('Publish') {
      steps {
        sh 'scp target/demo-0.0.1-SNAPSHOT.jar autojenkins@www.greatwebtech.cn:~/'
      }
    }

    stage('Restart') {
      steps {
        sh 'ssh autojenkins@www.greatwebtech.cn "./restartSpringBoot.sh demo-0.0.1-SNAPSHOT.jar"'
      }
    }

  }
}
